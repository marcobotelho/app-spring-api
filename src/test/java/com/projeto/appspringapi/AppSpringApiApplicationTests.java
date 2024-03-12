package com.projeto.appspringapi;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.projeto.appspringapi.mapper.ClienteMapper;
import com.projeto.appspringapi.mapper.TelefoneMapper;
import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.repository.ClienteRepository;
import com.projeto.appspringapi.service.JwtService;

import jakarta.transaction.Transactional;

@SpringBootTest
class AppSpringApiApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ClienteRepository clienteRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void gerarChaveSecretaJwt() {
		SecretKey chaveSecreta = jwtService.getChaveSecreta();
		String chaveString = jwtService.secretKeyToString(chaveSecreta);
		System.out.println("Chave Secreta: " + chaveString);
	}

	@Test
	@Transactional
	void ClienteModelToRecord() {
		ClienteModel clienteModel = clienteRepository.findById(1L)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
		System.out.println("Model: " + clienteModel);
		System.out.println("Record: " + ClienteMapper.toRecord(clienteModel));

		System.out.println("Telefones: " + TelefoneMapper.toRecordList(clienteModel.getTelefones()));

	}

}
