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
import com.projeto.appspringapi.enums.TipoTelefoneEnum;
import com.projeto.appspringapi.record.TelefoneRecord;
import com.projeto.appspringapi.service.TelefoneService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TelefoneControllerTest {

        @MockBean
        private TelefoneService telefoneService;

        @Autowired
        private MockMvc mockMvc;

        private Long telefoneId;

        private TelefoneRecord telefoneRecord;

        private String telefoneJson;

        private String url;

        @Autowired
        private ObjectMapper mapper;

        @BeforeEach
        void setUp() throws JsonProcessingException {
                url = "/telefones";
                telefoneId = 1L;
                telefoneRecord = new TelefoneRecord(telefoneId, "(61) 99999-9999", TipoTelefoneEnum.CELULAR,
                                1L);
                telefoneJson = mapper.writeValueAsString(telefoneRecord);
        }

        @Test
        void testDeleteSuccess() throws Exception {
                String path = url.concat("/{id}");
                String message = "Telefone excluído com sucesso!";

                Mockito.doNothing().when(telefoneService).deleteById(telefoneId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, telefoneId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(telefoneService, Mockito.times(1)).deleteById(telefoneId);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testDeleteErrorNotFound() throws Exception {
                String path = url.concat("/{id}");
                String message = "Telefone com id " + telefoneId + " não encontrado!";

                Mockito.doThrow(new EntityNotFoundException(message)).when(telefoneService).deleteById(telefoneId);

                mockMvc.perform(MockMvcRequestBuilders.delete(path, telefoneId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(telefoneService, Mockito.times(1)).deleteById(telefoneId);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testGetAllSuccess() throws JsonProcessingException, Exception {
                Mockito.when(telefoneService.getAll()).thenReturn(List.of(telefoneRecord));

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(telefoneRecord))));

                Mockito.verify(telefoneService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testGetAllErrorEmpty() throws JsonProcessingException, Exception {
                Mockito.when(telefoneService.getAll()).thenReturn(Collections.emptyList());

                mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(telefoneService, Mockito.times(1)).getAll();
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testGetAllByClienteIdSuccess() throws JsonProcessingException, Exception {
                Long clienteId = 1L;
                Mockito.when(telefoneService.getAllByClienteId(clienteId)).thenReturn(List.of(telefoneRecord));

                url = url.concat("/cliente/{id}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, clienteId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(List.of(telefoneRecord))));

                Mockito.verify(telefoneService, Mockito.times(1)).getAllByClienteId(1L);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testGetAllByClienteIdEmpty() throws JsonProcessingException, Exception {
                Long clienteId = 1L;
                Mockito.when(telefoneService.getAllByClienteId(clienteId)).thenReturn(Collections.emptyList());

                url = url.concat("/cliente/{id}");
                mockMvc.perform(MockMvcRequestBuilders.get(url, clienteId).accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .json(mapper.writeValueAsString(Collections.emptyList())));

                Mockito.verify(telefoneService, Mockito.times(1)).getAllByClienteId(1L);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        public void testGetByIdSuccess() throws Exception {
                Mockito.when(telefoneService.getById(telefoneId)).thenReturn(telefoneRecord);

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), telefoneId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.numero").value(telefoneRecord.numero()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo")
                                                .value(telefoneRecord.tipo().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.clienteId")
                                                .value(telefoneRecord.clienteId()))
                                .andExpect(MockMvcResultMatchers.content().json(telefoneJson));

                Mockito.verify(telefoneService, Mockito.times(1)).getById(telefoneId);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        public void testGetByIdErrorNotFound() throws Exception {
                Mockito.when(telefoneService.getById(telefoneId))
                                .thenThrow(new EntityNotFoundException(
                                                "Telefone com id " + telefoneId + " não encontrado!"));

                mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/{id}"), telefoneId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Telefone com id " + telefoneId + " não encontrado!"))
                                .andDo(print());

                Mockito.verify(telefoneService, Mockito.times(1)).getById(telefoneId);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testSaveSuccess() throws Exception {
                telefoneRecord = new TelefoneRecord(null, "(61) 99999-9999", TipoTelefoneEnum.CELULAR,
                                1L);
                telefoneJson = mapper.writeValueAsString(telefoneRecord);

                Mockito.when(telefoneService.save(telefoneRecord)).thenReturn(telefoneRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(telefoneJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.numero").value(telefoneRecord.numero()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo")
                                                .value(telefoneRecord.tipo().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.clienteId")
                                                .value(telefoneRecord.clienteId()))
                                .andExpect(MockMvcResultMatchers.content().json(telefoneJson));

                Mockito.verify(telefoneService, Mockito.times(1)).save(telefoneRecord);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testSaveErrorId() throws Exception {
                String message = "Id deve ser nulo para incluir um novo telefone!";

                Mockito.doThrow(new IllegalArgumentException(message)).when(telefoneService).save(telefoneRecord);

                mockMvc.perform(MockMvcRequestBuilders.post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(telefoneJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(telefoneService, Mockito.times(1)).save(telefoneRecord);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testUpdateSuccess() throws Exception {
                Mockito.when(telefoneService.update(telefoneId, telefoneRecord)).thenReturn(telefoneRecord);

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), telefoneId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(telefoneJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.numero").value(telefoneRecord.numero()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo")
                                                .value(telefoneRecord.tipo().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.clienteId")
                                                .value(telefoneRecord.clienteId()))
                                .andExpect(MockMvcResultMatchers.content().json(telefoneJson));

                Mockito.verify(telefoneService, Mockito.times(1)).update(telefoneId, telefoneRecord);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }

        @Test
        void testUpdateErrorNotFound() throws Exception {
                String message = "Telefone com id " + telefoneId + " não encontrado!";

                Mockito.when(telefoneService.update(telefoneId, telefoneRecord))
                                .thenThrow(new EntityNotFoundException(message));

                mockMvc.perform(MockMvcRequestBuilders.put(url.concat("/{id}"), telefoneId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(telefoneJson))
                                .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message));

                Mockito.verify(telefoneService, Mockito.times(1)).update(telefoneId, telefoneRecord);
                Mockito.verifyNoMoreInteractions(telefoneService);
        }
}
