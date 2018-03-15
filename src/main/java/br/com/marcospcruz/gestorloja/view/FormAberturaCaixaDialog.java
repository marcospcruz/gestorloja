package br.com.marcospcruz.gestorloja.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.abstractfactory.CommandFactory;
import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.AbstractController;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.util.NumberDocument;
import br.com.marcospcruz.gestorloja.util.Util;

public class FormAberturaCaixaDialog extends JDialog implements ActionListener, WindowListener {
	private JFormattedTextField saldoTextField;
	private AbstractController caixaController;
	private Usuario operador;
	private JLabel dataAberturaLbl;
	private Timer timer;

	public FormAberturaCaixaDialog(JDialog owner, AbstractController caixaController) {
		super(owner, true);
		this.caixaController = caixaController;
		setSize(400, 600);

		operador = caixaController.getLoginFacade().getUsuarioLogado();

		criaFormPanel();

		try {

			criaCommandPanel();

			// carregaController();

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(true);

		} catch (Exception e) {

			e.printStackTrace();
			showMessage(e.getMessage());
		}
	}

	private void criaCommandPanel() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		CommandFactory factory = new CommandFactory();
		JPanel commandPanel = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		getContentPane().add(commandPanel, gbc_panel_1);

		commandPanel.add(factory.createButton("Abrir Caixa", this));
	}

	private void criaFormPanel() {
		initTimer();
		Caixa ultimoCaixa = ((CaixaController) caixaController).getUltimoCaixa();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Abertura Caixa"));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		dataAberturaLbl = new JLabel(Util.formataDataAtual());
		dataAberturaLbl.setBorder(new TitledBorder("Data de Abertura"));
		GridBagConstraints gbc_dataAberturaLbl = new GridBagConstraints();
		gbc_dataAberturaLbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataAberturaLbl.insets = new Insets(0, 0, 5, 5);
		gbc_dataAberturaLbl.gridx = 0;
		gbc_dataAberturaLbl.gridy = 0;
		panel.add(dataAberturaLbl, gbc_dataAberturaLbl);

		JLabel lblUsuarioLogado = new JLabel(operador.getNomeCompleto());
		lblUsuarioLogado.setBorder(new TitledBorder("Caixa aberto por:"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblUsuarioLogado, gbc_lblNewLabel);

		JLabel lblSaldoDoCaixa = new JLabel("Saldo do Caixa");
		GridBagConstraints gbc_lblSaldoDoCaixa = new GridBagConstraints();
		gbc_lblSaldoDoCaixa.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaldoDoCaixa.anchor = GridBagConstraints.WEST;
		gbc_lblSaldoDoCaixa.gridx = 0;
		gbc_lblSaldoDoCaixa.gridy = 1;
		panel.add(lblSaldoDoCaixa, gbc_lblSaldoDoCaixa);

//		String saldoFinal = "500";//ultimoCaixa != null ? ultimoCaixa.getSaldoFinal().toString() : "";
		saldoTextField = new JFormattedTextField();
		saldoTextField.setDocument(new NumberDocument(true));
		
		saldoTextField.setColumns(10);
		saldoTextField.setValue(ultimoCaixa != null ? ultimoCaixa.getSaldoFinal() : "");
		GridBagConstraints gbc_lbl_data = new GridBagConstraints();
		gbc_lbl_data.anchor = GridBagConstraints.WEST;
		gbc_lbl_data.gridwidth = 2;
		gbc_lbl_data.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_data.gridx = 1;
		gbc_lbl_data.gridy = 1;
		panel.add(saldoTextField, gbc_lbl_data);

	}

	private void initTimer() {
		timer = new Timer(1000, this);
		timer.start();

	}

	private void carregaController() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		caixaController = ControllerAbstractFactory.createController(ControllerAbstractFactory.CONTROLE_CAIXA);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() != null)
			switch (e.getActionCommand()) {
			case "Abrir Caixa":
				try {
					abreCaixa();
				} catch (Exception e1) {

					e1.printStackTrace();
				}

				dispose();
				return;
			case "Fechar Caixa":
				System.out.println();
			}

		atualizaDataAbertura();
	}

	private void abreCaixa() {

		String message = null;
		try {
			((CaixaController) caixaController).abreCaixa(saldoTextField.getValue().toString());
			message = "Caixa aberto com sucesso!";

		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			showMessage(message);
		}

	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);

	}

	private void atualizaDataAbertura() {
		dataAberturaLbl.setText(Util.formataDataAtual());
		repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.out.println("closed");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {

		timer.stop();

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
