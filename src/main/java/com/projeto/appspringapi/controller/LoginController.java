package com.projeto.appspringapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.projeto.appspringapi.record.LoginRecord;
import com.projeto.appspringapi.record.MsgRecord;
import com.projeto.appspringapi.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class LoginController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<MsgRecord> loginPage(@RequestBody @Valid LoginRecord loginRecord,
            HttpServletRequest request) {
        String token = usuarioService.getToken(loginRecord);

        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.OK;
        String message = "Bearer Token " + token;
        String path = request.getRequestURL().toString();

        MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
        return new ResponseEntity<MsgRecord>(msg, status);
    }
}
