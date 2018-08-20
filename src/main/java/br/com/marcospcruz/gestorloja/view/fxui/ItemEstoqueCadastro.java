package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.FabricanteController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.facade.OperacaoEstoqueFacade;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ItemEstoqueCadastro extends StageBase {

	private AutoCompleteTextField<Fabricante> comboFabricante;
	private AutoCompleteTextField<TipoProduto> comboCategoria;
	private AutoCompleteTextField<TipoProduto> subCategoria;
	private AutoCompleteTextField<Produto> produtocombo;
	private CheckBox temEstoqueChk;
	private NumberTextField txtQuantidade;
	private NumberTextField txtValorUnit;

	private TextField txtCodigoDeBarras;
	private GridPane grid;

	public ItemEstoqueCadastro() {
		super();

		setTitle("Cadastro de Item de Estoque");
		super.setDimension(750, 300);
		double thisWidth = (double) super.getWidth() - ((double) super.getWidth() * 10 / 100);
		setLayoutsMaxWidth(thisWidth);
		resizableProperty().setValue(Boolean.FALSE);
		Group root = new Group();
		scene = new Scene(root, width, height);
		grid = new GridPane();

		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setMinHeight(getHeight());
		grid.setMinWidth(getWidth());
		grid.setMaxHeight(getHeight());
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.ALWAYS);
		column2.setFillWidth(true);
		populaGridContent();

		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		grid.add(buttonsPane(), 1, 8, 2, 1);
		populaForm();
		root.getChildren().add(grid);

		setScene(scene);

	}

	protected void populaForm() {
		try {
			EstoqueController controller = getEstoqueController();
			if (controller.getItem() != null) {
				ItemEstoque item = controller.getItemEstoque();
				Fabricante fabricante = item.getFabricante();
				SubTipoProduto subCategoriaProduto = item.getTipoProduto();
				Produto produto = item.getProduto();
				comboFabricante.setValue(fabricante);
				comboCategoria.setValue(subCategoriaProduto.getSuperTipoProduto());
				subCategoria.setValue(subCategoriaProduto);
				produtocombo.setValue(produto);
				txtQuantidade.setText(item.getQuantidade().toString());
				txtValorUnit.setText(Util.formataStringDecimais(item.getValorUnitario()));
				txtCodigoDeBarras.setText(item.getCodigoDeBarras());
				temEstoqueChk.setSelected(item.isEstoqueDedutivel());
				btnExcluir.setDisable(false);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void excluiDados(ActionEvent event) {
		try {
			boolean confirm = super.showConfirmAtionMessage("Deseja excluir este ítem?");
			if (confirm) {
				EstoqueController controller = getEstoqueController();
				controller.apagaItem();
				showMensagemSucesso("Ítem excluído com sucesso.");
				hide();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void salvaDados(ActionEvent event) {
		try {
			EstoqueController controller = getEstoqueController();
			ProdutoController pController = getProdutoController();

			Produto produto = (Produto) parseProduto(produtocombo.getValue());

			Fabricante fabricante = (Fabricante) parseFabricante(comboFabricante.getValue());

			try {
				pController.busca(produto.getDescricaoProduto());
				produto = pController.getItem();
			} catch (Exception e) {
				e.printStackTrace();
			}

			SubTipoProduto subTipoProduto = (SubTipoProduto) parseTipoProduto(subCategoria.getValue());
			// (SubTipoProduto) parseCategoriaProduto(cmbSubCategoriaProduto);
			if (subTipoProduto.getSuperTipoProduto() == null) {
				SubTipoProduto superTipoProduto = (SubTipoProduto) parseTipoProduto(comboCategoria.getValue());
				// (SubTipoProduto) parseCategoriaProduto(cmbCategoriaProduto);
				if (!superTipoProduto.equals(subTipoProduto))
					subTipoProduto.setSuperTipoProduto(superTipoProduto);
			}
			// produto.setTipoProduto((SubTipoProduto) subTipoProduto);
			// List<SubTipoProduto> tipos = produto.getTiposProduto();
			// if (tipos == null)
			// tipos = new ArrayList<>();
			// tipos.add(subTipoProduto);
			// produto.setTiposProduto(tipos);

			String qtString = txtQuantidade.getText();
			// txtQuantidadeInicial.getText();
			if (qtString.isEmpty())
				qtString = "0";
			Integer quantidade = Integer.valueOf(qtString);
			String vlUnit = txtValueToStringDecimal(txtValorUnit.getText());
			Float valorUnitario = Util.parseStringDecimalToFloat(vlUnit);
			// String valorCusto = txtValueToStringDecimal(txtValorCusto.getText());
			// Float valorCustoFloat = Util.parseStringDecimalToFloat(valorCusto);
			ItemEstoque itemEstoque = controller.getItemEstoque() == null ? new ItemEstoque()
					: (ItemEstoque) controller.getItemEstoque();
			itemEstoque.setEstoqueDedutivel(temEstoqueChk.isSelected());
			itemEstoque.setFabricante(fabricante);
			itemEstoque.setProduto(produto);
			itemEstoque.setQuantidade(quantidade);
			itemEstoque.setValorUnitario(valorUnitario);
			itemEstoque.setTipoProduto(subTipoProduto);
			// itemEstoque.setValorCusto(valorCustoFloat);
			String codigoBarras = txtCodigoDeBarras.getText();

			if (codigoBarras.isEmpty()) {
				throw new Exception("Código de Barras inválido!");
			}
			if (itemEstoque.getIdItemEstoque() == null) {
				controller.buscaItemPorCodigoDeBarras(codigoBarras);
				ItemEstoque teste = controller.getItemEstoque();

				if (teste != null) {
					controller.setItemEstoque(null);
					throw new Exception("Código de Barras já cadastrado no estoque.");
				}
			}
			itemEstoque.setCodigoDeBarras(codigoBarras);

			OperacaoEstoqueFacade operacaoEstoqueFacade = new OperacaoEstoqueFacade((EstoqueController) controller);

			operacaoEstoqueFacade.adicionaItemEstoque(itemEstoque);
			super.showMensagemSucesso("Dados salvos com sucesso no Estoque.");
			// controller.criaItemEstoque(fabricante, txtQuantidadeInicial.getText());
			hide();

		} catch (Exception e1) {

			e1.printStackTrace();
			super.showErrorMessage(e1.getMessage());
		}
	}

	private Object parseTipoProduto(Object value) {
		if (value instanceof TipoProduto)
			return value;
		return new SubTipoProduto(value.toString());
	}

	private Object parseFabricante(Object value) throws Exception {
		if (value instanceof Fabricante)
			return value;
		else {
			FabricanteController controller = getFabricanteController();
			controller.buscaNome(value.toString());
			if (controller.getItem() == null)
				return new Fabricante(value.toString());
			return controller.getItem();
		}
	}

	private Object parseProduto(Object value) {
		if (value instanceof Produto)
			return value;

		return new Produto(value.toString());

	}

	private String txtValueToStringDecimal(String text) {
		if (text.isEmpty())
			return "0";
		return text;
	}

	protected void populaGridContent() {
		int row = 0;

		grid.add(new Label("Fabricante / Marca"), 0, row);
		comboFabricante = super.criaFabricanteComboBox(false);
		// comboFabricante.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(comboFabricante, 1, row++);
		grid.add(new Label("Categoria"), 0, row);
		comboCategoria = super.createCategoriaProdutoAutotextFieldBox();
		comboCategoria.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ObservableList<TipoProduto> items = subCategoria.getItems();
				try {
					TipoProduto tipoProduto = comboCategoria.getValue();
					items.addAll(tipoProduto.getSubTiposProduto());
				} catch (ClassCastException e) {

				}

			}
		});
		grid.add(comboCategoria, 1, row++);
		subCategoria = new AutoCompleteTextField<>();
		grid.add(new Label("Sub Categoria"), 0, row);
		grid.add(subCategoria, 1, row++);
		//
		produtocombo = super.criaProdutoAutoComboBox();
		grid.add(new Label("Produto"), 0, row);
		grid.add(produtocombo, 1, row++);
		grid.add(new Label("Código de Barras"), 0, row);
		txtCodigoDeBarras = new TextField();
		grid.add(txtCodigoDeBarras, 1, row++);
		grid.add(new Label("Tem Estoque"), 0, row);
		temEstoqueChk = new CheckBox();
		grid.add(temEstoqueChk, 1, row++);
		grid.add(new Label("Quantidade"), 0, row);
		txtQuantidade = new NumberTextField();
		grid.add(txtQuantidade, 1, row++);
		grid.add(new Label("Valor Unitário (R$)"), 0, row);
		txtValorUnit = new NumberTextField(true);
		grid.add(txtValorUnit, 1, row);

	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleTableClick(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void ajustaColumnsTableView(TableView table) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	void reloadForm() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
