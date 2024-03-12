package com.projeto.appspringapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.record.ClienteRecord;

public class ClienteMapper {

    public static ClienteModel toModel(ClienteRecord record) {
        return new ClienteModel(record.id(), record.nome(), record.email(), record.idade(),
                record.dataNascimento(), record.cep(), record.endereco(), record.bairro(), record.municipio(),
                record.estado());
    }

    public static ClienteRecord toRecord(ClienteModel model) {
        return new ClienteRecord(model.getId(), model.getNome(), model.getEmail(), model.getIdade(),
                model.getDataNascimento(), model.getCep(), model.getEndereco(), model.getBairro(), model.getMunicipio(),
                model.getEstado());
    }

    public static List<ClienteModel> toModelList(List<ClienteRecord> records) {
        return records.stream().map(ClienteMapper::toModel).collect(Collectors.toList());
    }

    public static List<ClienteRecord> toRecordList(List<ClienteModel> models) {
        return models.stream().map(ClienteMapper::toRecord).collect(Collectors.toList());
    }

}
