package com.projeto.appspringapi.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import com.projeto.appspringapi.record.LoginRecord;
import com.projeto.appspringapi.service.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private LoginRecord loginRecord;

    private String loginJson;

    @Autowired
    private ObjectMapper mapper;

    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        loginRecord = new LoginRecord("email@teste.com", "123456");
        loginJson = mapper.writeValueAsString(loginRecord);
        url = "/login";
    }

    @Test
    void testLoginPage() throws Exception {
        String token = "adfdfdfdfadfdffsdfdsfdfsdf";
        Mockito.when(usuarioService.getToken(loginRecord)).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bearer Token " + token))
                .andDo(print());

        Mockito.verify(usuarioService, Mockito.times(1)).getToken(loginRecord);
        Mockito.verifyNoMoreInteractions(usuarioService);
    }
}
