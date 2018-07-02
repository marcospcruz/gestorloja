package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class HistoricoOperacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7414908968988331865L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idHistoricoOperacao;
	@ManyToOne
	@JoinColumn(name = "idOperacao")
	private Operacao operacao;
	@ManyToOne
	@JoinColumn(name = "idItemEstoque")
	private ItemEstoque itemEstoque;
	private int quantidade;
	private float valorUnitario;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataOperacao;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	public HistoricoOperacao() {

	}

	public int getIdHistoricoOperacao() {
		return idHistoricoOperacao;
	}

	public void setIdHistoricoOperacao(int idHistoricoOperacao) {
		this.idHistoricoOperacao = idHistoricoOperacao;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public ItemEstoque getItemEstoque() {
		return itemEstoque;
	}

	public void setItemEstoque(ItemEstoque itemEstoque) {
		this.itemEstoque = itemEstoque;
		setQuantidade(itemEstoque.getQuantidade());
		setValorUnitario(itemEstoque.getValorUnitario());
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Override
	public String toString() {
		return "HistoricoOperacao [idHistoricoOperacao=" + idHistoricoOperacao + ", operacao=" + operacao
				+ ", itemEstoque=" + itemEstoque + ", quantidade=" + quantidade + ", valorUnitario=" + valorUnitario
				+ ", dataOperacao=" + dataOperacao + ", operador=" + operador + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataOperacao == null) ? 0 : dataOperacao.hashCode());
		result = prime * result + idHistoricoOperacao;
		result = prime * result + ((itemEstoque == null) ? 0 : itemEstoque.hashCode());
		result = prime * result + ((operacao == null) ? 0 : operacao.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + quantidade;
		result = prime * result + Float.floatToIntBits(valorUnitario);
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
		HistoricoOperacao other = (HistoricoOperacao) obj;
		if (dataOperacao == null) {
			if (other.dataOperacao != null)
				return false;
		} else if (!dataOperacao.equals(other.dataOperacao))
			return false;
		if (idHistoricoOperacao != other.idHistoricoOperacao)
			return false;
		if (itemEstoque == null) {
			if (other.itemEstoque != null)
				return false;
		} else if (!itemEstoque.equals(other.itemEstoque))
			return false;
		if (operacao == null) {
			if (other.operacao != null)
				return false;
		} else if (!operacao.equals(other.operacao))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (quantidade != other.quantidade)
			return false;
		if (Float.floatToIntBits(valorUnitario) != Float.floatToIntBits(other.valorUnitario))
			return false;
		return true;
	}

}
