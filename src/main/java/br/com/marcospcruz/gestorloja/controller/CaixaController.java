package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Pagamento;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;

public class CaixaController extends ControllerBase {

	public CaixaController() {
		super();

	}

	private static final String QUERY_BUSCA_TODOS = "caixa.findAll";
	private static final String BUSCA_CAIXA_ABERTO = "caixa.findCaixaAberto";
	// private Crud<Caixa> dao = new CrudDao<>();
	private List<Caixa> caixaList;
	private Caixa caixa;

	private Pagamento pagamento;

	@Override
	public void busca(Object id) {
		Map cache = getCacheMap();
		if (cache.isEmpty()) {
			Crud<Caixa> dao = new CrudDao<>();
			caixa = dao.busca(Caixa.class, Integer.parseInt(id.toString()));
		} else {

		}

	}

	@Override
	public List buscaTodos() {
		Map<Object, Object> cacheMap = getCacheMap();
		// && cacheMap.isEmpty()
		if (cacheMap == null || cacheMap.isEmpty()) {
			Crud<Caixa> dao = getDao();
			caixaList = null;
			// if (caixaList == null || caixaList.isEmpty())
			caixaList = dao.busca(QUERY_BUSCA_TODOS);
			// setCacheMap(
			// caixaList.stream().collect(Collectors.toMap(caixa -> ((Caixa)
			// caixa).getIdCaixa(), caixa -> caixa)));
			if (caixaList != null && !caixaList.isEmpty()) {
				carregaCache();
			}
		} else {
			caixaList = new ArrayList(cacheMap.values());
		}
		return caixaList;
	}

	private Crud<Caixa> getDao() {

		return new CrudDao<>();
	}

	@Override
	public List getList() {
		return buscaTodos();

	}

	@Override
	public void busca(String query) throws Exception {
		Map<Object, Object> cacheMap = getCacheMap();
		if (cacheMap == null || cacheMap.isEmpty()) {
			Crud<Caixa> dao = getDao();
			caixaList = dao.busca(query);

			carregaCache();
		}

	}

	@Override
	public Object getItem() {

		try {
			if (caixa == null) {
				busca(BUSCA_CAIXA_ABERTO);
				caixa = caixaList.get(0);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return caixa;
	}

	private void ajustaSaldoFinalZeroCaixa() {
		if (caixa.getSaldoFinal() == 0f && caixa.getSaldoInicial() > 0f) {
			caixa.setSaldoFinal(caixa.getSaldoInicial());
		}

	}

	@Override
	public void setList(List list) {
		this.caixaList = list;

	}

	@Override
	public void setItem(Object object) {
		this.caixa = (Caixa) object;

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva() throws Exception {

		ajustaSaldoFinalZeroCaixa();

		Crud<Caixa> dao = new CrudDao<>();
		caixa = dao.update(caixa);
		Map<Object, Object> cacheMap = getCacheMap();
		cacheMap.put(caixa.getIdCaixa(), caixa);

	}

	public void validateCaixaAberto() throws Exception {
		Map<Object, Object> cache = getCacheMap();
		if (cache == null || cache.isEmpty())
			busca(BUSCA_CAIXA_ABERTO);
		caixa = (Caixa) cache.values().stream().filter(caixa -> ((Caixa) caixa).getDataFechamento() == null).findFirst()
				.orElse(null);
		// if (caixaList != null && !caixaList.isEmpty()) {
		if (caixa != null) {
			throw new Exception("Há caixa aberto.");
		}
	}

	public void validateCaixaFechado() throws Exception {

		// Map<Object, Object> cacheMap = getCacheMap();
		// if (cacheMap.isEmpty()) {
		// busca(BUSCA_CAIXA_ABERTO);
		// } // caixaList = new ArrayList(getCacheMap().values());
		// loadCaixasAbertosFromCache();
		// Caixa caixaAberto = (Caixa) cacheMap.values().stream()
		// .filter(caixa -> ((Caixa) caixa).getDataFechamento() ==
		// null).findAny().orElse(null);
		// // if (caixaList == null || caixaList.isEmpty()) {
		// // if (!cacheMap.containsKey("status_aberto")) {
		// if (caixaAberto == null) {
		// throw new Exception("Não há caixa aberto.");
		// }
		// caixa = caixaAberto;
		try {
			validateCaixaAberto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (caixa == null) {
			throw new Exception("Não há caixa aberto.");
		}
	}

	private void loadCaixasAbertosFromCache() {
		Map<Object, Object> cache = getCacheMap();
		caixaList = new ArrayList(cache.values().stream().filter(caixa -> ((Caixa) caixa).getDataFechamento() == null)
				.collect(Collectors.toList()));
	}

	public void fechaCaixa() throws Exception {
		caixa.setDataFechamento(SingletonManager.getInstance().getData());
		caixa.setUsuarioFechamento(getUsuarioLogado());

		salva();
	}

	public void abreCaixa(String saldoAbertura) throws Exception {
		caixa = new Caixa();
		float saldoInicial = 0f;
		if (saldoAbertura != null) {

			saldoInicial = Float.parseFloat(saldoAbertura);
		}
		caixa.setSaldoInicial(saldoInicial);
		caixa.setUsuarioAbertura(getUsuarioLogado());

		salva();

	}

	public Caixa getUltimoCaixa() {
		int ultimoIndice = getList().size() - 1;
		Caixa ultimoCaixa = null;
		if (ultimoIndice >= 0)
			ultimoCaixa = (Caixa) getList().get(ultimoIndice);
		return ultimoCaixa;
	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void recebePagamento(Pagamento pagamento) throws Exception {
		// meioPagamentoDao.update(pagamento);
		Date dataVenda = pagamento.getDataVenda();
		Usuario operador = pagamento.getUsuarioLogado();
		for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
			// for (int i = 0; i < pagamento.getMeiosPagamento().size(); i++) {
			// MeioPagamento meioPagamento = pagamento.getMeiosPagamento().remove(i);

		}
		// setPagamento(pagamentoDao.update(pagamento));
		setPagamento(pagamento);
	}

	public TipoMeioPagamento buscaTipoMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {
		CrudDao<TipoMeioPagamento> tipoMeioPagamentoDao = new CrudDao<>();
		return tipoMeioPagamentoDao.busca("tipoMeioPagamento.buscaPorDescricao", "descricao",
				tipoMeioPagamento.getDescricaoTipoMeioPagamento());
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;

	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void atualizaPagamento(Pagamento pagamento) {
		Crud<Pagamento> pagamentoDao = new CrudDao<>();
		pagamento = pagamentoDao.update(pagamento);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	public Float getSubTotalVendas() {
		float subTotalVendas = 0;
		Set<Venda> vendas = caixa.getVendas();
		if (vendas != null)
			for (Venda venda : vendas) {
				subTotalVendas += venda.getTotalVendido();
			}
		return subTotalVendas;
	}

	public List controcalculaSubTotalRecebido() {
		List linhas = new ArrayList();
		Set<Venda> vendas = caixa.getVendas();
		if (vendas != null) {
			Map<TipoMeioPagamento, Float> subTotalRecebido = mapMeioPagamentosRecebidosCaixa(vendas);

			for (TipoMeioPagamento tm : subTotalRecebido.keySet()) {
				Object linha = new Object[] { tm.getDescricaoTipoMeioPagamento(),
						Util.formataMoeda(subTotalRecebido.get(tm)) };
				linhas.add(linha);
			}
		}
		return linhas;
	}

	private Map<TipoMeioPagamento, Float> mapMeioPagamentosRecebidosCaixa(Set<Venda> vendas) {
		Map<TipoMeioPagamento, Float> subTotalRecebido = new HashMap<>();
		for (Venda venda : vendas) {
			Pagamento pagamento = venda.getPagamento();

			for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {

				TipoMeioPagamento tipoMeioPagamento = meioPagamento.getTipoMeioPagamento();
				float valorRecebido = subTotalRecebido.containsKey(tipoMeioPagamento)
						? subTotalRecebido.get(tipoMeioPagamento)
						: 0;
				valorRecebido += meioPagamento.getValorPago();

				subTotalRecebido.put(tipoMeioPagamento, valorRecebido);
			}
		}
		return subTotalRecebido;
	}

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	public boolean validaMesmoTipoPagamento(MeioPagamento mp, Pagamento pagamento) {
		if (pagamento.getMeiosPagamento().contains(mp))
			for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
				if (meioPagamento.getTipoMeioPagamento().getDescricaoTipoMeioPagamento()
						.equals(mp.getTipoMeioPagamento().getDescricaoTipoMeioPagamento())
						&& meioPagamento.getValorPago() == mp.getValorPago()) {
					return true;
				}
			}
		return false;
	}

//@formatter:off
	public Caixa getCaixaAberto() {
		 if(!getCacheMap().isEmpty())
		 {
			 Map<Object, Object> cache = getCacheMap();
			 caixa=(Caixa) cache.values()
					 .stream()
					 .filter(caixa->((Caixa)caixa).getDataFechamento()==null)
					 .findFirst()
					 .orElse(null);
		 }
		return caixa;
	}
//@formatter:on

	@Override
	public void carregaCache() {

		Map results = caixaList.stream().collect(Collectors.toMap(caixa -> caixa.getIdCaixa(), caixa -> caixa));
		setCacheMap(results);
	}

	@Override
	public String validaExclusaoItem() {
		// TODO Auto-generated method stub
		return null;
	}
}
