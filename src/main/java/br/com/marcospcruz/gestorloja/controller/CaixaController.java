package br.com.marcospcruz.gestorloja.controller;

import java.util.Date;
import java.util.List;

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

public class CaixaController implements ControllerBase {

	public CaixaController() {
		super();
		meioPagamentoDao = new CrudDao<>();
		pagamentoDao = new CrudDao<>();
		tipoMeioPagamentoDao = new CrudDao<>();
	}

	private static final String QUERY_BUSCA_TODOS = "caixa.findAll";
	private static final String BUSCA_CAIXA_ABERTO = "caixa.findCaixaAberto";
	// private Crud<Caixa> dao = new CrudDao<>();
	private List<Caixa> caixaList;
	private Caixa caixa;
	private Crud<MeioPagamento> meioPagamentoDao;
	private Crud<Pagamento> pagamentoDao;
	private Pagamento pagamento;
	private Crud<Caixa> dao;
	private CrudDao<TipoMeioPagamento> tipoMeioPagamentoDao;

	@Override
	public void busca(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {
		if (dao == null)
			dao = getDao();
		caixaList=null;
		// if (caixaList == null || caixaList.isEmpty())
		caixaList = dao.busca(QUERY_BUSCA_TODOS);

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
		Crud<Caixa> dao = getDao();
		caixaList = dao.busca(query);
	}

	@Override
	public Object getItem() {

		try {
			busca(BUSCA_CAIXA_ABERTO);
			caixa = caixaList.get(0);

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

	@Override
	public void salva() throws Exception {

		ajustaSaldoFinalZeroCaixa();

		caixa = dao.update(caixa);

	}

	public void validateCaixaAberto() throws Exception {

		busca(BUSCA_CAIXA_ABERTO);

		if (caixaList != null && !caixaList.isEmpty()) {
			throw new Exception("Há caixa aberto.");
		}
	}

	public void validateCaixaFechado() throws Exception {

		busca(BUSCA_CAIXA_ABERTO);

		if (caixaList == null || caixaList.isEmpty()) {
			throw new Exception("Não há caixa aberto.");
		}
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
			meioPagamento.setDataPagamento(dataVenda);
			meioPagamento.setUsuarioLogado(operador);
			TipoMeioPagamento tipoMeioPagamento = meioPagamento.getTipoMeioPagamento();
			meioPagamento.setTipoMeioPagamento(buscaTipoMeioPagamento(tipoMeioPagamento));
			// meioPagamento = meioPagamentoDao.update(meioPagamento);
			if (meioPagamento.getTipoMeioPagamento().getIdTipoMeioPagamento() == 1) {
				float saldoFinal = caixa.getSaldoFinal() + meioPagamento.getValorPago();
				caixa.setSaldoFinal(saldoFinal);
				salva();
			}
			// pagamento.getMeiosPagamento().add(meioPagamento);
			meioPagamento.setPagamento(pagamento);

		}
		// setPagamento(pagamentoDao.update(pagamento));
		setPagamento(pagamento);
	}

	private TipoMeioPagamento buscaTipoMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {

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

}
