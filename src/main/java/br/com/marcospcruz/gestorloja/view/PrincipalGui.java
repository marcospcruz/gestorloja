package br.com.marcospcruz.gestorloja.view;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import br.com.marcospcruz.gestorloja.abstractfactory.CommandFactory;
import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class PrincipalGui extends AbstractJFrame implements WindowListener {

	/**
	 * @param loginFacade
	 */
	public PrincipalGui(LoginFacade loginFacade) {
		super("Gestor Loja", loginFacade);
		addWindowListener(this);

		carregaBotoesModulos(loginFacade);

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void carregaBotoesModulos(LoginFacade loginFacade) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Módulos"));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//@formatter:off
		loginFacade
			.getUsuarioLogado()
			.getPerfisUsuario()
			.stream()
			.forEach(perfilUsuario->{
				perfilUsuario.getInterfaces()
					.stream()
					.forEach(componente->{
						CommandFactory factory=new CommandFactory();
					
						try {
							JButton button=factory.createButton(componente.getNomeModulo(),this);
							panel.add(button);
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							
							e.printStackTrace();
						}
					
					});
		});
		//@formatter:on
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
		switch (e.getActionCommand()) {
		case InterfaceGrafica.ESTOQUE:
			new EstoquePrincipalGui(InterfaceGrafica.ESTOQUE, this);
			break;
		case InterfaceGrafica.CONTROLE_DE_CAIXA:
			
				new ControleCaixaGui(getLoginFacade(),InterfaceGrafica.CONTROLE_DE_CAIXA,this);
			

		}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		getLoginFacade().fechaSessaoUsuario();

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
