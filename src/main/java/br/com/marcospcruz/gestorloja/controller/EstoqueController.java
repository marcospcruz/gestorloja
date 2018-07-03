package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.HistoricoOperacao;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.Util;

public class EstoqueController implements ControllerBase {

	private static final String MESSAGE_QT_INVALIDA_EXCEPTION = "Quantidade informada Invï¿½lida!";

	private ItemEstoque itemEstoque;

	// private Crud<ItemEstoque> itemEstoqueDao;

	private List<ItemEstoque> itensEstoque;

	private ProdutoController produtoController;

	private CrudDao<ItemEstoque> itemEstoqueDao;

	private List<ItemEstoque> consultaEstoque;

	public EstoqueController() throws Exception {

		itemEstoqueDao = new CrudDao<>();
		carregaItensEstoque();
		produtoController = (ProdutoController) getController(ControllerAbstractFactory.PRODUTO);

	}

	public ItemEstoque getItemEstoque() {
		return itemEstoque;
	}

	public void setItemEstoque(ItemEstoque itemEstoque) {
		this.itemEstoque = itemEstoque;
	}

	public List<ItemEstoque> getItensEstoque() {

		// if (itensEstoque == null || itensEstoque.size() == 0)
		//
		// carregaItensEstoque();

		return itensEstoque;

	}

	private void carregaItensEstoque() {
		// if (itensEstoque == null || itensEstoque.isEmpty())
		itensEstoque = itemEstoqueDao.busca("itemEstoque.readAll");
		setList(itensEstoque);

	}

	private Crud<ItemEstoque> getDao() {

		return new CrudDao<>();
	}

	public void setItensEstoque(List<ItemEstoque> itensEstoque) {
		this.itensEstoque = itensEstoque;
	}

	public void criaItemEstoque(Produto produto, String quantidade) throws Exception {

		itemEstoque = new ItemEstoque();

		try {

			itemEstoque.setDataContagem(new Date());

			produtoController.busca(produto.getDescricaoProduto());

			itemEstoque.setProduto(produto);

			int qt = parseInt(quantidade);

			itemEstoque.setQuantidade(qt);

			itemEstoque.setOperador(getUsuarioLogado());
//			salva(itemEstoque);

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
		Crud<ItemEstoque> itemEstoqueDao = getDao();
		itemEstoque = itemEstoqueDao.busca("itemestoque.readCodigoEstoque", "codigo", codigoProduto);

	}

	public void buscaItemEstoque(String descricao, String descricaoTipo) throws Exception {
		if (Util.isPalavraAcentuada(descricao)) {
			buscaComAcentuacao(descricaoTipo, descricao);

		} else {
			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("descricaoProduto", "%" + descricao.toUpperCase() + "%");
			String namedQuery = "itemestoque.readDescricaoProduto";
			if (descricaoTipo != null) {
				paramsMap.put("descricaoTipo", "%" + descricaoTipo.toUpperCase() + "%");
				namedQuery = "itemestoque.readDescricaoPeca";
			}
			Crud<ItemEstoque> itemEstoqueDao = getDao();
			setList(itemEstoqueDao.buscaList(namedQuery, paramsMap));
		}
		if (itensEstoque.size() == 1)

			itemEstoque = itensEstoque.get(0);

	}

	public void busca(int idItemEstoque) {
		Crud<ItemEstoque> itemEstoqueDao = getDao();
		itemEstoque = itemEstoqueDao.busca(ItemEstoque.class, idItemEstoque);

	}

	public void apagaItem() throws Exception {

		if (itemEstoque == null) {

			throw new Exception("Exclusï¿½o invï¿½lida!");

		}
		Crud<ItemEstoque> itemEstoqueDao = getDao();
		itemEstoqueDao.delete(itemEstoque);

		anulaAtributos();

	}

	public void anulaAtributos() {

		itemEstoque = null;

		itensEstoque = null;

	}

	public void incrementaItem(int quantidade) throws Exception {

		if (quantidade < 1)
			throw new Exception("Quantidade inválida!");

		itemEstoque.setQuantidade(itemEstoque.getQuantidade() + quantidade);

		salva();

		// anulaAtributos();

	}

	public void validaBusca() throws Exception {

		if (itemEstoque == null && itensEstoque.isEmpty())

			throw new Exception(ConstantesEnum.ITEM_DO_ESTOQUE_NAO_ENCONTRADO.getValue().toString());

	}

	@Override
	public void busca(Object object) throws Exception {
		itemEstoque = (ItemEstoque) object;
		Crud<ItemEstoque> itemEstoqueDao = getDao();
		itemEstoque = itemEstoqueDao.busca("itemEstoque.readProduto", "idProduto",
				itemEstoque.getProduto().getIdProduto(), "idFabricante", itemEstoque.getFabricante().getIdFabricante());

	}

	@Override
	public List buscaTodos() {
		carregaItensEstoque();
		return getList();
	}

	@Override
	public List getList() {

		return consultaEstoque;
	}

	@Override
	public void busca(String text) throws Exception {
		// busca(text, null, null);
		Crud<ItemEstoque> dao = getDao();
		itemEstoque = dao.busca("itemestoque.readCodigo", "codigo", text);
	}

	@Override
	public Object getItem() {

		return itemEstoque;
	}

	@Override
	public void setList(List list) {
		try {

			List objetos = list.isEmpty() ? itensEstoque : list;
			consultaEstoque = new ArrayList<>(objetos);
			itensEstoque=objetos;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setItem(Object object) {

		this.itemEstoque = (ItemEstoque) object;
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
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	public void decrementaItem(int quantidade) throws Exception {
		itemEstoque.setQuantidade(itemEstoque.getQuantidade() - quantidade);

		salva();

		anulaAtributos();

	}

	public void busca(String tipoProduto, String produto, String fabricante) {
		if (Util.isPalavraAcentuada(tipoProduto)) {
			buscaComAcentuacao(tipoProduto);

			return;
		}
		String namedQuery = "itemestoque.readDescricaoTipo";
		Map<String, String> paramsMap = new HashMap<>();
		if (tipoProduto != null) {
			paramsMap.put("descricaoTipo", "%" + tipoProduto.trim().toUpperCase() + "%");
		}
		if (produto != null) {
			paramsMap.put("descricaoProduto", "%" + produto.trim().toUpperCase() + "%");
			namedQuery = "itemestoque.readDescricaoPeca";
		}

		if (fabricante != null) {
			paramsMap.put("nomeFabricante", "%" + fabricante.trim().toUpperCase() + "%");
			namedQuery = "itemestoque.readTipoFabricante";
		}

		itensEstoque = itemEstoqueDao.buscaList(namedQuery, paramsMap);
		consultaEstoque = itensEstoque;
	}

	private void buscaComAcentuacao(String... params) {
		buscaTodos();

		for (String param : params) {
			if (param == null)
				continue;
			List<ItemEstoque> objetos = new ArrayList<>();
			for (Object o : itensEstoque) {
				ItemEstoque itemEstoque = (ItemEstoque) o;
				Produto produto = itemEstoque.getProduto();
				SubTipoProduto tipoProduto = itemEstoque.getProduto().getTipoProduto();

				boolean tipoProdutoMatches = tipoProduto.getDescricaoTipo().toUpperCase().contains(param.toUpperCase())
						? true
						: tipoProduto.getSuperTipoProduto().getDescricaoTipo().toUpperCase()
								.contains(param.toUpperCase()) ? true : false;

				boolean produtoMatches = !tipoProdutoMatches
						&& produto.getDescricaoProduto().toUpperCase().contains(param.toUpperCase());

				if (tipoProdutoMatches || produtoMatches) {
					objetos.add(itemEstoque);
				}
			}
			setList(objetos);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void salva() throws Exception {

		itemEstoque.setOperador(getUsuarioLogado());
		itemEstoque.setDataContagem(new Date());

		itemEstoque = itemEstoqueDao.update(itemEstoque);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		Crud<HistoricoOperacao> historicoDao = new CrudDao<>();
		HistoricoOperacao historicoOperacao = new HistoricoOperacao();
		historicoOperacao.setDataOperacao(new Date());
		historicoOperacao.setItemEstoque(itemEstoque);
		historicoOperacao.setOperador(getUsuarioLogado());
		historicoOperacao.setOperacao(operacao);
		historicoDao.update(historicoOperacao);
	}

}
