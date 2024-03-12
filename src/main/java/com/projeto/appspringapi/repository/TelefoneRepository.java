package com.projeto.appspringapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.TelefoneModel;

public interface TelefoneRepository extends JpaRepository<TelefoneModel, Long> {

	List<TelefoneModel> findByClienteId(Long clienteId);
}
