package br.com.marcospcruz.gestorloja.controller.relatorio;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.marcospcruz.gestorloja.AppFx;
import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class RelatorioEstoqueGeral {

	private static final String PASTA_PROJETO = AppFx.CONTROLE_ESTOQUE_HOME + "/";

	private static final String ARQUIVO_JRXML = "META-INF/relatorios_jaspers/relatorio_estoque_geral.jrxml";

	private static final String RELATORIO_GERADO = "relatorio_estoque.pdf";

	private static final Object BRAZIL = new Locale("pt", "BR");

	public void gerarRelatorio() throws Exception {

		Collection<ItemEstoque> itensEstoque = listaEstoque();

		if (itensEstoque == null || itensEstoque.isEmpty())

			throw new Exception(ConstantesEnum.NO_DATA_FOUND.getValue().toString());

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(itensEstoque);

		Map parametros = new HashMap();

		parametros.put(JRParameter.REPORT_LOCALE, BRAZIL);

		gerarRelatorio(jrDataSource, parametros);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void gerarRelatorio(JRDataSource jrDataSource, Map parametros) throws Exception {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARQUIVO_JRXML);

		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

		if (inputStream == null)
			throw new Exception("Necessário compilar o Relatório.");

		File arquivoRelatorio = new File(PASTA_PROJETO + RELATORIO_GERADO);

		try {

			if (arquivoRelatorio.exists()) {

				arquivoRelatorio.delete();

			}

			JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, jrDataSource);

			JRExporter exporter = new JRPdfExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(arquivoRelatorio));

			exporter.exportReport();

			// outPut = new FileOutputStream(arquivoRelatorio);

			// JasperExportManager.exportReportToPdfStream(print, outPut);

			// outPut.close();

			Desktop.getDesktop().open(arquivoRelatorio);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

			throw new Exception(e.getMessage());

		} catch (JRException e) {

			e.printStackTrace();

			throw new Exception(ConstantesEnum.ERROR_MESSAGE_LOGO_MARCA.getValue().toString());

		}

	}

	public static Collection<ItemEstoque> listaEstoque() throws Exception {

		EstoqueController controller = (EstoqueController) SingletonManager.getInstance()
				.getController(ControllerAbstractFactory.ESTOQUE);

		return controller.getItensEstoque();

	}

	public static void main(String args[]) {

		try {

			new RelatorioEstoqueGeral().gerarRelatorio();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
