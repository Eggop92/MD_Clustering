package mineriadatos;

import java.util.LinkedList;

public class Cluster extends Instancia {

	public Cluster(LinkedList<Double> pLinea) {
		super(pLinea, "");
	}

	public Cluster(int nattr) {
		super(nuevoArray(nattr));
		/*for(int i=0; i<nattr; i++){
			linea.add(0.0);
		}*/
	}

	private static String[] nuevoArray(int nattr) {
		String[] rdo = new String[nattr];
		for(int i =0; i<nattr; i++){
			rdo[i]=0.0+"";
		}
		return rdo;
	}

	public void sumaInstancia(Instancia instancia) {
		for(int i=0; i<linea.size(); i++){
			linea.set(i, (linea.get(i)+instancia.getAtributo(i)));
		}
		
	}

	public void hacerMedia(int cont) {
		for(int i=0; i<linea.size(); i++){
			if(cont !=0){
				linea.set(i, (linea.get(i)/cont));
			}else{
				//linea.set(i, 0.0);
			}
			//System.out.print(linea.get(i)+",");
		}
		//System.out.print("\n");
	}



}
