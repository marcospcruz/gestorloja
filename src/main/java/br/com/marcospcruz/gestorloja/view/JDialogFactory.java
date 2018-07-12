package br.com.marcospcruz.gestorloja.view;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class JDialogFactory {

	public static void createDialog(String title, Object owner, String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Class[] parametersTypes = new Class[] { String.class,
				(owner instanceof JFrame ? JFrame.class : JDialog.class) };
		Object[] parameters = new Object[] { title, owner };
		Class clazz = Class.forName(className);
		clazz.getConstructor(parametersTypes).newInstance(parameters);
	}

}
