package com.projeto.appspringapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.PerfilModel;

public interface PerfilRepository extends JpaRepository<PerfilModel, Long> {

    List<PerfilModel> findByUsuariosId(Long usuarioId);

}
