package mineriadatos;

import java.util.LinkedList;

public class Instancia {
	
	protected LinkedList<Double> linea;
	protected String clase=null;

	public Instancia(String[] pLinea){
		linea = new LinkedList<Double>();
		for(int i =0; i<pLinea.length; i++){
			try{
				linea.add(Double.parseDouble(pLinea[i])); 
			}catch(NumberFormatException e){}
		}
		clase = pLinea[pLinea.length-1];
	}
	
	public Instancia(LinkedList<Double> pLinea, String pClase){
		linea = pLinea;
		clase = pClase;
	}

	public String getClase(){
		return clase;
	}
	
	public void setClase(String pClase){
		clase= pClase;
	}

	public double getAtributo(int attr){
		Double rdo = null;
		if(attr >=0 && attr <= getNumAtributos()){
			rdo = linea.get(attr);
		}
		return rdo;
	}
	
	
	public void setAtributo(int attr, double valor){
		if(attr >=0 && attr <= getNumAtributos()){
			linea.set(attr, valor);
		}

	}
	

	public int getNumAtributos(){
		return linea.size();
	}

	public LinkedList<Double> getInstancia() {
		return linea;
	}


}
