package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.TipoProdutoNotFoundException;

public class FabricanteController extends AbstractController {
	private static final String FABRICANTE_INVALIDO = "Fabricante inválido!";
	private Crud<Fabricante> fabricanteDao;
	private List<Fabricante> fabricantes;
	private Fabricante fabricante;

	public FabricanteController() {
		fabricanteDao = new CrudDao<>();
	}

	public List<Fabricante> buscaTodos() {

		setList(fabricanteDao.busca("fabricante.buscaTodos"));

		return getList();
	}

	@Override
	public void setList(List fabricantes) {
		this.fabricantes = fabricantes;

	}

	public List<Fabricante> getList() {
		if (fabricantes == null)
			buscaTodos();
		return fabricantes;
	}

	public void busca(String parametro) throws Exception {

		zeraAtributos();

		if (parametro.length() == 0) {

			throw new Exception(BUSCA_INVALIDA);

		}

		// if (contemAcentuacao(parametro)) {
		//
		// buscaInWorkAround(parametro);
		//
		// } else {

		String valor = "%" + parametro.toUpperCase() + "%";

		fabricantes = fabricanteDao.buscaList("fabricante.readParametroLike", "nome", valor);

		// }

		if (fabricantes.size() >= 1)

			fabricante = fabricantes.get(0);

		else if (fabricantes.size() == 0)

			throw new TipoProdutoNotFoundException(ConstantesEnum.FABRICANTE_NAO_ENCONTRADO.getValue().toString());

	}

	private void zeraAtributos() {
		setItem(null);
		setList(null);
	}

	public Fabricante getItem() {

		return fabricante;
	}

	public void busca(int idFabricante) {

		fabricante = fabricanteDao.busca(Fabricante.class, idFabricante);

	}

	public void excluir() throws Exception {
		
		if(!fabricante.getProdutos().isEmpty()) {
			throw new Exception("Exclusão não permitida! Há produtos cadastrados para este fabricante.");
		}

		fabricanteDao.delete(fabricante);

		zeraAtributos();

	}

	public void salva(String nome, boolean b, Object object) throws Exception {

		fabricante = new Fabricante();
		fabricante.setNome(nome);

		validaNovoFabricante();

		fabricanteDao.update(fabricante);

	}

	private void validaNovoFabricante() throws Exception {

		if (fabricante.getNome().length() == 0) {
			throw new Exception(FABRICANTE_INVALIDO);
		}

	}

	@Override
	public void setItem(Object object) {

		this.fabricante = (Fabricante) object;
	}

	@Override
	public void salva(Object object) throws Exception {

		// busca(((Fabricante) object).getNome());

		if (fabricante != null) {
			fabricante.setNome(((Fabricante) object).getNome());
		} else {
			fabricante = (Fabricante) object;
		}
		fabricante.setOperador(getLoginFacade().getUsuarioLogado());

		// validaNovoFabricante();

		fabricanteDao.update(fabricante);

	}

}
