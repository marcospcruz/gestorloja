package br.com.marcospcruz.gestorloja.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(value = "2")
public class MeioPagamentoCartaoDebito extends MeioPagamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5099581532103700407L;

	@Override
	public String toString() {
		return "MeioPagamentoCartaoDebito [toString()=" + super.toString() + "]";
	}

}
