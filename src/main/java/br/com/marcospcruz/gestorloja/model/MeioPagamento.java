package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MeioPagamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 119407608316667340L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idMeioPagamento;

	@ManyToOne
	@JoinColumn(name = "idPagamento")
	private Pagamento pagamento;

	private float valorPago;
	@ManyToOne
	@JoinColumn(name = "idTipoMeioPagamento")
	private TipoMeioPagamento tipoMeioPagamento;
	private Date dataPagamento;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOperador")
	private Usuario usuarioLogado;

	private int parcelas;
	private String descricao;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "MeioPagamento [idMeioPagamento=" + idMeioPagamento + ", pagamento=" + pagamento + ", valorPago="
				+ valorPago + ", tipoMeioPagamento=" + tipoMeioPagamento + ", dataPagamento=" + dataPagamento
				+ ", usuarioLogado=" + usuarioLogado + ", parcelas=" + parcelas + ", descricao=" + descricao + "]";
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

	public void setParcelas(int qtParcelas) {
		this.parcelas = qtParcelas;

	}

	public int getParcelas() {
		return parcelas;
	}

}
