package mineriadatos;

import java.util.LinkedList;
import java.util.Random;

public class Espacio {
	
	private LinkedList<Rango> espacio;
	
	public Espacio(LinkedList<Rango> pE){
		espacio = pE;
	}

	public Espacio[] subDividir() {
		Espacio[] rdo = new Espacio[2];
		Random r = new Random();
		int numRandom = r.nextInt(espacio.size());
		LinkedList<Rango> sub1 = new LinkedList<Rango>();
		LinkedList<Rango> sub2 = new LinkedList<Rango>();
		Rango rOriginal;
		for(int i=0; i<espacio.size(); i++){
			rOriginal = espacio.get(i);
			if(i==numRandom){
				sub1.add(new Rango(rOriginal.getMax(), (rOriginal.getMax()+rOriginal.getMin()/2)));
				sub2.add(new Rango((rOriginal.getMax()+rOriginal.getMin()/2), rOriginal.getMin()));
			}else{
				sub1.add(new Rango(rOriginal.getMax(), rOriginal.getMin()));
				sub2.add(new Rango(rOriginal.getMax(), rOriginal.getMin()));
			}
		}
		rdo[0] = new Espacio(sub1);
		rdo[1] = new Espacio(sub2);
		return rdo;
	}

	public Cluster obtenerCentroide() {
		LinkedList<Double> puntosMedios = new LinkedList<Double>();
		for(int i=0; i<espacio.size(); i++){
			puntosMedios.add(i, espacio.get(i).media()) ;
		}
		return new Cluster(puntosMedios);
	}

}
