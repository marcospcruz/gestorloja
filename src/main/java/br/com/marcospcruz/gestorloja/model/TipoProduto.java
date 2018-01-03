package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "idSuperTipo")
public abstract class TipoProduto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -798434974846591857L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idTipoItem;

	@Column(unique = true)
	private String descricaoTipo;

	@OneToMany(mappedBy = "superTipoProduto")
	@OrderBy("descricaoTipo")
	private Collection<SubTipoProduto> subTiposProduto;

	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInsercao;

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public void setIdTipoItem(Integer idTipoItem) {
		this.idTipoItem = idTipoItem;
	}

	public Integer getIdTipoItem() {
		return idTipoItem;
	}

	public void setIdTipoPecaRoupa(Integer idTipoItem) {
		this.idTipoItem = idTipoItem;
	}

	public String getDescricaoTipo() {
		return descricaoTipo;
	}

	public void setDescricaoTipo(String descricaoTipo) {
		this.descricaoTipo = descricaoTipo;
	}

	public Collection<SubTipoProduto> getSubTiposProduto() {
		return subTiposProduto;
	}

	public void setSubTiposProduto(Collection<SubTipoProduto> subTiposProduto) {
		this.subTiposProduto = subTiposProduto;
	}

	public void setDataInsercao(Date dataInsercao) {
		this.dataInsercao = dataInsercao;
	}

	public Date getDataInsercao() {
		return dataInsercao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricaoTipo == null) ? 0 : descricaoTipo.hashCode());
		result = prime * result + ((idTipoItem == null) ? 0 : idTipoItem.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((subTiposProduto == null) ? 0 : subTiposProduto.hashCode());
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
		TipoProduto other = (TipoProduto) obj;
		if (descricaoTipo == null) {
			if (other.descricaoTipo != null)
				return false;
		} else if (!descricaoTipo.equals(other.descricaoTipo))
			return false;
		if (idTipoItem == null) {
			if (other.idTipoItem != null)
				return false;
		} else if (!idTipoItem.equals(other.idTipoItem))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (subTiposProduto == null) {
			if (other.subTiposProduto != null)
				return false;
		} else if (!subTiposProduto.equals(other.subTiposProduto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return descricaoTipo;
	}

}
