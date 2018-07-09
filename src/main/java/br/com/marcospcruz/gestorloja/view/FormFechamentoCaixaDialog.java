package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.Util;

public class FormFechamentoCaixaDialog extends AbstractDialog {

	private static final String SALDO_ABERTURA_CAIXA_TITLE = "Saldo Abertura Caixa";
	private static final String SALDO_ATUAL_CAIXA_TITLE = "Saldo atual do Caixa";
	private static final String ESPACOS_BRANCO = "                                              ";
	private CaixaController caixaController;
	private Usuario operador;
	private JLabel dataAberturaLbl;
	private Timer timer;
	private JLabel dataFechamentoLbl;

	public FormFechamentoCaixaDialog(JDialog owner, ControllerBase caixaController) {
		super(owner, "Fechamento do Caixa", true);
		this.caixaController = (CaixaController) caixaController;
		setSize(600, 650);

		operador = caixaController.getUsuarioLogado();

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(criaCenterPanel(), BorderLayout.CENTER);
		getContentPane().add(criaCommandPanel(), BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

	}

	private Component criaCenterPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new GridLayout(1, 1));

		panel.setBorder(criaTitledBorder("Fechamento do Caixa:"));
		p2.add(criaFormPanel());
		panel.add(p2, BorderLayout.NORTH);
		p2.setBorder(criaTitledBorder("Teste:"));
		panel.add(criaPnlRecebimentos(), BorderLayout.CENTER);
		return panel;
	}

	private Component criaPnlRecebimentos() {
		JPanel pnlRecebimentos = new JPanel(new GridLayout(1, 1));
		pnlRecebimentos.setBorder(criaTitledBorder("Rebimentos:"));
		jPanelTable = new JPanel(new GridLayout());
		pnlRecebimentos.add(jPanelTable);
		carregaTableModel();
		return pnlRecebimentos;
	}

	private JPanel criaCommandPanel() {

		JPanel commandPanel = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;

		commandPanel.add(inicializaJButton("Fechar Caixa"));

		return commandPanel;

	}

	private JPanel criaFormPanel() {
		Caixa caixa = (Caixa) caixaController.getItem();
		initTimer();

		JPanel panel = new JPanel(new GridLayout(4, 2));

		dataAberturaLbl = criaJLabel(Util.formataData(caixa.getDataAbertura()));
		dataAberturaLbl.setBorder(criaTitledBorder("Data de Abertura:"));

		JLabel lblUsuarioAberturaCaixa = criaJLabel(operador.getNomeCompleto());
		lblUsuarioAberturaCaixa.setBorder(criaTitledBorder("Usuário Abertura:"));
		panel.add(dataAberturaLbl);
		panel.add(lblUsuarioAberturaCaixa);

		JLabel lblSaldoDoCaixa = criaJLabel(Util.formataMoeda(caixa.getSaldoInicial()));
		lblSaldoDoCaixa.setBorder(criaTitledBorder(SALDO_ABERTURA_CAIXA_TITLE));
		panel.add(lblSaldoDoCaixa);

		JLabel lblSaldoAtualDoCaixa = criaJLabel(Util.formataMoeda(caixa.getSaldoFinal()));
		lblSaldoAtualDoCaixa.setBorder(criaTitledBorder(SALDO_ATUAL_CAIXA_TITLE));
		panel.add(lblSaldoAtualDoCaixa);

		panel.add(lblSaldoAtualDoCaixa);

		JLabel lblSubTotalVenda = criaJLabel(Util.formataMoeda(caixaController.getSubTotalVendas()));
		lblSubTotalVenda.setBorder(criaTitledBorder("Total Vendido:"));
		panel.add(lblSubTotalVenda);

		// panel.add(pnlRecebimentos);

		dataFechamentoLbl = criaJLabel(Util.formataDataAtual());
		dataFechamentoLbl.setBorder(criaTitledBorder("Data de Fechamento:"));
		panel.add(dataFechamentoLbl);

		JLabel lblUsuarioLogado = criaJLabel(operador.getNomeCompleto());
		lblUsuarioLogado.setBorder(criaTitledBorder("Usuário Fechamento:"));
		panel.add(lblUsuarioLogado);

		// getContentPane().add(panel, BorderLayout.CENTER);
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
	protected JPanel carregaJpanelTable(int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaTableModel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaTableModel() {
		List linhas = caixaController.controcalculaSubTotalRecebido();
		super.carregaTableModel(linhas, new Object[] { "Meio Pagamento", "Valor Recebido" });

		jTable = inicializaJTable(myTableModel);
		if (jScrollPane == null)
			jScrollPane = new JScrollPane(jTable);

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTable.remove(jScrollPane);

		//

		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setFillsViewportHeight(true);
		//

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTable.add(jScrollPane);

		jPanelTable.repaint();

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
