package wordle;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

public class Interfaz {

    private JFrame frame;
    private JTextField[] filaActual; // Referencia a los 5 casilleros activos de la iteración actual
    private JButton botonJugar;
    private Aplicacion aplicacion;
    private int indicePosicionYDeLetras;
    
    // NOTA ARQUITECTÓNICA: Esta variable de estado debería delegarse a la clase Aplicacion (Modelo).
    private int intentos; 
    private final int MAX_INTENTOS = 6;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Interfaz window = new Interfaz();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Interfaz() {
        indicePosicionYDeLetras = 10;
        intentos = 0;
        aplicacion = new Aplicacion();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 477, 498);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        generarCasillerosLetras(indicePosicionYDeLetras);

        botonJugar = new JButton("JUGAR");
        botonJugar.setBackground(new Color(153, 255, 204));
        botonJugar.setForeground(new Color(0, 0, 0));
        botonJugar.setFont(new Font("Tahoma", Font.PLAIN, 25));
        botonJugar.setBounds(105, 406, 253, 45);
        
        botonJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jugar();
            }
        });
        
        // Listener unificado para capturar el Enter global en el botón
        botonJugar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jugar();
                }
            }
        });

        frame.getContentPane().add(botonJugar);
    }

    private void jugar() {
        // Validación de llenado mediante Early Return
        for (JTextField casillero : filaActual) {
            if (casillero.getText().length() == 0) {
                return; // Corta la ejecución si falta alguna letra
            }
        }

        // Extracción dinámica de los datos de la Vista
        String[] letrasIngresadas = new String[5];
        for (int i = 0; i < 5; i++) {
            letrasIngresadas[i] = filaActual[i].getText();
        }

        // Invocación a la lógica de negocio (Modelo)
        ColorLetra[] resultado = aplicacion.verificar(letrasIngresadas[0], letrasIngresadas[1], 
                                                      letrasIngresadas[2], letrasIngresadas[3], 
                                                      letrasIngresadas[4]);
        cambiarColores(resultado);
        intentos++;

        // Manejo de finalización de estado
        if (aplicacion.getGano()) {
            finalizarJuego("GANASTE!");
        } else if (intentos >= MAX_INTENTOS) {
            finalizarJuego("NO QUEDAN INTENTOS :(");
        } else {
            // Preparación del entorno visual para el siguiente intento
            indicePosicionYDeLetras += 55;
            generarCasillerosLetras(indicePosicionYDeLetras);
            filaActual[0].requestFocus();
        }
    }

    private void cambiarColores(ColorLetra[] resultado) {
        for (int i = 0; i < 5; i++) {
            filaActual[i].setBackground(GetColor(resultado[i]));
        }
    }

    public Color GetColor(ColorLetra colorLetra) {
        if (colorLetra.equals(ColorLetra.GRIS)) {
            return new Color(204, 204, 204);
        } else if (colorLetra.equals(ColorLetra.AMARILLO)) {
            return new Color(255, 255, 153);
        } else {
            return new Color(153, 255, 153);
        }
    }

    private void finalizarJuego(String mensajeTitulo) {
        botonJugar.setEnabled(false);

        JLabel lblTitulo = new JLabel(mensajeTitulo);
        lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTitulo.setVerticalAlignment(SwingConstants.TOP);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(105, 340, 253, 45);
        frame.getContentPane().add(lblTitulo);

        JLabel lblPalabra = new JLabel("La palabra era: " + aplicacion.getPalabra());
        lblPalabra.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPalabra.setHorizontalAlignment(SwingConstants.CENTER);
        lblPalabra.setBounds(99, 368, 264, 29);
        frame.getContentPane().add(lblPalabra);

        frame.revalidate();
        frame.repaint();
    }

    private void generarCasillerosLetras(int y) {
        filaActual = new JTextField[5];
        int coordenadaXInicial = 102;
        int incrementoX = 52; // Distancia calculada en píxeles entre componentes

        for (int i = 0; i < 5; i++) {
            JTextField txtLetra = new JTextField();
            txtLetra.setForeground(Color.BLACK);
            txtLetra.setBackground(Color.WHITE);
            txtLetra.setHorizontalAlignment(SwingConstants.CENTER);
            txtLetra.setColumns(10);
            txtLetra.setFont(new Font("Tahoma", Font.PLAIN, 25));
            txtLetra.setBounds(coordenadaXInicial + (i * incrementoX), y, 45, 45);

            // Variable final requerida para uso interno en la clase anónima KeyAdapter
            final int index = i; 
            
            txtLetra.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (letrasValidas(e)) {
                        txtLetra.setText(String.valueOf(e.getKeyChar()).toUpperCase());
                        txtLetra.setEnabled(false);
                        txtLetra.setDisabledTextColor(Color.BLACK);
                        
                        // Transferencia algorítmica de foco
                        if (index < 4) {
                            filaActual[index + 1].requestFocus();
                        } else {
                            botonJugar.requestFocus();
                        }
                    } else {
                        e.consume(); // Invalida el evento si no cumple el rango
                    }
                }
            });

            filaActual[i] = txtLetra;
            frame.getContentPane().add(txtLetra);
        }
        
        frame.revalidate();
        frame.repaint();
    }

    private boolean letrasValidas(KeyEvent e) {
        return (int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
                || (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64;
    }
}