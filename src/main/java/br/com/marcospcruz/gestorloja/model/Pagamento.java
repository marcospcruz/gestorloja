package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Pagamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2356444305643322916L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idPagamento;
	@OneToMany(cascade = CascadeType.ALL,mappedBy="pagamento")
	private List<MeioPagamento> meiosPagamento;
	private float valorPagamento;
	private float trocoPagamento;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idVenda")
	private Venda venda;

	private Date dataVenda;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario usuarioLogado;

	public int getIdPagamento() {
		return idPagamento;
	}

	public List<MeioPagamento> getMeiosPagamento() {
		if (meiosPagamento == null)
			meiosPagamento = new ArrayList<>();
		return meiosPagamento;
	}

	public void setMeiosPagamento(List<MeioPagamento> meiosPagamento) {
		this.meiosPagamento = meiosPagamento;
	}

	public void setIdPagamento(int idPagamento) {
		this.idPagamento = idPagamento;
	}

	public float getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(float valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public float getTrocoPagamento() {
		return trocoPagamento;
	}

	public void setTrocoPagamento(float trocoPagamento) {
		this.trocoPagamento = trocoPagamento;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	@Override
	public String toString() {
		return "Pagamento [idPagamento=" + idPagamento + ", meiosPagamento=" + meiosPagamento + ", valorPagamento="
				+ valorPagamento + ", trocoPagamento=" + trocoPagamento + ", venda=" + venda + ", dataVenda="
				+ dataVenda + ", usuarioLogado=" + usuarioLogado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result + idPagamento;
		result = prime * result + ((meiosPagamento == null) ? 0 : meiosPagamento.hashCode());
		result = prime * result + Float.floatToIntBits(trocoPagamento);
		result = prime * result + ((usuarioLogado == null) ? 0 : usuarioLogado.hashCode());
		result = prime * result + Float.floatToIntBits(valorPagamento);
		// result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		Pagamento other = (Pagamento) obj;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (idPagamento != other.idPagamento)
			return false;
		if (meiosPagamento == null) {
			if (other.meiosPagamento != null)
				return false;
		} else if (!meiosPagamento.equals(other.meiosPagamento))
			return false;
		if (Float.floatToIntBits(trocoPagamento) != Float.floatToIntBits(other.trocoPagamento))
			return false;
		if (usuarioLogado == null) {
			if (other.usuarioLogado != null)
				return false;
		} else if (!usuarioLogado.equals(other.usuarioLogado))
			return false;
		if (Float.floatToIntBits(valorPagamento) != Float.floatToIntBits(other.valorPagamento))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}

	public void setdataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;

	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

}
