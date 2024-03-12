package com.projeto.appspringapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.projeto.appspringapi.model.TelefoneModel;
import com.projeto.appspringapi.record.TelefoneRecord;
import com.projeto.appspringapi.repository.ClienteRepository;

public class TelefoneMapper {

    @Autowired
    private static ClienteRepository clienteRepository;

    public static TelefoneRecord toRecord(TelefoneModel model) {
        return new TelefoneRecord(model.getId(), model.getNumero(), model.getTipo(), model.getCliente().getId());
    }

    public static TelefoneModel toModel(TelefoneRecord record) {
        return new TelefoneModel(record.id(), record.numero(), record.tipo(),
                clienteRepository.getReferenceById(record.clienteId()));
    }

    public static List<TelefoneModel> toModelList(List<TelefoneRecord> records) {
        return records.stream().map(TelefoneMapper::toModel).collect(Collectors.toList());
    }

    public static List<TelefoneRecord> toRecordList(List<TelefoneModel> models) {
        return models.stream().map(TelefoneMapper::toRecord).collect(Collectors.toList());
    }
}
