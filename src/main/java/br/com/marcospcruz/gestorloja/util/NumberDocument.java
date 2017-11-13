package br.com.marcospcruz.gestorloja.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberDocument extends PlainDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7489934725537458032L;

	private boolean decimal;

	/**
	 * Construtor que n�o permite separador decimal.
	 */
	public NumberDocument() {

	}

	/**
	 * Construtor que permite separador decimal
	 * 
	 * @param decimal
	 */
	public NumberDocument(boolean decimal) {

		this.decimal = decimal;

	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {

		if (str == null) {
			return;
		}
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)) == false) {

				if (str.charAt(i) != ',' || str.charAt(i) == ',' && !decimal)

					return;

			}

		}

		super.insertString(offs, new String(str), a);

	}

}