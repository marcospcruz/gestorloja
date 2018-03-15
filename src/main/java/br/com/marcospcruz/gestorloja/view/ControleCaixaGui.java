package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.marcospcruz.gestorloja.abstractfactory.CommandFactory;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.util.MyTableModel;

public class ControleCaixaGui extends AbstractDialog implements WindowListener {

	private static final String ABRIR_CAIXA = "Abrir Caixa";
	private static final String FECHAR_CAIXA = "Fechar Caixa";
	private static final Object[] COLUNAS_JTABLE = new Object[] { "Abertura", "Operador Abertura", "Saldo Abertura",
			"Saldo Fechamento", "Fechamento", "Operador Fechamento" };
	private JPanel jpanel;

	public ControleCaixaGui(LoginFacade loginFacade, String tituloJanela, JFrame owner) throws Exception {

		super(owner, tituloJanela, true, loginFacade);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(configuraOperacoesPanel(), BorderLayout.NORTH);

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
		jTable = inicializaJTable();

		// JScrollPane
		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setSize(jpanel.getWidth() - 15, jpanel.getHeight() - 20);

		jpanel.add(jScrollPane, BorderLayout.CENTER);

		getContentPane().add(jpanel, BorderLayout.CENTER);
	}

	JTable inicializaJTable() {

		JTable jTable = new JTable(myTableModel);

		jTable.addMouseListener(this);

		DefaultTableCellRenderer direita = new DefaultTableCellRenderer();

		direita.setHorizontalAlignment(SwingConstants.RIGHT);

		jTable.getColumnModel().getColumn(0).setCellRenderer(direita);

		jTable.getColumnModel().getColumn(4).setCellRenderer(direita);

		jTable.getColumnModel().getColumn(5).setCellRenderer(direita);

		// jTable.getColumnModel().getColumn(6).setCellRenderer(direita);

		return jTable;

	}

	private JPanel configuraOperacoesPanel()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		JPanel panel = new JPanel();

		CommandFactory btnFactory = new CommandFactory();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(btnFactory.createButton("Abrir Caixa", this));
		panel.add(btnFactory.createButton("Fechar Caixa", this));
		panel.setBorder(new TitledBorder("Operações"));
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			switch (arg0.getActionCommand()) {
			case ABRIR_CAIXA:

				((CaixaController) controller).validateCaixaAberto();
				new FormAberturaCaixaDialog(this, controller);

				break;
			case FECHAR_CAIXA:
				((CaixaController) controller).validateCaixaFechado();
				new FormFechamentoCaixaDialog(this, controller);
			}
		} catch (Exception e) {

			e.printStackTrace();
			showMessage(e.getMessage());
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
	protected JPanel carregaJpanelTable(int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaTableModel(Object object) {

		List linhas = new ArrayList();
		try {
			linhas.add(object);
			carregaTableModel(carregaLinhasTableModel(linhas));
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
		List linhas = carregaLinhasTableModel(controller.getList());
		carregaTableModel(linhas, COLUNAS_JTABLE);

	}

	@Override
	protected List carregaLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		for (Object linha : lista) {
			linhas.add(processaLinha((Caixa) linha));
		}

		return linhas;
	}

	// @formatter:off
	private Object[] processaLinha(Caixa linha) {

		Usuario usuarioAbertura = linha.getUsuarioAbertura();
		Usuario usuarioFechamento = linha.getUsuarioFechamento();
		String nomeCompletoUsuarioAbertura = usuarioAbertura.getNomeCompleto();
		String nomeCompletoUsuarioFechamento = usuarioFechamento != null ? usuarioFechamento.getNomeCompleto() : "";
		return new Object[] { 
				Util.formataData(linha.getDataAbertura()), 
				nomeCompletoUsuarioAbertura, 
				Util.formataMoeda(linha.getSaldoInicial()),
				Util.formataMoeda(linha.getSaldoFinal()), 
				linha.getDataFechamento() == null ? "" : Util.formataData(linha.getDataFechamento()),
				nomeCompletoUsuarioFechamento

		};
	}

	// @formatter:on
	@Override
	protected void excluiItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvarItem() throws Exception {
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

}
