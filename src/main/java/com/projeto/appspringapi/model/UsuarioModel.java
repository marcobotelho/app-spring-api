package com.projeto.appspringapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuario", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "email" }, name = "uk_usuario_email")
})
public class UsuarioModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome obrigatório")
	@Length(min = 3, max = 100, message = "Nome inválido")
	private String nome;

	@NotBlank(message = "Email obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@NotBlank(message = "Senha obrigatória")
	@Length(min = 3, max = 100, message = "Senha inválida")
	private String senha;

	private String token;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_perfil", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
	private List<PerfilModel> perfis = new ArrayList<>();

	public UsuarioModel() {
	}

	public UsuarioModel(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public UsuarioModel(Long id, String nome, String email) {
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

	public UsuarioModel(Long id, String nome, String email, List<PerfilModel> perfis) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.perfis = perfis;
	}

	public UsuarioModel(Long id, String nome, String email, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<PerfilModel> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilModel> perfis) {
		this.perfis = perfis;
	}

	@Override
	public String toString() {
		return "UsuarioModel [id=" + id + ", nome=" + nome + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioModel other = (UsuarioModel) obj;
		return Objects.equals(id, other.id);
	}

}
