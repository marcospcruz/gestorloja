package br.com.marcospcruz.gestorloja;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.facade.LoginFacade;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.LogIn;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppFx extends Application {
	public static final String CONTROLE_ESTOQUE_HOME = "C:/gestorLoja";
	private static final String DESCRICAO = "descricao";

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = new LogIn();

		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		SingletonManager.getInstance().getLogger(this.getClass()).info("application stopped");
		System.exit(0);
	}

	public static void main(String args[]) {
		if (args.length > 0)
			switch (args[0]) {
			case "1":
				checkAndCreateAppHome();
				criaUsuario();
				createOperacao();
				createTipoMeioPagamento();
				// if (args.length > 1)
				// loadEstoqueSheet(args[1]);
			}
		launch(args);
	}
	//

	private static void createTipoMeioPagamento() {
		TipoMeioPagamento[] tipos = new TipoMeioPagamento[] { new TipoMeioPagamento("Dinheiro"),
				new TipoMeioPagamento("Cart�o de D�bito"), new TipoMeioPagamento("Cart�o de Cr�dito"),
				new TipoMeioPagamento("Outros") };
		int i = 1;
		Crud<TipoMeioPagamento> dao = new CrudDao<>();
		for (TipoMeioPagamento tipo : tipos) {

			TipoMeioPagamento t = dao.busca(TipoMeioPagamento.class, i++);
			if (t == null) {

				tipo = dao.update(tipo);
				System.out.println(tipo);
			}
		}

	}

	private static void createOperacao() {
		List<Operacao> operacoes = Arrays.asList(new Operacao("Entrada", new Usuario(1)),
				new Operacao("Sa�da", new Usuario(1)));
		operacoes.stream().forEach(operacao -> {
			Crud<Operacao> dao = new CrudDao<>();
			try {
				dao.busca("operacao.findOperacao", DESCRICAO, operacao.getDescricao());
			} catch (NoResultException e) {
				dao.update(operacao);
			}

		});

	}

	// private static void loadEstoqueSheet(String sheetString) {
	//
	// PlanilhaHandlerFacade facade = new PlanilhaHandlerFacade();
	//
	// try {
	// facade.importaPlanilha(sheetString);
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	//
	// }
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
				.asList(new PerfilUsuario[] { dao.busca("perfilusuario.findperfil", DESCRICAO, "Administrador") });

		List<Usuario> usuarios = Arrays
				.asList(new Usuario[] { new Usuario("Marcos Pereira da Cruz", "marcos", "123456", perfilUsuario)
//						new Usuario("Cibele Pereira Bellini", "cibele", "ci@2018", perfilUsuario),
//						new Usuario("Nadia", "nadia", "123456", perfilUsuario) 
						});

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
				dao.busca("perfilusuario.findperfil", DESCRICAO, perfil.getDescricao());
			} catch (NoResultException e) {
				e.printStackTrace();
				dao.update(perfil);
			} finally {

			}
			//
		});
		return dao;
	}

	
	//
}
