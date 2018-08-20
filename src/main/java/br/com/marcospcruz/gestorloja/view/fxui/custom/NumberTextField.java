package br.com.marcospcruz.gestorloja.view.fxui.custom;

import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NumberTextField extends TextField {
	protected static final List<KeyCode> SKIP_THOSE = Arrays.asList(KeyCode.HOME, KeyCode.END, KeyCode.UP, KeyCode.DOWN,
			KeyCode.RIGHT, KeyCode.ALT, KeyCode.CONTROL, KeyCode.LEFT, KeyCode.BACK_SPACE);
	private boolean acceptComma;

	public NumberTextField(boolean acceptComma) {
		this();
		this.acceptComma = acceptComma;
		// if (acceptComma)
		// setText("0,00");
		focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// if(newValue)
				// selectPreviousWord();
			}
		});
	}

	public NumberTextField() {
		super();

		setOnKeyPressed(this::handle);
	}

	public void handle(KeyEvent event) {
		if (event.getCode().equals(KeyCode.LEFT))
			positionCaret(getText().length());
		if (!SKIP_THOSE.contains(event.getCode())) {
			Platform.runLater(() -> {
				String value = getText();
				char[] charArray = value.toCharArray();
				StringBuilder temp = new StringBuilder();
				for (int i = 0; i < charArray.length; i++) {
					char caractere = charArray[i];
					if (Character.isDigit(caractere) || (acceptComma && caractere == ',')) {
						if (value.contains(",")) {
							int commaPos = value.indexOf(',');
							int result = temp.length() - commaPos;
							if (result > 2)
								continue;
						}
						temp.append(caractere);
					}
				}

				setText(temp.toString());
				positionCaret(getText().length());
			});
		}

	}
}