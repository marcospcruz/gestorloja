package br.com.marcospcruz.gestorloja.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

	private static final Locale CURRENT_LOCALE = new Locale("pt","BR");

	public static String formataDataAtual() {
		
		return formataData(new Date());
	}

	public static String formataMoeda(Float valorMoeda) {
		
		return NumberFormat.getCurrencyInstance(CURRENT_LOCALE).format(valorMoeda);
	}

	public static String formataData(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(data);
		
	}

}
