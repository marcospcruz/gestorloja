package br.com.marcospcruz.gestorloja.view.fxui;

import java.util.List;

import br.com.marcospcruz.gestorloja.controller.UsuarioController;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.view.fxui.model.ItemEstoqueModel;
import br.com.marcospcruz.gestorloja.view.fxui.model.UsuarioModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControleUsuarioGui extends CadastroBase {

	private static final String[] COLUNAS = { "codigo", "nomeCompleto", "nomeUsuario", "status" };

	public ControleUsuarioGui() throws Exception {
		super(COLUNAS, getUsuarioController());
		setTitle("Cadastro de Usuários");
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			UsuarioController controller = getUsuarioController();
			controller.novoUsuario();
			abreJanelaModal(new CadastroUsuarioGui());
			reloadTableView("Usuários");
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void populaForm() {

	}

	@Override
	void populaTableView() {
		TitledPane tablePane = super.criaTablePane("Usuários");
		// tablePane.setMaxHeight(500d);
		// tablePane.setMinHeight(220d);
		getGrid().add(tablePane, 0, 2, 2, 1);

	}

	@Override
	void populaCadastroForm() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaPesquisaPanel() {

		TitledPane titledPane = new TitledPane("", new Button());
		titledPane.setMinWidth(getLayoutsMaxWidth());
		titledPane.setCollapsible(false);
		FlowPane content = new FlowPane(Orientation.HORIZONTAL);
		Button button = new Button("Novo");
		button.setOnAction(this::handle);
		content.getChildren().add(button);
		titledPane.setContent(content);
		getGrid().add(titledPane, 0, 0, 2, 1);
	}

	@Override
	void reloadForm() throws Exception {
//		TableView<ItemEstoqueModel> table = (TableView<ItemEstoqueModel>) ((Pane) teste).getChildren().get(0);
//		carregaDadosTable(table);
		reloadTableView("Usuários");
	}

	@Override
	protected void carregaDadosTable(TableView table) throws Exception {
		ObservableList<UsuarioModel> usuarios = FXCollections.observableArrayList();
		controller.buscaTodos();
		List<Usuario> usuariosList = controller.getList();
		for (Usuario usuario : usuariosList) {
			UsuarioModel model = new UsuarioModel();
			model.setCodigo(usuario.getIdUsuario());
			model.setNomeCompleto(usuario.getNomeCompleto());
			model.setNomeUsuario(usuario.getNomeUsuario());
			model.setStatus(usuario.isAtivo() ? "Ativo" : "Desativado");
			usuarios.add(model);
		}
		table.setItems(usuarios);
	}

	@Override
	protected void pesquisaItem(ActionEvent event) {
	

	}

	protected void handleTableClick(Event event) {
		super.handleTableClick(event);
		try {

			Stage cadastro;
			if (SingletonManager.getInstance().getUsuarioLogado().getIdUsuario() == 1)
				cadastro = new CadastroUsuarioGui();
			else
				cadastro = new CadastroUsuarioGui(true);
			abreJanelaModal(cadastro);
			reloadForm();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		// abreTelaCadastro(new CadastroUsuarioGui())
	}
}
