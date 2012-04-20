package miniRSA;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

class Receiving extends Thread {
	Socket socket;
	Thread thisThread = null;
	long e, c, d;
	BufferedReader in;
	MiniRSA rsa = new MiniRSA();
	Sending send;

	public Receiving(Socket socket, long e, long c, long d,
			BufferedReader in){
		this.socket = socket;
		this.e = e;
		this.c = c;
		this.d = d;
		this.in = in;
	}

	public void putSend(Sending send){
		this.send = send;
	}

	@Override
	public void run(){
		String content = null;
		boolean done = false;
		while(done == false){
			try {
				while (!in.ready() && socket.isClosed() == false) {
					sleep(1);
				}
				if(socket.isClosed() == false) {
					content = in.readLine(); 
					System.out.print("Received string: '"  + content + "'\n"); // Read one line and output it
					String[] inputArray = content.split(" ");
					System.out.println("Decoding message:");
					String message = "";
					for(int i = 0; i < inputArray.length; i++){
						long letter = Long.parseLong(inputArray[i]);
						System.out.print((char) rsa.endecrypt(letter, d, c) + "");
						message += (char) rsa.endecrypt(letter, d, c);
					}
					if(message.equals("quit")){ done = true;
					System.out.println("\nSince Server exited, Client is exiting");
					} else{
						System.out.println();
						System.out.println("Finished Decoding"); }
				} else done = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {

			}

		}//while
		send.interrupt();
		try {
			socket.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		System.out.println("exit");
	}
}

class Client extends Thread {
	static Random random = new Random();
	static MiniRSA rsa = new MiniRSA();

	public static void main(String args[]) {
		try {
			int first = random.nextInt(20 - 10) + 10;
			int second = random.nextInt(20 - 10) + 10;
			while(first == second){
				second = random.nextInt(20 - 10) + 10;
			}
			long firstPrime = rsa.getPrimeNumber(first);
			long secondPrime = rsa.getPrimeNumber(second);
			//  		System.out.println(firstPrime + " " + secondPrime);
			long c = firstPrime * secondPrime;
			long m = (firstPrime - 1) * (secondPrime - 1);
			long e = rsa.coprime(m);
			long d = rsa.mod_inverse(e, m);
			System.out.println("Client's Public Key(e, c): (" + e + ", " + c + ")");
			System.out.println("Client's Private Key(d, c): (" + d + ", " + c + ")");
			String data = "e: " + e + " c: " + c;
			MiniRSA rsa = new MiniRSA();
			Socket socket = new Socket("localhost", 2000);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			System.out.println("Sending string: " + data);
			out.println(data);

			BufferedReader in = new BufferedReader(new
					InputStreamReader(socket.getInputStream()));
			System.out.print("Received string: '");
			while (!in.ready()) {}
			String header = in.readLine(); //"e: " + e + " c: " + c
			System.out.print(header + "'\n"); // Read one line and output it
			String[] stringArray = header.split(" ");
			long ServerE = Long.parseLong(stringArray[1]);
			long ServerC = Long.parseLong(stringArray[3]);
			int totient = rsa.totient(ServerC);
			long privateKey = rsa.mod_inverse(ServerE, totient);
			System.out.println("Server Private key: (" + privateKey + ", " + ServerC + ")\n");

			Receiving receive = new Receiving(socket, ServerE, ServerC, privateKey, in);
			Sending send = new Sending(socket, e, c, d, in, receive, out);
			receive.putSend(send);
			receive.start();
			send.start();

		}
		catch(Exception e) {
			System.out.println("Problem connecting to the server!");
		}
	}
}

class Sending extends Thread{
	Socket socket;
	Thread thisThread = null;
	long e, c, d;
	BufferedReader in;
	MiniRSA rsa = new MiniRSA();
	Receiving receive;
	PrintWriter out;

	public Sending(Socket socket, long e, long c, long d,
			BufferedReader in, Receiving receive, PrintWriter out){
		this.socket = socket;
		this.e = e;
		this.c = c;
		this.d = d;
		this.in = in;
		this.receive = receive;
		this.out = out;
	}

	@Override
	public void run(){
		do{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try{
				System.out.println("Please type the string: (type quit to exit)");
				while(!reader.ready()) { sleep(1); }
				ArrayList<Long> intArray = new ArrayList<Long>();
				String input = reader.readLine();
				if(input.equals("quit") == false){
					for(int i = 0; i < input.length(); i++){
						intArray.add(rsa.endecrypt(input.charAt(i), e, c));
					}
					String encryptedIntegers = "";
					for(Long integer: intArray){
						encryptedIntegers+= integer + " ";
					}
					out.println(encryptedIntegers);
				}

				else {
					try {
						out.close();
						socket.close();
					} catch (IOException exception) {}
				}
			} catch (Exception exception) {}
		} while(socket.isClosed() == false);
	}
}
