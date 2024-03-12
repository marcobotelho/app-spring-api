package com.projeto.appspringapi.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.projeto.appspringapi.model.PerfilModel;
import com.projeto.appspringapi.record.PerfilRecord;

public class PerfilMapper {

    public static PerfilRecord toRecord(PerfilModel model) {
        return new PerfilRecord(model.getId(), model.getNome(), model.getDescricao());
    }

    public static PerfilModel toModel(PerfilRecord record) {
        return new PerfilModel(record.id(), record.nome(), record.descricao());
    }

    public static List<PerfilModel> toModelList(List<PerfilRecord> records) {
        return records.stream().map(PerfilMapper::toModel).collect(Collectors.toList());
    }

    public static List<PerfilRecord> toRecordList(List<PerfilModel> models) {
        return models.stream().map(PerfilMapper::toRecord).collect(Collectors.toList());
    }

}
