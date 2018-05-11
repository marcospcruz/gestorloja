package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

//@formatter:off
@Entity
@NamedQueries({
		@NamedQuery(name = "produto.readall", query = "select p from Produto p " 
				+ "LEFT JOIN fetch p.itemEstoque "
				+ "LEFT JOIN fetch p.fabricante "),
		@NamedQuery(name = "produto.readparametrolike", query = "select p from Produto p "
				+ "LEFT JOIN fetch p.itemEstoque " 
				+ "LEFT JOIN fetch p.fabricante "
				+ "where upper(p.descricaoProduto) like :descricao"),
		@NamedQuery(name="produto.readProdutoFabricante",query="select p from Produto p "
				+ "JOIN p.fabricante f "
				+ "WHERE p.descricaoProduto = :descricao "
				+ "AND f.nome = :marca")})
		
//@formatter:on
public class Produto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 966393974554317965L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idProduto;

	private String descricaoProduto;

	private String unidadeMedida;

	private float valorUnitario;
	@Column(unique = true)
	private String codigoDeBarras;

	@OneToOne(targetEntity = ItemEstoque.class, fetch = FetchType.EAGER, mappedBy = "produto")
	@Fetch(FetchMode.JOIN)
	private ItemEstoque itemEstoque;

	@ManyToOne
	@JoinColumn(name = "idTipoProduto")
	@OrderBy
	private SubTipoProduto tipoProduto;

	@ManyToOne
	@JoinColumn(name = "idFabricante")
	private Fabricante fabricante;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	private Date dataInsercao;

	public Produto(TipoProduto tipoProduto, SubTipoProduto subTipoProduto, String descricao, String unidadeMedida,
			String codigoDeBarras, Fabricante fabricante, float valorUnitario) {
		setTipoProduto(subTipoProduto);
		getTipoProduto().setSuperTipoProduto((SubTipoProduto) tipoProduto);
		setDescricaoProduto(descricao);
		setUnidadeMedida(unidadeMedida);
		setValorUnitario(valorUnitario);
		setCodigoDeBarras(codigoDeBarras);
		setFabricante(fabricante);
	}

	public Produto() {
		setDataInsercao(new Date());
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

	public float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public ItemEstoque getItemEstoque() {
		return itemEstoque;
	}

	public void setItemEstoque(ItemEstoque itemEstoque) {
		this.itemEstoque = itemEstoque;
	}

	public SubTipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(SubTipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
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

	@Override
	public String toString() {
		return descricaoProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result + ((descricaoProduto == null) ? 0 : descricaoProduto.hashCode());
		result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
		result = prime * result + ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + ((itemEstoque == null) ? 0 : itemEstoque.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
		result = prime * result + ((unidadeMedida == null) ? 0 : unidadeMedida.hashCode());
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
		Produto other = (Produto) obj;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (descricaoProduto == null) {
			if (other.descricaoProduto != null)
				return false;
		} else if (!descricaoProduto.equals(other.descricaoProduto))
			return false;
		if (fabricante == null) {
			if (other.fabricante != null)
				return false;
		} else if (!fabricante.equals(other.fabricante))
			return false;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
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
		if (Float.floatToIntBits(valorUnitario) != Float.floatToIntBits(other.valorUnitario))
			return false;
		return true;
	}

}