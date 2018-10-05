package br.com.marcospcruz.gestorloja.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.Usuario;

public class UsuarioController extends ControllerBase {

	private List<Usuario> usuarios;
	private Crud<Usuario> dao;
	private Usuario usuario;
	private CrudDao<PerfilUsuario> perfilDao;

	public UsuarioController() {
		dao = new CrudDao<>();
	}

	@Override
	public void busca(Object id) throws Exception {
		usuario = dao.busca("usuario.findUsuario", "id", Integer.valueOf(id.toString()));

	}

	@Override
	public List buscaTodos() {
		setList(dao.busca("usuario.findAll"));
		return null;
	}

	@Override
	public List getList() {
		// TODO Auto-generated method stub
		return usuarios;
	}

	@Override
	public void busca(String text) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getItem() {
		// TODO Auto-generated method stub
		return usuario;
	}

	@Override
	public void setList(List list) {
		this.usuarios = list;

	}

	@Override
	public void setItem(Object object) {
		this.usuario = (Usuario) object;

	}

	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(String text, boolean b, Object object) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva(Object object, boolean validaDados) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salva() throws Exception {
		validaExistente(usuario.getNomeUsuario());
		boolean primeiroAcesso = usuario.getIdUsuario() == null;
		if (primeiroAcesso) {
			usuario.setPassword("loja");
			usuario.setPrimeiroAcesso(primeiroAcesso);
			usuario.setDataCriacao(new Date());
		}

		usuario = dao.update(usuario);
	}

	@Override
	public void registraHistoricoOperacao(Operacao operacao) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validaExistente(String text) throws Exception {
		try {
			Usuario teste = dao.busca("usuario.findUserName", "nomeUsuario", text);

			boolean existe = usuario.getIdUsuario() == 0 && teste == null;
			if (existe) {
				throw new Exception("Usuario " + text + " já existe no Sistema.");
			}
		} catch (NoResultException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void novoUsuario() {
		usuario = new Usuario();

	}

	public List<PerfilUsuario> getPerfisUsuario() {
		perfilDao = new CrudDao<>();

		return perfilDao.busca("perfilusuario.findperfisUsuario");
	}

	public void setPerfisUsuario(List<String> perfis) throws Exception {
		if(perfis.isEmpty())
			throw new Exception("Selecionar ao menos um Perfil para o Usuário.");
		usuario.setPerfisUsuario(new ArrayList<>());
		boolean adm = perfis.contains("Administrador");
		for (String descricao : perfis) {
			PerfilUsuario perfil = perfilDao.busca("perfilusuario.findperfil", "descricao", descricao);
			if (adm && perfil.getDescricao().equals("Administrador"))
				usuario.getPerfisUsuario().add(perfil);
		}

	}

}
