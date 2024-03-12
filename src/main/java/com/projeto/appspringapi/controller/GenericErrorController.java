package com.projeto.appspringapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.projeto.appspringapi.record.MsgRecord;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GenericErrorController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private MsgRecord getMsgRecord(String date, String status, String message, String path) {
        return new MsgRecord(date, status, message, path);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MsgRecord> handleException(Exception e, HttpServletRequest request) {
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = e.getMessage();
        String path = request.getRequestURL().toString();

        return new ResponseEntity<MsgRecord>(
                getMsgRecord(date, status.toString() + " | " + e.getClass().getSimpleName(), message, path), status);
    }

    @ExceptionHandler({ EntityNotFoundException.class, NoResourceFoundException.class })
    public ResponseEntity<MsgRecord> handleEntityNotFoundException(Exception e,
            HttpServletRequest request) {
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = e.getMessage();
        String path = request.getRequestURL().toString();

        if (e instanceof NoResourceFoundException) {
            message = "Recurso '" + request.getRequestURL().toString() + "' não encontrado";
        }
        return new ResponseEntity<MsgRecord>(
                getMsgRecord(date, status.toString() + " | " + e.getClass().getSimpleName(), message, path), status);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<MsgRecord> handleHttpMessageNotReadableException(Exception e,
            HttpServletRequest request) {
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage();
        String path = request.getRequestURL().toString();

        if (e instanceof HttpMessageNotReadableException) {
            message = "Formato JSON inválido no corpo da solicitação";
        }

        return new ResponseEntity<MsgRecord>(
                getMsgRecord(date, status.toString() + " | " + e.getClass().getSimpleName(), message, path), status);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<MsgRecord> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage();
        String path = request.getRequestURL().toString();

        // if (e instanceof MethodArgumentNotValidException) {
        message = e.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        // }
        return new ResponseEntity<MsgRecord>(
                getMsgRecord(date, status.toString() + " | " + e.getClass().getSimpleName(), message, path), status);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<MsgRecord> handleDataIntegrityViolationException(DataIntegrityViolationException e,
            HttpServletRequest request) {
        String date = LocalDateTime.now().format(formatter);
        HttpStatus status = HttpStatus.CONFLICT;
        String message = e.getMessage();
        String path = request.getRequestURL().toString();

        if (message.contains("Unique index or primary key violation:")) {
            String tableName = extractTableName(message);
            String fieldName = extractFieldName(message);

            message = "Ja existe um " + tableName.toLowerCase() + " com este " + fieldName.toLowerCase();
        }

        return new ResponseEntity<MsgRecord>(
                getMsgRecord(date, status.toString() + " | " + e.getClass().getSimpleName(), message, path), status);
    }

    public static String extractTableName(String errorMessage) {
        Pattern pattern = Pattern.compile("ON PUBLIC\\.([A-Z0-9_]+)\\(");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String extractFieldName(String errorMessage) {
        Pattern pattern = Pattern.compile("\\(([A-Z0-9_]+) NULLS FIRST\\)");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
