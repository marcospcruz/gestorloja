package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;

import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.ItemEstoqueModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public abstract class CadastroBase extends StageBase {

	private static final int HEIGHT = 600;
	protected static final int WIDTH = 600;
	protected GridPane grid;

	protected CadastroBase() {
		this(null, null);

	}

	public CadastroBase(Object[] colunas, ControllerBase controller) {
		this.controller = controller;
		if (controller != null)
			controller.novo();
		if (colunas != null)
			super.setLabelColunas(colunas);
		super.setDimension(WIDTH, HEIGHT);
		// double thisWidth = (double) WIDTH - ((double) WIDTH * 10 / 100);
		setLayoutsMaxWidth(width-(width*5/100));
		resizableProperty().setValue(Boolean.FALSE);
		Group root = new Group();
		scene = new Scene(root, WIDTH, HEIGHT);
		grid = new GridPane();

		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setMinHeight(WIDTH);
		grid.setMinWidth(HEIGHT);
		grid.setMaxHeight(HEIGHT);
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.ALWAYS);
		column2.setFillWidth(true);
		populaGridContent();
	
		populaForm();
		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		// grid.add(buttonsPane(), 1, 8, 2, 1);

		root.getChildren().add(grid);

		setScene(scene);

	}

	protected GridPane getGrid() {
		return grid;
	}

	public void setGrid(GridPane grid) {
		this.grid = grid;
	}

	protected abstract void populaForm();

	protected void populaGridContent() {
		populaPesquisaPanel();
		populaCadastroForm();
		populaTableView();
	}

	abstract void populaTableView();

	abstract void populaCadastroForm();

	public TitledPane criaTablePane(String string) {
		TitledPane pane = super.criaTablePane(string);
		// double thisWidth=width-(width*50d/100d);
		// pane.setMinHeight(thisWidth);
		// pane.setMaxHeight(thisWidth);
		pane.setMinWidth(getLayoutsMaxWidth());
		pane.setMaxWidth(getMaxWidth());
		FlowPane teste = (FlowPane) pane.getContent();
		teste.setVgap(10);
		// teste.setMinHeight(d);
		// teste.setMaxHeight(d);
		TableView table = (TableView) teste.getChildren().get(0);
		double thisHeight = height;
		thisHeight -= thisHeight * (50d / 100d);
		table.setMinHeight(thisHeight);
		table.setMaxHeight(thisHeight);
		return pane;
	}

	protected void reloadTableView(String paneTitle) throws Exception {
		ObservableList<Node> children = getGrid().getChildren();

		TitledPane pane = (TitledPane) children.stream().filter(t -> ((TitledPane) t).getText().equals(paneTitle))
				.findFirst().orElse(null);
		Node teste = null;
		if (pane != null)
			teste = pane.getContent();
		TableView<ItemEstoqueModel> table = (TableView<ItemEstoqueModel>) ((Pane) teste).getChildren().get(0);
		
		ObservableList<ItemEstoqueModel> items = table.getItems();
		items.removeAll(items);
		carregaDadosTable(table);
	}

	protected void salvaDados(ActionEvent event) {
		try {
			controller.salva();
			showMensagemSucesso("Dados salvos com sucesso.");
			controller.novo();
			btnExcluir.setDisable(true);
			controller.buscaTodos();
			reloadForm();
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void excluiDados(ActionEvent event) throws Exception {
		try {
			boolean confirmation = showConfirmAtionMessage("Deseja excluir estes dados?");
			if (confirmation) {

				controller.excluir();

				showMensagemSucesso("Dados excluídos com sucesso");
				controller.setList(new ArrayList<>());
				controller.novo();

			}
			reloadForm();
		} catch (Exception e) {

			throw e;
		}

	}

	public void validaDados(String... params) throws Exception {
		for (String param : params)
			if (param == null || param.isEmpty())
				throw new Exception("Dados inválidos.");

	}

	protected abstract void populaPesquisaPanel();

	protected void populaPesquisaPanel(AutoCompleteTextField combo) {
		TitledPane pesquisaPane = new TitledPane("Pesquisa", new Button());
		combo.setMaxWidth(Double.MAX_VALUE);
		pesquisaPane.setMaxWidth(getLayoutsMaxWidth());
		pesquisaPane.setMinWidth(getLayoutsMaxWidth());
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		// BorderPane pane = new BorderPane();
		// Insets insets = new Insets(20, 0, 20, 20);
		// pane.setPadding(insets);
		// pane.setMaxWidth(getLayoutsMaxWidth());
		// pane.setMinWidth(300);
		pane.setHgap(15);
		pesquisaPane.setCollapsible(Boolean.FALSE);
		pane.getChildren().add(combo);
		// Button btnSearch = new Button("Selecionar");
		// combo.setOnAction(this::pesquisaItem);
		// btnSearch.setOnAction(this::pesquisaItem);
		// pane.getChildren().add(btnSearch);
		// vbox.getChildren().add(fabricante);
		// pane.getChildren().addAll(vbox);
		pesquisaPane.setContent(pane);
		getGrid().add(pesquisaPane, 0, 0, 2, 1);
	}

	protected void handleTableClick(Event event) {
		int id = Integer.parseInt(super.getTableViewSelectedValueId(event));

		try {
			controller.busca(id);
			populaForm();
			btnExcluir.setDisable(false);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
