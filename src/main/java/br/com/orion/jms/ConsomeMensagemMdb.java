package br.com.orion.jms;

import java.util.Random;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import lombok.extern.slf4j.Slf4j;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/testeQueue"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "15") })
@Slf4j
public class ConsomeMensagemMdb implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			Random r = new Random();
			int numero = r.nextInt(5); 
			numero = numero * 1000;
			Thread.sleep(numero);
			Long id = message.getBody(Long.class);
			//String nome = message.getStringProperty("nome");
			log.info("Fim do processamento da mensagem : "  + id);
		} catch (InterruptedException | JMSException e) {
			e.printStackTrace();
		}
	}

}
