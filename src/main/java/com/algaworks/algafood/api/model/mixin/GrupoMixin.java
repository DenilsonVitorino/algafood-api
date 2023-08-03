package com.algaworks.algafood.api.model.mixin;

import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Permissao;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class GrupoMixin {

	@JsonIgnore
	private List<Permissao> permissaos = new ArrayList<>();
}
