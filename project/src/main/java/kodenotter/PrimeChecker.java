package kodenotter;

public class PrimeChecker {
	
	static boolean isPrime(int number) {
		if (number>1) { //1 er ikke primtall
			for (int i=2;i<number;i++) { //Skjekker delelighet med alle tall fra 2 til input tallet
				if (number%i==0) {
					return false; //dersom tallet er delelig er det ikke et primtall
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(isPrime(1));
		System.out.println(isPrime(4));
		System.out.println(isPrime(5));
		System.out.println(isPrime(19));
	}
}
