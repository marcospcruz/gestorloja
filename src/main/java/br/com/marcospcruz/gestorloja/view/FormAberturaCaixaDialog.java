package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.util.Util;

public class FormAberturaCaixaDialog extends AbstractDialog implements WindowListener {
	private JFormattedTextField saldoTextField;
	private CaixaController caixaController;
	private Usuario operador;
	private JLabel dataAberturaLbl;
	private Timer timer;

	public FormAberturaCaixaDialog(JDialog owner, ControllerBase caixaController) {
		super(owner, "Abertura do Caixa", true);
		this.caixaController = (CaixaController) caixaController;
		setSize(600, 650);

		operador = caixaController.getUsuarioLogado();

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(criaFormPanel(), BorderLayout.CENTER);
		getContentPane().add(criaCommandPanel(), BorderLayout.SOUTH);

		// criaFormPanel();

		// criaCommandPanel();

		// carregaController();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

	}

	private JPanel criaCommandPanel() {

		JPanel commandPanel = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		// getContentPane().add(commandPanel, gbc_panel_1);

		commandPanel.add(inicializaJButton("Abrir Caixa"));
		return commandPanel;
	}

	private JPanel criaFormPanel() {
		initTimer();
		int i = 0;
		Caixa ultimoCaixa = caixaController.getUltimoCaixa();
		Component[] componentes = new Component[3];
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.setBorder(super.criaTitledBorder("Abertura Caixa"));

		dataAberturaLbl = super.criaJLabel(Util.formataDataAtual());
		dataAberturaLbl.setBorder(super.criaTitledBorder("Data de Abertura"));
		componentes[i++] = dataAberturaLbl;
		JLabel lblUsuarioLogado = super.criaJLabel(operador.getNomeCompleto());
		lblUsuarioLogado.setBorder(super.criaTitledBorder("Usuário Abertura:"));
		componentes[i++] = lblUsuarioLogado;
		JPanel pnlSaldoDoCaixa = new JPanel();
		pnlSaldoDoCaixa.setBorder(criaTitledBorder("Saldo do Caixa"));

		// String saldoFinal = "500";//ultimoCaixa != null ?
		// ultimoCaixa.getSaldoFinal().toString() : "";
		saldoTextField = super.inicializaDecimalNumberField();

		saldoTextField.setValue(Util.formataStringDecimais(ultimoCaixa != null ? ultimoCaixa.getSaldoFinal() : 0f));
		pnlSaldoDoCaixa.add(saldoTextField);
		componentes[i++] = pnlSaldoDoCaixa;
		for (Component componente : componentes) {
			panel.add(componente);
		}

		return panel;

	}

	private void initTimer() {
		timer = new Timer(1000, this);
		timer.start();

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
				System.out.println("Fechar caixa!");
			}

		atualizaDataAbertura();
	}

	private void abreCaixa() {

		String message = null;
		try {
			caixaController.abreCaixa(saldoTextField.getValue().toString());
			message = "Caixa aberto com sucesso!";

		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			showMessage(this, message);
		}

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

	@Override
	protected void configuraJPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJPanelBusca() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void selecionaAcao(String actionCommand) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJpanelFormulario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel carregaJpanelTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaTableModel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaTableModel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List parseListToLinhasTableModel(List lista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void excluiItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaFormulario() {
		// TODO Auto-generated method stub

	}

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub

	}

}
