package miniRSA;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server extends Thread {
	
	public static void main(String args[]) {
		Server server = new Server();
		server.start();
	}

	@Override
	public void run() {
		ThreadJob threadJob;
		ServerSocket srvr = null;
		Socket socket = null;
		try {
			srvr = new ServerSocket(1234);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			try {
				socket = srvr.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			threadJob = new ThreadJob(socket);
			threadJob.start();
			String string = "";
			while(string.equals("quit") == false){
				Scanner scan = new Scanner(System.in);
				string = scan.next();
			}
			threadJob.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server is exiting");
	}
	
	class ThreadJob extends Thread { //Thread Job
		Socket socket;
	    Thread thisThread = null;
	    
		public ThreadJob(Socket socket){
			this.socket = socket;
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
					String header = in.readLine(); //"Public: " + publicKey + " C: " + c
					System.out.print(header + "'\n"); // Read one line and output it
					String[] stringArray = header.split(" ");
					int publicKey = Integer.parseInt(stringArray[1]);
					int c = Integer.parseInt(stringArray[3]);
					MiniRSA rsa = new MiniRSA();
					int totient = rsa.totient(c);
					int privateKey = rsa.mod_inverse(publicKey, totient);
					System.out.println("The generated Private key: " + privateKey);
					while(done2 == false){
						while (!in.ready()) {
							sleep(1);
						}
						String content = in.readLine();
						System.out.print("Received string: '"  + content + "'\n"); // Read one line and output it
						String[] inputArray = content.split(" ");
						System.out.println("Decoding message:");
						for(int i = 0; i < inputArray.length; i++){
							int letter = Integer.parseInt(inputArray[i]);
							System.out.print((char) rsa.endecrypt(letter, privateKey, c) + "");
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
