package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.FabricanteController;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class StageBase extends Stage implements EventHandler<ActionEvent> {

	protected static final double DEZ_PORCENTO = 10d / 100d;
	protected double width;
	protected double height;

	public void systemOut(Object... objs) {
		for (Object obj : objs) {
			System.out.print(obj + ",");
		}
		System.out.println();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected ComboBox<TipoProduto> createCategoriaProdutoComboBox() {
		ComboBox<TipoProduto> categoriaProduto = new ComboBox<>();
		categoriaProduto.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		TipoProdutoController controller;
		try {
			controller = getTipoProdutoController();

			ObservableList<TipoProduto> items = categoriaProduto.getItems();
			items.add(new SubTipoProduto("Selecione uma opção.", null));
			items.addAll(controller.getList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return categoriaProduto;
	}

	protected TipoProdutoController getTipoProdutoController() throws Exception {
		return (TipoProdutoController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
	}

	protected static EstoqueController getEstoqueController() throws Exception {

		return (EstoqueController) SingletonManager.getInstance().getController(ControllerAbstractFactory.ESTOQUE);
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

	public AutoCompleteTextField<Fabricante> criaFabricanteComboBox(boolean showItems) {
		AutoCompleteTextField<Fabricante> combo = new AutoCompleteTextField<>(showItems);
		try {
			FabricanteController fabricante = getFabricanteController();
			ObservableList<Fabricante> fabricantes = combo.getItems();
			fabricantes.addAll(fabricante.getList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return combo;
	}

	private FabricanteController getFabricanteController() throws Exception {
		// TODO Auto-generated method stub
		return (FabricanteController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.FABRICANTE);
	}
}
