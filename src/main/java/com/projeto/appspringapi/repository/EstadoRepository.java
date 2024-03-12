package com.projeto.appspringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.EstadoModel;

public interface EstadoRepository extends JpaRepository<EstadoModel, Long> {

}
