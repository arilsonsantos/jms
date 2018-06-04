package br.com.orion.jms;

import javax.ejb.Stateless;
import javax.jms.DeliveryMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless
public class EnviaMensagemService {

	public void enviarMensagem() {
		System.out.println("Rotina de envio de mensagens executado!");
		int limite = 200;
		long inicio = 1000000000;
		
		if (!JmsUtils.hasMoreMessages("testeQueue")) {
			log.info("<<<<<<<<<<<<<<< " + limite + " mensagens serão envidas para a fila >>>>>>>>>>>>>>>");
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (long i = inicio; i <= inicio + limite; i++) {
				JmsUtils.sendMessage("testeQueue", i, DeliveryMode.NON_PERSISTENT);
			}


		} else {
			log.info("****** Tentativa de envio de mensagens para a fila, mas o processamento da fila ainda náo terminou! ******");
		}
	}
}
