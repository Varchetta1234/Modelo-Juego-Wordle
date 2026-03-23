package wordle;


import java.util.Random;


public class Aplicacion {

	private String[] palabras;
	private String palabra;
	private Boolean gano;
	private Diccionario diccionario;

	public Aplicacion() {

		diccionario = new Diccionario();
		palabras = diccionario.obtenerPalabras();
		palabra = buscarPalabra();
		gano = false;

	}

	public String buscarPalabra() {
		Random random = new Random();
		int indice = random.nextInt(0, palabras.length);
		return palabras[indice];
	}

	public ColorLetra[] verificar(String letra1, String letra2, String letra3, String letra4, String letra5) {
		String nuevaPalabra = palabra;
		char[] letras = { letra1.charAt(0), letra2.charAt(0), letra3.charAt(0), letra4.charAt(0), letra5.charAt(0) };
		ColorLetra[] valorLetraLista = { ColorLetra.GRIS, ColorLetra.GRIS, ColorLetra.GRIS, ColorLetra.GRIS,
				ColorLetra.GRIS };

		for (int i = 0; i < letras.length; i++) {
			if (letras[i] == nuevaPalabra.charAt(i)) {
				valorLetraLista[i] = ColorLetra.VERDE;
				String nuevaPalabraAux = nuevaPalabra.substring(0, i) + '_' + nuevaPalabra.substring(i + 1);
				nuevaPalabra = nuevaPalabraAux;
			}
		}

		for (int i = 0; i < letras.length; i++) {
			if (valorLetraLista[i] != ColorLetra.VERDE) {
				boolean esAmarillo = false;
				for (int j = 0; j < letras.length; j++) {
					if (letras[i] == nuevaPalabra.charAt(j) && !esAmarillo) {
						esAmarillo = true;
						valorLetraLista[i] = ColorLetra.AMARILLO;
						String nuevaPalabraAux = nuevaPalabra.substring(0, j) + "_" + nuevaPalabra.substring(j + 1);
						nuevaPalabra = nuevaPalabraAux;
					}
				}
			}
		}

		if (valorLetraLista[0] == ColorLetra.VERDE && valorLetraLista[1] == ColorLetra.VERDE
				&& valorLetraLista[2] == ColorLetra.VERDE && valorLetraLista[3] == ColorLetra.VERDE
				&& valorLetraLista[4] == ColorLetra.VERDE) {
			gano = true;
		}

		return valorLetraLista;
	}

	public Boolean getGano() {
		return gano;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}
	
	

}
