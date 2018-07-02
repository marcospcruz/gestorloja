package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "idTipoMeioPagamento")
public abstract class MeioPagamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 119407608316667340L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idMeioPagamento;

	// @ManyToOne(cascade = CascadeType.MERGE)
	// @JoinColumn(name = "idTipoMeioPagamento")
	// private TipoMeioPagamento tipoMeioPagamento;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idPagamento")

	private Pagamento pagamento;
	private float valorPago;
	@ManyToOne(cascade = CascadeType.MERGE)
	private TipoMeioPagamento tipoMeioPagamento;
	private Date dataPagamento;
	private Usuario usuarioLogado;

	public TipoMeioPagamento getTipoMeioPagamento() {
		return tipoMeioPagamento;
	}

	public void setTipoMeioPagamento(TipoMeioPagamento tipoMeioPagamento) {
		this.tipoMeioPagamento = tipoMeioPagamento;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public int getIdMeioPagamento() {
		return idMeioPagamento;
	}

	public void setIdMeioPagamento(int idMeioPagamento) {
		this.idMeioPagamento = idMeioPagamento;
	}

	public float getValorPago() {
		return valorPago;
	}

	public void setValorPago(float valorPago) {
		this.valorPago = valorPago;
	}

	@Override
	public String toString() {
		return "MeioPagamento [idMeioPagamento=" + idMeioPagamento + ", pagamento=" + pagamento + ", valorPago="
				+ valorPago + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idMeioPagamento;
		result = prime * result + ((pagamento == null) ? 0 : pagamento.hashCode());
		result = prime * result + Float.floatToIntBits(valorPago);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeioPagamento other = (MeioPagamento) obj;
		if (idMeioPagamento != other.idMeioPagamento)
			return false;
		if (pagamento == null) {
			if (other.pagamento != null)
				return false;
		} else if (!pagamento.equals(other.pagamento))
			return false;
		if (Float.floatToIntBits(valorPago) != Float.floatToIntBits(other.valorPago))
			return false;
		return true;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;

	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;

	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

}
