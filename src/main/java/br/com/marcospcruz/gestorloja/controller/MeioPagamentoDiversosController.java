package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.Operacao;

public class MeioPagamentoDiversosController extends ControllerBase {

	private List list;

	@Override
	public void busca(Object id) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List buscaTodos() {
		Map<Object, Object> cache = getCacheMap();
		if (cache == null || cache.isEmpty()) {
			Crud<MeioPagamento> dao = new CrudDao<>();
			setList(dao.busca("meioPagamento.readMeioPagamentosDiversos"));
			list = getList();
			setCacheMap((Map<Object, Object>) list.stream()
					.collect(Collectors.toMap(mp -> ((MeioPagamento) mp).getIdMeioPagamento(), mp -> mp)));
		} else if (list == null || list.isEmpty()) {
			list = new ArrayList(cache.values());
		}

		return null;
	}

	@Override
	public List getList() {

		return list;
	}

	@Override
	public void busca(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setList(List list) {
		this.list = list;

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
