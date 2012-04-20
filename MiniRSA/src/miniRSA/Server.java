package miniRSA;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Server extends Thread {
	static Random random = new Random();
	MiniRSA rsa = new MiniRSA();

	public static void main(String args[]) {
		Server server = new Server();
		server.start();
	}

	@Override
	public void run() {
		ThreadJob threadJob;
		int first = random.nextInt(20 - 10) + 10;
		int second = random.nextInt(20 - 10) + 10;
		while(first == second){
			second = random.nextInt(20 - 10) + 10;
		}
		long firstPrime = rsa.getPrimeNumber(first);
		long secondPrime = rsa.getPrimeNumber(second);
		long c = firstPrime * secondPrime;
		long m = (firstPrime - 1) * (secondPrime - 1);
		long e = rsa.coprime(m);
		long d = rsa.mod_inverse(e, m);
		System.out.println("Server's Public Key(e, c): (" + e + ", " + c + ")");
		System.out.println("Server's Private Key(d, c): (" + d + ", " + c + ")");
		ServerSocket srvr = null;
		Socket socket = null;
		try {
			srvr = new ServerSocket(2000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			socket = srvr.accept();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		threadJob = new ThreadJob(socket, e, c, d);
		threadJob.start();

		System.out.println("Server is exiting");
	}

	class Sending extends Thread {
		Socket socket;
		Thread thisThread = null;
		long e, c, d;
		PrintWriter out;
		ThreadJob thread;
		public Sending(Socket socket, long e, long c, long d,
				PrintWriter out, ThreadJob thread){
			this.socket = socket;
			this.e = e;
			this.c = c;
			this.d = d;
			this.out = out;
			this.thread = thread;
		}

		@Override
		public void run(){
			ArrayList<Long> intArray = new ArrayList<Long>();
			Scanner scan = new Scanner(System.in);
			boolean done = false;
			while(done == false){
				System.out.println("Please type the string: (type quit to exit)");
				String input = scan.nextLine();
				if(input.equals("quit") == false){
					for(int i = 0; i < input.length(); i++){
						intArray.add(rsa.endecrypt(input.charAt(i), e, c));
					}
					String encryptedIntegers = new String();

					for(Long integer: intArray){
						encryptedIntegers+= integer + " ";
					}
					System.out.println("Sending from Server: '" + encryptedIntegers + "'");
					out.println(encryptedIntegers);
					intArray = new ArrayList<Long>();
				} else{
					for(int i = 0; i < input.length(); i++){
						intArray.add(rsa.endecrypt(input.charAt(i), e, c));
					}
					String encryptedIntegers = new String();

					for(Long integer: intArray){
						encryptedIntegers+= integer + " ";
					}
					out.println(encryptedIntegers);
					done = true;
					thread.interrupt();
					try {
						socket.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
			}
		}
	}

	class ThreadJob extends Thread { //Thread Job
		Socket socket;
		Thread thisThread = null;
		long e, c, d;

		public ThreadJob(Socket socket, long e, long c, long d){
			this.socket = socket;
			this.e = e;
			this.c = c;
			this.d = d;
		}

		@Override
		public void run(){
			boolean done = false, done2 = false;
			BufferedReader in = null;
			PrintWriter out = null;

			while(done == false){
				try {
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new
							InputStreamReader(socket.getInputStream()));

					System.out.print("Received string: '");
					while (!in.ready()) {}
					String header = in.readLine(); //"e: " + e + " c: " + c
					System.out.print(header + "'\n"); // Read one line and output it
					String[] stringArray = header.split(" ");
					long ClientE = Long.parseLong(stringArray[1]);
					long ClientC = Long.parseLong(stringArray[3]);
					MiniRSA rsa = new MiniRSA();
					int totient = rsa.totient(ClientC);
					long privateKey = rsa.mod_inverse(ClientE, totient);
					System.out.println("Client Private key: (" + privateKey + ", " + ClientC + ")\n");
					String data = "e: " + e + " c: " + c;
					out.println(data);

					Sending send = new Sending(socket, e, c, d, out, this);
					send.start();


					while(done2 == false){
						while (!in.ready()) {
							sleep(1);
						}
						String content = in.readLine();
						System.out.print("Received string: '"  + content + "'\n"); // Read one line and output it
						String[] inputArray = content.split(" ");
						System.out.println("Decoding message:");
						for(int i = 0; i < inputArray.length; i++){
							long letter = Long.parseLong(inputArray[i]);
							System.out.print((char) rsa.endecrypt(letter, privateKey, ClientC) + "");
						}
						System.out.println();
						System.out.println("Finished Decoding");
					}
					done = true;
				}
				catch(InterruptedException e) {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					out.close();
					done = true;
				}
				catch(Exception e) {
					System.out.println("Problem on connection");
				}
			}
		}
	}
}
