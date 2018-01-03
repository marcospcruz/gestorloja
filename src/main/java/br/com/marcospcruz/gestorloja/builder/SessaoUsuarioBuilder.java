package br.com.marcospcruz.gestorloja.builder;

import java.util.Date;

import br.com.marcospcruz.gestorloja.model.Usuario;

public class SessaoUsuarioBuilder {

	private SessaoUsuario sessaoUsuario;

	public SessaoUsuarioBuilder createSessaoUsuario() {
		sessaoUsuario = new SessaoUsuario();
		return this;

	}

	public SessaoUsuarioBuilder buildUsuario(Usuario usuario) {
		sessaoUsuario.setUsuario(usuario);
		return this;

	}

	public SessaoUsuario getSessaoUsuario() {

		return sessaoUsuario;
	}

	public void setSessaoUsuario(SessaoUsuario sessaoUsuario) {
		this.sessaoUsuario = sessaoUsuario;

	}

	public void setDataFim(Date dataFim) {
		sessaoUsuario.setDataFim(dataFim);
		
	}

}
