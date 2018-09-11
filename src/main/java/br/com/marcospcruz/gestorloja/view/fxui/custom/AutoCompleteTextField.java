package br.com.marcospcruz.gestorloja.view.fxui.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoCompleteTextField<T> extends ComboBox<T> {
	//@formatter:off
	protected static final List<KeyCode> SKIP_THOSE = Arrays.asList(
			KeyCode.LEFT, 
			KeyCode.HOME, 
			KeyCode.END, 
			KeyCode.UP,
			KeyCode.DOWN, 
			KeyCode.RIGHT, 
			KeyCode.ALT, 
			KeyCode.CONTROL, 
			KeyCode.TAB,
			KeyCode.SHIFT);
	//@formatter:on
	private List<T> backup;

	private String old;
	private int caretPosition;
	private KeyCode keyCode;
	private T value;

	public AutoCompleteTextField() {
		this(true);
	}

	public AutoCompleteTextField(boolean showItemsOnFocus) {
		setEditable(true);
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		TextField textComponent = getEditor();
		textComponent.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (showItemsOnFocus) {
					if (newValue) {
						String value = getEditor().getText();
						if (value.isEmpty() && backup != null) {
							getItems().removeAll(getItems());
							getItems().addAll(backup);
						}

						show();
					} else
						hide();
				}
			}
		});
		// textComponent.textProperty().addListener(new ChangeListener<String>() {
		//
		// @Override
		// public void changed(ObservableValue<? extends String> observable, String
		// oldValue, String newValue) {
		// Platform.runLater(() -> update(oldValue, newValue));
		// }
		// });

		textComponent.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				old = textComponent.getText();
				keyCode = event.getCode();
				// System.out.println(keyCode);
				if (keyCode.equals(KeyCode.TAB)) {
					hide();
				}
				if (SKIP_THOSE.contains(keyCode)) {
					return;
				}
				TextField source = (TextField) event.getSource();
				TextField target = (TextField) event.getTarget();
				caretPosition = target.getCaretPosition();
				// System.out.print(textComponent.getText() + " - ");
				// System.out.print(source.getText());
				// System.out.println(" - " + target.getText());
				// String oldValue = textComponent.getText();
				Platform.runLater(() -> update(target.getText(), null));

			}
		});

	}

	public void update(String oldValue, String newValue) {		ObservableList<T> items = getItems();
//		System.out.println(old + ": " + caretPosition);
		// if (newValue.length() == 1)
		// teste = newValue.toUpperCase();
		// else if (newValue.length() >= 1)
		// teste = "";
		// else if (oldValue.equalsIgnoreCase(teste)) {
		// items.removeAll(items);
		// items.addAll(backup);
		// getEditor().setText(newValue);
		// hide();
		// return;
		// }

		String value;
		// = newValue.isEmpty() ? oldValue.toUpperCase() :
		// newValue.toUpperCase();
		// String value = newValue.toUpperCase();
		// if (value.isEmpty())
		value = oldValue.toUpperCase();
//		System.out.println(oldValue + " - " + newValue + ": " + value);

		if (isShowing())
			hide();

		if (backup == null) {
			backup = new ArrayList<>(items);
		}
		// }
		// if (items.size() == 1) {
		// T t = items.get(0);
		// if (t.toString().equals(value)) {
		// setValue(t);
		// hide();
		// return;
		// }
		// }

		items.removeAll(items);
		items.addAll(backup);
		if (value.isEmpty())

		{
			show();
			setValue(null);
			getOnAction();
			return;
		}

		LinkedList<T> found = new LinkedList<>();
		for (int i = 0; i < items.size(); i++) {
			T t = items.get(i);
			if (t.toString().toUpperCase().contains(value)) {
				found.add(t);
			}
		}
		// if (!found.isEmpty()) {
		// if (found.size() > 1)
		// items.remove(1, items.size());
		// else
		items.removeAll(items);
		// if (found.size() == 1)
		// setValue(found.get(0));

		items.addAll(found);

		// }
		show();
		// if (!value.isEmpty() && !value.toUpperCase().equals(getEditor().getText()) &&
		// getEditor().getText().isEmpty()) {
		// getEditor().setText(value);
		// }
//		System.out.println(getValue());
		boolean condicao = (!keyCode.equals(KeyCode.DELETE) || !keyCode.equals(KeyCode.BACK_SPACE))
		// && getValue() == null
		;
		if (condicao || getValue() != null && getValue().toString().isEmpty()) {
			boolean isOldValue = old.isEmpty()
					&& (keyCode.equals(KeyCode.DELETE) || keyCode.equals(KeyCode.BACK_SPACE));
			getEditor().setText(isOldValue ? old : value);
			int cp;
			if (keyCode.equals(KeyCode.BACK_SPACE)) {
				if (caretPosition <= getEditor().getText().length())
					cp = caretPosition - 1;
				else
					cp = getEditor().getText().length();
			} else if (keyCode.equals(KeyCode.DELETE))
				cp = caretPosition;
			else
				cp = caretPosition + 1;
			getEditor().positionCaret(cp);
		}
		if (items.size() == 1 && getEditor().getText().equalsIgnoreCase(items.get(0).toString())) {
			getEditor().setText(items.get(0).toString());
			getEditor().positionCaret(getEditor().getText().length());
			setValue(items.get(0));
		}
	}

	public void setItems(List<T> itemsList) {
		ObservableList<T> list = getItems();
		list.removeAll(list);
		// subCategoriasObservableList.add("Selecione uma Opção.", null));
		list.addAll(itemsList);
		backup = null;
		// backup = itemsList;
	}

}
