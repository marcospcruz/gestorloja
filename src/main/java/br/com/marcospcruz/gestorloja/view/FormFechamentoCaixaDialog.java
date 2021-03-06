package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
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
import br.com.marcospcruz.gestorloja.model.Usuario;
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
	private JLabel lblSaldoDoCaixa;
	private JLabel lblSaldoAtualDoCaixa;

	public FormFechamentoCaixaDialog(JDialog owner, ControllerBase caixaController) {
		super(owner, "Fechamento do Caixa", true);
		this.caixaController = (CaixaController) caixaController;
		setSize(800, 1050);

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
		pnlRecebimentos.setBorder(criaTitledBorder("Recebimentos:"));
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

		initTimer();

		JPanel panel = new JPanel(new GridLayout(4, 2));

		dataAberturaLbl = criaJLabel("");
		dataAberturaLbl.setBorder(criaTitledBorder("Data de Abertura:"));

		JLabel lblUsuarioAberturaCaixa = criaJLabel(operador.getNomeCompleto());
		lblUsuarioAberturaCaixa.setBorder(criaTitledBorder("Usu�rio Abertura:"));
		panel.add(dataAberturaLbl);
		panel.add(lblUsuarioAberturaCaixa);

		lblSaldoDoCaixa = criaJLabel("");
		lblSaldoDoCaixa.setBorder(criaTitledBorder(SALDO_ABERTURA_CAIXA_TITLE));
		panel.add(lblSaldoDoCaixa);

		lblSaldoAtualDoCaixa = criaJLabel("");
		lblSaldoAtualDoCaixa.setBorder(criaTitledBorder(SALDO_ATUAL_CAIXA_TITLE));
		panel.add(lblSaldoAtualDoCaixa);

		panel.add(lblSaldoAtualDoCaixa);
		JPanel pnlTotalVenda = criaPnlVendas();
		panel.add(pnlTotalVenda);

		// panel.add(pnlRecebimentos);

		dataFechamentoLbl = criaJLabel(Util.formataDataAtual());
		dataFechamentoLbl.setBorder(criaTitledBorder("Data de Fechamento:"));
		panel.add(dataFechamentoLbl);

		JLabel lblUsuarioLogado = criaJLabel(operador.getNomeCompleto());
		lblUsuarioLogado.setBorder(criaTitledBorder("Usu�rio Fechamento:"));
		panel.add(lblUsuarioLogado);
		populaFormulario();
		// getContentPane().add(panel, BorderLayout.CENTER);
		return panel;
	}

	private JPanel criaPnlVendas() {
		JPanel pnlTotalVenda = new JPanel(new GridLayout(2, 1));
		pnlTotalVenda.setBorder(criaTitledBorder("Vendas do Dia"));
		JLabel lblSubTotalVenda = criaJLabel(Util.formataMoeda(caixaController.getSubTotalVendas()));
		lblSubTotalVenda.setBorder(criaTitledBorder("Total Vendido:"));
		pnlTotalVenda.add(lblSubTotalVenda);
		JPanel pnlButton = new JPanel();
		JButton btnVendas = inicializaJButton("Visualizar Vendas");
		pnlButton.add(btnVendas);
		pnlTotalVenda.add(pnlButton);
		return pnlTotalVenda;
	}

	private void initTimer() {
		timer = new Timer(1000, this);
		timer.start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() != null) {
			String actionCommand = e.getActionCommand();
			switch (actionCommand) {
			case "Fechar Caixa":
				try {
					fechaCaixa();
				} catch (Exception e1) {

					e1.printStackTrace();
				}

				dispose();
				return;
			case "Visualizar Vendas":
				try {
					new VendasDiaDialog(this, "Vendas realizadas em "
							+ Util.formataData(caixaController.getUltimoCaixa().getDataAbertura()), true);
				} catch (Exception e1) {

					showErrorMessage(this, e1.getMessage());
				}
			}
			populaFormulario();
			carregaTableModel();
			repaint();
		}

		atualizaDataFechamento();
	}

	private void fechaCaixa() {
		String message = null;

		try {
			((CaixaController) caixaController).fechaCaixa();

			message = "Caixa fechado com sucesso!";
			super.exibeMensagemSucesso(this, message);
		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			
		}

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
		
		@SuppressWarnings("rawtypes")
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
		Caixa caixa = (Caixa) caixaController.getItem();
		dataAberturaLbl.setText(Util.formataData(caixa.getDataAbertura()));
		lblSaldoDoCaixa.setText(Util.formataMoeda(caixa.getSaldoInicial()));
		lblSaldoAtualDoCaixa.setText(Util.formataMoeda(caixa.getSaldoFinal()));
	}

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub

	}

}
