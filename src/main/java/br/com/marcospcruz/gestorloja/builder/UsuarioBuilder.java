package br.com.marcospcruz.gestorloja.builder;

import br.com.marcospcruz.gestorloja.model.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	public UsuarioBuilder() {
		super();
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public UsuarioBuilder buildUsuario(String nomeUsuario) {
		usuario.setNomeUsuario(nomeUsuario);
		return this;
	}

	public void buildSenha(String senha) {
		usuario.setPassword(senha);
		
	}

}
