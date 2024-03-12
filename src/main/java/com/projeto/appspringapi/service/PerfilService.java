package com.projeto.appspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringapi.mapper.PerfilMapper;
import com.projeto.appspringapi.model.PerfilModel;
import com.projeto.appspringapi.record.PerfilRecord;
import com.projeto.appspringapi.repository.PerfilRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    private PerfilModel getPerfilModelById(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil com id " + id + " não encontrado!"));
    }

    public PerfilRecord save(PerfilRecord record) {
        PerfilModel perfilModel = perfilRepository.save(PerfilMapper.toModel(record));
        return PerfilMapper.toRecord(perfilModel);
    }

    public void delete(PerfilRecord record) {
        getById(record.id());
        perfilRepository.delete(PerfilMapper.toModel(record));
    }

    public void deleteById(Long id) {
        getById(id);
        perfilRepository.deleteById(id);
    }

    public PerfilRecord update(Long id, PerfilRecord record) {
        PerfilModel perfilModel = getPerfilModelById(id);
        perfilModel.setNome(record.nome());
        perfilModel.setDescricao(record.descricao());
        perfilModel = perfilRepository.save(perfilModel);
        return PerfilMapper.toRecord(perfilModel);
    }

    public PerfilRecord getById(Long id) {
        PerfilModel perfilModel = getPerfilModelById(id);
        return PerfilMapper.toRecord(perfilModel);
    }

    public List<PerfilRecord> getAll() {
        return PerfilMapper.toRecordList(perfilRepository.findAll());
    }

    public List<PerfilRecord> getAllByUsuarioId(Long usuarioId) {
        return PerfilMapper.toRecordList(perfilRepository.findByUsuariosId(usuarioId));
    }
}
