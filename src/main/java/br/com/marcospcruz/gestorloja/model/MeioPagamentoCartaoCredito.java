package br.com.marcospcruz.gestorloja.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "3")
public class MeioPagamentoCartaoCredito extends MeioPagamento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7111063265875821631L;
	private int quantidadeParcelas;

	public int getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(int quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + quantidadeParcelas;
		return result;
	}

	@Override
	public String toString() {
		return "MeioPagamentoCartaoCredito [quantidadeParcelas=" + quantidadeParcelas + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeioPagamentoCartaoCredito other = (MeioPagamentoCartaoCredito) obj;
		if (quantidadeParcelas != other.quantidadeParcelas)
			return false;
		return true;
	}

}
