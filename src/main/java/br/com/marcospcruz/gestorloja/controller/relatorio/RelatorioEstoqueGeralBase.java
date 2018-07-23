package br.com.marcospcruz.gestorloja.controller.relatorio;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.marcospcruz.gestorloja.App;
import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.model.EstoqueReportBeanModel;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public abstract class RelatorioEstoqueGeralBase {
	private static final String PASTA_PROJETO = App.CONTROLE_ESTOQUE_HOME + "/";
	protected FileOutputStream outputStream;
	private File arquivoRelatorio;

	public abstract void geraRelatorio() throws Exception;

	protected abstract void escreveArquivoRelatorio(Object... params) throws Exception;

	protected Collection<EstoqueReportBeanModel> listaEstoque() throws Exception {

		EstoqueController controller = (EstoqueController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.ESTOQUE);
		List<EstoqueReportBeanModel> estoqueReportBeans = new ArrayList<>();
		controller.getItensEstoque().stream().forEach(report -> {
			estoqueReportBeans.add(new EstoqueReportBeanModel(report));
		});
		return estoqueReportBeans;

	}

	protected void abreArquivoRelatorio() throws IOException {
		Desktop.getDesktop().open(arquivoRelatorio);

	}

	protected void setOutputStream(String string) throws FileNotFoundException {

		arquivoRelatorio = new File(PASTA_PROJETO + string);

		outputStream = new FileOutputStream(arquivoRelatorio);

	}

	public void deletaArquivoExistente() {
		if (arquivoRelatorio.exists()) {

			arquivoRelatorio.delete();

		}
		
	}

}
