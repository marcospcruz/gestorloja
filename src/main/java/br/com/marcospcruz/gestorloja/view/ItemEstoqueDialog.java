package br.com.marcospcruz.gestorloja.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ItemEstoqueDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3124929220441244866L;
	private JComboBox cmbProduto;
	private JFormattedTextField txtQuantidadeInicial;
	private JButton btnAdicionarItem;
	private EstoqueController controller;

	public ItemEstoqueDialog(EstoqueController controller, JDialog owner) throws Exception {

		super(owner, "Item de Estoque", true);

		setSize(800, 200);

		this.controller = controller;

		setLayout(new FlowLayout(FlowLayout.CENTER));

		add(configuraJPanelNovoItem());

		btnAdicionarItem = inicializaJButton("Adicionar Item");

		add(btnAdicionarItem);

		setVisible(true);

	}

	private JPanel configuraJPanelNovoItem() throws Exception {

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(2, 2, 2, 5));

		panel.setBorder(new TitledBorder("Novo Item de Estoque"));

		panel.add(new JLabel("Produto"));

		cmbProduto = new JComboBox();

		cmbProduto.setModel(carregaComboProdutoModel());

		panel.add(cmbProduto);

		panel.add(new JLabel("Quantidade"));

		txtQuantidadeInicial = inicializaNumberField();

		panel.add(txtQuantidadeInicial);

		return panel;

	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	private ComboBoxModel carregaComboProdutoModel() throws Exception {

		ProdutoController controller = new ProdutoController();

		List lista = controller.getList();
		
		if(lista.isEmpty()){
			throw new Exception("Não há Produtos cadastrados.");
		}

		Object[] objetos = new Object[lista.size() + 1];

		objetos[0] = ITEM_ZERO_COMBO;

		for (int i = 0; i < lista.size(); i++) {

			objetos[i + 1] = lista.get(i);

		}

		// Object[] objetos = { "Selecione uma Opï¿½ï¿½o" };

		DefaultComboBoxModel model = new DefaultComboBoxModel(objetos);

		return model;

	}

	/**
	 * xxx
	 */
	public void actionPerformed(ActionEvent arg0) {

		String mensagem = "";

		int tipoMensagem = 0;

		try {

			salvarItem();

			mensagem = "Item adicionado ao estoque com sucesso!";

			tipoMensagem = JOptionPane.INFORMATION_MESSAGE;

		} catch (Exception e) {

			e.printStackTrace();

			mensagem = e.getMessage();

			tipoMensagem = JOptionPane.ERROR_MESSAGE;

		}

		exibeMensagemRetorno(mensagem, tipoMensagem);

		if (tipoMensagem != JOptionPane.ERROR_MESSAGE)

			dispose();

	}

	private void exibeMensagemRetorno(String mensagem, int tipoMensagem) {

		JOptionPane.showMessageDialog(null, mensagem, "Alerta", tipoMensagem);

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
	protected List carregaLinhasTableModel(List lista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void populaFormulario() {
		// TODO Auto-generated method stub

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
	protected void salvarItem() throws Exception {
		// TODO Auto-generated method stub

		Produto produto = null;

		try {

			produto = (Produto) cmbProduto.getSelectedItem();

		} catch (ClassCastException e) {

			e.printStackTrace();

			// atualizaView();

			throw new Exception(ConstantesEnum.PRODUTO_NECESSARIO_SELECIONAR_ALERTA.getValue().toString());

		}

		controller.buscaItemEstoque(produto.getDescricaoProduto());

		if (cmbProduto.getSelectedIndex() == 0) {

			throw new Exception(ConstantesEnum.SELECAO_INVALIDA.getValue().toString());

		}

		if (controller.getItemEstoque() != null) {

			controller.anulaAtributos();

			throw new Exception(ConstantesEnum.PRODUTO_JA_EXISTENTE_MESSAGE_EXCEPTION.getValue().toString());

		}

		controller.criaItemEstoque(produto, txtQuantidadeInicial.getText());

	}

}
