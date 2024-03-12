package com.projeto.appspringapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "perfil")
public class PerfilModel {

    @Override
    public String toString() {
        return "RoleModel [id=" + id + ", nome=" + nome + "]";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name obrigatório")
    private String nome;

    @NotBlank(message = "Descrição obrigatória")
    private String descricao;

    @ManyToMany(mappedBy = "perfis")
    private List<UsuarioModel> usuarios = new ArrayList<>();

    public PerfilModel() {
    }

    public PerfilModel(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public PerfilModel(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<UsuarioModel> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioModel> usuarios) {
        this.usuarios = usuarios;
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
        PerfilModel other = (PerfilModel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
