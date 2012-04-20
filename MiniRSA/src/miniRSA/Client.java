package miniRSA;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
/*MainServer
 * 
 * 1. A static Vector<Socket> with a predefined size.
 * 2. Customized enqueue and dequeue operations on the Vector.
 * 3. Run forever : accept() and enqueue
 *  
 * A function to kill or terminate all worker threads
 * 
 * constructor: spawn all worker threads
 * 
 * */

/*ThreadWorkers
 * 
 * 1.Dequeue from mainServer's Vector
 * 2. Read()/write()
 * Will encompass your RSA
 * 
 * */
class Client {
	
   public static void main(String args[]) {
      try {
    	  Scanner scan = new Scanner(System.in);
		  System.out.println("Please enter a public key: ");
		  int publicKey = scan.nextInt();
		  System.out.println("Please enter c: ");
		  int c = scan.nextInt();
		  scan.nextLine();
		  String data = "Public: " + publicKey + " C: " + c;
		  MiniRSA rsa = new MiniRSA();
		  Socket socket = new Socket("localhost", 1234);
    	  PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		  System.out.println("Sending string: " + data);
		  out.println(data);
		  do{
			  ArrayList<Integer> intArray = new ArrayList<Integer>();
			  System.out.println("Please type the string: (type quit to exit)");
			  String input = scan.nextLine();
			  if(input.equals("quit") == false){
				  for(int i = 0; i < input.length(); i++){
					  intArray.add(rsa.endecrypt(input.charAt(i), publicKey, c));
				  }
				  String encryptedIntegers = "";
				  for(Integer integer: intArray){
					  encryptedIntegers+= integer + " ";
				  }
				  out.println(encryptedIntegers);
			  }
			  else {
				  out.close();
				  socket.close();
			  }
		  } while(socket.isClosed() == false);
			
      }
      catch(Exception e) {
         System.out.println("Problem connecting to the server!");
      }
   }
}

