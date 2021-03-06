package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.hibernate.LazyInitializationException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.view.util.MyTableModel;

public abstract class AbstractDialog extends JDialog implements MyWindowAction {

	protected static final String NOVO_BUTTON_LBL = "Novo";

	protected static final String EXCLUIR_BUTTON_LBL = "Excluir";

	protected static final String SALVAR_BUTTON_LBL = "Salvar";

	protected static final int TXT_HEIGHT = 20;

	protected static final String ITEM_ZERO_COMBO = ConstantesEnum.SELECIONE_PRIMEIRA_MSG.getValue().toString();

	protected static final String TITLE_CONFIRMING_DELETE = ConstantesEnum.CONFIRMACAO_EXCLUSAO_TITLE.getValue()
			.toString();

	protected static final String MESSAGE_CONFIRMING_DELETE = ConstantesEnum.CONFIRMACAO_EXCLUSAO.getValue().toString();

	protected JButton btnNovo;
	protected JButton btnSalvar;
	protected boolean acaoBuscar;
	protected JButton btnDeletar;
	protected JPanel jPanelBusca;
	protected JButton btnBusca;
	protected JPanel jPanelFormulario;
	protected JPanel jPanelActions;
	protected JScrollPane jScrollPane;
	protected JPanel jPanelTable;
	protected JFormattedTextField txtBusca;
	protected JFormattedTextField txtDescricao;
	protected JTable jTable;

	protected MyTableModel myTableModel;

	// protected DefaultTableCellRenderer direita;

	protected int indiceLinhaTableModel;

	// private LoginFacade loginFacade;

	// protected static final Border BUSCAR_TITLED_BORDER = new
	// TitledBorder("Busca");

	private static final String TITLE_CONFIRMING_SAVING = ConstantesEnum.CONFIRMACAO_SALVAMENTO_TITLE.getValue()
			.toString();

	private static final String MESSAGE_CONFIRMING_SAVING = ConstantesEnum.CONFIRMACAO_SALVAMENTO.getValue().toString();

	protected static final boolean IS_DECIMAL = true;

	private static final int INDICE_COLUNA = 0;

	protected boolean atualizaTable;
	protected boolean disableActionPerformed;
	protected ControllerBase controller;

	private int indiceColunaJtable;

	public AbstractDialog(JDialog owner, String tituloJanela, boolean modal) {

		super(owner, tituloJanela, modal);

		configuraDialog();

	}

	public AbstractDialog(JDialog owner, String tituloJanela, String controllerClassName, boolean modal)
			throws Exception {

		this(owner, tituloJanela, modal);

		atualizaTable = true;

		controller = getController(controllerClassName);

		configuraJPanel();

		// System.out.println(getPreferredSize());
		setVisible(true);

	}

	protected AbstractDialog(JFrame owner, String tituloJanela, boolean modal) {
		super(owner, tituloJanela, modal);

		setSize(configuraDimensaoJanela());

	}

	public AbstractDialog(JFrame owner, String tituloJanela, String className, boolean isModal) throws Exception {
		this(owner, tituloJanela, isModal);

	}

	private ControllerBase getController(String controllerClass) throws Exception {

		return SingletonManager.getInstance().getController(controllerClass);

	}

	private void configuraDialog() {
		setSize(new Dimension(800, 850));

		setDefaultCloseOperation(HIDE_ON_CLOSE);

		// setLayout(null);
	}

	protected Dimension configuraDimensaoJanela() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();

		return toolkit.getScreenSize();

	}

	/**
	 * x
	 */
	protected void reloadJFrame() {

		Rectangle retangulo = jScrollPane.getBounds();

		jPanelTable.remove(jScrollPane);

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jScrollPane.setBounds(retangulo);

		jPanelTable.add(jScrollPane);

		jPanelTable.repaint();

	}

	protected JPanel carregaJPanelBusca() {

		JPanel mainPnl = new JPanel(new BorderLayout(20, 100));

		LayoutManager layout = new BorderLayout(20, 100);
		// new GridLayout(1, 2);
		// new FlowLayout(FlowLayout.LEFT);
		//
		JPanel jPanel = new JPanel(layout);
		//
		mainPnl.setBorder(criaTitledBorder("Pesquisa"));
		//
		mainPnl.add(jPanel, BorderLayout.NORTH);
		//
		JLabel lbl = criaJLabel("Descri��o:");
		// lbl.setBorder(BorderFactory.createEtchedBorder());
		jPanel.add(lbl, BorderLayout.WEST);
		// //
		// // lbl.setBounds(10, 15, 400, 30);
		// //
		//// JPanel op = new JPanel(new BorderLayout());
		//
		// // jPanel.add(lbl);
		//
		// //
		txtBusca = new JFormattedTextField();
		txtBusca.setFont(FontMapper.getFont(20));
		// txtBusca.setBounds(200, 20, 250, TXT_HEIGHT);

		jPanel.add(txtBusca, BorderLayout.CENTER);
		// //
		//// jPanel.add(op);
		btnBusca = inicializaJButton("Buscar");
		// //// inicializaJButton("Buscar", 460, 20, 90, txtBusca.getHeight());
		// //
		// JPanel jp = new JPanel(new FlowLayout());
		// // op.add(jp);
		//// jp.add(btnBusca);
		jPanel.add(btnBusca, BorderLayout.EAST);
		return mainPnl;

	}

	/**
	 * xxx
	 */
	// protected abstract void configuraJPanel();
	/**
	 * x
	 */
	protected void configuraJPanel() {
		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		// getContentPane().setLayout(new GridLayout());
		add(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		// mainPanel.setLayout(new GridLayout(3, 1));

		mainPanel.add(carregaJPanelBusca(), BorderLayout.NORTH);
		mainPanel.add(carregaJpanelFormulario(), BorderLayout.CENTER);
		mainPanel.add(carregaJpanelTable(), BorderLayout.SOUTH);
		// System.out.println(getPreferredSize());
	}

	protected abstract void selecionaAcao(String actionCommand) throws Exception;

	protected abstract JPanel carregaJpanelFormulario();

	/**
	 * 
	 * @return
	 */

	protected JPanel carregaJpanelTable() {

		jPanelTable = new JPanel(new GridLayout());

		jPanelTable.setBorder(BorderFactory.createEtchedBorder());

		carregaTableModel();

		jTable = inicializaJTable(myTableModel);

		jScrollPane = new JScrollPane(jTable);

		jPanelTable.add(jScrollPane);

		return jPanelTable;

	}

	protected abstract void atualizaTableModel(Object object);

	protected abstract void carregaTableModel();

	protected abstract List parseListToLinhasTableModel(List lista);

	/**
	 * 
	 * @throws Exception
	 */
	protected abstract void excluiItem() throws Exception;

	protected abstract void adicionaItemEstoque() throws Exception;

	protected abstract void populaFormulario();

	/**
	 * 
	 * @return
	 */
	protected JPanel carregaJpanelActions() {

		JPanel jPanel = new JPanel();

		jPanel.setBorder(new TitledBorder(""));

		jPanel.setPreferredSize(new Dimension(getWidth(), 50));

		btnNovo = inicializaJButton(NOVO_BUTTON_LBL, 10, 100, 130, 50);

		jPanel.add(btnNovo);

		btnSalvar = inicializaJButton(SALVAR_BUTTON_LBL, 10, 100, 130, 50);

		jPanel.add(btnSalvar);

		btnDeletar = inicializaJButton(EXCLUIR_BUTTON_LBL, 10, 100, 130, 50);

		btnDeletar.setEnabled(false);

		jPanel.add(btnDeletar);

		btnDeletar.setEnabled(false);

		return jPanel;

	}

	/**
	 * M�todo respons�vel em inicializar JButton.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	protected JButton inicializaJButton(String text, int x, int y, int width, int height) {

		JButton jButton = inicializaJButton(text);

		jButton.setBounds(x, y, width, height);

		return jButton;

	}

	protected void carregaTableModel(List linhas, Object[] colunas) {

		myTableModel = new MyTableModel(linhas, colunas);

		myTableModel.fireTableDataChanged();

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param msg
	 * @param title
	 * @param message
	 * @return
	 */
	protected int confirmaExclusaoItem(String msg) {
		String mensagem = MESSAGE_CONFIRMING_DELETE;
		if (msg != null)
			mensagem = msg;

		return showConfirmationMessage(TITLE_CONFIRMING_DELETE, mensagem);
	}

	private int showConfirmationMessage(String titleConfirmationDialog, String confirmationQuestion) {
		// TODO Auto-generated method stub
		return JOptionPane.showConfirmDialog(null, criaJLabel(confirmationQuestion), titleConfirmationDialog,
				JOptionPane.YES_NO_OPTION);
	}

	protected int confirmaSalvamentoItem() {

		return showConfirmationMessage(TITLE_CONFIRMING_SAVING, MESSAGE_CONFIRMING_SAVING);

	}

	public void mostraMensagemConfirmacaoSalvamento() {

		JOptionPane.showMessageDialog(this,
				criaJLabel(ConstantesEnum.CONFIRMACAO_REGISTRO_ATUALIZADO.getValue().toString()),
				ConstantesEnum.CONFIRMANDO_ATUALIZACAO_MSG_TITLE.getValue().toString(),
				JOptionPane.INFORMATION_MESSAGE);

	}

	public void mouseClicked(MouseEvent e) {

		if (e.getSource() instanceof JTable) {

			JTable table = (JTable) e.getSource();

			indiceLinhaTableModel = table.getSelectedRow();

			Object id = table.getModel().getValueAt(indiceLinhaTableModel, indiceColunaJtable);

			try {
				controller.busca(id);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			populaFormulario();
			controller.setList(null);

		}

	}

	public int getIndiceColunaJtable() {
		return indiceColunaJtable;
	}

	public void setIndiceColunaJtable(int indiceColunaJtable) {
		this.indiceColunaJtable = indiceColunaJtable;
	}

	protected void habilitaBotaoExcluir(boolean enabled) {
		btnDeletar.setEnabled(enabled);

	}

	@SuppressWarnings("rawtypes")
	protected DefaultComboBoxModel carregaComboTiposProdutoModel() throws Exception {

		// TipoProdutoController controller = new TipoProdutoController();
		TipoProdutoController tipoController = getCategoriaProdutoController();
		if (tipoController.getList() == null)
			tipoController.buscaTodos();
		List<TipoProduto> backup = new ArrayList(tipoController.getList());
		// List objetos = tipoController.buscaTodos();

		List objetos = tipoController.getList();

		DefaultComboBoxModel<TipoProduto> model = new DefaultComboBoxModel<>();
		TipoProduto t1 = new SubTipoProduto();
		t1.setDescricaoTipo(ITEM_ZERO_COMBO);
		model.addElement(t1);

		for (Object objeto : objetos) {

			model.addElement((TipoProduto) objeto);

		}

		return model;
	}

	/**
	 * M�todo respons�vel em carregar jcombobox Tipos de Produto
	 * 
	 * @return
	 * @throws Exception
	 */
	protected JComboBox<TipoProduto> carregaComboTiposProduto() throws Exception {

		JComboBox<TipoProduto> combo = new JComboBox(carregaComboTiposProdutoModel());

		combo.setSelectedIndex(0);

		return combo;
	}

	/**
	 * 
	 * @param combo
	 * @return
	 */
	protected ComboBoxModel selectModelSubTiposDeProduto(JComboBox combo) {

		// if (combo.getSelectedIndex() > 0)

		Object item = combo.getSelectedItem();
		return carregaSubTiposProdutoModel(item);

		// else

		// return carregaSubTiposProdutoModel(new SubTipoProduto());

	}

	public ComboBoxModel<Produto> selectModelProdutos(Collection<Produto> produtos) {
		DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>();
		model.addElement(new Produto("Selecione uma Op��o."));
		for (Produto p : produtos) {
			model.addElement(p);
		}
		return model;
	}

	/**
	 * 
	 * @param selectedItem
	 * @return
	 */
	private DefaultComboBoxModel carregaSubTiposProdutoModel(Object selectedItem) {

		DefaultComboBoxModel model = null;
		//
		SubTipoProduto tipoProduto = null;
		try {
			TipoProdutoController tipoProdutoController = getCategoriaProdutoController()
			// !(controller instanceof TipoProdutoController)
			// ? getCategoriaProdutoController()
			// : controller
			;
			tipoProdutoController.busca(((SubTipoProduto) selectedItem).getIdTipoItem());
			tipoProduto = (SubTipoProduto) tipoProdutoController.getItem();

		} catch (Exception e) {
			tipoProduto = new SubTipoProduto();
			tipoProduto.setDescricaoTipo(selectedItem.toString());
		}
		List<SubTipoProduto> tiposProduto = null;
		if (tipoProduto != null) {
			tiposProduto = (List<SubTipoProduto>) tipoProduto.getSubTiposProduto();
		}
		Object[] arrayTipos = null;

		if (tiposProduto == null)

			tiposProduto = new ArrayList<>();

		try {
			if (tiposProduto != null && !tiposProduto.isEmpty()) {
				arrayTipos = new Object[tiposProduto.size() + 1];

				arrayTipos[0] = ITEM_ZERO_COMBO;

				for (int i = 0; i < tiposProduto.size(); i++)

					arrayTipos[i + 1] = tiposProduto.get(i);

				model = new DefaultComboBoxModel(arrayTipos);
			} else
				model = new DefaultComboBoxModel<>();

		} catch (LazyInitializationException e) {

			e.printStackTrace();

			throw new LazyInitializationException(e.getMessage());

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return model;

	}

	public TipoProdutoController getCategoriaProdutoController() throws Exception {

		return (TipoProdutoController) getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
	}

	public abstract void atualizaTableModel();

	public TipoProduto parseCategoriaProduto(JComboBox jComboBox) throws Exception {
		TipoProdutoController tpController = getCategoriaProdutoController();
		tpController.setItem(null);
		validaSelecaoComboBox(jComboBox);
		Object selectedItem = jComboBox.getSelectedItem();
		TipoProduto categoriaProduto = null;
		if (selectedItem instanceof SubTipoProduto)
			return (TipoProduto) selectedItem;
		else {
			try {
				tpController.busca(selectedItem.toString().trim());
			} catch (Exception e) {
				e.printStackTrace();

			}

			categoriaProduto = tpController.getItem() == null ? new SubTipoProduto()
					: (SubTipoProduto) tpController.getItem();
			categoriaProduto.setDescricaoTipo(selectedItem.toString().trim());
		}
		if (categoriaProduto.getOperador() == null)
			categoriaProduto.setOperador(getUsuarioLogado());
		return categoriaProduto;
	}

	private void validaSelecaoComboBox(JComboBox jComboBox) throws Exception {
		boolean invalido = ITEM_ZERO_COMBO.equals(jComboBox.getModel().getSelectedItem().toString());
		if (invalido) {
			throw new Exception("Sele��o de �tem Inv�lida.");
		}
	}

	public Produto parseProduto(JComboBox jComboBox) throws Exception {
		validaSelecaoComboBox(jComboBox);
		Object selectedItem = jComboBox.getSelectedItem();
		Produto produto;
		if (selectedItem instanceof Produto)
			return (Produto) selectedItem;
		else {
			produto = new Produto();
			produto.setDescricaoProduto(selectedItem.toString().trim());
		}
		// if (produto.getOperador() == null)
		produto.setOperador(getUsuarioLogado());
		produto.setDataInsercao(SingletonManager.getInstance().getData());
		return produto;
	}

	public ControllerBase getFabricanteController() throws Exception {

		return getController(ControllerAbstractFactory.FABRICANTE);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ComboBoxModel carregaComboFabricantes() throws Exception {
		ControllerBase fabricanteController = getFabricanteController();

		List objetos = fabricanteController.getList();

		// List objetos = controller.getList();

		DefaultComboBoxModel<Fabricante> model = new DefaultComboBoxModel<>();
		Fabricante t1 = new Fabricante();
		t1.setNome(ITEM_ZERO_COMBO);
		model.addElement(t1);

		for (Object objeto : objetos) {

			model.addElement((Fabricante) objeto);

		}

		return model;
	}

	public Fabricante parseFabricante(JComboBox jComboBox) throws Exception {
		validaSelecaoComboBox(jComboBox);
		Object selectedItem = jComboBox.getSelectedItem();
		Fabricante fabricante;
		if (selectedItem instanceof Fabricante)
			return (Fabricante) selectedItem;
		else {
			fabricante = new Fabricante();
			fabricante.setNome(selectedItem.toString().trim());
			try {
				getFabricanteController().busca(fabricante.getNome());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (getFabricanteController().getItem() != null)
				fabricante = (Fabricante) getFabricanteController().getItem();
		}
		if (fabricante.getOperador() == null)
			fabricante.setOperador(getUsuarioLogado());
		return fabricante;
	}

	private Usuario getUsuarioLogado() {

		return SingletonManager.getInstance().getUsuarioLogado();
	}

	public void disableActionPerformed() {
		disableActionPerformed = true;

	}

	public void enableActionPerformance() {
		disableActionPerformed = false;

	}

	public EstoqueController getItemEstoqueController() throws Exception {

		return (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
	}

	public ComboBoxModel<ItemEstoque> selectModelItemEstoque() throws Exception {
		List<ItemEstoque> estoque = getItemEstoqueController().buscaTodos();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(ITEM_ZERO_COMBO);
		estoque.stream().forEach(objeto -> {
			ItemEstoque itemEstoque = objeto;
			// if (itemEstoque.getFabricante() == null)
			// System.out.println();
			model.addElement(itemEstoque);
		});
		return model;
	}

	public ProdutoController getProdutoController() throws Exception {

		return (ProdutoController) getController(ControllerAbstractFactory.PRODUTO);
	}

	public VendaController getVendaController() throws Exception {

		return (VendaController) getController(ControllerAbstractFactory.CONTROLE_VENDA);
	}

	public ControllerBase getCaixaController() throws Exception {

		return getController(ControllerAbstractFactory.CONTROLE_CAIXA);
	}

}
