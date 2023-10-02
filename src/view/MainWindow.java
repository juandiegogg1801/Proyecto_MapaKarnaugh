package view;
import controller.Control;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JPanel jPanel;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JLabel jLabel;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JTextField jTextField;
    private JButton jButton;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JButton jButton7;
    private JButton jButton8;
    private JButton jButton9;
    private JButton jButton10;
    private JTable jTable;
    private JTable jTable2;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableModel2;

    private JScrollPane scrollPane;
    private JScrollPane scrollPane2;
    private String currentText = "";

    public MainWindow() {
        this.setSize(500, 500);
        this.setTitle("Proyecto MDIS");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        // Cambiamos el layout principal a BorderLayout
        this.setLayout(new BorderLayout(10, 10)); // Espacio horizontal y vertical de 10 píxeles
        //Paneles
        jPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        jPanel2 = new JPanel(new GridLayout(2, 5));

        jPanel3 = new JPanel(new GridLayout(2, 2, 10, 10));
        jPanel3.setPreferredSize(new Dimension(200, 200)); // Establecemos el tamaño preferido de jPanel2

        //Componentes
        jLabel = new JLabel("Ingresar funcion booleana: ");
        jLabel2 = new JLabel("Tabla de verdad");
        jLabel3 = new JLabel("Mapa de Karnough");
        jLabel4 = new JLabel("Solucion: ");
        jTextField = new JTextField(20);
        jButton = new JButton("X");
        jButton2 = new JButton("Y");
        jButton3 = new JButton("Z");
        jButton4 = new JButton("W");
        jButton5 = new JButton("OK");
        jButton6 = new JButton("^");
        jButton7 = new JButton("v");
        jButton8 = new JButton("'");
        jButton9 = new JButton("(");
        jButton10 = new JButton(")");
        tableModel = new DefaultTableModel();
        jTable = new JTable(tableModel);
        tableModel2 = new DefaultTableModel();
        jTable2 = new JTable(tableModel2);

        //Agregar componentes
        jPanel2.add(jButton);
        jPanel2.add(jButton2);
        jPanel2.add(jButton3);
        jPanel2.add(jButton4);
        jPanel2.add(jButton5);
        jPanel2.add(jButton6);
        jPanel2.add(jButton7);
        jPanel2.add(jButton8);
        jPanel2.add(jButton9);
        jPanel2.add(jButton10);

        jPanel.add(jLabel);
        jPanel.add(jTextField);
        jPanel.add(jPanel2);

        jPanel3.add(jLabel2);
        jPanel3.add(jLabel3);
        scrollPane = new JScrollPane(jTable);
        scrollPane2 = new JScrollPane(jTable2);
        jPanel3.add(scrollPane);
        jPanel3.add(scrollPane2);

        this.add(jPanel, BorderLayout.NORTH);
        this.add(jPanel3, BorderLayout.CENTER);
        this.add(jLabel4, BorderLayout.SOUTH);
        this.add(jLabel4, BorderLayout.SOUTH); // Agregamos jPanel en la región sur

        //Eventos
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("X");
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("Y");
            }
        });
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("Z");
            }
        });
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("W");
            }
        });
        jButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ir al control
                currentText=jTextField.getText();
                Control control = new Control(currentText, jTextField, jTable, jTable2, jLabel4);
            }
        });
        jButton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("^");
            }
        });
        jButton7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("v");
            }
        });
        jButton8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("'");
            }
        });
        jButton9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write("(");
            }
        });
        jButton10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                write(")");
            }
        });
    }
    private void write(String string) {
        currentText = jTextField.getText(); // Obtener el texto actual del JTextField
        currentText += string; // Agregar "X" al texto actual
        jTextField.setText(currentText);
    }
}
