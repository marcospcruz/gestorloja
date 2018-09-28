package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LazyInitializationException;

import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.TipoProdutoModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class CategoriaProdutoCadastro extends CadastroBase {

	private static final String CATEGORIAS_PRODUTO = "Categorias Produto";
	private static final Object[] COLUNAS = { "cod", "categoria", "superCategoria", "itensEstoque" };
	private TextField categoriaProduto;
	private AutoCompleteTextField<TipoProduto> superTiposProduto;
	private CheckBox subTipoCheck;
	private AutoCompleteTextField<TipoProduto> tiposProduto;

	public CategoriaProdutoCadastro() throws Exception {
		super(COLUNAS, getTipoProdutoController());

		setTitle(CATEGORIAS_PRODUTO);
	}

	@Override
	public void handle(ActionEvent arg0) {

	}

	@Override
	protected void populaForm() {

		SubTipoProduto tipoProduto = (SubTipoProduto) controller.getItem();
		if (tipoProduto != null) {
			TipoProduto superTipoProduto = tipoProduto.getSuperTipoProduto();
			boolean ehSubTipo = superTipoProduto != null;
			// if (ehSubTipo) {
			// }
			subTipoCheck.setSelected(ehSubTipo);
			if (subTipoCheck.isSelected()) {
				superTiposProduto.setDisable(subTipoCheck.isSelected());

				superTiposProduto.setDisable(superTipoProduto == null);
			}
			superTiposProduto.setValue(superTipoProduto);
			categoriaProduto.setText(tipoProduto.getDescricaoTipo());
			btnExcluir.setDisable(false);

		}
	}

	@Override

	void populaTableView() {
		TitledPane tablePane = super.criaTablePane(CATEGORIAS_PRODUTO);
		// tablePane.setMaxHeight(500d);
		// tablePane.setMinHeight(220d);
		getGrid().add(tablePane, 0, 2, 2, 1);

	}

	void populaCadastroForm() {
		TitledPane formPane = new TitledPane("Dados Categorias Produto", new Button());
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
		Label descricaoLabel = new Label("Descrição Categoria");
		categoriaProduto = new TextField();
		grid.add(descricaoLabel, 0, row);
		grid.add(categoriaProduto, 1, row++);
		subTipoCheck = new CheckBox();
		subTipoCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				superTiposProduto.setDisable(!arg2);

			}
		});
		subTipoCheck.setText("É sub categoria");
		grid.add(subTipoCheck, 1, row++);
		Label superLabel = new Label("Super Categoria Produto");
		superTiposProduto = super.createCategoriaProdutoAutotextFieldBox();
		superTiposProduto.setDisable(true);
		grid.add(superLabel, 0, row);
		grid.add(superTiposProduto, 1, row++);

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
		tiposProduto = super.criaTipoProdutoComboBox(Boolean.FALSE);
		tiposProduto.setOnAction(this::pesquisaItem);
		tiposProduto.prefWidthProperty().bind(pesquisaPane.prefWidthProperty());
		// VBox vbox = new VBox();
		// vbox.setSpacing(10);
		// vbox.setPadding(insets);
		tiposProduto.setMaxWidth(Double.MAX_VALUE);
		pane.getChildren().add(tiposProduto);

		// vbox.getChildren().add(fabricante);
		// pane.getChildren().addAll(vbox);
		pesquisaPane.setContent(pane);
		getGrid().add(pesquisaPane, 0, 0, 2, 1);
	}

	@Override
	protected void salvaDados(ActionEvent event) {
		SubTipoProduto categoria = (SubTipoProduto) controller.getItem();
		String descricao = categoriaProduto.getText();
		categoria.setDescricaoTipo(descricao);
		if (subTipoCheck.isSelected()) {
			TipoProduto superCategoria;
			try {
				superCategoria = superTiposProduto.getValue();
			} catch (ClassCastException e) {

				Object descricaoSuper = superTiposProduto.getValue();
				superCategoria = ((TipoProdutoController) controller).buscaSuperCategoria(descricaoSuper.toString());

			}
			categoria.setSuperTipoProduto((SubTipoProduto) superCategoria);
		}
		boolean confirmation = showConfirmAtionMessage("Deseja salvar estes Dados?");
		if (confirmation)
			super.salvaDados(event);
		tiposProduto.getEditor().setText("");

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		TipoProdutoController controller = getTipoProdutoController();

		ObservableList<TipoProdutoModel> items = table.getItems();
		controller.buscaTodos();
		carregaTiposProduto(controller.getList(), items);
		// items.addAll(controller.getList());
		table.setItems(items);

	}

	protected void carregaTiposProduto(List<TipoProduto> list, ObservableList<TipoProdutoModel> items) {
		list.stream().forEach(f -> {
			SubTipoProduto tipoProduto = new CrudDao<SubTipoProduto>().update((SubTipoProduto) f);
			String descricaoSuper = tipoProduto.getSuperTipoProduto() == null ? ""
					: tipoProduto.getSuperTipoProduto().getDescricaoTipo();
			int qtItensEstoque = 0;
			try {
				qtItensEstoque = tipoProduto.getItensEstoque().size();
			} catch (LazyInitializationException e) {
				SingletonManager.getInstance().getLogger(getClass())
						.warn("LazyInitializationException para " + tipoProduto, e);
			}
			TipoProdutoModel model = new TipoProdutoModel(tipoProduto.getIdTipoItem(), tipoProduto.getDescricaoTipo(),
					descricaoSuper, qtItensEstoque);

			items.add(model);
			List subTipos = (List<SubTipoProduto>) tipoProduto.getSubTiposProduto();
			try {
				if (subTipos != null && !subTipos.isEmpty()) {
					carregaTiposProduto(subTipos, items);
				}
			} catch (LazyInitializationException e) {
				SingletonManager.getInstance().getLogger(getClass())
						.warn("LazyInitializationException para " + tipoProduto, e);
			}
		});

	}

	@Override
	void reloadForm() throws Exception {
		populaForm();
		reloadTableView(CATEGORIAS_PRODUTO);
		super.criaObservableList(tiposProduto.getItems(), controller);
		// tiposProduto.getEditor().setText("");

		super.criaObservableList(superTiposProduto.getItems(), controller);

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		if (!(tiposProduto.getValue() instanceof TipoProduto))
			return;
		TipoProduto item = tiposProduto.getValue();
		controller.setList(new ArrayList<>());
		controller.getList().add(item);
		controller.setItem(item);
		try {
			reloadForm();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
