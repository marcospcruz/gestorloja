package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
@Table(name = "Venda")
@NamedQuery(name = "venda.findVenda", query = "select v from Venda v " + "join fetch v.itensVenda "
		+ "where v.idVenda=:id")
public class Venda implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -90043730582596068L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idVenda;

	private float totalVendido;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "venda")
	// @MapKeyJoinColumn(name = "codigoDeBarras", table = "estoque")
	// private Map<String, ItemVenda> itensVenda;
	private List<ItemVenda> itensVenda;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	@OneToOne(cascade = CascadeType.MERGE)
	// (cascade = { CascadeType.ALL})
	@JoinColumn(name = "idPagamento")
	private Pagamento pagamento;
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	// (cascade = CascadeType.ALL)
	@JoinColumn(name = "idCaixa")
	private Caixa caixa;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	private int porcentagemDesconto;

	private boolean estornado;

	public Venda() {
		setDataVenda(SingletonManager.getInstance().getData());
		itensVenda = new ArrayList<>();
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public int getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(int idVenda) {
		this.idVenda = idVenda;
	}

	public float getTotalVendido() {
		return totalVendido;
	}

	public void setTotalVendido(float totalVendido) {
		this.totalVendido = totalVendido;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public Pagamento getPagamento() {
		if (pagamento == null)
			pagamento = new Pagamento();
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public void setPorcentagemDesconto(int porcentagemDesconto) {
		this.porcentagemDesconto = porcentagemDesconto;

	}

	public float getPorcentagemDesconto() {
		return porcentagemDesconto;
	}

	@Override
	public String toString() {
		return "Venda [idVenda=" + idVenda + ", totalVendido=" + totalVendido + ", itensVenda=" + itensVenda
				+ ", dataVenda=" + dataVenda + ", pagamento=" + pagamento + ", operador=" + operador
				+ ", porcentagemDesconto=" + porcentagemDesconto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result + idVenda;
		// result = prime * result + ((itensVenda == null) ? 0 : itensVenda.hashCode());
		result = prime * result + ((pagamento == null) ? 0 : pagamento.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + Float.floatToIntBits(porcentagemDesconto);
		result = prime * result + Float.floatToIntBits(totalVendido);
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
		Venda other = (Venda) obj;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (idVenda != other.idVenda)
			return false;
		if (itensVenda == null) {
			if (other.itensVenda != null)
				return false;
		} else if (!itensVenda.equals(other.itensVenda))
			return false;
		if (pagamento == null) {
			if (other.pagamento != null)
				return false;
		} else if (!pagamento.equals(other.pagamento))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (Float.floatToIntBits(porcentagemDesconto) != Float.floatToIntBits(other.porcentagemDesconto))
			return false;
		if (Float.floatToIntBits(totalVendido) != Float.floatToIntBits(other.totalVendido))
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
