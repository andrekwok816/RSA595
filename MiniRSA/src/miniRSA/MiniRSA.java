package miniRSA;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MiniRSA {
	static Random random = new Random();
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] arg){
		MiniRSA rsa = new MiniRSA();
//		rsa.run();
//		rsa.run2();
		rsa.run3();
	}
	
	public long getPrimeNumber(long n){
		long count = n, num = 1;
		while(count != 0){			
			num++;
			if(isPrime(num) == true)
				count--;
		}
		return num;
	}
	
	public void run(){
//		System.out.println(mod_inverse(451, totient(2623)));
		System.out.println("Enter the nth prime and the mth prime to compute");
		String number = scan.next();
		String number2 = scan.next();
		long nthNumber = Long.parseLong(number);
		long mthNumber = Long.parseLong(number2);
		System.out.println(nthNumber + " " + mthNumber);
		long count = nthNumber, num = 1, firstPrime, secondPrime, c, m, e, d;
		while(count != 0){			
			num++;
			if(isPrime(num) == true)
				count--;
		}
		firstPrime = num;
		count = mthNumber;
		num = 1;
		while(count != 0){			
			num++;
			if(isPrime(num) == true)
				count--;
		}
		secondPrime = num;
		System.out.println(firstPrime + " " + secondPrime);
		c = firstPrime * secondPrime;
		m = (firstPrime - 1) * (secondPrime - 1);
		e = coprime(m);
		d = mod_inverse(e, m);
		System.out.println(nthNumber + "th prime = " + firstPrime + ", " + mthNumber +
				"th prime = " + secondPrime + ", c = " + c + ", m = " + m + ", e = " + e +
				", d = " + d + ", Public Key = (" + e + ", " + c + "), Private Key = (" +
				d + ", " + c + ")");
	}
	
	public void run2(){
		System.out.println("Please enter the public key (e, c): first e, then c");
		String number = scan.next();
		String number2 = scan.next();
		long intNum = Long.parseLong(number);
		long intNum2 = Long.parseLong(number2);
		System.out.println("Please enter a sentence to encrypt");
		String input = scan.next();
		for(int i = 0; i < input.length(); i++){
			System.out.println(endecrypt(input.charAt(i), intNum, intNum2));
		}
		System.out.println("Please enter the private key (d, c): first d, then c");
		String stringNumber = scan.next();
		String stringNumber2 = scan.next();
		long intNumber = Long.parseLong(stringNumber);
		long intNumber2 = Long.parseLong(stringNumber2);
		String input2 = "";
		while(input2.equals("quit") == false){
			System.out.println("Enter next char cipher value as an int, type quit to quit");
			input2 = scan.next();
			if(input2.equals("quit") == false){
				long intNumber3 = Long.parseLong(input2);
				long encryptNumber = endecrypt(intNumber3, intNumber, intNumber2);
				System.out.println((char) encryptNumber + " " + encryptNumber);
			}
		}
	}
	
	public void run3(){
		System.out.println("Enter the public key value");
		String stringNumber = scan.next();
		String stringNumber2 = scan.next();
		long intNumber = Long.parseLong(stringNumber);
		long intNumber2 = Long.parseLong(stringNumber2);
		long a = 0, b = 0, totient = totient(2623), d = 0;
		//451 2623
		long[] array = calculateC(intNumber2);
		a = array[1];
		b = array[0];
		d = mod_inverse(intNumber, totient);
		
		String input = "";
		System.out.println("Enter the c that goes with the public key");
		//calculate ...
		
		System.out.println("a was " + a + " b was " + b + "\nThe totient is " + 
				totient + "\nD was found to be " + d);
		
		while(input.equals("quit") != true){
			System.out.println("Enter a letter to encrypt/decrypt, or quit to exit");
			input = scan.next();
			if(input.equals("quit") == false){
				long intNumber3 = Long.parseLong(input); //1148
				long decryptedValue = endecrypt(intNumber3, d, intNumber2);
				System.out.println("This char decrypted to " + decryptedValue);
				System.out.println("The letter is " + (char) decryptedValue);
			}
		}
		System.out.println("Done!");
	}//run3
	
	private long nextPrime(long n){
		long number = n + 1;
		while(isPrime(number) == false){
			number++;
		}
		return number;
	}
	
	public long[] calculateC(long n){
		ArrayList<Long> array = new ArrayList<Long>();
		long[] returnArray = new long[2];
		boolean isDone = false;
		array.add((long)2);
		while(isDone == false){
			array.add(nextPrime(array.get(array.size()- 1)));
			for(int i = array.size() - 1; i < array.size(); i++){
				for(int j = 0; j < array.size(); j++){
					if(array.get(i) * array.get(j) == n && i != j){
						returnArray[0] = array.get(i);
						returnArray[1] = array.get(j);
						isDone = true;
					}			
				}//inner loop
			}//outer loop
		}//while
		return returnArray;
	}
	
	private boolean isPrime(long n) {
	    for(int i = 2; i < n; i++) {
	        if(n % i == 0)
	            return false;
	    }
	    return true;
	}
	
	public long coprime(long x){
		long number;
		number = Math.abs(random.nextInt());
		while(GCD(number, x) != 1){
			number = Math.abs(random.nextInt());
		}
		return number;
	}
	
	public long GCD(long a, long b){
		long s;
		if (a > b) s = b;
		else s = a;
		
		for (long i = s; i > 0; i--) {
			if ((a%i == 0) && (b%i == 0))
				return i;
		}
		return -1;
	}
	
	public long mod_inverse(long base, long m){
        long[] array = extendedEuclid(base, m);
        if (array[0] == 1){
            long value = array[1] % m;
            if (value < 0) value = value + m;
                return value;
        }
        return 0;
}

private long[] extendedEuclid(long base, long m){
        long[] array = new long[3];
        if (base == m){
                array[0] = base;
                array[1] = 1;
                array[2] = 0;
        }
        else{
                ArrayList<Long>[] list = new ArrayList[6];
                for (int i = 0; i < 6; i++){
                        list[i] = new ArrayList();
                }
                if (base < m){
                        list[0].add(base);
                        list[1].add(m);
                }
                else{
                        list[0].add(m);
                        list[1].add(base);
                }
                list[2].add(list[1].get(0) / list[0].get(0));
                list[3].add(list[1].get(0) - list[2].get(0) * list[0].get(0));
                int i = 1;
                while (!(list[3].get(i - 1) == 0)){
                        list[0].add(list[3].get(i - 1));
                        list[1].add(list[0].get(i - 1));
                        list[2].add(list[1].get(i) / list[0].get(i));
                        list[3].add(list[1].get(i) - list[2].get(i) * list[0].get(i));
                        i = i + 1;
                }
                i = i - 1;
                list[4].add((long)1);
                list[5].add((long)0);
                i = i - 1;
                int j = 0;
                while (i >= 0){
                        list[5].add(list[4].get(j));
                        list[4].add(list[5].get(j) - list[2].get(i) * list[4].get(j));
                        j = j + 1;
                        i = i - 1;
                }
                if (base > m){
                        array[0] = GCD(base, m);
                        array[1] = list[5].get(j);
                        array[2] = list[4].get(j);
                }
                else{
                        array[0] = GCD(base, m);
                        array[1] = list[4].get(j);
                        array[2] = list[5].get(j);
                }
        }
        return array;
}

public int modulo(int a , int b, int c){
        return (int)Math.pow(a, b) % c;
}

public int totient(long n){
        int count = 0; 
        for(int i = 1; i < n; i++) 
                if(GCD(n,i) == 1) count++; 
        return count; 
}

public long endecrypt(long msg_or_cipher, long key, long c){
        // H ->72, key ->451, c ->2623 result 1148
        // 72^451 % 2623
        long result = 0;
        if (key == 0){
                result = 1 % c;
        }
        else if (key == 1){
                result = msg_or_cipher % c;
        }
        else{
                ArrayList<Long> intArray = int2baseTwo(key);
                ArrayList<Long> copyIntArray = new ArrayList<Long>();
                for (int i = 0; i < intArray.size(); i++){
                        copyIntArray.add(intArray.get(i));
                }
                int i = 0;
                while (i < intArray.size()){
                        if (i == 0) {
                            copyIntArray.set(i, msg_or_cipher % c);
                        }
                        else{
                                copyIntArray.set(i, copyIntArray.get(i - 1) * copyIntArray.get(i - 1));
                                copyIntArray.set(i, copyIntArray.get(i) % c);
                        }
                        i = i + 1;
                }
                i = 0;
                while (i < intArray.size()){
                        if (intArray.get(i) == 1){
                                if (result == 0){
                                        result = copyIntArray.get(i);
                                }
                                else{
                                        result = result * copyIntArray.get(i);
                                }
                                result = result % c;
                        }
                        i = i + 1;
                }
        }
        return result;
}

private ArrayList<Long> int2baseTwo(long d){
        ArrayList<Long> intArray = new ArrayList<Long>();
        long num = d;
        while ( num >= 1){
                if (num % 2 == 0){
                        intArray.add((long)0);
                }
                else{
                        intArray.add((long)1);
                }
                num = num / 2;
        }
//        ArrayList<Integer> orderIntArray = new ArrayList<>();   //reverse order
//        for (int i = intArray.size() - 1; i >= 0; i--){
//                orderIntArray.add(intArray.get(i));
//        }
        return intArray;
}
}
