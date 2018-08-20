package br.com.marcospcruz.gestorloja.view.fxui;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.application.Platform;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class CaixaGui extends StageBase {

	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + "H:mm:ss";
	private GridPane grid;
	private Label horaAberturaLbl;
	private NumberTextField trocoInicial;
	private NumberTextField saldoFechamento;
	private Label horaFechamentoLbl;

	public CaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String parsedDate = sdf.format(SingletonManager.getInstance().getData());
		setTitle("Caixa dia " + parsedDate);
		super.setDimension(550, 250);
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
		column1.setHalignment(HPos.RIGHT);
		column1.setPercentWidth(30);
		column2.setPercentWidth(60);
		column2.setHgrow(Priority.NEVER);
		column2.setFillWidth(true);
		populaGridContent();

		grid.getColumnConstraints().add(column1);
		grid.getColumnConstraints().add(column2);
		grid.add(buttonsPane(), 1, 8, 2, 1);
		populaForm();
		root.getChildren().add(grid);

		setScene(scene);

	}

	protected Node buttonsPane() {
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		pane.setRowValignment(VPos.BASELINE);
		Button btnSave = new Button("Abre Caixa");
		btnSave.setOnAction(arg0 -> {
			try {
				abreCaixa(arg0);
			} catch (Exception e) {

				e.printStackTrace();
			}
		});

		btnExcluir = new Button("Cancelar");

		btnExcluir.setOnAction(this::close);
		pane.setHgap(10);
		pane.getChildren().addAll(btnSave, btnExcluir);
		return pane;
	}

	private void abreCaixa(ActionEvent e) throws Exception {
		if (showConfirmAtionMessage("Deseja abrir o Caixa?")) {
			controller.novo();
			Caixa caixa = (Caixa) controller.getItem();
			caixa.setDataAbertura(SingletonManager.getInstance().getData());
			caixa.setUsuarioAbertura(SingletonManager.getInstance().getUsuarioLogado());
			caixa.setSaldoInicial(Util.parseStringDecimalToFloat(trocoInicial.getText()));

			salvaDados(e);
			hide();
			reloadForm();

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

	protected void salvaDados(ActionEvent event) {
		try {
			controller.salva();
			showMensagemSucesso("Caixa aberto com sucesso.");
		} catch (Exception e) {
			showErrorMessage("Falha ao abrir o Caixa.");
			e.printStackTrace();
		}
	}

	protected void populaGridContent() {
		int row = 0;
		Caixa caixa = (Caixa) controller.getItem();
		grid.add(new Label("Data Hora Abertura Caixa:"), 0, row);
		horaAberturaLbl = new Label();
		grid.add(horaAberturaLbl, 1, row++);
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
		grid.add(new Label("Operador Abertura:"), 0, row);
		String nomeOperador;
		if (caixa != null && caixa.getIdCaixa() != 0)
			nomeOperador = caixa.getUsuarioAbertura().getNomeCompleto();
		else
			nomeOperador = SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto();
		grid.add(new Label(nomeOperador), 1, row++);
		grid.add(new Label("Saldo Abertura:"), 0, row);
		trocoInicial = new NumberTextField();
		if (caixa != null) {
			String valorTroco = Util.formataMoeda(caixa.getSaldoInicial());
			trocoInicial.setText(valorTroco);
		}
		grid.add(trocoInicial, 1, row++);
		grid.add(new Label("Data Hora Fechamento:"), 0, row);
		horaFechamentoLbl = new Label();
		grid.add(horaFechamentoLbl, 1, row++);

		grid.add(new Label("Operador Fechamento:"), 0, row);
		Label usuarioFechamento = new Label();
		if (caixa != null && caixa.getIdCaixa() != 0)
			usuarioFechamento.setText(SingletonManager.getInstance().getUsuarioLogado().getNomeCompleto());
		grid.add(usuarioFechamento, 1, row++);
		grid.add(new Label("Saldo Fechamento:"), 0, row);
		saldoFechamento = new NumberTextField();
		if (caixa != null && caixa.getIdCaixa() != 0)
			saldoFechamento.setText(Util.formataMoeda(caixa.getSaldoFinal()));
		else
			saldoFechamento.setDisable(true);
		grid.add(saldoFechamento, 1, row);

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
