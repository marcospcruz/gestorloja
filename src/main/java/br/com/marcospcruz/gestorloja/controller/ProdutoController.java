package br.com.marcospcruz.gestorloja.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ProdutoController implements AbstractController {

	private static final String RESULTADO_NAO_ENCONTRADO = "Produto n�o encontrado";

	private List<Produto> produtos;

	private Produto produto;

	private Crud<Produto> produtoDao;

	public ProdutoController() {

		produtoDao = new CrudDao<>();

	}

	public List<Produto> getList() {

		if (produtos == null || produtos.size() == 0)

			carregaProdutos();

		return produtos;

	}

	private void carregaProdutos() {

		produtos = produtoDao.busca("produto.readall");

	}

	public Produto getItem() {
		return produto;
	}

	public void setItem(Object produto) {
		this.produto = (Produto) produto;
	}

	public Object[] getArrayTiposProduto() {

		TipoProdutoController tipoProdutoController = null;
		List<TipoProduto> listaTipos = null;
		try {
			tipoProdutoController = (TipoProdutoController) getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);

			listaTipos = tipoProdutoController.getList();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return listaTipos.toArray();

	}

	/**
	 * 
	 * @param tipoRoupa
	 * @param subTipoRoupa
	 * @param descricao
	 * @param unidadeMedida
	 * @param valorUnitario
	 * @throws Exception
	 */
	public void salva(Object produto) throws Exception {

		valida((Produto) produto);

		// busca(((Produto) produto).getDescricaoProduto());
		if (this.produto != null) {
			merge(produto);
			this.produto = produtoDao.update(this.produto);
		} else {
			((Produto) produto).setDataInsercao(new Date());
			this.produto = (Produto) produto;

		}
		this.produto.setOperador(getUsuarioLogado());

		produtoDao.update(this.produto);

		this.produto = null;

	}

	private void merge(Object produto) {
		this.produto.setDescricaoProduto(((Produto) produto).getDescricaoProduto());
		this.produto.setFabricante(((Produto) produto).getFabricante());
		this.produto.setTipoProduto(((Produto) produto).getTipoProduto());
		this.produto.setUnidadeMedida(((Produto) produto).getUnidadeMedida());
		this.produto.setValorUnitario(((Produto) produto).getValorUnitario());
		this.produto.setCodigoDeBarras(((Produto) produto).getCodigoDeBarras());
	}

	private void valida(Produto produto) throws Exception {

		if (produto.getDescricaoProduto() == null || produto.getValorUnitario() == 0f) {

			throw new Exception("Necess�rio preencher todos os campos.");

		}

		if (produto.getCodigoDeBarras() == null) {
			throw new Exception("Necess�rio cadastrar o C�digo de Barras.");
		}

	}

	public void busca(Object id) {
		int idPecaRoupa = Integer.valueOf(id.toString());
		produto = produtoDao.busca(Produto.class, idPecaRoupa);

	}

	public void busca(String parametro) throws Exception {

		setItem(null);
		setList(null);

		if (contemAcentuacao(parametro)) {

			buscaInWorkAround(parametro);

		} else {

			String valor = "%" + parametro.trim().toUpperCase() + "%";

			produtos = produtoDao.buscaList("produto.readparametrolike", "descricao", valor);
		}

		if (produtos.size() >= 1)

			produto = produtos.get(0);

		else if (produtos.size() == 0)

			throw new Exception(RESULTADO_NAO_ENCONTRADO);

	}

	private void buscaInWorkAround(String parametro) {

		List<Produto> tmp = produtoDao.busca("produto.readall");

		produtos = new ArrayList<Produto>();

		for (Produto peca : tmp) {

			if (peca.getDescricaoProduto().contains(parametro)) {

				produtos.add(peca);

			}

		}

	}

	@Override
	public List buscaTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluir() throws Exception {
		if (produto.getItemEstoque() != null) {

			throw new Exception(ConstantesEnum.EXCLUSAO_PRODUTO_COM_ITENS_ESTOQUE.getValue().toString());

		}

		produtoDao.delete(produto);

		produto = null;
	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setList(List list) {

		this.produtos = list;
	}

}
