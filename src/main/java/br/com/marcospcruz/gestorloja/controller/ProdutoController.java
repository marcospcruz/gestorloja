package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ProdutoController extends ControllerBase {

	private static final String RESULTADO_NAO_ENCONTRADO = "Produto n�o encontrado";

	private List<Produto> produtos;

	private Produto produto;

	private Crud<Produto> produtoDao;

	private boolean validaDados = true;

	public ProdutoController() {

		produtoDao = new CrudDao<>();

		produto = new Produto();

	}

	public List<Produto> getList() {
		return produtos;

	}

	public Produto getItem() {
		return produto;
	}

	public void setItem(Object produto) {
		if (produto instanceof String) {
			this.produto = new Produto(produto.toString());
			return;
		}
		// else if (produto instanceof SubTipoProduto) {
		// this.produto = new Produto();
		// TODO

		// this.produto.getTiposProduto().add((SubTipoProduto) produto);
		// } else
		if (produto instanceof Produto)
			this.produto = (Produto) produto;
	}

	public Object[] getArrayTiposProduto() {

		TipoProdutoController tipoProdutoController = null;
		List<TipoProduto> listaTipos = null;
		try {
			tipoProdutoController = (TipoProdutoController) getController(
					ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);

			listaTipos = tipoProdutoController.getList();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return listaTipos.toArray();

	}

	// private void merge(Object produto) {
	// this.produto.setDescricaoProduto(((Produto) produto).getDescricaoProduto());
	// this.produto.setTipoProduto(((Produto) produto).getTipoProduto());
	// this.produto.setUnidadeMedida(((Produto) produto).getUnidadeMedida());
	// this.produto.setValorUnitario(((Produto) produto).getValorUnitario());
	// this.produto.setCodigoDeBarras(((Produto) produto).getCodigoDeBarras());
	// }

	// private void valida(Produto produto) throws Exception {
	//
	// if (produto.getDescricaoProduto() == null || produto.getValorUnitario() ==
	// 0f) {
	//
	// throw new Exception("Necess�rio preencher todos os campos.");
	//
	// }
	//
	// if (produto.getCodigoDeBarras() == null) {
	// throw new Exception("Necess�rio cadastrar o C�digo de Barras.");
	// }
	//
	// }

	public void busca(Object object) {
		Integer id = (Integer) object;
		produto = produtoDao.busca(Produto.class, id);

	}

//@formatter:off
	public void busca(String parametro) throws Exception {
		buscaTodos();
		Map<Object, Object> cache = getCacheMap();
		produtos = new ArrayList(
				cache.values()
				.stream()
				.filter(p -> ((Produto) p).getDescricaoProduto().toUpperCase().contains(parametro.toUpperCase()))
				.collect(Collectors.toList()));
		if (produtos.size() == 1) {
			produto = produtos.get(0);
		}else if(produtos.isEmpty()) {
			produtos=new ArrayList(cache.values());
			throw new Exception("Produto n�o encontrado.");
		}

	}
	//@formatter:on
	// public void busca(String parametro) throws Exception {
	// buscaTodos();
	// Map<Object, Object> cache = getCacheMap();
	//
	// setItem(null);
	// setList(null);
	//
	// if (contemAcentuacao(parametro)) {
	//
	// buscaInWorkAround(parametro);
	//
	// } else {
	//
	// String valor = "%" + parametro.trim().toUpperCase() + "%";
	//
	// produtos = produtoDao.buscaList("produto.readparametrolike", "descricao",
	// valor);
	// }
	//
	// if (produtos.size() >= 1)
	//
	// produto = produtos.get(0);
	//
	// else if (produtos.size() == 0)
	//
	// throw new Exception(RESULTADO_NAO_ENCONTRADO);
	//
	// }

	private void buscaInWorkAround(String parametro) {

		List<Produto> tmp = produtoDao.busca("produto.readall");

		produtos = new ArrayList<>();

		for (Produto peca : tmp) {

			if (peca.getDescricaoProduto().contains(parametro)) {

				produtos.add(peca);

			}

		}

	}

	@Override
	public List buscaTodos() {
		if (getCacheMap() == null || getCacheMap().isEmpty()) {
			produtos = produtoDao.busca("produto.readall");

			setCacheMap(produtos.stream().collect(Collectors.toMap(p -> ((Produto) p).getIdProduto(), p -> p)));
		} else {
			produtos = new ArrayList(getCacheMap().values());
		}
		return getList();
	}

	@Override
	public void excluir() throws Exception {

		getCacheMap().remove(produto.getIdProduto());
		produtoDao.delete(produto);

		produto = new Produto();
		produtos = null;
	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setList(List list) {

		this.produtos = list;
	}

	@Override
	public void salva() throws Exception {
		produto = produtoDao.update(produto);
		if (getCacheMap() != null) {
			getCacheMap().put(produto.getIdProduto(), produto);
			produtos = new ArrayList(getCacheMap().values());
		}

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void validaExistente(String text) throws Exception {
		if (text.isEmpty()) {
			throw new Exception("Descri��o de Produto Inv�lida.");
		}
		Produto novo = null;
		try {
			novo = produtoDao.busca("produto.readparametro", "descricao", text.toUpperCase());
		} catch (Exception e) {

		}
		if (produto.getIdProduto() == null && novo != null)
			throw new Exception("Produto j� cadastrado.");
	}

	@Override
	public void carregaCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validaExclusaoItem() {
		if (!produto.getEstoqueProduto().isEmpty()) {
			return "Produto cont�m " + produto.getEstoqueProduto().size() + " �tem(ns) no estoque relacionado a ele."
					+ " A exclus�o poder� causar perda de dados. Deseja continuar?";
		}
		return null;
	}

}
