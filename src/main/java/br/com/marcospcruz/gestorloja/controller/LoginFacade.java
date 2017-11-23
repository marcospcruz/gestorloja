package br.com.marcospcruz.gestorloja.controller;

import java.util.Date;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.builder.SessaoUsuario;
import br.com.marcospcruz.gestorloja.builder.SessaoUsuarioBuilder;
import br.com.marcospcruz.gestorloja.builder.UsuarioBuilder;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.view.LoginGui;
import br.com.marcospcruz.gestorloja.view.PrincipalGui;

public class LoginFacade {

	private UsuarioBuilder builder;

	private LoginGui loginGui;

	private SessaoUsuarioBuilder sessaoUsuarioBuilder;

	private CrudDao<SessaoUsuario> sessaoUsuarioDao;

	public LoginFacade(LoginGui loginGui) {
		builder = new UsuarioBuilder();
		this.loginGui = loginGui;
		sessaoUsuarioDao = new CrudDao<>();
	}

	public void processaLogin(String nomeUsuario, String senha) throws Exception {
		validaDadosInputados(nomeUsuario, senha);
		Usuario usuario = buscaUsuario(nomeUsuario);
		comparaSenhaUsuario(usuario, senha);
		buscaSessaoUsuarioAtiva(usuario);
		criaSessaoUsuario(usuario);
		abreInterfacePrincial();
		closeLoginGui();
	}

	private void buscaSessaoUsuarioAtiva(Usuario usuario) throws Exception {
		try {
			sessaoUsuarioDao.busca("sessaousuario.findSessaoAtiva", "idUsuario", usuario.getIdUsuario());
			throw new Exception("Usu�rio possui sess�o ainda ativa.");
		} catch (NoResultException e) {
			e.printStackTrace();
		}

	}

	private void criaSessaoUsuario(Usuario usuario) {
		sessaoUsuarioBuilder = new SessaoUsuarioBuilder();
		sessaoUsuarioBuilder.createSessaoUsuario().buildUsuario(usuario);

		SessaoUsuario sessaoUsuario = sessaoUsuarioDao.update(sessaoUsuarioBuilder.getSessaoUsuario());
		sessaoUsuarioBuilder.setSessaoUsuario(sessaoUsuario);
		usuario.setUltimoAcesso(sessaoUsuario.getDataInicio());
		new CrudDao<Usuario>().update(usuario);
	}

	private void abreInterfacePrincial() {

		new PrincipalGui(this);

	}

	private void closeLoginGui() {
		loginGui.dispose();

	}

	private void validaDadosInputados(String nomeUsuario, String senha) throws Exception {
		if (nomeUsuario == null || nomeUsuario.length() == 0)
			throw new Exception("Informar usu�rio!");
		if (senha == null || senha.length() == 0)
			throw new Exception("Informar senha!");

	}

	private void comparaSenhaUsuario(Usuario usuario, String senha) throws Exception {
		if (!usuario.getPassword().equals(senha)) {
			throw new Exception("Senha n�o confere!");
		}

	}

	private Usuario buscaUsuario(String nomeUsuario) throws Exception {
		Crud<Usuario> dao = new CrudDao<>();
		Usuario usuario;
		try {
			usuario = dao.busca("usuario.findNomeUsuario", "nomeUsuario", nomeUsuario);

		} catch (NoResultException e) {
			throw new Exception("Usu�rio " + nomeUsuario + " n�o encontrado!");
		}
		return usuario;
	}

	public LoginFacade fechaSessaoUsuario() {
		sessaoUsuarioBuilder.setDataFim(new Date());
		sessaoUsuarioDao.update(sessaoUsuarioBuilder.getSessaoUsuario());
		return this;
	}

}
