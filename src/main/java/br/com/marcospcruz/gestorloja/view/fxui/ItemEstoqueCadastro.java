package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ItemEstoqueCadastro extends StageBase {

	private AutoCompleteTextField<Fabricante> comboFabricante;
	private ComboBox<TipoProduto> comboCategoria;
	private AutoCompleteTextField<TipoProduto> subCategoria;
	private AutoCompleteTextField<Produto> produtocombo;
	private CheckBox temEstoqueChk;
	private NumberTextField txtQuantidade;

	public ItemEstoqueCadastro() {
		setTitle("Cadastro de Item de Estoque");
		Group root = new Group();
		Scene scene = new Scene(root, 600, 550);
		GridPane grid = new GridPane();

		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setMinHeight(550);
		grid.setMinWidth(600);

		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.ALWAYS);
		column2.setFillWidth(true);
		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		populaGrid(grid);
		root.getChildren().add(grid);
		setScene(scene);
	}

	private void populaGrid(GridPane grid) {
		grid.add(new Label("Fabricante / Marca"), 0, 0);
		comboFabricante = super.criaFabricanteComboBox(false);
		// comboFabricante.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(comboFabricante, 1, 0);
		grid.add(new Label("Categoria"), 0, 1);
		comboCategoria = super.createCategoriaProdutoComboBox();
		comboCategoria.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<TipoProduto> items = subCategoria.getItems();
				TipoProduto tipoProduto = comboCategoria.getValue();
				items.addAll(tipoProduto.getSubTiposProduto());

			}
		});
		grid.add(comboCategoria, 1, 1);
		subCategoria = new AutoCompleteTextField<>();
		grid.add(new Label("Sub Categoria"), 0, 2);
		grid.add(subCategoria, 1, 2);
		//
		produtocombo = new AutoCompleteTextField<>();
		grid.add(new Label("Produto"), 0, 3);
		grid.add(produtocombo, 1, 3);
		grid.add(new Label("Tem Estoque?"), 0, 4);
		temEstoqueChk = new CheckBox();
		grid.add(temEstoqueChk, 1, 4);
		grid.add(new Label("Quantidade"), 0, 5);
		txtQuantidade = new NumberTextField();
		
		grid.add(txtQuantidade, 1, 5);
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
