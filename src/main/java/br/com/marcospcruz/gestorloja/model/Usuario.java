package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
//@formatter:off
@NamedQueries({
		@NamedQuery(name = "usuario.findLogin", query = "select u from Usuario u "
				+ "JOIN FETCH u.perfisUsuario "
//				+ "LEFT JOIN u.operador o "						
				+ "where u.nomeUsuario=:nomeUsuario "
				+ "and u.password=:password"),
		@NamedQuery(name = "usuario.findNomeUsuario", query = "select u from Usuario u "
				+ "LEFT JOIN u.operador o "
//				+ "JOIN FETCH u.perfisUsuario p "
//				+ "LEFT JOIN FETCH u.interfaces i "				
				+ "where u.nomeUsuario=:nomeUsuario") })
//@formatter:on
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4577612498982187892L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;
	@Column(unique = true)
	private String nomeUsuario;
	private String password;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date ultimoAcesso;
	private String nomeCompleto;
	private boolean primeiroAcesso;
	@OneToMany
	@JoinTable(name = "Perfil_usuario", joinColumns = { @JoinColumn(name = "idUsuario") }, inverseJoinColumns = {
			@JoinColumn(name = "idPerfilUsuario") })
	private List<PerfilUsuario> perfisUsuario;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;

	public Usuario() {

	}

	public Usuario(String string, String string2, String string3, List<PerfilUsuario> perfisUsuario) {
		this();
		setNomeCompleto(string);
		setNomeUsuario(string2);
		setPassword(string3);
		setPerfisUsuario(perfisUsuario);
		setPrimeiroAcesso(primeiroAcesso);
	}

	public Usuario(int idUsuario) {
		setIdUsuario(idUsuario);
	}

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	private void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;

	}

	private void setPerfisUsuario(List<PerfilUsuario> perfisUsuario) {
		this.perfisUsuario = perfisUsuario;

	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public List<PerfilUsuario> getPerfisUsuario() {
		return perfisUsuario;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;

	}

	public void setPassword(String password) {

		this.password = password;
	}

	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}

	public boolean isPrimeiroAcesso() {
		return primeiroAcesso;
	}

	public void setPrimeiroAcesso(boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
		result = prime * result + ((nomeCompleto == null) ? 0 : nomeCompleto.hashCode());
		result = prime * result + ((nomeUsuario == null) ? 0 : nomeUsuario.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((perfisUsuario == null) ? 0 : perfisUsuario.hashCode());
		result = prime * result + (primeiroAcesso ? 1231 : 1237);
		result = prime * result + ((ultimoAcesso == null) ? 0 : ultimoAcesso.hashCode());
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
		Usuario other = (Usuario) obj;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		if (nomeCompleto == null) {
			if (other.nomeCompleto != null)
				return false;
		} else if (!nomeCompleto.equals(other.nomeCompleto))
			return false;
		if (nomeUsuario == null) {
			if (other.nomeUsuario != null)
				return false;
		} else if (!nomeUsuario.equals(other.nomeUsuario))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (perfisUsuario == null) {
			if (other.perfisUsuario != null)
				return false;
		} else if (!perfisUsuario.equals(other.perfisUsuario))
			return false;
		if (primeiroAcesso != other.primeiroAcesso)
			return false;
		if (ultimoAcesso == null) {
			if (other.ultimoAcesso != null)
				return false;
		} else if (!ultimoAcesso.equals(other.ultimoAcesso))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", nomeUsuario=" + nomeUsuario + ", password=" + password
				+ ", ultimoAcesso=" + ultimoAcesso + ", nomeCompleto=" + nomeCompleto + ", primeiroAcesso="
				+ primeiroAcesso + ", perfisUsuario=" + perfisUsuario + "]";
	}

}
