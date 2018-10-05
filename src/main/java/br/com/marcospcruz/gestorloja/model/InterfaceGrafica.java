package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = "interface.findinterface", query = "select i from InterfaceGrafica i "
				+ "JOIN FETCH i.perfisUsuario "
				+ "where i.className=:className"),
		@NamedQuery(name = "interface.findall", query = "select i from InterfaceGrafica i") })
public class InterfaceGrafica implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8914269988172254274L;

	// private static final String PACKAGE = "br.com.marcospcruz.gestorloja.view.";
	private static final String PACKAGE = "br.com.marcospcruz.gestorloja.view.fxui.";
	public static final String ESTOQUE = "Estoque";
	public static final String CONTROLE_DE_CAIXA = "Controle de Caixa";
	public static final String PONTO_DE_VENDA = "Ponto de Venda";
	public static final String CLASS_NAME_ESTOQUE = PACKAGE + "EstoquePrincipalGui";
	// public static final String CLASS_NAME_PDV = PACKAGE + "ControleVendaGui";
	public static final String CLASS_NAME_PDV = PACKAGE + "PDV";
	public static final String CLASS_NAME_PASSWORD = PACKAGE + "PasswordGui";
	public static final String CLASS_NAME_CAIXA = PACKAGE + "ControleCaixaGui";

	public static final String PASSWORD = "Alterar Senha";
	public static final String USUARIOS = "Controle Usuários";

	public static final String CLASS_NAME_USUARIOS = PACKAGE + "ControleUsuarioGui";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idInterfaceGrafica;
	@Column(unique = true)
	private String className;
	@ManyToOne
	@JoinColumn(name = "idOperador")
	private Usuario operador;
	@Column(unique = true)
	private String nomeModulo;

	@ManyToMany(mappedBy = "interfaces")
	// @JoinTable(name = "Perfis_interfaces", joinColumns = { @JoinColumn(name =
	// "idInterface") }, inverseJoinColumns = {
	// @JoinColumn(name = "idPerfilUsuario") })

	private List<PerfilUsuario> perfisUsuario;

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public InterfaceGrafica(String className, String nomeModulo) {
		this(className);
		this.nomeModulo = nomeModulo;
	}

	public InterfaceGrafica() {

	}

	public InterfaceGrafica(String className) {
		this.className = className;
	}

	public int getIdInterfaceGrafica() {
		return idInterfaceGrafica;
	}

	public void setIdInterfaceGrafica(int idInterfaceGrafica) {
		this.idInterfaceGrafica = idInterfaceGrafica;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(String nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public List<PerfilUsuario> getPerfisUsuario() {
		if (perfisUsuario == null)
			perfisUsuario = new ArrayList<PerfilUsuario>();
		return perfisUsuario;
	}

	public void setPerfisUsuario(List<PerfilUsuario> perfisUsuario) {
		this.perfisUsuario = perfisUsuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + idInterfaceGrafica;
		result = prime * result + ((nomeModulo == null) ? 0 : nomeModulo.hashCode());
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
		InterfaceGrafica other = (InterfaceGrafica) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (idInterfaceGrafica != other.idInterfaceGrafica)
			return false;
		if (nomeModulo == null) {
			if (other.nomeModulo != null)
				return false;
		} else if (!nomeModulo.equals(other.nomeModulo))
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
		return "InterfaceGrafica [idInterfaceGrafica=" + idInterfaceGrafica + " nomeModulo=" + nomeModulo + "]";
	}

}
