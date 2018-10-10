package br.com.marcospcruz.gestorloja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.builder.TransacaoFinanceiraBuilder;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.EstoqueController;
import br.com.marcospcruz.gestorloja.controller.UsuarioController;
import br.com.marcospcruz.gestorloja.controller.VendaController;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.ItemEstoque;
import br.com.marcospcruz.gestorloja.model.MeioPagamento;
import br.com.marcospcruz.gestorloja.model.TransacaoFinanceira;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.model.Venda;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class VendasTest {
	public static final String CONTROLE_ESTOQUE_HOME = "C:/gestorLoja";

	@Test
	public void rodaDia() throws IOException {
		abreCaixaDia(false);
		vende(false);
	}

	private void vende(boolean b) throws IOException {
		File vendasCsv = new File(CONTROLE_ESTOQUE_HOME + "/vendas.csv");
		BufferedReader reader = null;
		InputStream inputStream = null;
		Map<Integer, Venda> vendasMap = new HashMap<>();
		try {
			inputStream = new FileInputStream(vendasCsv);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			int cont = 0;
			while (reader.ready()) {
				String[] linha = reader.readLine().split(";");
				if (cont == 0) {
					cont++;
					continue;
				}

				VendaController controller = (VendaController) SingletonManager.getInstance()
						.getController(ControllerAbstractFactory.CONTROLE_VENDA);
				EstoqueController estoqueController = (EstoqueController) SingletonManager.getInstance()
						.getController(ControllerAbstractFactory.ESTOQUE);
				Venda venda;
				int id = Integer.parseInt(linha[0]);
				if (vendasMap.containsKey(id))
					venda = vendasMap.get(id);
				else
					venda = new Venda();
				controller.setItem(venda);
				// adiciona item
				controller.buscaProduto(linha[1]);
				ItemEstoque itemEstoque = estoqueController.getItemEstoque();
				controller.adicionaItemEstoque(itemEstoque, Integer.parseInt(linha[2]));

				// recebe Pagamento
				MeioPagamento mp = new MeioPagamento();
				mp.getTipoMeioPagamento().setIdTipoMeioPagamento(Integer.parseInt(linha[4]));
				mp.setValorPago(Integer.parseInt(linha[3]));
				
//				venda.getPagamento().getMeiosPagamento().add(mp);
				controller.recebePagamento(mp);
				venda.setPorcentagemDesconto(Integer.parseInt(linha[5]));
				vendasMap.put(id, venda);
				cont++;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			inputStream.close();
			reader.close();
		}

	}

	private void abreCaixaDia(boolean abreCaixa) throws IOException {
		File caixaCsv = new File(CONTROLE_ESTOQUE_HOME + "/caixa.csv");
		BufferedReader reader = null;
		InputStream inputStream = null;
		try {

			inputStream = new FileInputStream(caixaCsv);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			int cont = 0;
			while (reader.ready()) {
				String linha[] = reader.readLine().split(";");
				if (cont == 0) {
					cont++;
					continue;
				}

				Caixa caixa = new Caixa();
				caixa.setDataAbertura(new Date(Long.parseLong(linha[0])));
				caixa.setSaldoInicial(Float.parseFloat(linha[1]));
				UsuarioController userController = (UsuarioController) SingletonManager.getInstance()
						.getController(ControllerAbstractFactory.USUARIOS);
				CaixaController caixaController = (CaixaController) SingletonManager.getInstance()
						.getController(ControllerAbstractFactory.CONTROLE_CAIXA);
				userController.busca(Integer.parseInt(linha[2]));
				Usuario usuario = (Usuario) userController.getItem();
				caixa.setUsuarioAbertura(usuario);
				TransacaoFinanceira tf = new TransacaoFinanceiraBuilder().criaTransacao(true).setCaixa(caixa)
						.setMotivo("Abertura Caixa").setValorTransacaoFinanceira(linha[3]).getTransacaoFinanceira();

				// new TransacaoFinanceira();
				// tf.setMotivo("Abertura Caixa");
				// tf.setValorTransacaoFinanceira(Float.parseFloat(linha[3]));

				caixaController.setItem(caixa);
				caixaController.adicionaTransacao(tf);
				if (abreCaixa)
					caixaController.salva();
				SingletonManager.getInstance().setUsuarioLogado(usuario);
				cont++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			inputStream.close();
			reader.close();
		}

	}
}
