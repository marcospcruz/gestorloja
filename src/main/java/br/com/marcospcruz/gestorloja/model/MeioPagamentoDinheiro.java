package br.com.marcospcruz.gestorloja.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(value = "1")
public class MeioPagamentoDinheiro extends MeioPagamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1305146234428139911L;

//	@ManyToOne(cascade = CascadeType.MERGE)
//	@JoinColumn(name = "idTipoMeioPagamento")
//	private TipoMeioPagamento tipoMeioPagamento;

//	public TipoMeioPagamento getTipoMeioPagamento() {
//		return tipoMeioPagamento;
//	}
//
//	public void setTipoMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {
//		this.tipoMeioPagamento = tipoMeioPagamento;
//	}

	@Override
	public String toString() {
		return "MeioPagamentoDinheiro [getPagamento()=" + getPagamento() + ", getIdMeioPagamento()="
				+ getIdMeioPagamento() + ", getValorPago()=" + getValorPago() + ", toString()=" + super.toString()
				+ ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + "]";
	}

}
