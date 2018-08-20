package br.com.marcospcruz.gestorloja.view.fxui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.CaixaModel;
import br.com.marcospcruz.gestorloja.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ControleCaixaGui extends StageBase {
	//@formatter:off
	private static final Object[] COLUNAS_TABLEVIEW = {
			"abertura",
			"operadorAbertura",
			"trocoInicial",
			"fechamento",
			"operadorFechamento",
			"trocoFinal",
			"status"
	};
	//@formatter:on
	public ControleCaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		controller.buscaTodos();
		super.setLabelColunas(COLUNAS_TABLEVIEW);
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		super.width = screenSize.getWidth() - (screenSize.getWidth() * DEZ_PORCENTO);
		super.height = screenSize.getHeight() - (screenSize.getHeight() * DEZ_PORCENTO);

		getvBox().setSpacing(5);
		getvBox().setPadding(new Insets(10, 0, 0, 10));
		scene = new Scene(new Group(), width, height);
		double layoutsMaxWidth = scene.widthProperty().get();
		layoutsMaxWidth -= layoutsMaxWidth * (27d / 100d);
		super.setLayoutsMaxWidth(layoutsMaxWidth);

		TitledPane tablePane = super.criaTablePane("Caixa Di�rio");
		TitledPane buttonPane = criaButtonPane();
		getvBox().getChildren().addAll(buttonPane, tablePane);
		((Group) scene.getRoot()).getChildren().add(getvBox());
		setScene(scene);
	}

	private TitledPane criaButtonPane() {
		TitledPane titledPane = new TitledPane("", new Button());
		titledPane.setCollapsible(false);
		FlowPane content = new FlowPane(Orientation.HORIZONTAL);
		Button button = new Button("Abrir Caixa");
		button.setOnAction(this::handle);
		content.getChildren().add(button);
		titledPane.setContent(content);
		return titledPane;
	}

	@Override
	public void handle(ActionEvent arg0) {
		try {
			openCaixaGui();
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void openCaixaGui() throws Exception {
//		((CaixaController) controller).validateCaixaAberto();
		controller.novo();
		Stage caixa = new CaixaGui();

		abreTelaCadastro(caixa);
		reloadForm();
	}

	@Override
	void reloadForm() throws Exception {

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		List<CaixaModel> caixas = new ArrayList<>();
		if (controller.getList().isEmpty())
			controller.buscaTodos();

		controller.getList().stream().forEach(item -> {
			Caixa caixa = (Caixa) item;
			try {
				String usuarioAbertura = caixa != null ? caixa.getUsuarioAbertura().getNomeCompleto() : "";
				String trocoInicial = Util.formataMoeda(caixa != null ? caixa.getSaldoInicial() : 0f);
				String dataHoraFechamento = caixa != null && caixa.getDataFechamento() != null
						? Util.formataDataHora(caixa.getDataFechamento())
						: "";
				String trocoFinal = Util.formataMoeda(caixa != null ? caixa.getSaldoFinal() : 0f);
				String status = caixa != null && caixa.getDataFechamento() == null ? "Aberto" : "Fechado";
				String operadorFechamento = caixa != null && caixa.getUsuarioFechamento() != null
						? caixa.getUsuarioFechamento().getNomeCompleto()
						: "";
				//@formatter:off
				caixas.add(new CaixaModel(Util.formataDataHora(caixa.getDataAbertura()),
						usuarioAbertura,
						trocoInicial,
						dataHoraFechamento,operadorFechamento,
						trocoFinal,
						status
						));
						//@formatter:on
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		});
		ObservableList<CaixaModel> dados = FXCollections.observableArrayList(caixas);
		table.setItems(dados);
	}

	@Override
	void populaForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvaDados(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void excluiDados(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleTableClick(Event event) {
		super.handleTableClick(event);
		try {
			openCaixaGui();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
