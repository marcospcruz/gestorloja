package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.itextpdf.text.pdf.AcroFields.Item;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.HistoricoOperacao;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.Util;

public class EstoqueController extends ControllerBase {

	private static final String MESSAGE_QT_INVALIDA_EXCEPTION = "Quantidade informada Invï¿½lida!";

	private ItemEstoque itemEstoque;

	// private Crud<ItemEstoque> itemEstoqueDao;

	private List<ItemEstoque> itensEstoque;

	private ProdutoController produtoController;

	private List<ItemEstoque> consultaEstoque;

	public EstoqueController() throws Exception {

		// itemEstoqueDao = new CrudDao<>();
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

		itensEstoque = null;
		Map<Object, Object> cacheMap = super.getCacheMap();
		if (cacheMap == null || cacheMap.isEmpty()) {
			CrudDao<ItemEstoque> itemEstoqueDao = new CrudDao<>();
			itensEstoque = itemEstoqueDao.busca("itemEstoque.readAll");
			Map results = itensEstoque.stream().collect(
					Collectors.toMap(itemEstoque -> itemEstoque.getIdItemEstoque(), itemEstoque -> itemEstoque));
			setCacheMap(results);
		} else if (itensEstoque == null)
			itensEstoque = new ArrayList(getCacheMap().values());
		// setList(itensEstoque);

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

			itemEstoque.setDataContagem(SingletonManager.getInstance().getData());

			produtoController.busca(produto.getDescricaoProduto());

			itemEstoque.setProduto(produto);

			int qt = parseInt(quantidade);

			itemEstoque.setQuantidade(qt);

			itemEstoque.setOperador(getUsuarioLogado());
			// salva(itemEstoque);

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
		try {
			Map<Object, Object> cache = getCacheMap();

			if (cache != null || !cache.isEmpty()) {
				itemEstoque = (ItemEstoque) cache.values().stream()
						.filter(item -> codigoProduto.equals(((ItemEstoque) item).getCodigoDeBarras())).findAny()
						.orElse(null);
			} else {
				Crud<ItemEstoque> itemEstoqueDao = getDao();
				itemEstoque = itemEstoqueDao.busca("itemestoque.readCodigoEstoque", "codigo", codigoProduto);
			}
		} catch (Exception e) {
			itemEstoque = null;
		}

	}

	public void buscaItemEstoque(String descricao, String descricaoTipo) throws Exception {

		// if (Util.isPalavraAcentuada(descricao)) {
		buscaComAcentuacao(descricaoTipo, descricao);
		if (!itensEstoque.isEmpty())
			return;
		// } else {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("descricaoProduto", "%" + descricao.toUpperCase() + "%");
		String namedQuery = "itemestoque.readDescricaoProduto";
		if (descricaoTipo != null) {
			paramsMap.put("descricaoTipo", "%" + descricaoTipo.toUpperCase() + "%");
			namedQuery = "itemestoque.readDescricaoPeca";
		}
		Crud<ItemEstoque> itemEstoqueDao = getDao();
		setList(itemEstoqueDao.buscaList(namedQuery, paramsMap));
		// }
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

		SingletonManager.getInstance().resetControllers();

	}

	public void incrementaItem(int quantidade) throws Exception {

		if (quantidade < 1)
			throw new Exception("Quantidade inválida!");
		if (itemEstoque.isEstoqueDedutivel()) {
			itemEstoque.setQuantidade(itemEstoque.getQuantidade() + quantidade);

			salva();
		}

		// anulaAtributos();

	}

	public void validaBusca() throws Exception {

		if (itemEstoque == null && itensEstoque.isEmpty()) {
			buscaTodos();

			throw new Exception(ConstantesEnum.ITEM_DO_ESTOQUE_NAO_ENCONTRADO.getValue().toString());
		}
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
		return itensEstoque;
	}

	@Override
	public List getList() {
		// buscaTodos();
		// if (itensEstoque == null && !getCacheMap().isEmpty())

		Map<Object, Object> cacheMap = getCacheMap();
		if (cacheMap == null || cacheMap.isEmpty()) {
			carregaItensEstoque();
		}
		if (itensEstoque == null)
			itensEstoque = new ArrayList(cacheMap.values());
		return itensEstoque;
	}

	@Override
	public void busca(String text) throws Exception {
		// busca(text, null, null);

		Map<Object, Object> cache = getCacheMap();
		if (cache != null || !cache.isEmpty()) {
			itemEstoque = (ItemEstoque) cache.values().stream()
					.filter(item -> text.equals(((ItemEstoque) item).getCodigoDeBarras())).findAny().orElse(null);
			if (itemEstoque != null) {
				return;
			}
		}
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

			// List objetos = list != null && list.isEmpty() ? itensEstoque : list;
			// consultaEstoque = new ArrayList<>(objetos);
			itensEstoque = list;
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
		Crud<ItemEstoque> dao = new CrudDao<>();

		Map<Object, Object> cacheMap = getCacheMap();

		cacheMap.remove(itemEstoque.getIdItemEstoque());
		itemEstoque = dao.busca(ItemEstoque.class, itemEstoque.getIdItemEstoque());
		dao.delete(itemEstoque);
		setList(null);

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

	public void busca(String tipoProduto, String produto, String fabricante) throws Exception {
		// if (Util.isPalavraAcentuada(tipoProduto)) {
		buscaComAcentuacao(tipoProduto, produto);
		if (!itensEstoque.isEmpty())
			return;
		// }
		String namedQuery = "itemestoque.readDescricaoTipo";
		Map<String, String> paramsMap = new HashMap<>();
		if (tipoProduto != null && !tipoProduto.isEmpty()) {
			paramsMap.put("descricaoTipo", "%" + tipoProduto.trim().toUpperCase() + "%");
		}
		if (produto != null && !produto.isEmpty()) {
			paramsMap.put("descricaoProduto", "%" + produto.trim().toUpperCase() + "%");
			namedQuery = "itemestoque.readDescricaoPeca";
		}

		if (fabricante != null && !fabricante.isEmpty()) {
			paramsMap.put("nomeFabricante", "%" + fabricante.trim().toUpperCase() + "%");
			namedQuery = "itemestoque.readTipoFabricante";
		}
		// if (!getCacheMap().isEmpty()) {
		// Map<Object, Object> cache = getCacheMap();
		// if (tipoProduto != null) {
		// loadEstoqueSuperTipoCache(tipoProduto, cache);
		// if (itensEstoque == null || itensEstoque.isEmpty()) {
		// itensEstoque = new ArrayList(
		// cache.values().stream().filter(item -> ((ItemEstoque) item).getTipoProduto()
		// != null)
		// .filter(item -> tipoProduto
		// .equals(((ItemEstoque) item).getTipoProduto().getDescricaoTipo()))
		// .collect(Collectors.toList()));
		// }
		// if (itensEstoque != null || !itensEstoque.isEmpty())
		// return;

		// }

		// }
		CrudDao<ItemEstoque> itemEstoqueDao = new CrudDao<>();
		itensEstoque = itemEstoqueDao.buscaList(namedQuery, paramsMap);
		if (itensEstoque.isEmpty())
			throw new Exception("Ítem não encontrado no estoque.");
	}

	private void loadEstoqueSuperTipoCache(String tipoProduto, Map<Object, Object> cache) {
		System.out.println(tipoProduto);
		itensEstoque = new ArrayList(cache.values().stream()
				.filter(item -> ((ItemEstoque) item).getTipoProduto().getSuperTipoProduto() != null)
				.filter(item -> tipoProduto
						.equals(((ItemEstoque) item).getTipoProduto().getSuperTipoProduto().getDescricaoTipo()))
				.collect(Collectors.toList()));
	}

	private void buscaComAcentuacao(String... params) {

		buscaTodos();

		int cont = 0;
		for (String param : params) {

			try {
				Predicate<ItemEstoque> predicate = null;
				if (cont == 0)
					predicate = filtroPorCategoria(param);
				else if (cont == 1)
					predicate = filtroPorProduto(param);
				itensEstoque = itensEstoque.stream().filter(predicate).collect(Collectors.toList());

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			cont++; // if (param == null)
			// continue;
			// List<ItemEstoque> objetos = new ArrayList<>();
			// for (Object o : itensEstoque) {
			// ItemEstoque itemEstoque = (ItemEstoque) o;
			// Produto produto = itemEstoque.getProduto();
			//
			// SubTipoProduto tipoProduto = itemEstoque.getTipoProduto();
			// boolean tipoProdutoMatches = false;
			// try {
			// tipoProdutoMatches =
			// tipoProduto.getDescricaoTipo().toUpperCase().contains(param.toUpperCase())
			// ? true
			// : tipoProduto.getSuperTipoProduto().getDescricaoTipo().toUpperCase()
			// .contains(param.toUpperCase()) ? true : false;
			// } catch (NullPointerException e) {
			// e.printStackTrace();
			// }
			//
			// boolean produtoMatches = !tipoProdutoMatches
			// && produto.getDescricaoProduto().toUpperCase().contains(param.toUpperCase());
			//
			// if (tipoProdutoMatches || produtoMatches) {
			// objetos.add(itemEstoque);
			// }
			// }
			// setList(objetos);
		}
	}

	private Predicate<ItemEstoque> filtroPorProduto(String descricaoProduto) {
		// TODO Auto-generated method stub
		return item -> item.getProduto().getDescricaoProduto().toUpperCase()
				.contains(descricaoProduto.trim().toUpperCase());
	}

	private Predicate<ItemEstoque> filtroPorCategoria(String descricaoTipo) {
		// TODO Auto-generated method stub
		return item -> item.getTipoProduto().getSuperTipoProduto().getDescricaoTipo()
				.equalsIgnoreCase(descricaoTipo.trim())
				|| item.getTipoProduto().getDescricaoTipo().equalsIgnoreCase(descricaoTipo.trim());
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void salva() throws Exception {

		itemEstoque.setOperador(getUsuarioLogado());
		itemEstoque.setDataContagem(SingletonManager.getInstance().getData());

		CrudDao<ItemEstoque> itemEstoqueDao = new CrudDao<>();
		itemEstoque = itemEstoqueDao.update(itemEstoque);
		// responsável em fazer o fetch de listas dos objetos
		itemEstoque = itemEstoqueDao.busca(ItemEstoque.class, itemEstoque.getIdItemEstoque());
		Produto produto = itemEstoque.getProduto();
		Fabricante fabricante = itemEstoque.getFabricante();
		TipoProduto superTipoProduto = itemEstoque.getTipoProduto().getSuperTipoProduto();
		// superTipoProduto.getSubTiposProduto().add(itemEstoque.getTipoProduto());
		getCacheMap().put(itemEstoque.getIdItemEstoque(), itemEstoque);

		FabricanteController fabricanteController = (FabricanteController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.FABRICANTE);

		TipoProdutoController tipoProdutoController = (TipoProdutoController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);

		fabricanteController.getCacheMap().put(fabricante.getIdFabricante(), fabricante);

		produtoController.getCacheMap().put(produto.getIdProduto(), produto);

		tipoProdutoController.getCacheMap().put(superTipoProduto.getIdTipoItem(), superTipoProduto);

		setList(null);

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		Crud<HistoricoOperacao> historicoDao = new CrudDao<>();
		HistoricoOperacao historicoOperacao = new HistoricoOperacao();
		historicoOperacao.setDataOperacao(SingletonManager.getInstance().getData());
		historicoOperacao.setItemEstoque(itemEstoque);
		historicoOperacao.setOperador(getUsuarioLogado());
		historicoOperacao.setOperacao(operacao);
		historicoDao.update(historicoOperacao);
	}

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void carregaCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validaExclusaoItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
