package com.projeto.appspringapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.appspringapi.record.AlterarSenhaRecord;
import com.projeto.appspringapi.record.UsuarioRecord;
import com.projeto.appspringapi.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UsuarioControllerTest {

        @MockBean
        private UsuarioService usuarioService;

        @Autowired
        private MockMvc mockMvc;

        private Long usuarioId;

        private UsuarioRecord usuarioRecord;

        private String usuarioJson;

        private String url;

        @Autowired
        private ObjectMapper mapper;

        @BeforeEach
        void setUp() throws JsonProcessingException {
                url = "/usuarios";
                usuarioId = 1L;
                usuarioRecord = new UsuarioRecord(usuarioId, "Ana Silva", "ana@teste.com");
                usuarioJson = mapper.writeValueAsString(usuarioRecord);
        }

        @Test
        void testDeleteSuccess() throws Exception {
                String path = url.concat("/{id}");
                String message = "Usuario excluído com sucesso!";

                Mockito.doNothing().when(usuarioService).deleteById(usuarioId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, usuarioId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).deleteById(usuarioId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testDeleteErrorNotFound() throws Exception {
                String path = url.concat("/{id}");
                String message = "Usuario com id " + usuarioId + " não encontrado!";

                Mockito.doThrow(new EntityNotFoundException(message)).when(usuarioService).deleteById(usuarioId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, usuarioId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).deleteById(usuarioId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testGetAllSuccess() throws JsonProcessingException, Exception {
                Mockito.when(usuarioService.getAll()).thenReturn(List.of(usuarioRecord));

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(usuarioRecord))));

                Mockito.verify(usuarioService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testGetAllEmpty() throws JsonProcessingException, Exception {
                Mockito.when(usuarioService.getAll()).thenReturn(Collections.emptyList());

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(usuarioService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testgetAllByPerfilIdSuccess() throws JsonProcessingException, Exception {
                Long perfilId = 1L;
                Mockito.when(usuarioService.getAllByPerfilId(perfilId)).thenReturn(List.of(usuarioRecord));

                url = url.concat("/perfil/{perfilId}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, perfilId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(usuarioRecord))));

                Mockito.verify(usuarioService, Mockito.times(1)).getAllByPerfilId(perfilId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testgetAllByPerfilIdEmpty() throws JsonProcessingException, Exception {
                Long perfilId = 1L;
                Mockito.when(usuarioService.getAllByPerfilId(perfilId)).thenReturn(Collections.emptyList());

                url = url.concat("/perfil/{perfilId}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, perfilId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(usuarioService, Mockito.times(1)).getAllByPerfilId(perfilId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        public void testGetByIdSuccess() throws Exception {
                Mockito.when(usuarioService.getById(usuarioId)).thenReturn(usuarioRecord);

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(usuarioRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(usuarioJson));

                Mockito.verify(usuarioService, Mockito.times(1)).getById(usuarioId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        public void testGetByIdErrorNotFound() throws Exception {
                Mockito.when(usuarioService.getById(usuarioId))
                                .thenThrow(new EntityNotFoundException(
                                                "Usuario com id " + usuarioId + " não encontrado!"));

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Usuario com id " + usuarioId + " não encontrado!"))
                                .andDo(print());

                Mockito.verify(usuarioService, Mockito.times(1)).getById(usuarioId);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testSaveSuccess() throws Exception {
                usuarioRecord = new UsuarioRecord(null, "Ana Silva", "ana@teste.com");
                usuarioJson = mapper.writeValueAsString(usuarioRecord);
                String baseUrl = "http://localhost";

                Mockito.when(usuarioService.save(usuarioRecord, baseUrl)).thenReturn(usuarioRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .servletPath(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(usuarioRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(usuarioJson));

                Mockito.verify(usuarioService, Mockito.times(1)).save(usuarioRecord, baseUrl);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testSaveErrorId() throws Exception {
                String message = "Id deve ser nulo para incluir um novo usuario!";
                String baseUrl = "http://localhost";

                Mockito.doThrow(new IllegalArgumentException(message)).when(usuarioService).save(usuarioRecord,
                                baseUrl);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .servletPath(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).save(usuarioRecord, baseUrl);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testUpdateSuccess() throws Exception {
                Mockito.when(usuarioService.update(usuarioId, usuarioRecord)).thenReturn(usuarioRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(usuarioRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(usuarioJson));

                Mockito.verify(usuarioService, Mockito.times(1)).update(usuarioId, usuarioRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testUpdateErrorNotFound() throws Exception {
                String message = "Usuario com id " + usuarioId + " não encontrado!";

                Mockito.when(usuarioService.update(usuarioId, usuarioRecord))
                                .thenThrow(new EntityNotFoundException(message));

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).update(usuarioId, usuarioRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testResetSenhaSuccess() throws Exception {
                String email = usuarioRecord.email().toString();
                url = url.concat("/reset-senha/{email}");
                String servletPath = url.replace("{email}", email);
                String baseUrl = "http://localhost";
                String message = "E-mail com instrução de redefinição de senha enviado!";

                Mockito.doNothing().when(usuarioService).resetPassword(email, baseUrl);

                mockMvc.perform(MockMvcRequestBuilders.get(url, email)
                                .servletPath(servletPath)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).resetPassword(email, baseUrl);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testResetSenhaErrorGeneric() throws Exception {
                String email = usuarioRecord.email().toString();
                url = url.concat("/reset-senha/{email}");
                String baseUrl = "http://localhost";
                String message = "Erro ao resetar senha!";

                Mockito.doThrow(new Exception(message)).when(usuarioService).resetPassword(email, baseUrl);

                mockMvc.perform(MockMvcRequestBuilders.get(url, email)
                                .servletPath("/usuarios/reset-senha/".concat(email))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).resetPassword(email, baseUrl);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testAlterarSenhaSuccess() throws Exception {
                AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord("dfsffdfsfsdfdsf", "123456", "654321",
                                "654321");
                String alterarSenhaJson = mapper.writeValueAsString(alterarSenhaRecord);
                url = url.concat("/alterar-senha");
                String message = "Senha alterada com sucesso!";

                Mockito.doNothing().when(usuarioService).alterarSenha(alterarSenhaRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(alterarSenhaJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).alterarSenha(alterarSenhaRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testAlterarSenhaErrorNewPasswordNotEquals() throws Exception {
                AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord("dfsffdfsfsdfdsf", "123456", "654321",
                                "999999");
                String alterarSenhaJson = mapper.writeValueAsString(alterarSenhaRecord);
                url = url.concat("/alterar-senha");
                String message = "As senhas novas informadas sao diferentes!";

                Mockito.doThrow(new RuntimeException(message)).when(usuarioService).alterarSenha(alterarSenhaRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(alterarSenhaJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).alterarSenha(alterarSenhaRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testAlterarSenhaErrorTokenOutOfDate() throws Exception {
                AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord("dfsffdfsfsdfdsf", "123456", "654321",
                                "999999");
                String alterarSenhaJson = mapper.writeValueAsString(alterarSenhaRecord);
                url = url.concat("/alterar-senha");
                String message = "Recuperação de senha desatualizada! Gere nova recuperação de senha.";

                Mockito.doThrow(new RuntimeException(message)).when(usuarioService).alterarSenha(alterarSenhaRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(alterarSenhaJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).alterarSenha(alterarSenhaRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

        @Test
        void testAlterarSenhaErrorWrongPassword() throws Exception {
                AlterarSenhaRecord alterarSenhaRecord = new AlterarSenhaRecord("dfsffdfsfsdfdsf", "123456", "654321",
                                "999999");
                String alterarSenhaJson = mapper.writeValueAsString(alterarSenhaRecord);
                url = url.concat("/alterar-senha");
                String message = "Senha atual incorreta!";

                Mockito.doThrow(new RuntimeException(message)).when(usuarioService).alterarSenha(alterarSenhaRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(alterarSenhaJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(usuarioService, Mockito.times(1)).alterarSenha(alterarSenhaRecord);
                Mockito.verifyNoMoreInteractions(usuarioService);
        }

}
