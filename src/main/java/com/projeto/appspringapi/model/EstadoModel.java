package com.projeto.appspringapi.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "estado")
public class EstadoModel {

	@Id
	private Long id;

	@NotBlank(message = "Nome obrigatório")
	private String nome;

	@NotBlank(message = "Sigla obrigatória")
	private String sigla;

	@OneToMany(mappedBy = "estado", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<MunicipioModel> cidades;

	public EstadoModel() {

	}

	public EstadoModel(Long id, String nome, String sigla) {
		this.id = id;
		this.nome = nome;
		this.sigla = sigla;
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public List<MunicipioModel> getCidades() {
		return cidades;
	}

	public void setCidades(List<MunicipioModel> cidades) {
		this.cidades = cidades;
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
		EstadoModel other = (EstadoModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EstadoModel [id=" + id + ", nome=" + nome + ", sigla=" + sigla + "]";
	}

}
