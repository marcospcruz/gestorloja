package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.CaixaModel;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.view.fxui.model.VendaModel;
import br.com.marcospcruz.gestorloja.view.util.VendaModelBuilder;
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

public class VendasCaixaGui extends StageBase {
	//@formatter:off
	private static final Object[] COLUNAS_TABLEVIEW = {
			"id",
			"dataVenda",
			"nomeVendedor",
			"valorVenda",
			"subTotalVendido",
			"descontoConcedido",
			"totalRecebido",
			"meiosPagamento",
			"status"
	};
	private TitledPane tablePane;
	//@formatter:on
	public VendasCaixaGui() throws Exception {
		super();
		controller = getCaixaController();

		super.setLabelColunas(COLUNAS_TABLEVIEW);

		getvBox().setSpacing(5);
		getvBox().setPadding(new Insets(10, 0, 0, 10));
		scene = new Scene(new Group(), width, height);
		double layoutsMaxWidth = scene.widthProperty().get();

		super.setLayoutsMaxWidth(layoutsMaxWidth);

		tablePane = super.criaTablePane("Vendas");
		TitledPane buttonPane = criaButtonPane();
		getvBox().getChildren().addAll(buttonPane, tablePane);
		((Group) scene.getRoot()).getChildren().add(getvBox());
		setScene(scene);
	}

	private TitledPane criaButtonPane() throws Exception {
		TitledPane titledPane = new TitledPane("", new Button());
		titledPane.setCollapsible(false);
		FlowPane content = new FlowPane(Orientation.HORIZONTAL);
		Button button = new Button("Abrir Caixa");
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
			showErrorMessage(e.getMessage());
		}
	}

	protected void openCaixaGui() throws Exception {
		((CaixaController) controller).validateCaixaAberto();
		controller.novo();
		abreTelaCadastro();
		reloadForm();
	}

	protected void abreTelaCadastro() throws Exception {
		Stage pdv=new PDV(true);

		abreTelaCadastro(pdv);
	}

	@Override
	void reloadForm() throws Exception {

		TableView<CaixaModel> table = (TableView<CaixaModel>) ((Pane) tablePane.getContent()).getChildren().get(0);
		ObservableList<CaixaModel> items = table.getItems();
		items.removeAll(items);
		carregaDadosTable(table);
	}

	@Override
	protected void carregaDadosTable(@SuppressWarnings("rawtypes") TableView table) throws Exception {
		List<VendaModel> dadoss = new ArrayList<>();
		Caixa caixa = (Caixa) controller.getItem();
		VendaController controller = getVendaController();
		for (Venda venda : caixa.getVendas()) {
			controller.setVenda(venda);
			Date dataVenda = venda.getDataVenda();
			Usuario vendedor = venda.getOperador();
			float subTotalVendido = venda.getTotalVendido();
			float totalRecebido = controller.getValorTotalPagamentoVenda();
			float descontoConcedido = venda.getPorcentagemDesconto();
			StringBuilder tiposMeioPagamentoString = new StringBuilder();
			for (MeioPagamento meioPagamento : venda.getPagamento().getMeiosPagamento()) {
				String tmp = meioPagamento.getTipoMeioPagamento().getDescricaoTipoMeioPagamento() + ", ";
				tiposMeioPagamentoString.append(tmp);
			}
			tiposMeioPagamentoString = new StringBuilder(
					tiposMeioPagamentoString.toString().substring(0, tiposMeioPagamentoString.toString().length() - 2));
			float totalVendaBruto = controller.getValorBrutoVenda();
			//@formatter:off
			VendaModelBuilder builder=new VendaModelBuilder();
			VendaModel vendaModel = builder
					.setIdVenda(venda.getIdVenda())
					.setDataVenda(dataVenda)
					.setNomeVendedor(vendedor.getNomeCompleto())
					.setValorBrutoVenda(totalVendaBruto)
					.setSubTotalVendido(subTotalVendido)
					.setTotalPagamentoRecebido(totalRecebido)
					.setPorcentagemDescontoConcedido(descontoConcedido)
					.setMeiosPagamentos(tiposMeioPagamentoString.toString())
					.setStatus(venda.isEstornado())
					.getVendaModel();
			//@formatter:on
			dadoss.add(vendaModel);
		}
		ObservableList<VendaModel> dados = FXCollections.observableArrayList(dadoss);
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

	}

	@Override
	protected void handleTableClick(Event event) {
		// super.handleTableClick(event);
		int idVenda = Integer.parseInt(getTableViewSelectedValueId(event));
		Caixa caixa = (Caixa) controller.getItem();
		Venda venda = caixa.getVendas().stream().filter(v -> v.getIdVenda() == idVenda).findFirst().orElse(null);
	
		try {
			getVendaController().setVenda(venda);
			abreTelaCadastro();
			reloadForm();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
}
