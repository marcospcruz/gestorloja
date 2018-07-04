package br.com.marcospcruz.gestorloja.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.text.JTextComponent;

public class Util {

	private static final Locale CURRENT_LOCALE = new Locale("pt", "BR");
	private static final Character VIRGULA = ',';
	private static final Character PONTO = '.';

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

	public static Float moedaToFloat(String text) {

		String valor = text.replace(',', '.');

		valor = valor.substring(3);

		return Float.valueOf(valor);
	}

	public static String formataStringDecimais(float valor) {

		DecimalFormat formatadorNumero = new DecimalFormat("#.00");

		String retorno = formatadorNumero.format((double) valor);
		if (retorno.contains("-") || retorno.equals(".00"))
			retorno = "0";
		return retorno;

	}

	public static Float parseStringDecimalToFloat(String text) {
		if (text.isEmpty())
			return 0f;

		if (text.contains(VIRGULA.toString()) && text.contains(PONTO.toString())) {
			StringBuilder temp = new StringBuilder();
			for (char i : text.toCharArray())
				if (i != PONTO)
					temp.append(i);
			text = temp.toString();
		}
		String valor = text.replace(VIRGULA, PONTO);
		return Float.valueOf(valor);
	}

	public static boolean isPalavraAcentuada(String string) {

		return Pattern.compile("[^A-Za-z0-9\\._\\s]").matcher(string).find();

	}

	public static boolean isNotNull(Object... objects) {

		for (Object object : objects) {
			if (object != null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEqualsAny(String valor, String... valores) {
		for (String string : valores) {
			if (string.equals(valor))
				return true;
		}
		return false;
	}
}
