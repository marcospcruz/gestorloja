package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;

public class CaixaController extends AbstractController {

	private static final String QUERY_BUSCA_TODOS = "caixa.findAll";
	private Crud<Caixa> dao = new CrudDao<>();
	private List<Caixa> caixaList;
	private Caixa caixa;

	@Override
	public void busca(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {

		return dao.busca(QUERY_BUSCA_TODOS);
	}

	@Override
	public List getList() {
		return buscaTodos();

	}

	@Override
	public void busca(String query) throws Exception {
		caixaList = dao.busca(query);
	}

	@Override
	public Object getItem() {
		if(caixa==null){
			try {
				busca("caixa.findCaixaAberto");
				caixa=caixaList.get(0);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return caixa;
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

	@Override
	public void salva(Object object) throws Exception {

		validateCaixaAberto();
		caixa = (Caixa) object;

		caixa=dao.update(caixa);

	}

	public void validateCaixaAberto() throws Exception {

		busca("caixa.findCaixaAberto");

		if (caixaList != null && !caixaList.isEmpty()) {
			throw new Exception("Há caixa aberto.");
		}

	}

}
