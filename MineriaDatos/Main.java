package mineriadatos;

import java.util.GregorianCalendar;
import java.util.LinkedList;

public class Main {
	
	private static String archivo; //ruta del archivo arff
	private static int k; //numero de clusters
	private static int m; //metrica de minkowski
	private static char metodo; //tipo de inicializacion (Aleatorio = a ; division de espacios = d)
	private static char tipoParada; //tipo de parada en convergencia (Iteraciones = i ; disimilitud = d)
	private static float cteParada; //numero de iteraciones o disimilitud entre codebooks
	private static boolean conClase;// c si tiene clases, otra cosa si no lo tiene si no tiene clases

	public static void main(String[] args) {
		GregorianCalendar cal = new GregorianCalendar();
		System.out.println(cal.get(GregorianCalendar.HOUR)+":"+cal.get(GregorianCalendar.MINUTE)+":"+cal.get(GregorianCalendar.SECOND)+":"+cal.get(GregorianCalendar.MILLISECOND));
		if(args.length < 7){
			imprimirError();
		}else{
			try{
				archivo = args[0];
				k = Integer.parseInt(args[1]);
				m = Integer.parseInt(args[2]);
				metodo = args[3].toLowerCase().charAt(0);
				tipoParada = args[4].toLowerCase().charAt(0);
				cteParada = Float.parseFloat(args[5]);
				if(args[6].toLowerCase().charAt(0)=='c'){
					conClase= true;
				}else{
					conClase =false; 
				}
				//clases= args[6].toLowerCase().charAt(0);
				if(k>0 && m>0 && cteParada>0.0 && (metodo=='a' || metodo=='d') && (tipoParada=='i' || tipoParada=='d')){
					ejecutar();
				}
				else{
					imprimirError();
				}
			}catch(NumberFormatException e){
				imprimirError();
			}
			cal = new GregorianCalendar();
			System.out.println(cal.get(GregorianCalendar.HOUR)+":"+cal.get(GregorianCalendar.MINUTE)+":"+cal.get(GregorianCalendar.SECOND)+":"+cal.get(GregorianCalendar.MILLISECOND));
		}
	}
	private static void imprimirError() {
		System.out.println("El programa fue invocado de manera incorrecta, intentalo de nuevo.");
		System.out.println("El programa debe de tener los siguientes parametros:");
		System.out.println("\t String = archivo con instancias.");
		System.out.println("\t integer = numero (positivo) de clusters a tener encuenta.");
		System.out.println("\t integer = numero (positivo) para la metrica a evaluar.");
		System.out.println("\t char = metodo de inicializacion (Aleatorio = a ; division de espacios = d)");
		System.out.println("\t char = metodo de parada de convergencia (Iteraciones = i ; disimilitud = d)");
		System.out.println("\t float = numero de iteraciones o disimilitud");
		System.out.println("\t char = el archivo introducido dispone de clases (c=si ; n=no)");
	}
	
	private static void ejecutar() {
		//Realizamos el preproceso de carga y normalizacion
		LinkedList<Instancia> lI = Preproceso.getMiPreproceso().cargarDatos(archivo);
		lI = Preproceso.getMiPreproceso().normalizar(lI);
		//Dejamos a la clase Clustering que continue todo el proceso
		new Clustering(lI, k, m, metodo, tipoParada, cteParada, conClase);
	}

}
