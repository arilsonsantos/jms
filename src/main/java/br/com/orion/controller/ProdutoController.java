package br.com.orion.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.orion.jms.EnviaMensagemService;

@ViewScoped
@Named
public class ProdutoController implements Serializable {

	private static final long serialVersionUID = -5664884577446140764L;

	@Inject
	private EnviaMensagemService enviaMensagemService;

	public void enviaMessage() {

		enviaMensagemService.enviarMensagem();

	}

}
