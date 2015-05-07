package mineriadatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import weka.core.Instances;


public class Preproceso {

private static Preproceso miPreproceso;
	
	private Preproceso(){}
	
	public static Preproceso getMiPreproceso(){
		if(miPreproceso == null){
			miPreproceso = new Preproceso();
		}
		return miPreproceso;
	}
	
	public LinkedList<Instancia> cargarDatos(String pArchivo){
		LinkedList<Instancia> rdo = new LinkedList<Instancia>();
		Instances data=null;
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;
	 
	    try {
	       // Apertura del fichero y creacion de BufferedReader para poder
	       // hacer una lectura comoda (disponer del metodo readLine()).
	       archivo = new File (pArchivo);
	       fr = new FileReader (archivo);
	       br = new BufferedReader(fr);
	       
	       // Lectura del fichero
	       String linea;
	       while((linea=br.readLine())!=null){
	    	   linea.replace(';', ',');
	    	   if(linea.length()>0 && !(linea.charAt(0)=='@' || linea.charAt(0)=='%')){
	    		   //System.out.println(linea);
	    		   rdo.add(new Instancia(linea.split(",")));
	    	   }
	       }
	       br.close();
	       fr.close();
	       //para weka
	       archivo = new File (pArchivo);
	       fr = new FileReader (archivo);
	       br = new BufferedReader(fr);
	       data = new Instances(br);
	       OperacionesWeka.cargarListaInstanciasWeka(data);
	       
	       br.close();
	       fr.close();
	    }catch(Exception e){
	       e.printStackTrace();
	    }   
	    
		return rdo;
	}
	
	public LinkedList<Instancia> randomizar(LinkedList<Instancia> pInstancias){
		//Si da problemas revisar que se modifica pInstancias
		LinkedList<Instancia> rdo = new LinkedList<Instancia>();
		int i;
		Random rand = new Random();
		while(!pInstancias.isEmpty()){
			if(pInstancias.size()>1){
				i = rand.nextInt(pInstancias.size()-1);
				rdo.add(pInstancias.remove(i));
			}else{
				rdo.add(pInstancias.remove());
			}
			
		}
		return rdo;
	}
	
	public List<Instancia> devolverParticion(char pTipo, LinkedList<Instancia> pInstancias){ 
		//recibe una T si es para el test
		List<Instancia> rdo = new LinkedList<Instancia>();
		int inicio, fin;
		double tamano =(70.0/100.0);
		if(pTipo == 'T' || pTipo=='t'){
			inicio = ((int) Math.round(pInstancias.size()*tamano));
			fin = pInstancias.size()-1;
			rdo =pInstancias.subList(inicio, fin);
		}else{
			inicio = 0;
			fin = (int) ((pInstancias.size()*tamano)-1);
			rdo = pInstancias.subList(inicio, fin);
		}
		return rdo;
	}
	
	public LinkedList<String> obtenerClases(LinkedList<Instancia> pInstancias){
		LinkedList<String> rdo = new LinkedList<>();
		String clase;
		for(int i=0; i<pInstancias.size(); i++){
			clase = pInstancias.get(i).getClase();
			if(!rdo.contains(clase))
				rdo.add(clase);
		}
		return rdo;
	}
	

	public LinkedList<Instancia> normalizar(LinkedList<Instancia> lI) {
			LinkedList<Double> minimos = new LinkedList<Double>();
			LinkedList<Double> maximos = new LinkedList<Double>();
			for(int i=0; i< lI.get(0).getNumAtributos(); i++){
				minimos.add(Double.MAX_VALUE);
				maximos.add(Double.MIN_VALUE);
			}
			
			LinkedList<Instancia> normalizado = new LinkedList<Instancia>();
			
			for (int i = 0; i < lI.size(); i++) {
				for (int j = 0; j < lI.get(i).getNumAtributos(); j++) {
					double max=maximos.get(j);
						if(max < lI.get(i).getAtributo(j)){
							maximos.set(j, lI.get(i).getAtributo(j));
						}else{
							if(minimos.get(j) > lI.get(i).getAtributo(j)){
								minimos.set(j, lI.get(i).getAtributo(j));
							}
						}
				}
			}
			LinkedList<Double> instN;
			for (int i = 0; i < lI.size(); i++) {
				instN = new LinkedList<Double>();
				for (int j = 0; j < lI.get(i).getNumAtributos()-1; j++) {
					instN.add( (lI.get(i).getAtributo(j)-minimos.get(j))/(maximos.get(j)-minimos.get(j)));
				}
				normalizado.add(new Instancia(instN, lI.get(i).getClase()));
			}
			
			
		
		return normalizado;
	}
	
}
