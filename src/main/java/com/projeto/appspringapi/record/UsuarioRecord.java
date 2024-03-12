package com.projeto.appspringapi.record;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRecord(Long id,
                @NotBlank(message = "Nome obrigatório") @Length(min = 3, max = 100, message = "Nome inválido") String nome,
                @NotBlank(message = "Email obrigatório") @Email(message = "Email inválido") String email) {

}
