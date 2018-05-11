package br.com.marcospcruz.gestorloja.controller;

import java.util.Date;
import java.util.List;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;

public class EstoqueController implements AbstractController {

	private static final String MESSAGE_QT_INVALIDA_EXCEPTION = "Quantidade informada Inv�lida!";

	private ItemEstoque itemEstoque;

	private Crud<ItemEstoque> itemEstoqueDao;

	private List<ItemEstoque> itensEstoque;

	private ProdutoController produtoController;

	public EstoqueController() throws Exception {

		itemEstoqueDao = new CrudDao<ItemEstoque>();

		produtoController = (ProdutoController) getController(ControllerAbstractFactory.PRODUTO);

	}

	public ItemEstoque getItemEstoque() {
		return itemEstoque;
	}

	public void setItemEstoque(ItemEstoque itemEstoque) {
		this.itemEstoque = itemEstoque;
	}

	public List<ItemEstoque> getItensEstoque() {

		if (itensEstoque == null || itensEstoque.size() == 0)

			carregaItensEstoque();

		return itensEstoque;

	}

	private void carregaItensEstoque() {

		itensEstoque = itemEstoqueDao.busca("itemEstoque.readAll");

	}

	public void setItensEstoque(List<ItemEstoque> itensEstoque) {
		this.itensEstoque = itensEstoque;
	}

	public void criaItemEstoque(Produto produto, String quantidade) throws Exception {

		itemEstoque = new ItemEstoque();

		try {

			itemEstoque.setDataEntradaEstoque(new Date());

			produtoController.busca(produto.getDescricaoProduto());

			itemEstoque.setProduto(produto);

			int qt = parseInt(quantidade);

			itemEstoque.setQuantidade(qt);

			itemEstoque.setOperador(getUsuarioLogado());
			salva(itemEstoque);

		} catch (NumberFormatException e) {

			e.printStackTrace();

			throw new Exception(e.getMessage());

		} finally {

			itemEstoque = null;

			itensEstoque = null;

		}

	}

	private int parseInt(String quantidade) {

		int qt;

		try {

			qt = Integer.parseInt(quantidade);

			if (qt <= 0) {
				throw new NumberFormatException(MESSAGE_QT_INVALIDA_EXCEPTION);
			}

		} catch (NumberFormatException e) {

			throw new NumberFormatException(MESSAGE_QT_INVALIDA_EXCEPTION);

		}

		return qt;
	}

	public void buscaItemPorCodigoDeBarras(String codigoProduto) {
		itemEstoque = itemEstoqueDao.busca("itemestoque.readCodigoEstoque", "codigo", codigoProduto);

	}

	public void buscaItemEstoque(String descricao) throws Exception {

		String valor = "%" + descricao.toUpperCase() + "%";

		itensEstoque = (List<ItemEstoque>) itemEstoqueDao.buscaList("itemestoque.readDescricaoPecao", "descricao",
				valor);

		if (itensEstoque.size() == 1)

			itemEstoque = itensEstoque.get(0);

	}

	public void busca(int idItemEstoque) {

		itemEstoque = itemEstoqueDao.busca(ItemEstoque.class, idItemEstoque);

	}

	public void apagaItem() throws Exception {

		if (itemEstoque == null) {

			throw new Exception("Exclus�o inv�lida!");

		}

		itemEstoqueDao.delete(itemEstoque);

		anulaAtributos();

	}

	public void anulaAtributos() {

		itemEstoque = null;

		itensEstoque = null;

	}

	public void incrementaItem(int quantidade) throws Exception {

		itemEstoque.setQuantidade(itemEstoque.getQuantidade() + quantidade);

		salva(itemEstoque);

		anulaAtributos();

	}

	public void validaBusca() throws Exception {

		if (itemEstoque == null && itensEstoque.size() == 0)

			throw new Exception(ConstantesEnum.ITEM_DO_ESTOQUE_NAO_ENCONTRADO.getValue().toString());

	}

	@Override
	public void busca(Object id) throws Exception {
		int idProduto = (int) id;
		itemEstoque = itemEstoqueDao.busca("itemEstoque.readProduto", "idProduto", idProduto);

	}

	@Override
	public List buscaTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void busca(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getItem() {

		return itemEstoque;
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
		itemEstoque = itemEstoqueDao.update((ItemEstoque) object);

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

}
