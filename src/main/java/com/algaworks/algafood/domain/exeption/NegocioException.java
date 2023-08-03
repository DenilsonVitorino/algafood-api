package com.algaworks.algafood.domain.exeption;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST) //, reason = "Entidade não encontrada")
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
