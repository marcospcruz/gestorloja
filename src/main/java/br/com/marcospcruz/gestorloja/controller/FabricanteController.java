package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.TipoProdutoNotFoundException;

public class FabricanteController implements AbstractController {
	private static final String FABRICANTE_INVALIDO = "Fabricante inválido!";
	private Crud<Fabricante> fabricanteDao;
	private List<Fabricante> fabricantes;
	private Fabricante fabricante;

	public FabricanteController() {
		super();
		fabricanteDao = new CrudDao<>();
	}

	@Override
	public void busca(Object id) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getList() {
		if (fabricantes == null)
			fabricantes = fabricanteDao.busca("fabricante.buscaTodos");
		return fabricantes;
	}

	@Override
	public void busca(String text) throws Exception {
		String nomeFabricante = text;
		fabricante = fabricanteDao.busca("fabricante.readParametroLike", "nome", nomeFabricante);

	}

	@Override
	public Object getItem() {

		return fabricante;
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
		((Fabricante) object).setOperador(getUsuarioLogado());
		fabricante = fabricanteDao.update((Fabricante) object);

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void busca(String param1, String param2) {
		// TODO Auto-generated method stub

	}

}
