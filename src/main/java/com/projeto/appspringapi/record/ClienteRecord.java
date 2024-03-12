package com.projeto.appspringapi.record;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRecord(Long id,
        @NotBlank(message = "Nome obrigatório") @Length(min = 3, max = 100, message = "Nome inválido") String nome,
        @NotBlank(message = "Email obrigatório") @Email(message = "Email inválido") String email,
        @NotNull(message = "Idade obrigatória") @Range(min = 1, max = 150, message = "Idade inválida") Integer idade,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataNascimento,
        String cep, String endereco,
        String bairro, String municipio,
        String estado) {

}
