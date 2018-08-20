package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.Util;

public class VendasDiaDialog extends AbstractDialog {

	private static final Object[] COLUNAS = new Object[] { "#", "Id", "Data Venda", "Total Vendido", "Total Recebido",
			"Desconto Concedido" };
	private Caixa caixa;
	private float totalVendido;
	private float valorRecebido;
	private Venda venda;
	private JLabel totalVendidoLbl;
	private JLabel valorRecebidoLbl;
	private JLabel totalVendasLbl;

	public VendasDiaDialog(JDialog owner, String tituloJanela, boolean modal) throws Exception {
		super(owner, tituloJanela, modal);
		caixa = (Caixa) getCaixaController().getItem();
		setLayout(new BorderLayout());
		add(criaJTablePanel(), BorderLayout.CENTER);
		add(criaSumarioVendaPanel(), BorderLayout.SOUTH);
		add(criaNorthPanel(), BorderLayout.NORTH);
		setSize(1000, 1000);
		setVisible(true);
	}

	private Component criaNorthPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(criaJLabel("Clique na Venda para visualizá-la", 30));
		return panel;
	}

	private Component criaSumarioVendaPanel() {
		JPanel sumarioPnl = new JPanel();
		sumarioPnl.setBorder(criaTitledBorder("Sumário Vendas"));
		List<Component> componentes = new ArrayList<>();

		componentes.add(criaJLabel("Quantidade de Vendas no Período:"));
		totalVendasLbl = criaJLabel(Integer.toString(caixa.getVendas().size()) + " vendas");
		componentes.add(totalVendasLbl);
		componentes.add(criaJLabel("Total Vendido:", true));
		totalVendidoLbl = criaJLabel(Util.formataMoeda(totalVendido));
		componentes.add(totalVendidoLbl);
		componentes.add(criaJLabel("Total Recebido:", true));
		valorRecebidoLbl = criaJLabel(Util.formataMoeda(valorRecebido));
		componentes.add(valorRecebidoLbl);
		sumarioPnl.setLayout(new GridLayout(componentes.size() / 2, 2));
		for (Component componente : componentes) {
			sumarioPnl.add(componente);
		}

		return sumarioPnl;
	}

	private Component criaJTablePanel() throws Exception {
		jPanelTable = new JPanel(new GridLayout());
		carregaTableModel();
		jPanelTable.add(jScrollPane);
		return jPanelTable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

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
	protected void atualizaTableModel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaTableModel() {
		// jScrollPane = null;
		// jTable = null;
		try {
			getCaixaController().busca(caixa.getIdCaixa());
			caixa = (Caixa) getCaixaController().getItem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<Venda> vendasPeriodo = caixa.getVendas();
		List<Venda> linhas = new ArrayList<>(vendasPeriodo);

		super.carregaTableModel(parseListToLinhasTableModel(linhas), COLUNAS);
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

//@formatter:off
	@Override
	protected List parseListToLinhasTableModel(List lista) {
		List linhas=new ArrayList();
		float valorRecebido=0;
		float totalVendido=0;
		for(int i=0;i<lista.size();i++) {
			Venda venda=(Venda) lista.get(i);
			 totalVendido += venda.getTotalVendido();
			 valorRecebido += venda.getPagamento().getValorPagamento();
			linhas.add(new Object[] {
				i+1,
				venda.getIdVenda(),
				Util.formataDataHora(venda.getDataVenda()),
				Util.formataMoeda(venda.getTotalVendido()),
				Util.formataMoeda(venda.getPagamento().getValorPagamento()),
				Util.formataStringDecimais(venda.getPorcentagemDesconto())+"%"
			});
		}
		this.totalVendido=totalVendido;
		this.valorRecebido=valorRecebido;
		return linhas;
	}
//@formatter:on
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
		VendaController vendaController;
		try {
			vendaController = getVendaController();

			vendaController.buscaVenda(venda);
			JDialogFactory.createDialog(InterfaceGrafica.CLASS_NAME_PDV, this, InterfaceGrafica.CLASS_NAME_PDV);
			carregaTableModel();
			// repaint();
			totalVendidoLbl.setText(Util.formataMoeda(totalVendido));
			valorRecebidoLbl.setText(Util.formataMoeda(valorRecebido));
			totalVendasLbl.setText(Integer.toString(caixa.getVendas().size()) + " vendas");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JTable) {
			int rowIndex = jTable.getSelectedRow();
			int columnIndex = 0;
			int value = Integer.parseInt(jTable.getValueAt(rowIndex, columnIndex).toString()) - 1;
			venda = new ArrayList<>(caixa.getVendas()).get(value);
			populaFormulario();
		}
	}
}
