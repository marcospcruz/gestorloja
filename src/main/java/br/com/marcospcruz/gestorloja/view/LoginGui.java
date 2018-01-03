package br.com.marcospcruz.gestorloja.view;

import javax.swing.JDialog;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

import br.com.marcospcruz.gestorloja.controller.LoginFacade;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class LoginGui extends JDialog implements ActionListener {
	private JTextField txtUsuario;
	private JPasswordField passwordField;

	public LoginGui() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 60, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 41, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblUsurio = new JLabel("Usu\u00E1rio:");
		lblUsurio.setFont(new Font("Dialog", Font.BOLD, 19));
		GridBagConstraints gbc_lblUsurio = new GridBagConstraints();
		gbc_lblUsurio.anchor = GridBagConstraints.EAST;
		gbc_lblUsurio.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsurio.gridx = 4;
		gbc_lblUsurio.gridy = 2;
		getContentPane().add(lblUsurio, gbc_lblUsurio);

		txtUsuario = new JTextField();
		txtUsuario.setFont(new Font("Dialog", Font.PLAIN, 19));
		GridBagConstraints gbc_txtUsuario = new GridBagConstraints();
		gbc_txtUsuario.anchor = GridBagConstraints.WEST;
		gbc_txtUsuario.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsuario.gridx = 5;
		gbc_txtUsuario.gridy = 2;
		getContentPane().add(txtUsuario, gbc_txtUsuario);
		txtUsuario.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 19));
		GridBagConstraints gbc_lblSenha = new GridBagConstraints();
		gbc_lblSenha.anchor = GridBagConstraints.EAST;
		gbc_lblSenha.insets = new Insets(0, 0, 5, 5);
		gbc_lblSenha.gridx = 4;
		gbc_lblSenha.gridy = 3;
		getContentPane().add(lblSenha, gbc_lblSenha);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Dialog", Font.PLAIN, 19));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 0);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 5;
		gbc_passwordField.gridy = 3;
		getContentPane().add(passwordField, gbc_passwordField);

		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(this);
		GridBagConstraints gbc_btnLogIn = new GridBagConstraints();
		gbc_btnLogIn.anchor = GridBagConstraints.EAST;
		gbc_btnLogIn.insets = new Insets(0, 0, 5, 0);
		gbc_btnLogIn.gridx = 5;
		gbc_btnLogIn.gridy = 4;
		getContentPane().add(btnLogIn, gbc_btnLogIn);

	}

	public LoginGui(String title) {
		this();
		setTitle(title);
		setSize(400, 200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LoginFacade controller = new LoginFacade(this);
		String usuario = txtUsuario.getText();
		String senha = new String(passwordField.getPassword());

		try {
			controller.processaLogin(usuario, senha);
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);

		}
	}

}
