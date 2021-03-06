package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.Util;

public class ControleCaixaGui extends AbstractDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7955099211492942797L;
	private static final String ABRIR_CAIXA = "Abrir Caixa";
	private static final String FECHAR_CAIXA = "Fechar Caixa";
	private CaixaController controller;
	//@formatter:off
	private static final Object[] COLUNAS_JTABLE = new Object[] { 
			"Abertura", 
			"Operador Abertura", 
			"Saldo Abertura",
			"Total Vendido",
			//"Produtos Vendidos",
			//"Recebimento Dinheiro",
			//"Recebimento D�bito",
			//"Recebimento Cr�dito",
			//"Outros Recebimentos",
			"Saldo Fechamento", 
			"Fechamento", 
			"Operador Fechamento",
			"Status Caixa"};
	//@formatter:on
	private JPanel jpanel;

	public ControleCaixaGui(String tituloJanela, JFrame owner) throws Exception {

		super(owner, tituloJanela, ControllerAbstractFactory.CONTROLE_CAIXA, true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(configuraOperacoesPanel(), BorderLayout.NORTH);
		this.controller = (CaixaController) super.getCaixaController();
		atualizaJTable();

		setVisible(true);

	}

	private void atualizaJTable() {
		if (jpanel == null) {
			jpanel = new JPanel();
			// jpanel.setBorder(new TitledBorder("xxxxxxxxxxxx"));
			jpanel.setSize(new Dimension(getWidth(), getHeight()));
			jpanel.setLayout(new BorderLayout());

		} else {
			jpanel.remove(jScrollPane);
			jpanel.repaint();
		}

		carregaTableModel();
		jTable = inicializaJTable(myTableModel);

		// JScrollPane
		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setSize(jpanel.getWidth() - 15, jpanel.getHeight() - 20);

		jpanel.add(jScrollPane, BorderLayout.CENTER);

		getContentPane().add(jpanel, BorderLayout.CENTER);

		repaint();
	}

	// JTable inicializaJTable() {
	//
	// JTable jTable = new JTable(myTableModel);
	//
	// jTable.addMouseListener(this);
	//
	// DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
	//
	// direita.setHorizontalAlignment(SwingConstants.RIGHT);
	//
	// jTable.getColumnModel().getColumn(0).setCellRenderer(direita);
	//
	// jTable.getColumnModel().getColumn(4).setCellRenderer(direita);
	//
	// jTable.getColumnModel().getColumn(5).setCellRenderer(direita);
	//
	// // jTable.getColumnModel().getColumn(6).setCellRenderer(direita);
	//
	// return jTable;
	//
	// }

	private JPanel configuraOperacoesPanel()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(inicializaJButton("Abrir Caixa"));
		panel.add(inicializaJButton("Fechar Caixa"));
		panel.setBorder(criaTitledBorder("Opera��es"));
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			switch (arg0.getActionCommand()) {
			case ABRIR_CAIXA:

				controller.validateCaixaAberto();
				new FormAberturaCaixaDialog(this, controller);

				break;
			case FECHAR_CAIXA:
				controller.validateCaixaFechado();

				new FormFechamentoCaixaDialog(this, controller);
			}
		} catch (Exception e) {

			e.printStackTrace();
			showMessage(this, e.getMessage());
		} finally {
			controller.setList(null);
			// atualizaTableModel(null);
			atualizaJTable();
		}

	}

	@Override
	protected void configuraJPanel() {

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

		List linhas = new ArrayList();
		try {
			linhas.add(object);
			carregaTableModel(parseListToLinhasTableModel(linhas));
		} catch (NullPointerException e) {
			e.printStackTrace();
			carregaTableModel();
		} finally {
			atualizaJTable();
		}

	}

	private void carregaTableModel(List linhas) {

		carregaTableModel(linhas, COLUNAS_JTABLE);

	}

	@Override
	protected void carregaTableModel() {
		controller.setList(null);
		controller.setCacheMap(null);
		List linhas = parseListToLinhasTableModel(controller.getList());
		carregaTableModel(linhas, COLUNAS_JTABLE);

	}

	@Override
	protected List parseListToLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		try {
			for (Object linha : lista) {
				linhas.add(processaLinha((Caixa) linha));
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return linhas;
	}

	// @formatter:off
	private Object[] processaLinha(Caixa linha) {

		Usuario usuarioAbertura = linha.getUsuarioAbertura();
		Usuario usuarioFechamento = linha.getUsuarioFechamento();
		String nomeCompletoUsuarioAbertura = usuarioAbertura.getNomeCompleto();
		String nomeCompletoUsuarioFechamento = usuarioFechamento != null ? usuarioFechamento.getNomeCompleto() : "";
		String totalVendido=Util.formataMoeda(calculaTotalVendido(linha));
		return new Object[] { 
				Util.formataData(linha.getDataAbertura()), 
				nomeCompletoUsuarioAbertura, 
				Util.formataMoeda(linha.getSaldoInicial()),
				totalVendido,
				Util.formataMoeda(linha.getSaldoFinal()), 
				linha.getDataFechamento() == null ? "" : Util.formataData(linha.getDataFechamento()),
				nomeCompletoUsuarioFechamento,
				(linha.getDataFechamento()==null)?"Aberto":"Fechado"

		};
	}
	// @formatter:on
	private Float calculaTotalVendido(Caixa linha) {
		Set<Venda> vendas = linha.getVendas();
		float totalVendido = 0;
		if (vendas != null)
			for (Venda venda : vendas) {
				totalVendido += venda.getTotalVendido();
			}
		return totalVendido;
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
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {

		// controller

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

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub

	}

}
