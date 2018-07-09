package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Layout;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.FontMapper;

public class TipoProdutoDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1224811020311576735L;

	private static final Object[] COLUNAS_TABLE_MODEL = { ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			ConstantesEnum.DESCRICAO_LABEL.getValue().toString(),
			ConstantesEnum.SUB_DESCRICAO_LABEL.getValue().toString() };

	private static final String DESCRICAO_TIPO_PRODUTO = COLUNAS_TABLE_MODEL[1] + " / " + COLUNAS_TABLE_MODEL[2];

	private static final String DESCRICAO_LABEL = COLUNAS_TABLE_MODEL[1] + ":";

	private JCheckBox chkSubTipo;

	private JComboBox cmbTiposProduto;
	// private JComboBox cmbSexo;

	/**
	 * 
	 * @param owner
	 * @throws Exception
	 */
	public TipoProdutoDialog(JDialog owner) throws Exception {

		super(owner, ConstantesEnum.CADASTRO_TIPO_PRODUTO_TITLE.getValue().toString(),
				ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER, true);

	}

	/**
	 * x
	 */
	protected void configuraJPanel() {

		JPanel mainPanel = new JPanel();
		getContentPane().setLayout(new GridLayout());
		add(mainPanel);
		// mainPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new GridLayout(3, 1));
		jPanelBusca = carregaJPanelBusca();
		mainPanel.add(jPanelBusca, BorderLayout.NORTH);
		mainPanel.add(carregaJpanelFormulario(), BorderLayout.CENTER);
		mainPanel.add(carregaJpanelTable(), BorderLayout.SOUTH);

	}

	protected JPanel carregaJpanelActions() {

		JPanel jPanel = super.carregaJpanelActions();

		btnDeletar.setEnabled(false);

		return jPanel;

	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelFormulario() {

		JPanel formPanel = new JPanel();

		formPanel.setBorder(criaTitledBorder(ConstantesEnum.TIPO_PRODUTO_LABEL.getValue().toString()));

		formPanel.setLayout(new BorderLayout());
		// GridLayout centerPnlLayout = new GridLayout(2, 1);
		LayoutManager centerPnlLayout = new BorderLayout();
		// LayoutManager leftPnlLayout = new GridLayout(2, 1);
		LayoutManager leftPnlLayout = new BorderLayout();
		JPanel leftPanel = new JPanel(leftPnlLayout);
		formPanel.add(leftPanel, BorderLayout.WEST);
		JLabel lbl1 = criaJLabel(DESCRICAO_LABEL);

		leftPanel.add(lbl1, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(centerPnlLayout);
		formPanel.add(centerPanel, BorderLayout.CENTER);
		txtDescricao = new JFormattedTextField();

		txtDescricao.setFont(FontMapper.getFont(22));

		centerPanel.add(txtDescricao, BorderLayout.NORTH);

		// JLabel lbl2 = new JLabel("Sexo:");
		//
		// lbl2.setBounds(10, 45, 200, 50);
		//
		// jPanel.add(lbl2);

		// cmbSexo = carregaComboSexo();
		//
		// cmbSexo.setEnabled(false);
		//
		// cmbSexo.setBounds(200, 55, 200, TXT_HEIGHT);
		//
		// jPanel.add(cmbSexo);

		chkSubTipo = new JCheckBox(ConstantesEnum.SUB_TIPO_DE_LABEL.getValue().toString());
		// setBounds(10, 90, 150, 30);

		chkSubTipo.setFont(FontMapper.getFont(22));

		chkSubTipo.addActionListener(this);

		leftPanel.add(chkSubTipo, BorderLayout.SOUTH);

		try {
			cmbTiposProduto = super.carregaComboTiposProduto();
		} catch (Exception e) {

			e.printStackTrace();
		}

		cmbTiposProduto.setEnabled(chkSubTipo.isSelected());
		cmbTiposProduto.setFont(FontMapper.getFont(22));

		// formPanel.add(cmbTiposProduto);
		centerPanel.add(cmbTiposProduto, BorderLayout.SOUTH);
		formPanel.add(carregaJpanelActions(), BorderLayout.SOUTH);

		return formPanel;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelTable() {

		JPanel jPanel = new JPanel(new GridLayout());

		jPanel.setBorder(criaTitledBorder(ConstantesEnum.TIPOS_PRODUTOS_LABEL.getValue().toString()));

		carregaTableModel();

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jPanel.add(jScrollPane);

		return jPanel;

	}

	/**
	 * xx
	 */
	public void atualizaTableModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		carregaTableModel(parseListToLinhasTableModel(controller.getList()), COLUNAS_TABLE_MODEL);

		reloadJFrame();

	}

	/**
	 * xx
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void atualizaTableModel(Object object) {

		SubTipoProduto subTipo = (SubTipoProduto) object;

		try {

			List linhas = new ArrayList();

			linhas.add(subTipo);

			carregaTableModel(parseListToLinhasTableModel(linhas), COLUNAS_TABLE_MODEL);

		} catch (NullPointerException e) {

			e.printStackTrace();

			carregaTableModel();

		}

		reloadJFrame();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void carregaTableModel() {
		// TipoProdutoController controller = new TipoProdutoController();

		List linhas = parseListToLinhasTableModel(controller.getList());

		carregaTableModel(linhas, COLUNAS_TABLE_MODEL);

	}

	/**
	 * Transforma TipoProduto em Array de Objects. Para ser usado como linha na
	 * JTable
	 * 
	 * @param subTipo
	 * @return
	 */
	private Object[] processaColuna(SubTipoProduto subTipo) {

		Object[] linha = new Object[COLUNAS_TABLE_MODEL.length];

		linha[0] = subTipo.getIdTipoItem();

		linha[1] = subTipo.getSuperTipoProduto() == null ? subTipo.getDescricaoTipo()
				: subTipo.getSuperTipoProduto().getDescricaoTipo();

		linha[2] = subTipo.getSuperTipoProduto() != null ? subTipo.getDescricaoTipo() : "";

		// linha[3] = subTipo.getSexo();

		return linha;

	}

	/**
	 * Xxx
	 */

	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() instanceof JButton) {

			try {

				selecionaAcao(arg0.getActionCommand());

				atualizaTable = true;

			} catch (Exception e) {

				e.printStackTrace();

				JOptionPane.showMessageDialog(null, e.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);

				atualizaTable = false;

			} finally {

				if (atualizaTable) {

					// atualizaTableModel(controller.getItem());

					atualizaTableModel();

				}

			}

		} else {

			verificaSubTipoCheckBox();

		}

	}

	/**
	 * 
	 * @param actionCommand
	 * @throws Exception
	 */

	protected void selecionaAcao(String actionCommand) throws Exception {

		if (actionCommand.equals(EXCLUIR_BUTTON_LBL)) {

			excluiItem();

		} else if (actionCommand.equals(NOVO_BUTTON_LBL)) {

			acaoBuscar = false;

			atualizaTable = true;

			limpaFormulario();

		} else if (actionCommand.equals(SALVAR_BUTTON_LBL)) {

			acaoBuscar = false;

			adicionaItemEstoque();

			limpaFormulario();

		} else if (actionCommand.equals("Buscar")) {

			acaoBuscar = true;

			buscar();

		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	private void buscar() throws Exception {

		// TipoProdutoController controller = new TipoProdutoController();

		controller.busca(txtBusca.getText());

		limpaFormulario();

		if (controller.getList().size() >= 1)

			populaFormulario();

	}

	/**
	 * x
	 */
	@Override
	protected void populaFormulario() {

		// TipoProdutoController controller = new TipoProdutoController();

		boolean isSubTipo = ((SubTipoProduto) controller.getItem()).getSuperTipoProduto() != null;

		txtDescricao.setText(((SubTipoProduto) controller.getItem()).getDescricaoTipo());

		chkSubTipo.setEnabled(isSubTipo);
		//
		chkSubTipo.setSelected(isSubTipo);

		// provis�rio
		txtDescricao.setEnabled(true);
		//
		// provis�rio
		btnDeletar.setEnabled(true);
		//
		// cmbSexo.setEnabled(isSubTipo);
		//
		cmbTiposProduto.setEnabled(isSubTipo);

		if (isSubTipo) {

			// String sexo = controller.getItem().getSexo();

			// cmbSexo.setSelectedItem(sexo);

			SubTipoProduto subTipoProduto = ((SubTipoProduto) controller.getItem()).getSuperTipoProduto();

			// cmbTiposProduto.setSelectedItem(subTipoProduto);
			cmbTiposProduto.getModel().setSelectedItem(subTipoProduto);

		} else {

			// cmbSexo.setSelectedIndex(0);

			cmbTiposProduto.setSelectedIndex(0);
			//
			// btnDeletar.setEnabled(!(controller.getItem()
			// .getSubTiposProduto().size() > 0));

		}

		acaoBuscar = true;

	}

	private void verificaSubTipoCheckBox() {

		boolean selected = chkSubTipo.isSelected();

		cmbTiposProduto.setEnabled(selected);

		// cmbSexo.setEnabled(selected);

		if (!selected) {

			cmbTiposProduto.setSelectedIndex(0);

			// cmbSexo.setSelectedIndex(0);
			//

		}

		// limpaFormulario();

	}

	/**
	 * M�todo respons�vel em limpar o Formul�rio de Tipo de Produto.
	 * 
	 * @throws Exception
	 */
	private void limpaFormulario() throws Exception {

		if (!acaoBuscar) {

			controller.setItem(null);

			controller.setList(null);
		}

		txtDescricao.setEnabled(true);

		chkSubTipo.setEnabled(true);

		// /////////////////////////////

		txtDescricao.setText("");

		chkSubTipo.setSelected(false);

		verificaSubTipoCheckBox();

		cmbTiposProduto.setModel(super.carregaComboTiposProdutoModel());

		// cmbSexo.setSelectedIndex(0);

		acaoBuscar = acaoBuscar == true ? false : true;

		txtBusca.setText("");

		btnDeletar.setEnabled(false);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List parseListToLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		for (Object objeto : lista) {

			SubTipoProduto subTipo = (SubTipoProduto) objeto;

			linhas.add(processaColuna(subTipo));

			try {

				for (SubTipoProduto tipo : subTipo.getSubTiposProduto()) {

					linhas.add(processaColuna(tipo));

				}

			} catch (NullPointerException e) {

				e.printStackTrace();

			}

		}

		return linhas;
	}

	@Override
	protected void excluiItem() throws Exception {

		atualizaTable = false;

		int confirmacao = confirmaExclusaoItem();

		if (confirmacao == 0) {

			controller.excluir();

			limpaFormulario();

		}

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {
		// TODO Auto-generated method stub
		// TipoProdutoController controller = new TipoProdutoController();

		if (confirmaSalvamentoItem() == 0) {

			// String sexo = cmbSexo.getSelectedIndex() == 0 ? null : cmbSexo
			// .getSelectedItem().toString();

			Object tipoProduto = cmbTiposProduto.getSelectedIndex() == 0 ? null : cmbTiposProduto.getSelectedItem();

			// controller.salva(txtDescricao.getText(), chkSubTipo.isSelected(),
			// tipoProduto, sexo);

			controller.salva(txtDescricao.getText(), chkSubTipo.isSelected(), tipoProduto);

			mostraMensagemConfirmacaoSalvamento();

		}
	}

}