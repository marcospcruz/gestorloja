package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Operacao;

public class FabricanteController extends ControllerBase {
	private static final String FABRICANTE_INVALIDO = "Fabricante inválido!";
	// private Crud<Fabricante> fabricanteDao;
	private List<Fabricante> fabricantes;
	private Fabricante fabricante;
	private CrudDao<Fabricante> fabricanteDao;

	public FabricanteController() {
		super();
		fabricanteDao = new CrudDao<>();
	}

	@Override
	public void busca(Object id) throws Exception {

		fabricante = fabricanteDao.busca(Fabricante.class, new Integer(id.toString()));

	}

	@Override
	public List buscaTodos() {
		if (fabricantes == null || fabricantes.isEmpty()) {

			fabricantes = fabricanteDao.busca("fabricante.buscaTodos");
		}
		return getList();
	}

	@Override
	public List getList() {

		return fabricantes;
	}

	@Override
	public void busca(String text) throws Exception {
		// String nomeFabricante = "%"+text.toUpperCase()+"%";
		// fabricante = fabricanteDao.busca("fabricante.readParametroLike", "nome",
		// nomeFabricante);
		int id = Integer.parseInt(text);
		busca(id);
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
		fabricante = fabricanteDao.update(fabricante);
		if (!fabricante.getItensEstoque().isEmpty())
			throw new Exception("Exclusão inválida. Há " + fabricante.getItensEstoque().size()
					+ " ítens deste Fabricante / Marca no Estoque.");

		// busca(fabricante.getIdFabricante());

		try {
			// fabricante = fabricanteDao.busca(Fabricante.class,
			// fabricante.getIdFabricante());
			fabricanteDao.delete(fabricante);

		} catch (IllegalArgumentException e) {

			throw new Exception("Falha ao excluir dados.");
		}

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

		fabricantes = new ArrayList<>();
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

	@Override
	public void novo() {
		fabricante = new Fabricante();
	}

	public void buscaNome(String nome) {

		fabricante = fabricanteDao.busca("fabricante.readNome", "nome", nome.toUpperCase());

	}

}
