package br.com.marcospcruz.gestorloja.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.MyFormatador;

public class ItemDoEstoqueDialog extends AbstractDialog {

	private JLabel lblCodigo;
	private JLabel lblDescricaoTipoProduto;
	private JLabel lblDescricaoProduto;
	private JLabel lblValorUnitario;
	private JFormattedTextField txtQuantidade;
	private JButton btnSalvar;
	private JButton btnApagar;

	private EstoqueController controller;

	private ItemDoEstoqueDialog(JDialog owner, String tituloJanela, boolean modal) throws Exception {

		super(owner, tituloJanela, modal);

		setSize(600, 300);

		setLayout(new FlowLayout());

		add(carregaItemEstoquePnl());

		add(quantidadeField());

		add(carregaJButtons());

	}

	public ItemDoEstoqueDialog(JDialog owner, EstoqueController controller) throws Exception {

		this(owner, "Item do Estoque: "
				+ controller.getItemEstoque().getProduto()
						.getDescricaoProduto(), true);

		this.controller = controller;

		populaFormulario(controller.getItemEstoque());

		setVisible(true);

	}

	private JPanel carregaJButtons() {

		JPanel panelBotoes = new JPanel();

		btnSalvar = inicializaJButton(SALVAR_BUTTON_LBL);

		panelBotoes.add(btnSalvar);

		btnApagar = inicializaJButton(EXCLUIR_BUTTON_LBL);

		panelBotoes.add(btnApagar);

		return panelBotoes;
	}

	private JPanel quantidadeField() {

		JPanel panel = new JPanel();

		panel.add(new JLabel("Quantidade"));

		txtQuantidade = inicializaNumberField();

		panel.add(txtQuantidade);

		return panel;
	}

	private JPanel carregaItemEstoquePnl() {

		// JPanel itemEstoquePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel itemEstoquePnl = new JPanel(new GridLayout(2, 2));

		itemEstoquePnl.setPreferredSize(new Dimension(500, 150));

		// itemEstoquePnl.setBorder(new TitledBorder("Item de Estoque"));

		lblCodigo = new JLabel();

		lblCodigo.setBorder(new TitledBorder(ConstantesEnum.CODIGO_LABEL
				.getValue().toString()));

		itemEstoquePnl.add(lblCodigo);

		lblDescricaoTipoProduto = new JLabel();

		lblDescricaoTipoProduto.setBorder(new TitledBorder(
				ConstantesEnum.TIPO_PRODUTO_LABEL.getValue().toString()));

		itemEstoquePnl.add(lblDescricaoTipoProduto);

		lblDescricaoProduto = new JLabel();

		lblDescricaoProduto.setBorder(new TitledBorder(
				ConstantesEnum.PRODUTO_LABEL.getValue().toString()));

		itemEstoquePnl.add(lblDescricaoProduto);

		lblValorUnitario = new JLabel();

		lblValorUnitario.setBorder(new TitledBorder(
				ConstantesEnum.VALOR_UNITARIO_LABEL.getValue().toString()));

		itemEstoquePnl.add(lblValorUnitario);

		return itemEstoquePnl;

	}

	private void preencheEspacosVazios(JLabel lblField, String tituloBorda) {

		char espaco = ' ';

		String espacos = "";

		for (int i = 0; i < tituloBorda.length() + 20; i++)

			espacos += espaco;

		lblField.setText(espacos);

	}

	private void populaFormulario(ItemEstoque item) {

		String codigo = item.getIdItemEstoque().toString() + getEspacos();

		String descricaoTipoRoupa = item.getProduto().getTipoProduto()
				.toString()
				+ getEspacos();

		String valorUnitario = ConstantesEnum.SIMBOLO_MONETARIO_BR.getValue()
				.toString()
				+ MyFormatador.formataStringDecimais(item.getProduto()
						.getValorUnitario()) + getEspacos();

		String quantidade = item.getQuantidade().toString();

		String descricaoRoupa = item.getProduto().getDescricaoProduto()
				+ getEspacos();

		preencheFormulario(codigo, descricaoTipoRoupa, descricaoRoupa,
				valorUnitario, quantidade);

	}

	private String getEspacos() {

		String espacos = "";

		for (int i = 0; i < 20; i++)

			espacos += ' ';

		return espacos;

	}

	private void preencheFormulario(String codigo, String descricaoTipoRoupa,
			String descricaoRoupa, String valorUnitario, String quantidade) {

		lblCodigo.setText(codigo);

		lblDescricaoTipoProduto.setText(descricaoTipoRoupa);

		lblValorUnitario.setText(valorUnitario);

		txtQuantidade.setText(quantidade);

		lblDescricaoProduto.setText(descricaoRoupa);

	}

	/**
	 * xxx
	 */
	public void actionPerformed(ActionEvent arg0) {

		String actionCommand = arg0.getActionCommand();

		try {
			selecionaAcao(actionCommand);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void excluiItem() throws Exception {

		if (controller.getItemEstoque() == null) {

			throw new Exception(
					"Ã‰ necessÃ¡rio selecionar um Ã�tem no Estoque antes de Excluir.");

		}

		int confirmacao = confirmaExclusaoItem();
		// JOptionPane.showConfirmDialog(null,
		// "Confirmar ExclusÃ£o", "Deseja realmente excluir este item?",
		// JOptionPane.YES_NO_OPTION);

		if (confirmacao == 0) {

			controller.apagaItem();

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

		if (actionCommand.equals(SALVAR_BUTTON_LBL)) {

			salvarItem();

		} else if (actionCommand.equals(EXCLUIR_BUTTON_LBL)) {

			excluiItem();

		}

	}

	@Override
	protected void salvarItem() throws Exception {
		// TODO Auto-generated method stub

		if (controller.getItemEstoque() == null) {

			throw new Exception(
					ConstantesEnum.ITEM_DO_ESTOQUE_EXCEPTION_SALVAMENTO
							.getValue().toString());

		} else {

			int quantidade = new Integer(txtQuantidade.getText());

			controller.atualizaItem(quantidade);

			dispose();

		}

	}

}
