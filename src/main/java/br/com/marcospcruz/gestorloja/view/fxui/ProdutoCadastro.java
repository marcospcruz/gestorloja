package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;

import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ProdutoCadastro extends CadastroBase {

	private static final Object[] COLUNAS = { "cod", "produto", "itensEstoque" };
	private TextField descricaoProduto;
	private AutoCompleteTextField<Produto> produtos;

	public ProdutoCadastro() throws Exception {
		super(COLUNAS, getProdutoController());

		setTitle("Produto");
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaForm() {
		Produto produto = (Produto) controller.getItem();

		String descricao = produto.getDescricaoProduto();
		btnExcluir.setDisable(produto.getIdProduto() == null);
		descricaoProduto.setText(descricao);
	}

	void populaTableView() {
		TitledPane tablePane = super.criaTablePane("Produtos");
		// tablePane.setMaxHeight(500d);
		// tablePane.setMinHeight(220d);
		getGrid().add(tablePane, 0, 2, 2, 1);

	}

	void populaCadastroForm() {
		TitledPane formPane = new TitledPane("Dados Produto", new Button());
		formPane.setCollapsible(Boolean.FALSE);
		double thisWidth = getLayoutsMaxWidth();
		formPane.setMaxWidth(thisWidth);
		formPane.setMinWidth(thisWidth);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);

		// pane.setPadding(new Insets(20, 20, 20, 20));
		pane.setHgap(10);
		// categoriaProduto = new TextField();
		// superTiposProduto = super.createCategoriaProdutoAutotextFieldBox();
		// pane.getChildren().addAll(new Label("Descrição Categoria"), categoriaProduto,
		// new Label("Super Categoria Produto"),categoriaProduto, buttonsPane());
		//
		// formPane.setContent(pane);
		GridPane grid = new GridPane();

		grid.setAlignment(Pos.TOP_CENTER);
		int h = 125;
		grid.setMinHeight(h);
		grid.setMinWidth(getWidth());
		grid.setMaxHeight(h);
		grid.setVgap(5);
		int row = 0;
		Label descricaoLabel = new Label("Descrição Produto");
		descricaoProduto = new TextField();
		grid.add(descricaoLabel, 0, row);
		grid.add(descricaoProduto, 1, row++);

		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();

		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.ALWAYS);
		column2.setFillWidth(true);
		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		grid.add(buttonsPane(), 1, 8, 2, 1);
		pane.getChildren().add(grid);
		formPane.setContent(pane);
		getGrid().add(formPane, 0, 1, 2, 1);

	}

	protected void populaPesquisaPanel() {
		TitledPane pesquisaPane = new TitledPane("Pesquisa", new Button());

		pesquisaPane.setMaxWidth(getLayoutsMaxWidth());
		pesquisaPane.setMinWidth(getLayoutsMaxWidth());
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		// BorderPane pane = new BorderPane();
		// Insets insets = new Insets(20, 0, 20, 20);
		// pane.setPadding(insets);
		// pane.setMaxWidth(getLayoutsMaxWidth());
		// pane.setMinWidth(300);
		pesquisaPane.setCollapsible(Boolean.FALSE);
		produtos = super.criaProdutoComboBox(Boolean.FALSE);
		produtos.prefWidthProperty().bind(pesquisaPane.prefWidthProperty());
		produtos.setOnAction(this::pesquisaItem);
		// VBox vbox = new VBox();
		// vbox.setSpacing(10);
		// vbox.setPadding(insets);
		produtos.setMaxWidth(Double.MAX_VALUE);
		pane.getChildren().add(produtos);

		// vbox.getChildren().add(fabricante);
		// pane.getChildren().addAll(vbox);
		pesquisaPane.setContent(pane);
		getGrid().add(pesquisaPane, 0, 0, 2, 1);
	}

	@Override
	protected void salvaDados(ActionEvent event) {
		String descricao = descricaoProduto.getText();
		Produto produto = (Produto) controller.getItem();
		produto.setDescricaoProduto(descricao);
		try {
			super.validaDados(descricao);
			boolean confirmation = showConfirmAtionMessage("Deseja salvar estes Dados?");
			if (confirmation)
				super.salvaDados(event);
			controller.buscaTodos();
			produtos.getEditor().setText("");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		ProdutoController controller = getProdutoController();
		
		ObservableList<ProdutoModel> items = table.getItems();
		items.removeAll(items);
		controller.getList().stream().forEach(produto -> {

			ProdutoModel model = new ProdutoModel(produto.getIdProduto(), produto.getDescricaoProduto(),
					produto.getEstoqueProduto().size());

			items.add(model);

		});

		// items.addAll(controller.getList());
		table.setItems(items);

	}

	@Override
	void reloadForm() throws Exception {
		populaForm();
		reloadTableView("Produtos");
		super.criaObservableList(produtos.getItems(), getProdutoController());
		produtos.getEditor().setText("");

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		if (!(produtos.getValue() instanceof Produto))
			return;

		try {
			Produto produto = produtos.getValue();
			controller.setItem(produto);
			controller.setList(new ArrayList<>());
			controller.getList().add(produto);
			reloadForm();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}