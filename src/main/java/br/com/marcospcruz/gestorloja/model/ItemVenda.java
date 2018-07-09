package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "itemVenda")
public class ItemVenda implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7647914068901369106L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idVenda;
	@ManyToOne
	@JoinColumn(name = "idItemEstoque")
	private ItemEstoque itemEstoque;
	private Integer quantidade;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;
	private float valorVendido;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	@ManyToOne
	@JoinColumn(name = "idVenda", insertable = false, updatable = false)
	private Venda venda;

	public void setItemEstoque(ItemEstoque itemEstoque) {
		this.itemEstoque = itemEstoque;

	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public int getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(int idVenda) {
		this.idVenda = idVenda;
	}

	public ItemEstoque getItemEstoque() {
		return itemEstoque;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public float getValorVendido() {
		return valorVendido;
	}

	public void setValorVendido(float valorVendido) {
		this.valorVendido = valorVendido;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result + idVenda;
		result = prime * result + ((itemEstoque == null) ? 0 : itemEstoque.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + Float.floatToIntBits(valorVendido);
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		ItemVenda other = (ItemVenda) obj;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (idVenda != other.idVenda)
			return false;
		if (itemEstoque == null) {
			if (other.itemEstoque != null)
				return false;
		} else if (!itemEstoque.equals(other.itemEstoque))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (Float.floatToIntBits(valorVendido) != Float.floatToIntBits(other.valorVendido))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemVenda [idVenda=" + idVenda + ", itemEstoque=" + itemEstoque + ", quantidade=" + quantidade
				+ ", operador=" + operador + ", valorVendido=" + valorVendido + ", dataVenda=" + dataVenda + ", venda="
				+ venda + "]";
	}

}
