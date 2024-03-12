package com.projeto.appspringapi.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cliente", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "email" }, name = "uk_cliente_email")
})
public class ClienteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome obrigatório")
	@Length(min = 3, max = 100, message = "Nome inválido")
	private String nome;

	@NotBlank(message = "Email obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@NotNull(message = "Idade obrigatória")
	@Range(min = 1, max = 150, message = "Idade inválida")
	private Integer idade;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;

	private String cep;

	private String endereco;

	private String bairro;

	private String municipio;

	private String estado;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonIgnore
	private List<TelefoneModel> telefones;

	public ClienteModel() {

	}

	public ClienteModel(Long id, String nome, String email, Integer idade, LocalDate dataNascimento,
			String cep, String endereco, String bairro, String municipio, String estado) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.idade = idade;
		this.dataNascimento = dataNascimento;
		this.cep = cep;
		this.endereco = endereco;
		this.bairro = bairro;
		this.municipio = municipio;
		this.estado = estado;
	}

	public ClienteModel(String nome, String email, Integer idade, LocalDate dataNascimento,
			String cep, String endereco, String bairro, String municipio, String estado) {
		this.nome = nome;
		this.email = email;
		this.idade = idade;
		this.dataNascimento = dataNascimento;
		this.cep = cep;
		this.endereco = endereco;
		this.bairro = bairro;
		this.municipio = municipio;
		this.estado = estado;
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

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<TelefoneModel> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneModel> telefones) {
		this.telefones = telefones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteModel other = (ClienteModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClienteModel [id=" + id + ", nome=" + nome + ", email=" + email + ", idade=" + idade
				+ ", dataNascimento=" + dataNascimento + ", cep=" + cep + ", endereco=" + endereco + ", bairro="
				+ bairro + ", municipio=" + municipio + ", estado=" + estado + "]";
	}

}
