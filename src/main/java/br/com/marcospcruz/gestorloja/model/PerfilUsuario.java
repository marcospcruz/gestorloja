package br.com.marcospcruz.gestorloja.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "perfilusuario.findperfil", query = "select p from PerfilUsuario p where p.descricao=:descricao")
public class PerfilUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPerfilusuario;
	private String descricao;
	@OneToMany
	@JoinTable(name = "Perfis_interfaces", joinColumns = {
			@JoinColumn(name = "idPerfilUsuario") }, inverseJoinColumns = { @JoinColumn(name = "idInterface") })
	private List<InterfaceGrafica> interfaces;

	public PerfilUsuario(String descricao) {
		this.descricao = descricao;
	}

	public PerfilUsuario() {
	}

	public PerfilUsuario(String string, List<InterfaceGrafica> interfaces) {
		this(string);
		this.interfaces = interfaces;
	}

	public int getIdPerfilusuario() {
		return idPerfilusuario;
	}

	public void setIdPerfilusuario(int idPerfilusuario) {
		this.idPerfilusuario = idPerfilusuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<InterfaceGrafica> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfaceGrafica> interfaces) {
		this.interfaces = interfaces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + idPerfilusuario;
		result = prime * result + ((interfaces == null) ? 0 : interfaces.hashCode());
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
		PerfilUsuario other = (PerfilUsuario) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (idPerfilusuario != other.idPerfilusuario)
			return false;
		if (interfaces == null) {
			if (other.interfaces != null)
				return false;
		} else if (!interfaces.equals(other.interfaces))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PerfilUsuario [idPerfilusuario=" + idPerfilusuario + ", descricao=" + descricao + ", interfaces="
				+ interfaces + "]";
	}

}
