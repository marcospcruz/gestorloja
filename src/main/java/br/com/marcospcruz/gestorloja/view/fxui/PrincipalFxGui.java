package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
		Scene scene = new Scene(new Group());
		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
		flowPane.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		flowPane.setVgap(8);
		flowPane.setHgap(4);
		flowPane.setPrefWrapLength(300);
		flowPane.setAlignment(Pos.CENTER);
		Usuario usuarioLogado = SingletonManager.getInstance().getUsuarioLogado();
		List<PerfilUsuario> perfis = usuarioLogado.getPerfisUsuario();
		List<Button> btns = new ArrayList<>();

		perfis.stream().forEach(perfil -> {
			perfil.getInterfaces().stream().forEach(gui -> {
				Button btn = criaActionButton(gui);
				interfaces.put(gui.getNomeModulo(), gui);
				btns.add(btn);
			});
		});
		btns.stream().forEach(btn -> {
			flowPane.getChildren().add(btn);
		});
		scene.setRoot(flowPane);
		setScene(scene);
	}

	private Button criaActionButton(InterfaceGrafica gui) {
		Button btn = new Button(gui.getNomeModulo());
		btn.setOnAction(this);
		return btn;
	}

	@Override
	public void handle(ActionEvent event) {
		String pack="br.com.marcospcruz.gestorloja.view.fxui.";
		Button btn = (Button) event.getSource();
		InterfaceGrafica gui = interfaces.get(btn.getText());
		
		try {
			Stage stage = StageFactory.createStage(gui.getClassName());
			stage.initOwner(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
