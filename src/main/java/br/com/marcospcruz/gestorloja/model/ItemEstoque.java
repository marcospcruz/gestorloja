package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//@formatter:off
@Entity
@Table(name = "Estoque")
@NamedQueries({
		@NamedQuery(name = "itemestoque.readCodigo", query = "select ie from ItemEstoque ie "
			+ "JOIN ie.produto p " 
			+ "where upper(ie.codigoDeBarras) = :codigo"),
		@NamedQuery(name = "itemestoque.readCodigoEstoque", query = "select ie from ItemEstoque ie "
				+ "JOIN ie.produto p " 
				+ "WHERE upper(ie.codigoDeBarras) = :codigo "
				+ "AND ie.quantidade>0"),
		@NamedQuery(name = "itemestoque.readDescricaoPecao", query = "select ie from ItemEstoque ie "
				+ "JOIN ie.produto p " 
				+ "where upper(p.descricaoProduto) like :descricao"),
		@NamedQuery(name = "itemEstoque.readAll", query = "select ie from ItemEstoque ie " 
				+ "JOIN ie.produto p "
				+ "JOIN p.tipoProduto t "
				+ "order by t.descricaoTipo"),
		@NamedQuery(name="itemEstoque.readProduto",query="select ie from ItemEstoque ie "
				+ "JOIN ie.produto p "
				+ "JOIN ie.fabricante f "
				+ "WHERE p.idProduto=:idProduto "
				+ "AND f.idFabricante=:idFabricante")

})
//@formatter:on
public class ItemEstoque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193836692123395239L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idItemEstoque;

	@ManyToOne
	@JoinColumn(name = "idProduto")
	private Produto produto;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "idFabricante")
	// @JoinTable(name = "itemEstoque_fabricante", joinColumns = {
	// @JoinColumn(name = "idItemEstoque") }, inverseJoinColumns = {
	// @JoinColumn(name = "idFabricante") })
	private Fabricante fabricante;
	private Integer quantidade;
	@Column(unique = true)
	private String codigoDeBarras;
	private Date dataContagem;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	private float valorUnitario;


	public Integer getIdItemEstoque() {
		return idItemEstoque;
	}

	public void setIdItemEstoque(Integer idItemEstoque) {
		this.idItemEstoque = idItemEstoque;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public Date getDataContagem() {
		return dataContagem;
	}

	public void setDataContagem(Date dataContagem) {
		this.dataContagem = dataContagem;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result + ((dataContagem == null) ? 0 : dataContagem.hashCode());
		result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
		result = prime * result + ((idItemEstoque == null) ? 0 : idItemEstoque.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
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
		ItemEstoque other = (ItemEstoque) obj;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (dataContagem == null) {
			if (other.dataContagem != null)
				return false;
		} else if (!dataContagem.equals(other.dataContagem))
			return false;
		if (fabricante == null) {
			if (other.fabricante != null)
				return false;
		} else if (!fabricante.equals(other.fabricante))
			return false;
		if (idItemEstoque == null) {
			if (other.idItemEstoque != null)
				return false;
		} else if (!idItemEstoque.equals(other.idItemEstoque))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (Float.floatToIntBits(valorUnitario) != Float.floatToIntBits(other.valorUnitario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return produto.getTipoProduto() + " " + fabricante + " " + produto;
	}

}
