package mineriadatos;

public class Rango {
	
	private double max;
	private double min;
	
	public Rango(double pMax, double pMin){
		max = pMax;
		min = pMin;
	}

	public double getMax() {
		return max;
	}
	
	public double getMin() {
		return min;
	}
	
	public double media(){
		return (min+max)/2;
	}
}
