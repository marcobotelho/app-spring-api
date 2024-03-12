package com.projeto.appspringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.appspringapi.model.MunicipioModel;

public interface MunicipioRepository extends JpaRepository<MunicipioModel, Long> {

}
