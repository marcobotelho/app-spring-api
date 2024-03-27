package com.projeto.appspringapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.appspringapi.record.AlterarSenhaRecord;
import com.projeto.appspringapi.record.MsgRecord;
import com.projeto.appspringapi.record.UsuarioRecord;
import com.projeto.appspringapi.service.UsuarioService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("")
	public ResponseEntity<List<UsuarioRecord>> getAll() {
		List<UsuarioRecord> list = usuarioService.getAll();
		return new ResponseEntity<List<UsuarioRecord>>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioRecord> getById(@PathVariable("id") Long id) {
		UsuarioRecord usuario = usuarioService.getById(id);
		return new ResponseEntity<UsuarioRecord>(usuario, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioRecord> update(@PathVariable("id") Long id,
			@RequestBody @Valid UsuarioRecord usuario) {
		UsuarioRecord usuarioSalvo = usuarioService.update(id, usuario);
		return new ResponseEntity<UsuarioRecord>(usuarioSalvo, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<UsuarioRecord> save(@RequestBody @Valid UsuarioRecord usuario, HttpServletRequest request)
			throws MessagingException {
		String baseURL = request.getRequestURL().substring(0,
				request.getRequestURL().indexOf(request.getServletPath()));
		UsuarioRecord usuarioSalvo = usuarioService.save(usuario, baseURL);
		return new ResponseEntity<UsuarioRecord>(usuarioSalvo, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MsgRecord> delete(@PathVariable Long id, HttpServletRequest request) {
		usuarioService.deleteById(id);

		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "Usuario excluído com sucesso!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}

	@GetMapping("/reset-senha/{email}")
	public ResponseEntity<MsgRecord> resetSenha(@PathVariable("email") String email, HttpServletRequest request)
			throws Exception {
		String baseURL = request.getRequestURL().substring(0,
				request.getRequestURL().indexOf(request.getServletPath()));

		usuarioService.resetPassword(email, baseURL);

		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "E-mail com instrução de redefinição de senha enviado!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}

	@PutMapping("/alterar-senha")
	public ResponseEntity<MsgRecord> alterarSenha(@RequestBody @Valid AlterarSenhaRecord alterarSenhaRecord,
			HttpServletRequest request) {
		usuarioService.alterarSenha(alterarSenhaRecord);
		String date = LocalDateTime.now().format(formatter);
		HttpStatus status = HttpStatus.OK;
		String message = "Senha alterada com sucesso!";
		String path = request.getRequestURL().toString();

		MsgRecord msg = new MsgRecord(date, status.toString(), message, path);
		return new ResponseEntity<MsgRecord>(msg, status);
	}

	@GetMapping("/perfil/{perfilId}")
	public ResponseEntity<List<UsuarioRecord>> getAllByClienteId(@PathVariable("perfilId") Long perfilId) {
		List<UsuarioRecord> list = usuarioService.getAllByPerfilId(perfilId);
		return new ResponseEntity<List<UsuarioRecord>>(list, HttpStatus.OK);
	}
}
