package br.com.marcospcruz.gestorloja.view.fxui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.CaixaModel;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControleCaixaGui extends StageBase {
	//@formatter:off
	private static final Object[] COLUNAS_TABLEVIEW = {
			"abertura",
			"operadorAbertura",
			"trocoInicial",
			"totalVendido",
			"fechamento",
			"operadorFechamento",
			"trocoFinal",
			"status"
	};
	private TitledPane tablePane;
	private Button button;
	//@formatter:on
	public ControleCaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		controller.buscaTodos();
		super.setLabelColunas(COLUNAS_TABLEVIEW);

		// super.width = SingletonManager.getInstance().getScreenWidth();
		// super.height = SingletonManager.getInstance().getScreenHeight();

		getvBox().setSpacing(5);
		getvBox().setPadding(new Insets(10, 0, 0, 10));
		scene = new Scene(new Group(), width, height);
		double layoutsMaxWidth = scene.widthProperty().get();

		super.setLayoutsMaxWidth(layoutsMaxWidth);
		TitledPane buttonPane = criaButtonPane();
		tablePane = super.criaTablePane("Caixa Diário");

		getvBox().getChildren().addAll(buttonPane, tablePane);
		((Group) scene.getRoot()).getChildren().add(getvBox());
		setScene(scene);
	}

	private TitledPane criaButtonPane() throws Exception {
		TitledPane titledPane = new TitledPane("", new Button());
		titledPane.setCollapsible(false);
		FlowPane content = new FlowPane(Orientation.HORIZONTAL);
		button = new Button("Abrir Caixa");
		button.setOnAction(this::handle);

		CaixaController controller = getCaixaController();
		try {
			controller.validateCaixaAberto();
		} catch (Exception e) {
			button.setDisable(true);
		}
		content.getChildren().add(button);
		titledPane.setContent(content);
		return titledPane;
	}

	@Override
	public void handle(ActionEvent arg0) {
		try {

			openCaixaGui();
		} catch (Exception e) {
			super.showErrorMessage(e);
			e.printStackTrace();
		}
	}

	protected void openCaixaGui() throws Exception {
		((CaixaController) controller).validateCaixaAberto();
		try {

			controller.novoUsuario();
			abreTelaCadastro();

		} catch (Exception e) {

			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
		} finally {
			reloadForm();
		}
	}

	protected void abreTelaCadastro() throws Exception {
		Stage caixa = new CaixaGui();

		abreJanelaModal(caixa);
	}

	@Override
	void reloadForm() throws Exception {

		TableView<CaixaModel> table = (TableView<CaixaModel>) ((Pane) tablePane.getContent()).getChildren().get(0);
		ObservableList<CaixaModel> items = table.getItems();
		items.removeAll(items);
		carregaDadosTable(table);
	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		List<CaixaModel> caixas = new ArrayList<>();
		// if (controller.getList().isEmpty())
		controller.buscaTodos();
		boolean ativaBotaoAbrir = false;
		for (Object item : controller.getList()) {
			controller.busca(((Caixa) item).getIdCaixa());
			Caixa caixa = (Caixa) item;
			ativaBotaoAbrir = caixa.getDataFechamento() == null;
			try {
				String usuarioAbertura = caixa != null ? caixa.getUsuarioAbertura().getNomeCompleto() : "";
				String trocoInicial = Util.formataMoeda(caixa != null ? caixa.getSaldoInicial() : 0f);
				String dataHoraFechamento = caixa != null && caixa.getDataFechamento() != null
						? Util.formataDataHora(caixa.getDataFechamento())
						: "";
				String totalVendido = Util.formataMoeda(sumarizaVendasCaixa());

				String trocoFinal = Util.formataMoeda(caixa != null ? caixa.getSaldoFinal() : 0f);
				String status = caixa != null && caixa.getDataFechamento() == null ? "Aberto" : "Fechado";
				String operadorFechamento = caixa != null && caixa.getUsuarioFechamento() != null
						? caixa.getUsuarioFechamento().getNomeCompleto()
						: "";
				//@formatter:off
				caixas.add(new CaixaModel(Util.formataDataHora(caixa.getDataAbertura()),
						usuarioAbertura,
						trocoInicial,
						totalVendido,
						dataHoraFechamento,
						operadorFechamento,
						trocoFinal,
						status
						));
						//@formatter:on
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		ObservableList<CaixaModel> dados = FXCollections.observableArrayList(caixas);
		table.setItems(dados);
		button.setDisable(ativaBotaoAbrir);
	}

	/**
	 * 
	 * @param caixa
	 * @return
	 */
	private Float sumarizaVendasCaixa() {
		float totalVendas = 0;
		try {
			totalVendas = getCaixaController().getTotalVendasCaixa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return totalVendas;
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

	}

	@Override
	protected void handleTableClick(Event event) {
		super.handleTableClick(event);
		try {
			CaixaController controller = getCaixaController();
			
			controller.buscaCaixaDia(Util.parseData(super.queryParam));
			abreTelaCadastro();
			reloadForm();
		} catch (Exception e) {
			showErrorMessage(e);
		} finally {

		}
	}
}
