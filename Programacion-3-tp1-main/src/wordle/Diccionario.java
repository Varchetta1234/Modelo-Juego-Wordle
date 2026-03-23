package wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Diccionario {
	private String rutaTexto;

	public Diccionario() {
		rutaTexto = System.getProperty("user.dir") + "\\src\\wordle\\Palabras.txt";
	}

	public String[] obtenerPalabras() {
		List<String> palabras = new ArrayList<>();
		File file = new File(rutaTexto);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null) {
				palabras.add(st);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return palabras.stream().toArray(String[]::new);
	}
}
