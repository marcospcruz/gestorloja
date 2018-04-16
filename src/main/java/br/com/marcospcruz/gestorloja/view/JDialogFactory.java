package br.com.marcospcruz.gestorloja.view;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import br.com.marcospcruz.gestorloja.controller.LoginFacade;

public class JDialogFactory {

	public static void createDialog(String title, JFrame owner, String className)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class[] parametersTypes = new Class[] { String.class, JFrame.class };
		Object[] parameters = new Object[] { title, owner };
		createInstance(className, parametersTypes, parameters);

	}

	public static void createDialog(LoginFacade loginFacade, String title, JFrame owner, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		Class[] parametersTypes = new Class[] { LoginFacade.class, String.class, JFrame.class };
		Object[] parameters = new Object[] { loginFacade, title, owner };
		createInstance(className, parametersTypes, parameters);
	}

	private static void createInstance(String className, Class[] parametersTypes, Object[] parameters)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Class clazz = Class.forName(className);
		clazz.getConstructor(parametersTypes).newInstance(parameters);
	}

}
