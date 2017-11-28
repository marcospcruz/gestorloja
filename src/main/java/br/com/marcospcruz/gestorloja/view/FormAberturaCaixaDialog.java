package br.com.marcospcruz.gestorloja.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.AbstractController;
import br.com.marcospcruz.gestorloja.util.NumberDocument;
import br.com.marcospcruz.gestorloja.util.Util;

public class FormAberturaCaixaDialog extends JDialog {
	private JFormattedTextField saldoTextField;
	private AbstractController caixaController;

	public FormAberturaCaixaDialog(JDialog owner) {
		super(owner, true);
		setSize(400, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Abertura Caixa"));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);
		
		JLabel dataAberturaLbl = new JLabel(Util.formataDataAtual()); 
		dataAberturaLbl.setBorder(new TitledBorder("Data de Abertura"));
		GridBagConstraints gbc_dataAberturaLbl = new GridBagConstraints();
		gbc_dataAberturaLbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_dataAberturaLbl.gridwidth = 2;
		gbc_dataAberturaLbl.insets = new Insets(0, 0, 0, 5);
		gbc_dataAberturaLbl.gridx = 0;
		gbc_dataAberturaLbl.gridy = 0;
		panel.add(dataAberturaLbl, gbc_dataAberturaLbl);

		JLabel lblSaldoDoCaixa = new JLabel("Saldo do Caixa");
		GridBagConstraints gbc_lblSaldoDoCaixa = new GridBagConstraints();
		gbc_lblSaldoDoCaixa.insets = new Insets(0, 0, 0, 5);
		gbc_lblSaldoDoCaixa.anchor = GridBagConstraints.EAST;
		gbc_lblSaldoDoCaixa.gridx = 2;
		gbc_lblSaldoDoCaixa.gridy = 0;
		panel.add(lblSaldoDoCaixa, gbc_lblSaldoDoCaixa);

		saldoTextField = new JFormattedTextField();
		saldoTextField.setDocument(new NumberDocument(true));
		GridBagConstraints gbc_lbl_data = new GridBagConstraints();
		gbc_lbl_data.fill = GridBagConstraints.HORIZONTAL;
		gbc_lbl_data.gridx = 3;
		gbc_lbl_data.gridy = 0;
		panel.add(saldoTextField, gbc_lbl_data);
		saldoTextField.setColumns(10);
		try {
			carregaController();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
	}

	private void carregaController() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		caixaController=ControllerAbstractFactory.createController(ControllerAbstractFactory.CONTROLE_CAIXA);
		
	}

}
