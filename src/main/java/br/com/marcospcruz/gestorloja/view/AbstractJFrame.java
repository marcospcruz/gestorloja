package br.com.marcospcruz.gestorloja.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public abstract class AbstractJFrame extends JFrame implements MyWindowAction {

	public AbstractJFrame(String tituloJanela) {
		super(tituloJanela);
		setSize(configuraDimensaoJanela());

	}

	protected Dimension configuraDimensaoJanela() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();

		return toolkit.getScreenSize();

	}

	protected Usuario getUsuarioLogado() {
		return SingletonManager.getInstance().getUsuarioLogado();
	}
}
