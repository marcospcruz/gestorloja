package br.com.marcospcruz.gestorloja.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import br.com.marcospcruz.gestorloja.controller.LoginFacade;

public abstract class AbstractJFrame extends JFrame implements ActionListener, MouseListener {
	private LoginFacade loginFacade;

	public AbstractJFrame(String tituloJanela) {
		super(tituloJanela);
		setSize(configuraDimensaoJanela());

	}

	public AbstractJFrame(String string, LoginFacade loginFacade) {
		this(string);
		this.loginFacade = loginFacade;
	}

	protected Dimension configuraDimensaoJanela() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();

		return toolkit.getScreenSize();

	}

	public LoginFacade getLoginFacade() {
		return loginFacade;
	}

	public void setLoginFacade(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}

}
