package com.algaworks.algafood.core.jackson;

import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.mixin.CozinhaMixin;
import com.algaworks.algafood.api.v1.model.mixin.GrupoMixin;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Grupo;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class JacksonMixinModule extends SimpleModule {

	private static final long serialVersionUID = 1L;
	
	public JacksonMixinModule() {
		//setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
		setMixInAnnotation(Cozinha.class, CozinhaMixin.class);		
		//setMixInAnnotation(Cidade.class, CidadeMixin.class);
		setMixInAnnotation(Grupo.class, GrupoMixin.class);
	}	
}
