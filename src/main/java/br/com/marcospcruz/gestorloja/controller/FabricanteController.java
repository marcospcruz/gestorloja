package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Operacao;

public class FabricanteController implements ControllerBase {
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
		fabricanteDao = new CrudDao<>();
		fabricante = fabricanteDao.busca(Fabricante.class, new Integer(id.toString()));

	}

	@Override
	public List buscaTodos() {
		if (fabricantes == null || fabricantes.isEmpty())
			fabricantes = fabricanteDao.busca("fabricante.buscaTodos");
		return getList();
	}

	@Override
	public List getList() {

		return fabricantes;
	}

	@Override
	public void busca(String text) throws Exception {
		String nomeFabricante = "%"+text.toUpperCase()+"%";
		fabricante = fabricanteDao.busca("fabricante.readParametroLike", "nome", nomeFabricante);

	}

	@Override
	public Object getItem() {

		return fabricante;
	}

	@Override
	public void setList(List list) {
		fabricantes = list;

	}

	@Override
	public void setItem(Object object) {
		fabricante = (Fabricante) object;

	}

	@Override
	public void excluir() throws Exception {
		fabricanteDao.delete(fabricante);

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva() throws Exception {
		fabricante = fabricanteDao.update(fabricante);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validaExistente(String text) throws Exception {
		Fabricante novo = null;
		try {
			novo = fabricanteDao.busca("fabricante.readParametroLike", "nome", "%" + text.toUpperCase() + "%");
		} catch (Exception e) {

		}
		if (fabricante.getIdFabricante() == null && novo != null)
			throw new Exception("Marca / Fabricante já cadastrado.");

	}

}
