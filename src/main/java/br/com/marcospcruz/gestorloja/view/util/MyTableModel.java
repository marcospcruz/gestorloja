package br.com.marcospcruz.gestorloja.view.util;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

	private List linhas;

	private Object[] colunas;

	public MyTableModel(List linhas, Object[] colunas) {

		this.linhas = linhas;

		this.colunas = colunas;

		fireTableDataChanged();

	}

	public int getColumnCount() {

		return colunas.length;
	}

	public int getRowCount() {

		return linhas.size();
	}

	public Object getValueAt(int linha, int coluna) {

		Object[] array = (Object[]) linhas.get(linha);

		return array[coluna];

	}

	public String getColumnName(int col) {

		return colunas[col].toString();

	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		// return super.isCellEditable(rowIndex, columnIndex);

		return false;

	}

	public List getLinhas() {
	
		return linhas;
	}

}
