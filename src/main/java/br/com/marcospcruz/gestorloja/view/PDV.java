package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.abstractfactory.MeioPagamentoFactory;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoCartaoCredito;
import br.com.marcospcruz.gestorloja.model.MeioPagamentoOutros;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.components.AutocompleteJComboBox;
import br.com.marcospcruz.gestorloja.view.components.MyJCheckBox;

public class PDV extends AbstractDialog {
	private static final String VALOR = "Valor: ";
	private static final Object[] COLUNAS_JTABLE = { "Código Produto", "Marca / Fabricante", "Produto", "Quantidade",
			"Valor Produto", "Valor Venda", "Valor Total" };
	private static final String UM = "1";
	private static final int DEFAULT_GAP = 450;
	private static final int PRODUTO_HORIZONTAL_DEFAULT_GAP = 750;
	// private Java2sAutoComboBox autoCompleteComboBox;
	private VendaController vendaController;
	AutocompleteJComboBox<ItemEstoque> autoCompleteComboBox;
	private JFormattedTextField txtQuantidade;
	private List<MyJCheckBox> meiosPagamentosSelecionadosList;
	// private AutoCompleteComboBox<String> autoCompleteComboBox;

	private JLabel lblDescricaoCategoria;
	private JLabel lblDescricaoProduto;
	private JLabel lblNomeFabricante;
	private JLabel lblValorUnitario;
	private JLabel lblValorTotal;
	private JLabel lblSaldoEstoque;
	private JFormattedTextField txtTotalVenda;
	private JFormattedTextField txtDesconto;

	private JFormattedTextField txtValorPagtoDinheiro;
	private JLabel lblValorTroco;
	private JFormattedTextField txtValorPagtoCredito;
	private JFormattedTextField txtValorPagtoDebito;
	private JFormattedTextField txtQtParcelaCredito;
	private JFormattedTextField txtValorPagtoOutros;
	private JTextField txtDescricaoPagtoOutros;
	private MyJCheckBox outrosCheckBox;
	private MyJCheckBox dinheiroCheckBox;
	private MyJCheckBox debitoCheckBox;
	private MyJCheckBox creditoCheckBox;
	private Map<String, List<Component>> pagamentoComponentsMap;

	public PDV(String tituloJanela, JFrame owner) throws Exception {
		super(owner, tituloJanela, ControllerAbstractFactory.CONTROLE_VENDA, true);
		meiosPagamentosSelecionadosList = new ArrayList<>();
		vendaController = super.getVendaController();
		vendaController.resetVenda();
		getContentPane().setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel(new GridLayout());

		getContentPane().add(createHeaderPanel(), BorderLayout.NORTH);
		createListaCompraPanel();
		centerPanel.add(jPanelTable);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		// getContentPane().add(jPanelTable, BorderLayout.SOUTH);
		getContentPane().add(criaEastPanel(), BorderLayout.EAST);
		// getContentPane().add(finalizaCompraPanel(), BorderLayout.EAST);
		setVisible(true);
	}

	private Component criaEastPanel() {
		LayoutManager layout = new GridLayout(1, 1);
		JPanel panel = new JPanel();
		// panel.add(criaSubTotalDescontoPanel());
		// panel.add(criaFormaPagamentoPanel());
		// panel.add(criaFormaPgtoTrocoPanel("Troco"));
		// panel.add(criaBtnPanel());
		panel.add(centerPanel());
		return panel;
	}

	private Component criaSubTotalDescontoPanel() {
		// JPanel panel = new JPanel(new GridLayout(2, 2));
		// LayoutManager layout = new FlowLayout(FlowLayout.CENTER);
		JPanel principal = new JPanel();
		LayoutManager layout = new GridLayout(1, 2);
		JPanel panel = new JPanel(layout);

		panel.setBorder(super.criaTitledBorder("Sub-Total"));

		JLabel descontoLabel = super.criaJLabel("Desconto %: ", 25);

		// labelPanel.add(descontoLabel);
		txtDesconto = inicializaDecimalNumberField();
		// txtDesconto.setPreferredSize(new Dimension(100, 100));
		txtDesconto.setFont(FontMapper.getFont(25));
		// fieldPanel.add(descontoJLabel);
		txtDesconto.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				calculaPorcentagemDesconto(e.getSource());
				populaFormulario();
				carregaTableModel();
				if (txtTotalVenda != null)
					txtTotalVenda.setText(Util.formataMoeda(vendaController.getVenda().getTotalVendido()).substring(3));

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JLabel vlTotalLabel = super.criaJLabel("Valor Total R$:", 25);
		txtTotalVenda = inicializaDecimalNumberField();
		txtTotalVenda.setFont(FontMapper.getFont(25));
		txtTotalVenda.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				txtTotalVenda.selectAll();

			}
		});
		txtTotalVenda.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// System.out.println(e.getKeyCode() + " = " + e.getKeyChar());
				if (!(e.getKeyCode() >= 37 && e.getKeyCode() <= 40)) {
					// && e.getKeyCode() != 8
					calculaPorcentagemDesconto(e.getSource());
					populaFormulario();
					carregaTableModel();
					if (txtDesconto != null)
						txtDesconto.setText(Float.toString(vendaController.getVenda().getPorcentagemDesconto()));

				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JPanel pDescontoLabel = new JPanel(new BorderLayout());

		pDescontoLabel.add(descontoLabel, BorderLayout.WEST);

		// pDesconto, BorderLayout.CENTER);

		JPanel pCol1 = new JPanel(new GridLayout(2, 1));
		JPanel pCol2 = new JPanel(new GridLayout(2, 1));
		JPanel pVlTotalLabel = new JPanel(new BorderLayout());
		// txtTotalVenda.setPreferredSize(new Dimension(100, 100));
		// vlTotalLabel.setBorder(BorderFactory.createEtchedBorder());
		pVlTotalLabel.add(vlTotalLabel, BorderLayout.WEST);
		// pVlTotal.add(createDefaultTxtPanel(txtTotalVenda), BorderLayout.CENTER);

		// p.add(pDesconto);
		pCol2.add(createDefaultTxtPanel(txtDesconto));
		pCol2.add(createDefaultTxtPanel(txtTotalVenda));
		pCol1.add(pDescontoLabel);
		pCol1.add(pVlTotalLabel);
		panel.add(pCol1);
		panel.add(pCol2);
		principal.add(panel, BorderLayout.WEST);
		// principal.add(centerPanel(), BorderLayout.CENTER);
		// principal.add(panel3, BorderLayout.EAST);
		return principal;
	}

	private JPanel criaBtnPanel() {
		JPanel panel3 = new JPanel(new GridBagLayout());
//		panel3.setBorder(BorderFactory.createEtchedBorder());
		// panel3.setBackground(Color.CYAN);

		JButton btnFinalizar = inicializaJButton("Finalizar Venda");
		panel3.add(btnFinalizar);
		return panel3;
	}

	private Component centerPanel() {
		LayoutManager layout = new BorderLayout(150, 0);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(criaSubTotalDescontoPanel(), BorderLayout.NORTH);
		panel.add(criaFormaPagamentoPanel(), BorderLayout.CENTER);
		JPanel jp=new JPanel(new GridLayout(2, 1));
		panel.add(jp, BorderLayout.SOUTH);
		jp.add(criaFormaPgtoTrocoPanel("Troco"));
		jp.add(criaBtnPanel());
		return panel;
	}

	private Component criaFormaPagamentoPanel() {
		JPanel[] checkPanel = new JPanel[4];
		LayoutManager gridLayout = new GridLayout(checkPanel.length, 1, 10, 0);
		JPanel panel = new JPanel(new BorderLayout());
		JPanel formaPagamentoPnl = new JPanel(gridLayout);
		formaPagamentoPnl.setBorder(criaTitledBorder("Forma de Pagamento"));
		pagamentoComponentsMap = new HashMap<>();
		int i = 0;
		// JLabel lblDinheiro = criaJLabel("Dinheiro:");
		checkPanel[i++] = criaFormaPgtoDinheiroPanel("Dinheiro");
		checkPanel[i++] = criaFormaPgtoCartDebitoPanel("Cartão de Débito");
		checkPanel[i++] = criaFormaPgtoCartCreditoPanel("Cartão de Crédito");
		checkPanel[i] = criaFormaPgtoCartOutrosPanel("Outros");

		// JLabel lblCartaoDebito = criaJLabel("Cartão Débito:");
		// JLabel lblCartaoCredito = criaJLabel("Cartão de Crédito:");
		// JLabel lblVoucher = criaJLabel("Cartão de Crédito:");
		// JLabel lblCheque = criaJLabel("Cheque:");
		for (Component comp : checkPanel) {
			formaPagamentoPnl.add(comp);
		}
		for (List<Component> c : pagamentoComponentsMap.values()) {
			c.stream().forEach(component -> {
				component.setEnabled(false);
			});
		}
		// panel.add(lblCartaoDebito);
		// panel.add(lblCartaoCredito);
		// panel.add(lblVoucher);
		// panel.add(lblCheque);
		panel.add(formaPagamentoPnl, BorderLayout.CENTER);
		LayoutManager layoutPnl = new GridLayout(2, 1);
		JPanel pnl = new JPanel();

		panel.add(pnl, BorderLayout.EAST);
		return panel;
	}

	private JPanel criaFormaPgtoCartOutrosPanel(String string) {
		outrosCheckBox = criaJCheckBox(string);
		txtValorPagtoOutros = inicializaDecimalNumberField();
		txtValorPagtoOutros.addKeyListener(calculaPagtoListener());
		txtDescricaoPagtoOutros = inicializaAlfaNumericTextField();

		JPanel outrosPnl = criaDefaultFormaPagto(string, outrosCheckBox, txtDescricaoPagtoOutros, txtValorPagtoOutros,
				"Descrição: ", VALOR);
		outrosPnl.setBorder(BorderFactory.createEtchedBorder());

		pagamentoComponentsMap.put(string, Arrays.asList(txtDescricaoPagtoOutros, txtValorPagtoOutros));

		return outrosPnl;

	}

	private JPanel criaFormaPgtoCartCreditoPanel(String string) {
		creditoCheckBox = criaJCheckBox(string);
		txtQtParcelaCredito = inicializaNumberField();
		txtValorPagtoCredito = inicializaDecimalNumberField();
		txtValorPagtoCredito.addKeyListener(calculaPagtoListener());
		String qtParcelasLabelString = "Qt. Parcelas";
		JPanel cartCreditoPnl = criaDefaultFormaPagto(string, creditoCheckBox, txtQtParcelaCredito,
				txtValorPagtoCredito, qtParcelasLabelString, VALOR);

		pagamentoComponentsMap.put(string, Arrays.asList(txtQtParcelaCredito, txtValorPagtoCredito));
		return cartCreditoPnl;
	}

	private JPanel criaDefaultFormaPagto(String string, JCheckBox checkBox, JTextComponent txtField1,
			JTextComponent txtField2, String stringLabel1, String stringLabel2) {
		// LayoutManager layout = new GridLayout(1, 1, 10, 0);
		JPanel defaultPnl = new JPanel(new BorderLayout());
		defaultPnl.setBorder(BorderFactory.createEtchedBorder());

		LayoutManager valoresPanelGridLayout = new GridLayout(1, 5, 10, 0);
		JPanel valoresPanel = new JPanel(new BorderLayout());
		JPanel p = new JPanel(new BorderLayout());
		p.add(super.criaJLabel(stringLabel1), BorderLayout.WEST);

		p.add(createDefaultTxtPanel(txtField1), BorderLayout.EAST);
		valoresPanel.add(p, BorderLayout.NORTH);
		if (Util.isNotNull(stringLabel2, txtField2)) {
			JPanel p2 = new JPanel(new BorderLayout());
			p2.add(super.criaJLabel(stringLabel2), BorderLayout.WEST);

			p2.add(createDefaultTxtPanel(txtField2), BorderLayout.EAST);
			valoresPanel.add(p2);
		}

		defaultPnl.add(checkBox, BorderLayout.NORTH);
		defaultPnl.add(valoresPanel, BorderLayout.SOUTH);
		return defaultPnl;
	}

	private JPanel criaFormaPgtoCartDebitoPanel(String string) {

		JPanel cartaoDebitoPnl = new JPanel(new BorderLayout());
		cartaoDebitoPnl.setBorder(BorderFactory.createEtchedBorder());
		debitoCheckBox = criaJCheckBox(string);
		txtValorPagtoDebito = inicializaDecimalNumberField();
		txtValorPagtoDebito.addKeyListener(calculaPagtoListener());
		JPanel valoresPanel = new JPanel(new BorderLayout(10, 10));
		valoresPanel.add(super.criaJLabel(VALOR), BorderLayout.WEST);
		valoresPanel.add(createDefaultTxtPanel(txtValorPagtoDebito), BorderLayout.CENTER);
		cartaoDebitoPnl.add(debitoCheckBox, BorderLayout.NORTH);
		cartaoDebitoPnl.add(valoresPanel, BorderLayout.SOUTH);

		pagamentoComponentsMap.put(debitoCheckBox.getText(), Arrays.asList(txtValorPagtoDebito));
		return cartaoDebitoPnl;
	}

	private JPanel criaFormaPgtoTrocoPanel(String string) {
		LayoutManager layout = new GridLayout(1, 1, 0, 30);
		JPanel panel = new JPanel();
		panel.setBorder(criaTitledBorder(string));
		JPanel p = new JPanel(new FlowLayout());
		JLabel label = super.criaJLabel(VALOR, 25);

		lblValorTroco = super.criaJLabel(Util.formataMoeda(0f), 25);
		p.add(label);
		p.add(lblValorTroco);

		panel.add(p);
		return panel;
	}

	private JPanel criaFormaPgtoDinheiroPanel(String string) {
		dinheiroCheckBox = criaJCheckBox(string);
		dinheiroCheckBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (!dinheiroCheckBox.isSelected()) {
					txtValorPagtoDinheiro.setText("");
					lblValorTroco.setText(Util.formataMoeda(0f));
				}

			}
		});
		txtValorPagtoDinheiro = inicializaDecimalNumberField();

		txtValorPagtoDinheiro.addKeyListener(calculaPagtoListener());
		JPanel dinheiroPnl = criaDefaultFormaPagto(string, dinheiroCheckBox, txtValorPagtoDinheiro, null, VALOR, null);

		pagamentoComponentsMap.put(string, Arrays.asList(txtValorPagtoDinheiro));
		return dinheiroPnl;
	}

	private Component createDefaultTxtPanel(JTextComponent txtField) {
		JPanel panel = new JPanel();
		panel.add(txtField);
		return panel;
	}

	private KeyListener calculaPagtoListener() {

		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {

				calculaTroco();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
	}

	private MyJCheckBox criaJCheckBox(String string) {
		MyJCheckBox checkBox = new MyJCheckBox(string);
		checkBox.setFont(FontMapper.getFont(22));
		checkBox.addActionListener(criaJCheckBoxActionListener());
		return checkBox;
	}

	private ActionListener criaJCheckBoxActionListener() {
		// TODO Auto-generated method stub
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MyJCheckBox jCheckBox = (MyJCheckBox) e.getSource();
				if (jCheckBox.isSelected()) {
					meiosPagamentosSelecionadosList.add(jCheckBox);
				} else
					meiosPagamentosSelecionadosList.remove(jCheckBox);
				List<Component> componentes = pagamentoComponentsMap.get(e.getActionCommand());
				componentes.stream().forEach(componente -> {
					componente.setEnabled(jCheckBox.isSelected());
					if (!jCheckBox.isSelected() && componente instanceof JTextComponent) {
						((JTextComponent) componente).setText("");
					}

				});
				calculaTroco();
			}
		};
	}

	private void calculaPorcentagemDesconto(Object object) {
		float porcentagem = 0;
		String valor = object == null ? "0" : ((JTextComponent) object).getText();
		float valorTotalLista = vendaController.getSubTotal();
		if (object == txtDesconto) {

			// porcentagem = valor.isEmpty() ? 0 : Integer.parseInt(valor);
			porcentagem = valor.isEmpty() ? 0 : Float.parseFloat(valor);
		} else {
			// porcentagem = vendaController.getVenda().getPorcentagemDesconto();
			float valorTotal = valor.isEmpty() ? 0f : Float.parseFloat(valor);
			float diferenca = valorTotalLista - valorTotal;
			porcentagem = (diferenca / valorTotalLista) * 100;

		}

		vendaController.getVenda().setPorcentagemDesconto(porcentagem);
		// vendaController.getVenda().setTotalVendido(novoValorVenda);
		vendaController.calculaDescontoProdutos();
		// txtTotalVenda.setText(Util.formataMoeda(novoValorVenda).substring(3));

		calculaTroco();

	}

	private void createListaCompraPanel() {
		jPanelTable = new JPanel(new GridLayout());

		jPanelTable.setBorder(createTitledBorder("Lista de Produtos"));

		carregaTableModel();
		// jTable = inicializaJTable();
		// jTable.getTableHeader().setFont(FONT_TAHOMA_22);
		// jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// jTable.setFillsViewportHeight(true);
		// jScrollPane = new JScrollPane(jTable);
		jPanelTable.add(jScrollPane);

	}

	private Component createHeaderPanel() throws Exception {
		LayoutManager panelNorthLayout = new BorderLayout();
		// new GridLayout(1, 2);
		JPanel panelNorth = new JPanel(panelNorthLayout);

		JPanel pesquisaProdutoPanel = createPesquisaProdutoPanel();
		JPanel produtoPanel = createProdutoPanel();

		//@formatter:off
		panelNorth.add(pesquisaProdutoPanel,BorderLayout.NORTH);
		panelNorth.add(produtoPanel,BorderLayout.SOUTH);
		//@formatter:on

		return panelNorth;
	}

	private JPanel createPesquisaProdutoPanel() throws Exception {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		ParallelGroup horizontalGroup = layout.createParallelGroup();
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));
		SequentialGroup verticalGroup = layout.createSequentialGroup();
		layout.setVerticalGroup(verticalGroup);

		panel.setLayout(layout);

		JLabel lblDescricaoBusca = createJLabel("Pesquisa Produto:");
		lblDescricaoBusca.setFont(FontMapper.getFont(22));
		vendaController.buscaTodos();
		autoCompleteComboBox = new AutocompleteJComboBox<>(this, super.getItemEstoqueController(), null, false);
		autoCompleteComboBox.setFont(FontMapper.getFont(20));
		autoCompleteComboBox.setModel(super.selectModelItemEstoque());
		JButton btnAdicionar = inicializaJButton("Adicionar");

		//@formatter:off
				horizontalGroup
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addComponent(lblDescricaoBusca)
										.addComponent(autoCompleteComboBox)
										.addComponent(btnAdicionar)
										)
								
								);
				verticalGroup
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(Alignment.LEADING)
												.addGroup(layout.createSequentialGroup().addGap(16)
														.addGroup(layout.createParallelGroup(Alignment.BASELINE)
																.addComponent(lblDescricaoBusca)
																.addComponent(autoCompleteComboBox)
																.addComponent(btnAdicionar)
																)
														)
												)
										)
								);
				//@formatter:on

		return panel;
	}

	private JPanel createProdutoPanel() {
		int i = 0;

		// GroupLayout layout = new GroupLayout(panel);

		// layout.setAutoCreateGaps(true);
		// layout.setAutoCreateContainerGaps(true);
		// ParallelGroup horizontalGroup = layout.createParallelGroup();
		// layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));
		// SequentialGroup verticalGroup = layout.createSequentialGroup();
		// layout.setVerticalGroup(verticalGroup);

		// panel.setLayout(layout);
		String blankValue = "";
		lblSaldoEstoque = createJLabel(blankValue);
		lblNomeFabricante = createJLabel(blankValue);
		lblDescricaoCategoria = createJLabel(blankValue);
		lblDescricaoProduto = createJLabel(blankValue);
		lblValorUnitario = createJLabel(blankValue);
		lblValorTotal = createJLabel(blankValue);
		txtQuantidade = inicializaNumberField();
		txtQuantidade.setColumns(10);
		txtQuantidade.setText("1");
		txtQuantidade.setEnabled(false);
		populaFormulario();
		txtQuantidade.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				txtQuantidade.selectAll();

			}
		});
		txtQuantidade.addKeyListener(createQtKeyListener());

		Component lblFabricante = createJLabel("Fabricante:");
		Component lblCategoria = createJLabel("Categoria:");
		Component lblProduto = createJLabel("Produto:");
		Component lblVlUnitario = createJLabel("Valor (R$):");
		Component lblQuantidade = createJLabel("Quantidade:");
		Component lblVlTotal = createJLabel("Total (R$):");
		JLabel lblSaldoProdutoEstoque = createJLabel("Estoque:");
		JButton btnAdicionarVenda = inicializaJButton("Adicionar Venda");

		JButton btnRemoverVenda = inicializaJButton("Remover Venda");
		Component[] componentes = new Component[10];
		// componentes[0]=lblFabricante;
		componentes[i++] = lblNomeFabricante;
		// componentes[2]=lblCategoria;
		componentes[i++] = lblDescricaoCategoria;
		// componentes[4]=lblProduto;
		componentes[i++] = lblDescricaoProduto;
		// componentes[i++] = lblVlUnitario;
		componentes[i++] = lblValorUnitario;
		componentes[i++] = lblQuantidade;
		componentes[i++] = txtQuantidade;
		// componentes[7]=lblSaldoProdutoEstoque;
		// componentes[8]=lblSaldoEstoque;
		componentes[i++] = lblVlTotal;
		componentes[i++] = lblValorTotal;
		componentes[i++] = btnAdicionarVenda;
		componentes[i] = btnRemoverVenda;
		JPanel panel = new JPanel(new GridLayout(1, componentes.length, 20, 0));
		panel.setBorder(createTitledBorder("Produto"));
		for (Component c : componentes) {
			panel.add(c);
		}
		return panel;
	}

	private KeyListener createQtKeyListener() {
		return new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// float valorTotal =
				// calculaValorTotal(Integer.valueOf(txtQuantidade.getText()),
				// Float.valueOf(lblValorUnitario.getText()));
				// lblValorUnitario.setText(Util.formataMoeda(valorTotal));
				if (txtQuantidade.getText().equals("") || txtQuantidade.getText().equals("0")) {
					txtQuantidade.setText("0");
					txtQuantidade.selectAll();
				}

				vendaController.setItemVendaBackup();
				try {
					vendaController.deduzEstoqueProduto(Integer.parseInt(txtQuantidade.getText()));

					vendaController.calculaValorTotalVenda();
					populaFormulario();

				} catch (Exception e) {
					// txtQuantidade.setText("0");
					// showErrorMessage(super,e.getMessage());
					e.printStackTrace();
				} finally {

				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		};
	}

	private Border createTitledBorder(String string) {
		TitledBorder border = BorderFactory.createTitledBorder(string);
		border.setTitleFont(FontMapper.getFont(20));
		return border;
	}

	private void addProdutoComponent(GroupLayout layout, ParallelGroup horizontalGroup, SequentialGroup verticalGroup,
			Component label, Component field) {
		horizontalGroup.addGroup(layout.createSequentialGroup().addComponent(label).addComponent(field));
		verticalGroup.addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label).addComponent(field));
	}

	// private JLabel createJLabel(String string, boolean setBorder) {
	// JLabel label = createJLabel(string);
	// if (setBorder) {
	// label.setBorder(BorderFactory.createEtchedBorder());
	// label.setAlignmentX(SwingConstants.LEFT);
	// }
	// return label;
	// }

	private JLabel createJLabel(String string) {
		JLabel label = new JLabel(string);
		label.setAlignmentX(SwingConstants.RIGHT);
		label.setFont(FontMapper.getFont(22));
		return label;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String actionCommand = arg0.getActionCommand();
		// if(actionCommand.contains("combo")) {
		// autoCompleteComboBox.reloadModel();
		// }
		boolean populaFormulario = false;
		boolean carregaTableModel = false;
		try {
			if (actionCommand.equals("Adicionar")) {

				ItemEstoque produto;
				try {
					produto = autoCompleteComboBox.getSelectedItem();
				} catch (ClassCastException e) {
					throw new ClassCastException(VendaController.PRODUTO_INVALIDO);
				}
				populaFormulario = true;
				vendaController.populaItemEstoque(produto);

				// atualizaTableModel(produto);
			} else if (actionCommand.equals("Adicionar Venda")) {
				populaFormulario = true;
				carregaTableModel = true;

				adicionaItemEstoque();
				calculaTotalVenda();
			} else if (actionCommand.equals("Remover Venda")) {
				excluiItem();
				populaFormulario = true;
				carregaTableModel = true;

			} else if (actionCommand.equals("Finalizar")) {
				finalizaVenda();
				populaFormulario = true;
				carregaTableModel = true;
				exibeMensagemSucesso(this, "Venda salva com sucesso!");
				meiosPagamentosSelecionadosList.stream().filter(jCheckBox -> jCheckBox.isSelected()).forEach(jc -> {
					jc.setSelected(false);

					List<Component> lista = pagamentoComponentsMap.get(jc.getText());
					for (Component c : lista) {
						((JTextComponent) c).setText("");
					}
				});
			}

		} catch (Exception e) {
			showErrorMessage(this, e.getMessage());

			populaFormulario = true;

		} finally {
			if (populaFormulario) {
				populaFormulario();
				// vendaController.resetItemVenda();
				((JTextComponent) autoCompleteComboBox.getEditor().getEditorComponent()).setText("");
				// autoCompleteComboBox.setSelectedItem(null);
				autoCompleteComboBox.reloadModel();
			}
			if (carregaTableModel)
				carregaTableModel();

		}
	}

	private void finalizaVenda() throws Exception {
		Venda venda = vendaController.getVenda();
		venda.setPorcentagemDesconto(Integer.valueOf(txtDesconto.getText()));
		Pagamento pagamento = venda.getPagamento();
		float troco = Util.parseStringDecimalToFloat(lblValorTroco.getText().substring(3));
		if (pagamento == null)
			pagamento = new Pagamento();

		float valorTotalRecebido = 0;
		for (MyJCheckBox cb : meiosPagamentosSelecionadosList) {
			List<Component> pagamentosComponentes = pagamentoComponentsMap.get(cb.getText());

			int cont = 0;
			MeioPagamento meioPagamento = MeioPagamentoFactory.createMeioPagamento(cb.getText());
			float valorPago = 0f;
			for (Component comp : pagamentosComponentes) {

				String valor = ((JTextComponent) comp).getText();
				if (cb.getText().equals("Cartão de Crédito")) {
					if (cont == 0) {
						int qtParcelas = valor.isEmpty() ? 0 : Integer.parseInt(valor);
						((MeioPagamentoCartaoCredito) meioPagamento).setQuantidadeParcelas(qtParcelas);
						cont++;
						continue;
					}

				} else if (cb.getText().equals("Outros")) {
					if (cont == 0) {
						((MeioPagamentoOutros) meioPagamento).setDescricaoPagamentoOutros(valor);
						cont++;
						continue;
					}
				}
				valorPago += Util.parseStringDecimalToFloat(valor);

				cont++;
			}
			valorTotalRecebido += valorPago;
			meioPagamento.setValorPago(valorPago);
			meioPagamento.setPagamento(pagamento);
			pagamento.getMeiosPagamento().add(meioPagamento);
		}
		pagamento.setValorPagamento(valorTotalRecebido);
		pagamento.setTrocoPagamento(troco);
		venda.setPagamento(pagamento);
		vendaController.finalizaVenda();

	}

	@Override
	protected void configuraJPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJPanelBusca() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void selecionaAcao(String actionCommand) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJpanelFormulario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel carregaJpanelTable(int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaTableModel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaTableModel() {
		List<ItemVenda> linhas = vendaController.getList();
		if (linhas == null)
			linhas = new ArrayList<>();
		super.carregaTableModel(parseListToLinhasTableModel(linhas), COLUNAS_JTABLE);
		jTable = inicializaJTable(myTableModel);
		if (jScrollPane == null)
			jScrollPane = new JScrollPane(jTable);
		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTable.remove(jScrollPane);

		//

		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setFillsViewportHeight(true);
		//

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTable.add(jScrollPane);

		jPanelTable.repaint();
	}

	@Override
	protected List parseListToLinhasTableModel(List lista) {
		List linhas = new ArrayList<>();

		for (Object objeto : lista) {
			ItemVenda itemVenda = (ItemVenda) objeto;
			ItemEstoque itemEstoque = itemVenda.getItemEstoque();
			//@formatter:off
			float valorProduto= itemEstoque.getValorUnitario();
			float valorVenda = itemVenda.getValorVendido();
			linhas.add(new Object[] { 
					itemEstoque.getCodigoDeBarras(),
					itemEstoque.getFabricante().getNome(),
					itemEstoque.getProduto().getDescricaoProduto(), 
					itemVenda.getQuantidade(),
					Util.formataMoeda(valorProduto),
					Util.formataMoeda(valorVenda),
					Util.formataMoeda(valorVenda * itemVenda.getQuantidade()) });
		}
		//@formatter:on
		return linhas;

	}

	@Override
	protected void excluiItem() throws Exception {
		vendaController.devolveProduto();
		calculaTotalVenda();

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {
		vendaController.adicionaVendaLista();

	}

	private void calculaTotalVenda() {
		if (txtTotalVenda != null) {
			float valorVenda = vendaController.getVenda() == null ? 0f : vendaController.getVenda().getTotalVendido();
			String valorVendaString = Util.formataMoeda(valorVenda).substring(3);
			txtTotalVenda.setText(valorVendaString);
			String vendaDesconto = Float.toString(vendaController.getVenda().getPorcentagemDesconto());
			txtDesconto.setText(vendaDesconto);
			// Util.formataStringDecimais(vendaController.getVenda().getPorcentagemDesconto()));
			if (valorVenda > 0 && vendaController.getVenda().getPorcentagemDesconto() > 0f)
				calculaPorcentagemDesconto(null);
			calculaTroco();
			repaint();
		}
	}

	@Override
	protected void populaFormulario() {
		ItemVenda itemVenda = vendaController.getItemVenda();

		ItemEstoque itemEstoque = itemVenda != null ? itemVenda.getItemEstoque() : null;

		int quantidade = (itemVenda == null || itemVenda.getQuantidade() == null) ? 0 : itemVenda.getQuantidade();
		float valorUnitario = itemEstoque != null ? itemEstoque.getValorUnitario() : 0f;
		float valorTotal = calculaValorTotal(quantidade, valorUnitario);
		lblDescricaoCategoria
				.setText(itemEstoque == null ? "" : itemEstoque.getProduto().getTipoProduto().getDescricaoTipo());
		lblDescricaoProduto.setText(itemEstoque == null ? "" : itemEstoque.getProduto().getDescricaoProduto());
		lblValorUnitario.setText(valorUnitario > 0 ? Util.formataMoeda(valorUnitario) : "");
		lblValorTotal.setText(Util.formataMoeda(valorTotal));
		lblNomeFabricante.setText(itemEstoque == null ? "" : itemEstoque.getFabricante().getNome());
		if (itemVenda != null)
			itemVenda.setQuantidade(quantidade);

		txtQuantidade.setText(Integer.toString(quantidade));
		if (quantidade == 0)
			txtQuantidade.selectAll();
		txtQuantidade.setEnabled(itemEstoque != null);
		autoCompleteComboBox.setSelectedItem(null);
		lblSaldoEstoque
				.setText((itemVenda != null && itemEstoque != null) ? itemEstoque.getQuantidade().toString() : "0");
	}

	private float calculaValorTotal(int quantidade, float valorUnitario) {
		return valorUnitario * quantidade;
	}

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			controller = getVendaController();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		super.mouseClicked(e);
	}

	private void calculaTroco() {
		float valorRecebido = Util.parseStringDecimalToFloat(txtValorPagtoDinheiro.getText())
				+ Util.parseStringDecimalToFloat(txtValorPagtoDebito.getText())
				+ Util.parseStringDecimalToFloat(txtValorPagtoCredito.getText())
				+ Util.parseStringDecimalToFloat(txtValorPagtoOutros.getText());
		float troco = 0;
		if (valorRecebido > 0) {
			float subTotalVenda = vendaController.getVenda().getTotalVendido();
			troco = valorRecebido - subTotalVenda;
		}
		lblValorTroco.setText(Util.formataMoeda(troco));
		repaint();
	}

}