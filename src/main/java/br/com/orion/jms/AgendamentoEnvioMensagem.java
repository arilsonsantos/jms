package br.com.orion.jms;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AgendamentoEnvioMensagem {

	@Inject
	EnviaMensagemService enviarMensagemService;
	
	//Para ativar o agendamento, descomente a linha abaixo
	//@Schedule(hour = "*", minute = "*", second = "*/15", persistent = false)
	public void enviarMensagem(){
		enviarMensagemService.enviarMensagem();
	}
}
