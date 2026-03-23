package wordle;

import static org.junit.Assert.*;

import org.junit.Test;

public class AplicacionTest {

	@Test
	public void validarColorVerdeLetra1Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("CASAS");
		ColorLetra[] resultado = app.verificar("C", "A", "S", "O", "S");
		assertEquals(ColorLetra.VERDE, resultado[0]);
	}
	@Test
	public void validarColorVerdeLetra2Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("CASAS");
		ColorLetra[] resultado = app.verificar("M", "A", "L", "O", "S");
		assertEquals(ColorLetra.VERDE, resultado[1]);
	}
	@Test
	public void validarColorVerdeLetra3Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("CASAS");
		ColorLetra[] resultado = app.verificar( "C", "A", "S", "A", "R");
		assertEquals(ColorLetra.VERDE, resultado[2]);
	}
	@Test
	public void validarColorVerdeLetra4Test() {
		Aplicacion app = new Aplicacion();
		ColorLetra[] resultado = app.verificar( "A", "S", "A", app.getPalabra().charAt(3) + "","S");
		assertEquals(ColorLetra.VERDE, resultado[3]);
	}
	@Test
	public void validarColorVerdeLetra5Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("CASAS");
		ColorLetra[] resultado = app.verificar( "L", "O", "N", "A", "S");
		assertEquals(ColorLetra.VERDE, resultado[4]);
	}
	
	@Test
	public void validarColorAmarilloLetra1Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ARCOS");
		ColorLetra[] resultado = app.verificar("O", "R", "C", "A", "S");
		assertEquals(ColorLetra.AMARILLO, resultado[0]);
	}	
	@Test
	public void validarColorAmarilloLetra2Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ARCOS");
		ColorLetra[] resultado = app.verificar("A", "S", "A", "D", "O");
		assertEquals(ColorLetra.AMARILLO, resultado[1]);
	}
	@Test
	public void validarColorAmarilloLetra3Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ARCOS");
		ColorLetra[] resultado = app.verificar( "C", "O", "S", "E", "R");
		assertEquals(ColorLetra.AMARILLO, resultado[2]);
	}
	@Test
	public void validarColorAmarilloLetra4Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ARCOS");
		ColorLetra[] resultado = app.verificar( "A", "M", "A", "R","A");
		assertEquals(ColorLetra.AMARILLO, resultado[3]);
	}
	@Test
	public void validarColorAmarilloLetra5Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ARCOS");
		ColorLetra[] resultado = app.verificar( "C", "A", "L", "O", "R");
		assertEquals(ColorLetra.AMARILLO, resultado[4]);
	}

	public void validarColorGrisLetra1Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("PAPEL");
		ColorLetra[] resultado = app.verificar( "X", "A", "P", "E", "L");
		assertEquals(ColorLetra.GRIS, resultado[0]);
	}	
	@Test
	public void validarColorGrisLetra2Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("PAPEL");
		ColorLetra[] resultado = app.verificar( "P", "X", "P", "E", "L");
		assertEquals(ColorLetra.GRIS, resultado[1]);
	}
	@Test
	public void validarColorGrisLetra3Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("PAPEL");
		ColorLetra[] resultado = app.verificar( "P", "A", "X", "E", "L");
		assertEquals(ColorLetra.GRIS, resultado[2]);
	}
	@Test
	public void validarColorGrisLetra4Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("PAPEL");
		ColorLetra[] resultado = app.verificar( "P", "A", "P", "X", "L");
		assertEquals(ColorLetra.GRIS, resultado[3]);
	}
	@Test
	public void validarColorGrisLetra5Test() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("PAPEL");
		ColorLetra[] resultado = app.verificar( "P", "A", "P", "E", "X");
		assertEquals(ColorLetra.GRIS, resultado[4]);
	}
	
	@Test
	public void validarTodosVerdesTest() {
		Aplicacion app = new Aplicacion();
		app.setPalabra("ALGAS");
		ColorLetra[] resultado = app.verificar( "A", "L", "G", "A", "S");
		assertEquals(ColorLetra.VERDE, resultado[0]);
		assertEquals(ColorLetra.VERDE, resultado[1]);
		assertEquals(ColorLetra.VERDE, resultado[2]);
		assertEquals(ColorLetra.VERDE, resultado[3]);
		assertEquals(ColorLetra.VERDE, resultado[4]);
		assertTrue(app.getGano());
	}
}


