package com.projeto.appspringapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.appspringapi.record.ClienteRecord;
import com.projeto.appspringapi.record.MsgRecord;
import com.projeto.appspringapi.service.ClienteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	@Autowired
	private ClienteService clienteService;

	@GetMapping("")
	public ResponseEntity<List<ClienteRecord>> getAll() {
		List<ClienteRecord> list = clienteService.getAll();
		return new ResponseEntity<List<ClienteRecord>>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteRecord> getById(@PathVariable("id") Long id) {
		ClienteRecord cliente = clienteService.getById(id);
		return new ResponseEntity<ClienteRecord>(cliente, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteRecord> update(@PathVariable("id") Long id,
			@RequestBody @Valid ClienteRecord cliente) {
		ClienteRecord clienteSalvo = clienteService.update(id, cliente);
		return new ResponseEntity<ClienteRecord>(clienteSalvo, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ClienteRecord> save(@RequestBody @Valid ClienteRecord cliente) {
		ClienteRecord clienteSalvo = clienteService.save(cliente);
		return new ResponseEntity<ClienteRecord>(clienteSalvo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MsgRecord> delete(@PathVariable Long id, HttpServletRequest request) {
		clienteService.deleteById(id);

		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "Cliente excluído com sucesso!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}
}
