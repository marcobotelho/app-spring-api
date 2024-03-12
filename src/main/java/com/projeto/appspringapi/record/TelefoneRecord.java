package com.projeto.appspringapi.record;

import com.projeto.appspringapi.enums.TipoTelefoneEnum;

public record TelefoneRecord(Long id, String numero, TipoTelefoneEnum tipo, Long clienteId) {

}
