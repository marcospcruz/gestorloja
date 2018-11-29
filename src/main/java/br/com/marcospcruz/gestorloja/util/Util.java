package br.com.marcospcruz.gestorloja.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

import br.com.marcospcruz.gestorloja.AppFx;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;

public class Util {

	private static final Locale CURRENT_LOCALE = new Locale("pt", "BR");
	private static final Character VIRGULA = ',';
	private static final Character PONTO = '.';
	private static final String CONFIG_FILE_PATH = AppFx.CONTROLE_ESTOQUE_HOME + "/config/config.properties";

	public static String encryptaPassword(String password) throws NoSuchAlgorithmException {
		// Create MessageDigest instance for MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		// Add password bytes to digest
		md.update(password.getBytes());
		// Get the hash's bytes
		byte[] bytes = md.digest();
		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		// Get complete hashed password in hex format
		return sb.toString();
	}

	public static String formataDataAtual() {

		return formataDataHora(SingletonManager.getInstance().getData());
	}

	public static String formataMoeda(Float valorMoeda) {

		return NumberFormat.getCurrencyInstance(CURRENT_LOCALE).format(valorMoeda);
	}

	public static String formataDataHora(Date data) {

		return formataDataHora(data, "dd/MM/yyyy HH:mm:ss");

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
			retorno = "0,00";

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
		String valor = text.trim().replace(VIRGULA, PONTO);
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

	public static LocalDate parseDate(String text) {
		if (text.isEmpty()) {
			return null;
		}
		String[] data = text.split("/");

		int ano = Integer.parseInt(data[2]);
		int mes = Integer.parseInt(data[1]);
		int diaMes = Integer.parseInt(data[0]);
		return LocalDate.of(ano, mes, diaMes);
	}

	public static String[] aumentaArray(String[] array) {
		String[] backup = new String[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			backup[i] = array[i];
		}
		return backup;
	}

	public static String[] diminueArray(String[] array) {

		String[] backup = new String[array.length];
		for (int i = 0; i < array.length - 1; i++) {
			backup[i] = array[i];
		}
		return backup;
	}

	public static String formataData(Date data) {

		return formataDataHora(data, "dd/MM/yyyy");
	}

	private static String formataDataHora(Date data, String string) {
		SimpleDateFormat sdf = new SimpleDateFormat(string);
		return sdf.format(data);
	}

	public static double getDoubleValue(String value) {

		return Double.valueOf(value);
	}

	public static Date parseData(String text) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		return sdf.parse(text);
	}

	public static Properties getConfigFileProperties() throws IOException {
		Properties configProperties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(CONFIG_FILE_PATH));
			configProperties.load(inputStream);
			return configProperties;
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return null;
	}

	public static void saveConfigProperty(String key, String value) throws IOException {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(CONFIG_FILE_PATH);
			Properties properties = getConfigFileProperties();
			// if
			properties.put(key, value);
			properties.store(output, null);
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			output.close();
		}

	}

	public static void removeConfigProperty(String dataManutencaoKey) {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(CONFIG_FILE_PATH);
			Properties properties = getConfigFileProperties();
			properties.remove(dataManutencaoKey);
			properties.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean matchValorFloat(String value) {

		String re1 = ".*?"; // Non-greedy match on filler
		String re2 = "(\\.)"; // Any Single Character 1
		String re3 = "([0-9])"; // Any Single Character 2
		// String re4 = "([0-9])"; // Any Single Character 3
		String re4 = "*";
		String regex = re1 + re2 + re3 + re4;

		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();

	}

	public static String formataStringDecimaisVirgula(float newFloatValue) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format((double) newFloatValue).replace('.', ',');

	}

	public static String formataStringDecimais(String newValue) {

		newValue = newValue.replace(".", "");
		return Float.toString(Float.parseFloat(newValue) / 100);
	}

	public static String formataMoedaSemSimbolo(float valor) {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(CURRENT_LOCALE);
		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("");
		((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);
		return numberFormat.format(valor);
	}

}
