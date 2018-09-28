package br.com.marcospcruz.gestorloja.facade;

import javax.persistence.NoResultException;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

import br.com.marcospcruz.gestorloja.builder.SessaoUsuario;
import br.com.marcospcruz.gestorloja.builder.SessaoUsuarioBuilder;
import br.com.marcospcruz.gestorloja.builder.UsuarioBuilder;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.LogIn;

public class LoginFacade {

	private Object loginGui;

	private SessaoUsuarioBuilder sessaoUsuarioBuilder;

	private CrudDao<SessaoUsuario> sessaoUsuarioDao;

	public LoginFacade(Object gui) {
		new UsuarioBuilder();
		this.loginGui = gui;
		sessaoUsuarioDao = new CrudDao<>();
	}

	public void processaLogin(String nomeUsuario, String senha) throws Exception {
		validaDadosInputados(nomeUsuario, senha);
		Usuario usuario = buscaUsuario(nomeUsuario);
		comparaSenhaUsuario(usuario, senha);
		buscaSessaoUsuarioAtiva(usuario);
		criaSessaoUsuario(usuario);
		SingletonManager.getInstance().setUsuarioLogado(getUsuarioLogado());
		// abreInterfacePrincipal();
		closeLoginGui();
	}

	private void buscaSessaoUsuarioAtiva(Usuario usuario) throws Exception {
		if (usuario.getIdUsuario() == 1)
			return;
		try {
			SessaoUsuario sessaoUsuario = buscaUsuarioLogado(usuario);
			throw new Exception(
					"Usu�rio " + sessaoUsuario.getUsuario().getNomeUsuario() + " ainda possui sess�o ativa.");
		} catch (NoResultException e) {

			e.printStackTrace();
		}

	}

	SessaoUsuario sessaoUsuario;

	private SessaoUsuario buscaUsuarioLogado(Usuario usuario) {
		try {
			sessaoUsuario = sessaoUsuarioDao.busca("sessaousuario.findSessaoAtiva", "idUsuario",
					usuario.getIdUsuario());
		} catch (NoResultException e) {
			throw new NoResultException("N�o h� sess�o ativa para o usu�rio " + usuario.getNomeUsuario());
		}
		return sessaoUsuario;
	}

	private void criaSessaoUsuario(Usuario usuario) {
		sessaoUsuarioBuilder = new SessaoUsuarioBuilder();
		sessaoUsuarioBuilder.createSessaoUsuario().buildUsuario(usuario);

		SessaoUsuario sessaoUsuario = sessaoUsuarioDao.update(sessaoUsuarioBuilder.getSessaoUsuario());
		sessaoUsuarioBuilder.setSessaoUsuario(sessaoUsuario);
		usuario.setUltimoAcesso(sessaoUsuario.getDataInicio());
		usuario.setPrimeiroAcesso(false);
		new CrudDao<Usuario>().update(usuario);
	}

	private void abreInterfacePrincipal() {

		// new PrincipalGui(this);

	}

	private void closeLoginGui() {
		if (loginGui instanceof JDialog)
			((JDialog) loginGui).dispose();
		else
			((LogIn) loginGui).close();

	}

	private void validaDadosInputados(String nomeUsuario, String senha) throws Exception {
		logInfo("Autenticando usuario "+nomeUsuario);
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
		if (sessaoUsuarioBuilder == null) {
			sessaoUsuarioBuilder = new SessaoUsuarioBuilder();
		}
		sessaoUsuarioBuilder.createSessaoUsuario().setDataFim(SingletonManager.getInstance().getData());
		SessaoUsuario sessao = sessaoUsuarioBuilder.getSessaoUsuario();
		// temp
		sessaoUsuarioDao.update(sessao);
		StringBuilder logMessage = new StringBuilder("Finalizando sess�o do usu�rio ");
		logMessage.append(sessao.getUsuario().getNomeUsuario());
		logMessage.append(" em " + sessao.getDataFim());
		logInfo(logMessage.toString());
		return this;
	}

	protected void logInfo(String string) {
		Logger logger=SingletonManager.getInstance().getLogger(this.getClass());
		logger.info(string);
	}

	public Usuario getUsuarioLogado() {
		Usuario usuario = sessaoUsuarioBuilder.getSessaoUsuario().getUsuario();

		return new CrudDao<Usuario>().busca(Usuario.class, usuario.getIdUsuario());
	}

	public void setSessaoUsuarioBuilder(SessaoUsuarioBuilder sessaoUsuarioBuilder) {
		this.sessaoUsuarioBuilder = sessaoUsuarioBuilder;
	}

}
