package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
@NamedQueries({
		@NamedQuery(name = "operacao.findOperacao", query = "select o from Operacao o  where descricao=:descricao") })
public class Operacao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2885276051452312103L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idOperacao;
	private String descricao;
	private Date dataCriacao;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	public Operacao(String descricao) {
		this();
		setDescricao(descricao);
	}

	public Operacao() {
		setDataCriacao(SingletonManager.getInstance().getData());
	}

	public Operacao(int idOperacao) {
		this();
		setIdOperacao(idOperacao);
	}

	public Operacao(String string, Usuario usuario) {
		this(string);
		setOperador(usuario);
	}

	public int getIdOperacao() {
		return idOperacao;
	}

	public void setIdOperacao(int idOperacao) {
		this.idOperacao = idOperacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	@Override
	public String toString() {
		return "Operacao [idOperacao=" + idOperacao + ", descricao=" + descricao + ", dataCriacao=" + dataCriacao
				+ ", operador=" + operador + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + idOperacao;
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
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
		Operacao other = (Operacao) obj;
		if (dataCriacao == null) {
			if (other.dataCriacao != null)
				return false;
		} else if (!dataCriacao.equals(other.dataCriacao))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (idOperacao != other.idOperacao)
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		return true;
	}

}
