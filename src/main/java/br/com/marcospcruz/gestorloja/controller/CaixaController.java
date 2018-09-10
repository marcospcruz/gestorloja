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
import javafx.scene.control.Label;

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
		Crud<Caixa> dao = new CrudDao<>();
		caixa = dao.busca(Caixa.class, Integer.parseInt(id.toString()));

	}

	@Override
	public List buscaTodos() {

		Crud<Caixa> dao = getDao();
		caixaList = null;
		// if (caixaList == null || caixaList.isEmpty())
		caixaList = dao.busca(QUERY_BUSCA_TODOS);

		return caixaList;
	}

	private Crud<Caixa> getDao() {

		return new CrudDao<>();
	}

	@Override
	public List getList() {
		return caixaList;

	}

	@Override
	public void busca(String query) throws Exception {
		// Crud<Caixa> dao = getDao();
		// caixaList = dao.busca(query);
		caixa = caixaList.stream().filter(c -> {
			Date data = c.getDataAbertura();

			String formattedDate = Util.formataDataHora(data);
			boolean mesmaDataAbertura = query.equals(formattedDate);
			return mesmaDataAbertura;

		}).findFirst().orElse(null);

	}

	@Override
	public Object getItem() {

		try {
			if (caixa == null) {
				// busca(BUSCA_CAIXA_ABERTO);
				if (!caixaList.isEmpty())
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

	}

	public void validateCaixaAberto() throws Exception {
		//@formatter:off
		int size = caixaList.stream()
				.filter(caixa -> caixa.getDataFechamento() == null)
				.collect(Collectors.toList())
				.size();
		//@formatter:on
		if (size > 0) {
			throw new Exception("Caixa já aberto.");
		}
	}

	public void validateCaixaFechado() throws Exception {

		busca(BUSCA_CAIXA_ABERTO);

		if (caixaList == null || caixaList.isEmpty()) {
			throw new Exception("Não há caixa aberto.");
		}
		caixa = caixaList.get(0);
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
		for (Venda venda : caixa.getVendas()) {
			subTotalVendas += venda.getTotalVendido();
		}
		return subTotalVendas;
	}

	public List controcalculaSubTotalRecebido() {

		Set<Venda> vendas = caixa.getVendas();
		Map<TipoMeioPagamento, Float> subTotalRecebido = mapMeioPagamentosRecebidosCaixa(vendas);
		List linhas = new ArrayList();
		for (TipoMeioPagamento tm : subTotalRecebido.keySet()) {
			Object linha = new Object[] { tm.getDescricaoTipoMeioPagamento(),
					Util.formataMoeda(subTotalRecebido.get(tm)) };
			linhas.add(linha);
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

	@Override
	public void novo() {
		caixa = new Caixa();

	}

	public Float getTotalRecebidoCaixa() {

		float totalRecebido = 0;
		for (Venda venda : caixa.getVendas()) {
			if (venda.isEstornado())
				continue;
			Pagamento pagamento = venda.getPagamento();
			for (MeioPagamento meioPagamento : pagamento.getMeiosPagamento()) {
				totalRecebido += meioPagamento.getValorPago();

			}
			totalRecebido -= pagamento.getTrocoPagamento();
		}
		return totalRecebido;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Float> getSubTotaisMeiosPagamentoRecebidos() {
		Map<String, Float> subTotais = inicializaSubTotaisMap();

		Set<Venda> vendas = caixa.getVendas();

		for (Venda venda : vendas) {
			if (!venda.isEstornado())
				for (MeioPagamento meioPagamento : venda.getPagamento().getMeiosPagamento()) {
					String key = meioPagamento.getTipoMeioPagamento().getDescricaoTipoMeioPagamento();
					float value = subTotais.get(key) + meioPagamento.getValorPago();

					subTotais.put(key, value);
				}
		}
		return subTotais;
	}

	private Map<String, Float> inicializaSubTotaisMap() {
		Map<String, Float> map = new HashMap<>();
		Crud<TipoMeioPagamento> dao = new CrudDao<>();
		for (TipoMeioPagamento tipo : dao.busca("tipoMeioPagamento.buscaTodos")) {
			map.put(tipo.getDescricaoTipoMeioPagamento(), 0f);
		}
		return map;
	}

	public int getQuantidadeVendasEstornadas() {
		Set<Venda> vendas = caixa.getVendas();
		int qtVendas = vendas.stream().filter(venda -> venda.isEstornado()).collect(Collectors.toList()).size();

		return qtVendas;
	}

	public int getQuantidadeVendasEfetivadas() {

		return caixa.getVendas().size() - getQuantidadeVendasEstornadas();
	}

}
