package com.projeto.appspringapi.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.projeto.appspringapi.mapper.PerfilMapper;
import com.projeto.appspringapi.model.PerfilModel;
import com.projeto.appspringapi.model.UsuarioModel;
import com.projeto.appspringapi.record.PerfilRecord;
import com.projeto.appspringapi.repository.PerfilRepository;

import jakarta.persistence.EntityNotFoundException;

public class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Autowired
    @InjectMocks
    private PerfilService perfilService;

    private Long usuarioId;

    private Long perfilId;

    private PerfilModel perfilModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioId = 1L;
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setId(usuarioId);
        usuarioModel.setNome("Usuario Teste");
        usuarioModel.setEmail("teste@teste.com");
        usuarioModel.setSenha("123");

        perfilId = 1L;
        perfilModel = new PerfilModel(perfilId, "ROLE_USER", "Perfil Usuario");
        perfilModel.setUsuarios(List.of(usuarioModel));
    }

    @Test
    @DisplayName("Deletar um perfil")
    void testDeleteSuccess() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfilModel));

        perfilService.delete(PerfilMapper.toRecord(perfilModel));

        Mockito.verify(perfilRepository, Mockito.times(1)).delete(perfilModel);
        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
    }

    @Test
    @DisplayName("Erro ao deletar um perfil inexistente")
    void testDeleteErrorNotFound() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            perfilService.delete(PerfilMapper.toRecord(perfilModel));
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(0)).delete(perfilModel);

        Assertions.assertEquals("Perfil com id " + perfilId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Deletar um perfil por id")
    void testDeleteByIdSuccess() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfilModel));

        perfilService.deleteById(perfilId);

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(1)).deleteById(perfilId);
        Mockito.verifyNoMoreInteractions(perfilRepository);
    }

    @Test
    @DisplayName("Erro ao deletar um perfil por id inexistente")
    void testDeleteByIdErrorNotFound() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            perfilService.deleteById(perfilId);
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(0)).deleteById(perfilId);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals("Perfil com id " + perfilId + " não encontrado!", exception.getMessage());
    }

    @Test
    void testGetAll() {
        Mockito.when(perfilRepository.findAll()).thenReturn(List.of(perfilModel));

        List<PerfilRecord> result = perfilService.getAll();

        Mockito.verify(perfilRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(PerfilMapper.toRecordList(List.of(perfilModel)), result);
    }

    @Test
    @DisplayName("Listar todos os perfis de um usuario")
    void testGetAllByUsuarioId() {
        Mockito.when(perfilRepository.findByUsuariosId(usuarioId)).thenReturn(List.of(perfilModel));

        List<PerfilRecord> result = perfilService.getAllByUsuarioId(usuarioId);

        Mockito.verify(perfilRepository, Mockito.times(1)).findByUsuariosId(usuarioId);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(PerfilMapper.toRecordList(List.of(perfilModel)), result);
    }

    @Test
    @DisplayName("Pegar um perfil por id")
    void testGetByIdSuccess() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfilModel));

        PerfilRecord result = perfilService.getById(perfilId);

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(PerfilMapper.toRecord(perfilModel), result);
    }

    @Test
    @DisplayName("Erro ao pegar um perfil por id inexistente")
    void testGetByIdErrorNotFound() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            perfilService.getById(perfilId);
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals("Perfil com id " + perfilId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Salvar um perfil")
    void testSaveSuccess() {
        Mockito.when(perfilRepository.save(Mockito.any(PerfilModel.class))).thenReturn(perfilModel);

        PerfilRecord result = perfilService.save(PerfilMapper.toRecord(perfilModel));

        Mockito.verify(perfilRepository, Mockito.times(1)).save(perfilModel);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(PerfilMapper.toRecord(perfilModel), result);
    }

    @Test
    @DisplayName("Erro ao salvar um perfil")
    void testSaveError() {
        Mockito.when(perfilRepository.save(Mockito.any(PerfilModel.class)))
                .thenThrow(DataIntegrityViolationException.class);

        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            perfilService.save(PerfilMapper.toRecord(perfilModel));
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).save(perfilModel);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals(DataIntegrityViolationException.class, exception.getClass());
    }

    @Test
    @DisplayName("Atualizar um perfil")
    void testUpdateSuccess() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfilModel));
        Mockito.when(perfilRepository.save(Mockito.any(PerfilModel.class))).thenReturn(perfilModel);

        PerfilRecord result = perfilService.update(perfilId, PerfilMapper.toRecord(perfilModel));

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(1)).save(perfilModel);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(PerfilMapper.toRecord(perfilModel), result);
    }

    @Test
    @DisplayName("Erro ao atualizar um perfil id inexistente")
    void testUpdateErrorNotFound() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());
        Mockito.when(perfilRepository.save(Mockito.any(PerfilModel.class))).thenReturn(perfilModel);

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            perfilService.update(perfilId, PerfilMapper.toRecord(perfilModel));
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(0)).save(perfilModel);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals("Perfil com id " + perfilId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Erro ao atualizar um perfil id inexistente")
    void testUpdateErrorSave() {
        Mockito.when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfilModel));
        Mockito.when(perfilRepository.save(Mockito.any(PerfilModel.class)))
                .thenThrow(DataIntegrityViolationException.class);

        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            perfilService.update(perfilId, PerfilMapper.toRecord(perfilModel));
        });

        Mockito.verify(perfilRepository, Mockito.times(1)).findById(perfilId);
        Mockito.verify(perfilRepository, Mockito.times(1)).save(perfilModel);
        Mockito.verifyNoMoreInteractions(perfilRepository);
        Assertions.assertEquals(DataIntegrityViolationException.class, exception.getClass());
    }

}
