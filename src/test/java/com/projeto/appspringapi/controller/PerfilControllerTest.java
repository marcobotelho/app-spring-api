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
import com.projeto.appspringapi.record.PerfilRecord;
import com.projeto.appspringapi.service.PerfilService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class PerfilControllerTest {

        @MockBean
        private PerfilService perfilService;

        @Autowired
        private MockMvc mockMvc;

        private Long perfilId;

        private Long usuarioId;

        private PerfilRecord perfilRecord;

        private String perfilJson;

        private String url;

        @Autowired
        private ObjectMapper mapper;

        @BeforeEach
        void setUp() throws JsonProcessingException {
                url = "/perfis";
                perfilId = 1L;
                usuarioId = 1L;
                perfilRecord = new PerfilRecord(perfilId, "ROLE_USER", "Perfil Usuário");
                perfilJson = mapper.writeValueAsString(perfilRecord);
        }

        @Test
        void testDeleteSuccess() throws Exception {
                String path = url.concat("/{id}");
                String message = "Perfil excluído com sucesso!";

                Mockito.doNothing().when(perfilService).deleteById(perfilId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, perfilId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(perfilService, Mockito.times(1)).deleteById(perfilId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testDeleteErrorNotFound() throws Exception {
                String path = url.concat("/{id}");
                String message = "Perfil com id " + perfilId + " não encontrado!";

                Mockito.doThrow(new EntityNotFoundException(message)).when(perfilService).deleteById(perfilId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, perfilId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(perfilService, Mockito.times(1)).deleteById(perfilId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testGetAllSuccess() throws JsonProcessingException, Exception {
                Mockito.when(perfilService.getAll()).thenReturn(List.of(perfilRecord));

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(perfilRecord))));

                Mockito.verify(perfilService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testGetAllErrorEmpty() throws JsonProcessingException, Exception {
                Mockito.when(perfilService.getAll()).thenReturn(Collections.emptyList());

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(perfilService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testGetAllByClienteIdSuccess() throws JsonProcessingException, Exception {
                Mockito.when(perfilService.getAllByUsuarioId(usuarioId)).thenReturn(List.of(perfilRecord));

                url = url.concat("/usuario/{id}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, usuarioId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(perfilRecord))));

                Mockito.verify(perfilService, Mockito.times(1)).getAllByUsuarioId(usuarioId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testGetAllByClienteIdEmpty() throws JsonProcessingException, Exception {
                Mockito.when(perfilService.getAllByUsuarioId(usuarioId)).thenReturn(Collections.emptyList());

                url = url.concat("/usuario/{id}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, usuarioId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(perfilService, Mockito.times(1)).getAllByUsuarioId(usuarioId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        public void testGetByIdSuccess() throws Exception {
                Mockito.when(perfilService.getById(perfilId)).thenReturn(perfilRecord);

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), perfilId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(perfilRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value(perfilRecord.descricao().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(perfilJson));

                Mockito.verify(perfilService, Mockito.times(1)).getById(perfilId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        public void testGetByIdErrorNotFound() throws Exception {
                Mockito.when(perfilService.getById(perfilId))
                                .thenThrow(new EntityNotFoundException(
                                                "Perfil com id " + perfilId + " não encontrado!"));

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), perfilId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Perfil com id " + perfilId + " não encontrado!"))
                                .andDo(print());

                Mockito.verify(perfilService, Mockito.times(1)).getById(perfilId);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testSaveSuccess() throws Exception {
                perfilRecord = new PerfilRecord(null, "ROLE_USER", "Perfil Usuário");
                perfilJson = mapper.writeValueAsString(perfilRecord);

                Mockito.when(perfilService.save(perfilRecord)).thenReturn(perfilRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(perfilJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(perfilRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value(perfilRecord.descricao().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(perfilJson));

                Mockito.verify(perfilService, Mockito.times(1)).save(perfilRecord);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testSaveErrorId() throws Exception {
                String message = "Id deve ser nulo para incluir um novo perfil!";

                Mockito.doThrow(new IllegalArgumentException(message)).when(perfilService).save(perfilRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(perfilJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(perfilService, Mockito.times(1)).save(perfilRecord);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testUpdateSuccess() throws Exception {
                Mockito.when(perfilService.update(perfilId, perfilRecord)).thenReturn(perfilRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), perfilId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(perfilJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(perfilRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao")
                                                .value(perfilRecord.descricao().toString()))
                                .andExpect(MockMvcResultMatchers.content().json(perfilJson));

                Mockito.verify(perfilService, Mockito.times(1)).update(perfilId, perfilRecord);
                Mockito.verifyNoMoreInteractions(perfilService);
        }

        @Test
        void testUpdateErrorNotFound() throws Exception {
                String message = "Perfil com id " + perfilId + " não encontrado!";

                Mockito.when(perfilService.update(perfilId, perfilRecord))
                                .thenThrow(new EntityNotFoundException(message));

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), perfilId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(perfilJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(perfilService, Mockito.times(1)).update(perfilId, perfilRecord);
                Mockito.verifyNoMoreInteractions(perfilService);
        }
}
