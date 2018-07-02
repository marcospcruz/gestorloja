package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ProdutoController implements ControllerBase {

	private static final String RESULTADO_NAO_ENCONTRADO = "Produto não encontrado";

	private List<Produto> produtos;

	private Produto produto;

	private Crud<Produto> produtoDao;

	private boolean validaDados = true;

	public ProdutoController() {

		produtoDao = new CrudDao<>();

	}

	public List<Produto> getList() {

		return produtos;

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
	// throw new Exception("Necessário preencher todos os campos.");
	//
	// }
	//
	// if (produto.getCodigoDeBarras() == null) {
	// throw new Exception("Necessário cadastrar o Código de Barras.");
	// }
	//
	// }

	public void busca(Object object) {
		Integer id = (Integer) object;
		produto = produtoDao.busca(Produto.class, id);

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

		produtos = new ArrayList<>();

		for (Produto peca : tmp) {

			if (peca.getDescricaoProduto().contains(parametro)) {

				produtos.add(peca);

			}

		}

	}

	@Override
	public List buscaTodos() {

		produtos = produtoDao.busca("produto.readall");
		return getList();
	}

	@Override
	public void excluir() throws Exception {

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

	@Override
	public void salva() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
