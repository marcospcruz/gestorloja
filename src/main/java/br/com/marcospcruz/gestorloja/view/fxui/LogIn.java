package br.com.marcospcruz.gestorloja.view.fxui;

import javax.persistence.PersistenceException;

import br.com.marcospcruz.gestorloja.facade.LoginFacade;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LogIn extends Stage {

	public LogIn() {
		super();
		setTitle("Gestão Loja - Login de Usuário");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Bem Vindo");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("Usuário:");
		grid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		Button btn = new Button("Entrar");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);

		final Text actiontarget = new Text();
		grid.add(actiontarget, 1, 6);

		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				processaDadosFornecidos(userTextField, pwBox, actiontarget);
			}

		});

		Scene scene = new Scene(grid, 300, 275);
		setScene(scene);
	}

	private void processaDadosFornecidos(TextField userTextField, PasswordField pwBox, final Text actiontarget) {
		LoginFacade controller = new LoginFacade(this);
		String nomeUsuario = userTextField.getText();
		String senha = pwBox.getText();
		try {
			controller.processaLogin(nomeUsuario, senha);
			Stage stage = new PrincipalFxGui();
			stage.initOwner(this);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

		} catch (PersistenceException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText("Erro de Conexão com Banco de Dados.");
			alert.showAndWait();
		} catch (Exception e1) {

			e1.printStackTrace();
			actiontarget.setText(e1.getMessage());
		}
	}
}
