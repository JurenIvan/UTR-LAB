import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Parser {

	private static String input;
	private static int pointer = 0;

	public static void main(String[] args) throws IOException {
		String path="C:\\Faks\\G2\\S4\\Utr\\LabTestovi\\lab4_primjeri\\test";
		
		String addon="20";
		
		Scanner inputFile=new Scanner(Files.newInputStream(Paths.get(path+addon+"\\test.in")));
		Scanner outputFile=new Scanner(Files.newInputStream(Paths.get(path+addon+"\\test.out")));
		
		System.out.println(outputFile.nextLine());
		System.out.println(outputFile.nextLine());
		System.out.println();
		
		Scanner sc = new Scanner(inputFile.nextLine());
//		Scanner sc = new Scanner(System.in);
		input = sc.nextLine();
		
		try {
		S();
		}catch (StringIndexOutOfBoundsException e) {
			System.out.print("\nNE");
			return;
		}
		if (pointer == input.length())
			System.out.print("\nDA");
		else
			System.out.print("\nNE");
	}

	public static void S() {
		System.out.print("S");
		if (input.charAt(pointer) == 'a') {
			pointer++;
			A();
			B();
			return;
		}
		if (input.charAt(pointer) == 'b') {
			pointer++;
			B();
			A();
			return;
		}
		System.out.print("\nNE");
		System.exit(0);

	}

	public static void A() {
		System.out.print("A");
		if (input.charAt(pointer) == 'b') {
			pointer++;
			C();
			return;
		}
		if (input.charAt(pointer) == 'a') {
			pointer++;
			return;
		}
		System.out.print("\nNE");
		System.exit(0);
	}

	public static void B() {
		System.out.print("B");
		
		if(pointer+1>input.length()) {
			return;
		}
		
		if (input.charAt(pointer) == 'c' && input.charAt(pointer + 1) == 'c') {
			pointer += 2;
			S();
			if (input.charAt(pointer) == 'b' && input.charAt(pointer + 1) == 'c') {
				pointer += 2;
				return;
			}
			System.out.print("\nNE");
			System.exit(0);
		}
		
		return;
	}

	public static void C() {
		System.out.print("C");
		A();
		A();
	}

}
