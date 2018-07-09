package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import br.com.marcospcruz.gestorloja.abstractfactory.ControllerAbstractFactory;
import br.com.marcospcruz.gestorloja.controller.CaixaController;
import br.com.marcospcruz.gestorloja.controller.LoginFacade;
import br.com.marcospcruz.gestorloja.model.Caixa;
import br.com.marcospcruz.gestorloja.model.InterfaceGrafica;
import br.com.marcospcruz.gestorloja.model.Usuario;
import br.com.marcospcruz.gestorloja.systemmanager.SingletonManager;
import br.com.marcospcruz.gestorloja.util.FontMapper;
import br.com.marcospcruz.gestorloja.util.Util;
import br.com.marcospcruz.gestorloja.view.util.MyDateFormatter;

public class PrincipalGui extends AbstractJFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2738133745490348435L;
	private LoginFacade loginFacade;
	private JDatePickerImpl calendario;

	/**
	 * @param loginFacade
	 * @param loginFacade
	 */
	public PrincipalGui(LoginFacade loginFacade) {
		super("Gestor Loja");
		Usuario userLogado = loginFacade.getUsuarioLogado();
		int row = 1;
		int col = 1;
		// setLayout(new GridLayout(row, col));
		addWindowListener(this);
		this.loginFacade = loginFacade;
		carregaBotoesModulos();
		if (userLogado.getIdUsuario() == 1) {
			carregaDatePicker();
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}

	private void carregaDatePicker() {
		JPanel panel = new JPanel();
		panel.setBorder(criaTitledBorder("Manutenção"));
		getContentPane().add(panel, BorderLayout.CENTER);
		JButton btnLimpaData = inicializaJButton("Limpar data");
		btnLimpaData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SingletonManager.getInstance().setDataManutencao(null);
				calendario.getJFormattedTextField().setText("");
			}
		});
		JPanel dataPanel=criaDatePanel();
		dataPanel.add(btnLimpaData);
		panel.add(dataPanel);
		
	}

	private JPanel criaDatePanel() {
		JPanel datePanel = new JPanel();
		datePanel.setBorder(criaTitledBorder("Configurar Data da Aplicação"));
		UtilDateModel model = new UtilDateModel();
		Properties properties = new Properties();
		properties.put("text.today", "Hoje");
		properties.put("text.month", "Mês");
		properties.put("text.year", "Ano");
		JDatePanelImpl jDatePanel = new JDatePanelImpl(model, properties);

		calendario = new JDatePickerImpl(jDatePanel, new MyDateFormatter());
		calendario.getJFormattedTextField().setFont(FontMapper.getFont(22));
		datePanel.add(calendario);
		calendario.setFont(FontMapper.getFont(22));
		calendario.addActionListener(this);
		return datePanel;
	}

	private void carregaBotoesModulos() {
		JPanel panel = new JPanel();
		panel.setBorder(criaTitledBorder("Módulos"));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//@formatter:off
		getUsuarioLogado()
			.getPerfisUsuario()
			.stream()
			.forEach(perfilUsuario->{
				perfilUsuario.getInterfaces()
					.stream()
					.forEach(componente->{
						
					
						JButton button=inicializaJButton(componente.getNomeModulo());
						panel.add(button);
					
					});
		});
		//@formatter:on
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			LocalDate dataManutencao = Util.parseDate(calendario.getJFormattedTextField().getText());
			if (dataManutencao != null)
				SingletonManager.getInstance().setDataManutencaoSistema(dataManutencao);
			switch (e.getActionCommand()) {
			case InterfaceGrafica.ESTOQUE:
				JDialogFactory.createDialog(InterfaceGrafica.ESTOQUE, this, InterfaceGrafica.CLASS_NAME_ESTOQUE);
				break;
			case InterfaceGrafica.CONTROLE_DE_CAIXA:
				JDialogFactory.createDialog(InterfaceGrafica.CONTROLE_DE_CAIXA, this,
						InterfaceGrafica.CLASS_NAME_CAIXA);
				// new ControleCaixaGui(getLoginFacade(), InterfaceGrafica.CONTROLE_DE_CAIXA,
				// this);
				break;
			case InterfaceGrafica.PONTO_DE_VENDA:
				CaixaController caixaController = (CaixaController) SingletonManager.getInstance()
						.getController(ControllerAbstractFactory.CONTROLE_CAIXA);
				Caixa caixaAberto = (Caixa) caixaController.getItem();
				if (caixaAberto == null) {
					throw new NullPointerException("Não há Caixa aberto para realização de vendas.");
				}
				JDialogFactory.createDialog(InterfaceGrafica.CLASS_NAME_PDV, this, InterfaceGrafica.CLASS_NAME_PDV);
			}
		} catch (Exception e1) {

			e1.printStackTrace();
			showErrorMessage(this, e1.getMessage());
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(calendario.getJFormattedTextField().getText());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		loginFacade.fechaSessaoUsuario();

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
