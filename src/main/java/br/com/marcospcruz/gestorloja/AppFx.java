package br.com.marcospcruz.gestorloja;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;

import br.com.marcospcruz.gestorloja.dao.Crud;
import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.facade.LoginFacade;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.Operacao;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.TipoMeioPagamento;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
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
	public void stop() {
		Logger logger = SingletonManager.getInstance().getLogger(this.getClass());
		try {
			super.stop();
			logger.info("Aplicação fechada com sucesso.");

			
		} catch (Exception e) {
			logger.error("Falha ao finalizar aplicação!");
			logger.error(e);
		} finally {
			System.exit(0);
		}

	}

	public static void main(String args[]) throws NoSuchAlgorithmException {
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
				new TipoMeioPagamento("Cartão de Débito"), new TipoMeioPagamento("Cartão de Crédito"),
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
				new Operacao("Saída", new Usuario(1)));
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

	private static void criaUsuario() throws NoSuchAlgorithmException {
		Crud<InterfaceGrafica> iDao = criaInterfaceGrafica();
		Crud<PerfilUsuario> dao = criaPerfisUsuario(iDao);
		criaUsuarios(dao);

	}

	/**
	 * 
	 * @return
	 */
	private static Crud<InterfaceGrafica> criaInterfaceGrafica() {
		InterfaceGrafica[] guis = new InterfaceGrafica[] {
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_ESTOQUE, InterfaceGrafica.ESTOQUE),
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_CAIXA, InterfaceGrafica.CONTROLE_DE_CAIXA),
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_PDV, InterfaceGrafica.PONTO_DE_VENDA),
				new InterfaceGrafica(InterfaceGrafica.CLASS_NAME_USUARIOS, InterfaceGrafica.USUARIOS)
				// new InterfaceGrafica(pack + "VendaPrincipalGui","Venda")
		};
		PerfilUsuario[] perfis = { new PerfilUsuario("Manutencao", Arrays.asList(guis)),
				new PerfilUsuario("Administrador", Arrays.asList(guis)),
				new PerfilUsuario("Vendedor", Arrays.asList(guis[1], guis[2])),
				new PerfilUsuario("Caixa", Arrays.asList(guis[1])),
				new PerfilUsuario("Estoque", Arrays.asList(guis[0])) };
		Crud<PerfilUsuario> pDao = new CrudDao<>();
		// List<InterfaceGrafica> interfaces = Arrays.asList(guis);
		Crud<InterfaceGrafica> iDao = new CrudDao<>();
		for (int x = 0; x < perfis.length; x++) {
			PerfilUsuario i = perfis[x];
			try {

				if (x >= 1) {
					ArrayList<InterfaceGrafica> newGuis = new ArrayList<>(i.getInterfaces());
					i.setInterfaces(new ArrayList<>());
					for (int y = 0; y < newGuis.size(); y++) {
						InterfaceGrafica ii = newGuis.get(y);
						ii = iDao.busca("interface.findinterface", "className", ii.getClassName());
						ii.getPerfisUsuario().add(i);
						i.getInterfaces().add(ii);
					}
				}
				pDao.busca("perfilusuario.findperfil", "descricao", i.getDescricao());
			} catch (NoResultException e) {
				e.printStackTrace();
				pDao.update(i);
			}
		}
		return iDao;
	}

	/**
	 * metodo provisorio
	 * 
	 * @param dao
	 * @throws NoSuchAlgorithmException
	 */
	private static void criaUsuarios(Crud<PerfilUsuario> dao) throws NoSuchAlgorithmException {
		List perfilUsuario = Arrays
				.asList(new PerfilUsuario[] { dao.busca("perfilusuario.findperfil", DESCRICAO, "Manutencao") });

		List<Usuario> usuarios = Arrays.asList(new Usuario[] {
				new Usuario("Marcos Pereira da Cruz", "marcos", Util.encryptaPassword("123456"), perfilUsuario)
				// new Usuario("Cibele Pereira Bellini", "cibele", "ci@2018", perfilUsuario),
				// new Usuario("Nadia", "nadia", "123456", perfilUsuario)
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
		 List<InterfaceGrafica> guis = iDao.busca("interface.findall");
		PerfilUsuario[] perfis = { new PerfilUsuario("Manutencao",guis),
				new PerfilUsuario("Administrador"),new PerfilUsuario("Vendedor"), new PerfilUsuario("Caixa"), new PerfilUsuario("Estoque") };

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
