package br.com.marcospcruz.gestorloja.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
@Table(name = "TipoProduto")
//@formatter:off
@NamedQueries({ 
	@NamedQuery(name = "tipoProduto.readAll", query = "select distinct t from SubTipoProduto t "
		+ "LEFT JOIN FETCH t.subTiposProduto " 
//		+ "LEFT JOIN FETCH t.itensEstoque " 
		+ "order by t.descricaoTipo"),
		@NamedQuery(name = "tipoProduto.readtiposabstratos", query = "select distinct t from SubTipoProduto t "
				+ "LEFT JOIN FETCH t.subTiposProduto " 
//				+ "LEFT JOIN FETCH t.itensEstoque "
				+ "where t.superTipoProduto is null order by t.descricaoTipo"),
		@NamedQuery(name = "tipoProduto.readParametro", query = "select distinct t from SubTipoProduto t "
				+ "LEFT JOIN FETCH t.subTiposProduto " 
//				+ "LEFT JOIN FETCH t.itensEstoque "
				+ "where lower(t.descricaoTipo) = :descricao"),
		@NamedQuery(name = "tipoProduto.readParametroLike", query = "select distinct t from SubTipoProduto t "
				+ "LEFT JOIN FETCH t.subTiposProduto " 
//				+ "LEFT JOIN FETCH t.itensEstoque "
				+ "where UPPER(t.descricaoTipo) like :descricao "
		// + "and t.superTipoProduto is null"
		) })
public class SubTipoProduto extends TipoProduto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7095619182533502712L;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idSuperTipoProduto")
	private SubTipoProduto superTipoProduto;

	@OneToMany(mappedBy = "tipoProduto", fetch = FetchType.EAGER,orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ItemEstoque> itensEstoque;

	private String sexo;

	/**
	 * M�todo Construtor
	 */
	public SubTipoProduto() {

	}

	public SubTipoProduto(String descricao, Usuario operador) {
		setDescricaoTipo(descricao);
		setDataInsercao(SingletonManager.getInstance().getData());
		setOperador(operador);

	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public SubTipoProduto getSuperTipoProduto() {
		return superTipoProduto;
	}

	public void setSuperTipoProduto(SubTipoProduto superTipoProduto) {
		this.superTipoProduto = superTipoProduto;
	}

	public List<ItemEstoque> getItensEstoque() {
		return itensEstoque;
	}

	public void setItensEstoque(List<ItemEstoque> itensEstoque) {
		this.itensEstoque = itensEstoque;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((itensEstoque == null) ? 0 : itensEstoque.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((superTipoProduto == null) ? 0 : superTipoProduto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubTipoProduto other = (SubTipoProduto) obj;
		if (itensEstoque == null) {
			if (other.itensEstoque != null)
				return false;
		} else if (!itensEstoque.equals(other.itensEstoque))
			return false;
		if (sexo == null) {
			if (other.sexo != null)
				return false;
		} else if (!sexo.equals(other.sexo))
			return false;
		if (superTipoProduto == null) {
			if (other.superTipoProduto != null)
				return false;
		} else if (!superTipoProduto.equals(other.superTipoProduto))
			return false;
		return true;
	}

	public String toString() {

		return getDescricaoTipo();

	}

}