package br.com.marcospcruz.gestorloja.controller;

import java.util.Date;
import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;

public class CaixaController extends AbstractController {

	private static final String QUERY_BUSCA_TODOS = "caixa.findAll";
	private static final String BUSCA_CAIXA_ABERTO = "caixa.findCaixaAberto";
	// private Crud<Caixa> dao = new CrudDao<>();
	private List<Caixa> caixaList;
	private Caixa caixa;

	@Override
	public void busca(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {
		Crud<Caixa> dao = getDao();
		if (caixaList == null)
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
		if (caixa == null) {
			try {
				busca(BUSCA_CAIXA_ABERTO);
				caixa = caixaList.get(0);

			} catch (Exception e) {

				e.printStackTrace();
			}
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
	public void salva(Object object) throws Exception {

		
		caixa = (Caixa) object;
		ajustaSaldoFinalZeroCaixa();
		Crud<Caixa> dao = getDao();
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
		caixa.setDataFechamento(new Date());
		caixa.setUsuarioFechamento(getLoginFacade().getUsuarioLogado());

		salva(caixa);
	}

	public void abreCaixa(String saldoAbertura) throws Exception {
		caixa = new Caixa();
		float saldoInicial = 0f;
		if (saldoAbertura!=null) {

			saldoInicial = Float.parseFloat(saldoAbertura);
		}
		caixa.setSaldoInicial(saldoInicial);
		caixa.setUsuarioAbertura(getLoginFacade().getUsuarioLogado());
		
		salva(caixa);

	}

}
