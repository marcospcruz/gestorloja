package br.com.marcospcruz.gestorloja.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.AbstractController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.model.TipoProduto;

public class PlanilhaHandlerFacade {

	private static final String PLANILHA_ESTOQUE = "Estoque";
	private static final int GRUPO = 1;
	private static final int INICIO_DADOS = 2;
	private static final int MARCA_PRODUTO = 2;
	private static final int PRODUTO = 3;
	private static final int QUANTIDADE = 7;
	private static final String GENERICO = "Genérico";
	private Sheet planilha;
	private AbstractController tipoProdutoController;
	private AbstractController fabricanteController;
	private AbstractController produtoController;

	public PlanilhaHandlerFacade() {

	}

	public void importaPlanilha(String workBookString) throws Exception {

		abrePlanilha(workBookString);
		cadastraProdutos();

	}

	private void cadastraProdutos() {

		Iterator<Row> linhaIterator = planilha.iterator();

		while (linhaIterator.hasNext()) {

			Row linhaAtual = linhaIterator.next();

			if (linhaAtual.getRowNum() < INICIO_DADOS)
				continue;

			try {
				cadastraTipoProduto(linhaAtual.getCell(GRUPO));
				cadastraMarcaProduto(linhaAtual.getCell(MARCA_PRODUTO));
				cadastraProduto(linhaAtual.getCell(PRODUTO).toString());
				cadastraItemEstoque(linhaAtual.getCell(QUANTIDADE).getNumericCellValue());
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	}

	private void cadastraItemEstoque(double qt) throws Exception {
		AbstractController estoqueController = getController(ControllerAbstractFactory.ESTOQUE);

		ItemEstoque itemEstoque;
		try {
			estoqueController.busca(((Produto) produtoController.getItem()).getIdProduto());
			itemEstoque = (ItemEstoque) estoqueController.getItem();
		} catch (Exception e) {
			itemEstoque = new ItemEstoque();
			itemEstoque.setDataEntradaEstoque(new Date());
			itemEstoque.setProduto((Produto) produtoController.getItem());

		}
		itemEstoque.setQuantidade(new Double(qt).intValue());
		try {
			estoqueController.salva(itemEstoque);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void cadastraProduto(String descricaoProduto) throws Exception {
		produtoController = getController(ControllerAbstractFactory.PRODUTO);
		Produto produto = new Produto();
		SubTipoProduto tipoProduto=tipoProdutoController.getItem();
		produto.setDescricaoProduto(descricaoProduto);
		produto.setTipoProduto(tipoProduto);
		try {
			
			produtoController.busca(produto);
		} catch (Exception e) {
			
			produto.setDescricaoProduto(descricaoProduto);
			produto.setFabricante((Fabricante) fabricanteController.getItem());
			produto.setTipoProduto((SubTipoProduto) tipoProdutoController.getItem());

			produtoController.salva(produto, false);

		}

	}

	private void cadastraMarcaProduto(Cell marcaProduto) throws Exception {
		System.out.println(marcaProduto);
		fabricanteController = getController(ControllerAbstractFactory.FABRICANTE);
		try {
			fabricanteController.busca(marcaProduto.toString());
		} catch (Exception e) {

			Fabricante fabricante = new Fabricante();
			fabricante.setNome(marcaProduto.toString());

			fabricanteController.salva(fabricante);
		}

	}

	private void cadastraTipoProduto(Cell cellGrupo) throws Exception {
		tipoProdutoController = getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
		String descricao = GENERICO;
		if (cellGrupo != null)
			descricao = cellGrupo.toString();
		try {
			tipoProdutoController.busca(descricao);
		} catch (Exception e) {

			tipoProdutoController.salva(descricao);

		}
	}

	private AbstractController getController(String controllerName) throws Exception {

		return ControllerAbstractFactory.createController(controllerName);
	}

	private void abrePlanilha(String sheetString) throws IOException {
		FileInputStream excelFile = null;
		Workbook workbook = null;
		try {
			excelFile = new FileInputStream(new File(sheetString));
			workbook = new XSSFWorkbook(excelFile);
			planilha = workbook.getSheet(PLANILHA_ESTOQUE);
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (workbook != null)
				workbook.close();
			if (excelFile != null)
				excelFile.close();
		}

	}

}
