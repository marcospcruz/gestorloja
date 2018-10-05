package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.HistoricoOperacao;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.Util;

public class EstoqueController extends ControllerBase {

	private static final String MESSAGE_QT_INVALIDA_EXCEPTION = "Quantidade informada Inválida!";

	private ItemEstoque itemEstoque;

	private Crud<ItemEstoque> itemEstoqueDao;

	private List<ItemEstoque> itensEstoque;

	private ProdutoController produtoController;

	private List<ItemEstoque> consultaEstoque;

	// private CrudDao<ItemEstoque> itemEstoqueDao;

	public EstoqueController() throws Exception {

		itemEstoqueDao = new CrudDao<>();
		carregaItensEstoque();
		produtoController = (ProdutoController) getController(ControllerAbstractFactory.PRODUTO);

	}

	public ItemEstoque getItemEstoque() {
		if (itemEstoque != null && itemEstoque.getIdItemEstoque() != null)
			itemEstoque = itemEstoqueDao.update(itemEstoque);
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
		itemEstoque = itemEstoqueDao.busca("itemestoque.readCodigo", "codigo", codigoProduto);

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

			setList(itemEstoqueDao.buscaList(namedQuery, paramsMap));
		}
		if (itensEstoque.size() == 1)

			itemEstoque = itensEstoque.get(0);

	}

	public void busca(int idItemEstoque) {

		itemEstoque = itemEstoqueDao.busca(ItemEstoque.class, idItemEstoque);

	}

	public void apagaItem() throws Exception {

		if (itemEstoque == null) {

			throw new Exception("Exclusão inválida!");

		}

		try {
			// itemEstoque = itemEstoqueDao.busca("itemEstoque.readProduto", "idProduto",
			// itemEstoque.getProduto().getIdProduto(), "idFabricante",
			// itemEstoque.getFabricante().getIdFabricante(), "idTipoProduto",
			// itemEstoque.getTipoProduto().getIdTipoItem());

			itemEstoqueDao.delete(itemEstoque);

		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {

		}
		anulaAtributos();

	}

	public void anulaAtributos() {

		itemEstoque = null;

		itensEstoque = null;

	}

	public void incrementaItem(int quantidade) throws Exception {
		if (!itemEstoque.isEstoqueDedutivel())
			return;
		if (quantidade < 1 && !SingletonManager.getInstance().isPermiteVendaSemControlarEstoque())
			throw new Exception("Quantidade inválida!");
		if (itemEstoque.isEstoqueDedutivel()
		// && quantidade > 0
		) {
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
		return itensEstoque;
	}

	@Override
	public void busca(String text) throws Exception {
		// busca(text, null, null);

		try {
			itemEstoque = itemEstoqueDao.busca("itemestoque.readCodigo", "codigo", text);
		} catch (NoResultException e) {
			throw new Exception("Ítem código " + text + " não encontrado no estoque.");
		}
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
		// itemEstoque.setQuantidade(itemEstoque.getQuantidade() - quantidade);

		// salva();
		incrementaItem(quantidade * -1);
		anulaAtributos();

	}

	// public void busca(String tipoProduto, String produto, String fabricante) {
	// buscaTodos();
	// if (tipoProduto != null) {
	// List<ItemEstoque> itens = itensEstoque.stream()
	// .filter(item -> ((ItemEstoque)
	// item).getTipoProduto().getSuperTipoProduto().getDescricaoTipo()
	// .equalsIgnoreCase(tipoProduto)
	// || ((ItemEstoque)
	// item).getTipoProduto().getDescricaoTipo().equalsIgnoreCase(tipoProduto))
	// .collect(Collectors.toList());
	// if (!itens.isEmpty() && itens != itensEstoque) {
	// itensEstoque = itens;
	// return;
	// }
	//
	// } else if (fabricante != null) {
	// List<ItemEstoque> itens = itensEstoque.stream()
	// .filter(item -> ((ItemEstoque)
	// item).getFabricante().getNome().equals(fabricante))
	// .collect(Collectors.toList());
	// if (!itens.isEmpty() && itens != itensEstoque) {
	// itensEstoque = itens;
	// return;
	// }
	// }
	//
	// if (tipoProduto != null && Util.isPalavraAcentuada(tipoProduto)) {
	// buscaComAcentuacao(tipoProduto);
	//
	// return;
	// }
	// String namedQuery = "itemestoque.readDescricaoTipo";
	// Map<String, String> paramsMap = new HashMap<>();
	// if (tipoProduto != null) {
	// // paramsMap.put("descricaoTipo", "%" + tipoProduto.trim().toUpperCase() +
	// "%");
	// }
	// if (produto != null) {
	// paramsMap.put("descricaoProduto", "%" + produto.trim().toUpperCase() + "%");
	// namedQuery = "itemestoque.readDescricaoPeca";
	// }
	//
	// if (fabricante != null) {
	// paramsMap.put("nomeFabricante", "%" + fabricante.trim().toUpperCase() + "%");
	// namedQuery = "itemestoque.readTipoFabricante";
	// }
	//
	// itensEstoque = itemEstoqueDao.buscaList(namedQuery, paramsMap);
	// consultaEstoque = itensEstoque;
	// }
	public void busca(String tipoProduto, String produto, String fabricante) throws Exception {
		buscaTodos();
		List<ItemEstoque> tmpItensEstoque = new ArrayList<>(itensEstoque);
		List<ItemEstoque> backup = new ArrayList<>(itensEstoque);
		;
		if (fabricante != null) {
			itensEstoque = tmpItensEstoque.stream()
					.filter(itemEstoque -> fabricante.equals(itemEstoque.getFabricante().getNome()))
					.collect(Collectors.toList());
		}

		if (tipoProduto != null) {
			tmpItensEstoque = new ArrayList<>(itensEstoque);
			backup = new ArrayList<>(itensEstoque);
			if (tipoProduto != null) {
				itensEstoque = tmpItensEstoque.stream()
						.filter(itemEstoque -> itemEstoque.getTipoProduto().getSuperTipoProduto() != null && itemEstoque
								.getTipoProduto().getSuperTipoProduto().getDescricaoTipo().equals(tipoProduto))
						.collect(Collectors.toList());
				if (itensEstoque.isEmpty()) {
					tmpItensEstoque = new ArrayList<>(itensEstoque);
					if (tmpItensEstoque.isEmpty())
						tmpItensEstoque = new ArrayList<>(backup);
					itensEstoque = tmpItensEstoque.stream().filter(
							itemEstoque -> itemEstoque.getTipoProduto().getDescricaoTipo().contains(tipoProduto))
							.collect(Collectors.toList());
					

				}
			}
		}
		if (produto != null) {
			tmpItensEstoque = new ArrayList<>(itensEstoque);

			itensEstoque = tmpItensEstoque.stream()
					.filter(itemEstoque -> itemEstoque.getCodigoDeBarras().equals(produto))
					.collect(Collectors.toList());
			if (itensEstoque.isEmpty()) {
				tmpItensEstoque = new ArrayList<>(itensEstoque);
				itensEstoque = tmpItensEstoque.stream()
						.filter(itemEstoque -> itemEstoque.getProduto().getDescricaoProduto().contains(produto))
						.collect(Collectors.toList());
			}
		}

		if (itensEstoque.isEmpty())
			throw new Exception("Dados não encontrados");
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

				SubTipoProduto tipoProduto = itemEstoque.getTipoProduto();
				boolean tipoProdutoMatches = false;
				try {
					tipoProdutoMatches = tipoProduto.getDescricaoTipo().toUpperCase().contains(param.toUpperCase())
							? true
							: tipoProduto.getSuperTipoProduto().getDescricaoTipo().toUpperCase()
									.contains(param.toUpperCase()) ? true : false;
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				boolean produtoMatches = !tipoProdutoMatches
						&& produto.getDescricaoProduto().toUpperCase().contains(param.toUpperCase());

				if (tipoProdutoMatches || produtoMatches) {
					objetos.add(itemEstoque);
				}
			}
			if (!objetos.isEmpty())
				setList(objetos);
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void salva() throws Exception {

		itemEstoque.setOperador(getUsuarioLogado());
		itemEstoque.setDataContagem(SingletonManager.getInstance().getData());

		try {
			itemEstoque = itemEstoqueDao.update(itemEstoque);

		} catch (PersistenceException e) {
			// e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {

		HistoricoOperacao historicoOperacao = new HistoricoOperacao();
		historicoOperacao.setDataOperacao(SingletonManager.getInstance().getData());
		historicoOperacao.setItemEstoque(itemEstoque);
		historicoOperacao.setOperador(getUsuarioLogado());
		historicoOperacao.setOperacao(operacao);
		Crud<HistoricoOperacao> historicoDao = new CrudDao<>();
		historicoDao.update(historicoOperacao);
	}

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void novoUsuario() {
		itemEstoque = new ItemEstoque();

	}

	public ItemEstoque validaCodigoDeBarras(String codigoBarras) {
		buscaTodos();
		//@formatter:off
		return itensEstoque
				.stream()
				.filter(item->codigoBarras.equals(item.getCodigoDeBarras()))
				.findFirst()
				.orElse(null);
		//@formatter:on

	}

}
