package br.com.marcospcruz.gestorloja.view;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.LazyInitializationException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ProdutoDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2443699410031970101L;
	//@formatter:off
	private static final Object[] COLUNAS_TABLE_MODEL = { 
			ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			ConstantesEnum.TIPO_PRODUTO_LABEL.getValue().toString(),
			ConstantesEnum.DESCRICAO_LABEL.getValue().toString(),
			ConstantesEnum.UNIDADE_MEDIDA_LABEL.getValue().toString(),
			 };
	//@formatter:on
	private static final String DESCRICAO_LABEL = (String) COLUNAS_TABLE_MODEL[2] + ":";

	private static final String TIPO_PRODUTO_LABEL = COLUNAS_TABLE_MODEL[1] + ":";

	private JFormattedTextField txtUnidadeMedida;

	private JComboBox cmbTiposDeProduto;
	private JComboBox cmbSubTiposDeProduto;

	public ProdutoDialog(JDialog owner) throws Exception {

		super(owner, "Cadastro de Produtos", ControllerAbstractFactory.PRODUTO, true);

		// controller = new ProdutoController();

		configuraJPanel();

		setVisible(true);

	}

	public void actionPerformed(ActionEvent arg0) {

		String actionCommand = arg0.getActionCommand();

		if (arg0.getSource() instanceof JComboBox) {

			cmbSubTiposDeProduto.setModel(super.selectModelSubTiposDeProduto((JComboBox) arg0.getSource()));

		} else {

			try {

				selecionaAcao(actionCommand);

			} catch (LazyInitializationException e) {

				e.printStackTrace();

			} catch (Exception e) {

				e.printStackTrace();

				JOptionPane.showMessageDialog(null, e.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);

			} finally {

				atualizaTableModel(controller.getList());

			}

		}

		acaoBuscar = false;

	}

	

	@Override
	protected void selecionaAcao(String actionCommand) throws Exception {

		if (actionCommand.equals(SALVAR_BUTTON_LBL)) {

			adicionaItemEstoque();

		} else if (actionCommand.equals(NOVO_BUTTON_LBL)) {

			limpaFormulario();

			habilitaBotaoExcluir(false);

		} else if (actionCommand.equals(EXCLUIR_BUTTON_LBL)) {

			excluiItem();

		} else {

			buscarItem();

		}

	}

	private void buscarItem() throws Exception {
		acaoBuscar = true;

		limpaFormulario();

		controller.busca(txtBusca.getText());

		populaFormulario();

		btnDeletar.setEnabled(acaoBuscar);
	}

	@Override
	protected void excluiItem() throws Exception {

		int confirmacao = confirmaExclusaoItem();

		if (confirmacao == 0) {

			controller.excluir();

			limpaFormulario();

			btnDeletar.setEnabled(false);

		}

	}

	private void validaValorUnitario(String valorUnitario) throws Exception {

		int contadorVirgula = 0;

		for (int i = 0; i < valorUnitario.length(); i++) {

			if (valorUnitario.charAt(i) == ',')

				contadorVirgula++;

		}

		if (contadorVirgula > 1) {

			throw new Exception(COLUNAS_TABLE_MODEL[4] + " inválido!");

		}

	}

	/**
	 * x
	 * 
	 */
	private void limpaFormulario() {

		if (!acaoBuscar) {

			controller.setItem(null);

			controller.setList(null);

			txtBusca.setText("");

			cmbTiposDeProduto.setModel(carregaComboTiposProdutoModel());

			cmbTiposDeProduto.setSelectedIndex(0);

//			cmbSubTiposDeProduto.setModel(carregaSubTiposProdutoModel(new SubTipoProduto()));

		}

		txtDescricao.setText("");

		txtUnidadeMedida.setText("");

	}

	private ComboBoxModel carregaFabricantes(Fabricante fabricante) {

		ControllerBase fabricanteController = null;
		try {
			fabricanteController = ControllerAbstractFactory.createController(ControllerAbstractFactory.FABRICANTE);
		} catch (Exception e) {

			e.printStackTrace();
		}

		List<Fabricante> fabricantes = fabricanteController.getList();

		Object[] arrayFabricantes = new Object[fabricantes.size() + 1];

		arrayFabricantes[0] = "Selecione uma Opção";

		int i = 0;
		for (Fabricante f : fabricantes) {
			arrayFabricantes[++i] = f;
		}

		DefaultComboBoxModel model = new DefaultComboBoxModel<>(arrayFabricantes);
		return model;
	}

	

	@Override
	protected void populaFormulario() {

		Produto produto = (Produto) controller.getItem();

		txtUnidadeMedida.setText(produto.getUnidadeMedida());

		txtDescricao.setText(produto.getDescricaoProduto());

		SubTipoProduto subTipoProduto = (SubTipoProduto) produto.getTipoProduto();

		TipoProduto tipoProduto = subTipoProduto.getSuperTipoProduto();

		cmbTiposDeProduto.getModel().setSelectedItem(tipoProduto);

//		cmbSubTiposDeProduto.setModel(carregaSubTiposProdutoModel(tipoProduto));

		cmbSubTiposDeProduto.getModel().setSelectedItem(subTipoProduto);

		habilitaBotaoExcluir(true);

	}

	@Override
	protected void configuraJPanel() {

		int y = 0;

		jPanelBusca = carregaJPanelBusca();

		jPanelBusca.setBounds(0, y, getWidth() - 17, txtBusca.getHeight() + 30);

		add(jPanelBusca);

		y += jPanelBusca.getHeight();

		jPanelFormulario = carregaJpanelFormulario();

		jPanelFormulario.setBounds(0, y, getWidth() - 17, 250);

		add(jPanelFormulario);

		y += jPanelFormulario.getHeight();

		jPanelActions = carregaJpanelActions();

		jPanelActions.setBounds(0, y, getWidth() - 17, btnDeletar.getHeight());

		add(jPanelActions);

		y += jPanelActions.getHeight();

		jPanelTable = carregaJpanelTable(y);

		jPanelTable.setBounds(0, y, getWidth() - 17, 320);

		add(jPanelTable);

	}

	@Override
	protected JPanel carregaJpanelFormulario() {

		JPanel jPanel = new JPanel();

		jPanel.setBorder(new TitledBorder("Produto"));

		jPanel.setLayout(null);

		int y = 10;

		JLabel lbl = new JLabel(TIPO_PRODUTO_LABEL);

		lbl.setBounds(10, y, 200, 50);

		jPanel.add(lbl);

		cmbTiposDeProduto = new JComboBox();

		cmbTiposDeProduto.setModel(carregaComboTiposProdutoModel());

		cmbTiposDeProduto.setBounds(150, y + 15, 150, TXT_HEIGHT);

		cmbTiposDeProduto.addActionListener(this);

		jPanel.add(cmbTiposDeProduto);

		JLabel lb = new JLabel("Sub-Tipo:");

		lb.setBounds(cmbTiposDeProduto.getWidth() + 155, y, 200, 50);

		jPanel.add(lb);

		cmbSubTiposDeProduto = new JComboBox();

//		cmbSubTiposDeProduto.setModel(carregaSubTiposProdutoModel(new SubTipoProduto()));

		cmbSubTiposDeProduto.setBounds(cmbTiposDeProduto.getWidth() + 210, y + 15, 150, TXT_HEIGHT);

		jPanel.add(cmbSubTiposDeProduto);

		y += 45;
		// -------------------------------------------------------
		JLabel lblMarca = new JLabel(ConstantesEnum.FABRICANTE.getValue().toString());

		lblMarca.setBounds(10, y, 200, TXT_HEIGHT);

		jPanel.add(lblMarca);

		y += 25;
		// --------------------------------------------------------
		JLabel lbl1 = new JLabel(DESCRICAO_LABEL);

		lbl1.setBounds(10, y, 200, TXT_HEIGHT);

		jPanel.add(lbl1);
		// 20
		y += 10;

		txtDescricao = new JFormattedTextField();

		txtDescricao.setBounds(150, y - 10, 350, TXT_HEIGHT);

		jPanel.add(txtDescricao);
		// 45
		// y += 18;

		JLabel lbl2 = new JLabel("Un. Medida:");

		lbl2.setBounds(10, y, 200, 50);

		jPanel.add(lbl2);
		// 55
		y += 10;

		txtUnidadeMedida = new JFormattedTextField();

		txtUnidadeMedida.setBounds(150, y + 10, 200, TXT_HEIGHT);

		jPanel.add(txtUnidadeMedida);

		y += 35;
		// --------------------------------------
		JLabel lblCodigoDeBarras = new JLabel("Código de Barras: ");

		lblCodigoDeBarras.setBounds(10, y, 150, 30);

		jPanel.add(lblCodigoDeBarras);

		// txtCodigoBarras = new JTextField();
		//
		// txtCodigoBarras.setBounds(150, y, 150, TXT_HEIGHT);
		//
		// jPanel.add(txtCodigoBarras);
		// --------------------------------------
		// y += 30;

		return jPanel;

	}

	protected DefaultComboBoxModel carregaComboTiposProdutoModel() {

		Object[] tmp = ((ProdutoController) controller).getArrayTiposProduto();

		Object[] items = new Object[tmp.length + 1];

		items[0] = ITEM_ZERO_COMBO;

		for (int i = 0; i < tmp.length; i++) {

			items[i + 1] = tmp[i];

		}

		DefaultComboBoxModel comboModel = new DefaultComboBoxModel(items);

		return comboModel;

	}

	@Override
	protected JPanel carregaJpanelTable(int y) {

		JPanel jPanel = new JPanel(null);

		jPanel.setBounds(0, y, getWidth() - 17, getHeight() - 315);

		jPanel.setBorder(new TitledBorder("Produto"));

		carregaTableModel();

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(6, 15, jPanel.getWidth() - 15, jPanel.getHeight() - 20);

		jPanel.add(jScrollPane);

		return jPanel;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void atualizaTableModel(Object object) {

		// Produto produto = (Produto) object;

		try {

			// List linhas = new ArrayList();
			List linhas = (List) object;

			// linhas.add(produto);

			carregaTableModel(parseListToLinhasTableModel(linhas), COLUNAS_TABLE_MODEL);

		} catch (NullPointerException e) {

			e.printStackTrace();

			carregaTableModel();

		}

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTable.remove(jScrollPane);

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTable.add(jScrollPane);

		jPanelTable.repaint();

	}

	

	@SuppressWarnings("rawtypes")
	@Override
	protected void carregaTableModel() {

		List linhas = parseListToLinhasTableModel(controller.getList());

		carregaTableModel(linhas, COLUNAS_TABLE_MODEL);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List parseListToLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		for (Object objeto : lista) {

			Produto produto = (Produto) objeto;

			//@formatter:off
			Object[] linha = { 
					produto.getIdProduto(), 
					produto.getTipoProduto(), 
					produto.getDescricaoProduto(),
					
					produto.getUnidadeMedida(),
					
					};
			//@formatter:on
			linhas.add(linha);

		}

		return linhas;

	}

	@Override
	protected JPanel carregaJPanelBusca() {

		JPanel jPanel = new JPanel(null);

		jPanel.setBorder(BUSCAR_TITLED_BORDER);

		JLabel lbl = new JLabel(DESCRICAO_LABEL);

		lbl.setBounds(10, 15, 400, 30);

		jPanel.add(lbl);

		txtBusca = new JFormattedTextField();

		txtBusca.setBounds(150, 20, 250, TXT_HEIGHT);

		jPanel.add(txtBusca);

		btnBusca = inicializaJButton("Buscar", 450, 20, 90, txtBusca.getHeight());

		jPanel.add(btnBusca);

		return jPanel;

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {

		if (confirmaSalvamentoItem() == 0) {

			if (cmbTiposDeProduto.getSelectedIndex() == 0) {

				throw new Exception(
						ConstantesEnum.SELECAO_TIPO_PRODUTO_INVALIDA_EXCEPTION_MESSAGE.getValue().toString());

			}
			// else if (cmbSubTiposDeProduto.getSelectedIndex() == 0)
			//
			// throw new Exception(
			// ConstantesEnum.SELECAO_SUB_TIPO_PRODUTO_INVALIDA_EXCEPTION_MESSAGE.getValue().toString());

			Object tipoProduto = cmbTiposDeProduto.getSelectedItem();

			Object subTipoProduto = cmbSubTiposDeProduto.getSelectedItem();

			String descricao = txtDescricao.getText();

			String unidadeMedida = txtUnidadeMedida.getText();

//			controller.salva(
//					new Produto((TipoProduto) tipoProduto, (SubTipoProduto) subTipoProduto, descricao, unidadeMedida));

			mostraMensagemConfirmacaoSalvamento();

			limpaFormulario();

		}

	}

	@Override
	public void atualizaTableModel() {
		// TODO Auto-generated method stub
		
	}

}
