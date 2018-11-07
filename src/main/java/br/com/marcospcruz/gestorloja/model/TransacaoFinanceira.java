package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
public class TransacaoFinanceira implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6962908827365885221L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTransacaoFinanceira;
	@ManyToOne
	@JoinColumn(name = "idCaixa")
	private Caixa caixa;
	@OneToOne(cascade= {CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name = "idMeioPagamento")
	private MeioPagamento meioPagamento;
	@ManyToOne
	@JoinColumn(name = "idOperacao")
	private Operacao operacao;
	private String motivo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTransacao;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario usuario;
	private float valorTransacaoFinanceira;

	public TransacaoFinanceira() {
		setDataTransacao(SingletonManager.getInstance().getData());
		setUsuario(SingletonManager.getInstance().getUsuarioLogado());
	}

	public int getIdTransacaoFinanceira() {
		return idTransacaoFinanceira;
	}

	public void setIdTransacaoFinanceira(int idTransacaoFinanceira) {
		this.idTransacaoFinanceira = idTransacaoFinanceira;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Float getValorTransacaoFinanceira() {
		return valorTransacaoFinanceira;
	}

	public void setValorTransacaoFinanceira(float valorTransacaoFinanceira) {
		this.valorTransacaoFinanceira = valorTransacaoFinanceira;
	}

	@Override
	public String toString() {
		return "TransacaoFinanceira [idTransacaoFinanceira=" + idTransacaoFinanceira + ", caixa=" + caixa
				+ ", meioPagamento=" + meioPagamento + ", operacao=" + operacao + ", motivo=" + motivo
				+ ", dataTransacao=" + dataTransacao + ", usuario=" + usuario + ", valorTransacaoFinanceira="
				+ valorTransacaoFinanceira + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caixa == null) ? 0 : caixa.hashCode());
		result = prime * result + ((dataTransacao == null) ? 0 : dataTransacao.hashCode());
		result = prime * result + idTransacaoFinanceira;
		result = prime * result + ((meioPagamento == null) ? 0 : meioPagamento.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result + ((operacao == null) ? 0 : operacao.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + Float.floatToIntBits(valorTransacaoFinanceira);
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
		TransacaoFinanceira other = (TransacaoFinanceira) obj;
		if (caixa == null) {
			if (other.caixa != null)
				return false;
		} else if (!caixa.equals(other.caixa))
			return false;
		if (dataTransacao == null) {
			if (other.dataTransacao != null)
				return false;
		} else if (!dataTransacao.equals(other.dataTransacao))
			return false;
		if (idTransacaoFinanceira != other.idTransacaoFinanceira)
			return false;
		if (meioPagamento == null) {
			if (other.meioPagamento != null)
				return false;
		} else if (!meioPagamento.equals(other.meioPagamento))
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		if (operacao == null) {
			if (other.operacao != null)
				return false;
		} else if (!operacao.equals(other.operacao))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (Float.floatToIntBits(valorTransacaoFinanceira) != Float.floatToIntBits(other.valorTransacaoFinanceira))
			return false;
		return true;
	}

}
