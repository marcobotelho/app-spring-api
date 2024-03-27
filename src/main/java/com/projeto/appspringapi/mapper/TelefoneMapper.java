package com.projeto.appspringapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.projeto.appspringapi.model.ClienteModel;
import com.projeto.appspringapi.model.TelefoneModel;
import com.projeto.appspringapi.record.TelefoneRecord;

public class TelefoneMapper {

    public static TelefoneRecord toRecord(TelefoneModel model) {
        return new TelefoneRecord(model.getId(), model.getNumero(), model.getTipo(), model.getCliente().getId());
    }

    public static TelefoneModel toModel(TelefoneRecord record) {
        ClienteModel clienteModel = new ClienteModel();
        clienteModel.setId(record.clienteId());
        return new TelefoneModel(record.id(), record.numero(), record.tipo(), clienteModel);
    }

    public static List<TelefoneModel> toModelList(List<TelefoneRecord> records, ClienteModel clienteModel) {
        return records.stream().map(record -> toModel(record)).collect(Collectors.toList());
    }

    public static List<TelefoneRecord> toRecordList(List<TelefoneModel> models) {
        return models.stream().map(TelefoneMapper::toRecord).collect(Collectors.toList());
    }
}
