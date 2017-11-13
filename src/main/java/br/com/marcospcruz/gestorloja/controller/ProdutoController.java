package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class ProdutoController extends AbstractController {

	private static final String RESULTADO_NAO_ENCONTRADO = "Produto n�o encontrado";

	private List<Produto> produtos;

	private Produto produto;

	private Crud<Produto> produtoDao;

	public ProdutoController() {

		produtoDao = new CrudDao<Produto>();

	}

	public List<Produto> getProdutos() {

		if (produtos == null || produtos.size() == 0)

			carregaProdutos();

		return produtos;

	}

	private void carregaProdutos() {

		produtos = produtoDao.busca("produto.readall");

	}

	public void setPecasRoupa(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Object[] getArrayTiposProduto() {

		TipoProdutoController tipoProdutoController = new TipoProdutoController();

		List<TipoProduto> listaTipos = tipoProdutoController.getTiposProdutos();

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
	public void salva(Object tipoRoupa, Object subTipoRoupa, String descricao,
			String unidadeMedida, String valorUnitario) throws Exception {

		valida(descricao, unidadeMedida, valorUnitario);

		if (produto == null)

			produto = new Produto();

		produto.setUnidadeMedida(unidadeMedida);

		produto.setDescricaoProduto(descricao);

		produto.setValorUnitario(Float.parseFloat(valorUnitario.replace(',',
				'.')));

		produto.setTipoProduto((SubTipoProduto) subTipoRoupa);

		produtoDao.update(produto);

		produto = null;

	}

	private void valida(String descricao, String cor, String valorUnitario)
			throws Exception {

		if (descricao == null || cor == null || valorUnitario == null
				|| descricao.isEmpty() || cor.isEmpty()
				|| valorUnitario.isEmpty()) {

			throw new Exception(
					"Necess�rio preencher todos os campos da informa��o.");

		}

	}

	public void busca(int idPecaRoupa) {

		produto = produtoDao.busca(Produto.class, idPecaRoupa);

	}

	public void deletaProduto() throws Exception {

		if (produto.getItemEstoque() != null) {

			throw new Exception(
					ConstantesEnum.EXCLUSAO_PRODUTO_COM_ITENS_ESTOQUE
							.getValue().toString());

		}

		produtoDao.delete(produto);

		produto = null;

	}

	public void busca(String parametro) throws Exception {

		zeraAtributos();

		if (contemAcentuacao(parametro)) {

			buscaInWorkAround(parametro);

		} else {

			String valor = "%" + parametro.toUpperCase() + "%";

			produtos = produtoDao.buscaList("produto.readparametrolike",
					"descricao", valor);
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

	private void zeraAtributos() {

		produto = null;

		produto = null;

	}

}
