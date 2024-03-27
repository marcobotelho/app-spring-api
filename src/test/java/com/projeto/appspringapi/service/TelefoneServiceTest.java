package com.projeto.appspringapi.service;

import java.util.Collections;
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

import com.projeto.appspringapi.enums.TipoTelefoneEnum;
import com.projeto.appspringapi.mapper.TelefoneMapper;
import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.model.TelefoneModel;
import com.projeto.appspringapi.record.TelefoneRecord;
import com.projeto.appspringapi.repository.TelefoneRepository;

import jakarta.persistence.EntityNotFoundException;

public class TelefoneServiceTest {

    @Mock
    private TelefoneRepository telefoneRepository;

    @Autowired
    @InjectMocks
    private TelefoneService telefoneService;

    private Long telefoneId;

    private Long clienteId;

    private TelefoneModel telefoneModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        telefoneId = 1L;
        clienteId = 1L;
        telefoneModel = new TelefoneModel(telefoneId, "(61) 99999-9999", TipoTelefoneEnum.CELULAR,
                new ClienteModel(clienteId, "Teste", "teste@teste.com", 25));
    }

    @Test
    @DisplayName("Excluir um telefone")
    void testDeleteSuccess() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.of(telefoneModel));

        telefoneService.delete(TelefoneMapper.toRecord(telefoneModel));

        Mockito.verify(telefoneRepository, Mockito.times(1)).delete(telefoneModel);
        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);
    }

    @Test
    @DisplayName("Erro ao excluir um telefone inexistente")
    void testDeleteNotFound() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            telefoneService.delete(TelefoneMapper.toRecord(telefoneModel));
        });

        Mockito.verify(telefoneRepository, Mockito.times(0)).delete(telefoneModel);
        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);

        Assertions.assertEquals("Telefone com id " + telefoneId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Excluir um telefone por id")
    void testDeleteByIdSuccess() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.of(telefoneModel));

        telefoneService.deleteById(telefoneId);

        Mockito.verify(telefoneRepository, Mockito.times(1)).deleteById(telefoneId);
        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);
    }

    @Test
    @DisplayName("Erro ao excluir um telefone por id inexistente")
    void testDeleteByIdNotFound() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            telefoneService.deleteById(telefoneId);
        });

        Mockito.verify(telefoneRepository, Mockito.times(0)).deleteById(telefoneId);
        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);

        Assertions.assertEquals("Telefone com id " + telefoneId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Listar todos os telefones")
    void testGetAll() {
        List<TelefoneModel> telefoneModels = List.of(telefoneModel);

        Mockito.when(telefoneRepository.findAll()).thenReturn(telefoneModels);

        List<TelefoneRecord> result = telefoneService.getAll();

        Mockito.verify(telefoneRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(TelefoneMapper.toRecordList(telefoneModels), result);

    }

    @Test
    @DisplayName("Listar todos os telefones por cliente id")
    void testGetAllByClienteIdExists() {
        List<TelefoneModel> telefoneModels = List.of(telefoneModel);

        Mockito.when(telefoneRepository.findByClienteId(clienteId)).thenReturn(telefoneModels);

        List<TelefoneRecord> result = telefoneService.getAllByClienteId(clienteId);

        Mockito.verify(telefoneRepository, Mockito.times(1)).findByClienteId(clienteId);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(TelefoneMapper.toRecordList(telefoneModels), result);

        Mockito.verify(telefoneRepository, Mockito.times(1)).findByClienteId(clienteId);
    }

    @Test
    @DisplayName("Listar todos os telefones por cliente id inexistente")
    void testGetAllByClienteIdNotExists() {
        Mockito.when(telefoneRepository.findByClienteId(clienteId)).thenReturn(Collections.emptyList());

        List<TelefoneRecord> result = telefoneService.getAllByClienteId(clienteId);

        Mockito.verify(telefoneRepository, Mockito.times(1)).findByClienteId(clienteId);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Retorna um telefone por id")
    void testGetByIdSuccess() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.of(telefoneModel));

        TelefoneRecord result = telefoneService.getById(telefoneId);

        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);
        Assertions.assertEquals(TelefoneMapper.toRecord(telefoneModel), result);
    }

    @Test
    @DisplayName("Erro ao retornar um telefone por id inexistente")
    void testGetByIdNotFound() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            telefoneService.getById(telefoneId);
        });

        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);
        Assertions.assertEquals("Telefone com id " + telefoneId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Salva um telefone")
    void testSaveSuccess() {
        telefoneModel = new TelefoneModel(null, "(61) 99999-9999", TipoTelefoneEnum.CELULAR,
                new ClienteModel(clienteId, "Teste", "teste@teste.com", 25));

        Mockito.when(telefoneRepository.save(telefoneModel)).thenReturn(telefoneModel);

        TelefoneRecord result = telefoneService.save(TelefoneMapper.toRecord(telefoneModel));

        Mockito.verify(telefoneRepository, Mockito.times(1)).save(telefoneModel);
        Assertions.assertEquals(TelefoneMapper.toRecord(telefoneModel), result);
    }

    @Test
    @DisplayName("Erro ao salvar um telefone com id nulo")
    void testSaveErrorIdNotNull() {
        String errorMessage = "Id deve ser nulo para incluir um novo telefone!";
        Mockito.when(telefoneRepository.save(telefoneModel)).thenThrow(new IllegalArgumentException(errorMessage));

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            telefoneService.save(TelefoneMapper.toRecord(telefoneModel));
        });

        Mockito.verify(telefoneRepository, Mockito.times(0)).save(telefoneModel);
        Mockito.verifyNoMoreInteractions(telefoneRepository);
        Assertions.assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Erro ao salvar um telefone por integridade dados")
    void testSaveErrorDataIntegrity() {
        telefoneModel = new TelefoneModel(null, "(61) 99999-9999", TipoTelefoneEnum.CELULAR,
                new ClienteModel(clienteId, "Teste", "teste@teste.com", 25));

        Mockito.when(telefoneRepository.save(telefoneModel)).thenThrow(DataIntegrityViolationException.class);

        Exception exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            telefoneService.save(TelefoneMapper.toRecord(telefoneModel));
        });

        Mockito.verify(telefoneRepository, Mockito.times(1)).save(telefoneModel);
        Assertions.assertEquals(DataIntegrityViolationException.class, exception.getClass());
    }

    @Test
    void testUpdate() {
        Mockito.when(telefoneRepository.findById(telefoneId)).thenReturn(Optional.of(telefoneModel));
        Mockito.when(telefoneRepository.save(telefoneModel)).thenReturn(telefoneModel);

        TelefoneRecord updatedRecord = telefoneService.update(telefoneId, TelefoneMapper.toRecord(telefoneModel));

        Mockito.verify(telefoneRepository, Mockito.times(1)).save(telefoneModel);
        Mockito.verify(telefoneRepository, Mockito.times(1)).findById(telefoneId);
        Assertions.assertEquals(TelefoneMapper.toRecord(telefoneModel), updatedRecord);
    }
}
