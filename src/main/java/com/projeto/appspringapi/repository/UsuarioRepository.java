package com.projeto.appspringapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmail(String email);

    List<UsuarioModel> findAllByPerfisId(Long perfilId);
}
