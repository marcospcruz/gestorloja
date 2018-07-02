package br.com.marcospcruz.gestorloja.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @see https://stackoverflow.com/a/41926375/230513
 * @see https://stackoverflow.com/a/14858272/230513
 * @see https://stackoverflow.com/a/8504753/230513
 * @see https://stackoverflow.com/a/14011536/230513
 */
public class DynamicGroupLayout {

    private static final int NUM = 6;
    private GroupLayout layout;
    private GroupLayout.ParallelGroup horizontalGroup;
    private GroupLayout.SequentialGroup verticalGroup;
    private int i;

    private JPanel create() {
        JPanel panel = new JPanel();
        layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        horizontalGroup = layout.createParallelGroup();
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontalGroup));
        verticalGroup = layout.createSequentialGroup();
        layout.setVerticalGroup(verticalGroup);
        for (int i = 0; i < NUM; i++) {
            add();
        }
        return panel;
    }

    private void add() {
        JLabel label = new JLabel(String.valueOf(i + 1), JLabel.RIGHT);
        JTextField field = new JTextField(String.valueOf("String " + (i + 1)));
        label.setLabelFor(field);
        horizontalGroup.addGroup(layout.createSequentialGroup().
            addComponent(label).addComponent(field));
        verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
            addComponent(label).addComponent(field));
        i++;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new JFrame("DynamicGroupLayout");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                final DynamicGroupLayout dgl = new DynamicGroupLayout();
                final JPanel panel = dgl.create();
                JScrollPane jsp = new JScrollPane(panel) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(320, 240);
                    }
                };
                jsp.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                });
                f.add(jsp);
                JPanel controls = new JPanel();
                controls.add(new JButton(new AbstractAction("Add") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dgl.add();
                        panel.validate();
                    }
                }));
                f.add(controls, BorderLayout.SOUTH);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}