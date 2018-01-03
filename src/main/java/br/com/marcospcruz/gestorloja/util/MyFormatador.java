package br.com.marcospcruz.gestorloja.util;

import java.text.DecimalFormat;

/**
 * Utilitario de formata��o.
 * 
 * @author Marcos
 * 
 */
public class MyFormatador {
	/**
	 * formatador casas decimais.
	 * 
	 * @param valor
	 * @return
	 */
	public static String formataStringDecimais(float valor) {

		DecimalFormat formatadorNumero = new DecimalFormat("#.00");

		return formatadorNumero.format((double) valor);

	}

}
