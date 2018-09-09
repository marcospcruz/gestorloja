package br.com.marcospcruz.gestorloja.view.fxui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.FabricanteController;
import br.com.marcospcruz.gestorloja.facade.ImportadorArquivo;
import br.com.marcospcruz.gestorloja.facade.ImportadorArquivoCsv;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.ItemEstoqueModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class EstoquePrincipalGui extends StageBase {
//@formatter:off
	private static final Object[] COLUNAS_JTABLE = {
			// ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			"cod", 
			"fabricante", // ConstantesEnum.CATEGORIA_LABEL.getValue().toString(),
									// ConstantesEnum.TIPO_ITEM_LABEL.getValue().toString(),
			"categoria", // ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString(),
			// ConstantesEnum.CODIGO_DE_BARRAS.getValue().toString(),
			"subCategoria", // ConstantesEnum.QUANTIDADE_LABEL.getValue().toString(),
			"produto", // ConstantesEnum.VALOR_UNITARIO_LABEL.getValue().toString(),
			"qt", // ConstantesEnum.VALOR_TOTAL_LABEL.getValue().toString()
			"vlUnitario", 
			"vlTotal" };//@formatter:on

	// private ObservableList<ItemEstoqueModel> dadosEstoque;

	private AutoCompleteTextField<Fabricante> comboFabricantes;
	private AutoCompleteTextField<TipoProduto> categoriaProduto;
	// private TableView<ItemEstoqueModel> table;

	private AutoCompleteTextField<SubTipoProduto> subCategoriaProduto;

	public EstoquePrincipalGui() throws Exception {
		super();
		super.setLabelColunas(COLUNAS_JTABLE);
		controller = getEstoqueController();
		controller.buscaTodos();

		double layoutsMaxWidth = SingletonManager.getInstance().getScreenWidth();
		// layoutsMaxWidth -= layoutsMaxWidth * (5d / 100d);
		super.setLayoutsMaxWidth(layoutsMaxWidth);

		TitledPane pesquisaPane = criaPesquisaPanel();
		TitledPane cadastroPane = criaCadastroPanel();
		TitledPane operacoes = criaOperacoesPanel();
		TitledPane tablePane = super.criaTablePane("Estoque de Produtos");
		getvBox().getChildren().addAll(cadastroPane, pesquisaPane, tablePane);
		getvBox().getChildren().add(operacoes);

	}

	@Override
	protected void handleTableClick(Event event) {
		super.handleTableClick(event);
		ItemEstoque item = (ItemEstoque) controller.getItem();
		System.out.println(item);
		try {
			abreTelaCadastro(new ItemEstoqueCadastro());

			reloadForm();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void reloadComboCategoria() throws Exception {
		ObservableList<TipoProduto> items = categoriaProduto.getItems();
		super.criaObservableList(items, getTipoProdutoController());
		categoriaProduto.setItems(items);
		ObservableList<SubTipoProduto> subItems = subCategoriaProduto.getItems();
		subItems.removeAll(subItems);
	}

	private void reloadComboFabricantes() throws Exception {
		FabricanteController controller = getFabricanteController();
		controller.setList(null);
		controller.buscaTodos();
		ObservableList<Fabricante> items = comboFabricantes.getItems();
		items.removeAll(items);
		// controller.buscaTodos();
		items.addAll(controller.getList());
	}

	private TitledPane criaOperacoesPanel() {
		TitledPane pane = new TitledPane("Operação", new Button());
		pane.setCollapsible(false);
		FlowPane content = new FlowPane(Orientation.HORIZONTAL);
		Button btn = new Button("Importar Dados de Estoque");
		btn.setOnAction(this::importaDadosEstoque);
		content.getChildren().add(btn);
		pane.setContent(content);
		return pane;
	}

	private void importaDadosEstoque(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Escolha o arquivo");
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Planilha Excel", ".xlsx"));

		File planilha = fileChooser.showOpenDialog(this);

		ImportadorArquivo importador = new ImportadorArquivoCsv(planilha);
		showMensagemSucesso("Iniciando importação de dados de estoque.");
		Service<Void> service = new Service() {

			@Override
			protected Task createTask() {
				try {
					importador.carregaDados();
					showMensagemSucesso(importador.getMensagemRetorno());
					reloadForm();
				} catch (Exception e) {
					showErrorMessage(e.getMessage());
					e.printStackTrace();
				}

				return null;
			}

		};
		service.start();
	}

	private TitledPane criaCadastroPanel() {

		FlowPane flowPane = createHorizontalFlowPane();
		TitledPane pane = criaTitledPane("Cadastros");
		//
		Button itemEstoqueCadastro = criaButton("Novo Item Estoque");
		Button fabricantesCadBtn = criaButton("Fabricantes / Marca");
		Button categoriaProduto = criaButton("Categoria Produto");
		Button produto = criaButton("Produto");
		flowPane.getChildren().addAll(fabricantesCadBtn, categoriaProduto, produto, itemEstoqueCadastro);

		//
		pane.setContent(flowPane);

		pane.setMaxWidth(getLayoutsMaxWidth());
		return pane;
	}

	protected Button criaButton(String title) {
		Button itemEstoqueCadastro = new Button(title);
		itemEstoqueCadastro.setOnAction(this::handleAbreCadastro);
		return itemEstoqueCadastro;
	}

	private void handleAbreCadastro(ActionEvent actionEvent) {

		try {
			Stage stage = null;
			Button source = (Button) actionEvent.getSource();
			String buttonTitle = source.getText();
			switch (buttonTitle) {
			case "Fabricantes / Marca":
				stage = new FabricantesCadastro();
				break;
			case "Categoria Produto":
				stage = new CategoriaProdutoCadastro();
				break;
			case "Produto":
				stage = new ProdutoCadastro();
				break;
			default:
				stage = new ItemEstoqueCadastro();
			}
			abreTelaCadastro(stage);

			reloadForm();
		} catch (Exception e) {

			e.printStackTrace();
			showErrorMessage(e.getMessage());
		}

	}

	void reloadForm() throws Exception {
		comboFabricantes.getEditor().setText("");
		categoriaProduto.getEditor().setText("");
		subCategoriaProduto.getEditor().setText("");
		reloadComboFabricantes();
		reloadComboCategoria();
		controller.setCacheMap(null);
		controller.buscaTodos();
		reloadTableView();
		controller.setItem(null);
	}

	private TitledPane criaPesquisaPanel() throws Exception {
		FlowPane flowPane = createHorizontalFlowPane();
		TitledPane pane = new TitledPane("Pesquisa de Produto", new Button(""));
		pane.setCollapsible(false);

		categoriaProduto = super.createCategoriaProdutoAutotextFieldBox();
		subCategoriaProduto = new AutoCompleteTextField<>();
		categoriaProduto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// ((TextField)subCategoriaProduto.getEditor()).setText("");
				if (!(categoriaProduto.getValue() instanceof TipoProduto))
					return;
				TipoProduto categoria = categoriaProduto.getValue();
				List<SubTipoProduto> subCategorias = (List<SubTipoProduto>) categoria.getSubTiposProduto();
				subCategoriaProduto.setItems(subCategorias);

				try {
					reloadDadosEstoque(categoria);
					// StageBase.autoResizeColumns(table);
					// carregaDadosTable(table);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		subCategoriaProduto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					SubTipoProduto categoria = subCategoriaProduto.getValue();
					if (categoria != null) {
						reloadDadosEstoque(categoria);
					}
				} catch (Exception e) {
					try {
						reloadDadosEstoque(categoriaProduto.getValue());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		comboFabricantes = super.criaFabricanteComboBox(true);
		comboFabricantes.setOnAction(new EventHandler<ActionEvent>() {

			private boolean showAlert = true;

			@Override
			public void handle(ActionEvent event) {
				try {
					EstoqueController controller = getEstoqueController();
					if (!(comboFabricantes.getValue() instanceof Fabricante))
						return;
					showAlert = true;
					Fabricante fabricante = comboFabricantes.getValue();
					controller.busca(null, null, fabricante.getNome());
					reloadTableView();
					// carregaDadosTable(table);
				} catch (Exception e) {
					if (showAlert) {
						showErrorMessage("Dados não encontrados.");
						showAlert = false;
					}
					e.printStackTrace();
				}

			}

		});
		flowPane.getChildren().addAll(new Label("Fabricante"), comboFabricantes, new Label("Categoria:"),
				categoriaProduto, new Label("Sub-Categoria:"), subCategoriaProduto);
		pane.setContent(flowPane);

		// flowPane.prefWidthProperty().bind(new
		// SimpleDoubleProperty(teste).subtract(teste * (50d / 100d)));
		// flowPane.prefWidthProperty().bind(new SimpleDoubleProperty(teste));
		pane.setMinWidth(getLayoutsMaxWidth());
		pane.setMaxWidth(getLayoutsMaxWidth());
		return pane;
	}

	private void reloadDadosEstoque(TipoProduto categoria) throws Exception {
		EstoqueController estoqueController = StageBase.getEstoqueController();

		estoqueController.busca(categoria.getDescricaoTipo(), null, null);
		reloadTableView();

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {

		List<ItemEstoqueModel> estoque = new ArrayList<>();
		if (controller.getList().isEmpty())
			controller.buscaTodos();

		controller.getList().stream().forEach(item -> {
			ItemEstoque itemEstoque = (ItemEstoque) item;
			try {
				//@formatter:off
				estoque.add(new ItemEstoqueModel(estoque.size(),
						itemEstoque.getCodigoDeBarras(), 
						itemEstoque.getFabricante().getNome(),
						itemEstoque.getTipoProduto().getSuperTipoProduto().getDescricaoTipo(),
						itemEstoque.getTipoProduto().getDescricaoTipo(),
						itemEstoque.getProduto().getDescricaoProduto(),
						itemEstoque.getQuantidade().toString(),
						Util.formataMoeda(itemEstoque.getValorUnitario()),
						Util.formataMoeda(itemEstoque.getQuantidade()*itemEstoque.getValorUnitario())));
				//@formatter:on
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		});

		ObservableList dadosEstoque = FXCollections.observableArrayList(estoque);
		// FilteredList<ItemEstoqueModel> sorteredDados=new
		// FilteredList<>(dadosEstoque,estoqueModel->estoqueModel.getIndexOf()<maxValues);
		table.setItems(dadosEstoque);
		table.refresh();
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvaDados(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void excluiDados(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	protected void reloadTableView() throws Exception {
		//
		ObservableList<Node> children = getvBox().getChildren();
		TitledPane pane = (TitledPane) children.stream()
				.filter(t -> ((TitledPane) t).getText().equals("Estoque de Produtos")).findFirst().orElse(null);
		Node teste = null;
		if (pane != null)
			teste = pane.getContent();
		TableView<ItemEstoqueModel> table = (TableView<ItemEstoqueModel>) ((Pane) teste).getChildren().get(0);

		// children.stream().forEach(t -> {
		// System.out.println(((TitledPane) t).getText().equals("Estoque de Produtos"));
		// });

		// int indexOfTable = getvBox().getChildren().indexOf(table);
		// if (table != null)
		// vBox.getChildren().remove(table);
		ObservableList<ItemEstoqueModel> items = table.getItems();
		items.removeAll(items);
		carregaDadosTable(table);
		// vBox.getChildren().add(indexOfTable, table);
		// if(vBox.getChildren().contains(this.table)) {
		// vBox.getChildren().
		// }
	}

	@Override
	void populaForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
