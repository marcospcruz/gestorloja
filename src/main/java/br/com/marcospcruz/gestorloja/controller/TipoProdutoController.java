package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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
 * Classe respons�vel em gerenciar regras de neg�cio para Tipos de Pe�as
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
	 * M�todo Construtor sem Par�metros.
	 */
	public TipoProdutoController() {

		tipoProdutoDao = new CrudDao<>();

	}

	/**
	 * M�todo respons�vel em salvar novo tipo de roupa ou atualizar o j�
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

		// if (tiposProdutos == null || tiposProdutos.size() < 1)
		//
		// carregaTiposProdutos();

		return tiposProdutos;

	}

	public void setList(List tiposProdutos) {
		this.tiposProdutos = tiposProdutos;
	}

	public void setItem(Object tipoProduto) {
		// if (tipoProduto != null && !(tipoProduto instanceof TipoProduto)) {
		// tipoProduto = new SubTipoProduto(tipoProduto.toString(), getUsuarioLogado());
		// }
		// this.tipoProduto = (SubTipoProduto) tipoProduto;
		// // implementa��o para AutocompleteComboBox
		// if (tipoProduto != null)
		// setList((List) ((SubTipoProduto) tipoProduto).getSubTiposProduto());
		this.tipoProduto = (SubTipoProduto) tipoProduto;
	}

	public void carregaTiposProdutos() {

		setList(tipoProdutoDao.busca("tipoProduto.readtiposabstratos"));

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

		zeraAtributos();

		if (parametro.length() == 0) {

			throw new Exception(BUSCA_INVALIDA);

		}

		if (contemAcentuacao(parametro)) {

			buscaInWorkAround(parametro);

		}

		String valor = "%" + parametro.toUpperCase() + "%";

		tiposProdutos = tipoProdutoDao.buscaList("tipoProduto.readParametroLike", "descricao", valor);
		// setCacheMap((Map<Object, Object>) tiposProdutos.stream()
		// .collect(Collectors.toMap(tipo -> ((SubTipoProduto) tipo).getIdTipoItem(),
		// tipo -> tipo)));

		if (!tiposProdutos.isEmpty())

			tipoProduto = (SubTipoProduto) tiposProdutos.get(0);

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

			String descricao = peca.getDescricaoTipo();

			if (descricao.contains(parametro)) {

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
		tipoProduto = tipoProdutoDao.update(tipoProduto);
		if (tipoProduto == null || tipoProduto.getIdTipoItem() == null) {

			throw new Exception(REMOCAO_INVALIDA_EXCEPTION);

		}

		// if (tipoProduto.getProdutos() != null && tipoProduto.getProdutos().size() >
		// 0)
		//
		// throw new Exception(REMOCAO_TIPO_POPULADO);

		if (tipoProduto.getSubTiposProduto().size() > 0)
			throw new Exception(REMOCAO_SUPERTIPO_POPULADO);

		tipoProduto = tipoProdutoDao.busca(tipoProduto.getClass(), tipoProduto.getIdTipoItem());

		// SubTipoProduto superTipoProduto =
		// tipoProdutoDao.update(tipoProduto.getSuperTipoProduto());
		//
		// superTipoProduto.getSubTiposProduto().remove(tipoProduto);

		if (tipoProduto.getSuperTipoProduto() != null && tipoProduto.getItensEstoque() != null
				&& !tipoProduto.getItensEstoque().isEmpty()) {
			int qtEstoque = tipoProduto.getItensEstoque().size();
			throw new Exception("N�o � poss�vel excluir esta categoria! H� " + qtEstoque
					+ (qtEstoque > 1 ? " itens cadastrados" : " �tem cadastrado") + " no estoque.");
		}
		tipoProdutoDao.delete(tipoProduto);
		// tipoProdutoDao.update(superTipoProduto);

		setItem(null);

		setList(null);

	}

	/**
	 * 
	 * @param idSubProduto
	 */
	public void busca(Object id) {
		Integer idSubProduto = Integer.valueOf(id.toString());
		try {
			tipoProduto = tipoProdutoDao.busca(SubTipoProduto.class, idSubProduto);
			tipoProduto = tipoProdutoDao.update(tipoProduto);
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(getClass()).warn(e.getMessage());
		}

	}

	public List buscaTodos() {
		// implementa��o para AutocompleteJComboBox
		// if (getList() == null || getList().isEmpty())

		setList(tipoProdutoDao.busca("tipoProduto.readtiposabstratos"));
		return getList();

	}

	@Override
	public Object getItem() {

		return tipoProduto;
	}

	public void salva(Object descricao) throws Exception {
		buscaSuperTipoProduto("Gen�rico");
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
		tipoProduto = tipoProdutoDao.update(tipoProduto);
	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validaExistente(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void novoUsuario() {
		tipoProduto = new SubTipoProduto();

	}

	public TipoProduto buscaSuperCategoria(String string) {
		buscaTodos();
		return (TipoProduto) tiposProdutos.stream().filter(t -> ((TipoProduto) t).getDescricaoTipo().equals(string))
				.findFirst().orElse(null);

	}

	public void buscaTipoProduto(String string) {
		tipoProduto = tipoProdutoDao.busca("tipoProduto.readParametro", "descricao", string.toLowerCase());
	}

	public void attachEntidade(SubTipoProduto tipoProduto) throws Exception {
		setItem(tipoProduto);
		if (tipoProduto.getIdTipoItem() != null)
			salva();

	}

	// public void iniciaTipoPecaRoupa() {
	//
	// tipoProduto = new SubTipoProduto();
	//
	// }

}
