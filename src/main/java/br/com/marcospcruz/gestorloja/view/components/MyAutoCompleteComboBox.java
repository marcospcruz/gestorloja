package br.com.marcospcruz.gestorloja.view.components;

import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class MyAutoCompleteComboBox<T> extends JComboBox<T> {
	public MyAutoCompleteComboBox() {
		super();
		setEditable(true);
		setModel((ComboBoxModel<T>) carregaComboBoxModel(null));
		JTextComponent editor = (JTextComponent) getEditor().getEditorComponent();
		editor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setModel((ComboBoxModel<T>)carregaComboBoxModel(editor.getText()));
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}
		});
	}

	private DefaultComboBoxModel<String> carregaComboBoxModel(String string) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		if (string != null)
			model = new DefaultComboBoxModel<>(new String[] { "Marcos", "Aroldo", "João" });
		return model;
	}

	private void update() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setPopupVisible(true);
//				System.out.println(new Date());
			}
		});
	}
}
