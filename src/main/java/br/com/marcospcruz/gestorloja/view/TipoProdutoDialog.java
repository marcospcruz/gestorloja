package br.com.marcospcruz.gestorloja.view;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

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

	private boolean atualizaTable;

	/**
	 * 
	 * @param owner
	 * @param loginFacade 
	 * @throws Exception
	 */
	public TipoProdutoDialog(JDialog owner, LoginFacade loginFacade) throws Exception {

		super(owner, ConstantesEnum.CADASTRO_TIPO_PRODUTO_TITLE.getValue().toString(), ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER, true, loginFacade);

		atualizaTable = true;

		configuraJPanel();

		setVisible(true);

	}

	/**
	 * x
	 */
	protected void configuraJPanel() {

		int y = 0;

		jPanelBusca = carregaJPanelBusca();

		jPanelBusca.setBounds(0, y, getWidth() - 17, txtBusca.getHeight() + 30);

		add(jPanelBusca);

		y += jPanelBusca.getHeight();

		jPanelFormulario = carregaJpanelFormulario();

		jPanelFormulario.setBounds(0, y, getWidth() - 17, 130);

		add(jPanelFormulario);

		y += jPanelFormulario.getHeight();

		jPanelActions = carregaJpanelActions();

		jPanelActions.setBounds(0, y, getWidth() - 17, btnDeletar.getHeight());

		add(jPanelActions);

		y += jPanelActions.getHeight();

		jPanelTable = carregaJpanelTable(y);

		// jPanelTable.setBounds(0, y, getWidth() - 17, 320);

		add(jPanelTable);

	}

	protected JPanel carregaJpanelActions() {

		JPanel jPanel = super.carregaJpanelActions();

		btnDeletar.setEnabled(false);

		return jPanel;

	}

	/**
	 * M�todo respons�vel em carregar jcombobox Tipos de Produto
	 * 
	 * @return
	 */
	private JComboBox carregaComboTiposProduto() {

		DefaultComboBoxModel model = carregaComboTiposProdutoModel();

		JComboBox combo = new JComboBox(model);

		combo.setSelectedIndex(0);

		return combo;
	}

	@SuppressWarnings("rawtypes")
	private DefaultComboBoxModel carregaComboTiposProdutoModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		List objetos = controller.buscaTodos();

		// List objetos = controller.getList();

		DefaultComboBoxModel model = new DefaultComboBoxModel();

		model.addElement(ITEM_ZERO_COMBO);

		for (Object objeto : objetos) {

			model.addElement((TipoProduto) objeto);

		}

		return model;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelFormulario() {

		JPanel jPanel = new JPanel();

		jPanel.setBorder(new TitledBorder(ConstantesEnum.TIPO_PRODUTO_LABEL.getValue().toString()));

		jPanel.setLayout(null);

		JLabel lbl1 = new JLabel(DESCRICAO_LABEL);

		lbl1.setBounds(10, 10, 200, 50);

		jPanel.add(lbl1);

		txtDescricao = new JFormattedTextField();

		txtDescricao.setBounds(200, 20, 200, TXT_HEIGHT);

		jPanel.add(txtDescricao);

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

		chkSubTipo.setBounds(10, 45, 190, 50);

		chkSubTipo.addActionListener(this);

		jPanel.add(chkSubTipo);

		cmbTiposProduto = carregaComboTiposProduto();

		// setBounds(200, 90, 200, TXT_HEIGHT);

		cmbTiposProduto.setBounds(200, 55, 200, TXT_HEIGHT);

		cmbTiposProduto.setEnabled(chkSubTipo.isSelected());

		jPanel.add(cmbTiposProduto);

		return jPanel;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelTable(int y) {

		JPanel jPanel = new JPanel(null);

		jPanel.setBounds(0, y, getWidth() - 17, 320);

		jPanel.setBorder(new TitledBorder(ConstantesEnum.TIPOS_PRODUTOS_LABEL.getValue().toString()));

		carregaTableModel();

		jTable = inicializaJTable();

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(6, 15, jPanel.getWidth() - 15, jPanel.getHeight() - 20);

		jPanel.add(jScrollPane);

		return jPanel;

	}

	/**
	 * xx
	 */
	private void atualizaTableModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		carregaTableModel(carregaLinhasTableModel(controller.getList()), COLUNAS_TABLE_MODEL);

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

			carregaTableModel(carregaLinhasTableModel(linhas), COLUNAS_TABLE_MODEL);

		} catch (NullPointerException e) {

			e.printStackTrace();

			carregaTableModel();

		}

		reloadJFrame();
	}

	/**
	 * x
	 */
	private void reloadJFrame() {

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTable.remove(jScrollPane);

		jTable = inicializaJTable();

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTable.add(jScrollPane);

		jPanelTable.repaint();

	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void carregaTableModel() {
		// TipoProdutoController controller = new TipoProdutoController();

		List linhas = carregaLinhasTableModel(controller.getList());

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

			salvarItem();

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

//			cmbTiposProduto.setSelectedItem(subTipoProduto);
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
	 */
	private void limpaFormulario() {

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

		cmbTiposProduto.setModel(carregaComboTiposProdutoModel());

		// cmbSexo.setSelectedIndex(0);

		acaoBuscar = acaoBuscar == true ? false : true;

		txtBusca.setText("");

		btnDeletar.setEnabled(false);

	}

	public void mouseClicked(MouseEvent e) {

		// TipoProdutoController controller = new TipoProdutoController();

		if (e.getSource() instanceof JTable) {

			JTable table = (JTable) e.getSource();

			int indiceLinha = table.getSelectedRow();

			int idSubTipo = (Integer) table.getModel().getValueAt(indiceLinha, 0);

			controller.busca(idSubTipo);

			populaFormulario();

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List carregaLinhasTableModel(List lista) {

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
	protected JPanel carregaJPanelBusca() {

		JPanel jPanel = new JPanel(null);

		jPanel.setBorder(BUSCAR_TITLED_BORDER);

		JLabel lbl = new JLabel(DESCRICAO_TIPO_PRODUTO);

		lbl.setBounds(10, 15, 400, 30);

		jPanel.add(lbl);

		txtBusca = new JFormattedTextField();

		txtBusca.setBounds(200, 20, 250, TXT_HEIGHT);

		jPanel.add(txtBusca);

		btnBusca = inicializaJButton("Buscar", 460, 20, 90, txtBusca.getHeight());

		jPanel.add(btnBusca);

		return jPanel;

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
	protected void salvarItem() throws Exception {
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