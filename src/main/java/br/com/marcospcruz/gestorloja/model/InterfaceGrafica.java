package br.com.marcospcruz.gestorloja.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = "interface.findinterface", query = "select i from InterfaceGrafica i where i.className=:className"),
		@NamedQuery(name = "interface.findall", query = "select i from InterfaceGrafica i") })
public class InterfaceGrafica implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8000151228417933839L;
	public static final String ESTOQUE = "Estoque";
	public static final String CONTROLE_DE_CAIXA = "Controle de Caixa";
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

	public Usuario getOperador() {
		return operador;
	}

	public void setOperador(Usuario operador) {
		this.operador = operador;
	}

	public InterfaceGrafica(String string, String string2) {
		this(string);
		this.nomeModulo = string2;
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

	@Override
	public String toString() {
		return "InterfaceGrafica [idInterfaceGrafica=" + idInterfaceGrafica + ", className=" + className + ", operador="
				+ operador + "]";
	}

	public String getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(String nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + idInterfaceGrafica;
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
		if (operador == null) {
			if (other.operador != null)
				return false;
		} else if (!operador.equals(other.operador))
			return false;
		return true;
	}

}
