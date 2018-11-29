package br.com.marcospcruz.gestorloja.facade;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportadorPlanilha extends ImportadorArquivo {

	public ImportadorPlanilha(File planilha) {
		super.setFile(planilha);
	}

	public void abreArquivo() throws IOException {

		super.abreArquivo();

	}

	@Override
	protected void leDados() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();
				if (row.getRowNum() > 1) {
					Iterator<Cell> cellIterator = row.cellIterator();
					int i = 0;
					while (cellIterator.hasNext()) {

						// if (i == 1) {
						Cell cell = cellIterator.next();
						System.out.println(cell);
						// }
						i++;
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	void salvaDados() {
		// TODO Auto-generated method stub
		
	}

}
