package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.relatorio.RelatorioEstoqueGeral;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.MyFormatador;
import br.com.marcospcruz.gestorloja.view.util.MyTableModel;

public class EstoquePrincipalGui extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2191305646130817805L;

	private static final Object[] COLUNAS_JTABLE = { ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			ConstantesEnum.FABRICANTE.getValue().toString(), ConstantesEnum.CATEGORIA_LABEL.getValue().toString(),
			ConstantesEnum.TIPO_ITEM_LABEL.getValue().toString(),
			ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString(),
			ConstantesEnum.QUANTIDADE_LABEL.getValue().toString(),
			ConstantesEnum.VALOR_UNITARIO_LABEL.getValue().toString(),
			ConstantesEnum.VALOR_TOTAL_LABEL.getValue().toString() };

	private JPanel jPanelEstoque;

	private JPanel jPanelCadastros;

	private JFormattedTextField txtBuscaDescricaoProduto;

	private EstoqueController controller;

	private MyTableModel myTableModel;

	private JTable jTable;

	private JScrollPane jScrollPane;

	private JPanel jPanelTableEstoque;

	public EstoquePrincipalGui(String tituloJanela, JFrame owner) {

		super(owner, tituloJanela, true);

		controller = new EstoqueController();
		controller.setLoginFacade(((PrincipalGui)owner).getLoginFacade());

		carregaJPanel();

		setVisible(true);

	}

	private JPanel carregaLogoMarca() {

		JLabel label = null;

		try {

			URI uriImagem = getClass().getClassLoader().getResource("META-INF\\logo_marca.jpg").toURI();

			ImageIcon icon = new ImageIcon(uriImagem.toURL());

			label = new JLabel(icon);

		} catch (URISyntaxException | MalformedURLException e) {

			e.printStackTrace();
		}

		label.setPreferredSize(new Dimension(400, 100));

		JPanel p = new JPanel(new FlowLayout());

		p.add(label);

		p.setBackground(Color.YELLOW);

		return p;

	}

	private void carregaJPanel() {

		jPanelCadastros = new JPanel();

		jPanelCadastros.setBorder(new TitledBorder("Cadastros"));

		jPanelCadastros.setSize(getWidth(), 300);

		carregaComponentesPanelCadastro();

		add(jPanelCadastros, BorderLayout.NORTH);

		carregaComponentesPanelEstoque();

		add(jPanelEstoque, BorderLayout.CENTER);

	}

	private void carregaComponentesPanelEstoque() {

		// jPanelEstoque = new JPanel(new GridLayout(2, 1));

		jPanelEstoque = new JPanel(new BorderLayout());

		jPanelEstoque.setBorder(new TitledBorder("Estoque Loja"));

		jPanelEstoque.add(carregaAcoesEstoque(), BorderLayout.NORTH);

		jPanelEstoque.add(carregaFormEstoque(), BorderLayout.CENTER);

	}

	private JPanel carregaTableEstoquePnl() {

		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(new TitledBorder("Estoque de Produtos"));

		carregaTableModel();

		jTable = inicializaJTable();

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setSize(new Dimension(panel.getWidth() - 15, panel.getHeight() - 20));

		panel.add(jScrollPane, BorderLayout.CENTER);

		// panel.add(carregaLogoMarca(), BorderLayout.SOUTH);

		return panel;
	}

	@SuppressWarnings("rawtypes")
	protected void carregaTableModel() {

		List linhas = carregaLinhasTableModel(controller.getItensEstoque());

		carregaTableModel(linhas);
	}

	@SuppressWarnings("rawtypes")
	protected void carregaTableModel(List linhas) {

		myTableModel = new MyTableModel(linhas, COLUNAS_JTABLE);

		myTableModel.fireTableDataChanged();

	}

	@Override
	protected List carregaLinhasTableModel(List itensEstoque) {

		List linhas = new ArrayList();

		for (Object itemEstoque : itensEstoque) {

			linhas.add(processaColuna((ItemEstoque) itemEstoque));

		}

		return linhas;

	}

	private Object[] processaColuna(ItemEstoque itemEstoque) {

		float valor = itemEstoque.getQuantidade() * itemEstoque.getProduto().getValorUnitario();

		String simboloMonetarioBr = ConstantesEnum.SIMBOLO_MONETARIO_BR.getValue().toString();

		String valorTotal = simboloMonetarioBr + MyFormatador.formataStringDecimais(valor);

		String valorUnitario = simboloMonetarioBr
				+ MyFormatador.formataStringDecimais(itemEstoque.getProduto().getValorUnitario());

		SubTipoProduto tipoProduto = itemEstoque.getProduto().getTipoProduto();
		Produto produto = itemEstoque.getProduto();
		String nomeFabricante = produto.getFabricante() == null ? "" : produto.getFabricante().getNome();
//@formatter:off		
		return new Object[] { 
				itemEstoque.getIdItemEstoque(), 
				nomeFabricante,
				tipoProduto.getSuperTipoProduto().getDescricaoTipo(),
				tipoProduto.getDescricaoTipo(),
				produto.getDescricaoProduto(),
				itemEstoque.getQuantidade(), 
				valorUnitario, 
				valorTotal 
				};
//@formatter:on

	}

	private JPanel carregaAcoesEstoque() {

		JPanel panel = new JPanel();

		panel.add(carregaBuscaItemPnl());

		panel.add(carregaPnlBotoes());

		return panel;

	}

	private JPanel carregaPnlBotoes() {

		JPanel panel = new JPanel();

		panel.setBorder(new TitledBorder(""));

		JButton btnNovoItemEstoque = inicializaJButton(ConstantesEnum.LBL_BTN_NOVO_ITEM_ESTOQUE.getValue().toString());

		panel.add(btnNovoItemEstoque);

		return panel;

	}

	private JPanel carregaBuscaItemPnl() {

		JPanel panel = new JPanel(new GridLayout(2, 1));

		panel.setBorder(new TitledBorder("Consulta de Estoque"));

		panel.add(carregaFormularioBusca());

		panel.add(carregaBotoesConsulta());

		return panel;

	}

	private JPanel carregaFormularioBusca() {

		JPanel panel = new JPanel();

		panel.add(new JLabel(ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString()));

		txtBuscaDescricaoProduto = new JFormattedTextField();

		txtBuscaDescricaoProduto.setColumns(20);

		panel.add(txtBuscaDescricaoProduto);

		return panel;
	}

	private JPanel carregaBotoesConsulta() {

		JPanel panel = new JPanel(new GridLayout(1, 2));

		JButton btnBuscaItem = inicializaJButton("Consulta Estoque");

		JButton btnLimpaConsulta = inicializaJButton("Limpar Consulta");

		panel.add(btnBuscaItem);

		panel.add(btnLimpaConsulta);

		return panel;

	}

	private JPanel carregaFormEstoque() {

		JPanel panel = new JPanel(new BorderLayout());

		// panel.add(carregaItemEstoquePnl(), BorderLayout.NORTH);

		jPanelTableEstoque = carregaTableEstoquePnl();

		panel.add(jPanelTableEstoque, BorderLayout.CENTER);

		panel.add(carregaPanelBtnRelatorios(), BorderLayout.EAST);

		return panel;
	}

	private JPanel carregaPanelBtnRelatorios() {

		JPanel panel = new JPanel();

		panel.setBorder(new TitledBorder(ConstantesEnum.RELATORIO_TITLE_BORDER.getValue().toString()));

		JButton btnRelatorioGeralEstoque = inicializaJButton(ConstantesEnum.LBL_BTN_RELATORIO.getValue().toString());

		panel.add(btnRelatorioGeralEstoque);

		return panel;
	}

	private void carregaComponentesPanelCadastro() {

		Rectangle retangulo = new Rectangle(10, 100, 130, 50);

		jPanelCadastros.add(inicializaJButton(ConstantesEnum.FABRICANTE.getValue().toString(), retangulo));

		JButton btnTipoProduto = inicializaJButton(ConstantesEnum.LBL_BTN_TIPO_PRODUTO.getValue().toString(),
				retangulo);

		jPanelCadastros.add(btnTipoProduto);

		JButton btnProduto = inicializaJButton(ConstantesEnum.PRODUTO_LABEL.getValue().toString(),
				btnTipoProduto.getWidth() + 20, 100, 130, 50);

		jPanelCadastros.add(btnProduto);

		jPanelCadastros.setSize(btnProduto.getWidth(), btnProduto.getHeight());

	}

	protected JButton inicializaJButton(String text) {

		JButton jButton = new JButton(text);

		jButton.addActionListener(this);

		return jButton;
	}

	private JButton inicializaJButton(String text, Rectangle retangulo) {

		return inicializaJButton(text, (int) retangulo.getX(), (int) retangulo.getY(), (int) retangulo.getWidth(),
				(int) retangulo.getHeight());

	}

	protected JButton inicializaJButton(String text, int x, int y, int width, int height) {

		JButton jButton = inicializaJButton(text);

		jButton.setBounds(x, y, width, height);

		return jButton;

	}

	public void actionPerformed(ActionEvent e) {

		try {

			selecionaAcao(e.getActionCommand());

		} catch (Exception e1) {

			e1.printStackTrace();

			JOptionPane.showMessageDialog(null, e1.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);

		}

		atualizaTableModel(controller.getItemEstoque());

	}

	protected void selecionaAcao(String actionCommand) throws Exception {

		if (actionCommand.equals(ConstantesEnum.LBL_BTN_NOVO_ITEM_ESTOQUE.getValue().toString())) {

			new ItemEstoqueDialog(controller, this);

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_RELATORIO.getValue().toString())) {

			RelatorioEstoqueGeral printer = new RelatorioEstoqueGeral();

			printer.gerarRelatorio();

		} else if (actionCommand.equals(ConstantesEnum.CONSULTA_ESTOQUE.getValue().toString())) {

			buscaItemEstoque();

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_TIPO_PRODUTO.getValue().toString())) {

			new TipoProdutoDialog(this);

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.PRODUTO_LABEL.getValue().toString())) {

			new ProdutoDialog(this);

			// atualizaView();

			controller.anulaAtributos();

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_LIMPAR.getValue().toString())) {

			controller.anulaAtributos();

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.FABRICANTE.getValue().toString())) {

			new FabricanteDialog(this);

			// atualizaView();

		} else {

			txtBuscaDescricaoProduto.setText("");

			// controller.anulaAtributos();

		}

	}

	private void buscaItemEstoque() throws Exception {

		String descricao = txtBuscaDescricaoProduto.getText();

		if (descricao.length() > 0) {

			controller.anulaAtributos();

			controller.buscaItemEstoque(descricao);

			controller.validaBusca();

			txtBuscaDescricaoProduto.setText("");

		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void atualizaTableModel(ItemEstoque item) {

		try {

			List linhas = new ArrayList();

			linhas.add(item);

			carregaTableModel(carregaLinhasTableModel(linhas));

		} catch (NullPointerException e) {

			e.printStackTrace();

			carregaTableModel();

		}

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTableEstoque.remove(jScrollPane);

		jTable = inicializaJTable();

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTableEstoque.add(jScrollPane);

		jPanelTableEstoque.repaint();

	}

	JTable inicializaJTable() {

		JTable jTable = new JTable(myTableModel);

		jTable.addMouseListener(this);

		DefaultTableCellRenderer direita = new DefaultTableCellRenderer();

		direita.setHorizontalAlignment(SwingConstants.RIGHT);

		jTable.getColumnModel().getColumn(0).setCellRenderer(direita);

		jTable.getColumnModel().getColumn(4).setCellRenderer(direita);

		jTable.getColumnModel().getColumn(5).setCellRenderer(direita);

		jTable.getColumnModel().getColumn(6).setCellRenderer(direita);

		return jTable;

	}

	public void mouseClicked(MouseEvent e) {

		if (e.getSource() instanceof JTable) {

			JTable table = (JTable) e.getSource();

			int indiceLinha = table.getSelectedRow();

			int idItemEstoque = (Integer) table.getModel().getValueAt(indiceLinha, 0);

			controller.busca(idItemEstoque);

			new ItemDoEstoqueDialog(this, controller);

			controller.anulaAtributos();

			atualizaTableModel(null);

		}

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
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

	public EstoqueController getController() {
		return controller;
	}

	public void setController(EstoqueController controller) {
		this.controller = controller;
	}

}
