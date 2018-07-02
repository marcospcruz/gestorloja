package br.com.marcospcruz.gestorloja.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;

/**
 * 
 * Implementation of the Searchable interface that searches a List of String
 * objects.
 * 
 * This implementation searches only the beginning of the words, and is not be
 * optimized
 * 
 * for very large Lists.
 * 
 * @author G. Cope
 *
 * 
 * 
 */

public class StringSearchable implements Searchable<String, String> {

	private List<String> terms = new ArrayList<>();
	private ControllerBase controller;

	/**
	 * 
	 * Constructs a new object based upon the parameter terms.
	 * 
	 * @param terms
	 *            The inventory of terms to search.
	 * 
	 */

	public StringSearchable(List<String> terms, ControllerBase controller) {
		this.controller = controller;
		this.terms.addAll(terms);

	}

	@Override

	public Collection<String> search(String value) {

		List<String> founds = new ArrayList<>();
		
		try {
			controller.busca(value);
			for(Object item:controller.getList()) {
				founds.add(item.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String s : terms) {

			if (s.indexOf(value) == 0) {

				founds.add(s);

			}

		}

		return founds;

	}

}