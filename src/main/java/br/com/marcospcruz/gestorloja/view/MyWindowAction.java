package br.com.marcospcruz.gestorloja.view;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import br.com.marcospcruz.gestorloja.controller.ControllerBase;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.util.NumberDocument;
import br.com.marcospcruz.gestorloja.view.util.MyTableCellRenderer;
import br.com.marcospcruz.gestorloja.view.util.MyTableModel;

public interface MyWindowAction extends ActionListener, MouseListener {
	default void removeController(ControllerBase controller) {
		SingletonManager.getInstance().removeController(controller);

	}

	default JButton inicializaJButton(String text) {

		JButton jButton = new JButton(text);

		jButton.addActionListener(this);

		jButton.setFont(FontMapper.getFont(20));

		return jButton;
	}

	default JFormattedTextField inicializaNumberField() {

		return inicializaTextField(false, false);
	}

	default JFormattedTextField inicializaDecimalNumberField() {

		return inicializaTextField(false, true);
	}

	default JFormattedTextField inicializaAlfaNumericTextField() {
		return inicializaTextField(true, false);

	}

	default JFormattedTextField inicializaTextField(boolean isAlfaNumeric, boolean isNumericDecimalField) {
		JFormattedTextField txtField = new JFormattedTextField();
		if (!isAlfaNumeric)
			txtField.setDocument(new NumberDocument(isNumericDecimalField));

		txtField.setColumns(10);
		txtField.setFont(FontMapper.getFont(20));
		return txtField;
	}

	default Border criaTitledBorder(String string) {
		TitledBorder border = BorderFactory.createTitledBorder(string);
		border.setTitleFont(FontMapper.getFont(20));

		return border;
	}

	public default void showErrorMessage(Component owner, String message) {
		JLabel label = criaJLabel(message);
		JOptionPane.showMessageDialog(owner, label, "Erro!", JOptionPane.ERROR_MESSAGE);

	}

	public default void showMessage(Component owner, String message) {
		JLabel label = criaJLabel(message, true);
		JOptionPane.showMessageDialog(owner, label);

	}

	default JLabel criaJLabel(String string) {

		return criaJLabel(string, false);
	}

	default JLabel criaJLabel(String string, int fontSize) {
		JLabel label = criaJLabel(string);
		label.setFont(FontMapper.getFont(fontSize));
		return label;
	}

	default JLabel criaJLabel(String string, boolean rightAlignment) {

		int alignment = !rightAlignment ? SwingConstants.RIGHT : SwingConstants.LEFT;
		JLabel label = new JLabel(string);
		label.setAlignmentX(alignment);
		label.setFont(FontMapper.getFont(20));
		// label.setBorder(BorderFactory.createEtchedBorder());
		return label;
	}

	default JTextField createJTextField() {
		JTextField text = new JTextField();
		text.setFont(FontMapper.getFont(20));
		return text;
	}

	public default void exibeMensagemSucesso(Component owner, String mensagem) {

		JLabel label = criaJLabel(mensagem);

		JOptionPane.showMessageDialog(owner, label, "", JOptionPane.INFORMATION_MESSAGE);

	}

	public default JTable inicializaJTable(MyTableModel myTableModel) {

		JTable jTable = new JTable(myTableModel) {
			@Override
			public Class getColumnClass(int x) {
				return getModel().getColumnClass(x);
			}
		};
		jTable.getTableHeader().setFont(FontMapper.getFont(20));
		jTable.addMouseListener(this);
		jTable.setFillsViewportHeight(true);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		for (int i = 0; i < jTable.getColumnCount(); i++) {
			TableColumn coluna = jTable.getColumnModel().getColumn(i);

			MyTableCellRenderer tableCellRenderer = new MyTableCellRenderer();
			coluna.setCellRenderer(tableCellRenderer);
			if(i==8)
				coluna.setCellEditor(new DefaultCellEditor(new JCheckBox()));
			// coluna.setPreferredWidth(15);
		}
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// direita = new DefaultTableCellRenderer();

		// direita.setHorizontalAlignment(SwingConstants.RIGHT);

		jTable.setRowHeight(50);

		return jTable;

	}
}
