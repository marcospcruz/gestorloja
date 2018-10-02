package br.com.marcospcruz.gestorloja.view.fxui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.dao.CrudDao;
import br.com.marcospcruz.gestorloja.facade.LoginFacade;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrincipalFxGui extends StageBase {

	private Map<String, InterfaceGrafica> interfaces;

	public PrincipalFxGui() {
		super();
		interfaces = new HashMap<>();
		Scene scene = new Scene(new Group(),800,600);
		FlowPane btnPane = new FlowPane(Orientation.HORIZONTAL);
		btnPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		btnPane.setVgap(8);
		btnPane.setHgap(4);
		btnPane.setPrefWrapLength(300);
		btnPane.setAlignment(Pos.CENTER);
		Usuario usuarioLogado = new CrudDao<Usuario>().update(SingletonManager.getInstance().getUsuarioLogado());

		List<PerfilUsuario> perfis = usuarioLogado.getPerfisUsuario();
		List<Button> btns = new ArrayList<>();

		perfis.stream().forEach(perfil -> {
			perfil = new CrudDao<PerfilUsuario>().update(perfil);
			perfil.getInterfaces().stream().forEach(gui -> {
				Button btn = criaActionButton(gui);
				interfaces.put(gui.getNomeModulo(), gui);
				btns.add(btn);
			});
		});
		btns.stream().forEach(btn -> {
			btnPane.getChildren().add(btn);
		});
		// btnPane.getChildren().add(maintenancePane());
		boolean isAdministrador = usuarioLogado.getPerfisUsuario().stream()
				.anyMatch(perfil -> perfil.getDescricao().equalsIgnoreCase("Administrador"));
		if (isAdministrador) {
			TitledPane manutencaoPane = (TitledPane) maintenancePane();
			btnPane.getChildren().add(manutencaoPane);
		}
		scene.setRoot(btnPane);
		setScene(scene);
		LoginFacade facade = new LoginFacade(this);
		setOnCloseRequest(event -> {
			facade.fechaSessaoUsuario();
		});

	}

	private Node maintenancePane() {
		TitledPane mainPane = new TitledPane("Manutenção Aplicação", new Button());
		mainPane.setCollapsible(false);
		FlowPane pane = new FlowPane(Orientation.HORIZONTAL);
		DatePicker dataManutencao = new DatePicker();
		pane.getChildren().add(dataManutencao);
		mainPane.setContent(pane);
		dataManutencao.setOnAction(evt -> {
			DatePicker picker = (DatePicker) evt.getSource();
			LocalDate dataSistema = picker.getValue();
			SingletonManager.getInstance().setDataManutencao(dataSistema);

		});
		// CheckBox salvaCheck = new CheckBox();
		// salvaCheck.setText("Salva Configuração");
		// salvaCheck.setOnAction(evt -> {

		// try {
		// String dataManutencaoKey = "dataManutencao";
		// if (salvaCheck.isSelected())
		// Util.saveConfigProperty(dataManutencaoKey,
		// SingletonManager.getInstance().getDataManutencao().toString());
		// else
		// Util.removeConfigProperty(dataManutencaoKey);
		// } catch (IOException e) {

		// e.printStackTrace();
		// }

		// });
		// pane.getChildren().add(salvaCheck);
		return mainPane;
	}

	private Button criaActionButton(InterfaceGrafica gui) {
		Button btn = new Button(gui.getNomeModulo());
		btn.setOnAction(this);
		return btn;
	}

	@Override
	public void handle(ActionEvent event) {
		String pack = "br.com.marcospcruz.gestorloja.view.fxui.";
		Button btn = (Button) event.getSource();
		InterfaceGrafica gui = interfaces.get(btn.getText());
		String className = gui.getClassName();
		try {
			Stage stage = StageFactory.createStage(className);
			stage.initOwner(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}

	}

	@Override
	void reloadForm() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	void populaForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvaDados(ActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void excluiDados(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
