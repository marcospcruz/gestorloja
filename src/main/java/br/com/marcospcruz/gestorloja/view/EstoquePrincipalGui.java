
package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.relatorio.RelatorioEstoqueGeral;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.components.AutocompleteJComboBox;
import br.com.marcospcruz.gestorloja.view.util.MyTableCellRenderer;

public class EstoquePrincipalGui extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2191305646130817805L;
	//@formatter:off
	private static final Object[] COLUNAS_JTABLE = {
//			ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			ConstantesEnum.CODIGO_DE_BARRAS.getValue().toString(),
			ConstantesEnum.FABRICANTE.getValue().toString(), 
			ConstantesEnum.CATEGORIA_LABEL.getValue().toString(),
			ConstantesEnum.TIPO_ITEM_LABEL.getValue().toString(),
			ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString(),
//			ConstantesEnum.CODIGO_DE_BARRAS.getValue().toString(),
			ConstantesEnum.QUANTIDADE_LABEL.getValue().toString(),
			ConstantesEnum.VALOR_UNITARIO_LABEL.getValue().toString(),
			ConstantesEnum.VALOR_TOTAL_LABEL.getValue().toString() };
//@formatter:on
	private JPanel jPanelEstoque;

	private JPanel jPanelCadastros;

	private JFormattedTextField txtBuscaDescricaoProduto;

	// private MyTableModel myTableModel;

	// private JTable jTable;

	private JScrollPane jScrollPane;

	private JPanel jPanelTableEstoque;
	private JComboBox<TipoProduto> cmbCategoriaProduto;
	private AutocompleteJComboBox<SubTipoProduto> cmbSubCategoriaProduto;

	public EstoquePrincipalGui(String tituloJanela, JFrame owner) throws Exception {

		super(owner, tituloJanela, ControllerAbstractFactory.ESTOQUE, true);

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

	private void carregaJPanel() throws Exception {

		jPanelCadastros = new JPanel();

		jPanelCadastros.setBorder(criaTitledBorder("Cadastros"));

		jPanelCadastros.setSize(getWidth(), 300);

		carregaComponentesPanelCadastro();

		// add(jPanelCadastros, BorderLayout.NORTH);

		carregaComponentesPanelEstoque();

		add(jPanelEstoque, BorderLayout.CENTER);

	}

	private void carregaComponentesPanelEstoque() throws Exception {

		// jPanelEstoque = new JPanel(new GridLayout(2, 1));

		jPanelEstoque = new JPanel(new BorderLayout());

		jPanelEstoque.setBorder(super.criaTitledBorder("Estoque Loja"));

		jPanelEstoque.add(carregaAcoesEstoque(), BorderLayout.NORTH);

		jPanelEstoque.add(carregaFormEstoque(), BorderLayout.CENTER);

	}

	private JPanel carregaTableEstoquePnl() {

		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(super.criaTitledBorder("Estoque de Produtos"));

		carregaTableModel();

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setSize(new Dimension(panel.getWidth() - 15, panel.getHeight() - 20));

		panel.add(jScrollPane, BorderLayout.CENTER);

		// panel.add(carregaLogoMarca(), BorderLayout.SOUTH);

		return panel;
	}

	@SuppressWarnings("rawtypes")
	protected void carregaTableModel() {
		try {
			EstoqueController estoqueController = getController();
			List<ItemEstoque> itemsEstoque = estoqueController.getList();

			List linhas = parseListToLinhasTableModel(itemsEstoque);

			carregaTableModel(linhas);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private EstoqueController getController() throws Exception {

		return getItemEstoqueController();
	}

	/**
	 * Método responsável em passar linhas para o tableModel.
	 * 
	 * @param linhas
	 */
	@SuppressWarnings("rawtypes")
	protected void carregaTableModel(List linhas) {

		super.carregaTableModel(linhas, COLUNAS_JTABLE);
	}

	@Override
	protected List parseListToLinhasTableModel(List itensEstoque) {

		List linhas = new ArrayList();

		for (Object itemEstoque : itensEstoque) {

			linhas.add(parseRow((ItemEstoque) itemEstoque));

		}

		return linhas;

	}

	private Object[] parseRow(ItemEstoque itemEstoque) {

		float valor = itemEstoque.getQuantidade() * itemEstoque.getValorUnitario();

		String simboloMonetarioBr = ConstantesEnum.SIMBOLO_MONETARIO_BR.getValue().toString();

		String valorTotal = simboloMonetarioBr + Util.formataStringDecimais(valor);

		String valorUnitario = simboloMonetarioBr + Util.formataStringDecimais(itemEstoque.getValorUnitario());

		SubTipoProduto tipoProduto = itemEstoque.getProduto().getTipoProduto();
		Produto produto = itemEstoque.getProduto();
		String nomeFabricante = itemEstoque.getFabricante() == null ? "" : itemEstoque.getFabricante().getNome();

		String descricaoSuperTipoProduto = tipoProduto.getSuperTipoProduto() == null ? ""
				: tipoProduto.getSuperTipoProduto().getDescricaoTipo();
		//@formatter:off		
		return new Object[] { 
//				itemEstoque.getIdItemEstoque(), 
				itemEstoque.getCodigoDeBarras(),
				nomeFabricante,
				descricaoSuperTipoProduto,
				tipoProduto.getDescricaoTipo(),
				produto.getDescricaoProduto(),
//				itemEstoque.getCodigoDeBarras(),
				itemEstoque.getQuantidade(), 
				valorUnitario, 
				valorTotal 
				};
//@formatter:on

	}

	private JPanel carregaAcoesEstoque() throws Exception {

		JPanel panel = new JPanel();

		panel.add(carregaBuscaItemPnl());

		return panel;

	}

	private JPanel carregaBuscaItemPnl() throws Exception {

		JPanel panel = new JPanel(new GridLayout(2, 1));

		panel.setBorder(super.criaTitledBorder("Consulta de Estoque"));

		panel.add(carregaFormularioBusca());

		panel.add(carregaBotoesConsulta());

		return panel;

	}

	private JPanel carregaFormularioBusca() throws Exception {

		JPanel panel = new JPanel();

		panel.add(super.criaJLabel("Categoria Produto"));

		cmbCategoriaProduto = super.carregaComboTiposProduto();

		panel.add(cmbCategoriaProduto);

		cmbCategoriaProduto.addActionListener(this);
		cmbCategoriaProduto.setFont(FontMapper.getFont(20));
		panel.add(super.criaJLabel("Sub-Categoria Produto"));

		cmbSubCategoriaProduto = new AutocompleteJComboBox<>(this, getCategoriaProdutoController(),
				cmbCategoriaProduto);
		cmbSubCategoriaProduto.setFont(FontMapper.getFont(20));

		cmbSubCategoriaProduto.addActionListener(this);
		JTextComponent component = (JTextComponent) cmbSubCategoriaProduto.getEditor().getEditorComponent();
		component.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				String string = component.getText();

				if (string.isEmpty() || string.equals("Selecione uma Opção.")) {
					loadItensSuperTipoEstoque();
					atualizaTableModel();
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(cmbCategoriaProduto));
		panel.add(cmbSubCategoriaProduto);

		panel.add(super.criaJLabel(ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString()));

		txtBuscaDescricaoProduto = new JFormattedTextField();

		txtBuscaDescricaoProduto.setColumns(20);

		txtBuscaDescricaoProduto.setFont(FontMapper.getFont(20));

		panel.add(txtBuscaDescricaoProduto);

		return panel;
	}

	private JPanel carregaBotoesConsulta() {

		JPanel panel = new JPanel(new GridLayout(1, 3, 90, 90));

		JButton btnBuscaItem = inicializaJButton("Consulta Estoque");

		JButton btnLimpaConsulta = inicializaJButton("Limpar Consulta");

		panel.add(btnBuscaItem);

		panel.add(btnLimpaConsulta);

		JButton btnNovoItemEstoque = inicializaJButton(ConstantesEnum.LBL_BTN_NOVO_ITEM_ESTOQUE.getValue().toString());

		panel.add(btnNovoItemEstoque);

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

		panel.setBorder(super.criaTitledBorder(ConstantesEnum.RELATORIO_TITLE_BORDER.getValue().toString()));

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

	// protected JButton inicializaJButton(String text) {
	//
	// JButton jButton = new JButton(text);
	//
	// jButton.addActionListener(this);
	//
	// return jButton;
	// }

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
		if (disableActionPerformed)
			return;
		try {
			EstoqueController estoqueController = getController();

			if (e.getActionCommand().equals("comboBoxChanged")) {
				selecionaAcaoComboBox(e, estoqueController);
			} else {
				selecionaAcao(e.getActionCommand());
				// atualizaTableModel(estoqueController.getItemEstoque());
			}

		} catch (Exception e1) {

			e1.printStackTrace();

			JOptionPane.showMessageDialog(null, e1.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);
			limpaPesquisa();

		} finally {
			atualizaTableModel();

		}
	}

	private void selecionaAcaoComboBox(ActionEvent e, EstoqueController estoqueController) {

		Object categoriaProduto = ((JComboBox<TipoProduto>) e.getSource()).getSelectedItem();
		if (e.getSource().equals(cmbCategoriaProduto)) {
			loadItensSuperTipoEstoque();

		} else {
			// if (categoriaProduto == null) {
			// JTextComponent component = (JTextComponent) ((JComboBox<TipoProduto>)
			// e.getSource()).getEditor()
			// .getEditorComponent();
			// if (component.getText().isEmpty())
			// return;
			// }

			if (categoriaProduto != null
			// && !cmbCategoriaProduto.getSelectedItem().toString().equals("Selecione uma
			// Opção.")
			// && !(categoriaProduto instanceof String)
			// && !"Selecione uma Opção".equals(categoriaProduto.toString())
			) {
				String descricaoTipo = (categoriaProduto instanceof String) ? categoriaProduto.toString()
						: ((SubTipoProduto) categoriaProduto).getDescricaoTipo();
				estoqueController.busca(descricaoTipo, null, null);

				// if (estoqueController.getList().isEmpty()) {
				//
				// loadItensSuperTipoEstoque();
				// }
			}
		}

	}

	private void loadItensSuperTipoEstoque() {
		Object categoriaProduto = cmbCategoriaProduto.getSelectedItem();
		cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(cmbCategoriaProduto));
		EstoqueController estoqueController = null;
		try {
			estoqueController = getController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		estoqueController.busca(categoriaProduto.toString(), null, null);
	}

	@Override
	public void atualizaTableModel() {

		try {
			EstoqueController estoqueController = getController();
			atualizaTableModel(estoqueController.getItemEstoque());

			// try {
			// cmbCategoriaProduto.setModel(carregaComboTiposProdutoModel());
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			repaint();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public void teste() {
		// jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		for (int i = 0; i < jTable.getColumnCount(); i++) {
			TableColumn coluna = jTable.getColumnModel().getColumn(i);

			MyTableCellRenderer tableCellRenderer = new MyTableCellRenderer();
			coluna.setCellRenderer(tableCellRenderer);

		}

	}

	protected void selecionaAcao(String actionCommand) throws Exception {

		EstoqueController estoqueController = getController();
		if (actionCommand.equals(ConstantesEnum.LBL_BTN_NOVO_ITEM_ESTOQUE.getValue().toString())) {

			new ItemEstoqueDialog(estoqueController, this);

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_RELATORIO.getValue().toString())) {

			RelatorioEstoqueGeral printer = new RelatorioEstoqueGeral();

			printer.gerarRelatorio();

		} else if (actionCommand.equals(ConstantesEnum.CONSULTA_ESTOQUE.getValue().toString())) {

			buscaItemEstoque(estoqueController);

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_TIPO_PRODUTO.getValue().toString())) {

			new TipoProdutoDialog(this);

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.PRODUTO_LABEL.getValue().toString())) {

			new ProdutoDialog(this);

			// atualizaView();

			estoqueController.anulaAtributos();

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_LIMPAR.getValue().toString())) {

			estoqueController.anulaAtributos();

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.FABRICANTE.getValue().toString())) {

			new FabricanteDialog(this);

			// atualizaView();

		} else {

			limpaPesquisa();

			// ((JTextComponent)
			// cmbSubCategoriaProduto.getEditor().getEditorComponent()).setText("Selecione
			// uma Opção");

			// controller.anulaAtributos();

		}

	}

	private void limpaPesquisa() {
		txtBuscaDescricaoProduto.setText("");
		// estoqueController.anulaAtributos();
		cmbCategoriaProduto.setSelectedIndex(0);
	}

	private void buscaItemEstoque(EstoqueController estoqueController) throws Exception {
		String descricaoSubCategoria;
		try {
			descricaoSubCategoria = ITEM_ZERO_COMBO.equals(cmbSubCategoriaProduto.getSelectedItem().toString()) ? null
					: cmbSubCategoriaProduto.getSelectedItem().toString();

		} catch (ClassCastException e) {
			throw new Exception("Seleção de Categoria Inválida.");
		}
		String descricao = txtBuscaDescricaoProduto.getText();
		estoqueController.anulaAtributos();

		if (descricao.length() == 0) {
			throw new Exception("Descrição de Produto Inválida");
		}

		estoqueController.buscaItemEstoque(descricao, descricaoSubCategoria);

		estoqueController.validaBusca();

		// txtBuscaDescricaoProduto.setText("");
		populaFormulario();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void atualizaTableModel(ItemEstoque item) {

		try {

			List linhas = new ArrayList();

			linhas.add(item);

			carregaTableModel(parseListToLinhasTableModel(linhas));

		} catch (NullPointerException e) {

			// showMessage("Não foram encontrados Produtos com esta categoria no estoque.");

			carregaTableModel();

		}

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTableEstoque.remove(jScrollPane);

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTableEstoque.add(jScrollPane);

		jPanelTableEstoque.repaint();

	}

	// JTable inicializaJTable() {
	//
	// JTable jTable = super.inicializaJTable();
	//
	// jTable.getColumnModel().getColumn(0).setCellRenderer(direita);
	//
	// jTable.getColumnModel().getColumn(4).setCellRenderer(direita);
	//
	// jTable.getColumnModel().getColumn(5).setCellRenderer(direita);
	//
	// jTable.getColumnModel().getColumn(6).setCellRenderer(direita);
	//
	// return jTable;
	//
	// }

	public void mouseClicked(MouseEvent e) {

		if (e.getSource() instanceof JTable) {

			JTable table = (JTable) e.getSource();

			int indiceLinha = table.getSelectedRow();

			String codigoProduto = table.getModel().getValueAt(indiceLinha, 0).toString();

			EstoqueController estoqueController = null;
			try {
				estoqueController = getItemEstoqueController();
				estoqueController.busca(codigoProduto);

				// new ItemDoEstoqueDialog(this, estoqueController);
				new ItemEstoqueDialog(estoqueController, this);
				estoqueController.setItem(null);
			} catch (Exception e1) {

				e1.printStackTrace();
			}

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
	protected void adicionaItemEstoque() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaFormulario() {
		EstoqueController estoqueController = null;
		try {
			estoqueController = getItemEstoqueController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemEstoque itemEstoque = (ItemEstoque) estoqueController.getItem();
		cmbCategoriaProduto.getModel().setSelectedItem(itemEstoque.getProduto().getTipoProduto().getSuperTipoProduto());
		cmbSubCategoriaProduto.setSelectedItem(itemEstoque.getProduto().getTipoProduto());

	}

}
