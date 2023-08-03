package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.domain.exeption.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exeption.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exeption.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exeption.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exeption.NegocioException;
import com.algaworks.algafood.domain.exeption.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	//@Autowired
	//private SmartValidator validator;
		
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping()
	public List<RestauranteModel> listar() {						
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNome() {						
		return listar();
	}
	
	/*@GetMapping()
	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
		List<Restaurante> restaurantes = restauranteRepository.findAll();		
		List<RestauranteModel> restauranteModels = restauranteModelAssembler.toCollectionModel(restaurantes);
		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restauranteModels);
		
		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
		
		if ("apenas-nome".equals(projecao)) {
			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
		} else if ("completo".equals(projecao)) {
			restaurantesWrapper.setSerializationView(null);
		}
		return restaurantesWrapper;
	}	
	
	@GetMapping("/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {		
		return restauranteRepository.queryByTaxaFreteBetween(taxaInicial, taxaFinal);
	}
	
	@GetMapping("/por-nome")
	public List<Restaurante> restaurantesPorNome(String nome, Long cozinhaId) {		
		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}
	
	@GetMapping("/primeiro-por-nome")
	public Optional<Restaurante> restaurantesPrimeiroPorNome(String nome) {		
		return restauranteRepository.findFirstRestauranteByNomeContaining(nome);
	}
	
	@GetMapping("/primeiro")
	public Optional<Restaurante> primeiroRestaurante() {		
		return restauranteRepository.buscarPrimeiro();
	}
	
	@GetMapping("/top2-por-nome")
	public List<Restaurante> restauranteTop2PorNome(String nome) {		
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
	@GetMapping("/count-por-cozinha")
	public int restauranteCountPorCozinha(Long cozinhaId) {		
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}
	
	@GetMapping("/por-nome-e-frete")
	public List<Restaurante> restaurantesPorNomeEFrete(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {		
		return restauranteRepository.find(nome, taxaInicial, taxaFinal);
	}
	
	@GetMapping("/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal) {				
		return restauranteRepository.findComfreteGratis(nome);
	}*/
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toModel(restaurante);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(
			@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));	
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}				
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, 
			@RequestBody @Valid RestauranteInput restauranteInput) {
		
		//Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);		
		
		restauranteInputDisassembler.copyToDomainObject(restauranteInput,restauranteAtual);
		
		//BeanUtils.copyProperties(restaurante, restauranteAtual,
		//		"id","formasPagamento","endereco","dataCadastro","produtos");
		try {
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));	
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}					
	}
	
	/*@PatchMapping("/{restauranteId}")
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {		
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);		
		merge(campos,restauranteAtual, request);
		
		validate(restauranteAtual, "restaurante");
		
		return atualizar(restauranteId, restauranteAtual);		
	}
	
	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}
	

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new  ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem,Restaurante.class);	
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field =  ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				//System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}*/
	
	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<?> remover(@PathVariable Long restauranteId) {
		try {
			cadastroRestaurante.excluir(restauranteId);
			return ResponseEntity.noContent().build();			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());		
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}	
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.ativar(restauranteIds);			
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}    	
    }
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.inativar(restauranteIds);			
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
    }
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
	    cadastroRestaurante.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
	    cadastroRestaurante.fechar(restauranteId);
	} 
}
