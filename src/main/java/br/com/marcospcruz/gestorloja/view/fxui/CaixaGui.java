package br.com.marcospcruz.gestorloja.view.fxui;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CaixaGui extends StageBase {

	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + "H:mm:ss";
	private GridPane grid;
	private Label horaAberturaLbl;
	private Node trocoInicial;
	private NumberTextField saldoFechamento;
	private Label horaFechamentoLbl;

	public CaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String parsedDate = sdf.format(SingletonManager.getInstance().getData());
		setTitle("Caixa dia " + parsedDate);
		super.setDimension(650, 450);
		double thisWidth = super.getWidth() - (super.getWidth() * 10 / 100);
		setLayoutsMaxWidth(thisWidth);
		resizableProperty().setValue(Boolean.FALSE);
		Group root = new Group();
		scene = new Scene(root, width, height);

		grid = new GridPane();

		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setMinHeight(getHeight());

		double w = getWidth();
		grid.setMinWidth(width);
		grid.setMaxHeight(getHeight());
		populaGridContent();

		// grid.add(buttonsPane(), 1, 9, 2, 1);

		populaForm();
		root.getChildren().add(grid);

		setScene(scene);

	}

	private Node criaResumoDeVendasTitledView() throws Exception {
		TitledPane titledPane = new TitledPane("Resumo Operações do Caixa", new Button());
		titledPane.setCollapsible(false);
		GridPane grid = new GridPane();

		//
		grid.setHgap(20);
		grid.setVgap(10);
		// grid.setMinHeight(getHeight());
		// grid.setMinWidth(getWidth());
		// grid.setMaxHeight(getHeight());
		//
		TitledPane subTotaisPane = (TitledPane) criaSubTotaisView();
		subTotaisPane.setMaxHeight(Double.MAX_VALUE);
		grid.add(subTotaisPane, 0, 0);
		grid.add(criaSubTotaisPagamentos(), 1, 0);
		titledPane.setContent(grid);
		return titledPane;
	}

	private Node criaSubTotaisPagamentos() throws Exception {
		TitledPane titledPane = new TitledPane("Pagamentos Recebidos", new Button());
		titledPane.setCollapsible(false);
		GridPane grid = new GridPane();
		// GridPane.setFillHeight(grid, Boolean.TRUE);
		grid.setHgap(20);
		grid.setVgap(10);
		CaixaController controller = getCaixaController();
		Map<String, Float> subTotaisMap = controller.getSubTotaisMeiosPagamentoRecebidos();
		int rowIndex = 0;
		for (String key : subTotaisMap.keySet()) {
			Label descricao = criaLabelBold(key);
			grid.add(descricao, 0, rowIndex);
			Label valor = new Label(Util.formataMoeda(subTotaisMap.get(key)));
			grid.add(valor, 1, rowIndex++);
		}
		titledPane.setContent(grid);
		return titledPane;
	}

	private Node criaSubTotaisView() throws Exception {
		TitledPane titledPane = new TitledPane("Sub Totais", new Button());
		titledPane.setCollapsible(false);

		GridPane subTotalGrid = new GridPane();
		subTotalGrid.setHgap(5);
		subTotalGrid.setVgap(10);
		// grid.setMinHeight(getHeight());
		// grid.setMinWidth(getWidth());
		// grid.setMaxHeight(getHeight());
		//
		CaixaController controller = getCaixaController();

		Label totalVendidoLbl = criaLabelBold("Total Vendido:");
		subTotalGrid.add(totalVendidoLbl, 0, 0);
		String totalVendido = Util.formataMoeda(controller.getSubTotalVendas());
		subTotalGrid.add(new Label(totalVendido), 1, 0);
		String totalRecebido = Util.formataMoeda(controller.getTotalRecebidoCaixa());
		// String totalRecebido=
		subTotalGrid.add(criaLabelBold("Total Pagamentos:"), 0, 1);
		subTotalGrid.add(new Label(totalRecebido), 1, 1);
		//
		subTotalGrid.add(criaLabelBold("Diferença (Venda - Pagamentos):"), 0, 2);
		float diferenca = controller.getSubTotalVendas() - controller.getTotalRecebidoCaixa();
		subTotalGrid.add(new Label(Util.formataMoeda(diferenca)), 1, 2);
		Button button = new Button("Visualizar Vendas");
		button.setOnAction(evt -> {
			try {
				Stage stage = StageFactory.createStage("br.com.marcospcruz.gestorloja.view.fxui.VendasCaixaGui");
				stage.initOwner(this);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {

				e.printStackTrace();
			} finally {
				atualizaDadosCaixa();

			}

		});
		subTotalGrid.add(button, 0, 3);
		//
		titledPane.setContent(subTotalGrid);
		return titledPane;
	}

	public void atualizaDadosCaixa() {
		try {
			CaixaController controller = getCaixaController();
			Caixa caixa = (Caixa) controller.getItem();
			saldoFechamento.setText(Util.formataMoeda(caixa.getSaldoFinal()));
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	protected Node buttonsPane() {
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		// pane.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE,
		// CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setMaxWidth(Double.MAX_VALUE);
		pane.setRowValignment(VPos.BASELINE);
		pane.setAlignment(Pos.BASELINE_CENTER);
		Button btnSave = new Button("Abre Caixa");
		btnSave.setOnAction(arg0 -> {
			try {
				abreCaixa(arg0);
				showMensagemSucesso("Caixa aberto com sucesso.");
			} catch (Exception e) {
				showErrorMessage("Falha ao abrir o Caixa.");
				e.printStackTrace();
			} finally {
				hide();
			}
		});

		btnExcluir = new Button("Cancelar");

		btnExcluir.setOnAction(this::close);
		pane.setHgap(10);
		Button btnFechar = new Button("Fechar Caixa");
		btnFechar.setOnAction(arg0 -> {
			try {
				fechaCaixa(arg0);
				showMensagemSucesso("Caixa Fechado com Sucesso");
				reloadForm();
			} catch (Exception e1) {
				showErrorMessage(e1.getMessage());
				e1.printStackTrace();
			} finally {
				hide();
			}
		});

		pane.getChildren().addAll(btnSave, btnFechar, btnExcluir);
		try {
			defineEstadoBotoes(pane.getChildren());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return pane;
	}

	/**
	 * 
	 * @param children
	 * @throws Exception
	 */
	private void defineEstadoBotoes(ObservableList<Node> children) throws Exception {
		Caixa caixa = (Caixa) getCaixaController().getItem();
		for (Node node : children) {
			Button button = (Button) node;

			if (caixa.getIdCaixa() == 0) {
				if (button.getText().contains("Abre"))
					button.setDisable(false);
				if (button.getText().contains("Fecha"))
					button.setDisable(true);

			} else {
				if (button.getText().contains("Abre"))
					button.setDisable(true);
				if (button.getText().contains("Fecha"))
					button.setDisable(false);
			}
		}

	}

	private void fechaCaixa(ActionEvent e) throws Exception {
		CaixaController controller = getCaixaController();
		if (showConfirmAtionMessage("Deseja fechar o Caixa?")) {
			Caixa caixa = (Caixa) controller.getItem();
			// caixa.setDataFechamento(Util.parseData(horaFechamentoLbl.getText()));
			controller.fechaCaixa();

		}
	}

	private void abreCaixa(ActionEvent e) throws Exception {
		if (showConfirmAtionMessage("Deseja abrir o Caixa?")) {
			controller.novo();
			Caixa caixa = (Caixa) controller.getItem();
			caixa.setDataAbertura(SingletonManager.getInstance().getData());
			caixa.setUsuarioAbertura(SingletonManager.getInstance().getUsuarioLogado());
			caixa.setSaldoInicial(Util.parseStringDecimalToFloat(((TextField) trocoInicial).getText()));

			salvaDados(e);
			// hide();
			// reloadForm();

		}
	}

	private void close(ActionEvent e) {
		controller.novo();
		hide();
	}

	protected void populaForm() {
	}

	protected void excluiDados(ActionEvent event) {
	}

	protected void salvaDados(ActionEvent event) throws Exception {

		controller.salva();

	}

	protected void populaGridContent() throws Exception {
		grid.add(criaDadosCaixa(), 0, 0);
		grid.add(criaResumoDeVendasTitledView(), 0, 1);
		grid.add(buttonsPane(), 0, 2);
	}

	private Node criaDadosCaixa() {
		TitledPane titledPane = new TitledPane("Dados do Caixa", new Button());
		titledPane.setCollapsible(false);

		GridPane dadosGrid = new GridPane();
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setHalignment(HPos.RIGHT);
		// column1.setPercentWidth(60);
		// column2.setPercentWidth(30);
		column2.setHgrow(Priority.NEVER);
		column2.setFillWidth(true);
		dadosGrid.getColumnConstraints().add(column1);
		dadosGrid.getColumnConstraints().add(column2);
		dadosGrid.setHgap(10);
		dadosGrid.setVgap(10);

		int row = 0;
		Caixa caixa = (Caixa) controller.getItem();
		dadosGrid.add(criaLabelBold("Data Hora Abertura Caixa:"), 0, row);
		horaAberturaLbl = new Label();
		dadosGrid.add(horaAberturaLbl, 1, row++);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
				String formattedDate;
				if (caixa.getIdCaixa() == 0) {

					formattedDate = sdf.format(SingletonManager.getInstance().getData());

				} else {
					formattedDate = sdf.format(caixa.getDataAbertura());
				}
				Platform.runLater(() -> horaAberturaLbl.setText(formattedDate));
			}
		}, 0, 1000);
		dadosGrid.add(criaLabelBold("Operador Abertura:"), 0, row);
		String nomeOperador;
		if (caixa != null && caixa.getIdCaixa() != 0)
			nomeOperador = caixa.getUsuarioAbertura().getNomeCompleto();
		else
			nomeOperador = SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto();
		dadosGrid.add(criaLabelNormal(nomeOperador), 1, row++);
		dadosGrid.add(criaLabelBold("Saldo Abertura:"), 0, row);

		if (caixa != null) {
			boolean caixaJaAberto = caixa.getSaldoInicial() > 0f;
			if (caixaJaAberto)
				trocoInicial = criaLabelNormal("");
			else
				trocoInicial = new NumberTextField(true);
			String valorTroco = Util.formataMoeda(caixa.getSaldoInicial());
			defineNode(trocoInicial, valorTroco);

		}
		dadosGrid.add(trocoInicial, 1, row++);
		dadosGrid.add(criaLabelBold("Data Hora Fechamento:"), 0, row);
		horaFechamentoLbl = new Label();
		dadosGrid.add(horaFechamentoLbl, 1, row++);
		//
		Timer timer2 = new Timer();
		timer2.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
				String formattedDate;
				if (caixa.getIdCaixa() != 0) {

					formattedDate = sdf.format(SingletonManager.getInstance().getData());

				} else {
					formattedDate = "";
				}
				Platform.runLater(() -> horaFechamentoLbl.setText(formattedDate));
			}
		}, 0, 1000);
		//
		dadosGrid.add(criaLabelBold("Operador Fechamento:"), 0, row);
		Label usuarioFechamento = new Label();
		if (caixa != null && caixa.getIdCaixa() != 0)
			usuarioFechamento.setText(SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto());
		dadosGrid.add(usuarioFechamento, 1, row++);
		dadosGrid.add(criaLabelBold("Saldo Fechamento:"), 0, row);
		saldoFechamento = new NumberTextField(true);
		if (caixa != null && caixa.getIdCaixa() != 0) {
			saldoFechamento.setText(Util.formataMoeda(caixa.getSaldoFinal()));

		} else
			saldoFechamento.setDisable(true);
		dadosGrid.add(saldoFechamento, 1, row);

		titledPane.setContent(dadosGrid);
		return titledPane;
	}

	private void defineNode(Node trocoInicial, String valorTroco) {
		if (trocoInicial instanceof Label)
			((Label) trocoInicial).setText(valorTroco);
		else
			((TextField) trocoInicial).setText(valorTroco);

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
