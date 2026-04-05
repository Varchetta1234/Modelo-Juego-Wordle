//de 305 lineas a 200 lineas codigo de compañera
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
//package wordle;
//
//import java.awt.EventQueue;
//
//import javax.swing.JFrame;
//import javax.swing.JTextField;
//import javax.swing.JLabel;
//
//
//import java.awt.Font;
//import javax.swing.SwingConstants;
//
//import java.awt.Color;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import javax.swing.JButton;
//
//public class Interfaz {
//
//	private JFrame frame;
//	private JTextField letra1;
//	private JTextField letra2;
//	private JTextField letra3;
//	private JTextField letra4;
//	private JTextField letra5;
//	private JButton botonJugar;
//	private Aplicacion aplicacion;
//	private int indicePosicionYDeLetras;
//	private int intentos;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) { // Método de entrada principal. Arranca la aplicación en el hilo de eventos de Swing.
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Interfaz window = new Interfaz();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public Interfaz() { // Constructor de la clase. Establece los valores iniciales del juego y llama al inicializador de la vista.
//		indicePosicionYDeLetras = 10;
//		intentos = 0;
//		aplicacion = new Aplicacion();
//		initialize();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() { // Método que configura la ventana (JFrame), genera la primera fila de cuadraditos y configura el botón Jugar.
//		frame = new JFrame();
//		frame.setBounds(100, 100, 477, 498);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(null); // Desactiva el gestor de diseño para permitir posicionamiento absoluto (x, y).
//
//		generarCasillerosLetras(indicePosicionYDeLetras);
//
//		botonJugar = new JButton("JUGAR");
//		botonJugar.setBackground(new Color(153, 255, 204));
//
//		botonJugar.setForeground(new Color(0, 0, 0));
//		botonJugar.addKeyListener(new KeyListener() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//			}
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					jugar();
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//			}
//		});
//
//		botonJugar.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				jugar();
//			}
//		});
//		botonJugar.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		botonJugar.setBounds(105, 406, 253, 45); // Método fundamental: Ubica el botón en una posición fija de la ventana.
//		frame.getContentPane().add(botonJugar);
//
//	}
//
//	private void jugar() { // Método orquestador del juego. Valida las letras, cambia colores, revisa si ganaste/perdiste y genera nuevas filas.
//		if (letra1.getText().length() == 1 && letra2.getText().length() == 1 && letra3.getText().length() == 1
//				&& letra4.getText().length() == 1 && letra5.getText().length() == 1) { // Verifica que todas las casillas tengan una letra.
//			ColorLetra[] resultado = aplicacion.verificar(letra1.getText(), letra2.getText(), letra3.getText(),
//					letra4.getText(), letra5.getText());
//			cambiarColores(resultado);
//			intentos += 1;
//			if (aplicacion.getGano()) {
//				botonJugar.setEnabled(false); // Método fundamental: Deshabilita el botón jugar porque el juego terminó.
//				mostrarGanador(aplicacion.getPalabra());
//
//			} else if (intentos == 6) {
//				botonJugar.setEnabled(false);
//				mostrarPerdedor(aplicacion.getPalabra());
//
//			} else {
//				indicePosicionYDeLetras += 55; // Incrementa la posición Y para dibujar la siguiente fila de casilleros más abajo.
//				generarCasillerosLetras(indicePosicionYDeLetras);
//				letra1.requestFocus(); // Método fundamental: Mueve el cursor automáticamente a la primera letra de la nueva fila.
//			}
//		}
//	}
//
//	private void cambiarColores(ColorLetra[] resultado) { // Toma el resultado de la validación y aplica los colores a los JTextField actuales.
//		letra1.setBackground(GetColor(resultado[0]));
//		letra2.setBackground(GetColor(resultado[1]));
//		letra3.setBackground(GetColor(resultado[2]));
//		letra4.setBackground(GetColor(resultado[3]));
//		letra5.setBackground(GetColor(resultado[4]));
//	}
//
//	public Color GetColor(ColorLetra colorLetra) { // Método traductor. Convierte el enum de la lógica del negocio a un objeto Color visual de Java.
//		if (colorLetra.equals(ColorLetra.GRIS)) {
//			return new Color(204, 204, 204);
//		} else if (colorLetra.equals(ColorLetra.AMARILLO)) {
//			return new Color(255, 255, 153);
//		} else {
//			return new Color(153, 255, 153);
//		}
//	}
//
//	private void mostrarGanador(String palabra) { // Crea e inyecta las etiquetas (JLabel) de victoria en la parte inferior de la ventana.
//		JLabel lblNewLabel = new JLabel("GANASTE!");
//		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
//		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		lblNewLabel.setBounds(105, 340, 253, 45);
//		frame.getContentPane().add(lblNewLabel);
//
//		JLabel lblNewLabel_1 = new JLabel("La palabra era: " + palabra);
//		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
//		lblNewLabel_1.setBounds(99, 368, 264, 29);
//		frame.getContentPane().add(lblNewLabel_1);
//		frame.revalidate(); // Método fundamental: Le avisa a la ventana que se agregaron nuevos JLabel y debe recalcularse.
//		frame.repaint(); // Método fundamental: Obliga a redibujar la ventana para que los mensajes de victoria aparezcan en pantalla.
//	}
//
//	private void mostrarPerdedor(String palabra) { // Crea e inyecta las etiquetas (JLabel) de derrota en la parte inferior de la ventana.
//		JLabel lblNewLabel = new JLabel("NO QUEDAN INTENTOS :(");
//		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
//		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		lblNewLabel.setBounds(105, 340, 253, 45);
//		frame.getContentPane().add(lblNewLabel);
//
//		JLabel lblNewLabel_1 = new JLabel("La palabra era: " + palabra);
//		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
//		lblNewLabel_1.setBounds(99, 368, 264, 29);
//		frame.getContentPane().add(lblNewLabel_1);
//		frame.revalidate(); // Método fundamental: Recalcula la jerarquía de componentes tras añadir los mensajes de pérdida.
//		frame.repaint(); // Método fundamental: Refresca el lienzo para mostrar los componentes recién añadidos.
//	}
//
//	private void generarCasillerosLetras(int y) { // Crea, configura y ubica en pantalla una nueva fila de 5 JTextFields en la coordenada Y indicada.
//		letra1 = new JTextField();
//		letra1.setForeground(Color.BLACK);
//		letra1.setBackground(Color.WHITE);
//		letra1.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//				if ((int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
//						|| (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64) {
//					letra1.setText(String.valueOf(e.getKeyChar()).toUpperCase());
//					letra2.requestFocus(); // Método fundamental: Transfiere automáticamente el cursor a la siguiente casilla (letra2).
//					letra1.setEnabled(false); // Método fundamental: Bloquea esta casilla para que no se pueda borrar o cambiar la letra.
//					letra1.setDisabledTextColor(Color.BLACK);
//				} else {
//					e.consume(); // Método fundamental: Destruye el evento si se presiona un número o símbolo, evitando que se escriba en el JTextField.
//				}
//			}
//		});
//		letra1.setHorizontalAlignment(SwingConstants.CENTER);
//		letra1.setColumns(10);
//		letra1.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		letra1.setBounds(102, y, 45, 45); // Método fundamental: Coloca físicamente el JTextField en la ventana usando la variable dinámica 'y'.
//
//		frame.getContentPane().add(letra1);
//
//		letra2 = new JTextField();
//		letra2.setForeground(Color.BLACK);
//		letra2.setBackground(Color.WHITE);
//		letra2.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//				if ((int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
//						|| (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64) {
//					letra2.setText(String.valueOf(e.getKeyChar()).toUpperCase());
//					letra3.requestFocus(); // Método fundamental: Pasa el foco al casillero letra3.
//					letra2.setEnabled(false); // Método fundamental: Bloquea el componente actual.
//					letra2.setDisabledTextColor(Color.BLACK);
//				} else {
//					e.consume(); // Método fundamental: Evita ingresos no válidos.
//				}
//			}
//		});
//		letra2.setHorizontalAlignment(SwingConstants.CENTER);
//		letra2.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		letra2.setColumns(10);
//		letra2.setBounds(154, y, 45, 45);
//		frame.getContentPane().add(letra2);
//
//		letra3 = new JTextField();
//		letra3.setForeground(Color.BLACK);
//		letra3.setBackground(Color.WHITE);
//		letra3.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//				if ((int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
//						|| (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64) {
//					letra3.setText(String.valueOf(e.getKeyChar()).toUpperCase());
//					letra4.requestFocus(); // Método fundamental: Pasa el foco al casillero letra4.
//					letra3.setEnabled(false); // Método fundamental: Bloquea el componente actual.
//					letra3.setDisabledTextColor(Color.BLACK);
//				} else {
//					e.consume(); // Método fundamental: Evita ingresos no válidos.
//				}
//
//			}
//		});
//
//		letra3.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		letra3.setHorizontalAlignment(SwingConstants.CENTER);
//		letra3.setColumns(10);
//		letra3.setBounds(206, y, 45, 45);
//		frame.getContentPane().add(letra3);
//
//		letra4 = new JTextField();
//		letra4.setForeground(Color.BLACK);
//		letra4.setBackground(Color.WHITE);
//		letra4.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//				if ((int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
//						|| (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64) {
//					letra4.setText(String.valueOf(e.getKeyChar()).toUpperCase());
//					letra5.requestFocus(); // Método fundamental: Pasa el foco al último casillero (letra5).
//					letra4.setEnabled(false); // Método fundamental: Bloquea el componente actual.
//					letra4.setDisabledTextColor(Color.BLACK);
//				} else {
//					e.consume(); // Método fundamental: Evita ingresos no válidos.
//				}
//			}
//		});
//		letra4.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		letra4.setHorizontalAlignment(SwingConstants.CENTER);
//		letra4.setColumns(10);
//		letra4.setBounds(258, y, 45, 45);
//		frame.getContentPane().add(letra4);
//
//		letra5 = new JTextField();
//		letra5.setForeground(Color.BLACK);
//		letra5.setBackground(Color.WHITE);
//		letra5.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//				if ((int) e.getKeyChar() < 123 && (int) e.getKeyChar() > 96
//						|| (int) e.getKeyChar() < 91 && (int) e.getKeyChar() > 64) {
//					letra5.setText(String.valueOf(e.getKeyChar()).toUpperCase());
//					// Ya no hay requestFocus() aquí porque es la última letra de la fila. El usuario ahora debe apretar "JUGAR" o Enter.
//					letra5.setEnabled(false); // Método fundamental: Bloquea el último componente.
//					letra5.setDisabledTextColor(Color.BLACK);
//				} else {
//					e.consume(); // Método fundamental: Evita ingresos no válidos.
//				}
//			}
//		});
//		letra5.setFont(new Font("Tahoma", Font.PLAIN, 25));
//		letra5.setHorizontalAlignment(SwingConstants.CENTER);
//		letra5.setColumns(10);
//		letra5.setBounds(310, y, 45, 45);
//		frame.getContentPane().add(letra5);
//		
//		frame.revalidate(); // Método fundamental: Avisa a Swing que la nueva fila de JTextFields fue agregada a la estructura.
//		frame.repaint(); // Método fundamental: Pinta los nuevos 5 cuadraditos en pantalla.
//	}
//}