package mineriadatos;

import java.util.LinkedList;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instances;



public class OperacionesWeka {
	
	private static LinkedList<Instancia> listaInstanciasWeka;
	private static LinkedList<Integer> bitsDePertenenciaWeka;
	private static Instances InstanciasWeka;
	private static OperacionesWeka misOperacionesWeka;
	
	private OperacionesWeka(){
		bitsDePertenenciaWeka= new LinkedList<Integer>();
		listaInstanciasWeka = new LinkedList<Instancia>();
	}
	
	public static OperacionesWeka getMisOperacionesWeka(){
		if(misOperacionesWeka == null){
			misOperacionesWeka= new OperacionesWeka();
		}
		return misOperacionesWeka;
	}
	
	public static void cargarListaInstanciasWeka(Instances pInstanciasWeka){
		InstanciasWeka= pInstanciasWeka;
	}
	
	// en el pre proceso tengo q crear la lista de instances
	//pSinCLases true -> si los datos recibidos no tienen clase y false al contrario.
	public LinkedList<Instancia> calcularClustersWeka( int pK, boolean pClases) throws Exception{
		
		String [] atri = null;
		if (pClases){
			InstanciasWeka.deleteAttributeAt(InstanciasWeka.numAttributes()-1);
		}
		
		InstanciasWeka.deleteStringAttributes();
		
		SimpleKMeans s = new SimpleKMeans();
		try {
			s.setNumClusters(pK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//creamos la distancia de minkowski
		EuclideanDistance df = new EuclideanDistance();
		
		//le decimos que distancia queremos que use
		s.setDistanceFunction(df);
		
		//calcula los clusters (grupos) para el dataset de entrenamiento indicado, en este aso InstanciasWeka
		s.buildClusterer(InstanciasWeka);
		
		//esto tomando en cuenta que la listaInstanciasWeka estan en el mismo orden que la lista de instancias
		for (int i = 0; i < InstanciasWeka.numInstances(); i++) {
			
			//int clusterInstance(Instance instance): indica el cluster al que pertenece la instancia pasada como argumento. [Exige haber invocado antes a buildClusterer()]
			bitsDePertenenciaWeka.add(s.clusterInstance(InstanciasWeka.get(i)));//creo la lista de bitsPertenencia de weka
			
			//relleno la lista de insancia de weka
			atri = new String[InstanciasWeka.get(i).numAttributes()+1];
			for (int j = 0; j <InstanciasWeka.get(i).numAttributes() ; j++){ 
				//System.out.print(InstanciasWeka.get(i).toDoubleArray()[j]+",");
				atri[j]=InstanciasWeka.get(i).toDoubleArray()[j]+"";
			}
			
			//si los datos no tenian clase le asigno el cluster q ha dado weka
			//System.out.print("Cluster"+s.clusterInstance(InstanciasWeka.get(i))+"\n");
			atri[atri.length-1]="WekaCluster"+(s.clusterInstance(InstanciasWeka.get(i))+1);
			
			Instancia ins = new Instancia(atri);

			listaInstanciasWeka.add(ins);
		}
	
		return listaInstanciasWeka ;
			
	}
	
	public LinkedList<Integer>  getBitsDePertenenciaWeka() {
		return bitsDePertenenciaWeka;
	}
	
	public LinkedList<Instancia>  getlistaInstanciasWeka() {
		return listaInstanciasWeka;
	}
	
}

