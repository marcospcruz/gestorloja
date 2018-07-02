package br.com.marcospcruz.gestorloja.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author MarcosPereiradaCruz
 *
 */
@Entity
// @Table(name = "MeioPagamento")
@DiscriminatorValue(value = "4")
public class MeioPagamentoOutros extends MeioPagamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4653713610666074696L;
	private String descricaoPagamentoOutros;

	public String getDescricaoPagamentoOutros() {
		return descricaoPagamentoOutros;
	}

	public void setDescricaoPagamentoOutros(String descricaoPagamentoOutros) {
		this.descricaoPagamentoOutros = descricaoPagamentoOutros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricaoPagamentoOutros == null) ? 0 : descricaoPagamentoOutros.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeioPagamentoOutros other = (MeioPagamentoOutros) obj;
		if (descricaoPagamentoOutros == null) {
			if (other.descricaoPagamentoOutros != null)
				return false;
		} else if (!descricaoPagamentoOutros.equals(other.descricaoPagamentoOutros))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MeioPagamentoOutros [descricaoPagamentoOutros=" + descricaoPagamentoOutros + ", toString()="
				+ super.toString() + "]";
	}

}
