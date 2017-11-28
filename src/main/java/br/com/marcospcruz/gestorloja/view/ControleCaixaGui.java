package br.com.marcospcruz.gestorloja.view;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.marcospcruz.gestorloja.abstractfactory.CommandFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;

public class ControleCaixaGui extends AbstractDialog {

	public ControleCaixaGui(String tituloJanela, JFrame owner) {

		super(owner, tituloJanela, true);
		getContentPane().setLayout(new BorderLayout(0, 0));

		try {
			getContentPane().add(configuraOperacoesPanel(), BorderLayout.NORTH);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setVisible(true);

	}

	private JPanel configuraOperacoesPanel() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		JPanel panel = new JPanel();

		CommandFactory btnFactory = new CommandFactory();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(btnFactory.createButton("Abrir Caixa", this));		
		panel.add(btnFactory.createButton("Fechar Caixa", this));
		panel.setBorder(new TitledBorder("Operações"));
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.getActionCommand());

	}

	@Override
	protected void configuraJPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJPanelBusca() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void selecionaAcao(String actionCommand) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected JPanel carregaJpanelFormulario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JPanel carregaJpanelTable(int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaTableModel(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void carregaTableModel() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List carregaLinhasTableModel(List lista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void excluiItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salvarItem() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void populaFormulario() {
		// TODO Auto-generated method stub

	}

}
