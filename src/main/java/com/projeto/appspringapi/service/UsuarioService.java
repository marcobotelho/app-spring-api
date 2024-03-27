package com.projeto.appspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.appspringapi.mapper.UsuarioMapper;
import com.projeto.appspringapi.model.UsuarioModel;
import com.projeto.appspringapi.record.AlterarSenhaRecord;
import com.projeto.appspringapi.record.LoginRecord;
import com.projeto.appspringapi.record.UsuarioRecord;
import com.projeto.appspringapi.repository.UsuarioRepository;
import com.projeto.appspringapi.security.JwtTokenUtil;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil jwtService;

	@Autowired
	private PasswordGeneratorService passwordGeneratorService;

	private UsuarioModel getUsuarioModelById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário com id " + id + " não encontrado!"));
	}

	public UsuarioRecord save(UsuarioRecord record, String baseURL) throws MessagingException {
		if (record.id() != null) {
			throw new RuntimeException("Não informar id no cadastro de novos usuários!");
		}

		UsuarioModel usuario = UsuarioMapper.toModel(record);

		String senhaNova = passwordGeneratorService.generateRandomPassword();
		String token = jwtService.gerarToken(usuario.getEmail());

		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuario.setToken(token);
		usuario = usuarioRepository.save(usuario);

		String linkAcesso = baseURL + "/redefinir-senha/" + token;
		String corpoEmail = "<html><body>" +
				"<p>Olá,</p>" +
				"<p>Você foi cadastrado em nosso sistema. Aqui está sua nova senha: <strong>" + senhaNova
				+ "</strong></p>" +
				"<p>Para acessar sua conta, clique no link abaixo:</p>" +
				"<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
				"<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
				"<p>Obrigado.</p>" +
				"</body></html>";
		emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", corpoEmail, true);

		return UsuarioMapper.toRecord(usuario);
	}

	public void delete(UsuarioRecord record) {
		UsuarioModel model = getUsuarioModelById(record.id());
		usuarioRepository.delete(model);
	}

	public void deleteById(Long id) {
		getUsuarioModelById(id);
		usuarioRepository.deleteById(id);
	}

	public UsuarioRecord update(Long id, UsuarioRecord usuarioRecord) {
		UsuarioModel usuarioModel = getUsuarioModelById(id);
		usuarioModel.setNome(usuarioRecord.nome());
		usuarioModel.setEmail(usuarioRecord.email());
		usuarioModel = usuarioRepository.save(usuarioModel);

		return UsuarioMapper.toRecord(usuarioModel);
	}

	public UsuarioRecord getById(Long id) {
		UsuarioModel usuarioModel = getUsuarioModelById(id);
		return UsuarioMapper.toRecord(usuarioModel);
	}

	public List<UsuarioRecord> getAll() {
		List<UsuarioModel> list = usuarioRepository.findAll();
		return UsuarioMapper.toRecordList(list);
	}

	public void resetPassword(String email, String baseURL) throws Exception {
		UsuarioModel usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
				"Usuário com email " + email + " não encontrado!"));
		String senhaNova = passwordGeneratorService.generateRandomPassword();
		String token = jwtService.gerarToken(usuario.getEmail());
		String linkAcesso = baseURL + "/usuario/alterar-senha";
		usuario.setSenha(passwordEncoder.encode(senhaNova));
		usuario.setToken(token);
		usuarioRepository.save(usuario);
		String corpoEmail = "<html><body>" +
				"<p>Olá,</p>" +
				"<p>Você solicitou uma recuperação de senha. Aqui está sua nova senha: <strong>" + senhaNova
				+ "</strong></p>" +
				"<p>Para acessar api, acesse o endpoint abaixo com PUT e informe senhaNova e senhaNovaConfirmacao:</p>"
				+
				"<p>" + linkAcesso + "</p>" +
				"<p>" +
				"{<br>" +
				"\"token\": \"" + token + "\",<br>" +
				"\"senhaAtual\": \"" + senhaNova + "\",<br>" +
				"\"senhaNova\": \"\",<br>" +
				"\"senhaNovaConfirmacao\": \"\"<br>" +
				"}<br>" +
				"</p>" +
				// "<p><a href=\"" + linkAcesso + "\">Acessar Sua Conta</a></p>" +
				"<p>Se não foi você quem solicitou essa recuperação de senha, ignore este e-mail.</p>" +
				"<p>Obrigado.</p>" +
				"</body></html>";
		emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha", corpoEmail, true);
	}

	public void alterarSenha(AlterarSenhaRecord alterarSenhaRecord) {
		String usuarioEmail = jwtService.extrairUsuarioEmail(alterarSenhaRecord.token());
		UsuarioModel usuario = usuarioRepository.findByEmail(usuarioEmail).get();
		if (!alterarSenhaRecord.senhaNova().equals(alterarSenhaRecord.senhaNovaConfirmacao())) {
			throw new RuntimeException("As senhas novas informadas sao diferentes!");
		}
		if (!alterarSenhaRecord.token().equals(usuario.getToken())) {
			throw new RuntimeException("Recuperação de senha desatualizada! Gere nova recuperação de senha.");
		}
		if (!passwordEncoder.matches(alterarSenhaRecord.senhaAtual(), usuario.getSenha())) {
			throw new RuntimeException("Senha atual incorreta!");
		}
		usuario.setSenha(passwordEncoder.encode(alterarSenhaRecord.senhaNova()));

		usuario.setToken(null);
		usuarioRepository.save(usuario);
	}

	public String getToken(@Valid LoginRecord loginRecord) {
		UsuarioModel usuario = usuarioRepository.findByEmail(loginRecord.email()).orElseThrow(() -> {
			throw new RuntimeException("Email ou senha inválidos!");
		});

		if (!passwordEncoder.matches(loginRecord.senha(), usuario.getSenha())) {
			throw new RuntimeException("Email ou senha inválidos!");
		}

		String token = jwtService.gerarToken(loginRecord.email());
		return token;
	}

	public List<UsuarioRecord> getAllByPerfilId(Long perfilId) {
		return UsuarioMapper.toRecordList(usuarioRepository.findAllByPerfisId(perfilId));
	}
}
