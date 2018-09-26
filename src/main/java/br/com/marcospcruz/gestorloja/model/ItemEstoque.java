package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

//@formatter:off
@Entity
@Table(name = "Estoque")
@NamedQueries({
		@NamedQuery(name = "itemestoque.readCodigo", query = "select distinct ie from ItemEstoque ie "
			+ "JOIN ie.fabricante "
			+ "JOIN ie.produto p "
			+"JOIN ie.tipoProduto t "
			+ "where upper(ie.codigoDeBarras) = :codigo"),
		@NamedQuery(name = "itemestoque.readCodigoEstoque", query = "select distinct ie from ItemEstoque ie "
				+ "JOIN ie.produto p " 
				+"JOIN ie.tipoProduto t "
				+ "WHERE upper(ie.codigoDeBarras) = :codigo "
//				+ "AND ie.quantidade>0"
				),
		@NamedQuery(name = "itemestoque.readDescricaoPeca", query = "select distinct ie from ItemEstoque ie "
				+ "JOIN ie.produto p " 
				+"JOIN ie.tipoProduto t "
				+ "where "
				+ "upper(t.descricaoTipo) like :descricaoTipo "
				+ "AND upper(p.descricaoProduto) like :descricaoProduto"
				),
		@NamedQuery(name = "itemestoque.readDescricaoProduto", query = "select distinct ie from ItemEstoque ie "
				+ "JOIN ie.produto p "
				+"JOIN ie.tipoProduto t "
				+ "where "
				+ "upper(p.descricaoProduto) like :descricaoProduto"),
		@NamedQuery(name = "itemestoque.readDescricaoTipo", query = "select distinct ie from ItemEstoque ie "
				+ "JOIN ie.fabricante f "
				+ "JOIN ie.produto p " 
				+ "JOIN ie.tipoProduto t "
				+ "JOIN t.superTipoProduto st "
				+ "where "
				+ "upper(t.descricaoTipo) like :descricaoTipo "
				+ "OR UPPER(st.descricaoTipo) like :descricaoTipo "
				+ "order by f.nome,t.descricaoTipo"
				),
		@NamedQuery(name = "itemEstoque.readAll", query = "select distinct ie from ItemEstoque ie " 
				+ "JOIN fetch ie.fabricante f "
				+ "JOIN fetch ie.produto p "
				+ "JOIN fetch ie.tipoProduto t "
				+ "LEFT JOIN fetch t.superTipoProduto t "
				+ "order by f.nome, t.descricaoTipo"),
		@NamedQuery(name="itemEstoque.readProduto",query="select distinct ie from ItemEstoque ie "
				+ "JOIN ie.produto p "
				+"JOIN ie.tipoProduto t "
				+ "JOIN ie.fabricante f "
				+ "WHERE p.idProduto=:idProduto "
				+ "AND f.idFabricante=:idFabricante "
				+ "AND t.idTipoItem=:idTipoProduto"),
		
		@NamedQuery(name = "itemestoque.readTipoFabricante", query = "select distinct ie from ItemEstoque ie "
				+ "JOIN ie.produto p " 
				+ "JOIN ie.fabricante f "
				+ "JOIN ie.tipoProduto t "
				+ "where "
				+ "upper(t.descricaoTipo) like :descricaoTipo "
				+ "AND upper(p.descricaoProduto) like :descricaoProduto "
				+ "AND upper(f.nome) like :nomeFabricante")
		

})
//@formatter:on
public class ItemEstoque implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193836692123395239L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idItemEstoque;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idProduto")
	private Produto produto;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "idTipoItem")
	private SubTipoProduto tipoProduto;

	@ManyToOne(optional = false, cascade = CascadeType.MERGE)
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
	private boolean estoqueDedutivel;
	private float valorUnitario;
	private float valorCusto;

	public ItemEstoque() {
		setDataContagem(SingletonManager.getInstance().getData());
		setEstoqueDedutivel(Boolean.TRUE);
		setQuantidade(0);
		// setOperador(SingletonManager.getInstance().getUsuarioLogado());
	}

	public Integer getIdItemEstoque() {
		return idItemEstoque;
	}

	public void setIdItemEstoque(Integer idItemEstoque) {
		this.idItemEstoque = idItemEstoque;
	}

	public Produto getProduto() {
		return produto;
	}

	public boolean isEstoqueDedutivel() {
		return estoqueDedutivel;
	}

	public void setEstoqueDedutivel(boolean estoqueDedutivel) {
		this.estoqueDedutivel = estoqueDedutivel;
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

		// this.quantidade = quantidade;
		this.quantidade = isEstoqueDedutivel() ? quantidade : 0;

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

	public SubTipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(SubTipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public float getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(float valorCusto) {
		this.valorCusto = valorCusto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result + ((dataContagem == null) ? 0 : dataContagem.hashCode());
		result = prime * result + (estoqueDedutivel ? 1231 : 1237);
		result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
		result = prime * result + ((idItemEstoque == null) ? 0 : idItemEstoque.hashCode());
		result = prime * result + ((operador == null) ? 0 : operador.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
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
		if (estoqueDedutivel != other.estoqueDedutivel)
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
		if (tipoProduto == null) {
			if (other.tipoProduto != null)
				return false;
		} else if (!tipoProduto.equals(other.tipoProduto))
			return false;
		if (Float.floatToIntBits(valorUnitario) != Float.floatToIntBits(other.valorUnitario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return fabricante.getNome() + " - " + tipoProduto.getDescricaoTipo() + " - " + produto.getDescricaoProduto();
	}

}
