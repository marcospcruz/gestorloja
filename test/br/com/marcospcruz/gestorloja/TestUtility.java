package br.com.marcospcruz.gestorloja;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import br.com.marcospcruz.gestorloja.facade.ImportadorArquivo;
import br.com.marcospcruz.gestorloja.facade.ImportadorArquivoCsv;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.fxui.StageFactory;
import javafx.stage.Stage;

public class TestUtility {
	@Test
	public void planilhaEstoqueReaderTest() throws Exception {
		File arquivo = new File("C:\\gestorLoja\\cadastro_produtos.csv");
		ImportadorArquivo loader = new ImportadorArquivoCsv(arquivo);

		loader.carregaDados();

	}

	@Test
	public void testUtilRegex() {
		assertTrue(Util.matchValorFloat("190.00"));
		assertFalse(Util.matchValorFloat("190,00"));
		assertTrue(Util.matchValorFloat("190.5"));
		assertFalse(Util.matchValorFloat("190,55"));
		assertFalse(Util.matchValorFloat("190,5"));
		assertFalse(Util.matchValorFloat("190,52"));
		assertTrue(Util.matchValorFloat("190.52"));
	}
}