package br.com.marcospcruz.gestorloja.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

import javax.swing.JDialog;
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
import br.com.marcospcruz.gestorloja.util.Util;

public class FormFechamentoCaixaDialog extends JDialog implements ActionListener, WindowListener {

	private static final String SALDO_ABERTURA_CAIXA_TITLE = "Saldo Abertura Caixa";
	private static final String SALDO_ATUAL_CAIXA_TITLE = "Saldo atual do Caixa";
	private static final String ESPACOS_BRANCO = "                                              ";
	private AbstractController caixaController;
	private Usuario operador;
	private JLabel dataAberturaLbl;
	private Timer timer;
	private JLabel dataFechamentoLbl;

	public FormFechamentoCaixaDialog(JDialog owner, AbstractController caixaController) {
		super(owner, true);
		this.caixaController = caixaController;
		setSize(400, 600);

		operador = caixaController.getUsuarioLogado();

		criaFormPanel();

		try {

			criaCommandPanel();

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

		commandPanel.add(factory.createButton("Fechar Caixa", this));
	}

	private void criaFormPanel() {
		Caixa caixa = (Caixa) caixaController.getItem();
		initTimer();
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

		dataAberturaLbl = new JLabel(Util.formataData(caixa.getDataAbertura()));
		dataAberturaLbl.setBorder(new TitledBorder("Data de Abertura"));
		GridBagConstraints gbc_dataAberturaLbl = new GridBagConstraints();
		gbc_dataAberturaLbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataAberturaLbl.insets = new Insets(0, 0, 5, 5);
		gbc_dataAberturaLbl.gridx = 0;
		gbc_dataAberturaLbl.gridy = 0;
		panel.add(dataAberturaLbl, gbc_dataAberturaLbl);

		JLabel lblUsuarioAberturaCaixa = new JLabel(operador.getNomeCompleto());
		lblUsuarioAberturaCaixa.setBorder(new TitledBorder("Caixa aberto por:"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblUsuarioAberturaCaixa, gbc_lblNewLabel);

		JLabel lblSaldoDoCaixa = new JLabel(Util.formataMoeda(caixa.getSaldoInicial()));
		lblSaldoDoCaixa.setBorder(new TitledBorder(SALDO_ABERTURA_CAIXA_TITLE));

		GridBagConstraints gbc_lblSaldoDoCaixa = new GridBagConstraints();
		gbc_lblSaldoDoCaixa.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaldoDoCaixa.anchor = GridBagConstraints.WEST;
		gbc_lblSaldoDoCaixa.gridx = 0;
		gbc_lblSaldoDoCaixa.gridy = 1;
		panel.add(lblSaldoDoCaixa, gbc_lblSaldoDoCaixa);

		JLabel lblSaldoAtualDoCaixa = new JLabel(Util.formataMoeda(caixa.getSaldoFinal()) + ESPACOS_BRANCO);
		lblSaldoAtualDoCaixa.setBorder(new TitledBorder(SALDO_ATUAL_CAIXA_TITLE));

		GridBagConstraints gbc_lblSaldoAtualDoCaixa = new GridBagConstraints();
		gbc_lblSaldoAtualDoCaixa.anchor = GridBagConstraints.WEST;
		gbc_lblSaldoAtualDoCaixa.gridwidth = 2;
		gbc_lblSaldoAtualDoCaixa.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaldoAtualDoCaixa.gridx = 1;
		gbc_lblSaldoAtualDoCaixa.gridy = 1;
		panel.add(lblSaldoAtualDoCaixa, gbc_lblSaldoAtualDoCaixa);
		gbc_dataAberturaLbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataAberturaLbl.insets = new Insets(0, 0, 5, 5);
		gbc_dataAberturaLbl.gridx = 0;
		gbc_dataAberturaLbl.gridy = 2;

		dataFechamentoLbl = new JLabel(Util.formataDataAtual());
		dataFechamentoLbl.setBorder(new TitledBorder("Data de Fechamento"));
		GridBagConstraints gbc_dataFechamentoLbl = new GridBagConstraints();
		gbc_dataFechamentoLbl.anchor = GridBagConstraints.WEST;
		gbc_dataFechamentoLbl.gridx = 0;
		gbc_dataFechamentoLbl.gridy = 2;
		gbc_dataFechamentoLbl.insets = new Insets(0, 0, 5, 5);
		// gbc_dataFechamentoLbl.gridx = 0;
		// gbc_dataFechamentoLbl.gridy = 2;
		panel.add(dataFechamentoLbl, gbc_dataFechamentoLbl);
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);

		JLabel lblUsuarioLogado = new JLabel(operador.getNomeCompleto());
		lblUsuarioLogado.setBorder(new TitledBorder("Fechamento caixa efetuado por:"));
		GridBagConstraints gbc_lblUsuarioLogado = new GridBagConstraints();
		gbc_lblUsuarioLogado.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsuarioLogado.gridx = 1;
		gbc_lblUsuarioLogado.gridy = 2;

		panel.add(lblUsuarioLogado, gbc_lblUsuarioLogado);
	}

	private void initTimer() {
		timer = new Timer(1000, this);
		timer.start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() != null)
			switch (e.getActionCommand()) {
			case "Fechar Caixa":
				try {
					fechaCaixa();
				} catch (Exception e1) {

					e1.printStackTrace();
				}

				dispose();
				return;
			}

		atualizaDataFechamento();
	}

	private void fechaCaixa() {
		String message = null;

		try {
			((CaixaController) caixaController).fechaCaixa();

			message = "Caixa fechado com sucesso!";

		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			showMessage(message);
		}

	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);

	}

	private void atualizaDataFechamento() {
		dataFechamentoLbl.setText(Util.formataDataAtual());
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
