package Vista;

import config.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Objects;

public class Programa extends JDialog {
    JPanel contentPane;
    JButton buttonCancel;
    JTextField txtPregunta;
    JTextField txtRespuesta1;
    JTextField txtRespuesta2;
    JTextField txtRespuesta3;
    JButton btnAgregar;
    JButton btnModificar;
    JButton btnBorrar;
    JButton btnLimpiar;
    JTable tablaDatos;
    JTextField txtId;
    JComboBox<String> comboBox;
    JButton updateButton;
    JButton btnComenzar;
    Conexion cn = new Conexion();
    Connection con;
    DefaultTableModel model;
    Statement st;
    ResultSet rs;
    int id = 0;

    public Programa() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnAgregar);
        listar();

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Agregar();
                listar();
                limpiar();
            }
        });

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Modificar();
            listar();
            limpiar();
            }
        });

        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Eliminar();
            listar();
            limpiar();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            limpiar();
            }
        });
        tablaDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaDatos.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "No se Selecciono");
                } else {
                    id = Integer.parseInt((String) tablaDatos.getValueAt(row, 0).toString());
                    String Enunciado = (String) tablaDatos.getValueAt(row, 1);
                    String Respuesta1 = (String) tablaDatos.getValueAt(row, 2);
                    String Respuesta2 = (String) tablaDatos.getValueAt(row, 3);
                    String Respuesta3 = (String) tablaDatos.getValueAt(row, 4);
                    txtId.setText("" + id);
                    txtPregunta.setText(Enunciado);
                    txtRespuesta1.setText(Respuesta1);
                    txtRespuesta2.setText(Respuesta2);
                    txtRespuesta3.setText(Respuesta3);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarComboBox();
            }
        });

        btnComenzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tablaDatos.getRowCount() == 0){
                    JOptionPane.showMessageDialog(null, "Debe de haber una pregunta para comenzar con el cuestionario");
                } else {
                dispose();
                comenzarQuizz comenzar = new comenzarQuizz();
                comenzar.pack();
                comenzar.iniciarCuestionario();
                comenzar.setSize(800, 600);
                comenzar.setLocationRelativeTo(null);
                comenzar.setVisible(true);
                System.exit(0);
                }
            }
        });
    }

    private void actualizarComboBox() {
        comboBox.removeAllItems();
        comboBox.addItem(txtRespuesta1.getText());
        comboBox.addItem(txtRespuesta2.getText());
        comboBox.addItem(txtRespuesta3.getText());
    }


    private void salir() {
        dispose();
    }

    void listar() {
        String sql = "select * from preguntas";
        try {
            con = cn.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            Object[] preguntas = new Object[6];
            String[] titulos = {"CÓDIGO","PREGUNTA","RESPUESTA1","RESPUESTA2","RESPUESTA3","RESPUESTACORRECTA"};
            model = new DefaultTableModel(null,titulos);
            tablaDatos.setModel(model);
            while (rs.next()) {
                preguntas[0] = rs.getInt("codigo");
                preguntas[1] = rs.getString("pregunta");
                preguntas[2] = rs.getString("respuesta1");
                preguntas[3] = rs.getString("respuesta2");
                preguntas[4] = rs.getString("respuesta3");
                preguntas[5] = rs.getString("respuestaCorrecta");
                model.addRow(preguntas);
            }
            tablaDatos.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Agregar() {
        String id = txtId.getText();
        String Enunciado = txtPregunta.getText();
        String Respuesta1 = txtRespuesta1.getText();
        String Respuesta2 = txtRespuesta2.getText();
        String Respuesta3 = txtRespuesta3.getText();
        String Resultado = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
        try {
            if (Enunciado.equals("") || Respuesta1.equals("") || Respuesta2.equals("") || Respuesta3.equals("") || Resultado.equals("")) {
                JOptionPane.showMessageDialog(null, "Debe Ingresar Datos");
                limpiarTabla(model);
            } else {
                String sql = "INSERT INTO preguntas (codigo, pregunta, respuesta1, respuesta2, respuesta3, respuestaCorrecta) VALUES('" + id + "','" + Enunciado + "','" + Respuesta1 + "','" + Respuesta2 + "','" + Respuesta3 + "','" + Resultado + "')";
                con = cn.getConnection();
                st = con.prepareStatement(sql);
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Pregunta añadida con Exito");

                limpiarTabla(model);
            }

        } catch (Exception e) {
        }
    }

    void Modificar() {
        String id = txtId.getText();
        String Enunciado = txtPregunta.getText();
        String Respuesta1 = txtRespuesta1.getText();
        String Respuesta2 = txtRespuesta2.getText();
        String Respuesta3 = txtRespuesta3.getText();
        String Resultado = Objects.requireNonNull(comboBox.getSelectedItem()).toString();

        String sql = "update pregunta set pregunta='" + Enunciado + "',respuesta1='" + Respuesta1 + "', respuesta2='" + Respuesta2 +"', respuesta3='" + Respuesta3 +"' where codigo=" + id;
        try {
            if (Enunciado != null || Respuesta1 != null || Respuesta2 != null || Respuesta3 != null || Resultado != null) {
                con = cn.getConnection();
                st = (PreparedStatement) con.createStatement();
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Usuario Modificado");
                limpiarTabla(model);

            } else {
                JOptionPane.showMessageDialog(null, "Error...!!!");
            }

        } catch (Exception e) {
        }
    }

    void Eliminar() {
        int fila = tablaDatos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null,"Usuario no Seleccionado");
        } else {
            String sql = "delete from preguntas where codigo=" + id;
            try {
                con = cn.getConnection();
                st = con.prepareStatement(sql);
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Usuario Eliminado");
                limpiarTabla(model);

            } catch (Exception e) {
            }
        }
    }

    void limpiar() {
        txtId.setText("");
        txtPregunta.setText("");
        txtRespuesta1.setText("");
        txtRespuesta2.setText("");
        txtRespuesta3.setText("");
        comboBox.setName("");
    }

    void limpiarTabla(DefaultTableModel model) {
        for (int i = 0; i <= tablaDatos.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }

    }
}
