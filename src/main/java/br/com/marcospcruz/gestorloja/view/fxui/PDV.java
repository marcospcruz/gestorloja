package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PDV extends StageBase {
	private static final String VALOR_R$ = "Valor (R$):";
	//@formatter:off
	private static final Object[] COLUNAS_TABLEVIEW = { 
			 
			"codigoProduto", 
			"fabricante", 
			"categoria", 
			"produto",
			"quantidade", 
			"valorProduto", 
			"valorVenda", 
			"valorTotal" 
			};
	//@formatter:on
	private AutoCompleteTextField<ItemEstoque> estoqueDropDown;
	private RadioButton radioCodigoBarra;
	private RadioButton radioDescricaoProduto;
	private Button adicionarBtn;
	private Button removerBtn;
	private Label fabricanteLabel;
	private Label subCategoriaLabel;
	private Label produtoLabel;
	private Label valorUnitLabel;
	private NumberTextField quantidadeTxt;

	private List<Node> nodesDadosProdutos;
	private List<Node> dadosProdutos;
	private Label marcaLabel;
	private Label categoriaLbl;
	private Label produtoLbl;
	private Label valorUnitLbl;
	private Label qtLabel;
	private NumberTextField descontoTxt;
	private NumberTextField subTotalTxt;
	private Label trocoLabel;
	private CheckBox formaPagamentoDinheiroCheckBox;
	// private NumberTextField valorDinheiroTxt;
	private Map<String, Node> formaPagamentoMap;
	private CheckBox formapagamentoCartaoDebitoCheckBox;
	private CheckBox formapagamentoCartaoCreditoCheckBox;
	private CheckBox formapagamentoOutrosCheckBox;
	private ItemEstoque itemEstoque;
	private TableView<ItemVendaModel> table;
	private String selectedCodigoBarra;

	public PDV() {
		super();

		String dataAtual = Util.formataData(SingletonManager.getInstance().getData());
		setTitle("Vendas - " + dataAtual);
		try {
			getVendaController().resetVenda();
		} catch (Exception e) {

			e.printStackTrace();
		}

		double layoutsMaxWidth = scene.widthProperty().get();
		layoutsMaxWidth -= layoutsMaxWidth * (27d / 100d);
		super.setLayoutsMaxWidth(layoutsMaxWidth);
		TitledPane pesquisaPane = criaPesquisaPane();
		TitledPane produtoPane = criaProdutoPane();
		GridPane panel = criaGridPane();
		getvBox().getChildren().addAll(pesquisaPane, produtoPane, panel);
	}

	private GridPane criaGridPane() {
		GridPane gridPane = new GridPane();
		// gridPane.setPadding(new Insets(0, 15, 5, 0));
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.add(criaTablePane(), 0, 0, 1, 3);
		gridPane.add(criaSubTotalPane(), 1, 0);
		gridPane.add(criaFormasPagtoPanel(), 1, 1);
		gridPane.add(criaTrocoPanel(), 1, 2);
		return gridPane;
	}

	private TitledPane criaTrocoPanel() {
		TitledPane titledPane = criaTitledPane("Troco");
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.add(criaLabel("Troco (R$):"), 0, 0);
		trocoLabel = new Label();
		// trocoLabel = new TextField();
		// trocoLabel.setPrefColumnCount(8);
		grid.add(trocoLabel, 1, 0);
		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		double width = getPercentageWidth(80.999d);
		titledPane.setMaxWidth(width);
		return titledPane;
	}

	private TitledPane criaFormasPagtoPanel() {
		TitledPane titledPane = criaTitledPane("Formas de Pagamento");
		double width = getLayoutsMaxWidth() * 0.18629502790461694;

		titledPane.setMaxWidth(width);
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setVgap(10);
		formaPagamentoMap = new HashMap<>();
		//
		// flow.getChildren().addAll(criaDinheiroGrid(), criaFormaPagamentoDebitoPane(),
		// criaFormaPagamentoCreditoPane(),
		// criaFormaPagamentoOutros());
		grid.add(criaDinheiroGrid(), 0, 0);
		grid.add(criaFormaPagamentoDebitoPane(), 0, 1);
		grid.add(criaFormaPagamentoCreditoPane(), 0, 2);
		grid.add(criaFormaPagamentoOutrosPane(), 0, 3);
		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		return titledPane;
	}

	private Node criaFormaPagamentoOutrosPane() {
		GridPane grid = criaFormaPagamentoGridPane();
		formapagamentoOutrosCheckBox = criaCheckBox("Outros");

		grid.add(formapagamentoOutrosCheckBox, 0, 0, 2, 1);
		grid.add(criaLabel("Forma pagto:", false), 0, 1);
		AutoCompleteTextField<String> descricaoPagamento = new AutoCompleteTextField<>();
		descricaoPagamento.setDisable(true);
		descricaoPagamento.setMaxWidth(120);
		formaPagamentoMap.put(formapagamentoOutrosCheckBox.getText(), descricaoPagamento);
		grid.add(descricaoPagamento, 1, 1);

		grid.add(criaLabel(VALOR_R$, false), 0, 2);
		NumberTextField valor = criaNumberTextField();
		formaPagamentoMap.put(formapagamentoOutrosCheckBox.getText(), valor);
		grid.add(valor, 1, 2);
		return grid;
	}

	private CheckBox criaCheckBox(String string) {
		CheckBox checkBox = new CheckBox(string);
		checkBox.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		return checkBox;
	}

	private Node criaFormaPagamentoCreditoPane() {

		GridPane grid = criaFormaPagamentoGridPane();
		formapagamentoCartaoCreditoCheckBox = criaCheckBox("Crédito");
		grid.add(formapagamentoCartaoCreditoCheckBox, 0, 0, 2, 1);
		grid.add(criaLabel("Parcelas:", false), 0, 1);
		NumberTextField parcelasTxt = criaNumberTextField();
		grid.add(parcelasTxt, 1, 1);
		grid.add(criaLabel(VALOR_R$, false), 0, 2);
		NumberTextField valor = criaNumberTextField();
		formaPagamentoMap.put(formapagamentoCartaoCreditoCheckBox.getText(), valor);
		grid.add(valor, 1, 2);
		return grid;
	}

	private Node criaFormaPagamentoDebitoPane() {
		GridPane grid = criaFormaPagamentoGridPane();
		formapagamentoCartaoDebitoCheckBox = criaCheckBox("Débito:");
		grid.add(formapagamentoCartaoDebitoCheckBox, 0, 0, 2, 1);
		grid.add(criaLabel(VALOR_R$, false), 0, 1);
		NumberTextField valorDebito = criaNumberTextField();
		formaPagamentoMap.put(formapagamentoCartaoDebitoCheckBox.getText(), valorDebito);
		grid.add(valorDebito, 1, 1);
		return grid;
	}

	private GridPane criaDinheiroGrid() {
		GridPane grid = criaFormaPagamentoGridPane();
		//
		formaPagamentoDinheiroCheckBox = criaCheckBox("Dinheiro");

		grid.add(formaPagamentoDinheiroCheckBox, 0, 0, 2, 1);
		grid.add(criaLabel(VALOR_R$, false), 0, 1);
		NumberTextField valorDinheiroTxt = criaNumberTextField();
		formaPagamentoMap.put(formaPagamentoDinheiroCheckBox.getText(), valorDinheiroTxt);
		grid.add(valorDinheiroTxt, 1, 1);

		return grid;
	}

	protected NumberTextField criaNumberTextField() {
		NumberTextField valorDinheiroTxt = new NumberTextField();
		valorDinheiroTxt.setPrefColumnCount(8);
		valorDinheiroTxt.setDisable(true);
		return valorDinheiroTxt;
	}

	private GridPane criaFormaPagamentoGridPane() {

		return criaFormaPagamentoGridPane(2);
	}

	protected GridPane criaFormaPagamentoGridPane(int vGap) {
		GridPane grid = new GridPane();
		// grid.setAlignment(Pos.BASELINE_RIGHT);
		grid.setHgap(10);
		grid.setVgap(vGap);
		return grid;
	}

	private TitledPane criaSubTotalPane() {
		TitledPane titledPane = criaTitledPane("Sub-Total");
		double width = getPercentageWidth();
		titledPane.setMaxWidth(width);
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(5);

		grid.add(criaLabel("Desconto %:"), 0, 0);
		descontoTxt = new NumberTextField();
		descontoTxt.setText("0");
		descontoTxt.setPrefColumnCount(3);
		grid.add(descontoTxt, 1, 0);
		grid.add(criaLabel("Sub Total(R$):"), 0, 1);
		subTotalTxt = new NumberTextField();
		subTotalTxt.setPrefColumnCount(8);
		subTotalTxt.setText(Util.formataMoeda(0f));
		grid.add(subTotalTxt, 1, 1);

		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		return titledPane;
	}

	protected double getPercentageWidth() {
		return getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (99d / 100d));
	}

	protected double getPercentageWidth(double percentage) {
		return getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (percentage / 100d));
	}

	private TitledPane criaTablePane() {
		TitledPane titledPane = criaTitledPane("Ítens da Venda");
		// criaTablePane(title)
		titledPane.setMaxHeight(height);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		pane.setAlignment(Pos.CENTER);
		setLabelColunas(COLUNAS_TABLEVIEW);
		pane.setVgap(10);
		// double width = getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (1.5d / 100d));
		double width = getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (21d / 100d));
		table = criaTableView(width);
		// table.setMaxWidth();
		titledPane.setMaxHeight(height);
		pane.getChildren().add(table);
		Button button = new Button("Finalizar Venda");
		button.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		pane.getChildren().add(button);

		titledPane.setContent(pane);
		return titledPane;
	}

	private TitledPane criaProdutoPane() {
		FlowPane produtoFlowPane = createHorizontalFlowPane();
		produtoFlowPane.setAlignment(Pos.CENTER);
		TitledPane pane = criaTitledPane("Produto");
		produtoFlowPane.setHgap(10);
		pane.setMaxWidth(getLayoutsMaxWidth());
		pane.setMinWidth(getLayoutsMaxWidth());
		pane.setCollapsible(false);
		pane.setContent(produtoFlowPane);
		adicionaProdutoItems(produtoFlowPane);

		return pane;
	}

	protected void adicionaProdutoItems(FlowPane produtoFlowPane) {

		fabricanteLabel = new Label();
		ObservableList<Node> children = produtoFlowPane.getChildren();
		subCategoriaLabel = new Label();
		produtoLabel = new Label();
		valorUnitLabel = new Label();
		quantidadeTxt = new NumberTextField();
		quantidadeTxt.setText("1");
		quantidadeTxt.setPrefColumnCount(4);
		quantidadeTxt.setVisible(false);
		if (dadosProdutos == null)
			dadosProdutos = new ArrayList<>();
		dadosProdutos.add(quantidadeTxt);
		nodesDadosProdutos = new ArrayList<>();
		nodesDadosProdutos.add(quantidadeTxt);
		marcaLabel = criaLabel("Marca:");
		marcaLabel.setVisible(false);
		children.add(marcaLabel);
		nodesDadosProdutos.add(marcaLabel);
		children.add(fabricanteLabel);
		categoriaLbl = criaLabel("Categoria:");
		categoriaLbl.setVisible(false);
		children.add(categoriaLbl);
		nodesDadosProdutos.add(categoriaLbl);
		children.add(subCategoriaLabel);
		produtoLbl = criaLabel("Produto:");
		produtoLbl.setVisible(false);
		children.add(produtoLbl);
		nodesDadosProdutos.add(produtoLbl);
		children.add(produtoLabel);
		valorUnitLbl = criaLabel("Valor Unit.:");
		valorUnitLbl.setVisible(false);
		children.add(valorUnitLbl);
		nodesDadosProdutos.add(valorUnitLbl);
		children.add(valorUnitLabel);
		qtLabel = criaLabel("Quantidade:");
		qtLabel.setVisible(false);
		nodesDadosProdutos.add(qtLabel);
		children.add(qtLabel);
		children.add(quantidadeTxt);
		adicionarBtn = new Button("Adicionar");
		adicionarBtn.setOnAction(arg0 -> {
			try {
				adicionaCarrinho(arg0);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		});
		adicionarBtn.setVisible(false);
		children.add(adicionarBtn);
		removerBtn = new Button("Remover");
		removerBtn.setOnAction(arg0 -> {
			try {
				removeCarrinho(arg0);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		});
		removerBtn.setVisible(false);
		removerBtn.setDisable(true);
		children.add(removerBtn);
		dadosProdutos.add(fabricanteLabel);
		dadosProdutos.add(subCategoriaLabel);
		dadosProdutos.add(valorUnitLabel);
		dadosProdutos.add(produtoLabel);
		nodesDadosProdutos.add(adicionarBtn);
		nodesDadosProdutos.add(removerBtn);
	}

	private Label criaLabel(String string, boolean setBold) {
		Label label = new Label(string);
		if (setBold)
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

		return label;

	}

	private Label criaLabel(String string) {
		return criaLabel(string, true);
	}

	private TitledPane criaPesquisaPane() {
		FlowPane flowPane = createHorizontalFlowPane();
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setHgap(20);
		TitledPane pane = criaTitledPane("Pesquisa Estoque");
		pane.setMaxWidth(getLayoutsMaxWidth());
		pane.setMinWidth(getLayoutsMaxWidth());
		pane.setCollapsible(false);

		try {
			estoqueDropDown = criaEstoqueProdutoDropDown();
			estoqueDropDown.setOnAction(this::populaProduto);
			radioCodigoBarra = new RadioButton("Código de Barras");
			radioCodigoBarra.setSelected(true);
			ToggleGroup group = new ToggleGroup();
			radioCodigoBarra.setToggleGroup(group);
			radioDescricaoProduto = new RadioButton("Descrição de Produto");
			radioDescricaoProduto.setToggleGroup(group);
			flowPane.getChildren().add(estoqueDropDown);
			flowPane.getChildren().add(radioCodigoBarra);
			flowPane.getChildren().add(radioDescricaoProduto);
		} catch (Exception e) {

			e.printStackTrace();
		}

		pane.setContent(flowPane);
		return pane;
	}

	private void removeCarrinho(ActionEvent event) throws Exception {
		try {
			VendaController vendaController = getVendaController();
			vendaController.devolveProduto(selectedCodigoBarra);
			subTotalTxt.setText(Util.formataMoeda(vendaController.getSubTotal()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			carregaDadosTable(table);
			limpaDadosProduto();
		}

	}

	protected void limpaDadosProduto() {
		exibeEscondeProdutosNodes(false);
		estoqueDropDown.getEditor().setText("");
		dadosProdutos.stream().forEach(node -> {

			if (node instanceof Label)
				((Label) node).setText("");
			else
				((TextField) node).setText("1");
		});
		removerBtn.setDisable(true);
	}

	private void adicionaCarrinho(ActionEvent event) throws Exception {
		try {
			int quantidade = Integer.parseInt(quantidadeTxt.getText());
			VendaController controller = super.getVendaController();
			// ItemVenda itemVenda = new ItemVenda();
			// itemVenda.setQuantidade(quantidade);
			// itemVenda.setItemEstoque(itemEstoque);
			// controller.setItemVenda(itemVenda);
			controller.populaItemEstoque(itemEstoque, quantidade);
			controller.adicionaProdutoLista();
			float subTotal = controller.getVenda().getTotalVendido();
			subTotalTxt.setText(Util.formataMoeda(subTotal));
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		} finally {
			carregaDadosTable(table);
			limpaDadosProduto();
		}

	}

	@Override
	protected void handleTableClick(Event event) {
		try {
			selectedCodigoBarra = getTableViewSelectedValueId(event);

			ItemVenda itemVenda = getVendaController().getItemVenda(selectedCodigoBarra);
			ItemEstoque itemEstoque = itemVenda.getItemEstoque();
			populaDadosProduto(itemEstoque);
			removerBtn.setDisable(false);
			quantidadeTxt.setText(itemVenda.getQuantidade().toString());
			if (itemEstoque.getQuantidade() < itemVenda.getQuantidade()) {
				adicionarBtn.setDisable(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// populaDadosProduto();
	}

	private void populaProduto(ActionEvent event) {
		removerBtn.setDisable(true);
		Object estoqueValue = estoqueDropDown.getValue();
		boolean ehItemEstoque = estoqueValue instanceof ItemEstoque;
		if (ehItemEstoque && radioDescricaoProduto.isSelected()) {
			populaDadosProduto((ItemEstoque) estoqueValue);
			adicionarBtn.setDisable(false);
		} else if (estoqueValue != null && estoqueValue instanceof String && radioCodigoBarra.isSelected()) {
			populaDadosProduto((String) estoqueValue);
			adicionarBtn.setDisable(false);
		} else if (estoqueValue != null) {
			System.out.println(estoqueValue);
		}
	}

	private void populaDadosProduto(String codigoDeBarras) {
		try {
			EstoqueController controller = getEstoqueController();
			controller.busca(codigoDeBarras);
			populaDadosProduto((ItemEstoque) controller.getItem());
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	protected void populaDadosProduto(ItemEstoque estoqueValue) {
		itemEstoque = (ItemEstoque) estoqueValue;
		Produto produto = itemEstoque.getProduto();
		Fabricante fabricante = itemEstoque.getFabricante();
		SubTipoProduto subCategoria = itemEstoque.getTipoProduto();
		String valorUnitario = Util.formataMoeda(itemEstoque.getValorUnitario());
		fabricanteLabel.setText(fabricante.getNome());
		subCategoriaLabel.setText(subCategoria.getDescricaoTipo());
		valorUnitLabel.setText(valorUnitario);
		produtoLabel.setText(produto.getDescricaoProduto());
		exibeEscondeProdutosNodes(true);
	}

	private void exibeEscondeProdutosNodes(boolean showNode) {
		nodesDadosProdutos.stream().forEach(node -> {
			node.setVisible(showNode);

		});

	}

	private AutoCompleteTextField<ItemEstoque> criaEstoqueProdutoDropDown() {
		AutoCompleteTextField<ItemEstoque> lista = new AutoCompleteTextField<>();
		lista.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		try {
			EstoqueController controller = getEstoqueController();

			ObservableList<ItemEstoque> items = lista.getItems();
			if (controller.getList() == null || controller.getList().isEmpty())
				controller.buscaTodos();
			// items.add(new I("Selecione uma opção.", null));
			items.addAll(controller.getList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	void reloadForm() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		ObservableList<ItemVendaModel> items = table.getItems();
		items.removeAll(items);
		VendaController vendaController = getVendaController();
		vendaController.getList().stream().forEach(itemVenda -> {
			ItemVenda item = (ItemVenda) itemVenda;

			ItemVendaModel itemVendaModel = new ItemVendaModel(item);

			items.add(itemVendaModel);
		});
		// items.addAll(vendaController.getList());
		table.refresh();

	}

	@Override
	void populaForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvaDados(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void excluiDados(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
