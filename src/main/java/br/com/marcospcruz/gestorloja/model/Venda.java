package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "venda")
public class Venda implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7647914068901369106L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idVenda;
	private ItemEstoque itemEstoque;
	private Integer quantidade;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idVenda;
		result = prime * result + ((itemEstoque == null) ? 0 : itemEstoque.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "Venda [idVenda=" + idVenda + ", itemEstoque=" + itemEstoque + ", quantidade=" + quantidade
				+ ", operador=" + operador + "]";
	}

}
