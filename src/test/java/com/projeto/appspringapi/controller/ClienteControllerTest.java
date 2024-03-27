package com.projeto.appspringapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
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
import com.projeto.appspringapi.record.ClienteRecord;
import com.projeto.appspringapi.service.ClienteService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ClienteControllerTest {

        @MockBean
        private ClienteService clienteService;

        @Autowired
        private MockMvc mockMvc;

        private Long clienteId;

        private ClienteRecord clienteRecord;

        private String clienteJson;

        private String url;

        @Autowired
        private ObjectMapper mapper;

        @BeforeEach
        void setUp() throws JsonProcessingException {
                url = "/clientes";
                clienteId = 1L;
                clienteRecord = new ClienteRecord(clienteId, "Nome do cliente", "email@teste.com", 15,
                                LocalDate.of(1998, 1, 1),
                                "00000-000", "Endereço", "Bairro", "Cidade", "SP");
                clienteJson = mapper.writeValueAsString(clienteRecord);
        }

        @Test
        void testDeleteSuccess() throws Exception {
                String path = url.concat("/{id}");
                String message = "Cliente excluído com sucesso!";

                Mockito.doNothing().when(clienteService).deleteById(clienteId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, clienteId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(clienteService, Mockito.times(1)).deleteById(clienteId);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testDeleteErrorNotFound() throws Exception {
                String path = url.concat("/{id}");
                String message = "Cliente com id " + clienteId + " não encontrado!";

                Mockito.doThrow(new EntityNotFoundException(message)).when(clienteService).deleteById(clienteId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, clienteId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(clienteService, Mockito.times(1)).deleteById(clienteId);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testGetAllSuccess() throws JsonProcessingException, Exception {
                Mockito.when(clienteService.getAll()).thenReturn(List.of(clienteRecord));

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(clienteRecord))));

                Mockito.verify(clienteService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testGetAllErrorEmpty() throws JsonProcessingException, Exception {
                Mockito.when(clienteService.getAll()).thenReturn(Collections.emptyList());

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(clienteService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        public void testGetByIdSuccess() throws Exception {
                Mockito.when(clienteService.getById(clienteId)).thenReturn(clienteRecord);

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), clienteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(clienteRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(clienteRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.idade")
                                                .value(clienteRecord.idade()))
                                .andExpect(MockMvcResultMatchers.content().json(clienteJson));

                Mockito.verify(clienteService, Mockito.times(1)).getById(clienteId);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        public void testGetByIdErrorNotFound() throws Exception {
                Mockito.when(clienteService.getById(clienteId))
                                .thenThrow(new EntityNotFoundException(
                                                "Cliente com id " + clienteId + " não encontrado!"));

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), clienteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Cliente com id " + clienteId + " não encontrado!"))
                                .andDo(print());

                Mockito.verify(clienteService, Mockito.times(1)).getById(clienteId);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testSaveSuccess() throws Exception {
                clienteRecord = new ClienteRecord(null, "Nome do cliente", "email@teste.com", 15,
                                LocalDate.of(1998, 1, 1),
                                "00000-000", "Endereço", "Bairro", "Cidade", "SP");
                clienteJson = mapper.writeValueAsString(clienteRecord);

                Mockito.when(clienteService.save(clienteRecord)).thenReturn(clienteRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(clienteRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(clienteRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.idade")
                                                .value(clienteRecord.idade()))
                                .andExpect(MockMvcResultMatchers.content().json(clienteJson));

                Mockito.verify(clienteService, Mockito.times(1)).save(clienteRecord);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testSaveErrorId() throws Exception {
                String message = "Id deve ser nulo para incluir um novo cliente!";

                Mockito.doThrow(new IllegalArgumentException(message)).when(clienteService).save(clienteRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(clienteService, Mockito.times(1)).save(clienteRecord);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testUpdateSuccess() throws Exception {
                Mockito.when(clienteService.update(clienteId, clienteRecord)).thenReturn(clienteRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), clienteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(clienteRecord.nome()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                                                .value(clienteRecord.email().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.idade")
                                                .value(clienteRecord.idade()))
                                .andExpect(MockMvcResultMatchers.content().json(clienteJson));

                Mockito.verify(clienteService, Mockito.times(1)).update(clienteId, clienteRecord);
                Mockito.verifyNoMoreInteractions(clienteService);
        }

        @Test
        void testUpdateErrorNotFound() throws Exception {
                String message = "Cliente com id " + clienteId + " não encontrado!";

                Mockito.when(clienteService.update(clienteId, clienteRecord))
                                .thenThrow(new EntityNotFoundException(message));

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), clienteId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(clienteService, Mockito.times(1)).update(clienteId, clienteRecord);
                Mockito.verifyNoMoreInteractions(clienteService);
        }
}
