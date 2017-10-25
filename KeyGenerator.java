
public class KeyGenerator {
	
	private static final int b = 3;
	private static final int g = 23;
	private static final int p = 86;
	
	public int getKey() {
		return (int) Math.pow(g, b) % p;
	}
	
	public int getKeyG(int g) {
		return (int) Math.pow(g, b) % p;
	}
	
	

}
