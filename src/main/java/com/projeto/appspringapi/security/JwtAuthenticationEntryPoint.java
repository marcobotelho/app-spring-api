package com.projeto.appspringapi.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.appspringapi.record.MsgRecord;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Este método é chamado quando ocorre uma exceção de autenticação não
        // autorizada.

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "Acesso não autorizado. Por favor, faça login para acessar este recurso.";
        String path = request.getRequestURL().toString();

        MsgRecord msgRecord = new MsgRecord(date, status.toString(), message, path);
        String jsonMsgRecord = objectMapper.writeValueAsString(msgRecord);

        // Escrever a resposta no HttpServletResponse
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write(jsonMsgRecord);
        response.flushBuffer();
    }
}
