package mineriadatos;


import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
public class Clustering {

	private LinkedList<Instancia> listaInstancias;
	private LinkedList<Cluster> listaClusters;
	private int numIteraciones;
	
	private int k; //numero de clusters
	private int m; //metrica de minkowski
	private char metodo; //tipo de inicializacion (Aleatorio = a ; division de espacios = d)
	private char tipoParada; //tipo de parada en convergencia (Iteraciones = i ; disimilitud = d)
	private float cteParada; //numero de iteraciones o disimilitud entre codebooks
	private boolean clases;//true si el fichero tiene clases y false en el caso contrario
	
	public Clustering(LinkedList<Instancia> pListaInstancias, int pK, int pM, char pMetodo, char pTipoParada, float pCteParada, boolean conClase){
		listaInstancias = pListaInstancias;
		numIteraciones = 0;
		
		k= pK;
		m= pM;
		metodo = pMetodo;
		tipoParada =pTipoParada;
		cteParada = pCteParada;
		clases= conClase;
				
		crearCentroides();
		ajustarCentroides();
		
		//aqui se ejecuta todo lo de weka
		try {
			OperacionesWeka.getMisOperacionesWeka().calcularClustersWeka(k,clases);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//realizarMapeo();
		
		HashMap<String, LinkedList<Integer>> clasesClusters = new HashMap<String, LinkedList<Integer>>();
		
		if (clases){
			clasesClusters = asignarClasesAClusters(listaInstancias,hallarBitsPertenencia());
		}else{
			/**En el caso de que los datos no tengan clases, uso la lista de instancias de weka que tendran
			 * como clase el cluster que nos ofrece weka
			 */
			clasesClusters = asignarClasesAClusters(OperacionesWeka.getMisOperacionesWeka().getlistaInstanciasWeka(),hallarBitsPertenencia());
		}
		
		imprimirHasMap(clasesClusters);
		imprimirAsignacion(clasesClusters);
		
	}
	

	private void crearCentroides(){
		listaClusters = new LinkedList<Cluster>();
		//System.out.println("creamos centroides");
		if(metodo=='a'){
			//System.out.println("por el metodo aleatorio");
			//generamos los centroides escogiendo instancias aleatoriamente
			LinkedList<Integer> sacados = new LinkedList<Integer>();
			Random r = new Random();
			int j;
			while(sacados.size()<k){
				j = r.nextInt(listaInstancias.size());
				if(!sacados.contains(j)){
					listaClusters.add(new Cluster(listaInstancias.get(j).getInstancia()));
					sacados.add(j);
				}
				//System.out.println("cluster numero: "+i);
			}
		}else{
			//generamos los centroides dividiendo el espacio, particionandolo y obteniendo los puntos medios
			//generamos una cola con un espacio que abarque todo suponiendo que esta normalizado a 0-1.
			Queue<Espacio> colaEspacios = new LinkedList<Espacio>();
			LinkedList<Rango> lR= new LinkedList<Rango>();
			for(int i=0; i<listaInstancias.get(0).getNumAtributos(); i++){
				lR.add(new Rango(1, 0));
			}
			colaEspacios.add(new Espacio(lR));
			
			//dividimos los espacios en dos subEspacios que incluimos en la cola
			//hasta tener los que necesitamos
			Espacio e;
			while(colaEspacios.size()<k){
				e= colaEspacios.remove();
				Espacio[] subEspacios= e.subDividir();
				colaEspacios.add(subEspacios[0]);
				colaEspacios.add(subEspacios[1]);
			}
			//por cada espacio en la cola, obtenemos el punto medio, que sera el centroide para el cluster
			while(!colaEspacios.isEmpty()){
				e = colaEspacios.remove();
				listaClusters.add(e.obtenerCentroide());
			}
		}
	}

	/**
	 * recalcula los centroides hasta que el criterio de parada se cumpla.
	 */
	private void ajustarCentroides(){
		boolean parar = false;
		LinkedList<Integer> bitsDePertenencia;
		while(!parar){
			//System.out.println("se calcula la vuelta: "+numIteraciones);
			//Se calcula para cada instancia a que Cluster pertenece
			bitsDePertenencia = hallarBitsPertenencia();
			//se mueven los centroides al medio de las instancias.
			LinkedList<Cluster> nLC = moverCentroides(bitsDePertenencia);
			//Se mira si el criterio de parada se cumple (numero de iteraciones o disimilitud)
			parar = cumpleCriteriosDeParada(nLC);
			listaClusters = nLC;
		}
	}
	
	/**
	 * Para cada instancia se decide cual es el cluster que tiene mas cerca
	 */
	private LinkedList<Integer> hallarBitsPertenencia() {
		LinkedList<Integer> bitsDePertenencia = new LinkedList<Integer>();
		for(int i =0; i<listaInstancias.size(); i++){
			bitsDePertenencia.add(-1);
		}
		double min, aux;
		for(int i =0; i<listaInstancias.size(); i++){
			min = Double.MAX_VALUE;
			for(int j= 0; j<listaClusters.size(); j++){
				aux = calcularDistancia(listaClusters.get(j), listaInstancias.get(i));
				if(aux < min){
					min = aux;
					bitsDePertenencia.set(i, j);
				}
			}
		}
		
		return bitsDePertenencia;
	}


	/**
	 * Calcula la distancia de minkowski entre dos instancias o clusters
	 * @param pOrigen
	 * @param pDestino
	 * @return
	 */
	private double calcularDistancia(Instancia pOrigen, Instancia pDestino){
		double rdo=0.0;
		double resta;
		//suponemos que todos los atributos son numericos
		for(int i=0; i<pDestino.getNumAtributos();i++){
			resta= pDestino.getAtributo(i)-pOrigen.getAtributo(i);
			resta = Math.abs(resta);
			rdo = rdo + Math.pow(resta, m);
		}
		return Math.pow(rdo, (1.0/(double)m));
	}

	/**
	 * Recalcula los nuevos centroides
	 * @param bitsDePertenencia
	 */
	private LinkedList<Cluster> moverCentroides(LinkedList<Integer> bitsDePertenencia) {
		Cluster c;
		int cont;
		int numAttr = listaInstancias.get(0).getNumAtributos();
		LinkedList<Cluster> nuevaListaClusters = new LinkedList<Cluster>();
		for(int i=0; i<listaClusters.size(); i++){
			c = new Cluster(numAttr);
			cont=0;
			for(int j=0; j<listaInstancias.size(); j++){
				if(bitsDePertenencia.get(j)==i){
					c.sumaInstancia(listaInstancias.get(j));
					cont++;
				}
			}
			if(cont==0){
				c= new Cluster(listaClusters.get(i).getInstancia());
			}else{
				c.hacerMedia(cont);
			}
			c.setClase("C"+i);
			nuevaListaClusters.add(c);	
		}
		return nuevaListaClusters;
	}


	/**
	 * Comprueba si se cumplen los criterios de parada, vease numero de vueltas o disimilitud entre conjunto de centroides
	 * @param nLC
	 * @return
	 */
	private boolean cumpleCriteriosDeParada(LinkedList<Cluster> nLC) {
		//System.out.println(tipoParada+" - "+cteParada+" - ");
		boolean rdo = true;
		if(tipoParada=='i'){
			//parada por iteraciones
			if(cteParada > numIteraciones){
				rdo=false;
				numIteraciones++;
			}
		}else{
			//parada por disimilitud
			if(cteParada <= disimilitud(nLC)){
				rdo=false;
			}
		}
		return rdo;
	}

	/**
	 * calcula la media de las distancias entre los centroides nuevos y actuales, hallando la disimilitud
	 * @param nLC
	 * @return
	 */
	private double disimilitud(LinkedList<Cluster> nLC) {
		double rdo = 0;
		for(int i=0; i< nLC.size(); i++){
			
			rdo = rdo+calcularDistancia(nLC.get(i), listaClusters.get(i));
		}
		return rdo/nLC.size();
	}
	

	/**
	 * Devuelve un hasmap en el que l key es la clase y el value una lista de integers donde cada indice coresponde al cluster
	 * y el integer a la suma de las veces q esta esa clase en el cluster
	 * @param pListaInstancias
	 * @param pBitsPertenencia
	 * @return
	 */
	private HashMap<String, LinkedList<Integer>> asignarClasesAClusters(LinkedList<Instancia> pListaInstancias, LinkedList<Integer> pBitsPertenencia) {
		HashMap<String, LinkedList<Integer>> clasesClusters = new HashMap<String, LinkedList<Integer>>();
		LinkedList<Integer> aux;
		int sum;
		//recorre la lista de instancias
		for(int i=0; i<pListaInstancias.size(); i++){
			//miramos si la clase ya esta en el HashMap
			if(clasesClusters.containsKey(pListaInstancias.get(i).getClase())){//si esta
				aux = clasesClusters.remove(pListaInstancias.get(i).getClase());//eliminamos y guardamos la lista en aux
				sum = aux.get(pBitsPertenencia.get(i));//guardamos el valor
				aux.set(pBitsPertenencia.get(i), sum+1);//le sumamos 1
				clasesClusters.put(pListaInstancias.get(i).getClase(), aux);//volvemos a agregar al HashMap
			}else{
				aux = new LinkedList<Integer>();
				for(int j=0; j<k; j++){
					aux.add(0);
				}
				aux.set(pBitsPertenencia.get(i), 1);
				clasesClusters.put(pListaInstancias.get(i).getClase(), aux);
			}
		}
		
		return clasesClusters;
			
	}	

	private void imprimirHasMap(HashMap<String, LinkedList<Integer>> pClasesCluster) {
		
		for (int i = 0; i < listaClusters.size(); i++) {
			System.out.print("\tC"+(i+1));
		}
        System.out.print("\n");
        Iterator<String> itr = pClasesCluster.keySet().iterator();
		while (itr.hasNext()) { 
            String clase =(String) itr.next();
            LinkedList<Integer> cont =pClasesCluster.get(clase);
            for (int i = 0; i < cont.size(); i++) {
            	System.out.print("\t"+cont.get(i));
			}
            System.out.print("\t-->  "+clase);
            System.out.print("\n");
	   
		}
		
	}
	
	
	
	private static void imprimirAsignacion(HashMap<String, LinkedList<Integer>> pClasesCluster) {
			//Buscamos mientras el HashMap tenga datos
		System.out.print("\n");
			while(!pClasesCluster.isEmpty()){
				LinkedList<String> clases = new LinkedList<String>();
				LinkedList<Integer> maximo = new LinkedList<Integer>();
				LinkedList<Integer> cluster = new LinkedList<Integer>();
				//Buscamos el mayor en cada fila
				Iterator<String> itr = pClasesCluster.keySet().iterator();
				while (itr.hasNext()) {
					String clase =(String) itr.next();
				    clases.add(clase);
				    int max= Collections.max(pClasesCluster.get(clase));
				    cluster.add(pClasesCluster.get(clase).indexOf(max));
				    maximo.add(max);
				}
				//Obtenemos el mayor de los mayores de cada fila
				int i= maximo.indexOf(Collections.max(maximo));
				//Imprimirmos a quien pertenece esa asignacion
				System.out.print("Cluster"+(cluster.get(i)+1)+"\t-->  "+clases.get(i)+"\n");
				//borramos la fila(clase)
				pClasesCluster.remove(clases.get(i));
				i=cluster.get(i);
				Iterator<String> itr2 = pClasesCluster.keySet().iterator();
				//en cada fila restante ponemos la columna (corresponde al cluster) al
				//valor minimo para que no la seleccione nunca.
				while (itr2.hasNext()) {
					String c = itr2.next();
					cluster = pClasesCluster.get(c);
					cluster.set(i, Integer.MIN_VALUE);
					pClasesCluster.put(c, cluster);
				}
		}
	}

}
