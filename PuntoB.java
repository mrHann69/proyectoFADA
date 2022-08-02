import java.io.*;
import java.util.Iterator;
import java.util.Random;

public class PuntoB {
	/*
	 * Nombres de los archivos de lectura y escritura, modifique como considere.
	 */
	static String ARCHIVO_LECTURA = "inB";
	static String ARCHIVO_ESCRITURA = "outB";

	/*
	 * Método para realizar la lectura del problema, no modificar.
	 */
	public static Entrada input() {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		Entrada input = null;
		try {
			archivo = new File(ARCHIVO_LECTURA + ".txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			linea = br.readLine();
			String[] data = linea.split(" ");
			int n = Integer.valueOf(data[0]);
			int m = Integer.valueOf(data[1]);
			Libro[] libros = new Libro[m];
			for (int i = 0; i < m; ++i) {
				linea = br.readLine();
				data = linea.split(" ");
				libros[i] = new Libro(data[0], Integer.valueOf(data[1]));
			}
			input = new Entrada(n, m, libros);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != fr) {
				fr.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return input;
	}

	/*
	 * Método para realizar la escritura de la respuesta del problema, no modificar.
	 */
	public static void output(Respuesta output) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(ARCHIVO_ESCRITURA + ".txt");
			pw = new PrintWriter(fichero);
			pw.println(output.tiempoTotal);
			for (int i = 0; i < output.libroQueEmpieza.length; ++i) {
				pw.println(output.libroQueEmpieza[i] + " - " + output.libroQueTermina[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != fichero)
				fichero.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	/*
	 * Implementar el algoritmo y devolver un objeto de tipo Respuesta, el cual
	 * servirá para imprimir la solución al problema como se requiere en el
	 * enunciado.
	 */
	/*
	 * [8,10,4, 5,12,8, 4,6,14] 0 1 2 3 4 5 6 7 8 tiempoTotal = 25 libroQueEmpieza =
	 * [0, 3, 6] libroQueTermina = [2, 5, 8]
	 */
	/* Libro= {nombre, paginas} */

	static Respuesta solve(int n_escribas, int m_libros, Libro[] libros) {
		int tiempoTotal = 0;
		String[] libroEmpieza = new String[n_escribas];
		String[] libroTermina = new String[n_escribas];
		int[] numLibrosXescriba = new int[n_escribas];
		int[] arrayMaximos = new int[n_escribas];
		for (int i : arrayMaximos)
			arrayMaximos[i] = 0;

		/* igual cantidad de escribas que de libros, se reparte y se saca el maximo */
		if (n_escribas == m_libros) {
			int maximo = libros[0].paginas;
			int i = 0;
			for (Libro l : libros) {
				if (l.paginas > maximo)
					maximo = l.paginas;
				libroEmpieza[i] = l.nombre;
				libroTermina[i] = l.nombre;
				++i;
			}
			tiempoTotal = maximo;
		}
		/*
		 * si el numero de libros es mayor que el numero de escribas dividir el numero
		 * de libros entre el numero de escribas, sacarle el valor absoluto para obtener
		 * el numero de posiciones, luego hacer la diferencia entre el numero de libros
		 * y numero de escribas, hacer un funcion random en el rango de escribas y
		 * sumarle la diferencia al escriba resultado de la funcion random en el array
		 * de cantidades de libros por escriba, luego, por cada valor en el arreglo de
		 * cantidades de libro por escriba, sumar esas paginas de libros el arreglo de
		 * maximos
		 */
		/*
		 * SOLUCION FACTIBLE → entrega una solucion al problema de asignación (solucion
		 * no optima en todos los casos)
		 */
		if (m_libros > n_escribas) {
			int numParticiones = (int) Math.abs(m_libros / n_escribas);
			int diferenciaLibrosEscribas = m_libros - n_escribas;
			int rand = obtenerEscribaRandom(n_escribas);
			/*
			 * si la multiplicacion es menor significa que las particiones son desiguales y
			 * hay que sumar la diferencia a un escriba random
			 */
			for (int i = 0; i < n_escribas; ++i)
				numLibrosXescriba[i] = numParticiones; // llenar con # particiones
			
			if ((numParticiones * n_escribas) < m_libros)
				numLibrosXescriba[rand] += diferenciaLibrosEscribas; // escojo escriba al azar y sumo diferencia

			int inicio = 0, fin = 0, aux=0;
			for (int i = 0; i < numLibrosXescriba.length; i++) {
				fin += numLibrosXescriba[i]-1; //indice fin avanza sumando en cada iteracion de escriba
				aux += numLibrosXescriba[i];
				for (int j = inicio; j <= fin; j++) {
					if(numLibrosXescriba[i]==1) {
						inicio = j;
						fin = j;
						libroEmpieza[i]=libros[inicio].nombre;
						libroTermina[i]=libros[fin].nombre;
					}
					arrayMaximos[i] += libros[j].paginas;

					inicio = fin;
				}
				//System.out.println("empieza: "+libroEmpieza[i]+" termina: "+libroTermina[i]);
			}
			
			tiempoTotal = arrayMaximos[0];
			for (int i = 0; i < arrayMaximos.length; ++i) {
				if(tiempoTotal<arrayMaximos[i]) tiempoTotal = arrayMaximos[i];
				System.out.println("maximos: "+arrayMaximos[i]+" librosXescriba: "+numLibrosXescriba[i]);
			} 
			System.out.println("tiempoMaximo : "+tiempoTotal);
		}

		return new Respuesta(tiempoTotal, libroEmpieza, libroTermina);
	}

	static int obtenerEscribaRandom(int n_escriba) {
		Random rand = new Random();
		return rand.nextInt(n_escriba);
	}

	public static void main(String[] args) {
		Entrada input = input();
		Respuesta r = solve(input.n, input.m, input.libros);
		output(r);
	}

	static class Entrada {
		int n; // numero escribas
		int m; // numero libros
		Libro[] libros;

		public Entrada(int n, int m, Libro[] libros) {
			this.n = n;
			this.m = m;
			this.libros = libros;
		}

	}

	static class Respuesta {
		int tiempoTotal;
		/*
		 * Esta variable contiene en su posición i, el nombre del libro por el que
		 * empieza el i-ésimo escritor.
		 */
		String[] libroQueEmpieza;
		/*
		 * Esta variable contiene en su posición i, el nombre del libro por el que
		 * termina el i-ésimo escritor.
		 */
		String[] libroQueTermina;

		public Respuesta(int tiempoTotal, String[] libroQueEmpieza, String[] libroQueTermina) {
			this.tiempoTotal = tiempoTotal;
			this.libroQueEmpieza = libroQueEmpieza;
			this.libroQueTermina = libroQueTermina;
		}

	}

	/*
	 * Clase base para interpretar los objetos tratados en el problema.
	 */
	static class Libro {
		String nombre;
		int paginas;

		public Libro(String nombre, int paginas) {
			this.nombre = nombre;
			this.paginas = paginas;
		}
	}

}
