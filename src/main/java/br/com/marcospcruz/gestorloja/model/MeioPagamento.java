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

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

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
	private boolean estornado;

	public MeioPagamento(String descricaoMeioPagamento) {
		this();
		setDescricao(descricaoMeioPagamento);
	}

	public MeioPagamento() {
		setUsuarioLogado(SingletonManager.getInstance().getUsuarioLogado());
	}

	public TipoMeioPagamento getTipoMeioPagamento() {
		if (tipoMeioPagamento == null)
			tipoMeioPagamento = new TipoMeioPagamento();
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
		result = prime * result + ((dataPagamento == null) ? 0 : dataPagamento.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + idMeioPagamento;
		result = prime * result + ((pagamento == null) ? 0 : pagamento.hashCode());
		result = prime * result + parcelas;
		result = prime * result + ((tipoMeioPagamento == null) ? 0 : tipoMeioPagamento.hashCode());
		result = prime * result + ((usuarioLogado == null) ? 0 : usuarioLogado.hashCode());
		result = prime * result + Float.floatToIntBits(valorPago);
		return result;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeioPagamento other = (MeioPagamento) obj;
		if (dataPagamento == null) {
			if (other.dataPagamento != null)
				return false;
		}
		// else if (!dataPagamento.equals(other.dataPagamento))
		// return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			// return false;
			// if (idMeioPagamento != other.idMeioPagamento)
			// return false;
			// if (pagamento == null) {
			// if (other.pagamento != null)
			// return false;
			// } else if (!pagamento.equals(other.pagamento))
			// return false;
			if (parcelas != other.parcelas)
				return false;
		if (tipoMeioPagamento == null) {
			if (other.tipoMeioPagamento != null)
				return false;
		} else if (!tipoMeioPagamento.equals(other.tipoMeioPagamento))
			return false;
		// if (usuarioLogado == null) {
		// if (other.usuarioLogado != null)
		// return false;
		// } else if (!usuarioLogado.equals(other.usuarioLogado))
		// return false;
		if (Float.floatToIntBits(valorPago) != Float.floatToIntBits(other.valorPago))
			return false;
		return true;
	}

	public void setEstornado(boolean estornado) {
		this.estornado = estornado;

	}

	public boolean isEstornado() {
		return estornado;
	}

}
