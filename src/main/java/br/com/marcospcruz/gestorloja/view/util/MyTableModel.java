package br.com.marcospcruz.gestorloja.view.util;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.hibernate.dialect.ColumnAliasExtractor;

public class MyTableModel extends AbstractTableModel {

	private List linhas;

	private Object[] colunas;

	private Class<? extends Object>[] tiposColuna;

	public MyTableModel(List linhas, Object[] colunas) {

		this.linhas = linhas;

		this.colunas = colunas;

		defineTipoColuna();

		fireTableDataChanged();

	}

	private void defineTipoColuna() {
		// for (Object linha : linhas) {
		// Object[] colunas = (Object[]) linha;
		// int i = 0;
		// if (tiposColuna == null) {
		// tiposColuna = new Class[colunas.length];
		// }
		// for (Object coluna : colunas) {
		//
		// tiposColuna[i++] = coluna.getClass();
		// }
		//
		// }
	}

	public int getColumnCount() {

		return colunas.length;
	}

	public int getRowCount() {

		return linhas.size();
	}

	public Object getValueAt(int linha, int coluna) {

		Object[] array = (Object[]) linhas.get(linha);

		Object value = array[coluna];
		if (value instanceof String)
			return value.toString();
		else if (value instanceof Boolean)
			return Boolean.valueOf(value.toString());
		return value;

	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		int x = colunas.length - 1;
		if (arg0 == x) {
			return Boolean.class;

		} else
			return String.class;
	}
	// return tiposColuna[arg0];

	public String getColumnName(int col) {

		return colunas[col].toString();

	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		return super.isCellEditable(rowIndex, columnIndex);

		// return true;

	}

	public List getLinhas() {

		return linhas;
	}

}
