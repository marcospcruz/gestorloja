package br.com.marcospcruz.gestorloja.builder;

import java.util.Date;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class SessaoUsuarioBuilder {

	private SessaoUsuario sessaoUsuario;

	public SessaoUsuarioBuilder createSessaoUsuario() {
		buscaSessaoAberta();
		if (sessaoUsuario == null)
			sessaoUsuario = new SessaoUsuario();
		return this;

	}

	protected void buscaSessaoAberta() {
		Usuario usuario = SingletonManager.getInstance().getUsuarioLogado();
		if (usuario != null) {
			CrudDao<SessaoUsuario> sessaoUsuarioDao = new CrudDao<>();
			try {
				sessaoUsuario = sessaoUsuarioDao.busca("sessaousuario.findSessaoAtiva", "idUsuario",
						usuario.getIdUsuario());
			} catch (NoResultException e) {
				e.printStackTrace();
			}
		}
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
