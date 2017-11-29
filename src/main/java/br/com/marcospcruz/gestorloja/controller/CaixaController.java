package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Caixa;

public class CaixaController extends AbstractController {

	private Crud<Caixa> dao = new CrudDao<>();
	private List<Caixa> caixaList;

	@Override
	public void busca(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {

		return dao.busca("caixa.findAll");
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
		// TODO Auto-generated method stub
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

	@Override
	public void salva(Object object) throws Exception {

		validateCaixaAberto();
		Caixa caixa = (Caixa) object;

		dao.update(caixa);

	}

	public void validateCaixaAberto() throws Exception {

		busca("caixa.findCaixaAberto");

		if (caixaList != null && !caixaList.isEmpty()) {
			throw new Exception("Há caixa aberto.");
		}

	}

}
