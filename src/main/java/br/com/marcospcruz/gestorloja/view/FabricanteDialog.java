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

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.FontMapper;

public class FabricanteDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1224811020311576735L;

	private static final Object[] COLUNAS_TABLE_MODEL = { ConstantesEnum.CODIGO_LABEL.getValue().toString(), "Nome" };

	private static final String DESCRICAO_LABEL = COLUNAS_TABLE_MODEL[1] + ":";

	/**
	 * 
	 * @param owner
	 * @throws Exception
	 */
	public FabricanteDialog(JDialog owner) throws Exception {

		super(owner, "Cadastro de " + ConstantesEnum.FABRICANTE.getValue().toString(),
				ControllerAbstractFactory.FABRICANTE, true);

	}

	@SuppressWarnings("rawtypes")
	protected DefaultComboBoxModel carregaComboTiposProdutoModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		List objetos = controller.buscaTodos();

		// List objetos = controller.getTiposProdutos();

		DefaultComboBoxModel model = new DefaultComboBoxModel();

		model.addElement(ITEM_ZERO_COMBO);

		for (Object objeto : objetos) {

			// model.addElement((TipoProduto) objeto);

		}

		return model;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelFormulario() {

		JPanel formPanel = new JPanel(new BorderLayout(50, 50));

		formPanel.setBorder(criaTitledBorder(ConstantesEnum.FABRICANTE.getValue().toString()));

		// formPanel.setLayout(new BorderLayout());
		// GridLayout centerPnlLayout = new GridLayout(1, 1);
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

		txtDescricao.setFont(FontMapper.getFont(20));

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

		formPanel.add(carregaJpanelActions(), BorderLayout.SOUTH);

		return formPanel;
	}

	/**
	 * xx
	 */
	public void atualizaTableModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		// if (controller.getItem() == null) {

		controller.buscaTodos();

		// }

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
	//
	// /**
	// * x
	// */
	// private void reloadJFrame() {
	//
	// Rectangle retangulo = jScrollPane.getBounds();
	//
	// jPanelTable.remove(jScrollPane);
	//
	// jTable = inicializaJTable();
	//
	// jScrollPane = new JScrollPane(jTable);
	//
	// jScrollPane.setBounds(retangulo);
	//
	// jPanelTable.add(jScrollPane);
	//
	// jPanelTable.repaint();
	//
	// }

	@SuppressWarnings("rawtypes")
	@Override
	protected void carregaTableModel() {

		List linhas = parseListToLinhasTableModel(controller.getList());

		carregaTableModel(linhas, COLUNAS_TABLE_MODEL);

	}

	/**
	 * Transforma TipoProduto em Array de Objects. Para ser usado como linha na
	 * JTable
	 * 
	 * @param fabricante
	 * @return
	 */
	private Object[] processaColuna(Fabricante fabricante) {

		Object[] linha = new Object[COLUNAS_TABLE_MODEL.length];

		linha[0] = fabricante.getIdFabricante();

		linha[1] = fabricante.getNome();

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

				showErrorMessage(this, e.getMessage());

				atualizaTable = true;

			} finally {

				if (atualizaTable) {

					// atualizaTableModel(controller.getTipoPeca());

					atualizaTableModel();

				}

			}

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

		// controller.setList(new ArrayList());
		// controller.getList().add(controller.getItem());

		limpaFormulario();

		if (!controller.getList().isEmpty())

			populaFormulario();

	}

	/**
	 * x
	 */
	@Override
	protected void populaFormulario() {

		// TipoProdutoController controller = new TipoProdutoController();
		Fabricante fabricante = (Fabricante) controller.getItem();
		boolean encontrado = fabricante != null;
		if (encontrado)
			txtDescricao.setText(fabricante.getNome());

		//

		// provisï¿½rio
		txtDescricao.setEnabled(encontrado);
		//
		// provisï¿½rio
		btnDeletar.setEnabled(encontrado);
		//
		// cmbSexo.setEnabled(isSubTipo);
		//

		acaoBuscar = true;

	}

	/**
	 * Mï¿½todo responsï¿½vel em limpar o Formulï¿½rio de Tipo de Produto.
	 */
	private void limpaFormulario() {

		if (!acaoBuscar) {

			controller.setItem(new Fabricante());

			controller.setList(null);

			controller.buscaTodos();
			// controller.setList(null);
		}

		txtDescricao.setEnabled(true);

		// /////////////////////////////

		txtDescricao.setText("");

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

			Fabricante fabricante = (Fabricante) objeto;

			linhas.add(processaColuna(fabricante));

		}

		return linhas;
	}

	@Override
	protected void excluiItem() throws Exception {

		atualizaTable = false;

		String msg = controller.validaExclusaoItem();
		int

		confirmacao = confirmaExclusaoItem(msg);

		if (confirmacao == 0) {

			controller.excluir();

			showMessage(this, "Fabricante / Marca excluído com sucesso!");

			// controller.setItem(new Fabricante());
			//
			// controller.setList(null);

			limpaFormulario();

		}

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {
		String nome = txtDescricao.getText().trim();
		controller.validaExistente(nome);
		if (confirmaSalvamentoItem() == 0) {

			((Fabricante) controller.getItem()).setNome(nome);
			controller.salva();
			mostraMensagemConfirmacaoSalvamento();
		}

	}

}
