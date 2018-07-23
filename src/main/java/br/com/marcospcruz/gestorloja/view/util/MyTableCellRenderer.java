package br.com.marcospcruz.gestorloja.view.util;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import br.com.marcospcruz.gestorloja.util.FontMapper;

public class MyTableCellRenderer extends DefaultTableCellRenderer {
	// static final Font FONT_TAHOMA_22 = new Font("Tahoma", Font.PLAIN, 22);
	// static final Font FONT_TAHOMA_20 = new Font("Tahoma", Font.PLAIN, 20);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Object cellValue = table.getValueAt(row, column);
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setFont(FontMapper.getFont(20));
		setHorizontalAlignment(SwingConstants.RIGHT);
		int width = component.getPreferredSize().width;
		TableColumn coluna = table.getColumnModel().getColumn(column);
		int tableWidth = table.getPreferredSize().width;
		int minWidth = coluna.getMinWidth();
		int maxWidth = coluna.getMaxWidth();
		// if (column == 0) {
		// System.out.print("table: " + tableWidth);
		int colunaPrefferedWidth = Math.max(width + table.getIntercellSpacing().width, coluna.getPreferredWidth());
		// System.out.println(" coluna " + column + ": " + colunaPrefferedWidth + "
		// percentual: "
		// + ((float) colunaPrefferedWidth / tableWidth));
		// }
		coluna.setPreferredWidth(colunaPrefferedWidth);
		return component;
	}

}
