package com.projeto.appspringapi.model;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.projeto.appspringapi.enums.TipoTelefoneEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "telefone")
public class TelefoneModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Número obrigatório")
	@Length(min = 15, max = 15, message = "Número inválido")
	private String numero;

	@NotNull(message = "Tipo obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoTelefoneEnum tipo;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_telefone_cliente_id"))
	private ClienteModel cliente;

	public TelefoneModel() {

	}

	public TelefoneModel(String numero, TipoTelefoneEnum tipo, ClienteModel cliente) {
		this.numero = numero;
		this.tipo = tipo;
		this.cliente = cliente;
	}

	public TelefoneModel(Long id, String numero, TipoTelefoneEnum tipo, ClienteModel cliente) {
		this.id = id;
		this.numero = numero;
		this.tipo = tipo;
		this.cliente = cliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public TipoTelefoneEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoTelefoneEnum tipo) {
		this.tipo = tipo;
	}

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "TelefoneModel [id=" + id + ", numero=" + numero + ", tipo=" + tipo + ", cliente=" + cliente + "]";
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
		TelefoneModel other = (TelefoneModel) obj;
		return Objects.equals(id, other.id);
	}

}
