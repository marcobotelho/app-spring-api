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

import com.projeto.appspringapi.record.MsgRecord;
import com.projeto.appspringapi.record.TelefoneRecord;
import com.projeto.appspringapi.service.TelefoneService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/telefones")
public class TelefoneController {

	@Autowired
	private TelefoneService telefoneService;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	@GetMapping("")
	public ResponseEntity<List<TelefoneRecord>> getAll() {
		List<TelefoneRecord> list = telefoneService.getAll();
		return new ResponseEntity<List<TelefoneRecord>>(list, HttpStatus.OK);
	}

	@GetMapping("/cliente/{id}")
	public ResponseEntity<List<TelefoneRecord>> getAllByClienteId(@PathVariable("id") Long id) {
		List<TelefoneRecord> list = telefoneService.getAllByClienteId(id);
		return new ResponseEntity<List<TelefoneRecord>>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TelefoneRecord> getById(@PathVariable("id") Long id) {
		TelefoneRecord telefone = telefoneService.getById(id);
		return new ResponseEntity<TelefoneRecord>(telefone, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TelefoneRecord> update(@PathVariable("id") Long id,
			@RequestBody @Valid TelefoneRecord telefone) {
		TelefoneRecord telefoneSalvo = telefoneService.update(id, telefone);
		return new ResponseEntity<TelefoneRecord>(telefoneSalvo, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<TelefoneRecord> save(@RequestBody @Valid TelefoneRecord telefone) {
		TelefoneRecord telefoneSalvo = telefoneService.save(telefone);
		return new ResponseEntity<TelefoneRecord>(telefoneSalvo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MsgRecord> delete(@PathVariable Long id, HttpServletRequest request) {
		telefoneService.deleteById(id);

		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "Telefone excluído com sucesso!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}
}
