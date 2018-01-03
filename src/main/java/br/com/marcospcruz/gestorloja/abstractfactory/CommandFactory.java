package br.com.marcospcruz.gestorloja.abstractfactory;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;

public class CommandFactory {

	private static final String CLASS_NAME = "javax.swing.JButton";

	public JButton createButton(String nomeModulo, ActionListener actionListener)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class controllerClass = Class.forName(CLASS_NAME);
		Object instance = controllerClass.newInstance();
		try {
			Method setTextMethod = controllerClass.getMethod("setText", String.class);
			Method addActionListenerMethod = controllerClass.getMethod("addActionListener", ActionListener.class);

			invokeMethod(instance, new Object[] { nomeModulo, actionListener }, setTextMethod, addActionListenerMethod);
		} catch (IllegalArgumentException | InvocationTargetException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		}
		return (JButton) instance;
	}

	private void invokeMethod(Object instance, Object[] args, Method... methods)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int x = 0;
		for (Method method : methods) {
			method.invoke(instance, args[x++]);
		}
	}

}
