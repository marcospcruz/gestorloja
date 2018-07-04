package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

//@formatter:off
@Entity
@NamedQueries({
		@NamedQuery(name = "produto.readall", query = "select p from Produto p " 
),
		@NamedQuery(name = "produto.readparametrolike", query = "select p from Produto p "
				+ "where upper(p.descricaoProduto) like :descricao")})
		
//@formatter:on
public class Produto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1325592421055276202L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idProduto;

	private String descricaoProduto;

	private String unidadeMedida;

	// @OneToMany(targetEntity = ItemEstoque.class, fetch = FetchType.LAZY, mappedBy
	// = "produto")
	// @Fetch(FetchMode.JOIN)
	// private List<ItemEstoque> itensEstoque;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idTipoProduto")
	@OrderBy
	private SubTipoProduto tipoProduto;

	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	private Date dataInsercao;

	private boolean estoqueDedutivel;

	@OneToMany(mappedBy = "produto")
//	@Fetch(FetchMode.JOIN)
	private Collection<ItemEstoque> estoqueProduto;

	public Produto(TipoProduto tipoProduto, SubTipoProduto subTipoProduto, String descricao, String unidadeMedida) {
		setTipoProduto(subTipoProduto);
		getTipoProduto().setSuperTipoProduto((SubTipoProduto) tipoProduto);
		setDescricaoProduto(descricao);
		setUnidadeMedida(unidadeMedida);

	}

	public Produto() {
		setDataInsercao(SingletonManager.getInstance().getData());
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public SubTipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(SubTipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public Date getDataInsercao() {
		return dataInsercao;
	}

	public void setDataInsercao(Date dataInsercao) {
		this.dataInsercao = dataInsercao;
	}

	public Collection<ItemEstoque> getEstoqueProduto() {
		return estoqueProduto;
	}

	public void setEstoqueProduto(Collection<ItemEstoque> estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}

	@Override
	public String toString() {
		return descricaoProduto;
	}

	public boolean isEstoqueDedutivel() {
		return estoqueDedutivel;
	}

	public void setEstoqueDedutivel(boolean estoqueDedutivel) {
		this.estoqueDedutivel = estoqueDedutivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataInsercao == null) ? 0 : dataInsercao.hashCode());
		result = prime * result + ((descricaoProduto == null) ? 0 : descricaoProduto.hashCode());
		result = prime * result + (estoqueDedutivel ? 1231 : 1237);
		result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
		result = prime * result + ((unidadeMedida == null) ? 0 : unidadeMedida.hashCode());
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
		Produto other = (Produto) obj;
		if (dataInsercao == null) {
			if (other.dataInsercao != null)
				return false;
		} else if (!dataInsercao.equals(other.dataInsercao))
			return false;
		if (descricaoProduto == null) {
			if (other.descricaoProduto != null)
				return false;
		} else if (!descricaoProduto.equals(other.descricaoProduto))
			return false;
		if (estoqueDedutivel != other.estoqueDedutivel)
			return false;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (tipoProduto == null) {
			if (other.tipoProduto != null)
				return false;
		} else if (!tipoProduto.equals(other.tipoProduto))
			return false;
		if (unidadeMedida == null) {
			if (other.unidadeMedida != null)
				return false;
		} else if (!unidadeMedida.equals(other.unidadeMedida))
			return false;
		return true;
	}

}