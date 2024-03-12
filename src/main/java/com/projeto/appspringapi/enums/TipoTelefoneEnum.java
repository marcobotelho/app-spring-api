package com.projeto.appspringapi.enums;

public enum TipoTelefoneEnum {

	RESIDENCIAL("Residencial"), COMERCIAL("Comercial"), CELULAR("Celular");

	private final String displayName;

	TipoTelefoneEnum(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

}
