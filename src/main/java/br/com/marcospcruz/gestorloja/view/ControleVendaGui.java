package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.util.Util;

public class ControleVendaGui extends AbstractDialog {
	//@formatter:off
	private static final Object[] COLUNAS_JTABLE = {
			"Marca / Fabricante",
			"Produto",
			"Quantidade",
			"Valor Unitário",
			"Valor Total"
	};
	private static final String UM = "1";
	//@formatter:on
	private JTextField txtCodigoProduto;
	private JTextField txtQuantidade;
	private VendaController vendaController;
	private JLabel codigoProdutoValue;
	private JLabel descricaoProdutoValue;
	private JLabel valorUnitarioValue;
	private JLabel fabricanteValue;
	private ItemEstoque itemEstoque;

	public ControleVendaGui(LoginFacade loginFacade, String tituloJanela, JFrame owner) throws Exception {
		super(owner, tituloJanela, true, loginFacade);
		vendaController = new VendaController();
		Font fontTahoma32 = new Font("Tahoma", Font.PLAIN, 32);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblCdigoDoProduto = new JLabel("C\u00F3digo do Produto");
		lblCdigoDoProduto.setFont(fontTahoma32);
		panel.add(lblCdigoDoProduto);

		txtCodigoProduto = new JTextField();

		txtCodigoProduto.setFont(fontTahoma32);
		panel.add(txtCodigoProduto);
		txtCodigoProduto.setColumns(20);

		JButton btnNewButton = new JButton("Buscar");
		btnNewButton.setFont(fontTahoma32);
		btnNewButton.addActionListener(this);
		panel.add(btnNewButton);
		// produto
		JPanel produto = new JPanel();
		Border titledBorder = new TitledBorder(null, "Produto:", TitledBorder.LEADING, TitledBorder.TOP, fontTahoma32,
				null);

		produto.setBorder(titledBorder);
		panel.add(produto);
		produto.setLayout(new GridLayout(0, 2));

		JLabel lblCdigoProduto = new JLabel("C\u00F3digo:");
		lblCdigoProduto.setFont(fontTahoma32);
		produto.add(lblCdigoProduto, "name_246661095553608");

		codigoProdutoValue = new JLabel("");
		codigoProdutoValue.setFont(fontTahoma32);
		codigoProdutoValue.setBackground(Color.WHITE);
		codigoProdutoValue.setForeground(Color.BLUE);
		produto.add(codigoProdutoValue);

		JLabel lblFabricanteMarca = new JLabel("Fabricante / Marca:");
		lblFabricanteMarca.setFont(fontTahoma32);
		produto.add(lblFabricanteMarca);

		fabricanteValue = new JLabel("");
		fabricanteValue.setForeground(Color.BLUE);
		fabricanteValue.setFont(fontTahoma32);
		produto.add(fabricanteValue);

		JLabel lblDescrio = new JLabel("Descri\u00E7\u00E3o:");
		lblDescrio.setFont(fontTahoma32);
		produto.add(lblDescrio, "name_246690503140716");

		descricaoProdutoValue = new JLabel("");
		descricaoProdutoValue.setForeground(Color.BLUE);
		descricaoProdutoValue.setFont(fontTahoma32);
		produto.add(descricaoProdutoValue);

		JLabel lblValorUnitrio = new JLabel("Valor Unit\u00E1rio:");
		lblValorUnitrio.setFont(fontTahoma32);
		produto.add(lblValorUnitrio);

		valorUnitarioValue = new JLabel("");
		valorUnitarioValue.setForeground(Color.BLUE);
		valorUnitarioValue.setFont(fontTahoma32);
		produto.add(valorUnitarioValue);

		JLabel lblQuantidade = new JLabel("Quantidade:");
		lblQuantidade.setFont(fontTahoma32);
		produto.add(lblQuantidade);

		txtQuantidade = new JTextField();
		txtQuantidade.setFont(fontTahoma32);
		txtQuantidade.setForeground(Color.BLUE);
		txtQuantidade.setText(UM);
		produto.add(txtQuantidade);
		txtQuantidade.setColumns(10);

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setFont(fontTahoma32);
		btnAdicionar.addActionListener(this);
		produto.add(btnAdicionar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(fontTahoma32);
		btnCancelar.addActionListener(this);
		produto.add(btnCancelar);
		// carrinho de compra
		jPanelTable = new JPanel();
		jPanelTable.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Carrinho de Compra",
				TitledBorder.LEADING, TitledBorder.TOP, fontTahoma32, new Color(0, 0, 0)));
		getContentPane().add(jPanelTable, BorderLayout.SOUTH);
		carregaTableModel();
		jTable = inicializaJTable();
		jScrollPane = new JScrollPane(jTable);
		jPanelTable.add(jScrollPane);

		setVisible(true);
	}

	JTable inicializaJTable() {
		JTable jTable = super.inicializaJTable();

		return jTable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			switch (e.getActionCommand()) {
			case "Cancelar":
				fabricanteValue.setText("");
				codigoProdutoValue.setText("");
				valorUnitarioValue.setText("");
				descricaoProdutoValue.setText("");
				txtQuantidade.setText(UM);
				break;
			case "Adicionar":
				System.out.println("Adicionar");
				if (itemEstoque != null) {
					Venda venda = new Venda();
					venda.setItemEstoque(itemEstoque);
					venda.setQuantidade(Integer.valueOf(txtQuantidade.getText()));
					if (verificaSacola(venda.getItemEstoque(), myTableModel.getLinhas()))
						throw new Exception("Produto já adicionado ao Carrinho!");

					adicionarItemCarrinho(venda);
					limpaProdutoTela();
					itemEstoque=null;
				}
				break;
			case "Buscar":
				String codigoProduto = txtCodigoProduto.getText();

				preencheProdutoInterface(codigoProduto);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} finally {
			atualizaTableModel();
			
		}
	}

	private boolean verificaSacola(ItemEstoque itemEstoque, List<Object[]> itensSacola) {
		return itensSacola.stream().anyMatch(item -> item[1].equals(itemEstoque.getProduto().getDescricaoProduto()));
	}

	private void limpaProdutoTela() {
		fabricanteValue.setText("");
		codigoProdutoValue.setText("");
		valorUnitarioValue.setText("");
		descricaoProdutoValue.setText("");

	}

	private void preencheProdutoInterface(String codigoProduto) throws Exception {
		itemEstoque = vendaController.buscaProduto(codigoProduto);
		fabricanteValue.setText(itemEstoque.getProduto().getFabricante().getNome());
		codigoProdutoValue.setText(itemEstoque.getProduto().getCodigoDeBarras());
		valorUnitarioValue.setText(Util.formataMoeda(itemEstoque.getProduto().getValorUnitario()));
		descricaoProdutoValue.setText(itemEstoque.getProduto().getDescricaoProduto());
	}

	private void atualizaTableModel() {
		List linhas = myTableModel.getLinhas();
		if (linhas == null)
			linhas = new ArrayList<>();
		super.carregaTableModel(linhas, COLUNAS_JTABLE);
		super.reloadJFrame();

	}

	private void adicionarItemCarrinho(Venda venda) throws Exception {
		vendaController.subtraiEstoque(venda);
		List linhas = myTableModel.getLinhas();
		if (linhas == null)
			linhas = new ArrayList<>();
		linhas.add(parseVenda(venda));
		carregaTableModel(linhas);
	}

	private Object parseVenda(Venda venda) {
		//@formatter:off
		String[] colunas= {
				venda.getItemEstoque().getProduto().getFabricante().getNome(),
				venda.getItemEstoque().getProduto().toString(),
				Integer.toString(venda.getQuantidade()),
				Util.formataMoeda(venda.getItemEstoque().getProduto().getValorUnitario()),
				Util.formataMoeda(venda.getItemEstoque().getProduto().getValorUnitario()*venda.getQuantidade()) 
			};
		//@formatter:on
		return colunas;
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
		carregaTableModel();

	}

	@Override
	protected void carregaTableModel() {
		List<ItemEstoque> linhas = new ArrayList<>();
		carregaTableModel(linhas);

	}

	/**
	 * Método responsável em passar linhas para o tableModel.
	 * 
	 * @param linhas
	 */
	@SuppressWarnings("rawtypes")
	protected void carregaTableModel(List linhas) {

		super.carregaTableModel(linhas, COLUNAS_JTABLE);
	}

	@Override
	protected List carregaLinhasTableModel(List lista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void excluiItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvarItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaFormulario() {
		// TODO Auto-generated method stub

	}

}
