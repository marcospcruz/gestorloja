package br.com.marcospcruz.gestorloja.view.fxui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
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
	private static final String INTERFACE_VENDAS_CAIXA = "VendasCaixaGui";
	private GridPane grid;
	private Label horaAberturaLbl;
	private Label trocoInicial;
	private Label saldoFechamento;
	private Label horaFechamentoLbl;

	public CaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String parsedDate = sdf.format(SingletonManager.getInstance().getData());
		setTitle("Caixa dia " + parsedDate);
		super.setDimension(650, 600);
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
			Label valor = criaLabelNormal(Util.formataMoeda(subTotaisMap.get(key)));
			grid.add(valor, 1, rowIndex++);
		}
		titledPane.setContent(grid);
		titledPane.setMaxHeight(Double.MAX_VALUE);
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
		int column = 0, row = 0;
		CaixaController controller = getCaixaController();
		Label qtVendasEfetuadasLbl = criaLabelBold("Qt. Vendas Efetivadas:");
		int qtVendas = controller.getQuantidadeVendasEfetivadas();
		subTotalGrid.add(qtVendasEfetuadasLbl, column++, row);
		subTotalGrid.add(criaLabelNormal(Integer.toString(qtVendas)), column--, row++);
		Label qtVendasEstornadasLbl = criaLabelBold("Qt. Vendas Estornadas:");
		int qtVendasEstornadas = controller.getQuantidadeVendasEstornadas();
		subTotalGrid.add(qtVendasEstornadasLbl, column++, row);
		subTotalGrid.add(criaLabelNormal(Integer.toString(qtVendasEstornadas)), column--, row++);
		Label totalVendidoLbl = criaLabelBold("Total Vendido:");
		subTotalGrid.add(totalVendidoLbl, column++, row);
		String totalVendido = Util.formataMoeda(controller.getSubTotalVendas());
		subTotalGrid.add(criaLabelNormal(totalVendido), column--, row++);
		String totalRecebido = Util.formataMoeda(controller.getTotalPagamentosVendasRecebidoCaixa());
		// String totalRecebido=
		subTotalGrid.add(criaLabelBold("Total Pagamentos:"), column++, row);
		subTotalGrid.add(criaLabelNormal(totalRecebido), column--, row++);
		//
		subTotalGrid.add(criaLabelBold("Diferença (Venda - Pagamentos):"), column++, row);
		float diferenca = controller.getSubTotalVendas() - controller.getTotalPagamentosVendasRecebidoCaixa();
		subTotalGrid.add(criaLabelNormal(Util.formataMoeda(diferenca)), column--, row++);
		Crud<Caixa> dao = new CrudDao<>();
		Caixa caixa = (Caixa) controller.getItem();
		if (caixa.getIdCaixa() != 0)
			caixa = dao.update(caixa);
		Button button = new Button("Visualizar Vendas");
		boolean disableButton;
		try {

			disableButton = caixa.getVendas() == null || caixa.getVendas().isEmpty();
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(this.getClass()).warn(e);
			disableButton = qtVendas == 0;
		}
		button.setDisable(disableButton);
		button.setOnAction(evt -> {
			abrirInterface(INTERFACE_VENDAS_CAIXA);

		});
		subTotalGrid.add(button, column--, row++);
		//
		titledPane.setContent(subTotalGrid);
		return titledPane;
	}

	protected void abrirInterface(String nomeInterface) {
		try {

			Stage stage = StageFactory.createStage("br.com.marcospcruz.gestorloja.view.fxui." + nomeInterface);
			stage.initOwner(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			ObservableList<Node> children = grid.getChildren();
			children.removeAll(children);

			populaGridContent();
		} catch (Exception e) {

			SingletonManager.getInstance().getLogger(this.getClass()).error(e.getMessage(), e);
		}
		// finally {
		// atualizaDadosCaixa();
		//
		// }
	}

	/**
	 * 
	 */
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
				if (showConfirmAtionMessage("Deseja abrir o Caixa?")) {
					abreCaixa(arg0);
					showMensagemSucesso("Caixa aberto com sucesso.");
				}

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
		try {
			CaixaController controller = getCaixaController();
			boolean caixaFechado = ((Caixa) controller.getItem()).getDataFechamento() != null;
			btnFechar.setDisable(caixaFechado);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		btnFechar.setOnAction(arg0 -> {
			try {
				fechaCaixa(arg0);

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
				if (button.getText().contains("Fecha")) {
					boolean desativa = false;
					if (caixa.getDataFechamento() != null)
						desativa = true;
					button.setDisable(desativa);
				}
			}
		}

	}

	private void fechaCaixa(ActionEvent e) throws Exception {
		CaixaController controller = getCaixaController();
		if (showConfirmAtionMessage("Deseja fechar o Caixa?")) {
			// Caixa caixa = (Caixa) controller.getItem();
			// caixa.setDataFechamento(Util.parseData(horaFechamentoLbl.getText()));
			controller.fechaCaixa();
			showMensagemSucesso("Caixa Fechado com Sucesso");

		}
	}

	private void abreCaixa(ActionEvent e) throws Exception {

		// controller.novo();
		Caixa caixa = (Caixa) controller.getItem();
		// caixa.setDataAbertura(SingletonManager.getInstance().getData());
		// caixa.setUsuarioAbertura(SingletonManager.getInstance().getUsuarioLogado());
		// caixa.setSaldoInicial(Util.parseStringDecimalToFloat(trocoInicial.getText()));

		salvaDados(e);
		// hide();
		// reloadForm();

	}

	private void close(ActionEvent e) {
		controller.novoUsuario();
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

	private Node criaDadosCaixa() throws Exception {
		CaixaController controller = getCaixaController();
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
		horaAberturaLbl = criaLabelNormal("");
		dadosGrid.add(horaAberturaLbl, 1, row++);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(() -> {
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
					String dataAbertura = sdf.format(caixa.getDataAbertura());
					// preencheData(caixa, caixa.getDataAbertura());
					String dataFechamento = preencheData(caixa, caixa.getDataFechamento());

					horaAberturaLbl.setText(dataAbertura);

					horaFechamentoLbl.setText(dataFechamento);
				});
				// horaAberturaLbl.setText(dataAbertura);
				// horaFechamentoLbl.setText(dataFechamento);
			}

			protected String preencheData(Caixa caixa, Date data) {

				String formattedDate;
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
				if (caixa.getIdCaixa() == 0 || data == null) {

					formattedDate = sdf.format(SingletonManager.getInstance().getData());

				} else {

					formattedDate = sdf.format(data);
				}
				return formattedDate;
			}
		}, 0, 1000);
		dadosGrid.add(criaLabelBold("Operador Abertura:"), 0, row);
		String nomeOperador;
		if (caixa != null && caixa.getIdCaixa() != 0 || caixa.getUsuarioAbertura() != null)
			nomeOperador = caixa.getUsuarioAbertura().getNomeCompleto();
		else
			nomeOperador = SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto();
		dadosGrid.add(criaLabelNormal(nomeOperador), 1, row++);
		dadosGrid.add(criaLabelBold("Saldo Abertura:"), 0, row);
		trocoInicial = criaLabelNormal("");
		if (caixa != null) {
			boolean caixaJaAberto = caixa.getSaldoInicial() > 0f;
			String valorTroco = Util.formataStringDecimaisVirgula(!caixaJaAberto ? 0f : caixa.getSaldoInicial());
			trocoInicial.setText(valorTroco);

		}
		dadosGrid.add(trocoInicial, 1, row++);
		dadosGrid.add(criaLabelBold("Data Hora Fechamento:"), 0, row);
		horaFechamentoLbl = criaLabelNormal("");
		dadosGrid.add(horaFechamentoLbl, 1, row++);
		//
		// Timer timer2 = new Timer();
		// timer2.scheduleAtFixedRate(new TimerTask() {
		//
		// @Override
		// public void run() {
		// SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
		// String formattedDate;
		//// if (caixa.getIdCaixa() != 0) {
		//
		// formattedDate = sdf.format(SingletonManager.getInstance().getData());
		//
		//// } else {
		//// formattedDate = "";
		//// }
		// Platform.runLater(() -> horaFechamentoLbl.setText(formattedDate));
		// }
		// }, 0, 1000);
		//
		dadosGrid.add(criaLabelBold("Operador Fechamento:"), 0, row);
		Label usuarioFechamento = criaLabelNormal("");
		if (caixa != null && caixa.getIdCaixa() != 0)
			usuarioFechamento.setText(SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto());
		dadosGrid.add(usuarioFechamento, 1, row++);
		dadosGrid.add(criaLabelBold("Saldo Fechamento:"), 0, row);
		saldoFechamento = criaLabelNormal("");
		if (caixa != null
		// && caixa.getIdCaixa() != 0
		) {
			saldoFechamento.setText(Util.formataMoeda(caixa.getSaldoFinal()));

		}
		dadosGrid.add(saldoFechamento, 1, row++);
		dadosGrid.add(criaLabelBold("Total Receita:"), 0, row);
		dadosGrid.add(criaLabelNormal(Util.formataMoeda(controller.getTotalTransacoesCaixa(1))), 1, row++);
		dadosGrid.add(criaLabelBold("Total Despesa:"), 0, row);
		dadosGrid.add(criaLabelNormal(Util.formataMoeda(controller.getTotalTransacoesCaixa(2))), 1, row++);

		Button button = new Button("Adicionar Entrada / Saída");
		boolean desativaButton = ((Caixa) controller.getItem()).getDataFechamento() != null;
		button.setDisable(desativaButton);
		button.setOnAction(evt -> {
			abrirInterface("OperacaoCaixaGui");
			try {

				// controller.atualizaSaldoCaixa();

			} catch (Exception e) {

				e.printStackTrace();
			} finally {

			}
		});
		dadosGrid.add(button, 0, ++row, 2, 1);
		titledPane.setContent(dadosGrid);
		return titledPane;
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
