package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

@Entity
//@formatter:off
@NamedQueries({ 
	@NamedQuery(name = "fabricante.buscaTodos", query = "select f from Fabricante f order by f.nome"),
	@NamedQuery(name = "fabricante.readParametroLike", query = "select f from Fabricante f where UPPER(f.nome) like :nome"), 
	@NamedQuery(name="fabricante.readNome",query="select f from Fabricante f where UPPER(f.nome) = :nome")
	})
//@formatter:on
public class Fabricante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1041277187240232341L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idFabricante;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataInsercao;

	@OneToMany(mappedBy = "fabricante")
	private List<ItemEstoque> itensEstoque;

	public Fabricante() {
		dataInsercao = SingletonManager.getInstance().getData();
	}

	public Fabricante(String string) {
		this();
		setNome(string);
	}

	public List<ItemEstoque> getItensEstoque() {
		return itensEstoque;
	}

	public void setItensEstoque(List<ItemEstoque> itensEstoque) {
		this.itensEstoque = itensEstoque;
	}

	public Integer getIdFabricante() {
		return idFabricante;
	}

	public void setIdFabricante(Integer idFabricante) {
		this.idFabricante = idFabricante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;

	}

	public Usuario getOperador() {
		return operador;
	}

	public Date getDataInsercao() {
		return dataInsercao;
	}

	public void setDataInsercao(Date dataInsercao) {
		this.dataInsercao = dataInsercao;
	}

	// public List<Produto> getProdutos() {
	// return produtos;
	// }
	//
	// public void setProdutos(List<Produto> produtos) {
	// this.produtos = produtos;
	// }

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataInsercao == null) ? 0 : dataInsercao.hashCode());
		result = prime * result + ((idFabricante == null) ? 0 : idFabricante.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Fabricante other = (Fabricante) obj;
		if (dataInsercao == null) {
			if (other.dataInsercao != null)
				return false;
		} else if (!dataInsercao.equals(other.dataInsercao))
			return false;
		if (idFabricante == null) {
			if (other.idFabricante != null)
				return false;
		} else if (!idFabricante.equals(other.idFabricante))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		return true;
	}

}
