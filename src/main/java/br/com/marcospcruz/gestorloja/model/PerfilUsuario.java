package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
@NamedQueries({
		@NamedQuery(name = "perfilusuario.findperfil", query = "select p from PerfilUsuario p where p.descricao=:descricao"),
		@NamedQuery(name = "perfilusuario.findperfilUsuario", query = "select p " + "from PerfilUsuario p "
				+ "join p.usuarios u " + "where u.idUsuario=:idUsuario") })
public class PerfilUsuario implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPerfilusuario;
	private String descricao;
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "Perfis_interfaces", joinColumns = {
			@JoinColumn(name = "idPerfilUsuario") }, inverseJoinColumns = { @JoinColumn(name = "idInterface") })
	@OrderBy(value = "nomeModulo")
	private List<InterfaceGrafica> interfaces;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PerfilUsuario_usuario", joinColumns = {
			@JoinColumn(name = "idPerfilUsuario") }, inverseJoinColumns = { @JoinColumn(name = "idUsuario") })
	private List<Usuario> usuarios;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOperador")
	private Usuario operador;

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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + idPerfilusuario;
		result = prime * result + ((interfaces == null) ? 0 : interfaces.hashCode());
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
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PerfilUsuario [idPerfilusuario=" + idPerfilusuario + ", descricao=" + descricao + ", operador="
				+ operador + "]";
	}

}
