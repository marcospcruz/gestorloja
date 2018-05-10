package br.com.marcospcruz.gestorloja;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.facade.PlanilhaHandlerFacade;
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
				checkAndCreateAppHome();
				criaUsuario();
				if (args.length > 1)
					loadEstoqueSheet(args[1]);
			}
		initApp();

	}

	private static void loadEstoqueSheet(String sheetString) {
		
		PlanilhaHandlerFacade facade = new PlanilhaHandlerFacade();
		
		facade.importaPlanilha(sheetString);
		
	}

	/**
	 * s
	 */
	private static void initApp() {

		// new EstoquePrincipalGui("Controle de Estoque");
		new LoginGui("Gestão Loja - Login de Usuário").setVisible(true);
		// new UsuarioGui().setVisible(true);

	}

	private static void checkAndCreateAppHome() {
		File file = new File(CONTROLE_ESTOQUE_HOME);

		if (!file.exists()) {

			file.mkdir();

			// File subdir = new File(DB_HOME);
			//
			// subdir.mkdir();

		}
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

		List<InterfaceGrafica> interfaces = Arrays.asList(new InterfaceGrafica[] {
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_ESTOQUE, InterfaceGrafica.ESTOQUE),
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_CAIXA, InterfaceGrafica.CONTROLE_DE_CAIXA),
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_PDV, InterfaceGrafica.PONTO_DE_VENDA)
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
