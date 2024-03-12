package com.projeto.appspringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

}
