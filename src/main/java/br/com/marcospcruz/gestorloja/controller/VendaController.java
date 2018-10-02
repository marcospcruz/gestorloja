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
	private Crud<Venda> vendaDao;
	private Crud<ItemVenda> itemVendaDao;
	private Venda venda;
	private ItemVenda itemVendaBackup;
	private boolean fazBackup;
	private CaixaController caixaController;
	// private Map<String,ItemVenda> itensVendaMap;

	public VendaController() throws Exception {
		super();
		vendaDao = new CrudDao<>();
		estoqueController = (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
		caixaController = (CaixaController) getController(ControllerAbstractFactory.CONTROLE_CAIXA);
		resetVenda();
		// vendaDao = new CrudDao<>();
		itemVendaDao = new CrudDao<>();
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

	public void subtraiEstoque(int quantidade) throws Exception {

		ItemEstoque itemEstoque =
				// estoqueController.getItemEstoque() != null ?
				// estoqueController.getItemEstoque()
				estoqueController.getItemEstoque();
		if (!itemEstoque.isEstoqueDedutivel())
			return;

		boolean permiteVendaSemControlarEstoque = SingletonManager.getInstance().isPermiteVendaSemControlarEstoque();
		if (itemEstoque.getQuantidade() < quantidade && !permiteVendaSemControlarEstoque) {

			// devolveProduto(itemEstoque.getCodigoDeBarras(), quantidade);

			throw new EstoqueInsuficienteException(
					"Quantidade de ítens no estoque é insuficiente para a venda! Total em estoque: "
							+ itemEstoque.getQuantidade());
		}
		itemEstoque.setQuantidade(itemEstoque.getQuantidade() - quantidade);
		if (!permiteVendaSemControlarEstoque) {
			estoqueController.setItemEstoque(itemEstoque);

			estoqueController.salva();
		}
		// venda.setItemEstoque(itemEstoque);
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

		return new ArrayList<>(venda.getItensVenda());
	}

	@Override
	public void busca(String text) throws Exception {

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
		this.venda = (Venda) object;

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	public ItemVenda getItemVenda(String codigoBarras) {
		return itemVendaMap.get(codigoBarras);
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
		itemVendaMap = new HashMap<>();

	}

	public void devolveProduto(String selectedCodigoBarra, int quantidade) throws Exception {
		// ItemVenda itemVenda = getItemVenda();
		ItemVenda itemVenda;
		if (itemVendaMap != null && !itemVendaMap.isEmpty())

			itemVenda = itemVendaMap.get(selectedCodigoBarra);
		else
			itemVenda = venda.getItensVenda().stream()
					.filter(item -> item.getItemEstoque().getCodigoDeBarras().equals(selectedCodigoBarra)).findFirst()
					.orElse(null);
		if (itemVenda != null) {

			itemVenda.setQuantidade(itemVenda.getQuantidade() - quantidade);
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

			estoqueController.incrementaItem(quantidade);

			// float valorTotal = venda.getTotalVendido() - (item.getQuantidade() *
			// item.getItemEstoque().getValorUnitario());
			// venda.setTotalVendido(valorTotal);

			// resetItemVenda();

			List<ItemVenda> itensVenda = venda.getItensVenda();
			String codigoDeBarras = itemEstoque.getCodigoDeBarras();
			if (itemVendaBackup != null)
				itensVenda.add(itemVendaBackup);
			else {
				if (itemVenda.getQuantidade() == 0)
					itemVendaMap.remove(codigoDeBarras);
				venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
			}
			calculaValorTotalVenda();

		}
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void calculaValorTotalVenda() {
		float valorTotal = 0f;
		for (ItemVenda item : venda.getItensVenda()) {
			float valorItemVenda = item.getItemEstoque().getValorUnitario();
			// System.out.println(valorItemVenda);
			// valorTotal += valorItemVenda;

			float desconto = Float.isNaN(venda.getPorcentagemDesconto()) ? 0 : (venda.getPorcentagemDesconto() / 100f);
			float tmp = valorItemVenda * desconto;
			valorItemVenda -= tmp;
			item.setValorVendido(valorItemVenda);
			valorTotal += item.getQuantidade() * valorItemVenda;
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

	public void populaItemEstoque(ItemEstoque itemEstoque, int quantidade) throws Exception {
		String key = itemEstoque.getCodigoDeBarras();
		estoqueController.setItem(itemEstoque);
		if (itemVendaMap == null)
			resetItemVenda();
		ItemVenda itemVenda = itemVendaMap.get(key);
		//////////////////////////////////
		// if (venda.getItensVenda() != null &&
		// venda.getItensVenda().contains(itemEstoque.getCodigoDeBarras())) {
		// estoqueController.anulaAtributos();
		// throw new ProdutoNaListaException("Produto já adicionado na lista de
		// vendas.");
		//
		// }

		// if (itemVenda == null) {
		// itemVenda = new ItemVenda();
		// itemVenda.setItemEstoque(itemEstoque);
		// }
		// else if (itemVenda.getQuantidade() == quantidade) {
		// throw new ProdutoNaListaException("Produto já presente na lista com a
		// quantidade informada.");
		// }
		if (itemVenda == null)
			itemVenda = new ItemVenda();
		itemVenda.setItemEstoque(itemEstoque);

		itemVenda.setQuantidade(quantidade + itemVenda.getQuantidade());
		itemVenda.setVenda(venda);
		subtraiEstoque(quantidade);

		itemVenda.setValorVendido(itemEstoque.getValorUnitario());

		if (itemVenda.getQuantidade() > 0)
			itemVendaMap.put(key, itemVenda);

		venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
		calculaValorTotalVenda();
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
		venda = null;
		venda = new Venda();
		venda.setOperador(getUsuarioLogado());
		venda.setCaixa((Caixa) caixaController.getItem());

		resetItemVenda();

		itemVendaMap = new HashMap<>();

	}

	public void finalizaVenda() throws Exception {
		// if (venda.getIdVenda() != 0)
		// venda = vendaDao.busca(Venda.class, venda.getIdVenda());
		if (venda.getItensVenda().isEmpty()) {
			throw new Exception("Lista de Produtos vazia!");
		}

		// venda.setDataVenda(SingletonManager.getInstance().getData());
		venda.setOperador(getUsuarioLogado());
		// venda.setItensVenda(new ArrayList<>(itemVendaMap.values()));
		recebePagamento();
		if (venda.getIdVenda() == 0)
			for (ItemVenda itemVenda : venda.getItensVenda()) {
				itemVenda.setOperador(getUsuarioLogado());
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
		salva();
		// SingletonManager.getInstance().getLogger(getClass())
		// .info("Total de Vendas salvas no Caixa: " +
		// venda.getCaixa().getVendas().size());
		resetVenda();
		estoqueController.anulaAtributos();

		itemVendaMap = new HashMap<>();
	}

	public void salva() {

		venda = vendaDao.update(venda);
		// venda.getPagamento().setVenda(venda);
		// caixaController.atualizaPagamento(venda.getPagamento());
	}

	private void recebePagamento() throws Exception {
		// TODO BUSCAR TIPOMEIOPAGAMENTO NO BANCO
		Pagamento pagamento = venda.getPagamento();
		if (pagamento == null) {
			throw new NullPointerException("Pagamento não informado para finalizar a Venda.");
		}
		Date dataVenda = venda.getDataVenda();
		pagamento.setVenda(venda);
		pagamento.setDataVenda(dataVenda);
		Usuario operador = venda.getOperador();
		pagamento.setUsuarioLogado(operador);

		if (pagamento.getMeiosPagamento() == null || pagamento.getMeiosPagamento().isEmpty())
			throw new Exception("Cobrar a venda antes de finalizar.");
		// pagamento.getMeiosPagamento().stream().forEach(meioPagamento -> {
		for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
			float valorPagamento = meioPagamento.getValorPago() + pagamento.getValorPagamento();
			pagamento.setValorPagamento(valorPagamento);
			meioPagamento.setDataPagamento(dataVenda);
			meioPagamento.setUsuarioLogado(operador);
			TipoMeioPagamento tipoMeioPagamento = meioPagamento.getTipoMeioPagamento();
			tipoMeioPagamento = caixaController.buscaTipoMeioPagamento(tipoMeioPagamento);
			meioPagamento.setTipoMeioPagamento(tipoMeioPagamento);
			meioPagamento.setPagamento(pagamento);
			// meioPagamento = meioPagamentoDao.update(meioPagamento);

			// pagamento.getMeiosPagamento().add(meioPagamento);
			// meioPagamento.setPagamento(pagamento);
			if (meioPagamento.getIdMeioPagamento() == 0
					&& meioPagamento.getTipoMeioPagamento().getIdTipoMeioPagamento() == 1) {

				caixaController.geraReceitaDeVendaCaixa(meioPagamento, venda.getDataVenda());

			}
		}
		venda.setPagamento(pagamento);

	}

	private void salvaItemVenda(ItemVenda itemVenda) {
		itemVenda.setDataVenda(venda.getDataVenda());
		itemVenda.setOperador(venda.getOperador());
		itemVendaDao.update(itemVenda);

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
		this.venda = dao.busca(Venda.class, venda.getIdVenda());

	}

	public Map<String, ItemVenda> getItemVendaMap() {
		return itemVendaMap;
	}

	@Override
	public void novo() {
		// TODO Auto-generated method stub

	}

	public float getValorTotalPagamentoVenda() {
		float valorPagamentoVenda = 0;
		Crud<Pagamento> dao = new CrudDao<>();
		try {
			Pagamento pagamento = venda.getPagamento();
			if (pagamento.getIdPagamento() != 0)
				pagamento = dao.update(pagamento);
			for (MeioPagamento meio : pagamento.getMeiosPagamento())
				valorPagamentoVenda += meio.getValorPago();
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(getClass()).warn(e);
		}

		return valorPagamentoVenda;
	}

	public float getValorBrutoVenda() {
		float total = 0;
		for (ItemVenda item : venda.getItensVenda()) {
			total += item.getQuantidade() * item.getItemEstoque().getValorUnitario();
		}
		return total;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;

	}

	public void estornaVenda() throws Exception {
		Caixa caixa = venda.getCaixa();
		Pagamento pagamento = venda.getPagamento();
		for (MeioPagamento mp : pagamento.getMeiosPagamento()) {
			TipoMeioPagamento tp = mp.getTipoMeioPagamento();
			if (tp.getIdTipoMeioPagamento() == 1) {
				float saldoCaixa = caixa.getSaldoFinal() - mp.getValorPago();
				caixa.setSaldoFinal(saldoCaixa);
			}
			mp.setEstornado(true);
		}
		pagamento.setEstornado(true);
		// pagamento.setValorPagamento(0);
		for (ItemVenda itemVenda : venda.getItensVenda()) {
			itemVenda.setDevolvido(true);
			devolveProduto(itemVenda.getItemEstoque().getCodigoDeBarras(), itemVenda.getQuantidade());

		}

		venda.setEstornado(true);
		salva();
		resetVenda();
	}

	public Pagamento getPagamentoVenda() {
		Pagamento pagamento = venda.getPagamento();
		if (pagamento != null && pagamento.getIdPagamento() != 0)
			pagamento = new CrudDao<Pagamento>().update(pagamento);
		return pagamento;
	}
}
