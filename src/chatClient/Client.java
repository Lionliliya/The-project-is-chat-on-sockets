package chatClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
	public Client(String name) {
		super();
		this.name = name;
		Scanner sc=new Scanner(System.in);
		System.out.println("Input Server IP");
		String S = sc.nextLine();

		try {
			soc = new Socket(S, 20000); /**Setting of the client connection**/
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		dt = new Date();
	}

	private String name;
	Socket soc;
	Date dt;
	private int n = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void start() {
/**Input and output
network streams**/		
		try (DataInputStream in = new DataInputStream(soc.getInputStream()); // Use DataInputStream because of UTF writing and reading methods
				BufferedReader console = new BufferedReader(
						new InputStreamReader(System.in));
				DataOutputStream out = new DataOutputStream(
						soc.getOutputStream())) {

			out.writeUTF(name + " join to chat at " + new Date().toString());
		
/**Create a process that takes messages in
parallel thread**/			
			Thread t = new Thread() {
				
				public int n = 0;

				@Override
				public void run() {
					for (; n < 3;) {
						String message = "";
						try {
							message = in.readUTF();
						} catch (IOException e) {
							n++;
						}
						System.out.println(message);
					}
					System.out.println("Client listener shutdown");
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			
/**Thread will end when the main thread 
 * is interrupted**/			
			t.setDaemon(true);
			
			t.start();

/**Sending a message to the 
 * server**/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MMM:dd HH:mm");
			for (; t.isAlive();) {
				String response = console.readLine();
				if (response == null) {
					break;
				}
				out.writeUTF("[" + name + " " + sdf.format(new Date()) + " "
						+ " ] - " + response);
			}
			System.out.println("Client sender shutdown");
			out.close();
			console.close();
		} catch (IOException e) {
			n++;
		}
	}
}
