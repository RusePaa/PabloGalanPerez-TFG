package config;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MandarMail {
    public void enviarCorreoElectronico(String destinatario, String asunto, String mensaje) {
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", true);
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.starttls.enable", true);

        String remitente = "soportequizzmaster@gmail.com";
        String password = "gdemdebcrzmcrhys";

        Session session = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            Message correo = new MimeMessage(session);
            correo.setFrom(new InternetAddress(remitente));
            correo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            correo.setSubject(asunto);
            correo.setText(mensaje);
            Transport.send(correo);
        } catch (MessagingException e) {
            System.out.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }
}
