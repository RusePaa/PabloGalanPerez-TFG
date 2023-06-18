package Vista;

import config.Conexion;
import config.Pregunta;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class comenzarQuizz extends JDialog {
    JPanel contentPane;
    JLabel preguntaLabel;
    JRadioButton opcion1RadioButton;
    JRadioButton opcion2RadioButton;
    JRadioButton opcion3RadioButton;
    private List<Pregunta> preguntas = new ArrayList<>();
    JButton siguienteButton;
    private int preguntaActual = 0;
    private int puntos = 0;
    Statement statement;
    ResultSet resultSet;
    Conexion cn = new Conexion();
    BufferedWriter writer;
    Connection connection;

    public comenzarQuizz() {
        setContentPane(contentPane);
        setModal(true);

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
        siguienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (siguienteButton.getText().equals("Siguiente")) {
                    guardarRespuesta();
                    mostrarSiguientePregunta();
                } else {
                    guardarRespuesta();
                    finalizarCuestionario();
                }
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(opcion1RadioButton);
        buttonGroup.add(opcion2RadioButton);
        buttonGroup.add(opcion3RadioButton);
    }

    public void guardarRespuesta() {
        Pregunta pregunta = preguntas.get(preguntaActual);
        String respuestaUsuario = "";

        if (opcion1RadioButton.isSelected()) {
            respuestaUsuario = pregunta.getRespuesta1();
        } else if (opcion2RadioButton.isSelected()) {
            respuestaUsuario = pregunta.getRespuesta2();
        } else if (opcion3RadioButton.isSelected()) {
            respuestaUsuario = pregunta.getRespuesta3();
        }

        preguntas.get(preguntaActual).setRespuestaUsuario(respuestaUsuario);

        if (respuestaUsuario.equals(pregunta.getRespuestaCorrecta())) {
            puntos++; // Sumar 1 punto si la respuesta es correcta
        }
    }

    public void mostrarSiguientePregunta() {
        if (preguntaActual < preguntas.size() - 1) {
            preguntaActual++;
            mostrarPregunta();
        } else {
            siguienteButton.setText("Finalizar");
        }
    }

    public void finalizarCuestionario() {
        try {
            writer = new BufferedWriter(new FileWriter("resultado.txt"));
            for (Pregunta pregunta : preguntas) {
                writer.write("Pregunta " + pregunta.getCodigo() + ": " + pregunta.getPregunta());
                writer.newLine();
                writer.write("Respuesta 1: " + pregunta.getRespuesta1());
                writer.newLine();
                writer.write("Respuesta 2: " + pregunta.getRespuesta2());
                writer.newLine();
                writer.write("Respuesta 3: " + pregunta.getRespuesta3());
                writer.newLine();
                writer.write("Respuesta Correcta: " + pregunta.getRespuestaCorrecta());
                writer.newLine();
                writer.write("Resultado por Usuario: " + pregunta.getRespuestaUsuario() + "\n");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Puntos obtenidos: " + puntos + " de " + preguntas.size(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void salir() {
        dispose();
    }

    public void iniciarCuestionario() {
        try {
            String sql = "SELECT codigo, pregunta, respuesta1, respuesta2, respuesta3, respuestaCorrecta FROM preguntas";

            connection = cn.getConnection();
            statement = connection.prepareStatement(sql);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int codigo = resultSet.getInt("codigo");
                String pregunta = resultSet.getString("pregunta");
                String respuesta1 = resultSet.getString("respuesta1");
                String respuesta2 = resultSet.getString("respuesta2");
                String respuesta3 = resultSet.getString("respuesta3");
                String respuestaCorrecta = resultSet.getString("respuestaCorrecta");

                preguntas.add(new Pregunta(codigo, pregunta, respuesta1, respuesta2, respuesta3, respuestaCorrecta));
            }
            mostrarPregunta();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarPregunta() {
            Pregunta pregunta = preguntas.get(preguntaActual);
            preguntaLabel.setText(pregunta.getPregunta());
            opcion1RadioButton.setText(pregunta.getRespuesta1());
            opcion2RadioButton.setText(pregunta.getRespuesta2());
            opcion3RadioButton.setText(pregunta.getRespuesta3());

            siguienteButton.setText("Siguiente");

    }
}
