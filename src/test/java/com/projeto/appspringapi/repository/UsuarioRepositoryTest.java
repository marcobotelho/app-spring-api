package com.projeto.appspringapi.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.projeto.appspringapi.model.PerfilModel;
import com.projeto.appspringapi.model.UsuarioModel;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Test
    @DisplayName("Retorna todos os usuários por perfil id")
    void testFindAllByPerfisIdSucesso() {
        Long perfilId = 1L;
        PerfilModel perfil = new PerfilModel(perfilId, "ROLE_ADMIN", "Administrador");
        perfil = salvarPerfil(perfil);

        List<PerfilModel> perfis = List.of(perfil);

        UsuarioModel usuarioModel = new UsuarioModel(1L, "marco", "marco@email.com", "123");
        usuarioModel.setPerfis(perfis);
        usuarioModel = salvarUsuario(usuarioModel);

        List<UsuarioModel> result = usuarioRepository.findAllByPerfisId(perfilId);

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Não Retorna todos os usuários por perfil id quando nao existem")
    void testFindAllByPerfisIdNaoEncontrado() {
        Long perfilId = 1L;

        List<UsuarioModel> result = usuarioRepository.findAllByPerfisId(perfilId);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Retorna um usuário pelo email")
    void testFindByEmailSucesso() {
        String email = "marco@email.com";

        UsuarioModel usuarioModel = new UsuarioModel(1L, "marco", email, "123");
        salvarUsuario(usuarioModel);

        Optional<UsuarioModel> result = usuarioRepository.findByEmail(email);

        Assertions.assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Não Retorna um usuário pelo email quando nao existe")
    void testFindByEmailNaoEncontrado() {
        String email = "marco@email.com";

        Optional<UsuarioModel> result = usuarioRepository.findByEmail(email);

        Assertions.assertThat(result).isNotPresent();
    }

    private UsuarioModel salvarUsuario(UsuarioModel model) {
        model = usuarioRepository.save(model);
        return model;
    }

    private PerfilModel salvarPerfil(PerfilModel model) {
        model = perfilRepository.save(model);
        return model;
    }
}
