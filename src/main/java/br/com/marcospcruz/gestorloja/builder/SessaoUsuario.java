package br.com.marcospcruz.gestorloja.builder;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.marcospcruz.gestorloja.model.Usuario;

@Entity
//@formatter:off
@NamedQueries({ @NamedQuery(name = "sessaousuario.findSessaoAtiva", query = "select su from SessaoUsuario su "
		+ "join su.usuario u " 
		+ "where " 
		+ "u.idUsuario=:idUsuario " 
		+ "and su.dataFim = null"
		) 
})
//@formatter:on
public class SessaoUsuario implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idSessaoUsuario;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;

	public SessaoUsuario() {
		setDataInicio(new Date());
	}

	public int getIdSessaoUsuario() {
		return idSessaoUsuario;
	}

	public void setIdSessaoUsuario(int idSessaoUsuario) {
		this.idSessaoUsuario = idSessaoUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataFim == null) ? 0 : dataFim.hashCode());
		result = prime * result + ((dataInicio == null) ? 0 : dataInicio.hashCode());
		result = prime * result + idSessaoUsuario;
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		SessaoUsuario other = (SessaoUsuario) obj;
		if (dataFim == null) {
			if (other.dataFim != null)
				return false;
		} else if (!dataFim.equals(other.dataFim))
			return false;
		if (dataInicio == null) {
			if (other.dataInicio != null)
				return false;
		} else if (!dataInicio.equals(other.dataInicio))
			return false;
		if (idSessaoUsuario != other.idSessaoUsuario)
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SessaoUsuario [idSessaoUsuario=" + idSessaoUsuario + ", usuario=" + usuario + ", dataInicio="
				+ dataInicio + ", dataFim=" + dataFim + "]";
	}

}
