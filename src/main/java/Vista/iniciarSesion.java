package Vista;

import config.Conexion;
import config.MandarMail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class iniciarSesion extends JDialog {
    JPanel contentPane;
    JButton botonCrear;
    JTextField textUsuario;
    JPasswordField textPassword;
    JButton continuarButton;
    Conexion cn = new Conexion();
    Connection con;
    Statement st;

    String correoUsuario = "pgalper400@g.educaand.es";
    String asunto = "Bienvenido a QuizzMaster";
    String mensaje = "Acabas de iniciar sesión en QuizzMaster. Gracias por acceder a nuestro programa";

    public iniciarSesion() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(continuarButton);

        botonCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = textUsuario.getText();
                String password = new String(textPassword.getPassword());
                String sql = "INSERT INTO users (name, password) VALUES (?,?)";
                try {
                    con = cn.getConnection();
                    st = con.prepareStatement(sql);
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, usuario);
                    statement.setString(2, password);
                    int resultSet = statement.executeUpdate();

                        MandarMail mail = new MandarMail();
                        mail.enviarCorreoElectronico(correoUsuario, asunto, mensaje);
                        JOptionPane.showMessageDialog(null, "                  Se ha creado una cuenta para iniciar sesión \nSe ha enviado un correo electrónico por iniciar sesión");
                        dispose();
                        Programa programa = new Programa();
                        programa.pack();
                        programa.setLocationRelativeTo(null);
                        programa.setVisible(true);
                        System.exit(0);

                    statement.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                }
            }
        });

        continuarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = textUsuario.getText();
                String password = new String(textPassword.getPassword());
                String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
                try {
                    con = cn.getConnection();
                    st = con.prepareStatement(sql);
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, usuario);
                    statement.setString(2, password);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        //MandarMail mail = new MandarMail();
                        //mail.enviarCorreoElectronico(correoUsuario, asunto, mensaje);
                        JOptionPane.showMessageDialog(null, "                          Se ha iniciado Sesión \nSe ha enviado un correo electrónico por iniciar sesión");
                        dispose();
                        Programa programa = new Programa();
                        programa.pack();
                        programa.setLocationRelativeTo(null);
                        programa.setVisible(true);
                        System.exit(0);

                    } else {
                        JOptionPane.showMessageDialog(null, "Credenciales incorrectas");
                    }

                    resultSet.close();
                    statement.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                }
            }
        });
    }
}
