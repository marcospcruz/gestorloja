package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class VendaController extends ControllerBase {
	public static final String PRODUTO_INVALIDO = "Selecão de Produto inválida.";
	private EstoqueController estoqueController;
	private Map<String, ItemVenda> itemVendaMap;

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
		// vendaDao = new CrudDao<>();
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
			estoqueController.buscaItemPorCodigoDeBarras(codigoProduto);

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
		if (!itemEstoque.isEstoqueDedutivel())
			return;
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

		if (venda.getItensVenda() == null || venda.getItensVenda().isEmpty())
			return new ArrayList<>();

		return new ArrayList<>(itemVendaMap.values());
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

		return venda;
	}

	@Override
	public void setList(List list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(Object object) {
		this.venda = (Venda) object;
		itemVendaMap = venda.getItensVenda().stream()
				.collect(Collectors.toMap(iv -> ((ItemVenda) iv).getItemEstoque().getCodigoDeBarras(), iv -> iv));
		// setCacheMap(null);

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
		try {
			String key = estoqueController.getItemEstoque().getCodigoDeBarras();

			return itemVendaMap.get(key);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setItemVenda(ItemVenda itemVenda) {
		// this.itemVenda = itemVenda;

	}

	public void resetItemVenda() {
		// setItemVenda(new ItemVenda());

		// itemVenda.setOperador(getUsuarioLogado());
		estoqueController.setItemEstoque(null);
		itemVendaBackup = null;
		// itemVendaMap = new HashMap<>();

	}

	public void devolveProduto() throws Exception {
		ItemVenda itemVenda = getItemVenda();
		ItemEstoque itemEstoque = itemVenda.getItemEstoque();
		// if (!itemEstoque.isEstoqueDedutivel())
		// return;
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

		if (itemEstoque.isEstoqueDedutivel())
			estoqueController.incrementaItem(itemVenda.getQuantidade());

		// float valorTotal = venda.getTotalVendido() - (item.getQuantidade() *
		// item.getItemEstoque().getValorUnitario());
		// venda.setTotalVendido(valorTotal);

		// resetItemVenda();

		List<ItemVenda> itensVenda = venda.getItensVenda();
		String codigoDeBarras = itemEstoque.getCodigoDeBarras();
		if (itemVendaBackup != null)
			itensVenda.add(itemVendaBackup);
		else {
			itemVendaMap.remove(codigoDeBarras);
			venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
		}
		calculaValorTotalVenda();
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void adicionaProdutoLista() throws Exception {
		fazBackup = true;
		ItemVenda itemVenda = getItemVenda();
		if (itemVenda == null || itemVenda.getItemEstoque() == null) {
			resetItemVenda();
			throw new NullPointerException(PRODUTO_INVALIDO);

		}
		if (itemVenda.getQuantidade() < 1)
			throw new Exception("Quantidade inválida.");
		try {
			if (venda.getItensVenda() != null && venda.getItensVenda().contains(itemVenda)) {

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
				venda.setItensVenda(new ArrayList<>());

			// itensVenda.add(itemVenda); alterar
			if (itemVenda.getItemEstoque() != null) {
				// venda.getItensVenda().add(itemVenda);
				itemVenda.setVenda(venda);
				itemVendaMap.put(itemVenda.getItemEstoque().getCodigoDeBarras(), itemVenda);
				venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
				calculaValorTotalVenda();
				resetItemVenda();
			}

		}

	}

	public void calculaValorTotalVenda() {
		float valorTotal = 0f;
		for (ItemVenda item : venda.getItensVenda()) {
			valorTotal += item.getQuantidade() * item.getItemEstoque().getValorUnitario();

			valorTotal -= valorTotal * venda.getPorcentagemDesconto() / 100;
		}
		venda.setTotalVendido(valorTotal);
	}

	private ItemVenda getElementList(ItemVenda itemVenda) {
		for (ItemVenda item : venda.getItensVenda()) {
			if (itemVenda.getItemEstoque().getCodigoDeBarras().equals(item.getItemEstoque().getCodigoDeBarras())) {
				return item;
			}
		}

		return null;

	}

	public void setItemVendaBackup() {
		// if (fazBackup && (venda.getItensVenda() != null &&
		// venda.getItensVenda().containsValue(itemVenda)
		// && itemVendaBackup == null || itemVendaBackup != null &&
		// !itemVenda.equals(itemVendaBackup))) {
		itemVendaBackup = new ItemVenda();
		ItemVenda itemVenda = getItemVenda();
		itemVendaBackup.setItemEstoque(itemVenda.getItemEstoque());
		itemVendaBackup.setQuantidade(itemVenda.getQuantidade());
		itemVendaBackup.setOperador(itemVenda.getOperador());

		// }

	}

	public void populaItemEstoque(ItemEstoque itemEstoque) throws ProdutoNaListaException {
		String key = itemEstoque.getCodigoDeBarras();
		estoqueController.setItem(itemEstoque);
		// if (venda.getItensVenda() != null &&
		// venda.getItensVenda().contains(itemEstoque.getCodigoDeBarras())) {
		// estoqueController.anulaAtributos();
		// throw new ProdutoNaListaException("Produto já adicionado na lista de
		// vendas.");
		//
		// }
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setItemEstoque(itemEstoque);
		itemVenda.setQuantidade(1);

		itemVenda.getItemEstoque()
				.setQuantidade(itemVenda.getItemEstoque().getQuantidade() - itemVenda.getQuantidade());
		itemVenda.setValorVendido(itemEstoque.getValorUnitario());
		if (itemVendaMap == null)
			itemVendaMap = new HashMap<>();
		itemVendaMap.put(key, itemVenda);

	}

	public void deduzEstoqueProduto(int quantidadeItem) throws Exception {
		ItemVenda itemVenda = getItemVenda();
		if (!itemVenda.getItemEstoque().isEstoqueDedutivel())
			return;
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
		for (ItemVenda item : venda.getItensVenda())
			valor += (item.getQuantidade() * item.getItemEstoque().getValorUnitario());
		return valor;
	}

	public void resetVenda() {
		venda = new Venda();
		itemVendaMap = new HashMap<>();

	}

	public void finalizaVenda() throws Exception {
		// if (venda.getIdVenda() != 0)
		// venda = vendaDao.busca(Venda.class, venda.getIdVenda());
		if (venda.getItensVenda().isEmpty()) {
			throw new Exception("Lista de Produtos vazia!");
		}
		venda.setDataVenda(SingletonManager.getInstance().getData());
		venda.setOperador(getUsuarioLogado());
		// venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
		recebePagamento();
		if (venda.getIdVenda() == 0)
			for (ItemVenda itemVenda : venda.getItensVenda()) {
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
				// salvaItemVenda(itemVenda);
			}
		if (venda.getCaixa() == null)
			venda.setCaixa((Caixa) caixaController.getItem());
		salva();

		iniciaVenda();
		estoqueController.anulaAtributos();

		itemVendaMap = null;
	}

	public void salva() {
		Crud<Venda> vendaDao = new CrudDao<>();
		venda = vendaDao.update(venda);
		venda.getPagamento().setVenda(venda);
		// caixaController.atualizaPagamento(venda.getPagamento());
		// caixaController.setItem(venda.getCaixa());
		try {
			caixaController.salva();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void recebePagamento() throws Exception {
		// TODO BUSCAR TIPOMEIOPAGAMENTO NO BANCO
		Pagamento pagamento = venda.getPagamento();
		Date dataVenda = venda.getDataVenda();
		pagamento.setdataVenda(dataVenda);
		Usuario operador = venda.getOperador();
		pagamento.setUsuarioLogado(operador);
		// caixaController.recebePagamento(pagamento);
		Caixa caixa = venda.getCaixa() == null ? (Caixa) caixaController.getItem() : venda.getCaixa();
		pagamento.getMeiosPagamento().stream().forEach(meioPagamento -> {
			meioPagamento.setDataPagamento(dataVenda);
			meioPagamento.setUsuarioLogado(operador);
			TipoMeioPagamento tipoMeioPagamento = meioPagamento.getTipoMeioPagamento();
			meioPagamento.setTipoMeioPagamento(caixaController.buscaTipoMeioPagamento(tipoMeioPagamento));
			// meioPagamento = meioPagamentoDao.update(meioPagamento);

			// pagamento.getMeiosPagamento().add(meioPagamento);
			meioPagamento.setPagamento(pagamento);
			if (meioPagamento.getIdMeioPagamento() == 0
					&& meioPagamento.getTipoMeioPagamento().getIdTipoMeioPagamento() == 1) {
				float saldoFinal = caixa.getSaldoFinal() + meioPagamento.getValorPago();
				caixa.setSaldoFinal(saldoFinal);
				// salva();
			}
		});
		venda.setPagamento(pagamento);
		caixaController.setItem(caixa);
	}

	private void salvaItemVenda(ItemVenda itemVenda) {
		itemVenda.setDataVenda(venda.getDataVenda());
		itemVenda.setOperador(venda.getOperador());
		itemVendaDao.update(itemVenda);

	}

	public void calculaDescontoProdutos() {
		float porcentagemDesconto = (float) venda.getPorcentagemDesconto() / 100;
		float valorTotalVenda = 0;
		for (ItemVenda itemVenda : venda.getItensVenda()) {
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

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	public void validaPagamento(Pagamento pagamento) {

		Pagamento pagamentoAtual = venda.getPagamento();
		// CrudDao<Pagamento> dao = new CrudDao<>();
		if (pagamentoAtual != null) {
			List<MeioPagamento> meiosPagamentoAtual = pagamentoAtual.getMeiosPagamento();
			meiosPagamentoAtual.stream().forEach(mp -> {
				if (mp.getTipoMeioPagamento().getIdTipoMeioPagamento() == 1) {
					if (venda.getCaixa() != null) {
						float valor = venda.getCaixa().getSaldoFinal() - mp.getValorPago();
						venda.getCaixa().setSaldoFinal(valor);
					}
				}
			});
			meiosPagamentoAtual.removeAll(meiosPagamentoAtual);

			// CrudDao<MeioPagamento> dao = new CrudDao<>();
			// meiosPagamentoAtual.stream().forEach(mp -> {
			//
			// mp = dao.busca(MeioPagamento.class, mp.getIdMeioPagamento());
			// if (mp != null)
			// dao.delete(mp);
			// });
			// meiosPagamentoAtual.removeAll(meiosPagamentoAtual);
			float valorTotal = 0;
			for (MeioPagamento novoMp : pagamento.getMeiosPagamento()) {

				// MeioPagamento mp = meiosPagamentoAtual.stream()
				// .filter(p ->
				// novoMp.getTipoMeioPagamento().equals(p.getTipoMeioPagamento())).findAny()
				// // .orElse(novoMp)
				// .orElse(null);
				// if (mp != null) {
				// mp.setValorPago(mp.getValorPago());
				// mp.setDescricao(novoMp.getDescricao());
				// mp.setParcelas(novoMp.getParcelas());
				// } else if (mp == null) {
				// mp = novoMp;
				novoMp.setPagamento(pagamentoAtual);
				meiosPagamentoAtual.add(novoMp);

				valorTotal += novoMp.getValorPago();
				// if
				// (novoMp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento().equals("Dinheiro"))
				// {
				// float valor = venda.getCaixa().getSaldoFinal() + novoMp.getValorPago();
				// venda.getCaixa().setSaldoFinal(valor);
				// }
				// meiosPagamentoAtual.add(novoMp);
				// }
				// boolean isMesmoPagamento = mp != null && mp.equals(novoMp);
				// mp.getValorPago() == novoMp.getValorPago();
				// isMesmoPagamento = mp.getDescricao() == novoMp.getDescricao()
				// && mp.getDescricao().equals(novoMp.getDescricao())
				// ;
				// (&& mp.getDescricao().equals(novoMp.getDescricao()))
				// if (!isMesmoPagamento) {
				// meiosPagamentoAtual.remove(mp);
				//// meiosPagamentoAtual.add(novoMp)
				// }

				//// if (!pagtoOk) {
				// Crud<Pagamento> dao = new CrudDao<>();
				// dao.delete(pagamentoAtual);
				// venda.setPagamento(pagamento);
				// break;
				// }

			}
			pagamentoAtual.setValorPagamento(valorTotal);
		}
		if (venda.getIdVenda() == 0)
			venda.setPagamento(pagamento);

	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((caixaController == null) ? 0 :
	// caixaController.hashCode());
	// result = prime * result + ((estoqueController == null) ? 0 :
	// estoqueController.hashCode());
	// result = prime * result + (fazBackup ? 1231 : 1237);
	// result = prime * result + ((itemVendaBackup == null) ? 0 :
	// itemVendaBackup.hashCode());
	// result = prime * result + ((itemVendaDao == null) ? 0 :
	// itemVendaDao.hashCode());
	// result = prime * result + ((itemVendaMap == null) ? 0 :
	// itemVendaMap.hashCode());
	// result = prime * result + ((venda == null) ? 0 : venda.hashCode());
	// result = prime * result + ((vendaDao == null) ? 0 : vendaDao.hashCode());
	// return result;
	// }

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// VendaController other = (VendaController) obj;
	// if (caixaController == null) {
	// if (other.caixaController != null)
	// return false;
	// } else if (!caixaController.equals(other.caixaController))
	// return false;
	// if (estoqueController == null) {
	// if (other.estoqueController != null)
	// return false;
	// } else if (!estoqueController.equals(other.estoqueController))
	// return false;
	// if (fazBackup != other.fazBackup)
	// return false;
	// if (itemVendaBackup == null) {
	// if (other.itemVendaBackup != null)
	// return false;
	// } else if (!itemVendaBackup.equals(other.itemVendaBackup))
	// return false;
	// if (itemVendaDao == null) {
	// if (other.itemVendaDao != null)
	// return false;
	// } else if (!itemVendaDao.equals(other.itemVendaDao))
	// return false;
	// if (itemVendaMap == null) {
	// if (other.itemVendaMap != null)
	// return false;
	// } else if (!itemVendaMap.equals(other.itemVendaMap))
	// return false;
	// if (venda == null) {
	// if (other.venda != null)
	// return false;
	// } else if (!venda.equals(other.venda))
	// return false;
	// if (vendaDao == null) {
	// if (other.vendaDao != null)
	// return false;
	// } else if (!vendaDao.equals(other.vendaDao))
	// return false;
	// return true;
	// }

	public void buscaVenda(Venda venda) {
		Crud<Venda> dao = new CrudDao<>();
		setItem(dao.busca(Venda.class, venda.getIdVenda()));

	}

	@Override
	public void carregaCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validaExclusaoItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
