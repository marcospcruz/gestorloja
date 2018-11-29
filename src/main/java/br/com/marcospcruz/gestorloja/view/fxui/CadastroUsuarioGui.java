package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.ArrayList;
import java.util.List;

import br.com.marcospcruz.gestorloja.controller.UsuarioController;
import br.com.marcospcruz.gestorloja.model.PerfilUsuario;
import br.com.marcospcruz.gestorloja.model.Usuario;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class CadastroUsuarioGui extends StageBase {

	private static final String CSS_COLOR_BLACK = "-fx-text-inner-color: black;";
	private GridPane grid;
	private List<CheckBox> perfilBoxes;
	private TextField nomeUsuarioField;
	private TextField nomeCompletoField;
	private TitledPane perfisUsuarioPane;
	private boolean desabilitaFields;
	private CheckBox ativaUsuario;

	public CadastroUsuarioGui() {
		this(false);
	}

	public CadastroUsuarioGui(boolean desabilitaFields) {
		super();
		grid = new GridPane();
		setTitle("Cadastro de Usuário");
		super.setDimension(412, 270);
		// double thisWidth = (double) super.getWidth() - ((double) super.getWidth() *
		// 10 / 100);
		setLayoutsMaxWidth(scene.widthProperty().get());
		resizableProperty().setValue(Boolean.FALSE);
		Group root = new Group();
		scene = new Scene(root, width, height);
		this.desabilitaFields = desabilitaFields;
		populaGridContent();
		populaForm();
		// titledPane.setPrefHeight(height);
		grid.setVgap(10);
		root.getChildren().add(grid);
		// getvBox().setPadding(new Insets(10, 0, 0, 100));

		setScene(scene);

	}

	private void populaGridContent() {
		GridPane dadosUsuarioGrid = new GridPane();

		TitledPane titledPane = new TitledPane("Usuário", new Button());
		titledPane.setCollapsible(false);
		FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
		flow.getChildren().add(dadosUsuarioGrid);
		titledPane.setContent(flow);
		dadosUsuarioGrid.setAlignment(Pos.TOP_CENTER);
		// grid.setHgap(30);
		dadosUsuarioGrid.setVgap(10);
		// dadosUsuarioGrid.setMinHeight(getHeight());
		// dadosUsuarioGrid.setMinWidth(getWidth());
		// dadosUsuarioGrid.setMaxHeight(getHeight());
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column1.setPercentWidth(40);
		column2.setPercentWidth(60);
		// column2.setHgrow(Priority.ALWAYS);
		// column2.setFillWidth(true);

		dadosUsuarioGrid.getColumnConstraints().add(column1);
		dadosUsuarioGrid.getColumnConstraints().add(column2);
		int rowIndex = 0;
		dadosUsuarioGrid.add(criaLabelNormal("Nome Completo: "), 0, rowIndex);
		nomeCompletoField = new TextField();
		nomeCompletoField.setDisable(desabilitaFields);
		nomeCompletoField.setMinWidth(width * 0.25);
		dadosUsuarioGrid.add(nomeCompletoField, 1, rowIndex++);
		dadosUsuarioGrid.add(criaLabelNormal("Nome Usuário: "), 0, rowIndex);
		nomeUsuarioField = new TextField();
		nomeUsuarioField.setDisable(desabilitaFields);
		// nomeUsuarioField.setMaxWidth(width * 0.25);
		dadosUsuarioGrid.add(nomeUsuarioField, 1, rowIndex++);
		dadosUsuarioGrid.add(criaLabelNormal("Ativar Usuário: "), 0, rowIndex);
		ativaUsuario = new CheckBox("");
		dadosUsuarioGrid.add(ativaUsuario, 1, rowIndex++);
		// TextField nomeUsuarioField = new TextField();
		// nomeUsuarioField.setMinWidth(width * 0.25);
		// grid.add(nomeUsuarioField, 1, rowIndex++);

		perfisUsuarioPane = criaPerfisPane();
		Button button = new Button("Salvar");
		FlowPane pane = criaFlowPane(button);
		TitledPane titled = new TitledPane("", pane);
		button.setOnAction(this::handle);
		titled.setCollapsible(false);

		grid.add(titledPane, 0, 0);
		grid.add(perfisUsuarioPane, 0, 1);
		grid.add(titled, 0, 2);
		;
	}

	protected TitledPane criaPerfisPane() {
		TitledPane perfisUsuarioPane = criaTitledPane("Perfil Usuário: ");
		try {
			UsuarioController controller = getUsuarioController();
			List<PerfilUsuario> perfisUsuario = controller.getPerfisUsuario();
			perfilBoxes = new ArrayList<>();
			FlowPane pane = criaFlowPane();
			Usuario usuario = (Usuario) controller.getItem();
			for (PerfilUsuario perfil : perfisUsuario) {

				CheckBox check = new CheckBox(perfil.getDescricao());
				if (usuario.getIdUsuario() != null) {
					boolean select = usuario.getPerfisUsuario().stream()
							.anyMatch(p -> p.getIdPerfilUsuario() == perfil.getIdPerfilUsuario());
					check.setSelected(select);
				}
				perfilBoxes.add(check);
				pane.getChildren().add(check);
			}

			perfisUsuarioPane.setContent(pane);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return perfisUsuarioPane;
	}

	protected FlowPane criaFlowPane(Node... node) {
		FlowPane pane = new FlowPane();
		pane.setHgap(10);
		pane.setAlignment(Pos.BASELINE_CENTER);
		pane.getChildren().addAll(node);
		return pane;
	}

	@Override
	public void handle(ActionEvent arg0) {
		if (showConfirmAtionMessage("Deseja salvar o Usuário?")) {
			try {
				String nomeCompleto = nomeCompletoField.getText();
				String nomeUsuario = nomeUsuarioField.getText();
				UsuarioController controller = getUsuarioController();

				List<String> perfis = new ArrayList<>();
				perfilBoxes.stream().filter(c -> c.isSelected()).forEach(c -> {
					perfis.add(c.getText());
				});
				controller.setPerfisUsuario(perfis);
				Usuario user = (Usuario) controller.getItem();
				user.setNomeUsuario(nomeUsuario);
				user.setNomeCompleto(nomeCompleto);
				user.setAtivo(ativaUsuario.isSelected());
				controller.salva();
				showMensagemSucesso("Usuário " + nomeUsuario + " salvo com sucesso.");
				close();
			} catch (Exception e) {
				showErrorMessage(e);
				e.printStackTrace();
			}

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
		try {
			controller = getUsuarioController();
			Usuario usuario = (Usuario) controller.getItem();
			if (usuario.getIdUsuario() == null)
				return;
			nomeUsuarioField.setText(usuario.getNomeUsuario());
			nomeCompletoField.setText(usuario.getNomeCompleto());
			ativaUsuario.setSelected(usuario.isAtivo());
			if (desabilitaFields) {
				nomeCompletoField.setStyle(CSS_COLOR_BLACK);
				nomeUsuarioField.setStyle(CSS_COLOR_BLACK);
			}
			// perfisUsuarioPane = criaPerfisPane();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void salvaDados(ActionEvent event) throws Exception {
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
