import Vista.iniciarSesion;

public class Main {
    public static void main(String[] args) {
        iniciarSesion dialog = new iniciarSesion();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}