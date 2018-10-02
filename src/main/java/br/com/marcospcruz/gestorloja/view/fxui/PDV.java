package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.custom.AutoCompleteTextField;
import br.com.marcospcruz.gestorloja.view.fxui.custom.NumberTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PDV extends StageBase {
	private static final String VALOR_R$ = "Valor (R$):";
	//@formatter:off
	private static final Object[] COLUNAS_TABLEVIEW = { 
			 
			"codigoProduto", 
			"fabricante", 
			"categoria", 
			"produto",
			"quantidade", 
			"valorProduto", 
			"valorVenda", 
			"valorTotal" 
			};
	private static final String TOTAL_RECEBIDO = "Total Recebido (R$):";
	private static final String TROCO_LABEL = "Troco (R$):";
	private static final String SUB_TOTAL_VENDA = "Sub-Total (R$):";
	private static final String FONT_VERDANA = "Verdana";
	private static final String VALOR_VENDA = "Valor Venda:";
	//@formatter:on
	private AutoCompleteTextField<ItemEstoque> estoqueDropDown;
	private RadioButton radioCodigoBarra;
	private RadioButton radioDescricaoProduto;
	private Button adicionarBtn;
	private Button removerBtn;
	private Label fabricanteLabel;
	private Label subCategoriaLabel;
	private Label produtoLabel;
	private Label valorUnitLabel;
	private NumberTextField quantidadeTxt;

	private List<Node> nodesDadosProdutos;
	private List<Node> dadosProdutos;
	private Label marcaLabel;
	private Label categoriaLbl;
	private Label produtoLbl;
	private Label valorUnitLbl;
	private Label qtLabel;
	private NumberTextField descontoTxt;
	private NumberTextField subTotalTxt;
	private Label trocoLabel;

	private NumberTextField valorDinheiroTxt;
	private Map<CheckBox, List<Node>> formaPagamentoMap;

	private ItemEstoque itemEstoque;
	private TableView<ItemVendaModel> table;
	private String selectedCodigoBarra;
	private CheckBox lastSelected;
	private Label valorVendaLabel;
	private boolean atualizaDesconto;
	private boolean naoAtualizaDesconto;
	private boolean novaVenda;
	private Label subTotalRecebido;
	private boolean editaVenda;

	public PDV() throws Exception {
		this(false);
	}

	protected void configuraLayout() throws Exception {
		double layoutsMaxWidth = SingletonManager.getInstance().getScreenWidth();
		// layoutsMaxWidth -= layoutsMaxWidth * (27d / 100d);

		super.setLayoutsMaxWidth(layoutsMaxWidth);
		TitledPane pesquisaPane = criaPesquisaPane();
		TitledPane produtoPane = criaProdutoPane();
		GridPane panel = criaGridPane();
		getvBox().getChildren().addAll(pesquisaPane, produtoPane, panel);
	}

	public PDV(boolean editaVenda) throws Exception {
		super();
		this.editaVenda = editaVenda;
		setOnCloseRequest(this::closingAction);
		if (!editaVenda) {
			novaVenda = true;
			String dataAtual = Util.formataData(SingletonManager.getInstance().getData());
			setTitle("Vendas - " + dataAtual);
			try {
				CaixaController caixaController = getCaixaController();
				caixaController.validateCaixaFechado();
				getVendaController().resetVenda();
			} catch (Exception e) {
				// showErrorMessage(e.getMessage() + "Abrir caixa para realização de vendas.");
				showErrorMessage("Abrir caixa para realização de vendas.");
				throw e;
			}
		}
		configuraLayout();

		if (editaVenda) {
			VendaController controller = getVendaController();
			atualizaDesconto = true;
			naoAtualizaDesconto = true;
			atualizaSubTotal(controller);
			atualizaSubTotalDesconto(controller);
			Pagamento pagamento = new CrudDao<Pagamento>().update(controller.getVenda().getPagamento());
			for (MeioPagamento mp : pagamento.getMeiosPagamento()) {
				TipoMeioPagamento tipoMeioPagamento = mp.getTipoMeioPagamento();
				//@formatter:off
				CheckBox checkBox = formaPagamentoMap.keySet()
						.stream()
						.filter(ck->ck.getText().equalsIgnoreCase(tipoMeioPagamento.getDescricaoTipoMeioPagamento()))
						.findFirst()
						.orElse(null);
				if(checkBox!=null) {
					checkBox.setSelected(true);
					habilitaComponentesPagamento(checkBox);
//					checkBox.setSelected(true);
//					List<Node> nodes = formaPagamentoMap.get(checkBox);
//					for(Node node:nodes) {
//						node.setDisable(false);
//						if(tipoMeioPagamento.getIdTipoMeioPagamento()==4 && node instanceof AutoCompleteTextField) {
//							((AutoCompleteTextField)node).getEditor().setText(mp.getDescricao());
//						}else if(node instanceof NumberTextField) {
//							
//							((NumberTextField)node).setText(Util.formataStringDecimais(mp.getValorPago()));
//						}
//					}
				}
//				
			}
			atualizaDesconto = false;
			naoAtualizaDesconto = false;

		}
	}
	
	private void closingAction(Event event) {
		Scene scene=getScene();
		try {
			scene.setCursor(Cursor.WAIT);
			if (!editaVenda)
				limpaFormularioPdv();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			scene.setCursor(Cursor.NONE);
		}

	}
	
	
	private GridPane criaGridPane() throws Exception {
		GridPane gridPane = new GridPane();
		// gridPane.setPadding(new Insets(0, 15, 5, 0));
		gridPane.setHgap(7);
		gridPane.setVgap(7);
		gridPane.add(criaTablePane(), 0, 0, 1, 3);
		gridPane.add(criaSubTotalPane(), 1, 0);
		gridPane.add(criaFormasPagtoPanel(), 1, 1);
		gridPane.add(criaTrocoPanel(), 1, 2);
		return gridPane;
	}

	private TitledPane criaTrocoPanel() {
		TitledPane titledPane = criaTitledPane("Sub-Total Recebido");
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		int rowIndex = 0;
		grid.add(criaLabelBold(TOTAL_RECEBIDO), 0, rowIndex);
		subTotalRecebido=new Label();
		subTotalRecebido.setFont(Font.font(FONT_VERDANA, FontWeight.NORMAL, 12));
		subTotalRecebido.setText(Util.formataMoeda(0f).substring(3));
		grid.add(subTotalRecebido, 1, rowIndex++);
		grid.add(criaLabelBold(TROCO_LABEL), 0, rowIndex);
		trocoLabel = new Label();
		trocoLabel.setFont(Font.font(FONT_VERDANA, FontWeight.NORMAL, 12));
		// trocoLabel = new TextField();
		// trocoLabel.setPrefColumnCount(8);
		String troco=Util.formataMoeda(0f).substring(3);
		trocoLabel.setText(troco);
		grid.add(trocoLabel, 1, rowIndex++);
		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		double width = getPercentageWidth();
		titledPane.setMaxWidth(width);
		return titledPane;
	}

	private TitledPane criaFormasPagtoPanel() throws Exception {
		TitledPane titledPane = criaTitledPane("Formas de Pagamento");
		// double width = getLayoutsMaxWidth() * 0.18629502790461694;
		// double width = getPercentageWidth();
		titledPane.setMaxWidth(getPercentageWidth(90));
		titledPane.setMinWidth(getPercentageWidth());
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setVgap(10);
		formaPagamentoMap = new HashMap<>();

		//
		// flow.getChildren().addAll(criaDinheiroGrid(), criaFormaPagamentoDebitoPane(),
		// criaFormaPagamentoCreditoPane(),
		// criaFormaPagamentoOutros());
		grid.add(criaDinheiroGrid(), 0, 0);
		grid.add(criaFormaPagamentoDebitoPane(), 0, 1);
		grid.add(criaFormaPagamentoCreditoPane(), 0, 2);
		grid.add(criaFormaPagamentoOutrosPane(), 0, 3);
		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		return titledPane;
	}

	private Node criaFormaPagamentoOutrosPane() throws Exception {
		GridPane grid = criaFormaPagamentoGridPane();
		CheckBox formapagamentoOutrosCheckBox = criaCheckBox("Outros");

		grid.add(formapagamentoOutrosCheckBox, 0, 0, 2, 1);
		grid.add(criaLabelNormal("Forma pagto:"), 0, 1);
		AutoCompleteTextField<String> descricaoPagamento = new AutoCompleteTextField<>();
		carregaTiposMeioPagamentoOutros(descricaoPagamento);
		descricaoPagamento.setDisable(true);
		descricaoPagamento.setMaxWidth(120);
		descricaoPagamento.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {

					VendaController controller = getVendaController();
					Pagamento pagamento = controller.getVenda().getPagamento();
					//@formatter:off
					MeioPagamento meioPagamento=pagamento.getMeiosPagamento()
							.stream()
							.filter(mp->mp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals(lastSelected.getText()))
							.findFirst()
							.orElse(null);
					//@formatter:on
					meioPagamento.setDescricao(descricaoPagamento.getEditor().getText());
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		});

		grid.add(descricaoPagamento, 1, 1);

		grid.add(criaLabelNormal(VALOR_R$), 0, 2);
		NumberTextField valor = criaCampoValor();
		formaPagamentoMap.put(formapagamentoOutrosCheckBox, Arrays.asList(valor, descricaoPagamento));
		grid.add(valor, 1, 2);
		return grid;
	}

	protected void carregaTiposMeioPagamentoOutros(AutoCompleteTextField<String> descricaoPagamento) throws Exception {
		CaixaController caixaController = getCaixaController();
		// ObservableList<String> items = descricaoPagamento.getItems();
		List outrosPagamentosList = caixaController.getTiposMeioPagamentoOutros();
		descricaoPagamento.setItems(FXCollections.observableArrayList(outrosPagamentosList));
	}

	private CheckBox criaCheckBox(String string) {
		CheckBox checkBox = new CheckBox(string);
		checkBox.setFont(Font.font(FONT_VERDANA, FontWeight.BOLD, 12));
		checkBox.setOnAction(action -> {
			habilitaComponentesPagamento((CheckBox) action.getSource());
		});
		return checkBox;
	}

	private void habilitaComponentesPagamento(CheckBox checkBox) {
		lastSelected = checkBox;
		List<Node> componentes = formaPagamentoMap.get(checkBox);

		try {
			VendaController controller = getVendaController();

			Pagamento pagamento = controller.getPagamentoVenda();
			if (pagamento == null)
				pagamento = new Pagamento();
			String descricaoTipoMeioPagamento = checkBox.getText();
			List<MeioPagamento> meiosPagamento = pagamento.getMeiosPagamento();
			// selecionando meio pagamento existente
			//@formatter:off
			MeioPagamento mp = meiosPagamento
					.stream()
					.filter(meioPagto -> meioPagto.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals(descricaoTipoMeioPagamento))
					.findFirst()
					.orElse(new MeioPagamento());
			//@formatter:on
			if (mp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento() == null)
				mp.getTipoMeioPagamento().setDescricaoTipoMeioPagamento(descricaoTipoMeioPagamento);
			int count = 0;
			for (Node node : componentes) {
				node.setDisable(!checkBox.isSelected());
				String valorString = "0,00";
				if (checkBox.isSelected()) {
					// valorString = null;
					if (mp.getTipoMeioPagamento().getIdTipoMeioPagamento() == 3 && count == 0) {
						valorString = Float.toString(mp.getParcelas());
						((TextField) node).setText(valorString);
					} else {
						// valorString = mp.getValorPago() == 0 ? "0,00"
						// : Util.formataStringDecimaisVirgula(mp.getValorPago());

						if (valorString.equals("0,00")
								&& (controller.getVenda() != null && controller.getVenda().getTotalVendido() > 0)) {
							// valorString =
							// Util.formataStringDecimaisVirgula(controller.getVenda().getTotalVendido());
							valorString = subTotalTxt.getText();
						}
						float valor = Float.parseFloat(valorString.replace(',', '.'));

						mp.setValorPago(valor);

					}

				}
				if (node instanceof AutoCompleteTextField) {

					((AutoCompleteTextField) node).getEditor().setText(mp.getDescricao());

				} else {
					if (checkBox.getText().equals("Cartão de Crédito") && count == 0) {
						count++;
						continue;
					}
					((TextField) node).setText(valorString);
				}
				count++;
			}

			//

			if (checkBox.isSelected()) {
				if (!meiosPagamento.contains(mp))
					meiosPagamento.add(mp);
				// valorPago = mp.getValorPago() + pagamento.getValorPagamento();

			} else if (mp.getIdMeioPagamento() == 0) {
				meiosPagamento.remove(mp);
				// valorPago = pagamento.getValorPagamento() - mp.getValorPago();
			}
			// pagamento.setValorPagamento(valorPago);

			controller.getVenda().setPagamento(pagamento);
			calculaTroco();

			//

		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	private Node criaFormaPagamentoCreditoPane() {

		GridPane grid = criaFormaPagamentoGridPane();
		CheckBox formapagamentoCartaoCreditoCheckBox = criaCheckBox("Cartão de Crédito");
		grid.add(formapagamentoCartaoCreditoCheckBox, 0, 0, 2, 1);
		grid.add(criaLabelNormal("Parcelas:"), 0, 1);
		NumberTextField parcelasTxt = criaNumberTextField();
		parcelasTxt.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				try {
					VendaController controller = getVendaController();
					Pagamento pagamento = controller.getVenda().getPagamento();
					//@formatter:off
					MeioPagamento meioPagamento=pagamento.getMeiosPagamento()
							.stream()
							.filter(mp->mp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals(lastSelected.getText()))
							.findFirst()
							.orElse(null);
					//@formatter:on
					if (!parcelasTxt.getText().isEmpty()) {
						int parcelas = Integer.parseInt(parcelasTxt.getText());
						meioPagamento.setParcelas(parcelas);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		});
		grid.add(parcelasTxt, 1, 1);
		grid.add(criaLabelNormal(VALOR_R$), 0, 2);
		NumberTextField valor = criaCampoValor();
		formaPagamentoMap.put(formapagamentoCartaoCreditoCheckBox, Arrays.asList(parcelasTxt, valor));
		grid.add(valor, 1, 2);
		return grid;
	}

	private Node criaFormaPagamentoDebitoPane() {
		GridPane grid = criaFormaPagamentoGridPane();
		CheckBox formapagamentoCartaoDebitoCheckBox = criaCheckBox("Cartão de Débito");
		grid.add(formapagamentoCartaoDebitoCheckBox, 0, 0, 2, 1);
		grid.add(criaLabelNormal(VALOR_R$), 0, 1);
		NumberTextField valorDebito = criaCampoValor();
		formaPagamentoMap.put(formapagamentoCartaoDebitoCheckBox, Arrays.asList(valorDebito));
		grid.add(valorDebito, 1, 1);
		return grid;
	}

	private GridPane criaDinheiroGrid() {
		GridPane grid = criaFormaPagamentoGridPane();
		//
		CheckBox formaPagamentoDinheiroCheckBox = criaCheckBox("Dinheiro");

		grid.add(formaPagamentoDinheiroCheckBox, 0, 0, 2, 1);
		grid.add(criaLabelNormal(VALOR_R$), 0, 1);
		NumberTextField valorDinheiroTxt = criaCampoValor();

		formaPagamentoMap.put(formaPagamentoDinheiroCheckBox, Arrays.asList(valorDinheiroTxt));
		grid.add(valorDinheiroTxt, 1, 1);

		return grid;
	}

	private void updatePagamento(CheckBox x, List<Node> nodes, String newValue) throws Exception {

		if (lastSelected == x && x.isSelected()) {

			VendaController controller = getVendaController();
			Pagamento pagamento = controller.getVenda().getPagamento();
			MeioPagamento mp = pagamento.getMeiosPagamento().stream()
					.filter(meio -> meio.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals(x.getText()))
					.findFirst().get();

			// if (mp != null) {
			mp.setValorPago(Util.parseStringDecimalToFloat(newValue));
			//// pagamento.setValorPagamento(controller.getValorTotalPagamentoVenda());
			calculaTroco();
			
			// }
		}
	}

	private NumberTextField criaCampoValor() {
		NumberTextField field = criaNumberTextField(true);
		field.textProperty().addListener((observableList, oldValue, newValue) -> {

			try {

				Platform.runLater(() -> {
					try {
						//@formatter:off
						formaPagamentoMap
								.forEach((t, u) -> {
									try {
										updatePagamento(t, u, newValue);
									} catch (Exception e) {
										
										e.printStackTrace();
									}
								});
						//@formatter:on
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (Exception e) {

				e.printStackTrace();
			}
		});
		return field;
	}

	protected void calculaTroco() throws Exception {
		VendaController controller = getVendaController();
		Pagamento pagamento = controller.getVenda().getPagamento();
		if (pagamento != null) {
			//@formatter:off
			formaPagamentoMap
				.forEach((key,value)->{
					//@formatter:off

					MeioPagamento mp = pagamento.getMeiosPagamento()
							.stream()
							.filter(meio -> meio.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals(key.getText()))
							.findFirst()
							.orElse(null);
					
					float valorVenda = Util.parseStringDecimalToFloat(subTotalTxt.getText()); 
//							controller.getVenda().getTotalVendido();

					float troco = 0;
					if (mp != null) {
						float valorPago = controller.getValorTotalPagamentoVenda();
								
						troco=pagamento.getTrocoPagamento();
						troco = valorPago == 0f ?
								0f : 
									valorPago - valorVenda;
						//@formatter:on
							pagamento.setTrocoPagamento(troco);
						}

						//@formatter:on

					});
			//@formatter:on

		}
		if (pagamento != null) {
			trocoLabel.setText(Util.formataMoedaSemSimbolo(pagamento.getTrocoPagamento()));
			subTotalRecebido.setText(Util.formataMoedaSemSimbolo(controller.getValorTotalPagamentoVenda()));
		}
	}

	private NumberTextField criaNumberTextField(boolean b) {

		NumberTextField valorDinheiroTxt = new NumberTextField(true);
		valorDinheiroTxt.setPrefColumnCount(8);
		valorDinheiroTxt.setDisable(true);
		return valorDinheiroTxt;
	}

	protected NumberTextField criaNumberTextField() {
		NumberTextField valorDinheiroTxt = new NumberTextField();
		valorDinheiroTxt.setPrefColumnCount(8);
		valorDinheiroTxt.setDisable(true);
		return valorDinheiroTxt;
	}

	private GridPane criaFormaPagamentoGridPane() {

		return criaFormaPagamentoGridPane(2);
	}

	protected GridPane criaFormaPagamentoGridPane(int vGap) {
		GridPane grid = new GridPane();
		// grid.setAlignment(Pos.BASELINE_RIGHT);
		grid.setHgap(10);
		grid.setVgap(vGap);
		return grid;
	}

	private TitledPane criaSubTotalPane() {
		TitledPane titledPane = criaTitledPane("Sub-Total");
		double width = getPercentageWidth();
		titledPane.setMaxWidth(width);
		FlowPane flow = new FlowPane();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		// grid.setHgap(5);
		grid.setVgap(5);
		grid.add(criaLabelBold(VALOR_VENDA), 0, 0);
		valorVendaLabel = criaLabelNormal(Util.formataMoeda(0f));
		grid.add(valorVendaLabel, 1, 0);
		grid.add(criaLabelBold("Desconto (%):"), 0, 1);
		descontoTxt = new NumberTextField();
		descontoTxt.setText("0");
		descontoTxt.setPrefColumnCount(3);
		descontoTxt.textProperty().addListener((observableList, oldValue, newValue) -> {
			Platform.runLater(() -> {

				calculaDesconto(newValue);

			});
		});
		grid.add(descontoTxt, 1, 1);
		grid.add(criaLabelBold(SUB_TOTAL_VENDA), 0, 2);
		subTotalTxt = new NumberTextField(true);
		subTotalTxt.focusedProperty().addListener((a, b, newValue) -> {
			atualizaDesconto = newValue;
		});
		subTotalTxt.textProperty().addListener((observable, oldText, newText) -> {
			VendaController controller = null;
			try {
				controller = getVendaController();
				controller.getVenda().setTotalVendido(Util.parseStringDecimalToFloat(newText));
				ObservableList<ItemVendaModel> itensTable = table.getItems();
				if (!itensTable.isEmpty()) {
					if (newText.equals("")) {
						newText = Util.formataStringDecimaisVirgula(controller.getValorBrutoVenda());
						subTotalTxt.setText(newText);
					}
				}
				float valorVendido = controller.getVenda().getTotalVendido();

				float descontoConcedido = controller.getValorBrutoVenda() - valorVendido;
				float porcentagemDesconto = (descontoConcedido / controller.getValorBrutoVenda()) * 100;
				if (!naoAtualizaDesconto)
					controller.getVenda().setPorcentagemDesconto(porcentagemDesconto);
				itensTable.stream().forEach(item -> {
					Float valorTotal = Util.parseStringDecimalToFloat(item.getValorTotal().substring(3));
					Float valorProduto = Util.parseStringDecimalToFloat(item.getValorProduto().substring(3));
					Float valorVenda = Util.parseStringDecimalToFloat(item.getValorVenda().substring(3));
					Float desconto = Util.parseStringDecimalToFloat(descontoTxt.getText());
					valorVenda = valorProduto - (valorProduto * desconto / 100);
					valorTotal = valorTotal - (valorTotal * desconto / 100);
					item.setValorVenda(Util.formataMoeda(valorVenda));
					item.setValorTotal(Util.formataMoeda(valorTotal));
				});
				table.refresh();
				calculaTroco();
				if (atualizaDesconto) {
					atualizaSubTotalDesconto(controller);

				}
			} catch (Exception e) {
				if (!naoAtualizaDesconto)
					controller.getVenda().setPorcentagemDesconto(0);
				atualizaSubTotalDesconto(controller);
				e.printStackTrace();
			}
		});
		subTotalTxt.setPrefColumnCount(8);
		subTotalTxt.setText(Util.formataStringDecimais(0f));
		grid.add(subTotalTxt, 1, 2);

		flow.getChildren().add(grid);
		titledPane.setContent(flow);
		return titledPane;
	}

	protected void calculaDesconto(String valorDesconto) {
		if (editaVenda)
			return;
		VendaController controller;
		try {
			float desconto = Float.parseFloat(!valorDesconto.isEmpty() ? valorDesconto.replace(',', '.') : "0");
			controller = getVendaController();
			controller.getVenda().setPorcentagemDesconto(desconto);
			controller.calculaValorTotalVenda();
			if (!atualizaDesconto)
				atualizaSubTotal(controller);
			carregaDadosTable(table);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	protected void atualizaSubTotalDesconto(VendaController controller) {

		int porcentagemDesconto = (int) controller.getVenda().getPorcentagemDesconto();
		descontoTxt.setText(Integer.toString(porcentagemDesconto));

	}

	private void calculaTroco(float valorPagamento) throws Exception {
		String valorRecebido = Util.formataStringDecimais(valorPagamento);
		// calculaTroco(valorRecebido);

	}

	protected double getPercentageWidth() {
		double width = getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (79d / 100d));
		;
		return width;
	}

	protected double getPercentageWidth(double percentage) {
		return getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (percentage / 100d));
	}

	private TitledPane criaTablePane() throws Exception {
		TitledPane titledPane = criaTitledPane("Ítens da Venda");
		// criaTablePane(title)
		titledPane.setMaxHeight(height);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		pane.setHgap(25);
		pane.setAlignment(Pos.CENTER);
		setLabelColunas(COLUNAS_TABLEVIEW);
		pane.setVgap(10);
		// double width = getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (1.5d / 100d));
		double width = getLayoutsMaxWidth() - (getLayoutsMaxWidth() * (24d / 100d));
		table = criaTableView(width);
		// table.setMaxWidth();
		titledPane.setMaxHeight(height);
		ObservableList<Node> children = pane.getChildren();
		children.add(table);
		if (novaVenda) {

			Button buttonLimpar = new Button("Cancelar");
			buttonLimpar.setFont(Font.font(FONT_VERDANA, FontWeight.BOLD, 12));
			buttonLimpar.setOnAction(evt -> {
				try {
					if (showConfirmAtionMessage("Deseja cancelar esta venda?")) {
						
						limpaFormularioPdv();
						getVendaController().resetVenda();
						
					}

				} catch (Exception e) {
					showErrorMessage("Falha ao limpar formulário.");
					e.printStackTrace();
				}
			});
			Button button = new Button("Finalizar Venda");

			button.setFont(Font.font(FONT_VERDANA, FontWeight.BOLD, 12));
			button.setOnAction(evt -> {
				try {
					boolean confirmaFinalizacao = showConfirmAtionMessage("Deseja finalizar esta Venda?");
					if (confirmaFinalizacao) {
						finalizarVenda();
						showMensagemSucesso("Venda finalizada com sucesso!");
					}
				} catch (Exception e) {
					showErrorMessage(e.getMessage());
					e.printStackTrace();
				} finally {

				}
			});
			children.addAll(button, buttonLimpar);
		} else {
			Button button = new Button("Estornar Venda");
			button.setFont(Font.font(FONT_VERDANA, FontWeight.BOLD, 12));

			button.setOnAction(evt -> {
				try {
					if (showConfirmAtionMessage("Deseja estornar esta Venda?")) {
						VendaController controller = getVendaController();
						controller.estornaVenda();
						// limpaFormularioPdv();
						showMensagemSucesso("Venda estornada com sucesso.");
						close();
					}

				} catch (Exception e) {
					showErrorMessage(e.getMessage());
					e.printStackTrace();
				}
			});
			VendaController vendaController = getVendaController();
			if (vendaController.getVenda().getCaixa().getUsuarioFechamento() == null)
				children.add(button);

		}
		titledPane.setContent(pane);
		return titledPane;
	}

	protected void finalizarVenda() throws Exception {

		VendaController controller = getVendaController();
		controller.getVenda().setTotalVendido(Util.parseStringDecimalToFloat(subTotalTxt.getText()));
		controller.finalizaVenda();
		carregaDadosTable(table);
		limpaFormularioPdv();
	}

	private void limpaFormularioPdv() throws Exception {

		limpaDadosProduto();
		limpaFormasPagamentoFields();

		subTotalTxt.setText("0,00");
		descontoTxt.setText("0");
		trocoLabel.setText(Util.formataMoedaSemSimbolo(0));
		subTotalRecebido.setText(Util.formataMoedaSemSimbolo(0));
		estoqueDropDown.getEditor().setText("");
		valorVendaLabel.setText("");
		limpaCarrinho();
	}

	private void limpaCarrinho() throws Exception {
		ObservableList<ItemVendaModel> items = table.getItems();
		List<String> codigosProduto = new ArrayList<>();
		for (ItemVendaModel item : items) {
			codigosProduto.add(item.getCodigoProduto());

		}
		codigosProduto.stream().forEach(codigoProduto -> {
			selectedCodigoBarra = codigoProduto;
			try {
				removeCarrinho();
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		});
	}

	private void removeCarrinho(String codigoProduto) throws Exception {
		VendaController controller = getVendaController();
		controller.getItemVenda(codigoProduto);
		removeCarrinho();

	}

	/**
	 * @throws Exception
	 * 
	 */
	protected void limpaFormasPagamentoFields() throws Exception {
		for (CheckBox checkBox : formaPagamentoMap.keySet()) {
			checkBox.setSelected(false);
			List<Node> componentes = formaPagamentoMap.get(checkBox);
			for (Node node : componentes) {
				node.setDisable(!checkBox.isSelected());
				if (!checkBox.isSelected()) {
					if (node instanceof NumberTextField) {
						NumberTextField valor = (NumberTextField) node;
						StringBuilder valorZero = new StringBuilder("0");
						if (valor.isAcceptComma())
							valorZero.append(",00");
						valor.setText(valorZero.toString());

					} else if (node instanceof AutoCompleteTextField) {
						AutoCompleteTextField<String> descricaoMeioPagamento = (AutoCompleteTextField<String>) node;
						carregaTiposMeioPagamentoOutros(descricaoMeioPagamento);
						descricaoMeioPagamento.getEditor().setText("");
					}

				}
			}

		}
	}

	private TitledPane criaProdutoPane() {
		FlowPane produtoFlowPane = createHorizontalFlowPane();
		produtoFlowPane.setAlignment(Pos.CENTER);
		TitledPane pane = criaTitledPane("Produto");
		produtoFlowPane.setHgap(10);
		double width = getPercentageWidth(0.5);
		pane.setMaxWidth(width);
		pane.setMinWidth(width);
		pane.setCollapsible(false);
		pane.setContent(produtoFlowPane);
		adicionaProdutoItems(produtoFlowPane);

		return pane;
	}

	protected void adicionaProdutoItems(FlowPane produtoFlowPane) {

		fabricanteLabel = new Label();
		ObservableList<Node> children = produtoFlowPane.getChildren();
		subCategoriaLabel = new Label();
		produtoLabel = new Label();
		valorUnitLabel = new Label();
		quantidadeTxt = new NumberTextField();
		quantidadeTxt.setText("1");
		quantidadeTxt.setPrefColumnCount(4);
		quantidadeTxt.setVisible(false);
		if (dadosProdutos == null)
			dadosProdutos = new ArrayList<>();
		dadosProdutos.add(quantidadeTxt);
		nodesDadosProdutos = new ArrayList<>();
		nodesDadosProdutos.add(quantidadeTxt);
		marcaLabel = criaLabelBold("Marca:");
		marcaLabel.setVisible(false);
		children.add(marcaLabel);
		nodesDadosProdutos.add(marcaLabel);
		children.add(fabricanteLabel);
		categoriaLbl = criaLabelBold("Categoria:");
		categoriaLbl.setVisible(false);
		children.add(categoriaLbl);
		nodesDadosProdutos.add(categoriaLbl);
		children.add(subCategoriaLabel);
		produtoLbl = criaLabelBold("Produto:");
		produtoLbl.setVisible(false);
		children.add(produtoLbl);
		nodesDadosProdutos.add(produtoLbl);
		children.add(produtoLabel);
		valorUnitLbl = criaLabelBold("Valor Unit.:");
		valorUnitLbl.setVisible(false);
		children.add(valorUnitLbl);
		nodesDadosProdutos.add(valorUnitLbl);
		children.add(valorUnitLabel);
		qtLabel = criaLabelBold("Quantidade:");
		qtLabel.setVisible(false);
		nodesDadosProdutos.add(qtLabel);
		children.add(qtLabel);
		children.add(quantidadeTxt);
		adicionarBtn = new Button("Adicionar");
		adicionarBtn.setOnAction(arg0 -> {
			try {
				adicionaCarrinho(arg0);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		});
		adicionarBtn.setVisible(false);
		children.add(adicionarBtn);
		removerBtn = new Button("Remover");
		removerBtn.setOnAction(arg0 -> {
			try {
				removeCarrinho();

			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
			}
		});
		removerBtn.setVisible(false);
		removerBtn.setDisable(true);
		children.add(removerBtn);
		// novoBtn=
		dadosProdutos.add(fabricanteLabel);
		dadosProdutos.add(subCategoriaLabel);
		dadosProdutos.add(valorUnitLabel);
		dadosProdutos.add(produtoLabel);
		nodesDadosProdutos.add(adicionarBtn);
		nodesDadosProdutos.add(removerBtn);
	}

	private TitledPane criaPesquisaPane() {
		FlowPane flowPane = createHorizontalFlowPane();
		flowPane.setAlignment(Pos.CENTER);
		flowPane.setHgap(20);
		TitledPane pane = criaTitledPane("Pesquisa Estoque");
		double width = getPercentageWidth(0.5);
		pane.setMaxWidth(width);

		pane.setMinWidth(width);
		pane.setCollapsible(false);

		try {
			estoqueDropDown = criaEstoqueProdutoDropDown();
			estoqueDropDown.setOnAction(arg0 -> {
				try {
					populaProduto(arg0);
				} catch (Exception e) {

					e.printStackTrace();
				}
			});
			radioCodigoBarra = new RadioButton("Código de Barras");
			radioCodigoBarra.setSelected(true);
			ToggleGroup group = new ToggleGroup();
			radioCodigoBarra.setToggleGroup(group);
			radioDescricaoProduto = new RadioButton("Descrição de Produto");
			radioDescricaoProduto.setToggleGroup(group);
			flowPane.getChildren().add(estoqueDropDown);
			flowPane.getChildren().add(radioCodigoBarra);
			flowPane.getChildren().add(radioDescricaoProduto);
		} catch (Exception e) {

			e.printStackTrace();
		}

		pane.setContent(flowPane);
		return pane;
	}

	private void removeCarrinho() throws Exception {

		VendaController vendaController = getVendaController();
		int quantidade = Integer.parseInt(quantidadeTxt.getText());
		// vendaController.populaItemEstoque(itemEstoque, quantidade);
		if (selectedCodigoBarra == null)
			throw new NullPointerException("Código de Produto não selecionado.");
		vendaController.devolveProduto(selectedCodigoBarra, quantidade);
		// subTotalTxt.setText(Util.formataMoeda(vendaController.getSubTotal()).substring(3));
		atualizaSubTotal(vendaController);
		carregaDadosTable(table);
		limpaDadosProduto();

	}

	protected void limpaDadosProduto() {
		exibeEscondeProdutosNodes(false);
		estoqueDropDown.getEditor().setText("");
		dadosProdutos.stream().forEach(node -> {

			if (node instanceof Label)
				((Label) node).setText("");
			else
				((TextField) node).setText("1");
		});
		removerBtn.setDisable(true);
	}

	private void adicionaCarrinho(ActionEvent event) throws Exception {
		try {
			int quantidade = Integer.parseInt(quantidadeTxt.getText());
			VendaController controller = super.getVendaController();
			// ItemVenda itemVenda = new ItemVenda();
			// itemVenda.setQuantidade(quantidade);
			// itemVenda.setItemEstoque(itemEstoque);
			// controller.setItemVenda(itemVenda);
			// itemEstoque.setQuantidade(quantidade);
			controller.populaItemEstoque(itemEstoque, quantidade);
			// TODO: APAGAR ESTE MÉTODO
			// controller.adicionaProdutoLista();

			atualizaSubTotal(controller);
			calculaTroco();
			limpaDadosProduto();
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		} finally {
			carregaDadosTable(table);

		}

	}

	private void atualizaSubTotal(VendaController controller, boolean atualizaDesconto) {
		if (atualizaDesconto)
			atualizaSubTotalDesconto(controller);
		atualizaSubTotal(controller);

	}

	protected void atualizaSubTotal(VendaController controller) {
		float subTotal = controller.getVenda().getTotalVendido();
		subTotalTxt.setText(Util.formataStringDecimaisVirgula(subTotal));

		float valorBrutoVenda = controller.getValorBrutoVenda();
		valorVendaLabel.setText(Util.formataMoeda(valorBrutoVenda));
	}

	@Override
	protected void handleTableClick(Event event) {
		try {
			selectedCodigoBarra = getTableViewSelectedValueId(event);

			ItemVenda itemVenda = getVendaController().getItemVenda(selectedCodigoBarra);
			itemEstoque = itemVenda.getItemEstoque();
			populaDadosProduto(itemEstoque);
			removerBtn.setDisable(false);
			quantidadeTxt.setText(itemVenda.getQuantidade().toString());
			boolean permiteVendaSemControleEstoque = SingletonManager.getInstance().isPermiteVendaSemControlarEstoque();
			boolean estoqueInsuficienteVenda = itemEstoque.getQuantidade() < itemVenda.getQuantidade();
			if (!permiteVendaSemControleEstoque) {
				adicionarBtn.setDisable(estoqueInsuficienteVenda);
			}
			estoqueDropDown.getEditor().setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// populaDadosProduto();
	}

	private void populaProduto(ActionEvent event) throws Exception {
		// removerBtn.setDisable(true);
		Object estoqueValue = estoqueDropDown.getValue();
		boolean ehItemEstoque = estoqueValue != null && estoqueValue instanceof ItemEstoque;

		if (ehItemEstoque && radioDescricaoProduto.isSelected()) {
			VendaController vendaController = getVendaController();
			itemEstoque = (ItemEstoque) estoqueValue;

			boolean temEstoqueSuficiente = itemEstoque.getQuantidade() > 0;
			populaDadosProduto(itemEstoque);
			if (!SingletonManager.getInstance().isPermiteVendaSemControlarEstoque())
				adicionarBtn.setDisable(!temEstoqueSuficiente);
			else
				adicionarBtn.setDisable(false);

			boolean desatibilitaRemover = vendaController.getItemVenda(itemEstoque.getCodigoDeBarras()) == null;
			removerBtn.setDisable(desatibilitaRemover);
		} else if (estoqueValue != null && estoqueValue instanceof String && radioCodigoBarra.isSelected()) {
			populaDadosProduto((String) estoqueValue);
			adicionarBtn.setDisable(false);
		}
		// else if (estoqueValue != null) {
		// System.out.println(estoqueValue);
		// }
	}

	private void populaDadosProduto(String codigoDeBarras) {
		try {
			EstoqueController controller = getEstoqueController();
			controller.busca(codigoDeBarras);
			populaDadosProduto((ItemEstoque) controller.getItem());
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
			e.printStackTrace();
		}

	}

	protected void populaDadosProduto(ItemEstoque estoqueValue) {
		itemEstoque = estoqueValue;
		Produto produto = itemEstoque.getProduto();
		Fabricante fabricante = itemEstoque.getFabricante();
		SubTipoProduto subCategoria = itemEstoque.getTipoProduto();
		String valorUnitario = Util.formataMoeda(itemEstoque.getValorUnitario());
		fabricanteLabel.setText(fabricante.getNome());
		subCategoriaLabel.setText(subCategoria.getDescricaoTipo());
		valorUnitLabel.setText(valorUnitario);
		produtoLabel.setText(produto.getDescricaoProduto());
		exibeEscondeProdutosNodes(true);
		selectedCodigoBarra = itemEstoque.getCodigoDeBarras();
	}

	private void exibeEscondeProdutosNodes(boolean showNode) {
		nodesDadosProdutos.stream().forEach(node -> {
			node.setVisible(showNode);

		});

	}

	private AutoCompleteTextField<ItemEstoque> criaEstoqueProdutoDropDown() {
		AutoCompleteTextField<ItemEstoque> lista = new AutoCompleteTextField<>();
		lista.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		try {
			EstoqueController controller = getEstoqueController();
			controller.buscaTodos();
			ObservableList<ItemEstoque> items = lista.getItems();
			if (controller.getList() == null || controller.getList().isEmpty())
				controller.buscaTodos();
			// items.add(new I("Selecione uma opção.", null));
			items.addAll(controller.getList());
		} catch (Exception e) {

			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	void reloadForm() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		ObservableList<ItemVendaModel> items = table.getItems();
		items.removeAll(items);
		VendaController vendaController = getVendaController();
		vendaController.getList().stream().forEach(itemVenda -> {
			ItemVenda item = (ItemVenda) itemVenda;

			ItemVendaModel itemVendaModel = new ItemVendaModel(item);

			items.add(itemVendaModel);
		});
		// items.addAll(vendaController.getList());
		table.refresh();

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

}
