package br.com.marcospcruz.gestorloja.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private static final int QUANTIDADE = 10;
	private static final String GENERICO = "Genérico";
	private Sheet planilha;

	public PlanilhaHandlerFacade() {

	}

	public void importaPlanilha(String workBookString) throws Exception {

		abrePlanilha(workBookString);
		cadastraProdutos();

	}

	private void cadastraProdutos() {
		List<ItemEstoque> produtosList = new ArrayList<>();
		Iterator<Row> linhaIterator = planilha.iterator();
		int contador = 0;
		while (linhaIterator.hasNext()) {

			Row linhaAtual = linhaIterator.next();

			if (linhaAtual.getRowNum() < INICIO_DADOS)
				continue;
//			System.out.println(linhaAtual.getRowNum());
			try {
				Fabricante fabricante = new Fabricante();
				String nomeFabricante = GENERICO;
				if (linhaAtual.getCell(MARCA_PRODUTO) != null)
					nomeFabricante = linhaAtual.getCell(MARCA_PRODUTO).toString();
				fabricante.setNome(nomeFabricante);
				SubTipoProduto tipoProduto = new SubTipoProduto();
				String descricaoTipo = GENERICO;
				if (linhaAtual.getCell(GRUPO) != null)
					descricaoTipo = linhaAtual.getCell(GRUPO).toString();
				tipoProduto.setDescricaoTipo(descricaoTipo);
				Produto produto = new Produto();
				produto.setDescricaoProduto(linhaAtual.getCell(PRODUTO).toString());
				produto.setTipoProduto(tipoProduto);
				ItemEstoque itemEstoque = new ItemEstoque();
				itemEstoque.setProduto(produto);
				itemEstoque.setFabricante(fabricante);
				Double quantidade = linhaAtual.getCell(QUANTIDADE).getNumericCellValue();
				itemEstoque.setQuantidade(quantidade.intValue());

				produtosList.add(itemEstoque);
				// Fabricante fabricante =
				// cadastraMarcaProduto(linhaAtual.getCell(MARCA_PRODUTO));
				// TipoProduto tipoProduto = cadastraTipoProduto(linhaAtual.getCell(GRUPO));
				// produto=cadastraProduto(linhaAtual.getCell(PRODUTO).toString());
				// cadastraItemEstoque(linhaAtual.getCell(QUANTIDADE).getNumericCellValue());
				contador++;
			} catch (Exception e) {
				e.printStackTrace();
				break;

			} finally {

			}

		}
		System.out.println(contador + " linhas carregadas na memória.");
		processaProdutos(produtosList);
	}

	private void processaProdutos(List<ItemEstoque> produtosList) {

		produtosList.stream().forEach(itemEstoque -> {

			try {
				Produto produto = itemEstoque.getProduto();
				SubTipoProduto tipoProduto = (SubTipoProduto) cadastraTipoProduto(
						itemEstoque.getProduto().getTipoProduto());
				Fabricante fabricante = cadastraMarcaProduto(itemEstoque.getFabricante());
				produto.setTipoProduto(tipoProduto);
				produto = cadastraProduto(produto);
				itemEstoque.setFabricante(fabricante);
				itemEstoque.setProduto(produto);
				cadastraItemEstoque(itemEstoque);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	private void cadastraItemEstoque(ItemEstoque itemEstoque) throws Exception {

		AbstractController estoqueController = getController(ControllerAbstractFactory.ESTOQUE);

		try {
			estoqueController.busca(itemEstoque);
			itemEstoque = (ItemEstoque) estoqueController.getItem();
		} catch (Exception e) {
			e.printStackTrace();

		}

		estoqueController.salva(itemEstoque);

	}

	private Produto cadastraProduto(Produto produto) throws Exception {

		AbstractController produtoController = getController(ControllerAbstractFactory.PRODUTO);

		try {
			produtoController.busca(produto.getDescricaoProduto());
		} catch (Exception e) {

			produtoController.salva(produto, false);

		}
		return (Produto) produtoController.getItem();

	}

	private Fabricante cadastraMarcaProduto(Fabricante fabricante) throws Exception {

		AbstractController fabricanteController = getController(ControllerAbstractFactory.FABRICANTE);
		String nome = fabricante.getNome();

		try {
			fabricanteController.busca(nome);
		} catch (Exception e) {

			fabricanteController.salva(fabricante);
		}
		return (Fabricante) fabricanteController.getItem();
	}

	private TipoProduto cadastraTipoProduto(SubTipoProduto tipoProduto) throws Exception {

		AbstractController tipoProdutoController = getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
		String descricao = tipoProduto.toString();

		try {
			tipoProdutoController.busca(descricao);
		} catch (Exception e) {

			tipoProdutoController.salva(descricao);

		}

		return (TipoProduto) tipoProdutoController.getItem();
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
