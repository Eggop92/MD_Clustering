package mineriadatos;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class Estadisticas {

	private String positivo;
	private static Estadisticas misEstadisitcas;
	private int TP;
	private int FP;
	private int TN;
	private int FN;
	
	private HashMap<String, LinkedList<Integer>>  matrizConfusionGeneral;
	private LinkedList<String> listaClases;
	

	private Estadisticas(){
		TP=0;
		FP=0;
		TN=0;
		FN=0;
	}

	public static Estadisticas getEstadisiticas(){
		if(misEstadisitcas  == null){
			misEstadisitcas = new Estadisticas();
		}
		return misEstadisitcas;
	}

	public void setPositivoYRestablecer(String pPositivo){
		positivo = pPositivo;
		TP=0;
		FP=0;
		TN=0;
		FN=0;
		matrizConfusionGeneral= new HashMap<String, LinkedList<Integer>>();
	}
	
	public void comparar(String real, String predicho){
		
		int valor= matrizConfusionGeneral.get(real).get(listaClases.indexOf(predicho))+1;
		LinkedList<Integer> datos= matrizConfusionGeneral.remove(real);
		datos.set(listaClases.indexOf(predicho), valor);
		matrizConfusionGeneral.put(real, datos);
		
		// aqui se va a comparar por cada clase, ya q en cada vuelta sera positivo una diferente
		if(positivo.equals(real)){
			if (real.equals(predicho)){
				TP++;
			}
			else{
				FN++;
			}
		}else{
			if (real.equals(predicho)){
				TN++;
			}
			else{
				FP++;
			}	
		}
		
	}
	
	public int getTP(){
		return TP;
	}

	public int getFP(){
		return FP;
	}

	public int getTN(){
		return TN;
	}

	public int getFN(){
		return FN;
	}

	public double getAccuracy(){
		double numerador, denominador, resul=0;
		numerador=((double)getTP()+(double)getTN());
		denominador=((double)getTP()+(double)getTN()+(double)getFP()+(double)getFN());
		if (!(denominador<=0)){
			resul=numerador/denominador;
		}
		return resul;		
	}

	public double getSpecificity(){
		double numerador, denominador, resul=0;
		numerador=(double)getTN();
		denominador=(double)getFP()+(double)getTN();
		if (!(denominador<=0)){
			resul=numerador/denominador;
		}
		return resul;

	}
	
	public double getSensitivity(){
		double numerador, denominador, resul=0;
		numerador=(double)getTP();
		denominador=(double)getTP()+(double)getFN();
		if (!(denominador<=0)){
			resul=numerador/denominador;
		}
		return resul;
	}

	public double getRecall(){
		double numerador, denominador, resul=0;
		numerador=(double)getTP();
		denominador=(double)getTP()+(double)getFN();
		if (!(denominador<=0)){
			resul=numerador/denominador;
		}
		return resul;
	}
	
	public void imprimirFigurasDeMerito(){
		
		DecimalFormat formateador = new DecimalFormat("####.##");
		//TP, FP, FN, TN, acuracy, sensitivity, specifity, recall, positivo
		System.out.println(formateador.format(getTP())+",\t "+formateador.format(getFP())+",\t "+formateador.format(getFN())+",\t "+formateador.format(getTN())+",\t "+formateador.format(getAccuracy())+",\t "+formateador.format(getSensitivity())+",\t "+formateador.format(getSpecificity())+",\t "+formateador.format(getRecall())+",\t "+positivo);

	}

	//inicializao a 0 el array de integer para luego poder ir sumando en cada posicion que le corresponda segun la clase q sea
	public void inicializarMatrizDeConfusionGeneral(LinkedList<String> pClases){
		 listaClases=pClases;
		 for (int i = 0; i < listaClases.size(); i++) {
			 LinkedList<Integer> aux = new  LinkedList<Integer>();
			 for(int j=0; j<listaClases.size();j++){
					aux.add(0);
			}
			matrizConfusionGeneral.put(listaClases.get(i),aux);
		}
	}
		
	
	public void imprimirMatrizDeConfusionGeneral(){
			
		 DecimalFormat formateador = new DecimalFormat("####.##");
		 String abecedario= "abcdefghijklmn�opqrstuvwxyz";
		 String cabeceraEstimado="";
		 for (int i = 0; i < listaClases.size(); i++) {
			//cabeceraEstimado=cabeceraEstimado+listaClases.get(i)+",\t ";
			 cabeceraEstimado=cabeceraEstimado+abecedario.charAt(i)+",\t ";
		 }
		 System.out.println(cabeceraEstimado+" <-- Estimados");
		 
		 //Iterator<Entry<String, LinkedList<Integer>>> itr = matrizConfusionGeneral.entrySet().iterator();
		 int k=0;
		 for (int j = 0; j < listaClases.size(); j++) {
			 LinkedList<Integer> aux= matrizConfusionGeneral.get(listaClases.get(j));
			 String salida="";
				for (int i = 0; i < aux.size(); i++) {
					salida=salida+formateador.format(aux.get(i))+",\t ";
				}
				salida = salida+abecedario.charAt(k)+" <-- "+listaClases.get(j);
				System.out.println(salida);k++;
			 }
		/* while (itr.hasNext()) {
			Map.Entry e = (Map.Entry)itr.next();
			LinkedList<Integer> aux=(LinkedList<Integer>) e.getValue();
			String salida="";
			for (int i = 0; i < aux.size(); i++) {
				salida=salida+formateador.format(aux.get(i))+",\t ";
			}
			salida = salida+abecedario.charAt(j)+" <-- "+(String) e.getKey();
			System.out.println(salida);j++;
		}*/
	}

}
