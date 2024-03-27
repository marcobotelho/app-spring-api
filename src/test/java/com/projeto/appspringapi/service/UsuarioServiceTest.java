package com.projeto.appspringapi.service;

import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeto.appspringapi.mapper.UsuarioMapper;
import com.projeto.appspringapi.model.PerfilModel;
import com.projeto.appspringapi.model.UsuarioModel;
import com.projeto.appspringapi.record.AlterarSenhaRecord;
import com.projeto.appspringapi.record.LoginRecord;
import com.projeto.appspringapi.record.UsuarioRecord;
import com.projeto.appspringapi.repository.UsuarioRepository;
import com.projeto.appspringapi.security.JwtTokenUtil;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtTokenUtil jwtService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private UsuarioService usuarioService;

    private Long usuarioId;

    private Long perfilId;

    private UsuarioModel usuarioModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = 1L;
        perfilId = 1L;
        usuarioModel = new UsuarioModel(usuarioId, "Usuario Teste", "teste@teste.com");
        usuarioModel.setToken(
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJjb2JvdGVsaG9hYnJldUBnbWFpbC5jb20iLCJpYXQiOjE3MTAyNTE4NzEsImV4cCI6MTcxMDMzODI3MX0.ohxslRWPxemYQP26GVLbBA1IJGCp8EJ-6PvOruYPRHw");
        usuarioModel.setPerfis(List.of(new PerfilModel(perfilId, "ROLE_USER", "Perfil Usuario")));
    }

    @Test
    @DisplayName("Alterar senha do usuário")
    void testAlterarSenhaSuccess() {
        AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord(usuarioModel.getToken(),
                "123456", "654321", "654321");
        Mockito.when(jwtService.extrairUsuarioEmail(alterarSenhaRecord.token())).thenReturn(usuarioModel.getEmail());
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(alterarSenhaRecord.senhaAtual(), usuarioModel.getSenha()))
                .thenReturn(true);

        usuarioService.alterarSenha(alterarSenhaRecord);

        Mockito.verify(jwtService, Mockito.times(1)).extrairUsuarioEmail(alterarSenhaRecord.token());
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(alterarSenhaRecord.senhaAtual(),
                usuarioModel.getSenha());
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Erro ao alterar senha do usuário - senhas novas nao conferem")
    void testAlterarSenhaErrorSenhasNovasNaoConferem() {
        AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord(usuarioModel.getToken(),
                "123456", "654321", "999999");
        Mockito.when(jwtService.extrairUsuarioEmail(alterarSenhaRecord.token())).thenReturn(usuarioModel.getEmail());
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(alterarSenhaRecord.senhaAtual(), usuarioModel.getSenha()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.alterarSenha(alterarSenhaRecord);
        });

        Mockito.verify(jwtService, Mockito.times(1)).extrairUsuarioEmail(alterarSenhaRecord.token());
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(0)).matches(alterarSenhaRecord.senhaAtual(),
                usuarioModel.getSenha());
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals("As senhas novas informadas sao diferentes!", exception.getMessage());
    }

    @Test
    @DisplayName("Erro ao alterar senha do usuário - token nao confere")
    void testAlterarSenhaErrorTokenNaoConfere() {
        AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord(anyString(),
                "123456", "654321", "654321");
        Mockito.when(jwtService.extrairUsuarioEmail(alterarSenhaRecord.token())).thenReturn(usuarioModel.getEmail());
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(alterarSenhaRecord.senhaAtual(), usuarioModel.getSenha()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.alterarSenha(alterarSenhaRecord);
        });

        Mockito.verify(jwtService, Mockito.times(1)).extrairUsuarioEmail(alterarSenhaRecord.token());
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(0)).matches(alterarSenhaRecord.senhaAtual(),
                usuarioModel.getSenha());
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals("Recuperação de senha desatualizada! Gere nova recuperação de senha.",
                exception.getMessage());
    }

    @Test
    @DisplayName("Erro ao alterar senha do usuário - senhas atual nao confere")
    void testAlterarSenhaErrorSenhaAtualNaoConfere() {
        AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord(usuarioModel.getToken(),
                "999999", "654321", "654321");
        Mockito.when(jwtService.extrairUsuarioEmail(alterarSenhaRecord.token())).thenReturn(usuarioModel.getEmail());
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(alterarSenhaRecord.senhaAtual(), usuarioModel.getSenha()))
                .thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.alterarSenha(alterarSenhaRecord);
        });

        Mockito.verify(jwtService, Mockito.times(1)).extrairUsuarioEmail(alterarSenhaRecord.token());
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(alterarSenhaRecord.senhaAtual(),
                usuarioModel.getSenha());
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals("Senha atual incorreta!", exception.getMessage());
    }

    @Test
    @DisplayName("Deleta um usuário")
    void testDeleteSuccess() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioModel));

        usuarioService.delete(UsuarioMapper.toRecord(usuarioModel));

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(1)).delete(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Erro ao deletar um usuário inexistente")
    void testDeleteErrorNotFound() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.delete(UsuarioMapper.toRecord(usuarioModel));
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(0)).delete(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);

        Assertions.assertEquals("Usuário com id " + usuarioId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Deleta um usuário por id")
    void testDeleteByIdSuccess() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioModel));

        usuarioService.deleteById(usuarioId);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(1)).deleteById(usuarioId);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    @DisplayName("Erro ao deletar um usuário id inexistente")
    void testDeleteByIdErrorNotFound() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.deleteById(usuarioId);
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(0)).delete(usuarioModel);
        Mockito.verifyNoMoreInteractions(usuarioRepository);

        Assertions.assertEquals("Usuário com id " + usuarioId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Listar todos os usuários")
    void testGetAll() {
        List<UsuarioModel> usuarioModels = List.of(usuarioModel);
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarioModels);

        List<UsuarioRecord> results = usuarioService.getAll();

        Mockito.verify(usuarioRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(UsuarioMapper.toRecordList(usuarioModels), results);
    }

    @Test
    @DisplayName("Listar todos os usuários por perfil id")
    void testGetAllByPerfilIdExists() {
        Mockito.when(usuarioRepository.findAllByPerfisId(perfilId)).thenReturn(List.of(usuarioModel));

        List<UsuarioRecord> results = usuarioService.getAllByPerfilId(perfilId);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findAllByPerfisId(perfilId);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(UsuarioMapper.toRecordList(List.of(usuarioModel)), results);
    }

    @Test
    @DisplayName("Listar todos os usuários por perfil id inexistente")
    void testGetAllByPerfilIdNotExists() {
        Mockito.when(usuarioRepository.findAllByPerfisId(perfilId)).thenReturn(Collections.emptyList());

        List<UsuarioRecord> results = usuarioService.getAllByPerfilId(perfilId);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findAllByPerfisId(perfilId);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Pegar um usuário por id")
    void testGetByIdSuccess() {
        Mockito.when(usuarioRepository.findById(perfilId)).thenReturn(Optional.of(usuarioModel));

        UsuarioRecord result = usuarioService.getById(perfilId);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals(UsuarioMapper.toRecord(usuarioModel), result);
    }

    @Test
    @DisplayName("Erro ao pegar um usuário por id inexistente")
    void testGetByIdErrorNotFound() {
        Mockito.when(usuarioRepository.findById(perfilId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.getById(perfilId);
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Assertions.assertEquals("Usuário com id " + usuarioId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Retorna um token")
    void testGetToken() {
        LoginRecord loginRecord = new LoginRecord(usuarioModel.getEmail(), "123456");
        Mockito.when(usuarioRepository.findByEmail(loginRecord.email())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(loginRecord.senha(), usuarioModel.getSenha())).thenReturn(true);
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());

        String result = usuarioService.getToken(loginRecord);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(loginRecord.email());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(loginRecord.senha(), usuarioModel.getSenha());
        Mockito.verify(jwtService, Mockito.times(1)).gerarToken(loginRecord.email());
        Mockito.verifyNoMoreInteractions(jwtService);
        Assertions.assertEquals(result, usuarioModel.getToken());
    }

    @Test
    @DisplayName("Erro ao retornar um token - email inexistente")
    void testGetTokenErrorEmail() {
        LoginRecord loginRecord = new LoginRecord(usuarioModel.getEmail(), "123456");
        Mockito.when(usuarioRepository.findByEmail(loginRecord.email())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(loginRecord.senha(), usuarioModel.getSenha())).thenReturn(true);
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.getToken(loginRecord);
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(loginRecord.email());
        Mockito.verify(passwordEncoder, Mockito.times(0)).matches(loginRecord.senha(), usuarioModel.getSenha());
        Mockito.verify(jwtService, Mockito.times(0)).gerarToken(loginRecord.email());
        Mockito.verifyNoMoreInteractions(jwtService);
        Assertions.assertEquals("Email ou senha inválidos!", exception.getMessage());
    }

    @Test
    @DisplayName("Erro ao retornar um token - senha inválida")
    void testGetTokenErrorSenha() {
        LoginRecord loginRecord = new LoginRecord(usuarioModel.getEmail(), "123456");
        Mockito.when(usuarioRepository.findByEmail(loginRecord.email())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordEncoder.matches(loginRecord.senha(), usuarioModel.getSenha())).thenReturn(false);
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.getToken(loginRecord);
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(loginRecord.email());
        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(loginRecord.senha(), usuarioModel.getSenha());
        Mockito.verify(jwtService, Mockito.times(0)).gerarToken(loginRecord.email());
        Mockito.verifyNoMoreInteractions(jwtService);
        Assertions.assertEquals("Email ou senha inválidos!", exception.getMessage());
    }

    @Test
    @DisplayName("Resetar senha")
    void testResetPassword() throws Exception {
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.of(usuarioModel));
        Mockito.when(passwordGeneratorService.generateRandomPassword()).thenReturn("999999");
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());
        Mockito.when(passwordEncoder.encode("999999")).thenReturn(usuarioModel.getSenha());

        String baseURL = "https://localhost:8080";
        String linkAcesso = baseURL + "/usuario/alterar-senha";
        String corpoEmail = "<html><body>" +
                "<p>Olá,</p>" +
                "<p>Você solicitou uma recuperação de senha. Aqui está sua nova senha: <strong>"
                + "999999"
                + "</strong></p>" +
                "<p>Para acessar api, acesse o endpoint abaixo com PUT e informe senhaNova e senhaNovaConfirmacao:</p>"
                +
                "<p>" + linkAcesso + "</p>" +
                "<p>" +
                "{<br>" +
                "\"token\": \"" + usuarioModel.getToken() + "\",<br>" +
                "\"senhaAtual\": \"" + "999999" + "\",<br>" +
                "\"senhaNova\": \"\",<br>" +
                "\"senhaNovaConfirmacao\": \"\"<br>" +
                "}<br>" +
                "</p>" +
                // "<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
                "<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
                "<p>Obrigado.</p>" +
                "</body></html>";

        usuarioService.resetPassword(usuarioModel.getEmail(), baseURL);

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordGeneratorService, Mockito.times(1)).generateRandomPassword();
        Mockito.verify(jwtService, Mockito.times(1)).gerarToken(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("999999");
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioModel);
        Mockito.verify(emailService, Mockito.times(1)).enviarEmail(usuarioModel.getEmail(), "Recuperação de Senha",
                corpoEmail, true);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Mockito.verifyNoMoreInteractions(passwordGeneratorService);
        Mockito.verifyNoMoreInteractions(jwtService);
        Mockito.verifyNoMoreInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(emailService);
    }

    @Test
    @DisplayName("Erro ao resetar senha - email inexistente")
    void testResetPasswordErrorEmailNotFound() throws MessagingException {
        Mockito.when(usuarioRepository.findByEmail(usuarioModel.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordGeneratorService.generateRandomPassword()).thenReturn("999999");
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());
        Mockito.when(passwordEncoder.encode("999999")).thenReturn(usuarioModel.getSenha());

        String baseURL = "https://localhost:8080";
        String linkAcesso = baseURL + "/usuario/alterar-senha";
        String corpoEmail = "<html><body>" +
                "<p>Olá,</p>" +
                "<p>Você solicitou uma recuperação de senha. Aqui está sua nova senha: <strong>"
                + "999999"
                + "</strong></p>" +
                "<p>Para acessar api, acesse o endpoint abaixo com PUT e informe senhaNova e senhaNovaConfirmacao:</p>"
                +
                "<p>" + linkAcesso + "</p>" +
                "<p>" +
                "{<br>" +
                "\"token\": \"" + usuarioModel.getToken() + "\",<br>" +
                "\"senhaAtual\": \"" + "999999" + "\",<br>" +
                "\"senhaNova\": \"\",<br>" +
                "\"senhaNovaConfirmacao\": \"\"<br>" +
                "}<br>" +
                "</p>" +
                // "<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
                "<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
                "<p>Obrigado.</p>" +
                "</body></html>";

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.resetPassword(usuarioModel.getEmail(), baseURL);
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(usuarioModel.getEmail());
        Mockito.verify(passwordGeneratorService, Mockito.times(0)).generateRandomPassword();
        Mockito.verify(jwtService, Mockito.times(0)).gerarToken(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode("999999");
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(usuarioModel);
        Mockito.verify(emailService, Mockito.times(0)).enviarEmail(usuarioModel.getEmail(), "Recuperação de Senha",
                corpoEmail, true);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Mockito.verifyNoMoreInteractions(passwordGeneratorService);
        Mockito.verifyNoMoreInteractions(jwtService);
        Mockito.verifyNoMoreInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(emailService);

        Assertions.assertEquals("Usuário com email " + usuarioModel.getEmail() + " não encontrado!",
                exception.getMessage());
    }

    @Test
    @DisplayName("Salvar usuário")
    void testSaveSuccess() throws MessagingException {
        usuarioModel.setId(null);

        Mockito.when(passwordGeneratorService.generateRandomPassword()).thenReturn("999999");
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());
        Mockito.when(passwordEncoder.encode("999999")).thenReturn(usuarioModel.getSenha());
        Mockito.when(usuarioRepository.save(usuarioModel)).thenReturn(usuarioModel);

        String baseURL = "https://localhost:8080";
        String linkAcesso = baseURL + "/redefinir-senha/" + usuarioModel.getToken();
        String corpoEmail = "<html><body>" +
                "<p>Olá,</p>" +
                "<p>Você foi cadastrado em nosso sistema. Aqui está sua nova senha: <strong>" + "999999"
                + "</strong></p>" +
                "<p>Para acessar sua conta, clique no link abaixo:</p>" +
                "<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
                "<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
                "<p>Obrigado.</p>" +
                "</body></html>";

        UsuarioRecord recordResult = usuarioService.save(UsuarioMapper.toRecord(usuarioModel), baseURL);

        Mockito.verify(passwordGeneratorService, Mockito.times(1)).generateRandomPassword();
        Mockito.verify(jwtService, Mockito.times(1)).gerarToken(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("999999");
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioModel);
        Mockito.verify(emailService, Mockito.times(1)).enviarEmail(usuarioModel.getEmail(), "Recuperação de Senha",
                corpoEmail, true);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Mockito.verifyNoMoreInteractions(passwordGeneratorService);
        Mockito.verifyNoMoreInteractions(jwtService);
        Mockito.verifyNoMoreInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(emailService);

        Assertions.assertNotNull(recordResult);
    }

    @Test
    @DisplayName("Salvar usuário com id não nulo")
    void testSaveErrorId() throws MessagingException {
        Mockito.when(passwordGeneratorService.generateRandomPassword()).thenReturn("999999");
        Mockito.when(jwtService.gerarToken(usuarioModel.getEmail())).thenReturn(usuarioModel.getToken());
        Mockito.when(passwordEncoder.encode("999999")).thenReturn(usuarioModel.getSenha());
        Mockito.when(usuarioRepository.save(usuarioModel)).thenReturn(usuarioModel);

        String baseURL = "https://localhost:8080";
        String linkAcesso = baseURL + "/redefinir-senha/" + usuarioModel.getToken();
        String corpoEmail = "<html><body>" +
                "<p>Olá,</p>" +
                "<p>Você foi cadastrado em nosso sistema. Aqui está sua nova senha: <strong>" + "999999"
                + "</strong></p>" +
                "<p>Para acessar sua conta, clique no link abaixo:</p>" +
                "<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
                "<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
                "<p>Obrigado.</p>" +
                "</body></html>";

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.save(UsuarioMapper.toRecord(usuarioModel), baseURL);
        });

        Mockito.verify(passwordGeneratorService, Mockito.times(0)).generateRandomPassword();
        Mockito.verify(jwtService, Mockito.times(0)).gerarToken(usuarioModel.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode("999999");
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(usuarioModel);
        Mockito.verify(emailService, Mockito.times(0)).enviarEmail(usuarioModel.getEmail(), "Recuperação de Senha",
                corpoEmail, true);
        Mockito.verifyNoMoreInteractions(usuarioRepository);
        Mockito.verifyNoMoreInteractions(passwordGeneratorService);
        Mockito.verifyNoMoreInteractions(jwtService);
        Mockito.verifyNoMoreInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(emailService);

        Assertions.assertEquals("Não informar id no cadastro de novos usuários!", exception.getMessage());
    }

    @Test
    @DisplayName("Alterar usuário")
    void testUpdateSuccess() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioModel));
        Mockito.when(usuarioRepository.save(Mockito.any(UsuarioModel.class))).thenReturn(usuarioModel);

        UsuarioRecord recordResult = usuarioService.update(usuarioId, UsuarioMapper.toRecord(usuarioModel));

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(Mockito.any(UsuarioModel.class));
        Mockito.verifyNoMoreInteractions(usuarioRepository);

        Assertions.assertNotNull(recordResult);
        Assertions.assertEquals(UsuarioMapper.toRecord(usuarioModel), recordResult);
    }

    @Test
    @DisplayName("Erro ao alterar usuário com id não encontrado")
    void testUpdateErrorNotFound() {
        Mockito.when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        Mockito.when(usuarioRepository.save(Mockito.any(UsuarioModel.class))).thenReturn(usuarioModel);

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.update(usuarioId, UsuarioMapper.toRecord(usuarioModel));
        });

        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuarioId);
        Mockito.verify(usuarioRepository, Mockito.times(0)).save(Mockito.any(UsuarioModel.class));
        Mockito.verifyNoMoreInteractions(usuarioRepository);

        Assertions.assertEquals("Usuário com id " + usuarioId + " não encontrado!", exception.getMessage());
    }
}
