package com.projeto.appspringapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.projeto.appspringapi.mapper.ClienteMapper;
import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.record.ClienteRecord;
import com.projeto.appspringapi.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Autowired
    @InjectMocks
    private ClienteService clienteService;

    private Long clienteId = 1L;

    private ClienteModel clienteModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteModel = new ClienteModel(clienteId, "Teste", "teste@teste.com", 25);
    }

    @Test
    @DisplayName("Excluir um cliente")
    void testDelete() {
        // Arrange
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteModel));

        // Act
        clienteService.delete(ClienteMapper.toRecord(clienteModel));

        // Assert
        verify(clienteRepository, times(1)).delete(clienteModel);
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    @DisplayName("Erro ao excluir um cliente inexistente")
    void testDeleteNotFound() {
        // Arrange
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clienteService.delete(ClienteMapper.toRecord(clienteModel));
        });

        // Assert
        verify(clienteRepository, times(0)).delete(any());
        verify(clienteRepository, times(1)).findById(clienteId);
        Assertions.assertEquals("Cliente com id " + clienteId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Excluir um cliente por id")
    void testDeleteById() {
        // Arrange
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteModel));

        // Act
        clienteService.deleteById(clienteId);

        // Assert
        verify(clienteRepository, times(1)).deleteById(clienteId);
        verify(clienteRepository, times(1)).findById(clienteId);
    }

    @Test
    @DisplayName("Erro ao excluir um cliente por id inexistente")
    void testDeleteByIdNotFound() {
        // Arrange
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // Act
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clienteService.deleteById(clienteId);
        });

        // Assert
        verify(clienteRepository, times(0)).deleteById(clienteId);
        verify(clienteRepository, times(1)).findById(clienteId);
        Assertions.assertEquals("Cliente com id " + clienteId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Retorna todos os clientes")
    void testGetAllSucesso() {
        List<ClienteModel> clienteModels = List.of(clienteModel);
        when(clienteRepository.findAll()).thenReturn(clienteModels);

        List<ClienteRecord> result = clienteService.getAll();

        verify(clienteRepository, times(1)).findAll();
        assertEquals(ClienteMapper.toRecordList(clienteModels), result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Retorna vazio quando os clientes não existem")
    void testGetAllVazio() {
        List<ClienteModel> clienteModels = new ArrayList<>();
        when(clienteRepository.findAll()).thenReturn(clienteModels);

        List<ClienteRecord> result = clienteService.getAll();

        verify(clienteRepository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Retorna o cliente pelo id")
    void testGetByIdSucesso() {
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteModel));

        ClienteRecord result = clienteService.getById(clienteId);

        verify(clienteRepository, times(1)).findById(clienteId);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Retorna não encontrado quando o cliente pelo id não existe")
    void testGetByIdNaoEncontrado() {
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clienteService.getById(clienteId);
        });

        verify(clienteRepository, times(1)).findById(clienteId);
        Assertions.assertEquals("Cliente com id " + clienteId + " não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Cria um novo cliente")
    void testSaveSucesso() {
        clienteModel.setId(null);

        // Arrange
        when(clienteRepository.save(clienteModel)).thenReturn(clienteModel);

        // Act
        clienteService.save(ClienteMapper.toRecord(clienteModel));

        // Assert
        verify(clienteRepository, times(1)).save(clienteModel);
    }

    @Test
    @DisplayName("Erro ao criar um novo cliente com id")
    void testSaveErroId() {
        // Arrange
        when(clienteRepository.save(clienteModel)).thenReturn(clienteModel);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clienteService.save(ClienteMapper.toRecord(clienteModel));
        });

        // Assert
        verify(clienteRepository, times(0)).save(clienteModel);
        Assertions.assertEquals("Id deve ser nulo para incluir um novo cliente!", exception.getMessage());
    }

    @Test
    @DisplayName("Atualiza um cliente")
    void testUpdateSucesso() {
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteModel));
        Mockito.when(clienteRepository.save(clienteModel)).thenReturn(clienteModel);

        ClienteRecord updatedRecord = clienteService.update(clienteId, ClienteMapper.toRecord(clienteModel));

        Mockito.verify(clienteRepository, Mockito.times(1)).save(clienteModel);
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Assertions.assertEquals(ClienteMapper.toRecord(clienteModel), updatedRecord);
    }

    @Test
    @DisplayName("Erro ao atualizar um cliente inexistente")
    void testUpdateNotFound() {
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clienteService.update(clienteId, any());
        });

        Mockito.verify(clienteRepository, Mockito.times(0)).save(any());
        Mockito.verify(clienteRepository, Mockito.times(1)).findById(clienteId);
        Assertions.assertEquals("Cliente com id " + clienteId + " não encontrado!", exception.getMessage());
    }
}
