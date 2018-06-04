package br.com.orion.jms;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AgendamentoEnvioMensagem {

	@Inject
	EnviaMensagemService enviarMensagemService;
	
	public void enviarMensagem(){
		enviarMensagemService.enviarMensagem();
	}
}
