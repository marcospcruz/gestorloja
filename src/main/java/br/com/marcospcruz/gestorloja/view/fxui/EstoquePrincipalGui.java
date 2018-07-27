package br.com.marcospcruz.gestorloja.view.fxui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.ItemEstoqueModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstoquePrincipalGui extends StageBase {

	private static final Object[] COLUNAS_JTABLE = {
			// ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			"cod", "fabricante", // ConstantesEnum.CATEGORIA_LABEL.getValue().toString(),
									// ConstantesEnum.TIPO_ITEM_LABEL.getValue().toString(),
			"categoria", // ConstantesEnum.DESCRICAO_ITEM_LABEL.getValue().toString(),
			// ConstantesEnum.CODIGO_DE_BARRAS.getValue().toString(),
			"subCategoria", // ConstantesEnum.QUANTIDADE_LABEL.getValue().toString(),
			"produto", // ConstantesEnum.VALOR_UNITARIO_LABEL.getValue().toString(),
			"qt", // ConstantesEnum.VALOR_TOTAL_LABEL.getValue().toString()
			"vlUnitario", "vlTotal" };
	private EstoqueController controller;
	private ObservableList<ItemEstoqueModel> dadosEstoque;
	private VBox vBox;
	private Scene scene;

	public EstoquePrincipalGui() throws Exception {
		controller = getEstoqueController();
		controller.buscaTodos();
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		super.width = screenSize.getWidth() - (screenSize.getWidth() * DEZ_PORCENTO);
		super.height = screenSize.getHeight() - (screenSize.getHeight() * DEZ_PORCENTO);
		super.systemOut(width, height);
		scene = new Scene(new Group(), width, height);
		GridPane gridPane = new GridPane();
		vBox = new VBox();
		vBox.setSpacing(5);
		vBox.setPadding(new Insets(10, 0, 0, 10));

		TitledPane pesquisaPane = criaPesquisaPanel();
		TitledPane cadastroPane = criaCadastroPanel();
		vBox.getChildren().addAll(cadastroPane, pesquisaPane);
		((Group) scene.getRoot()).getChildren().add(vBox);
		criaTableView();

		setScene(scene);
	}

	private void abreTelaCadastro(Stage stage) {
		stage.initOwner(this);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		reloadTableView();
	}

	private TitledPane criaCadastroPanel() {

		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
		flowPane.setHgap(10);
		TitledPane pane = new TitledPane("Cadastros", new Button(""));
		pane.setCollapsible(false);
		//
		Button itemEstoqueCadastro = new Button("Novo Item Estoque");
		itemEstoqueCadastro.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage stage = new ItemEstoqueCadastro();
				abreTelaCadastro(stage);
			}

		});
		flowPane.getChildren().add(itemEstoqueCadastro);

		//
		pane.setContent(flowPane);
		double teste = scene.widthProperty().get();
		flowPane.prefWidthProperty().bind(new SimpleDoubleProperty(teste).subtract(teste * (26.8d / 100d)));
		return pane;
	}

	private TitledPane criaPesquisaPanel() throws Exception {
		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
		flowPane.setHgap(10);
		TitledPane pane = new TitledPane("Pesquisa de Produto", new Button(""));
		pane.setCollapsible(false);

		ComboBox<TipoProduto> categoriaProduto = super.createCategoriaProdutoComboBox();
		// categoriaProduto.setEditable(true);
		TipoProdutoController tipoProdutoController = super.getTipoProdutoController();
		AutoCompleteTextField<SubTipoProduto> subCategoriaProduto = new AutoCompleteTextField<>();
		categoriaProduto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// ((TextField)subCategoriaProduto.getEditor()).setText("");
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
		AutoCompleteTextField<Fabricante> comboFabricantes = super.criaFabricanteComboBox(true);
		comboFabricantes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					EstoqueController controller = getEstoqueController();
					Fabricante fabricante = comboFabricantes.getValue();
					controller.busca(null, null, fabricante.getNome());
					reloadTableView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		flowPane.getChildren().addAll(new Label("Fabricante"), comboFabricantes, new Label("Categoria:"),
				categoriaProduto, new Label("Sub-Categoria:"), subCategoriaProduto);
		pane.setContent(flowPane);
		double teste = scene.widthProperty().get();
		flowPane.prefWidthProperty().bind(new SimpleDoubleProperty(teste).subtract(teste * (26.8d / 100d)));
		return pane;
	}

	private void reloadTableView() {
		TableView<ItemEstoqueModel> table = (TableView<ItemEstoqueModel>) vBox.getChildren().stream()
				.filter(t -> t instanceof TableView).findFirst().orElse(null);
		if (table != null)
			vBox.getChildren().remove(table);
		criaTableView();
	}

	private void reloadDadosEstoque(TipoProduto categoria) throws Exception {
		EstoqueController estoqueController = StageBase.getEstoqueController();

		estoqueController.busca(categoria.getDescricaoTipo(), null, null);
		reloadTableView();
	}

	private TableView<ItemEstoqueModel> criaTableView() {
		TableView<ItemEstoqueModel> table = null;
		try {

			table = new TableView<>();

			table.setColumnResizePolicy((param) -> true);

			// table
			carregaDadosTable(table);

			carregaTableView(table);

			//
			double teste = scene.widthProperty().get();

			table.prefWidthProperty().bind(new SimpleDoubleProperty(teste).subtract(teste * (26.8d / 100d)));
			ObservableList<Node> children = vBox.getChildren();
			children.add(table);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (table != null)
				customResize(table);
		}
		return table;
	}

	private void carregaTableView(TableView<ItemEstoqueModel> table) {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		Arrays.asList(COLUNAS_JTABLE).stream().forEach(columnName -> {
			TableColumn<ItemEstoqueModel, String> tableColumn = new TableColumn<>(columnName.toString());
			tableColumn.setCellValueFactory(new PropertyValueFactory<ItemEstoqueModel, String>(columnName.toString()));
			// tableColumn.setResizable(false);

			table.getColumns().add(tableColumn);

		});
	}

	private void customResize(TableView<?> table) {

		new Runnable() {

			@Override
			public void run() {
				AtomicLong width = new AtomicLong();
				table.getColumns().forEach(col -> {
					width.addAndGet((long) col.getWidth());
				});
				double tableWidth = table.getWidth();

				if (tableWidth > width.get()) {
					table.getColumns().forEach(col -> {
						col.setPrefWidth(col.getWidth() + ((tableWidth - width.get()) / table.getColumns().size()));
					});
				}

			}
		};
	}

	private void carregaDadosTable(TableView<ItemEstoqueModel> table) throws Exception {

		List<ItemEstoqueModel> estoque = new ArrayList<>();

		controller.getList().stream().forEach(item -> {
			ItemEstoque itemEstoque = (ItemEstoque) item;
			//@formatter:off
			estoque.add(new ItemEstoqueModel(
					itemEstoque.getCodigoDeBarras(), 
					itemEstoque.getFabricante().getNome(),
					itemEstoque.getTipoProduto().getSuperTipoProduto().getDescricaoTipo(),
					itemEstoque.getTipoProduto().getDescricaoTipo(),
					itemEstoque.getProduto().getDescricaoProduto(),
					itemEstoque.getQuantidade().toString(),
					Util.formataMoeda(itemEstoque.getValorUnitario()),
					Util.formataMoeda(itemEstoque.getQuantidade()*itemEstoque.getValorUnitario())));
			//@formatter:on
		});
		dadosEstoque = FXCollections.observableArrayList(estoque);
		table.setItems(dadosEstoque);
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
