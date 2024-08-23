package com.algaworks.algafood.domain.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class NotificacaoClientePedidoCanceladoListener {
	
	/*@Autowired
	private EnvioEmailService envioEmail;*/
	
	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoCanceladoEvent event) {
		
		Pedido pedido = event.getPedido();
		
		/*var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido Cancelado")
				.corpo("emails/pedido-cancelado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
				
		envioEmail.enviar(mensagem);*/
		
	}
}
