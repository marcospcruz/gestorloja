package br.com.marcospcruz.gestorloja.view.fxui.custom;

import java.util.Arrays;
import java.util.List;

import br.com.marcospcruz.gestorloja.util.Util;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumberTextField extends TextField {
	//@formatter:off
	protected static final List<KeyCode> SKIP_THOSE = Arrays.asList(
			KeyCode.SHIFT,
			KeyCode.HOME, 
			KeyCode.END, 
			KeyCode.UP, 
			KeyCode.DOWN,
			KeyCode.RIGHT, 
			KeyCode.ALT, 
			KeyCode.CONTROL, 
			KeyCode.LEFT,
			KeyCode.TAB);
	//@formatter:on
	private boolean acceptComma;

	public NumberTextField(boolean acceptComma) {
		this();
		this.acceptComma = acceptComma;
		// if (acceptComma)

		setText(Util.formataStringDecimais(0));
		if (acceptComma) {
			textProperty().addListener((observable, oldText, newText) -> {
				if (newText.length() > 10)
					setText(oldText);

				// String newValue = newText.replace(",", "");
				//
				// float novoValor = Float.parseFloat(newValue) * 0.01f;
				// String valorMoeda = Util.formataMoeda(novoValor).substring(3);
				// if (altera) {
				// altera = false;
				// setText(valorMoeda);
				// }

			});

			focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					String value = getText();
					int position = value.length();

					positionCaret(position);
					deselect();
				}
			});
		}
	}

	public NumberTextField() {
		super();

		setOnKeyPressed(this::handle);
	}

	public void handle(KeyEvent event) {
		String tmp = getText();
		int posicaoCaret = getCaretPosition();
		if (posicaoCaret < getText().length())
			positionCaret(tmp.length() + 1);
		if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.HOME))
			positionCaret(tmp.length() + 1);
		if (!SKIP_THOSE.contains(event.getCode())) {
			Platform.runLater(() -> {
				String valorString = getText().replace(".", "");
				valorString = valorString.replace(",", ".");
				StringBuilder value = new StringBuilder();
				// valorString.equals("") ? "0.00" : valorString.replace(',', '.'));

				//
				int contaPontos = 0;
				boolean skip = false;
				// limpando caracteres nao numericos
				for (char c : valorString.toCharArray()) {
					boolean ehDigito = Character.isDigit(c);
					boolean ehPonto = (c == '.');
					if (ehPonto)
						contaPontos++;
					if (ehDigito || (ehPonto && acceptComma) && contaPontos == 1) {
						// int i = value.indexOf(Character.toString(c));

						value.append(c);
					} else {
						skip = true;
					}

				}

				// tecla delete
				if (acceptComma) {
					float valor = Float.parseFloat(!value.toString().isEmpty() ? value.toString() : "0");
					if (!skip) {

						if (event.getCode() == KeyCode.DELETE) {
							valor /= 10f;

						} else if (event.getCode() == KeyCode.BACK_SPACE)
							valor /= 10f;
						else if (value.length() == 1)
							valor /= 100f;
						else
							valor *= 10f;

						String valorFinal = Float.toString(valor);
						if (valorFinal.substring(valorFinal.indexOf('.') + 1).length() == 2)
							setText(Util.formataMoeda(valor).substring(3));
						else
							valorFinal = Util.formataMoeda(Float.parseFloat(value.toString().replace(',', '.')))
									.substring(3);
					}
					setText(Util.formataMoeda(valor).substring(3));
				} else {
					int valor = Integer.parseInt(!value.toString().isEmpty() ? value.toString() : "0");
					setText(value.toString());
				}
				// value = Util.formataMoeda(valor).substring(3);
				// char[] charArray = value.toCharArray();
				// char[] charArray=.toCharArray();
				// StringBuilder temp = new StringBuilder();
				// for (int i = 0; i < charArray.length; i++) {
				// char caractere = charArray[i];
				// if (Character.isDigit(caractere) || (acceptComma && caractere == ',')) {
				// if (value.contains(",")) {
				// int commaPos = value.indexOf(',');
				// int result = temp.length() - commaPos;
				// if (result > 2)
				// continue;
				// }
				// temp.append(caractere);
				// }
				// }

				positionCaret(getText().length());
			});
		}

	}

	public void formataString() {
		Platform.runLater(() -> {
			@SuppressWarnings("unused")
			String value = getText();
			StringBuilder temp = new StringBuilder();
			// 1 trocar ponto por virgula separando decimais
			String regex = ".(\\d{2}+):";

			float valor = 0;
			if (Util.matchValorFloat(value)) {
				valor = Float.parseFloat(value);

				temp.append(Util.formataMoeda(valor).substring(3));

			} else
				temp = new StringBuilder(value);
			//

			setText(temp.toString());
		});
	}

	public void old(KeyEvent evt) {
		String value = getText();
		char[] charArray = value.toCharArray();
		StringBuilder temp = new StringBuilder();
		int contaVirgula = 0;
		for (int i = 0; i < charArray.length; i++) {
			char caractere = charArray[i];
			if (Character.isDigit(caractere) || (acceptComma && caractere == ',')) {
				if (value.contains(",")) {
					if (caractere == ',')
						contaVirgula++;
					// int commaPos = value.indexOf(',');
					// int result = temp.length() - commaPos;
					// if (result > 2)
					// continue;
				}
				if (contaVirgula > 1 && caractere == ',')
					continue;
				temp.append(caractere);
			}
		}

		setText(temp.toString());
		positionCaret(getText().length());
	}

	public boolean isAcceptComma() {
		return acceptComma;
	}

	public void setAcceptComma(boolean acceptComma) {
		this.acceptComma = acceptComma;
	}

}