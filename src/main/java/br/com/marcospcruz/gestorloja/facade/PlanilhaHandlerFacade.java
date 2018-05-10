package br.com.marcospcruz.gestorloja.facade;

import java.io.File;
import java.io.FileInputStream;

public class PlanilhaHandlerFacade {

	public PlanilhaHandlerFacade() {
		
	}

	public void importaPlanilha(String sheetString) {
		
		abrePlanilha(sheetString);
		
	}

	private void abrePlanilha(String sheetString) {
		FileInputStream excelFile=new FileInputStream(new File(sheetString));
		
		planilhaExcel=new XSSFWorkbook(excelFile);
		
	}

}
