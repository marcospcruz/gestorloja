package br.com.marcospcruz.gestorloja.view.components;

import javax.swing.JCheckBox;

public class MyJCheckBox extends JCheckBox {

	public MyJCheckBox(String string) {
		super(string);
	}

	@Override
	public boolean equals(Object arg0) {
		// boolean equals = super.equals(arg0);

		boolean equals = (arg0 instanceof MyJCheckBox) ? getText().equals(((MyJCheckBox) arg0).getText()) : false;
		return equals;
	}

}
