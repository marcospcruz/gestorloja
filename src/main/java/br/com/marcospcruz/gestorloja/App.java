package br.com.marcospcruz.gestorloja;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.view.LoginGui;

public class App {

	public static final String CONTROLE_ESTOQUE_HOME = "C:/gestorLoja";
	private static final String DB_HOME = CONTROLE_ESTOQUE_HOME + "\\db";

	public static void main(String args[]) {
		if (args.length > 0)
			switch (args[0]) {
			case "1":
				criaUsuario();
			}
		initApp();

	}

	/**
	 * s
	 */
	private static void initApp() {

		// File file = new File(CONTROLE_ESTOQUE_HOME);
		//
		// if (!file.exists()) {
		//
		// file.mkdir();
		//
		// File subdir = new File(DB_HOME);
		//
		// subdir.mkdir();
		//
		// }

		// new EstoquePrincipalGui("Controle de Estoque");
		new LoginGui("Gestão Loja - Login de Usuário").setVisible(true);
		// new UsuarioGui().setVisible(true);

	}

	private static void criaUsuario() {
		Crud<InterfaceGrafica> iDao = criaInterfaceGrafica();
		Crud<PerfilUsuario> dao = criaPerfisUsuario(iDao);
		criaUsuarios(dao);

	}

	/**
	 * 
	 * @return
	 */
	private static Crud<InterfaceGrafica> criaInterfaceGrafica() {
		String pack = "br.com.marcospcruz.gestorloja.view.";
		List<InterfaceGrafica> interfaces = Arrays
				.asList(new InterfaceGrafica[] { new InterfaceGrafica(pack + "EstoquePrincipalGui", "Estoque"),
						new InterfaceGrafica(pack + "ControleCaixaGui", "Controle de Caixa")
				// new InterfaceGrafica(pack + "VendaPrincipalGui","Venda")
		});
		Crud<InterfaceGrafica> iDao = new CrudDao<>();
		interfaces.stream().forEach(i -> {
			try {
				iDao.busca("interface.findinterface", "className", i.getClassName());
			} catch (NoResultException e) {
				e.printStackTrace();
				iDao.update(i);
			}
		});
		return iDao;
	}

	/**
	 * metodo provisorio
	 * 
	 * @param dao
	 */
	private static void criaUsuarios(Crud<PerfilUsuario> dao) {
		List perfilUsuario = Arrays
				.asList(new PerfilUsuario[] { dao.busca("perfilusuario.findperfil", "descricao", "Administrador") });

		List<Usuario> usuarios = Arrays
				.asList(new Usuario[] { new Usuario("Marcos Pereira da Cruz", "marcos", "123456", perfilUsuario) });

		usuarios.stream().forEach(usuario -> {
			Crud<Usuario> userdao = new CrudDao<>();
			try {
				userdao.busca("usuario.findNomeUsuario", "nomeUsuario", usuario.getNomeUsuario());
			} catch (NoResultException e) {
				e.printStackTrace();
				userdao.update(usuario);
			} finally {

			}
		});
	}

	/**
	 * metodo provisorio
	 * 
	 * @param iDao
	 * 
	 * @return
	 */
	private static Crud<PerfilUsuario> criaPerfisUsuario(Crud<InterfaceGrafica> iDao) {
		// @formatter:off
		PerfilUsuario[] perfis = { new PerfilUsuario("Administrador", iDao.busca("interface.findall")),
				new PerfilUsuario("Vendedor"), new PerfilUsuario("Caixa"), new PerfilUsuario("Estoque") };

		List<PerfilUsuario> perfisList = Arrays.asList(perfis);
		Crud<PerfilUsuario> dao = new CrudDao<>();
		perfisList.stream().forEach(perfil -> {

			try {
				dao.busca("perfilusuario.findperfil", "descricao", perfil.getDescricao());
			} catch (NoResultException e) {
				e.printStackTrace();
				dao.update(perfil);
			} finally {

			}
			//
		});
		return dao;
	}

}
