package com.projeto.appspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringapi.mapper.TelefoneMapper;
import com.projeto.appspringapi.model.TelefoneModel;
import com.projeto.appspringapi.record.TelefoneRecord;
import com.projeto.appspringapi.repository.TelefoneRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TelefoneService {

	@Autowired
	private TelefoneRepository telefoneRepository;

	private TelefoneModel getTelefoneModelById(Long id) {
		return telefoneRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Telefone com id " + id + " não encontrado!"));
	}

	public TelefoneRecord save(TelefoneRecord record) {
		TelefoneModel telefoneModel = telefoneRepository.save(TelefoneMapper.toModel(record));
		return TelefoneMapper.toRecord(telefoneModel);
	}

	public void delete(TelefoneRecord record) {
		getById(record.id());
		telefoneRepository.delete(TelefoneMapper.toModel(record));
	}

	public void deleteById(Long id) {
		getById(id);
		telefoneRepository.deleteById(id);
	}

	public TelefoneRecord update(Long id, TelefoneRecord record) {
		TelefoneModel telefoneModel = getTelefoneModelById(id);
		telefoneModel.setNumero(record.numero());
		telefoneModel.setTipo(record.tipo());
		telefoneModel = telefoneRepository.save(telefoneModel);
		return TelefoneMapper.toRecord(telefoneModel);
	}

	public TelefoneRecord getById(Long id) {
		TelefoneModel telefoneModel = getTelefoneModelById(id);
		return TelefoneMapper.toRecord(telefoneModel);
	}

	public List<TelefoneRecord> getAll() {
		return TelefoneMapper.toRecordList(telefoneRepository.findAll());
	}

	public List<TelefoneRecord> getAllByClienteId(Long usuarioId) {
		return TelefoneMapper.toRecordList(telefoneRepository.findByClienteId(usuarioId));
	}
}
