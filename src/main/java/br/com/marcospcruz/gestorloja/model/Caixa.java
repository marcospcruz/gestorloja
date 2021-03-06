package br.com.marcospcruz.gestorloja.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
//@formatter:off
@NamedQueries({
		@NamedQuery(name = "caixa.findCaixaAberto", query = "select distinct c from Caixa c " 
				+ "LEFT JOIN c.vendas "
				+ "LEFT JOIN fetch c.usuarioAbertura u "
				+ "LEFT JOIN fetch c.usuarioFechamento u "
				+ "where c.dataFechamento=null"),
		@NamedQuery(name = "caixa.findAll", query = "select distinct c from Caixa c "
				+ "LEFT JOIN fetch c.vendas v "
				+ "LEFT JOIN fetch c.usuarioAbertura u "
				+ "LEFT JOIN fetch c.usuarioFechamento u "
				+ "LEFT JOIN fetch v.pagamento p") })
//@formatter:on
public class Caixa extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891194078556715854L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCaixa;
	private float saldoInicial;
	private float saldoFinal;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFechamento;
	@ManyToOne
	@JoinColumn(name = "idUsuarioAbertura")
	private Usuario usuarioAbertura;
	@ManyToOne
	@JoinColumn(name = "idUsuarioFechamento")
	private Usuario usuarioFechamento;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caixa")
	@OrderBy(value = "idVenda")
	private Set<Venda> vendas;

	public Caixa() {
		setDataAbertura(SingletonManager.getInstance().getData());
	}

	public int getIdCaixa() {
		return idCaixa;
	}

	public void setIdCaixa(int idCaixa) {
		this.idCaixa = idCaixa;
	}

	public Float getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(float saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Float getSaldoFinal() {
		return saldoFinal;
	}

	public void setSaldoFinal(float saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Usuario getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(Usuario usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public Usuario getUsuarioFechamento() {
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(Usuario usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
	}

	public Set<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataAbertura == null) ? 0 : dataAbertura.hashCode());
		result = prime * result + ((dataFechamento == null) ? 0 : dataFechamento.hashCode());
		result = prime * result + idCaixa;
		result = prime * result + Float.floatToIntBits(saldoFinal);
		result = prime * result + Float.floatToIntBits(saldoInicial);
		result = prime * result + ((usuarioAbertura == null) ? 0 : usuarioAbertura.hashCode());
		result = prime * result + ((usuarioFechamento == null) ? 0 : usuarioFechamento.hashCode());
		result = prime * result + ((vendas == null) ? 0 : vendas.hashCode());
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
		Caixa other = (Caixa) obj;
		if (dataAbertura == null) {
			if (other.dataAbertura != null)
				return false;
		} else if (!dataAbertura.equals(other.dataAbertura))
			return false;
		if (dataFechamento == null) {
			if (other.dataFechamento != null)
				return false;
		} else if (!dataFechamento.equals(other.dataFechamento))
			return false;
		if (idCaixa != other.idCaixa)
			return false;
		if (Float.floatToIntBits(saldoFinal) != Float.floatToIntBits(other.saldoFinal))
			return false;
		if (Float.floatToIntBits(saldoInicial) != Float.floatToIntBits(other.saldoInicial))
			return false;
		if (usuarioAbertura == null) {
			if (other.usuarioAbertura != null)
				return false;
		} else if (!usuarioAbertura.equals(other.usuarioAbertura))
			return false;
		if (usuarioFechamento == null) {
			if (other.usuarioFechamento != null)
				return false;
		} else if (!usuarioFechamento.equals(other.usuarioFechamento))
			return false;
		if (vendas == null) {
			if (other.vendas != null)
				return false;
		} else if (!vendas.equals(other.vendas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Caixa [idCaixa=" + idCaixa + ", saldoInicial=" + saldoInicial + ", saldoFinal=" + saldoFinal
				+ ", dataAbertura=" + dataAbertura + ", dataFechamento=" + dataFechamento + ", usuarioAbertura="
				+ usuarioAbertura + ", usuarioFechamento=" + usuarioFechamento + ", vendas=" + vendas + "]";
	}

}
