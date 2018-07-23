package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Operacao;

public class FabricanteController extends ControllerBase {
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
		// fabricanteDao = new CrudDao<>();
		buscaTodos();
		fabricante = (Fabricante) getCacheMap().get(Integer.parseInt(id.toString()));
		// fabricante = fabricanteDao.busca(Fabricante.class, new
		// Integer(id.toString()));

	}

	@Override
	public List buscaTodos() {

		Map<Object, Object> cache = getCacheMap();
		if (cache == null || cache.isEmpty()) {
			List<Fabricante> f = fabricanteDao.busca("fabricante.buscaTodos");
			cache = f.stream()
					.collect(Collectors.toMap(fabricante -> fabricante.getIdFabricante(), fabricante -> fabricante));
			setCacheMap(cache);
		}
		if (fabricantes == null || fabricantes.isEmpty())
			fabricantes = new ArrayList(cache.values());
		return null;
	}

	@Override
	public List getList() {
		if (fabricantes == null)
			buscaTodos();
		return fabricantes;
	}

	@Override
	public void busca(String text) throws Exception {
		fabricante = null;
		String nomeFabricante = "%" + text.toUpperCase() + "%";
		// fabricante = fabricanteDao.busca("fabricante.readParametroLike", "nome",
		// nomeFabricante);

		if (getCacheMap().isEmpty()) {
			buscaTodos();
		}
		Map<Object, Object> cache = getCacheMap();
		fabricantes = new ArrayList(cache.values().stream()
				// .filter(fabricante -> text.equalsIgnoreCase(((Fabricante)
				// fabricante).getNome()))
				.filter(fabricante -> ((Fabricante) fabricante).getNome().toUpperCase().contains(text.toUpperCase()))
				.collect(Collectors.toList()));

		if (fabricantes.size() == 1) {
			fabricante = fabricantes.get(0);
		}

		if (fabricantes.isEmpty()) {
//			fabricante = new Fabricante();
			throw new Exception("Fabricante / Marca não encontrado.");
		}
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
		buscaTodos();
		Map<Object, Object> cacheMap = getCacheMap();
		cacheMap.remove(fabricante.getIdFabricante());
		fabricante = fabricanteDao.busca(Fabricante.class, fabricante.getIdFabricante());
		fabricanteDao.delete(fabricante);
		fabricante = new Fabricante();
		fabricantes = null;
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
		getCacheMap().put(fabricante.getIdFabricante(), fabricante);
		EstoqueController estoqueController = (EstoqueController) getController(ControllerAbstractFactory.ESTOQUE);
		estoqueController.setCacheMap(null);
		fabricantes = null;
	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validaExistente(String text) throws Exception {
		fabricantes = new ArrayList(getCacheMap().values().stream()
				.filter(fab -> ((Fabricante) fab).getNome().equals(text)).collect(Collectors.toList()));
		if (!fabricantes.isEmpty())
			throw new Exception("Marca / Fabricante já cadastrado.");

	}

	@Override
	public void carregaCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validaExclusaoItem() {
		try {
			fabricante = fabricanteDao.busca(Fabricante.class, fabricante.getIdFabricante());
			if (!fabricante.getItensEstoque().isEmpty())
				return "Existem " + fabricante.getItensEstoque().size()
						+ " ítens cadastrados no estoque. A remoção dele resultará em perda de dados. Deseja continuar?";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
