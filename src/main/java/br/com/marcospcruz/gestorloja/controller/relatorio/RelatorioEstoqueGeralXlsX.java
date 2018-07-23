package br.com.marcospcruz.gestorloja.controller.relatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressBase.CellPosition;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.marcospcruz.gestorloja.model.EstoqueReportBeanModel;
import br.com.marcospcruz.gestorloja.util.Util;

public class RelatorioEstoqueGeralXlsX extends RelatorioEstoqueGeralBase {

	private static final String SHEET_NAME = "Estoque de Produtos.";
	private static final String RELATORIO_GERADO = "relatorio_estoque.xlsx";
	//@formatter:off
	private static final String[] COLUNAS_HEADER = new String[] { 
			"Categoria Produto", 
			"Sub-Categoria Produto",
			"Marca / Fabricante", 
			"Código de Barras",
			"Descrição de Produto", 
			"Quantidade",
			"Valor Unitário", 
			"Valor total" 
			};

	//@formatter:on

	public void geraRelatorio() throws IOException {

		super.setOutputStream(RELATORIO_GERADO);

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

		int rowNum = 0;

		try {

			List<Object> dadosRelatorio = new ArrayList();
			dadosRelatorio.add(COLUNAS_HEADER);
			dadosRelatorio.addAll((List<EstoqueReportBeanModel>) listaEstoque());

			while (rowNum < dadosRelatorio.size()) {

				// XSSFCellStyle xssfCellStyle = createStype();
				Object dadoRelatorio = dadosRelatorio.get(rowNum);

				// EstoqueReportBeanModel dadoRelatorio = ;

				Object[] colunas = null;
				if (dadoRelatorio instanceof EstoqueReportBeanModel)
					colunas = parseColumns((EstoqueReportBeanModel) dadoRelatorio);
				else
					colunas = (Object[]) dadoRelatorio;
				Row row = sheet.createRow(rowNum);

				XSSFCellStyle xssfCellStyle = null;
				if (rowNum == 0) {
					xssfCellStyle = createStype(workbook, true, true);

				} else {
					xssfCellStyle = createStype(workbook, false, false);
				}
				populateRow(colunas, sheet, row, xssfCellStyle);

				rowNum++;

			}
			// cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			// cell.setCellFormula(strFormula);
			String formula = "SUM(H2:H" + sheet.getPhysicalNumberOfRows() + ")";
			Row subTotalRow = sheet.createRow(++rowNum);

			XSSFCellStyle xssfCellStyle = null;
			Cell cell = createCell(subTotalRow, xssfCellStyle, 7);

			// cell.setCellStyle(xssfCellStyle);
			setCellFormula(cell, formula);
			escreveArquivoRelatorio(workbook);

			super.abreArquivoRelatorio();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			workbook.close();
		}

	}

	private XSSFCellStyle createStype(XSSFWorkbook workbook, boolean isBold, boolean isCentered) {

		XSSFFont xssfFont = workbook.createFont();
		XSSFCellStyle xssfCellStyle = workbook.createCellStyle();
		//
		xssfFont.setFamily(FontFamily.SCRIPT);
		xssfFont.setBold(isBold);
		if (isCentered)
			xssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
		//
		xssfCellStyle.setFont(xssfFont);
		xssfCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0,00"));
		return xssfCellStyle;
	}

	/**
	 * 
	 * @param colunas
	 * @param sheet
	 * @param row
	 * @param isHeader
	 */
	private void populateRow(Object[] colunas, XSSFSheet sheet, Row row, XSSFCellStyle xssfCellStyle) {

		int column = 0;

		while (column < colunas.length) {

			Cell cell = createCell(row, xssfCellStyle, column);
			sheet.autoSizeColumn(cell.getColumnIndex());
			String valor = colunas[column++].toString();
			// if (xssfCellStyle != null)
			// xssfCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
			cell.setCellStyle(xssfCellStyle);
			if (column == 7)

				try {
					valor = Util.formataStringDecimais(Float.parseFloat(valor));

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			else if (column == 8) {
				String formula = "F" + (cell.getRowIndex() + 1) + "*H" + cell.getColumnIndex();
				setCellFormula(cell, formula);
			}
			if (column > 5) {

			}
			cell.setCellType(CellType.NUMERIC);
			cell.setCellValue(valor);

		}
	}

	private Cell createCell(Row row, XSSFCellStyle xssfCellStyle, int column) {
		Cell cell = row.createCell(column);
		if (xssfCellStyle != null)
			cell.setCellStyle(xssfCellStyle);
		return cell;
	}

	private void setCellFormula(Cell cell, String formula) {
		cell.setCellType(CellType.FORMULA);
		// style=workbook.createCellStyle();
		cell.setCellFormula(formula);
	}

	private Object[] parseColumns(EstoqueReportBeanModel dadoRelatorio) {
		//@formatter:off
		return new Object[] { 
				dadoRelatorio.getTipoProduto(), 
				dadoRelatorio.getTipoProduto(),
				dadoRelatorio.getFabricante(), 
				dadoRelatorio.getCodigoDeBarras(),
				dadoRelatorio.getProduto().getDescricaoProduto(), 
				dadoRelatorio.getQuantidade(),
				dadoRelatorio.getValorUnitario(), 
				dadoRelatorio.getValorTotal() };
		//@formatter:on

	}

	@Override
	protected void escreveArquivoRelatorio(Object... params) throws Exception {
		XSSFWorkbook workbook = (XSSFWorkbook) params[0];
		workbook.write(outputStream);

	}

}
