package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.apiopenapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exeption.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exeption.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exeption.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exeption.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi{

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll(); 
		
		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}
	
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping("/{cidadeId}")	
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId); 
		
		return cidadeModelAssembler.toModel(cidade);
	}
		
	@CheckSecurity.Cidades.PodeEditar
	@PostMapping
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {		
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidade = cadastroCidade.salvar(cidade); 
			
			CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
			
			return cidadeModel; 	
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}	
	}
		
	@CheckSecurity.Cidades.PodeEditar
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId, 			
			@RequestBody @Valid CidadeInput cidadeInput) {	
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			
			cidadeInputDisassembler.copyToDomainModel(cidadeInput, cidadeAtual);
			
			cidadeAtual = cadastroCidade.salvar(cidadeAtual);
			
			return cidadeModelAssembler.toModel(cidadeAtual);	
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}				
	}
		
	@CheckSecurity.Cidades.PodeEditar
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
		try {
			cadastroCidade.excluir(cidadeId);
			return ResponseEntity.noContent().build();			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());		
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}	
}
