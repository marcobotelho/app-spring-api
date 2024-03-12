package com.projeto.appspringapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.projeto.appspringapi.model.UsuarioModel;
import com.projeto.appspringapi.record.UsuarioRecord;

public class UsuarioMapper {

    public static UsuarioRecord toRecord(UsuarioModel model) {
        return new UsuarioRecord(model.getId(), model.getNome(), model.getEmail());
    }

    public static UsuarioModel toModel(UsuarioRecord record) {
        return new UsuarioModel(record.id(), record.nome(), record.email());
    }

    public static List<UsuarioModel> toModelList(List<UsuarioRecord> records) {
        return records.stream().map(UsuarioMapper::toModel).collect(Collectors.toList());
    }

    public static List<UsuarioRecord> toRecordList(List<UsuarioModel> models) {
        return models.stream().map(UsuarioMapper::toRecord).collect(Collectors.toList());
    }
}
