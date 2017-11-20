package br.com.marcospcruz.gestorloja.controller;

import java.util.List;

import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.Produto;

public abstract class AbstractController {

	protected static final String BUSCA_INVALIDA = "Busca Invïálida";

	protected boolean contemAcentuacao(String parametro) {

		String pattern = "ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½";

		for (char caractere : pattern.toCharArray())

			for (char c : parametro.toCharArray())

				if (c == caractere)

					return true;

		return false;

	}

	public abstract void busca(int id);

	public abstract List buscaTodos();

	public abstract List getList();

	public abstract void busca(String text) throws Exception;

	public abstract Object getItem();

	public abstract void setList(List list);

	public abstract void setItem(Object object);

	public abstract void excluir() throws Exception;

	public abstract void salva(String text, boolean b, Object object) throws Exception ;

	public abstract void salva(Object object) throws Exception;

}
