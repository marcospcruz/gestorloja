package br.com.marcospcruz.gestorloja.view.components;

import java.awt.Component;
import java.awt.IllegalComponentStateException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.view.AbstractDialog;

/**
 * 
 * JComboBox with an autocomplete drop down menu. This class is hard-coded for
 * String objects, but can be
 * 
 * altered into a generic form to allow for any searchable item.
 * 
 * @author G. Cope
 *
 * 
 * 
 */

public class AutocompleteJComboBox<T> extends JComboBox<T> {

	static final long serialVersionUID = 4321421L;

	private JComboBox primaryComboBox;
	private String desc;
	private ControllerBase controller;

	private AbstractDialog owner;

	private boolean keepCurrentModel;

	private Object objetoSelecionado;

	public AutocompleteJComboBox(AbstractDialog owner, ControllerBase controller, JComboBox primaryComboBox) {
		this(owner, controller, primaryComboBox, true);
	}

	/**
	 * 
	 * Constructs a new object based upon the parameter searchable
	 * 
	 * @param owner
	 * 
	 * @param s
	 * 
	 */

	public AutocompleteJComboBox(AbstractDialog owner, ControllerBase controller, JComboBox primaryComboBox,
			boolean setPopupVisibleOnFocus) {

		super();
		this.primaryComboBox = primaryComboBox;

		this.owner = owner;

		setEditable(true);

		this.controller = controller;

		Component c = getEditor().getEditorComponent();
		addActionListener(owner);
		if (c instanceof JTextComponent) {

			final JTextComponent tc = (JTextComponent) c;

			tc.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					// System.out.println();
					desc = "";
					if (primaryComboBox != null) {

					}

				}

				@Override
				public void focusGained(FocusEvent e) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							JTextComponent textComponent = (JTextComponent) e.getSource();
							// textComponent.setSelectionEnd(textComponent.getCaretPosition());
							try {
								textComponent.selectAll();
								setPopupVisible(setPopupVisibleOnFocus);
							} catch (IllegalComponentStateException e) {
								e.printStackTrace();
							}
						}
					});

				}
			});
			tc.getDocument().addDocumentListener(new DocumentListener() {

				@Override

				public void changedUpdate(DocumentEvent arg0) {
				}

				@Override

				public void insertUpdate(DocumentEvent arg0) {

					update();

				}

				@Override

				public void removeUpdate(DocumentEvent arg0) {

					update();

				}

				public void update() {

					// perform separately, as listener conflicts between the editing component

					// and JComboBox will result in an IllegalStateException due to editing

					// the component when it is locked.

					SwingUtilities.invokeLater(new Runnable() {

						@Override

						public void run() {

							DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) getModel();
							if(tc.getText().length()<5)
								return;
							// removeAllItems();
							// reloadModel(model);

							// if (keepCurrentModel)
							// System.out.println(keepCurrentModel + " - " + getSelectedItem());
							if (keepCurrentModel) {

								model.setSelectedItem(objetoSelecionado);
								tc.setText(objetoSelecionado.toString());
								// setSelectedItem(object);
								// keepCurrentModel = false;
								return;
							}
							ComboBoxModel<T> primaryModel;
							if (primaryComboBox != null) {
								primaryModel = primaryComboBox.getModel();
								if (primaryModel.getSelectedItem().toString().equals("Selecione uma Opção"))

									return;
							}
							try {

								if (!tc.getText().equals(desc)) {
									if (model.getElementAt(0) != null
											&& tc.getText().equals(model.getElementAt(0).toString()))
										return;
									String tcText = tc.getText();

									desc = tcText;

									// model.removeAllElements();
									owner.disableActionPerformed();
									
									try {
										removeAllItems();
									}catch(NullPointerException e) {
										e.printStackTrace();
									}
									reloadModel(model);

								} else
									return;
								List<T> founds = new ArrayList<>();
								if (!desc.isEmpty()) {

									for (int i = 1; i < model.getSize(); i++) {

										T item = model.getElementAt(i);
										// subCategorias.add(categoria);
										if (item.toString().toLowerCase().contains(desc.toLowerCase())) {
											// addItem(categoria);
											founds.add(item);
										}
										// addItem(categoria);

									}
									// model.removeAllElements();

									if (!founds.isEmpty()) {
										removeAllItems();
										for (T t : founds) {

											model.addElement((T) t);

										}
									} else
										model.addElement((T) " ");
									tc.setText(desc);
								}
								// Collections.sort(itensEstoquesString);// sort alphabetically

								// setEditable(false);
								// setFocusable(true);
								// removeAllItems();

								// if founds contains the search text, then only add once.

								// if (!foundSet.contains(tc.getText().toLowerCase())) {
								//
								// addItem(tc.getText());
								//
								// }
								//
								// for (String s : itensEstoquesString) {
								//
								// addItem(s);
								//
								// }

								setEditable(true);
								// controller.busca(desc); bugado
								atualizaDadosOwner();
								// Thread.sleep(500);
								setPopupVisible(false);
								boolean igualAUm = founds.size() >= 1;
								boolean descricaoDiferente = founds.size() <= 0 ? true
										: !founds.get(0).toString().equals(desc);
								// if (model.getSize() > 1)
								if (igualAUm && descricaoDiferente)
									setPopupVisible(true);
								// setSelectedIndex(0);
								if (!founds.isEmpty())
									setSelectedItem(founds.get(0));
								else
								// if (founds.size() == 1
								// && desc.equalsIgnoreCase(founds.get(0).toString()))
								if (desc.isEmpty())
									tc.selectAll();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								owner.enableActionPerformance();

							}

						}

					});

				}

			});

			// When the text component changes, focus is gained

			// and the menu disappears. To account for this, whenever the focus

			// is gained by the JTextComponent and it has searchable values, we show the
			// popup.

			tc.addFocusListener(new FocusListener() {

				@Override

				public void focusGained(FocusEvent arg0) {

					if (tc.getText().length() > 0) {

						setPopupVisible(true);

					}

				}

				@Override

				public void focusLost(FocusEvent arg0) {

				}

			});

		} else {

			throw new IllegalStateException("Editing component is not a JTextComponent!");

		}

	}

	@SuppressWarnings("unchecked")
	private void reloadModel(DefaultComboBoxModel<T> model) {
		T t1 = (T) "Selecione uma Opção.";
		model.addElement(t1);
		Collection<T> items = null;
		if (primaryComboBox != null) {
			try {
				controller.setItem(primaryComboBox.getSelectedItem());
			} catch (ClassCastException e) {
				e.printStackTrace();
			}

		} else {
			// controller.setList(null);
			controller.buscaTodos();
		}
		items = controller.getList();
		if (items != null)
			items.stream().forEach(result -> {

				model.addElement((T) result);

			});
	}

	private void atualizaDadosOwner() {
		// owner.getCategoriaProdutoController().setList(list);
		owner.atualizaTableModel();

	}

	public T getSelectedItem() {
		return (T) super.getSelectedItem();
	}

	public ComboBoxModel<T> getModel(boolean keepCurrentModel) {
		this.keepCurrentModel = keepCurrentModel;
		return super.getModel();
	}

	public void setSelectedItem(Object arg0, boolean keepCurrentModel) {
		this.keepCurrentModel = keepCurrentModel;
		objetoSelecionado = arg0;
		setSelectedItem(arg0);
	}

	@Override
	public void setSelectedItem(Object arg0) {

		super.setSelectedItem(arg0);
	}

	public void reloadModel() {
		DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) getModel();
		model.removeAllElements();
		reloadModel((DefaultComboBoxModel<T>) getModel());
		repaint();
	}

}