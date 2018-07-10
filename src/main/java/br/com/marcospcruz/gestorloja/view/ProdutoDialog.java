package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.FontMapper;

public class ProdutoDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1224811020311576735L;

	private static final Object[] COLUNAS_TABLE_MODEL = { ConstantesEnum.CODIGO_LABEL.getValue().toString(),
			ConstantesEnum.DESCRICAO_LABEL.getValue().toString() };

	private static final String DESCRICAO_LABEL = COLUNAS_TABLE_MODEL[1] + ":";

	// private JComboBox cmbSexo;

	/**
	 * 
	 * @param owner
	 * @throws Exception
	 */
	public ProdutoDialog(JDialog owner) throws Exception {

		super(owner, ConstantesEnum.CADASTRO_PRODUTO_TITLE.getValue().toString(), ControllerAbstractFactory.PRODUTO,
				true);

	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected JPanel carregaJpanelFormulario() {

		JPanel formPanel = new JPanel(new BorderLayout(50, 50));

		formPanel.setBorder(criaTitledBorder(ConstantesEnum.PRODUTO_LABEL.getValue().toString()));

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

		formPanel.add(carregaJpanelActions(), BorderLayout.SOUTH);

		return formPanel;
	}

	/**
	 * xx
	 */
	public void atualizaTableModel() {

		// TipoProdutoController controller = new TipoProdutoController();

		if (!acaoBuscar)
			controller.buscaTodos();

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

		controller.buscaTodos();

		List linhas = parseListToLinhasTableModel(controller.getList());

		carregaTableModel(linhas, COLUNAS_TABLE_MODEL);

	}

	/**
	 * Xxx
	 */

	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() instanceof JButton) {

			try {

				selecionaAcao(arg0.getActionCommand());

//				atualizaTable = true;

			} catch (Exception e) {

				e.printStackTrace();

				showErrorMessage(this, e.getMessage());

				atualizaTable = false;

			} finally {

				if (atualizaTable) {

					// atualizaTableModel(controller.getItem());

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

		// provis�rio
		txtDescricao.setEnabled(true);
		//
		// provis�rio
		btnDeletar.setEnabled(true);
		//
		// cmbSexo.setEnabled(isSubTipo);
		//
		Produto produto = (Produto) controller.getItem();
		txtDescricao.setText(produto.getDescricaoProduto());
		acaoBuscar = true;

	}

	/**
	 * M�todo respons�vel em limpar o Formul�rio de Tipo de Produto.
	 * 
	 * @throws Exception
	 */
	private void limpaFormulario() throws Exception {

		if (!acaoBuscar) {

			controller.setItem(new Produto());

			// controller.setList(null);
		}

		// /////////////////////////////

		txtDescricao.setText("");

		// cmbSexo.setSelectedIndex(0);

//		acaoBuscar = acaoBuscar == true ? false : true;

		txtBusca.setText("");

		btnDeletar.setEnabled(false);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List parseListToLinhasTableModel(List lista) {

		List linhas = new ArrayList();

		for (Object objeto : lista) {

			Produto produto = (Produto) objeto;

			linhas.add(processaColuna(produto));

		}

		return linhas;

	}

	private Object processaColuna(Produto produto) {
		Object[] linha = new Object[COLUNAS_TABLE_MODEL.length];
		linha[0] = produto.getIdProduto();
		linha[1] = produto.getDescricaoProduto();
		return linha;
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
		if (confirmaSalvamentoItem() == 0) {
			controller.validaExistente(txtDescricao.getText());
			((Produto) controller.getItem()).setDescricaoProduto(txtDescricao.getText());
			controller.salva();
			mostraMensagemConfirmacaoSalvamento();
		}
	}

}
