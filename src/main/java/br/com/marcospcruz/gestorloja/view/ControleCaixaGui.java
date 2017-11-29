package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.marcospcruz.gestorloja.abstractfactory.CommandFactory;
import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class ControleCaixaGui extends AbstractDialog {

	private static final String ABRIR_CAIXA = "Abrir Caixa";

	public ControleCaixaGui(LoginFacade loginFacade, String tituloJanela, JFrame owner) throws Exception {

		super(owner, tituloJanela, true, loginFacade);
		getContentPane().setLayout(new BorderLayout(0, 0));

		try {
			getContentPane().add(configuraOperacoesPanel(), BorderLayout.NORTH);
			carregaTableModel();
			jTable = inicializaJTable();
			JPanel panel = new JPanel();
			
			panel.setBorder(new TitledBorder("Caixa"));
			JScrollPane jScrollPane = new JScrollPane(jTable);

			Rectangle retangulo = this.getBounds();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			jScrollPane.setBounds(retangulo);

			panel.add(jScrollPane);
			getContentPane().add(panel, BorderLayout.CENTER);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {

			e.printStackTrace();
		}

		setVisible(true);

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
		switch (arg0.getActionCommand()) {
		case ABRIR_CAIXA:
			new FormAberturaCaixaDialog(this, controller);
			break;
		}

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
		List linhas = carregaLinhasTableModel(controller.getList());
		carregaTableModel(linhas, new Object[] { "Abertura", "Operador Abertura", "Saldo Abertura", "Saldo Fechamento",
				"Fechamento", "Operador Fechamento" });

	}

	@Override
	protected List carregaLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		for (Object linha : lista) {
			linhas.add(processaLinha((Caixa) linha));
		}

		return linhas;
	}

	//@formatter:off
	private Object[] processaLinha(Caixa linha) {

		Usuario usuarioAbertura = linha.getUsuarioAbertura();
		Usuario usuarioFechamento = linha.getUsuarioFechamento();
		String nomeCompletoUsuarioAbertura=usuarioAbertura.getNomeCompleto();
		String nomeCompletoUsuarioFechamento=usuarioFechamento!=null?usuarioFechamento.getNomeCompleto():"";
		return new Object[] { 
				linha.getDataAbertura(), 
				nomeCompletoUsuarioAbertura, 
				linha.getSaldoInicial(),
				linha.getSaldoFinal(), 
				linha.getDataFechamento()==null?"":linha.getDataFechamento(), 
				nomeCompletoUsuarioFechamento

		};
	}
	//@formatter:on
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

}
