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
import com.projeto.appspringapi.record.PerfilRecord;
import com.projeto.appspringapi.service.PerfilService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	@Autowired
	private PerfilService perfilService;

	@GetMapping("")
	public ResponseEntity<List<PerfilRecord>> getAll() {
		List<PerfilRecord> list = perfilService.getAll();
		return new ResponseEntity<List<PerfilRecord>>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PerfilRecord> getById(@PathVariable("id") Long id) {
		PerfilRecord perfil = perfilService.getById(id);
		return new ResponseEntity<PerfilRecord>(perfil, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PerfilRecord> update(@PathVariable("id") Long id,
			@RequestBody @Valid PerfilRecord perfil) {
		PerfilRecord perfilSalvo = perfilService.update(id, perfil);
		return new ResponseEntity<PerfilRecord>(perfilSalvo, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<PerfilRecord> save(@RequestBody @Valid PerfilRecord perfil) {
		PerfilRecord perfilSalvo = perfilService.save(perfil);
		return new ResponseEntity<PerfilRecord>(perfilSalvo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MsgRecord> delete(@PathVariable Long id, HttpServletRequest request) {
		perfilService.deleteById(id);

		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "Perfil excluído com sucesso!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<PerfilRecord>> getAllByClienteId(@PathVariable("usuarioId") Long usuarioId) {
		List<PerfilRecord> list = perfilService.getAllByUsuarioId(usuarioId);
		return new ResponseEntity<List<PerfilRecord>>(list, HttpStatus.OK);
	}

}
