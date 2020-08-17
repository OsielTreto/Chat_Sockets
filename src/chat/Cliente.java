package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente extends JFrame {

    JLabel fondopan, nomusuario, iconoimg;

    JFrame ventana;
    JButton enviar;
    JTextField escribirmensaje;
    JTextArea areamensajes;
    JPanel panelchat, panelescribir;
    JPanel panelbotones;
    JScrollPane scrolluno, scrolldos;
    Socket socket;
    BufferedReader lector;
    PrintWriter escritor;
    String nombre = "Osiel";//JOptionPane.showInputDialog(null, "Ingrese nombre de usuario");

    public Cliente() {
        hacerinterfaz();
    }

    public void hacerinterfaz() {
        nomusuario = new JLabel();
        fondopan = new JLabel();
        iconoimg = new JLabel();
        nomusuario.setText(nombre);
        nomusuario.setFont(new Font("Calibri (Cuerpo)", 0, 30));
        nomusuario.setForeground(Color.WHITE);
        ventana = new JFrame("Whatsapp Beta 3.0");
        ventana.setResizable(false);
        enviar = new JButton();

        enviar.setIcon(new ImageIcon(getClass().getResource("/img/icono.png")));
        escribirmensaje = new JTextField();

        areamensajes = new JTextArea(25, 12);
        areamensajes.setBackground(Color.WHITE);
        scrolluno = new JScrollPane(areamensajes);
        scrolldos = new JScrollPane(escribirmensaje);
        areamensajes.setFont(new Font("Calibri (Cuerpo)", 0, 15));

        panelescribir = new JPanel();
        panelescribir.setLayout(new GridLayout(1, 1));
        panelescribir.add(scrolldos);

        panelchat = new JPanel();
        panelchat.setLayout(new GridLayout(1, 1));
        panelchat.add(scrolluno);

        panelbotones = new JPanel();
        panelbotones.setLayout(new GridLayout(2, 2));
        //panelbotones.add(escribirmensaje);
        //panelbotones.add(enviar);

        ventana.setSize(900, 683);
        fondopan.setIcon(new ImageIcon(getClass().getResource("/img/fondofinal.png")));
        fondopan.setBounds(0, 0, 900, 698);
        iconoimg.setIcon(new ImageIcon(getClass().getResource("/img/osiel.png")));

        iconoimg.setBounds(40, 10, 60, 60);
        panelescribir.setBounds(20, 585, 775, 50);
        enviar.setBounds(795, 585, 100, 50);
        enviar.setBackground(new Color(47, 74, 101));
        nomusuario.setBounds(110, 25, 100, 30);
        panelchat.setBounds(20, 100, 850, 460);

        ventana.add(panelchat);
        ventana.add(panelescribir);
        ventana.add(enviar);
        ventana.add(nomusuario);
        ventana.add(iconoimg);
        ventana.add(fondopan);
        //ventana.setLayout(new BorderLayout());
        //ventana.add(panelchat, BorderLayout.NORTH);
        //ventana.add(panelbotones, BorderLayout.SOUTH);
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread principal = new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket("localhost", 9000);
                    leer();
                    escribir();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        principal.start();
    }

    public void leer() {
        Thread leer_hilo = new Thread(new Runnable() {
            public void run() {
                try {
                    lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        String mensaje_recibido = lector.readLine();
                        areamensajes.append("Osiel: " + mensaje_recibido + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        leer_hilo.start();
    }

    public void escribir() {
        Thread escribir_hilo = new Thread(new Runnable() {
            public void run() {
                try {
                    escritor = new PrintWriter(socket.getOutputStream(), true);
                    enviar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (escribirmensaje.getText().equalsIgnoreCase("")) {

                            } else {
                                String enviar_mensaje = escribirmensaje.getText();
                                areamensajes.append("Miguel: " + enviar_mensaje + "\n");

                                escritor.println(enviar_mensaje);
                                escribirmensaje.setText("");
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
