package br.com.marcospcruz.gestorloja.controller;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.builder.UsuarioBuilder;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class LoginFacade {

	private UsuarioBuilder builder;

	private Usuario usuario;

	public LoginFacade() {
		builder = new UsuarioBuilder();

	}

	public void processaLogin(String nomeUsuario, String senha) throws Exception {
		validaDadosInputados(nomeUsuario, senha);
		buscaUsuario(nomeUsuario);
		comparaSenhaUsuario(senha);
	}

	private void validaDadosInputados(String nomeUsuario, String senha) throws Exception {
		if (nomeUsuario == null || nomeUsuario.length() == 0)
			throw new Exception("Informar usuário!");
		if (senha == null || senha.length() == 0)
			throw new Exception("Informar senha!");

	}

	private void comparaSenhaUsuario(String senha) throws Exception {
		if (!usuario.getPassword().equals(senha)) {
			throw new Exception("Senha não confere!");
		}

	}

	private void buscaUsuario(String nomeUsuario) throws Exception {
		Crud<Usuario> dao = new CrudDao<>();
		try {
			usuario = dao.busca("usuario.findNomeUsuario", "nomeUsuario", nomeUsuario);

		} catch (NoResultException e) {
			throw new Exception("Usuário " + nomeUsuario + " não encontrado!");
		}

	}

}
