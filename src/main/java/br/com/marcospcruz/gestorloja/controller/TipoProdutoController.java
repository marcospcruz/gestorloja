package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import br.com.marcospcruz.gestorloja.util.TipoProdutoNotFoundException;

/**
 * Classe responsï¿½vel em gerenciar regras de negï¿½cio para Tipos de Peï¿½as
 * de Roupas.
 * 
 * @author Marcos
 * 
 */
public class TipoProdutoController extends ControllerBase {

	private static final String RESULTADO_NAO_ENCONTRADO = ConstantesEnum.TIPO_PRODUTO_NAO_ENCONTRADO.getValue()
			.toString();

	private static final String TIPO_ITEM_INVALIDO = ConstantesEnum.TIPO_ITEM_INVALIDO.getValue().toString();

	private static final String REMOCAO_INVALIDA_EXCEPTION = ConstantesEnum.REMOCAO_INVALIDA_EXCEPTION.getValue()
			.toString();

	private static final String SELECIONE_TIPO_PRODUTO_EXCEPTION = ConstantesEnum.TIPO_PRODUTO_SELECAO_MESSAGE_EXCEPTION
			.getValue().toString();

	private static final String TIPO_PRODUTO_JA_CADASTRADO = ConstantesEnum.TIPO_PRODUTO_JA_CADASTRADO.getValue()
			.toString();

	private static final String REMOCAO_TIPO_POPULADO = ConstantesEnum.TIPO_PRODUTO_POPULADO.getValue().toString();

	private static final String REMOCAO_SUPERTIPO_POPULADO = ConstantesEnum.TIPO_PRODUTO_POPULADO_SUPERTIPO.getValue()
			.toString();

	private SubTipoProduto tipoProduto;

	private Crud<SubTipoProduto> tipoProdutoDao;

	private List tiposProdutos;

	/**
	 * Mï¿½todo Construtor sem Parï¿½metros.
	 */
	public TipoProdutoController() {

		tipoProdutoDao = new CrudDao<>();

	}

	/**
	 * Mï¿½todo responsï¿½vel em salvar novo tipo de roupa ou atualizar o jï¿½
	 * existente.
	 * 
	 * @param descricao
	 * @param subTipo
	 * @param superTipoProduto
	 * @param sexo
	 * @throws Exception
	 */

	public void salva(String descricao, boolean subTipo, Object superTipoProduto) throws Exception {

		// validaTipoRoupa(descricao);

		if (descricao.length() == 0) {

			throw new Exception(TIPO_ITEM_INVALIDO);

		}

		validaCriaTipoProduto(subTipo, superTipoProduto, descricao);

		Usuario operador = getUsuarioLogado();

		tipoProduto.setOperador(operador);
		tipoProduto.setDataInsercao(SingletonManager.getInstance().getData());

		tipoProduto = tipoProdutoDao.update(tipoProduto);
		getCacheMap().put(tipoProduto.getIdTipoItem(), tipoProduto);
	}

	private void validaCriaTipoProduto(boolean subTipo, Object superTipoProduto, String descricao) throws Exception {

		try {

			if (tipoProduto == null) {

				tipoProduto = tipoProdutoDao.busca("tipoProduto.readParametro", "descricao", descricao.toLowerCase());

				tipoProduto = null;

				throw new Exception(TIPO_PRODUTO_JA_CADASTRADO);

			}
		} catch (NoResultException e) {

			e.printStackTrace();

			if (tipoProduto == null)
				tipoProduto = new SubTipoProduto();

		}

		if (subTipo) {

			validaCamposFormulario(superTipoProduto);

			tipoProduto.setSuperTipoProduto((SubTipoProduto) superTipoProduto);

			// tipoProduto.setSexo(sexo.toString());

		}

		tipoProduto.setDescricaoTipo(descricao);

	}

	private void validaTipoProduto(String descricao) throws Exception {

		if (tipoProduto == null) {

			busca(descricao);

			if (tipoProduto != null) {

				tipoProduto = null;

				throw new Exception("Tipo de Produto " + descricao + " já cadastrado!");

			}

		}

	}

	private void validaCamposFormulario(Object tipoProduto) throws Exception {

		// if (sexo == null) {
		//
		// throw new Exception("Selecione o Sexo para este tipo de Roupa");
		//
		// }

		if (tipoProduto == null)

			throw new Exception(SELECIONE_TIPO_PRODUTO_EXCEPTION);

	}

	public List<TipoProduto> getList() {

		return tiposProdutos;

	}

	public void setList(List tiposProdutos) {
		this.tiposProdutos = tiposProdutos;
	}

	public void setItem(Object tipoProduto) {
		if (tipoProduto != null && !(tipoProduto instanceof TipoProduto)) {
			tipoProduto = new SubTipoProduto(tipoProduto.toString(), getUsuarioLogado());
		}
		this.tipoProduto = (SubTipoProduto) tipoProduto;
		// implementação para AutocompleteComboBox
		if (tipoProduto != null)
			setList((List) ((SubTipoProduto) tipoProduto).getSubTiposProduto());
	}

	public void carregaTiposProdutos() {
		if (getCacheMap() == null || getCacheMap().isEmpty()) {
			setList(tipoProdutoDao.busca("tipoProduto.readtiposabstratos"));
			setCacheMap(getList().stream().collect(Collectors.toMap(i -> ((TipoProduto) i).getIdTipoItem(), i -> i)));
		} else {
			setList(new ArrayList(getCacheMap().values()));
		}

	}

	/**
	 * Busca Tipo de roupa
	 * 
	 * @param parametro
	 * @throws Exception
	 */
	// public void busca(String parametro) {
	//
	// try {
	//
	// tipoProduto = tipoProdutoDao.busca("tipopecaroupa.readParametro",
	// "descricao", parametro);
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	//
	// tipoProduto = null;
	//
	// }
	//
	// }
	/**
	 * 
	 * @param parametro
	 * @throws Exception
	 */
	public void busca(String parametro) throws Exception {
		if (parametro.length() == 0) {

			throw new Exception(BUSCA_INVALIDA);

		}
		Map<Object, Object> cache = getCacheMap();

		if (cache == null || cache.isEmpty()) {
			buscaTodos();
		}
		if (cache != null) {
			List todosTiposProdutos = new ArrayList();
			cache.values().stream().forEach(tp -> {
				todosTiposProdutos.add(tp);
				((SubTipoProduto) tp).getSubTiposProduto().stream().forEach(t -> {
					todosTiposProdutos.add(t);
				});
			});

			setList(todosTiposProdutos);
		}

		setList((ArrayList) tiposProdutos.stream()
				.filter(tp -> ((SubTipoProduto) tp).getDescricaoTipo().toUpperCase().contains(parametro.toUpperCase().trim())
						|| ((SubTipoProduto) tp).getSuperTipoProduto() != null && ((SubTipoProduto) tp)
								.getSuperTipoProduto().getDescricaoTipo().toUpperCase().contains(parametro.toUpperCase().trim()))
				.collect(Collectors.toList()));
		// zeraAtributos();
		if (tiposProdutos == null || tiposProdutos.isEmpty())
			if (contemAcentuacao(parametro)) {

				buscaInWorkAround(parametro);

			} else {

				String valor = "%" + parametro.toUpperCase() + "%";

				// tiposProdutos = tipoProdutoDao.buscaList("tipoProduto.readParametroLike",
				// "descricao", valor);

			}

		if (!tiposProdutos.isEmpty() && tiposProdutos.size() == 1) {
			tipoProduto = (SubTipoProduto) tiposProdutos.get(0);
			// if (!tipoProduto.getSubTiposProduto().isEmpty()) {
			//
			// List<SubTipoProduto> subTipos = new ArrayList<>();
			// // subTipos.add(tipoProduto);
			// // subTipos.addAll(tipoProduto.getSubTiposProduto());
			// // setList(subTipos);
			// tipoProduto = null;
			// }
		}

		else if (tiposProdutos.isEmpty())

			throw new TipoProdutoNotFoundException(RESULTADO_NAO_ENCONTRADO);

	}

	/**
	 * 
	 * @param parametro
	 */
	private void buscaInWorkAround(String parametro) {

		List<SubTipoProduto> tmp = tipoProdutoDao.busca("tipoProduto.readAll");

		tiposProdutos = new ArrayList<SubTipoProduto>();

		for (SubTipoProduto peca : tmp) {

			String descricao = peca.getDescricaoTipo().toLowerCase();

			if (descricao.contains(parametro.toLowerCase())) {

				tiposProdutos.add(peca);

			}

		}

	}

	private void zeraAtributos() {

		tiposProdutos = null;
		tipoProduto = null;

	}

	/**
	 * @throws Exception
	 * 
	 */
	public void excluir() throws Exception {

		if (tipoProduto == null || tipoProduto.getIdTipoItem() == null) {

			throw new Exception(REMOCAO_INVALIDA_EXCEPTION);

		}

		// if (tipoProduto.getProdutos() != null && tipoProduto.getProdutos().size() >
		// 0)
		//
		// throw new Exception(REMOCAO_TIPO_POPULADO);

		if (tipoProduto.getSubTiposProduto().size() > 0)
			throw new Exception(REMOCAO_SUPERTIPO_POPULADO);
		getCacheMap().remove(tipoProduto.getIdTipoItem());
		tipoProduto = tipoProdutoDao.busca(SubTipoProduto.class, tipoProduto.getIdTipoItem());

		tipoProdutoDao.delete(tipoProduto);

		setItem(null);

		setList(null);

	}

	/**
	 * 
	 * @param idSubProduto
	 */
	public void busca(Object id) {
		if (id != null) {
			Integer idSubProduto = Integer.valueOf(id.toString());
			tipoProduto = tipoProdutoDao.busca(SubTipoProduto.class, idSubProduto);
		} else
			throw new NullPointerException();
	}

	public List buscaTodos() {
		// implementação para AutocompleteJComboBox
		Map<Object, Object> cache = getCacheMap();
		if (cache == null || cache.isEmpty()) {
			setList(tipoProdutoDao.busca("tipoProduto.readtiposabstratos"));
			setCacheMap((Map<Object, Object>) tiposProdutos.stream()
					.collect(Collectors.toMap(tp -> ((SubTipoProduto) tp).getIdTipoItem(), tp -> tp)));
		} else {
			setList(new ArrayList(cache.values()));
		}
		return getList();

	}

	@Override
	public Object getItem() {

		return tipoProduto;
	}

	public void salva(Object descricao) throws Exception {
		buscaSuperTipoProduto("Genérico");
		SubTipoProduto superTipoProduto = tipoProduto;
		tipoProduto = new SubTipoProduto();
		tipoProduto.setDescricaoTipo(descricao.toString());
		tipoProduto.setSuperTipoProduto(superTipoProduto);

		tipoProduto.setDataInsercao(SingletonManager.getInstance().getData());

		salva(tipoProduto.getDescricaoTipo(), true, tipoProduto.getSuperTipoProduto());

	}

	private void buscaSuperTipoProduto(String string) throws Exception {
		try {
			busca(string);
		} catch (Exception e) {

			tipoProduto = tipoProdutoDao.update(new SubTipoProduto(string, getUsuarioLogado()));
		}

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

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
	public void validaExistente(String text) throws Exception {

		// if (tipoProduto == null || tipoProduto.getIdTipoItem() != null &&
		// tipoProduto.getIdTipoItem() != 0)
		// return;
		buscaTodos();
		List<SubTipoProduto> todosTipos = new ArrayList<>();
		tiposProdutos.stream().forEach(tipo -> {
			todosTipos.add((SubTipoProduto) tipo);
			((SubTipoProduto) tipo).getSubTiposProduto().stream().forEach(sub -> {
				todosTipos.add(sub);
			});
		});

		SubTipoProduto teste = todosTipos.stream()
				.filter(x -> ((SubTipoProduto) x).getDescricaoTipo().equalsIgnoreCase(text)).findAny().orElse(null);

		if (tipoProduto == null || tipoProduto.getIdTipoItem() == null && teste != null) {
			throw new Exception("Descrição de Categoria já existente.");
		}

	}

	@Override
	public void carregaCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public String validaExclusaoItem() {
		String string1 = "Categoria de Produtos contém ";
		String string2 = " A exclusão pode causar perda de dados. Deseja continuar?";
		if (!tipoProduto.getSubTiposProduto().isEmpty())
			return string1 + tipoProduto.getSubTiposProduto().size() + " sub-categorias cadastradas. " + string2;
		if (!((SubTipoProduto) tipoProduto).getItensEstoque().isEmpty())
			return string1 + ((SubTipoProduto) tipoProduto).getItensEstoque().size() + " ítens no estoque." + string2;
		;
		return null;
	}

	// public void iniciaTipoPecaRoupa() {
	//
	// tipoProduto = new SubTipoProduto();
	//
	// }

}
