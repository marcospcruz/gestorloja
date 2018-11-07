package br.com.marcospcruz.gestorloja.view.fxui;

import br.com.marcospcruz.gestorloja.controller.UsuarioController;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PasswordGui extends StageBase {
	public PasswordGui() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Label password = new Label("Nova Senha:");
		grid.add(password, 0, 1);

		PasswordField novaSenha = new PasswordField();
		grid.add(novaSenha, 1, 1);

		Label pw = new Label("Confirma nova Senha:");
		grid.add(pw, 0, 2);

		PasswordField confirmaNovaSenha = new PasswordField();
		grid.add(confirmaNovaSenha, 1, 2);

		Button btn = new Button("Alterar Senha");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		btn.setOnAction(event -> {
			String senha = novaSenha.getText();
			String confirmacao = confirmaNovaSenha.getText();
			try {
				UsuarioController controller = StageBase.getUsuarioController();
				if (!senha.equals(confirmacao)) {
					throw new Exception("Nova Senha e Confirma nova Senha não conferem");
				}
				SingletonManager.getInstance().getUsuarioLogado().setPassword(Util.encryptaPassword(senha));
				controller.setItem(SingletonManager.getInstance().getUsuarioLogado());
				controller.salva();
				showMensagemSucesso("Senha alterada com sucesso!");
				close();
			} catch (Exception e) {
				showErrorMessage(e);
				// e.printStackTrace();
			}

		});

		Scene scene = new Scene(grid, 350, 150);
		setScene(scene);
	}

	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	void reloadForm() throws Exception {
		// TODO Auto-generated method stub

	}

	protected void carregaDadosTable(TableView table) throws Exception {
		// TODO Auto-generated method stub

	}

	void populaForm() {
		// TODO Auto-generated method stub

	}

	protected void salvaDados(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	protected void excluiDados(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub

	}

	protected void pesquisaItem(ActionEvent event) {
		// TODO Auto-generated method stub

	}
}
