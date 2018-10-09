package br.com.marcospcruz.gestorloja.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.builder.ItemEstoqueBuilder;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.FabricanteController;
import br.com.marcospcruz.gestorloja.controller.ProdutoController;
import br.com.marcospcruz.gestorloja.controller.TipoProdutoController;
import br.com.marcospcruz.gestorloja.model.Fabricante;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.Produto;
import br.com.marcospcruz.gestorloja.model.SubTipoProduto;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.Util;

public class ImportadorArquivoCsv extends ImportadorArquivo {

	private static final int CODIGO_BARRAS = 1;
	private static final int CATEGORIA_PRODUTO = 3;
	private static final int SUPER_CATEGORIA_PRODUTO = 2;
	private static final int FABRICANTE = 4;
	private static final int PRODUTO = 5;
	private static final int VALOR_UNITARIO = 6;
	private static final int QUANTIDADE_ITEM = 7;
	private Map<String, ItemEstoque> dadosEstoqueMap;

	public ImportadorArquivoCsv(File arquivo) {
		super.setFile(arquivo);
	}

	@Override
	protected void leDados() throws Exception {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(inputStream));
		dadosEstoqueMap = new HashMap<>();
		SingletonManager.getInstance().getLogger(getClass())
				.info("Iniciando importação de arquivo de estoque em " + LocalDateTime.now());
		try {
			int rowIndex = 0;
			while (reader.ready()) {
				String row = reader.readLine();

				if (rowIndex > 1) {
					StringTokenizer token = new StringTokenizer(row, ";");
					String[] columns = new String[token.countTokens()];
					int x = 0;
					while (token.hasMoreTokens()) {
						columns[x++] = token.nextToken();
					}
					SingletonManager.getInstance().getLogger(getClass()).info("Lendo linha " + rowIndex);
					ItemEstoque item = parseRow(columns);
					if (!dadosEstoqueMap.containsKey(item.getCodigoDeBarras())) {
						dadosEstoqueMap.put(item.getCodigoDeBarras(), item);
					}

					// QT CADASTRADO;Codigo de barras;CATEGORIA;SUB-CATEGORIA;Marca /
					// Fabricante;Descrição;06/05/2018;07/05/2018;09/05/2018;11/05/2018;15/05/2018;17/05/2018;28/07/2018;VALOR;
					// TOTAL
					// ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

				}
				rowIndex++;
				// if(rowIndex==131)

			}
			setMensagemRetorno(rowIndex + " linhas lidas.");
		} catch (IOException e) {

			SingletonManager.getInstance().getLogger(getClass()).error(e);

		} finally {
			reader.close();

			inputStream.close();

			SingletonManager.getInstance().getLogger(getClass())
					.info("Leitura de arquivo de estoque concluído em " + LocalDateTime.now());
		}

	}

	private ItemEstoque parseRow(String[] columns) throws Exception {
		
		ItemEstoqueBuilder builder = new ItemEstoqueBuilder();
		if (columns[CATEGORIA_PRODUTO].contains("FORCE"))
			System.out.println();
		//@formatter:off
		ItemEstoque item=null; 
		try{
			item=builder.setCodigoDeBarras(columns[CODIGO_BARRAS])
				.setCategoriaProduto(columns[CATEGORIA_PRODUTO])
				.setSuperCategoriaProduto(columns[SUPER_CATEGORIA_PRODUTO])
				.setFabricante(columns[FABRICANTE])
				.setProduto(columns[PRODUTO])
				.setValorUnitario(columns[VALOR_UNITARIO]!=null?columns[VALOR_UNITARIO]:"0")
				.setQuantidade(columns[QUANTIDADE_ITEM])
				.getItemEstoque();
			
			//@formatter:on
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
			return parseRow(Util.aumentaArray(columns));
		} catch (NumberFormatException e) {
			return parseRow(Util.diminueArray(columns));
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new Exception("Dados Inválidos.");
		}
 		return item;
	}

	@Override
	void salvaDados() {
		int i = 0;
		try {
			EstoqueController controller = (EstoqueController) SingletonManager.getInstance()
					.getController(ControllerAbstractFactory.ESTOQUE);

			for (String codigoBarras : dadosEstoqueMap.keySet()) {
				try {
					controller.buscaItemPorCodigoDeBarras(codigoBarras);
				} catch (NoResultException e) {
					e.printStackTrace();
					SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage());
				}
				if (controller.getItem() == null) {
					ItemEstoque item = dadosEstoqueMap.get(codigoBarras);
					Fabricante fabricante = item.getFabricante();
					fabricante = buscaFabricante(fabricante);
					item.setFabricante(fabricante);
					SubTipoProduto tipoProduto = buscaTipoProduto(item.getTipoProduto());
					tipoProduto.setSuperTipoProduto(buscaTipoProduto(tipoProduto.getSuperTipoProduto()));
					item.setTipoProduto(tipoProduto);
					item.setProduto(buscaProduto(item.getProduto()));

					controller.setItem(item);
				}

				controller.salva();

				controller.setItem(null);
				SingletonManager.getInstance().getLogger(getClass()).info("Salvando registro: " + (i + 1));
				i++;
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			setMensagemRetorno(" " + i + " dados salvos.");
		}

	}

	private Produto buscaProduto(Produto produto) {
		try {

			ProdutoController controller = (ProdutoController) SingletonManager.getInstance()
					.getController(ControllerAbstractFactory.PRODUTO);
			controller.setItem(null);
			controller.busca(produto.getDescricaoProduto());
			if (controller.getItem() != null)
				return controller.getItem();
		} catch (Exception e) {
			System.out.println("\t" + e.getMessage());
			// e.printStackTrace();
		}
		return produto;
	}

	private SubTipoProduto buscaTipoProduto(SubTipoProduto tipoProduto) {
		try {
			TipoProdutoController controller = (TipoProdutoController) SingletonManager.getInstance()
					.getController(ControllerAbstractFactory.TIPO_PRODUTO_CONTROLLER);
			controller.setItem(null);
			controller.busca(tipoProduto.getDescricaoTipo());
			if (controller.getItem() != null)
				return (SubTipoProduto) controller.getItem();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return tipoProduto;
	}

	private Fabricante buscaFabricante(Fabricante fabricante) {
		try {
			FabricanteController controller = (FabricanteController) SingletonManager.getInstance()
					.getController(ControllerAbstractFactory.FABRICANTE);
			controller.setItem(null);
			controller.buscaNome(fabricante.getNome());
			if (controller.getItem() != null)
				return (Fabricante) controller.getItem();
		} catch (Exception e) {
			SingletonManager.getInstance().getLogger(getClass()).error(e.getMessage(), e);
		}
		return fabricante;
	}

}
