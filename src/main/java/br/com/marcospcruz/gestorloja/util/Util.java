package br.com.marcospcruz.gestorloja.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	public static String formataDataAtual() {
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(new Date());
	}

}
