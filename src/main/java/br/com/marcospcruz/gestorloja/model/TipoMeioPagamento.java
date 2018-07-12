package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TipoMeioPagamento")
@NamedQuery(name = "tipoMeioPagamento.buscaPorDescricao", query = "select t from TipoMeioPagamento t "
		+ "where t.descricaoTipoMeioPagamento=:descricao")
public class TipoMeioPagamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2192695069124255212L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idTipoMeioPagamento;
	private String descricaoTipoMeioPagamento;

	public TipoMeioPagamento() {
	}

	public TipoMeioPagamento(String descricaoTipoMeioPagamento) {
		this.descricaoTipoMeioPagamento = descricaoTipoMeioPagamento;
	}

	public int getIdTipoMeioPagamento() {
		return idTipoMeioPagamento;
	}

	public void setIdTipoMeioPagamento(int idTipoMeioPagamento) {
		this.idTipoMeioPagamento = idTipoMeioPagamento;
	}

	public String getDescricaoTipoMeioPagamento() {
		return descricaoTipoMeioPagamento;
	}

	public void setDescricaoTipoMeioPagamento(String descricaoTipoMeioPagamento) {
		this.descricaoTipoMeioPagamento = descricaoTipoMeioPagamento;
	}

	@Override
	public String toString() {
		return "TipoMeioPagamento [idTipoMeioPagamento=" + idTipoMeioPagamento + ", descricaoTipoMeioPagamento="
				+ descricaoTipoMeioPagamento + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricaoTipoMeioPagamento == null) ? 0 : descricaoTipoMeioPagamento.hashCode());
		result = prime * result + idTipoMeioPagamento;
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
		TipoMeioPagamento other = (TipoMeioPagamento) obj;
		if (descricaoTipoMeioPagamento == null) {
			if (other.descricaoTipoMeioPagamento != null)
				return false;
		} else if (!descricaoTipoMeioPagamento.equals(other.descricaoTipoMeioPagamento))
			return false;
//		if (idTipoMeioPagamento != other.idTipoMeioPagamento)
//			return false;
		return true;
	}

}
