package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Demo {

    private void createAndShowGUI() {

        JPanel groupLayoutPanel = new JPanel();        
        GroupLayout groupLayout = new GroupLayout(groupLayoutPanel);
        groupLayout.setAutoCreateGaps(true);        
        groupLayoutPanel.setLayout(groupLayout);

        GroupLayout.Group hg1 = groupLayout.createParallelGroup(Alignment.TRAILING);
        GroupLayout.Group hg2 = groupLayout.createParallelGroup();

        GroupLayout.Group vg1 = groupLayout.createParallelGroup();
        GroupLayout.Group vg2 = groupLayout.createParallelGroup();

        JLabel nameLabel = new JLabel("Name : ");
        hg1.addComponent(nameLabel);
        vg1.addComponent(nameLabel);

        JComboBox<String> nameComboBox = new JComboBox<>();
        hg2.addComponent(nameComboBox);
        vg1.addComponent(nameComboBox);

        JLabel descriptionLabel = new JLabel("Desc : ");
        hg1.addComponent(descriptionLabel);
        vg2.addComponent(descriptionLabel);

        JTextField descriptionTextField = new JTextField(20);
        hg2.addComponent(descriptionTextField);
        vg2.addComponent(descriptionTextField);

        // Horizontal group
        GroupLayout.SequentialGroup hseq1 = groupLayout.createSequentialGroup();
        hseq1.addGroup(hg1);
        hseq1.addGroup(hg2);

        // Vertical group
        GroupLayout.SequentialGroup vseq1 = groupLayout.createSequentialGroup();
        vseq1.addGroup(vg1);
        vseq1.addGroup(vg2);

        groupLayout.setHorizontalGroup(hseq1);
        groupLayout.setVerticalGroup(vseq1);

        JPanel contentPane = new JPanel(new BorderLayout(4, 4));
        contentPane.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        contentPane.add(groupLayoutPanel);

        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Demo().createAndShowGUI();
            }
        });
    }
}