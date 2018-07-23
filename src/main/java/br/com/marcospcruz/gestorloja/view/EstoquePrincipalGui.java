
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.relatorio.RelatorioEstoqueGeralPdf;
import br.com.marcospcruz.gestorloja.controller.relatorio.RelatorioEstoqueGeralXlsX;
import br.com.marcospcruz.gestorloja.model.Fabricante;
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
			ConstantesEnum.VALOR_TOTAL_LABEL.getValue().toString()};
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
	private EstoqueController estoqueController;

	public EstoquePrincipalGui(String tituloJanela, JFrame owner) throws Exception {

		super(owner, tituloJanela, ControllerAbstractFactory.ESTOQUE, true);
		estoqueController = getController();
		estoqueController.setList(null);
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

		add(jPanelCadastros, BorderLayout.NORTH);

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

			// estoqueController.setList(null);

			// if (estoqueController.getList() == null ||
			// estoqueController.getList().isEmpty())
			// estoqueController.buscaTodos();
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

			ItemEstoque item = (ItemEstoque) itemEstoque;

			Produto produto = item.getProduto();

			linhas.add(parseRow(item));

		}

		return linhas;

	}

	private Object[] parseRow(ItemEstoque itemEstoque) {

		float valor;
		try {
			valor = itemEstoque.getQuantidade() * itemEstoque.getValorUnitario();
		} catch (NullPointerException e) {
			valor = 0;
		}

		String valorTotal = Util.formataMoeda(valor);

		String valorUnitario = Util.formataMoeda(itemEstoque.getValorUnitario());

		SubTipoProduto tipoProduto = itemEstoque.getTipoProduto();

		Produto produto = itemEstoque.getProduto();
		Fabricante fabricante = itemEstoque.getFabricante();
		String nomeFabricante = fabricante == null ? "" : fabricante.getNome();

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
				itemEstoque.isEstoqueDedutivel()?itemEstoque.getQuantidade():"Sem Estoque", 
				valorUnitario, 
				valorTotal
//				true
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

	@SuppressWarnings("unchecked")
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
		cmbSubCategoriaProduto.setEnabled(false);
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
					try {
						loadItensSuperTipoEstoque();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					atualizaTableModel();
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		if (!cmbCategoriaProduto.getSelectedItem().toString().equals("Selecione uma Opção")) {
			cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(cmbCategoriaProduto));
		}
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
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		// panel.setLayout(new GridLayout(2, 1));
		panel.setBorder(super.criaTitledBorder(ConstantesEnum.RELATORIO_TITLE_BORDER.getValue().toString()));

		JButton btnRelatorioGeralEstoque = inicializaJButton(ConstantesEnum.LBL_BTN_RELATORIO.getValue().toString());
		JButton btnRelatorioGeralEstoqueXlsx = inicializaJButton("Relatório de Estoque (XLSX)");
		JPanel teste = new JPanel();
		JPanel teste2 = new JPanel();
		// teste.add(btnRelatorioGeralEstoque);
		// teste2.add(btnRelatorioGeralEstoqueXlsx);
		panel.add(teste);
		panel.add(teste2);
		panel.add(btnRelatorioGeralEstoque);
		panel.add(btnRelatorioGeralEstoqueXlsx);
		return panel;
	}

	private void carregaComponentesPanelCadastro() {

		Rectangle retangulo = new Rectangle(10, 100, 130, 50);

		jPanelCadastros.add(inicializaJButton(ConstantesEnum.FABRICANTE.getValue().toString()));

		JButton btnTipoProduto = inicializaJButton(ConstantesEnum.LBL_BTN_TIPO_PRODUTO.getValue().toString(),
				retangulo);

		jPanelCadastros.add(btnTipoProduto);

		JButton btnProduto = inicializaJButton(ConstantesEnum.PRODUTO_LABEL.getValue().toString());

		jPanelCadastros.add(btnProduto);
		//
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

			showErrorMessage(this, e1.getMessage());
			limpaPesquisa();

		} finally {
			atualizaTableModel();

		}
	}

	private void selecionaAcaoComboBox(ActionEvent e, EstoqueController estoqueController) throws Exception {

		Object categoriaProduto = ((JComboBox<TipoProduto>) e.getSource()).getSelectedItem();
		if (e.getSource().equals(cmbCategoriaProduto)) {
			try {
				loadItensSuperTipoEstoque();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
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

	private void loadItensSuperTipoEstoque() throws Exception {
		Object categoriaProduto = cmbCategoriaProduto.getSelectedItem();
		cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(cmbCategoriaProduto));
		boolean enableSubCategorias = cmbSubCategoriaProduto.getModel().getSize() > 0;
		cmbSubCategoriaProduto.setEnabled(enableSubCategorias);
		EstoqueController estoqueController = null;
		try {
			estoqueController = getController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String descricaoCategoria = null;
		String descricaoProduto = txtBuscaDescricaoProduto.getText();

		descricaoCategoria = ((SubTipoProduto) categoriaProduto).getDescricaoTipo();
		if (!descricaoCategoria.equals("Selecione uma Opção"))
			estoqueController.busca(descricaoCategoria, descricaoProduto, null);
		else
			estoqueController.setList(null);
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
			estoqueController.setItem(new ItemEstoque());
			new ItemEstoqueDialog(estoqueController, this);
			cmbCategoriaProduto.setSelectedIndex(0);
			txtBuscaDescricaoProduto.setText("");
			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);
			cmbCategoriaProduto.setModel(super.carregaComboTiposProdutoModel());
		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_RELATORIO.getValue().toString())) {

			RelatorioEstoqueGeralPdf printer = new RelatorioEstoqueGeralPdf();

			printer.geraRelatorio();

		} else if (actionCommand.contains("XLSX")) {
			RelatorioEstoqueGeralXlsX printer = new RelatorioEstoqueGeralXlsX();
			
			printer.geraRelatorio();

		} else if (actionCommand.equals(ConstantesEnum.CONSULTA_ESTOQUE.getValue().toString())) {

			buscaItemEstoque(estoqueController);

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_TIPO_PRODUTO.getValue().toString())) {

			new TipoProdutoDialog(this);
			txtBuscaDescricaoProduto.setText("");
			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.PRODUTO_LABEL.getValue().toString())) {

			new ProdutoDialog(this);
			txtBuscaDescricaoProduto.setText("");
			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);

			// atualizaView();

			estoqueController.anulaAtributos();

		} else if (actionCommand.equals(ConstantesEnum.LBL_BTN_LIMPAR.getValue().toString())) {

			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);
			txtBuscaDescricaoProduto.setText("");
			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);

			// atualizaView();

		} else if (actionCommand.equals(ConstantesEnum.FABRICANTE.getValue().toString())) {

			new FabricanteDialog(this);

			txtBuscaDescricaoProduto.setText("");
			estoqueController.anulaAtributos();
			estoqueController.setCacheMap(null);

		} else {

			limpaPesquisa();

			// ((JTextComponent)
			// cmbSubCategoriaProduto.getEditor().getEditorComponent()).setText("Selecione
			// uma Opção");

			estoqueController.anulaAtributos();

		}

	}

	private void limpaPesquisa() {
		txtBuscaDescricaoProduto.setText("");
		// estoqueController.anulaAtributos();
		cmbCategoriaProduto.setSelectedIndex(0);
	}

	private void buscaItemEstoque(EstoqueController estoqueController) throws Exception {

		String descricaoSubCategoria = null;
		String descricao = txtBuscaDescricaoProduto.getText();
		try {
			descricaoSubCategoria = ITEM_ZERO_COMBO.equals(cmbSubCategoriaProduto.getSelectedItem().toString()) ? null
					: cmbSubCategoriaProduto.getSelectedItem().toString();

		} catch (ClassCastException e) {
			if (descricao.isEmpty())
				throw new Exception("Seleção de Categoria Inválida.");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

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
	protected JPanel carregaJpanelTable() {
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
			ItemEstoque itemEstoque = (ItemEstoque) estoqueController.getItem();
			if (itemEstoque.getTipoProduto().getSuperTipoProduto() != null)
				cmbCategoriaProduto.getModel().setSelectedItem(itemEstoque.getTipoProduto().getSuperTipoProduto());
			if (itemEstoque.getTipoProduto() != null)
				cmbSubCategoriaProduto.setSelectedItem(itemEstoque.getTipoProduto());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
