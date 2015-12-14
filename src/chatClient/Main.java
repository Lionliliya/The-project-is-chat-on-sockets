package chatClient;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Input login");
		String name = sc.nextLine();
		Client a = new Client(name);
		a.start();
		sc.close();
	}
}

	


