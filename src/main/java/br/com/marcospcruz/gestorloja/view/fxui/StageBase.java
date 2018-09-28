package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.FabricanteController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.model.ItemEstoqueModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class StageBase extends Stage implements EventHandler<ActionEvent> {

	protected double width;
	protected double height;
	protected Scene scene;
	private VBox vBox;
	private Object[] labelColunas;
	private double layoutsMaxWidth;
	protected Button btnExcluir;
	protected ControllerBase controller;

	public StageBase() {
		SingletonManager singletonManager = SingletonManager.getInstance();
		// width = singletonManager.getScreenSize().getWidth() -
		// (singletonManager.getScreenSize().getWidth());
		// height = singletonManager.getScreenSize().getHeight() -
		// (singletonManager.getScreenSize().getHeight());
		width = singletonManager.getScreenWidth();
		height = singletonManager.getScreenHeight();
		vBox = new VBox();
		getvBox().setSpacing(5);
		scene = new Scene(new Group(), width, height);
		getvBox().setPadding(new Insets(10, 0, 0, 10));
		((Group) scene.getRoot()).getChildren().add(getvBox());
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, this::tableViewEventFilter);
		setScene(scene);
		setResizable(false);

	}

	protected void tableViewEventFilter(MouseEvent event) {
		Node source = event.getPickResult().getIntersectedNode();
		// move up through the node hierarchy until a TableRow or scene root is found
		while (source != null && !(source instanceof TableRow)) {
			source = source.getParent();
		}

		// clear selection on click anywhere but on a filled row
		if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty())) {

			TableView table = ((TableRow) source).getTableView();

			table.getSelectionModel().clearSelection();
		}

	}

	protected TitledPane criaTitledPane(String title) {
		TitledPane pane = new TitledPane(title, new Button(""));
		pane.setCollapsible(false);
		return pane;
	}

	public void systemOut(Object... objs) {
		for (Object obj : objs) {
			System.out.print(obj + ",");
		}

	}

	protected FlowPane createHorizontalFlowPane() {
		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
		flowPane.setHgap(10);
		return flowPane;
	}

	protected TableView criaTableView(double thisWidth) {

		TableView table = new TableView<>();

		try {

			table.setColumnResizePolicy((param) -> true);

			// table
			carregaDadosTable(table);
			ajustaColumnsTableView(table);
			// double teste = 0;
			// //
			// try {
			// teste = scene.widthProperty().get();
			// }catch(NullPointerException e) {
			// e.printStackTrace();
			// }
			// double thisWidth = (double) width - ((double) width * 10 / 100);
			// table.prefWidthProperty().bind(new
			// SimpleDoubleProperty(thisWidth).subtract(thisWidth * (26.8d / 100d)));
			table.prefWidthProperty().bind(new SimpleDoubleProperty(thisWidth)
			// .subtract(thisWidth * (10d / 100d))
			);
			// table.prefHeightProperty().bind(new
			// SimpleDoubleProperty(height).subtract(height *(50 / 100d)));
			table.setOnMouseClicked(this::handleTableClick);

		} catch (Exception e) {

			SingletonManager.getInstance().getLogger(getClass()).warn(e);

		} finally {

			// if (table != null)
			// customResize(table);

		}
		return table;
	}

	protected void handleTableClick(Event event) {
		String value = getTableViewSelectedValueId(event);
		try {

			controller.busca(value);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected String getTableViewSelectedValueId(Event event) {

		TableView<ItemEstoqueModel> table = (TableView<ItemEstoqueModel>) event.getSource();

		TablePosition tablePos = (TablePosition) table.getSelectionModel().getSelectedCells().get(0);
		int row = tablePos.getRow();
		int col = tablePos.getColumn();
		String value = ((TableColumn) table.getColumns().get(0)).getCellObservableValue(row).getValue().toString();
		return value;
	}

	abstract void reloadForm() throws Exception;

	void abreTelaCadastro(Stage stage) throws Exception {
		stage.initOwner(this);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

	}

	/**
	 * 
	 * @param table
	 */
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

	protected void ajustaColumnsTableView(TableView table) {
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		Arrays.asList(labelColunas).stream().forEach(columnName -> {
			TableColumn<ItemEstoqueModel, String> tableColumn = new TableColumn<>(columnName.toString());
			tableColumn.setCellValueFactory(new PropertyValueFactory<ItemEstoqueModel, String>(columnName.toString()));
			// tableColumn.setResizable(false);

			table.getColumns().add(tableColumn);

		});

	}

	protected abstract void carregaDadosTable(TableView table) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected AutoCompleteTextField<TipoProduto> createCategoriaProdutoAutotextFieldBox() {
		AutoCompleteTextField<TipoProduto> categoriaProduto = new AutoCompleteTextField<>();
		categoriaProduto.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		TipoProdutoController controller;
		try {
			controller = getTipoProdutoController();
			controller.buscaTodos();
			ObservableList<TipoProduto> items = categoriaProduto.getItems();
			items.add(new SubTipoProduto("Selecione uma opção.", null));
			items.addAll(controller.getList());
		} catch (Exception e) {

			e.printStackTrace();
		}

		return categoriaProduto;
	}

	protected static TipoProdutoController getTipoProdutoController() throws Exception {
		return (TipoProdutoController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
	}

	protected static EstoqueController getEstoqueController() throws Exception {

		return (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
	}

	/**
	 * 
	 * @param table
	 */
	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// Minimal width = columnheader
			Text t = new Text(column.getText());
			double max = t.getLayoutBounds().getWidth();
			for (int i = 0; i < table.getItems().size(); i++) {
				// cell must not be empty
				if (column.getCellData(i) != null) {
					t = new Text(column.getCellData(i).toString());
					double calcwidth = t.getLayoutBounds().getWidth();
					// remember new max-width
					if (calcwidth > max) {
						max = calcwidth;
					}
				}
			}
			// set the new max-widht with some extra space
			column.setPrefWidth(max + 10.0d);
		});
	}

	public AutoCompleteTextField<TipoProduto> criaTipoProdutoComboBox(Boolean showItems) {
		AutoCompleteTextField<TipoProduto> combo = new AutoCompleteTextField<>(showItems);
		try {
			TipoProdutoController controller = getTipoProdutoController();
			ObservableList<TipoProduto> items = combo.getItems();

			for (TipoProduto tipo : controller.getList()) {

				Collection<SubTipoProduto> subTipos = tipo.getSubTiposProduto();
				items.add(tipo);
				if (subTipos != null) {
					for (SubTipoProduto sub : subTipos)
						items.add(sub);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return combo;

	}

	public AutoCompleteTextField<Produto> criaProdutoComboBox(Boolean showItems) {
		AutoCompleteTextField<Produto> combo = new AutoCompleteTextField<>(showItems);
		try {
			ProdutoController controller = getProdutoController();
			ObservableList<Produto> items = combo.getItems();

			for (Produto tipo : controller.getList()) {

				items.add(tipo);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return combo;

	}

	public AutoCompleteTextField<Fabricante> criaFabricanteComboBox(boolean showItems) {
		AutoCompleteTextField<Fabricante> combo = new AutoCompleteTextField<>(showItems);
		try {

			ObservableList<Fabricante> fabricantes = combo.getItems();
			criaObservableList(fabricantes, getFabricanteController());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return combo;
	}

	@SuppressWarnings("unchecked")
	protected void criaObservableList(ObservableList items, ControllerBase controller) throws Exception {
		items.removeAll(items);
		items.addAll(controller.getList());
	}

	protected static FabricanteController getFabricanteController() throws Exception {

		return (FabricanteController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.FABRICANTE);
	}

	public AutoCompleteTextField<Produto> criaProdutoAutoComboBox() {
		AutoCompleteTextField<Produto> combo = new AutoCompleteTextField<>();
		ProdutoController produtoController = null;
		try {
			produtoController = getProdutoController();
			produtoController.buscaTodos();
			ObservableList<Produto> items = combo.getItems();
			items.addAll(produtoController.getList());
		} catch (Exception e) {

			e.printStackTrace();
		}

		return combo;
	}

	protected static ProdutoController getProdutoController() throws Exception {

		return (ProdutoController) getController(ControllerAbstractFactory.PRODUTO);
	}

	public void showErrorMessage(String message) {
		showErrorMessage("Erro", message);

	}

	public static CaixaController getCaixaController() throws Exception {

		return (CaixaController) getController(ControllerAbstractFactory.CONTROLE_CAIXA);
	}

	private static ControllerBase getController(String className) throws Exception {

		return SingletonManager.getInstance().getController(className);
	}

	private Alert showMessage(String message, AlertType alertType, String title, ButtonType... buttonType) {

		Alert alert;
		if (buttonType != null)
			alert = new Alert(alertType, message, buttonType);
		else
			alert = new Alert(alertType, message, ButtonType.OK);
		alert.setTitle(title);
		// alert.setContentText(message);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		return alert;
	}

	public void showMensagemSucesso(String message) {
		showMessage(message, AlertType.INFORMATION, "Operação realizada com sucesso!");
		SingletonManager.getInstance().getLogger(this.getClass()).info(message);
	}

	public boolean showConfirmAtionMessage(String string) {
		Alert alert = showMessage(string, AlertType.CONFIRMATION, "Confirma Operação.", ButtonType.YES, ButtonType.NO);
		SingletonManager.getInstance().getLogger(this.getClass()).warn(string);
		return alert.getResult() == ButtonType.YES;
	}

	private Alert showMessage(String string, AlertType confirmation, String title) {

		return showMessage(string, confirmation, title, null);
	}

	public VBox getvBox() {
		return vBox;
	}

	public void setvBox(VBox vBox) {
		this.vBox = vBox;
	}

	public void setDimension(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public TitledPane criaTablePane(String title) {
		TitledPane titledPane = new TitledPane(title, new Button());
		titledPane.setCollapsible(Boolean.FALSE);
		double thisWidth = layoutsMaxWidth;
		thisWidth -= thisWidth * (2d / 100d);
		// width - (width * 10d / 100d);
		titledPane.setMaxWidth(thisWidth);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		TableView table = criaTableView(thisWidth);
		// table.setMinHeight(100);
		// table.setMaxHeight(100);
		pane.getChildren().add(table);
		titledPane.setContent(pane);
		return titledPane;
	}

	protected void setLabelColunas(Object[] labelColunas) {
		this.labelColunas = labelColunas;

	}

	protected void setLayoutsMaxWidth(double layoutsMaxWidth) {
		this.layoutsMaxWidth = layoutsMaxWidth;

	}
	// public abstract void handleTableClick(ActionEvent mouseEvent);

	/**
	 * @return the layoutsMaxWidth
	 */
	protected double getLayoutsMaxWidth() {
		return layoutsMaxWidth;
	}

	protected Node buttonsPane() {
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		pane.setRowValignment(VPos.BASELINE);
		Button btnNovo = new Button("Novo");
		btnNovo.setOnAction(this::novo);
		Button btnSave = new Button("Salvar");
		btnSave.setOnAction(arg0 -> {
			try {
				salvaDados(arg0);
			} catch (Exception e1) {

				e1.printStackTrace();
			}
		});

		btnExcluir = new Button("Excluir");
		btnExcluir.setDisable(true);
		btnExcluir.setOnAction(arg0 -> {
			try {
				excluiDados(arg0);
			} catch (Exception e) {

				e.printStackTrace();
			}
		});
		pane.setHgap(10);
		pane.getChildren().addAll(btnNovo, btnSave, btnExcluir);
		return pane;
	}

	protected void novo(ActionEvent event) {
		btnExcluir.setDisable(true);
		controller.novo();
		populaForm();
	}

	abstract void populaForm();

	protected abstract void salvaDados(ActionEvent event) throws Exception;

	protected abstract void excluiDados(ActionEvent event) throws Exception;

	protected abstract void pesquisaItem(ActionEvent event);

	public VendaController getVendaController() throws Exception {

		return (VendaController) getController(ControllerAbstractFactory.CONTROLE_VENDA);
	}

	private Label criaLabel(String string, boolean setBold) {
		Label label = new Label(string);
		if (setBold)
			label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		else
			label.setFont(Font.font("Verdana", 12));

		return label;

	}

	protected Label criaLabelBold(String string) {
		return criaLabel(string, true);
	}

	protected Label criaLabelNormal(String string) {
		return criaLabel(string, false);
	}

	private void showErrorMessage(String string, String message) {
		showMessage(message, AlertType.ERROR, string);

	}

	public void showErrorMessage(String string, Exception e) {
		showMessage(e.getMessage(), AlertType.ERROR, string);
		SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage(), e);
	}

	public void showErrorMessage(Exception e) {
		showErrorMessage(e.getMessage());
		SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage(), e);

	}

}
