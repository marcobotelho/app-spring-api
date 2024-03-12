package com.projeto.appspringapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.appspringapi.mapper.ClienteMapper;
import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.record.ClienteRecord;
import com.projeto.appspringapi.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	private ClienteModel getClienteModelById(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente com id " + id + " não encontrado!"));
	}

	public ClienteRecord save(ClienteRecord record) {
		ClienteModel clienteModel = clienteRepository.save(ClienteMapper.toModel(record));
		return ClienteMapper.toRecord(clienteModel);
	}

	public void delete(ClienteRecord record) {
		getById(record.id());
		clienteRepository.delete(ClienteMapper.toModel(record));
	}

	public void deleteById(Long id) {
		getById(id);
		clienteRepository.deleteById(id);
	}

	public ClienteRecord update(Long id, ClienteRecord record) {
		ClienteModel clienteModel = getClienteModelById(id);
		clienteModel.setNome(record.nome());
		clienteModel.setEmail(record.email());
		clienteModel.setIdade(record.idade());
		clienteModel.setDataNascimento(record.dataNascimento());
		clienteModel.setCep(record.cep());
		clienteModel.setEndereco(record.endereco());
		clienteModel.setBairro(record.bairro());
		clienteModel.setMunicipio(record.municipio());
		clienteModel.setEstado(record.estado());
		clienteModel = clienteRepository.save(clienteModel);
		return ClienteMapper.toRecord(clienteModel);
	}

	public ClienteRecord getById(Long id) {
		ClienteModel clienteModel = getClienteModelById(id);
		return ClienteMapper.toRecord(clienteModel);
	}

	public List<ClienteRecord> getAll() {
		return ClienteMapper.toRecordList(clienteRepository.findAll());
	}
}
