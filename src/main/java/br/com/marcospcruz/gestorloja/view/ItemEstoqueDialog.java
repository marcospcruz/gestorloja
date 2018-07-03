package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.facade.OperacaoEstoqueFacade;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.components.AutocompleteJComboBox;

public class ItemEstoqueDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3124929220441244866L;
	private AutocompleteJComboBox<Produto> cmbProduto;
	private JFormattedTextField txtQuantidadeInicial;
	private JButton btnAdicionarItem;

	private AutocompleteJComboBox<SubTipoProduto> cmbCategoriaProduto;
	private AutocompleteJComboBox<SubTipoProduto> cmbSubCategoriaProduto;

	private JFormattedTextField txtValorUnitario;
	private AutocompleteJComboBox<Fabricante> cmbFabricante;
	private JTextField txtCodigoDeBarras;
	private Box[] boxes;

	public ItemEstoqueDialog(EstoqueController controller, JDialog owner) throws Exception {

		super(owner, "Item de Estoque", true);

		setSize(900, 550);

		// controller;

		// setLayout(new FlowLayout(FlowLayout.CENTER));
		// setLayout(new GridLayout(2, 1));
		setLayout(new BorderLayout());
		add(configuraJPanelNovoItem());

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		btnAdicionarItem = inicializaJButton("Salvar Item");

		btnPanel.add(btnAdicionarItem, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);
		populaFormulario();

		setResizable(false);

		setVisible(true);

	}

	private JPanel configuraJPanelNovoItem() throws Exception {

		JPanel panel = new JPanel(new BorderLayout(20, 0));
		LayoutManager gridLayout = new GridLayout(8, 1, 0, 20);

		JPanel labelPanel = new JPanel(gridLayout);

		JPanel fieldPanel = new JPanel(gridLayout);
		panel.add(labelPanel, BorderLayout.WEST);
		panel.add(fieldPanel, BorderLayout.CENTER);
		// panel.setLayout(new GridLayout(7, 2, 2, 5));
		// BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		// panel.setLayout(boxLayout);

		panel.setBorder(super.criaTitledBorder("Item de Estoque"));

		// p1.add(this.criaJLabel("Fabricante"));
		// p1.setBorder(criaTitledBorder());
		ControllerBase fabricanteController = super.getFabricanteController();

		cmbFabricante = createAutoCompleteComboBox(fabricanteController, null, super.carregaComboFabricantes(), false,
				false);
		cmbFabricante.setPreferredSize(new Dimension(100, 100));
		// criaBox("Fabricante", cmbFabricante);
		labelPanel.add(super.criaJLabel("Fabricante"));
		fieldPanel.add(cmbFabricante);
		// panel.add(super.criaJLabel("Categoria do Produto"));
		ControllerBase categoriaController = super.getCategoriaProdutoController();

		cmbCategoriaProduto = createAutoCompleteComboBox(categoriaController, null, carregaComboTiposProdutoModel(),
				true, false);

		cmbCategoriaProduto.addActionListener(this);

		// criaBox("Categoria", cmbCategoriaProduto);
		labelPanel.add(super.criaJLabel("Categoria"));
		fieldPanel.add(cmbCategoriaProduto);
		// if (cmbCategoriaProduto.getModel().getSize() > 1)
		// cmbCategoriaProduto.setSelectedIndex(1);

		// panel.add(cmbCategoriaProduto);

		// panel.add(super.criaJLabel("Sub-Categoria do Produto"));

		cmbSubCategoriaProduto = createAutoCompleteComboBox(categoriaController, cmbCategoriaProduto,
				selectModelSubTiposDeProduto(cmbCategoriaProduto), true, true);
		cmbSubCategoriaProduto.getEditor().getEditorComponent().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextComponent textEditor = (JTextComponent) e.getSource();
//				System.out.println(textEditor.getText());

			}

			@Override
			public void keyPressed(KeyEvent e) {

				JTextComponent textEditor = (JTextComponent) e.getSource();

			}
		});

		cmbSubCategoriaProduto.addActionListener(this);
		// criaBox("Sub Categoria", cmbSubCategoriaProduto);
		labelPanel.add(super.criaJLabel("Sub Categoria"));
		fieldPanel.add(cmbSubCategoriaProduto);
		// panel.add(cmbSubCategoriaProduto);

		// panel.add(super.criaJLabel("Produto"));
		ProdutoController produtoController = super.getProdutoController();

		cmbProduto = createAutoCompleteComboBox(produtoController, null, carregaComboProdutoModel(produtoController),
				true, false);

		// panel.add(cmbProduto);

		// panel.add(super.criaJLabel("Código de Barras"));
		// criaBox("Produto", cmbProduto);
		labelPanel.add(super.criaJLabel("Produto"));
		fieldPanel.add(cmbProduto);
		JPanel txtBarras = new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtCodigoDeBarras = super.createJTextField();
		txtCodigoDeBarras.setPreferredSize(new Dimension(200, 4));
		// criaBox("Código Produto", txtCodigoDeBarras);
		txtBarras.add(txtCodigoDeBarras);
		labelPanel.add(super.criaJLabel("Código Produto"));
		fieldPanel.add(txtBarras);
		// panel.add(txtCodigoDeBarras);

		// panel.add(super.criaJLabel("Quantidade"));
		JPanel txtQt = new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtQuantidadeInicial = inicializaNumberField();
		Dimension dimension = txtQuantidadeInicial.getPreferredSize();
		txtCodigoDeBarras.setPreferredSize(dimension);
		txtQt.add(txtQuantidadeInicial);
		// criaBox("Quantidade", txtQuantidadeInicial);
		labelPanel.add(super.criaJLabel("Quantidade"));
		fieldPanel.add(txtQt);
		// panel.add(txtQuantidadeInicial);

		// panel.add(super.criaJLabel("Valor Unitário (R$)"));

		txtValorUnitario = super.inicializaDecimalNumberField();
		// criaBox("Valor Unitário (R$)", txtValorUnitario);
		labelPanel.add(super.criaJLabel("Valor Unitário (R$)"));
		JPanel vlUnit = new JPanel(new FlowLayout(FlowLayout.LEFT));
		vlUnit.add(txtValorUnitario);
		fieldPanel.add(vlUnit);
		// panel.add(txtValorUnitario);

		// for (Box box : boxes)
		// panel.add(box);
		return panel;

	}

	// private JLabel createJLabel(String string) {
	// JLabel jLabel = new JLabel(string);
	// jLabel.setFont(FontMapper.getFont(22));
	// return jLabel;
	// }

	private Box criaBox(String title, Component component) {
		if (boxes == null)
			boxes = new Box[1];
		int i = boxes.length - 1;
		if (boxes[i] != null) {
			Box[] backup = boxes;
			boxes = new Box[backup.length + 1];
			int x = 0;
			for (Box box : backup) {
				boxes[x++] = box;
			}
			i++;
		}

		boxes[i] = Box.createVerticalBox();
		Box.createGlue();
		JPanel labelPanel = new JPanel(new GridLayout(1, 1));
		GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.fill = GridBagConstraints.HORIZONTAL;
		constraints1.weightx = 1;
		constraints1.gridwidth = 1;
		constraints1.ipady = 2;
		// constraints1.gridx=1;
		// constraints1.gridy=2;
		// p1.setBorder(criaTitledBorder(title));
		JLabel jLabel = super.criaJLabel(title);
		jLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel.setBorder(BorderFactory.createEtchedBorder());
		jLabel.setPreferredSize(new Dimension(labelPanel.getPreferredSize().width / 2, 20));
		labelPanel.add(jLabel, constraints1);
		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.fill = GridBagConstraints.HORIZONTAL;
		constraints2.weightx = 5;
		constraints2.weightx = 5;
		// constraints2.ipady=15;
		// constraints2.ipadx=10;
		if (component instanceof AutocompleteJComboBox) {
			component.setPreferredSize(new Dimension(labelPanel.getPreferredSize().width / 5, 200));
			// System.out.println("p1 width:"+);
		}
		labelPanel.add(component, constraints2);
		boxes[i].add(labelPanel);
		return boxes[i];
	}

	private AutocompleteJComboBox createAutoCompleteComboBox(ControllerBase controller, JComboBox primaryCombobox,
			ComboBoxModel comboBoxModel, boolean setPopupVisibleOnFocus, boolean apagaTextoOnFocus) {
		AutocompleteJComboBox autoComboBox = new AutocompleteJComboBox(this, controller, primaryCombobox,
				setPopupVisibleOnFocus);
		autoComboBox.setModel(comboBoxModel);
		// autoComboBox.setPreferredSize(new Dimension(100,100));
		autoComboBox.setFont(FontMapper.getFont(22));
		if (apagaTextoOnFocus) {
		}

		return autoComboBox;
	}

	/**
	 * 
	 * @param produtoController
	 * @return
	 * @throws Exception
	 */
	private ComboBoxModel<Produto> carregaComboProdutoModel(ProdutoController produtoController) throws Exception {

		List lista = produtoController.getList();

		// if (lista.isEmpty()) {
		// throw new Exception("Não há Produtos cadastrados.");
		// }

		Produto[] objetos = new Produto[lista.size() + 1];
		Produto p1 = new Produto();
		p1.setDescricaoProduto(ITEM_ZERO_COMBO);
		objetos[0] = p1;

		for (int i = 0; i < lista.size(); i++) {

			objetos[i + 1] = (Produto) lista.get(i);

		}

		// Object[] objetos = { "Selecione uma Opï¿½ï¿½o" };

		DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>(objetos);

		return model;

	}

	/**
	 * xxx
	 */
	public void actionPerformed(ActionEvent arg0) {

		String mensagem = "";

		int tipoMensagem = 0;
		if ("comboBoxChanged".equals(arg0.getActionCommand())) {
			if (arg0.getSource().equals(cmbCategoriaProduto)) {

				JComboBox jComboBox = (JComboBox) arg0.getSource();
				if (jComboBox.getSelectedIndex() < 0)
					return;
				cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(jComboBox));

			} else if (arg0.getSource().equals(cmbSubCategoriaProduto)) {
				// SubTipoProduto superCategoria = ((SubTipoProduto)
				// cmbSubCategoriaProduto.getSelectedItem())
				// .getSuperTipoProduto();
				// setCmbText(superCategoria);
			}
		}

		else if (arg0.getSource() instanceof JButton) {

			try {

				adicionaItemEstoque();

				mensagem = "Estoque atualizado com sucesso!";

				tipoMensagem = JOptionPane.INFORMATION_MESSAGE;

			} catch (Exception e) {

				e.printStackTrace();

				mensagem = e.getMessage();

				tipoMensagem = JOptionPane.ERROR_MESSAGE;

			}

//			exibeMensagemSucesso(this, tipoMensagem);
			exibeMensagemSucesso(this, mensagem);

			if (tipoMensagem != JOptionPane.ERROR_MESSAGE)

				dispose();
		}

	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	protected List parseListToLinhasTableModel(List lista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void populaFormulario() {
		EstoqueController controller = null;
		try {
			controller = (EstoqueController) super.getItemEstoqueController();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemEstoque itemEstoque = (ItemEstoque) controller.getItem();
		if (itemEstoque != null) {
			Fabricante fabricante = itemEstoque.getFabricante();
			Produto produto = itemEstoque.getProduto();
			SubTipoProduto categoria = produto.getTipoProduto();
			SubTipoProduto superCategoria = categoria.getSuperTipoProduto();
			cmbFabricante.setSelectedItem(fabricante);

			cmbProduto.setSelectedItem(produto);

			cmbCategoriaProduto.setSelectedItem(superCategoria);
			cmbSubCategoriaProduto.setModel(selectModelSubTiposDeProduto(cmbCategoriaProduto));

			// cmbSubCategoriaProduto.setSelectedItem(categoria);

			cmbSubCategoriaProduto.setSelectedItem(categoria, true);
			// for (int i = 1; i < model.getSize(); i++) {
			// SubTipoProduto objeto = model.getElementAt(i);
			// if(objeto.equals(categoria)) {
			// model.setSelectedItem(objeto);
			// }
			// }
			// ((JTextComponent) cmbSubCategoriaProduto.getEditor().getEditorComponent())
			// .setText(categoria.getDescricaoTipo());
			txtCodigoDeBarras.setText(itemEstoque.getCodigoDeBarras());
			txtQuantidadeInicial.setText(itemEstoque.getQuantidade().toString());
			String valorUnitario = Util.formataMoeda(itemEstoque.getValorUnitario());
			txtValorUnitario.setText(valorUnitario.substring(3));

		}
		// txtValorUnitario.setText(Util.formataStringDecimais(8f));

	}

	private void setCmbText(AutocompleteJComboBox comboBox, String string) {
		((JTextComponent) cmbCategoriaProduto.getEditor().getEditorComponent()).setText(string);
	}

	@Override
	protected void selecionaAcao(String actionCommand) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void excluiItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void adicionaItemEstoque() throws Exception {
		EstoqueController controller = getItemEstoqueController();
		Fabricante fabricante = super.parseFabricante(cmbFabricante);
		Produto produto = super.parseProduto(cmbProduto);
		SubTipoProduto subTipoProduto = (SubTipoProduto) parseCategoriaProduto(cmbSubCategoriaProduto);
		subTipoProduto.setSuperTipoProduto((SubTipoProduto) parseCategoriaProduto(cmbCategoriaProduto));
		produto.setTipoProduto((SubTipoProduto) subTipoProduto);
		Integer quantidade = Integer.valueOf(txtQuantidadeInicial.getText());
		Float valorUnitario = Util.parseStringDecimalToFloat(txtValorUnitario.getText());
		ItemEstoque itemEstoque = controller.getItem() == null ? new ItemEstoque() : (ItemEstoque) controller.getItem();
		itemEstoque.setFabricante(fabricante);
		itemEstoque.setProduto(produto);
		itemEstoque.setQuantidade(quantidade);
		itemEstoque.setValorUnitario(valorUnitario);
		itemEstoque.setCodigoDeBarras(txtCodigoDeBarras.getText());

		OperacaoEstoqueFacade operacaoEstoqueFacade = new OperacaoEstoqueFacade((EstoqueController) controller);

		operacaoEstoqueFacade.adicionaItemEstoque(itemEstoque);

		// controller.criaItemEstoque(fabricante, txtQuantidadeInicial.getText());

	}

	@Override
	public void atualizaTableModel() {

	}

}
