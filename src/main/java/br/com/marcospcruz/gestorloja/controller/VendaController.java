package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.exception.EstoqueInsuficienteException;
import br.com.marcospcruz.gestorloja.exception.ProdutoNaListaException;
import br.com.marcospcruz.gestorloja.facade.OperacaoEstoqueFacade;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.ItemVenda;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;

public class VendaController implements ControllerBase {
	public static final String PRODUTO_INVALIDO = "Selecão de Produto inválida.";
	private EstoqueController estoqueController;
	private ItemVenda itemVenda;
	private Crud<Venda> vendaDao;
	private Crud<ItemVenda> itemVendaDao;
	private Venda venda;
	private ItemVenda itemVendaBackup;
	private boolean fazBackup;
	private CaixaController caixaController;
	// private Map<String,ItemVenda> itensVendaMap;

	public VendaController() throws Exception {
		super();
		estoqueController = (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
		caixaController = (CaixaController) getController(ControllerAbstractFactory.CONTROLE_CAIXA);
		iniciaVenda();
		vendaDao = new CrudDao<>();
		itemVendaDao = new CrudDao<>();
	}

	private void iniciaVenda() {
		venda = new Venda();
		venda.setOperador(getUsuarioLogado());
		venda.setCaixa((Caixa) caixaController.getItem());
		
		resetItemVenda();

	}

	public void buscaProduto(String codigoProduto) throws Exception {

		try {
			// estoqueController.buscaItemPorCodigoDeBarras(codigoProduto);
			resetItemVenda();
			ItemVenda item = venda.getItensVenda().get(codigoProduto);
			itemVenda = new ItemVenda();
			itemVenda.setItemEstoque(item.getItemEstoque());
			itemVenda.setQuantidade(item.getQuantidade());
			itemVenda.setOperador(item.getOperador());
			setItemVenda(item);
			setItemVendaBackup();
		} catch (NoResultException e) {
			e.printStackTrace();
			// throw new Exception("Produto código " + codigoProduto + " não encontrado no
			// estoque.");
		}

	}

	public void subtraiEstoque(ItemVenda venda) throws Exception {
		ItemEstoque itemEstoque =
				// estoqueController.getItemEstoque() != null ?
				// estoqueController.getItemEstoque()
				venda.getItemEstoque();
		Integer quantidadeVenda = venda.getQuantidade();

		if (itemEstoque.getQuantidade() < 0) {

			devolveProduto();

			throw new EstoqueInsuficienteException("Quantidade de ítens no estoque é insuficiente para a venda!");
		}

		estoqueController.setItemEstoque(itemEstoque);

		estoqueController.salva();
		venda.setItemEstoque(itemEstoque);
		estoqueController.anulaAtributos();
	}

	@Override
	public void busca(Object id) throws Exception {
		try {
			fazBackup = true;
			buscaProduto(id.toString());
		} finally {
			fazBackup = false;
		}
	}

	@Override
	public List buscaTodos() {
		// estoqueController.carregaItensEstoque();
		return null;
	}

	@Override
	public List getList() {

		if (venda.getItensVenda() == null)
			return new ArrayList<>();

		return new ArrayList<>(venda.getItensVenda().values());
	}

	@Override
	public void busca(String text) throws Exception {
		String[] valueArray = text.split(" - ");
		String tipoProduto = valueArray[0];

		String produto = valueArray.length > 1 ? valueArray[1] : null;

		String fabricante = valueArray.length > 2 ? valueArray[2] : null;
		estoqueController.busca(tipoProduto, produto, fabricante);

	}

	@Override
	public Object getItem() {

		return null;
	}

	@Override
	public void setList(List list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	
	public ItemVenda getItemVenda() {

		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;

	}

	public void resetItemVenda() {
		setItemVenda(new ItemVenda());

		itemVenda.setOperador(getUsuarioLogado());
		itemVendaBackup = null;

	}

	public void devolveProduto() throws Exception {

		ItemEstoque itemEstoque = itemVenda.getItemEstoque();

		if (itemEstoque == null) {
			throw new Exception("Selecione um ítem na lista.");
		}
		// try {
		//// if (itemVenda.getItemEstoque() != null)
		//// estoqueController.busca(itemVenda.getItemEstoque());
		// } catch (NullPointerException e) {
		//
		// throw new Exception();
		// }
		estoqueController.setItemEstoque(itemEstoque);
		estoqueController.incrementaItem(itemVenda.getQuantidade());

		// float valorTotal = venda.getTotalVendido() - (item.getQuantidade() *
		// item.getItemEstoque().getValorUnitario());
		// venda.setTotalVendido(valorTotal);

		resetItemVenda();

		Map<String, ItemVenda> itensVenda = venda.getItensVenda();
		String codigoDeBarras = itemEstoque.getCodigoDeBarras();
		if (itemVendaBackup != null)
			itensVenda.put(codigoDeBarras, itemVendaBackup);
		else
			itensVenda.remove(codigoDeBarras);
		calculaValorTotalVenda();
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void adicionaVendaLista() throws Exception {
		fazBackup = true;

		if (itemVenda == null || itemVenda.getItemEstoque() == null) {
			resetItemVenda();
			throw new NullPointerException(PRODUTO_INVALIDO);

		}
		if (itemVenda.getQuantidade() < 1)
			throw new Exception("Quantidade inválida.");
		try {
			if (venda.getItensVenda() != null && venda.getItensVenda().containsValue(itemVenda)) {

				// ItemVenda item = procuraProdutoLista(itemVenda);
				// if (itensVenda.remove(itemVenda))
				// if (itemVendaBackup != null &&
				// itemVendaBackup.getItemEstoque().equals(itemVenda.getItemEstoque())
				// // && itemVendaBackup.getQuantidade() != itemVenda.getQuantidade()
				// ) {
				// estoqueController.incrementaItem(itemVendaBackup.getQuantidade());
				// itemVenda.setItemEstoque(estoqueController.getItemEstoque());
				// } else {
				// resetItemVenda();
				// return;
				// }
			}
			// if (venda.getItensVenda() != null && getElementList(itemVenda) != null
			// && getElementList(itemVenda).getQuantidade() != itemVenda.getQuantidade())
			subtraiEstoque(itemVenda);

		} catch (EstoqueInsuficienteException e) {

			itemVenda = itemVendaBackup != null ? itemVendaBackup : new ItemVenda();

			throw new EstoqueInsuficienteException(e.getMessage());
		} finally {

			if (venda.getItensVenda() == null)
				venda.setItensVenda(new HashMap<>());

			// itensVenda.add(itemVenda); alterar
			if (itemVenda.getItemEstoque() != null) {
				venda.getItensVenda().put(itemVenda.getItemEstoque().getCodigoDeBarras(), itemVenda);
				calculaValorTotalVenda();
				resetItemVenda();
			}

		}

	}

	public void calculaValorTotalVenda() {
		float valorTotal = 0f;
		for (ItemVenda item : venda.getItensVenda().values()) {
			valorTotal += item.getQuantidade() * item.getItemEstoque().getValorUnitario();

			valorTotal -= valorTotal * venda.getPorcentagemDesconto() / 100;
		}
		venda.setTotalVendido(valorTotal);
	}

	private ItemVenda getElementList(ItemVenda itemVenda) {

		return venda.getItensVenda().get(itemVenda.getItemEstoque().getCodigoDeBarras());
	}

	public void setItemVendaBackup() {
		// if (fazBackup && (venda.getItensVenda() != null &&
		// venda.getItensVenda().containsValue(itemVenda)
		// && itemVendaBackup == null || itemVendaBackup != null &&
		// !itemVenda.equals(itemVendaBackup))) {
		itemVendaBackup = new ItemVenda();
		itemVendaBackup.setItemEstoque(itemVenda.getItemEstoque());
		itemVendaBackup.setQuantidade(itemVenda.getQuantidade());
		itemVendaBackup.setOperador(itemVenda.getOperador());

		// }

	}

	public void populaItemEstoque(ItemEstoque itemEstoque) throws ProdutoNaListaException {
		if (venda.getItensVenda() != null && venda.getItensVenda().containsKey(itemEstoque.getCodigoDeBarras())) {
			estoqueController.anulaAtributos();
			throw new ProdutoNaListaException("Produto já adicionado na lista de vendas.");

		}
		itemVenda.setItemEstoque(itemEstoque);
		itemVenda.setQuantidade(1);
		itemVenda.getItemEstoque()
				.setQuantidade(itemVenda.getItemEstoque().getQuantidade() - itemVenda.getQuantidade());
		itemVenda.setValorVendido(itemEstoque.getValorUnitario());
	
	}

	public void deduzEstoqueProduto(int quantidadeItem) throws Exception {

		Integer quantidadeEstoque = itemVenda.getItemEstoque().getQuantidade() + itemVenda.getQuantidade()
				- quantidadeItem;
		if (quantidadeEstoque < 0) {
			// resetItemVenda();
			throw new Exception("Quantidade de ítens deste produto no Estoque é insuficiente para a venda.");

		}
		itemVenda.setQuantidade(quantidadeItem);
		itemVenda.getItemEstoque().setQuantidade(quantidadeEstoque);
	}

	public Venda getVenda() {

		return venda;
	}

	public float getSubTotal() {
		float valor = 0f;
		for (ItemVenda item : venda.getItensVenda().values())
			valor += (item.getQuantidade() * item.getItemEstoque().getValorUnitario());
		return valor;
	}

	public void resetVenda() {
		venda = new Venda();

	}

	public void finalizaVenda() throws Exception {
		venda.setDataVenda(new Date());
		venda.setOperador(getUsuarioLogado());
		recebePagamento();
		for (ItemVenda itemVenda : venda.getItensVenda().values()) {
			ItemEstoque novoItemEstoque = new ItemEstoque();
			ItemEstoque itemEstoque = itemVenda.getItemEstoque();
			novoItemEstoque.setIdItemEstoque(itemEstoque.getIdItemEstoque());
			novoItemEstoque.setCodigoDeBarras(itemEstoque.getCodigoDeBarras());
			novoItemEstoque.setDataContagem(itemEstoque.getDataContagem());
			novoItemEstoque.setFabricante(itemEstoque.getFabricante());
			novoItemEstoque.setOperador(itemEstoque.getOperador());
			novoItemEstoque.setProduto(itemEstoque.getProduto());
			novoItemEstoque.setQuantidade(itemVenda.getQuantidade());
			novoItemEstoque.setValorUnitario(itemVenda.getValorVendido());
			estoqueController.setItem(novoItemEstoque);
			estoqueController.registraHistoricoOperacao(OperacaoEstoqueFacade.SAIDA_ESTOQUE);
			salvaItemVenda(itemVenda);
		}
		salva();
		iniciaVenda();
	}

	public void salva() {
		venda = vendaDao.update(venda);
		venda.getPagamento().setVenda(venda);
		caixaController.atualizaPagamento(venda.getPagamento());
	}

	private void recebePagamento() throws Exception {

		Pagamento pagamento = venda.getPagamento();
		Date dataVenda = venda.getDataVenda();
		pagamento.setdataVenda(dataVenda);
		Usuario operador = venda.getOperador();
		pagamento.setUsuarioLogado(operador);
		caixaController.recebePagamento(pagamento);
		venda.setPagamento(pagamento);
	}

	private void salvaItemVenda(ItemVenda itemVenda) {
		itemVenda.setDataVenda(venda.getDataVenda());
		itemVenda.setOperador(venda.getOperador());
		itemVendaDao.update(itemVenda);

	}

	public void calculaDescontoProdutos() {
		float porcentagemDesconto = (float) venda.getPorcentagemDesconto() / 100;
		float valorTotalVenda = 0;
		for (ItemVenda itemVenda : venda.getItensVenda().values()) {
			float valorItemVenda = itemVenda.getItemEstoque().getValorUnitario();
			valorItemVenda -= valorItemVenda * porcentagemDesconto;
			itemVenda.setValorVendido(valorItemVenda);
			valorTotalVenda += valorItemVenda;
		}
		venda.setTotalVendido(valorTotalVenda);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub
		
	}

}
