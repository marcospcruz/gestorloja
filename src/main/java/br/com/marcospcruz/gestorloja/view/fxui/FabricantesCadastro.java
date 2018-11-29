package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;

import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.FabricanteModel;
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

public class FabricantesCadastro extends CadastroBase {
	private static final Object[] COLUNAS = { "cod", "nome", "itensEstoque" };
	private static final String TITLE_PANE = "Fabricantes / Marcas";
	private TextField nomeFabricanteTxt;
	private AutoCompleteTextField<Fabricante> fabricanteCombo;

	public FabricantesCadastro() throws Exception {
		super(COLUNAS, getFabricanteController());
		setTitle("Cadastro de Fabricantes / Marca");
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaForm() {
		Fabricante fabricante = (Fabricante) controller.getItem();
		boolean disable = fabricante == null;
		nomeFabricanteTxt.setText(!disable ? fabricante.getNome() : "");
		// btnExcluir.setDisable(disable);
		// controller.novo();
	}

	@Override
	void populaTableView() {
		try {
			// controller = getFabricanteController();

			TitledPane tablePane = super.criaTablePane(TITLE_PANE);
			// tablePane.setMaxHeight(500d);
			// tablePane.setMinHeight(220d);

			getGrid().add(tablePane, 0, 2, 2, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void populaCadastroForm() {
		TitledPane formPane = new TitledPane("Dados Fabricante / Marca", new Button());
		formPane.setCollapsible(Boolean.FALSE);
		double thisWidth = width - (width * .05);
		formPane.setMaxWidth(thisWidth);
		formPane.setMinWidth(thisWidth);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		pane.setHgap(10);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		int h = 125;
		grid.setMinHeight(h);
		grid.setMinWidth(getWidth());
		grid.setMaxHeight(h);
		grid.setVgap(35);

		nomeFabricanteTxt = new TextField();
		int row = 0;
		grid.add(new Label("Nome Fabricante/Marca"), 0, row);
		grid.add(nomeFabricanteTxt, 1, row++);
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();

		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.ALWAYS);
		column2.setFillWidth(true);
		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		grid.add(buttonsPane(), 1, row, 2, 1);
		pane.getChildren().add(grid);
		formPane.setContent(pane);
		getGrid().add(formPane, 0, 1, 2, 1);

	}

	@Override
	protected void salvaDados(ActionEvent event) {

		String nome = nomeFabricanteTxt.getText();

		Fabricante fabricante = (Fabricante) controller.getItem();
		fabricante.setNome(nome);

		try {
			super.validaDados(nome);
			boolean confirmation = showConfirmAtionMessage("Deseja salvar estes Dados?");
			if (confirmation)
				super.salvaDados(event);
			ObservableList<Fabricante> items = fabricanteCombo.getItems();

			super.criaObservableList(items, getFabricanteController());
		} catch (Exception e) {
			super.showErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {

		ObservableList<FabricanteModel> items = table.getItems();
		// items.removeAll(items);
		controller.getList().stream().forEach(f -> {
			Fabricante fabricante = new CrudDao<Fabricante>().update((Fabricante) f);
			FabricanteModel model = new FabricanteModel(fabricante.getIdFabricante(), fabricante.getNome(),
					fabricante.getItensEstoque().size());
			items.add(model);
		});
		// items.addAll(controller.getList());
		table.setItems(items);

	}

	@Override
	void reloadForm() throws Exception {
		populaForm();
		reloadTableView(TITLE_PANE);
		super.criaObservableList(fabricanteCombo.getItems(), getFabricanteController());
		fabricanteCombo.getEditor().setText("");
	}

	@Override
	protected void excluiDados(ActionEvent event) {

		try {
			super.excluiDados(event);
			controller.buscaTodos();
			reloadForm();
		} catch (Exception e) {

			showErrorMessage(e);
		}

	}

	@Override
	protected void populaPesquisaPanel() {
		fabricanteCombo = super.criaFabricanteComboBox(Boolean.FALSE);
		// fabricanteCombo.prefWidthProperty().bind(pesquisaPane.prefWidthProperty());
		// VBox vbox = new VBox();
		// vbox.setSpacing(10);
		// vbox.setPadding(insets);
		fabricanteCombo.setOnAction(this::pesquisaItem);
		fabricanteCombo.setMaxWidth(Double.MAX_VALUE);
		super.populaPesquisaPanel(fabricanteCombo);
	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		if (!(fabricanteCombo.getValue() instanceof Fabricante))
			return;
		Fabricante item = fabricanteCombo.getValue();
		controller.setItem(item);
		controller.setList(new ArrayList<>());
		controller.getList().add(item);
		try {
			reloadForm();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
