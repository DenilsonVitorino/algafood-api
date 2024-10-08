package com.algaworks.algafood.domain.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class NotificacaoClientePedidoConfirmadoListener {
	
	/*@Autowired 
	private EnvioEmailService envioEmail;*/
	
	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		
		Pedido pedido = event.getPedido();
		
		/*var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido Confirmado")
				.corpo("emails/pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
				
		envioEmail.enviar(mensagem);*/
		
	}

}
