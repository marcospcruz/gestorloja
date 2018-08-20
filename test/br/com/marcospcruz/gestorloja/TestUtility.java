package br.com.marcospcruz.gestorloja;

import java.io.File;

import org.junit.jupiter.api.Test;

import br.com.marcospcruz.gestorloja.facade.ImportadorArquivo;
import br.com.marcospcruz.gestorloja.facade.ImportadorArquivoCsv;

public class TestUtility {
	@Test
	public void planilhaEstoqueReaderTest() throws Exception {
		File arquivo = new File("C:\\gestorLoja\\cadastro_produtos.csv");
		ImportadorArquivo loader = new ImportadorArquivoCsv(arquivo);

		loader.carregaDados();

	}
}
