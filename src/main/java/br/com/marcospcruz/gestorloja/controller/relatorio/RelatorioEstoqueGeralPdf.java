package br.com.marcospcruz.gestorloja.controller.relatorio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.marcospcruz.gestorloja.model.EstoqueReportBeanModel;
import br.com.marcospcruz.gestorloja.util.ConstantesEnum;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class RelatorioEstoqueGeralPdf extends RelatorioEstoqueGeralBase {

	private static final String ARQUIVO_JASPER = "META-INF/relatorios_jaspers/relatorio_estoque_geral.jasper";

	private static final String RELATORIO_GERADO = "relatorio_estoque.pdf";

	private static final Object BRAZIL = new Locale("pt", "BR");

	@Override
	public void geraRelatorio() throws Exception {

		Collection<EstoqueReportBeanModel> itensEstoque = super.listaEstoque();

		if (itensEstoque.isEmpty())

			throw new Exception(ConstantesEnum.NO_DATA_FOUND.getValue().toString());

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(itensEstoque);

		Map parametros = new HashMap();

		parametros.put(JRParameter.REPORT_LOCALE, BRAZIL);

		escreveArquivoRelatorio(jrDataSource, parametros);

		// outPut = new FileOutputStream(arquivoRelatorio);

		// JasperExportManager.exportReportToPdfStream(print, outPut);

		// outPut.close();

		abreArquivoRelatorio();

	}

	@Override
	protected void escreveArquivoRelatorio(Object... params) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARQUIVO_JASPER);
		try {
			super.setOutputStream(RELATORIO_GERADO);
			if (inputStream == null)
				throw new Exception("Necessário compilar o Relatório.");

			JRDataSource jrDataSource = (JRDataSource) params[0];
			Map parametros = (Map) params[1];
			JasperPrint print = JasperFillManager.fillReport(inputStream, parametros, jrDataSource);

			JRExporter exporter = new JRPdfExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, super.outputStream);

			exporter.exportReport();
		} catch (FileNotFoundException e) {

			e.printStackTrace();

			throw new Exception(e.getMessage());

		} catch (JRException e) {

			e.printStackTrace();

			throw new Exception(ConstantesEnum.ERROR_MESSAGE_LOGO_MARCA.getValue().toString());

		} finally {
			outputStream.close();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		try {

			new RelatorioEstoqueGeralPdf().geraRelatorio();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
public void abreArquivoRelatorio() throws IOException {
	super.deletaArquivoExistente();
	super.abreArquivoRelatorio();
}
}
