package br.com.marcospcruz.gestorloja.view.fxui;

import java.text.SimpleDateFormat;

import br.com.marcospcruz.gestorloja.builder.TransacaoFinanceiraBuilder;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.TransacaoFinanceira;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class OperacaoCaixaGui extends StageBase {

	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private GridPane grid;
	private TextField txtDescricao;
	private NumberTextField txtValorTransacao;
	private RadioButton radioReceita;
	private RadioButton radioDespesa;

	public OperacaoCaixaGui() throws Exception {
		super();
		controller = getCaixaController();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String parsedDate = sdf.format(SingletonManager.getInstance().getData());
		setTitle("Caixa dia " + parsedDate);
		super.setDimension(650, 160);
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
		Button btnSave = new Button("Salvar");
		btnSave.setOnAction(arg0 -> {
			try {
				if (showConfirmAtionMessage("Deseja salvar esta Operação?")) {
					salvaOperacao(arg0);
					showMensagemSucesso("Operação registrada com sucesso.");
					hide();
				}

			} catch (Exception e) {
				
				super.showErrorMessage("Falha ao registrar operação.",e);
				e.printStackTrace();
			} finally {

			}
		});

		pane.setHgap(10);
		Button btnFechar = new Button("Cancelar");
		btnFechar.setOnAction(arg0 -> {
			hide();
		});
		btnFechar.setDisable(controller.getItem() == null);

		pane.getChildren().addAll(btnSave, btnFechar);
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
			// Caixa caixa = (Caixa) controller.getItem();
			// caixa.setDataFechamento(Util.parseData(horaFechamentoLbl.getText()));
			controller.fechaCaixa();

		}
	}

	private void salvaOperacao(ActionEvent e) throws Exception {

		Caixa caixa = (Caixa) controller.getItem();
		//@formatter:off
		TransacaoFinanceira transacao = new TransacaoFinanceiraBuilder()
				.criaTransacao(radioReceita.isSelected())
				.setCaixa(caixa)
				.setMotivo(txtDescricao.getText())
				.setValorTransacaoFinanceira(txtValorTransacao.getText())
				.getTransacaoFinanceira();
		// .criaTransacao(radioReceita.isSelected())
		((CaixaController)controller).adicionaTransacao(transacao);
		
//		salvaDados(e);
		// hide();
		// reloadForm();

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

		grid.add(buttonsPane(), 0, 1);
	}

	private Node criaDadosCaixa() {
		TitledPane titledPane = new TitledPane("Transação", new Button());
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
		dadosGrid.add(criaLabelBold("Motivo:"), 0, row);
		txtDescricao = new TextField();
		dadosGrid.add(txtDescricao, 1, row++);
		
		dadosGrid.add(criaLabelBold("Valor (R$):"), 0, row);
		txtValorTransacao = new NumberTextField(true);
		dadosGrid.add(txtValorTransacao, 1, row++);
		
		dadosGrid.add(criaLabelBold("Tipo Operação:"), 0, row);
		FlowPane flowPane = new FlowPane();
		flowPane.setHgap(20);
		radioReceita = new RadioButton("Receita");
		radioReceita.setSelected(true);
		ToggleGroup group = new ToggleGroup();
		radioReceita.setToggleGroup(group);
		radioDespesa = new RadioButton("Despesa");
		radioDespesa.setToggleGroup(group);

		flowPane.getChildren().addAll(radioReceita, radioDespesa);
		dadosGrid.add(flowPane, 1, row++);
		titledPane.setContent(dadosGrid);
		return titledPane;
	}

	private void defineNode(Node trocoInicial, String valorTroco) {
		if (trocoInicial instanceof Label) {

			((Label) trocoInicial).setText(Util.formataMoeda(Util.parseStringDecimalToFloat(valorTroco)));
		} else
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
