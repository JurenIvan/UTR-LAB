package hr.fer.zemris.utr.lab03;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class SimDPA {

	public static void main(String[] args) {
		String input="";
		String output="";
		List<String> linesInput=null;
		List<String> linesOutput=null;
		
		String numberOfTest="25";
		String pathToTest="C:\\Faks\\G2\\S4\\Utr\\LabTestovi\\lab3_primjeri\\test"+numberOfTest;
		try {
			linesInput = Files.readAllLines(Paths.get(pathToTest+"\\primjer.in"));
			linesOutput= Files.readAllLines(Paths.get(pathToTest+"\\primjer.out"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(var line:linesInput) 
			input=input+line+"\n";
		
		for(var line:linesOutput) 
			output=output+line+"\n";
		
		Scanner sc = new Scanner(input);
		DPA dpa = new DPA();

		// 01 input ulaza
		String[] inputStrings = sc.nextLine().split("\\|");

		// 02 input stanja
		String[] possibleStates = sc.nextLine().split(",");
		dpa.addNodes(possibleStates);

		// 03 accepted alphabet
		String[] alphabet = sc.nextLine().split(",");
		dpa.setAlphabet(alphabet);
		
		// 04 accepted alphabet
		String[] stackAlphabet = sc.nextLine().split(",");
		dpa.setStackAlphabet(stackAlphabet);
		
		// 05 FINAL states
		String[] finishNode = sc.nextLine().split(",");
		dpa.setFinalNodes(finishNode);

		// 06 Active at Beginning
		String startNode = sc.nextLine().trim();
		dpa.setInitialState(startNode);
		
		// 06 Active at Beginning
		char startStackChar = sc.nextLine().trim().charAt(0);
		dpa.setInitialStackChar(startStackChar);

		
		// 07 States and changes
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();

			String nodeThatTriggers = line.split("->")[0].split(",")[0];
			char charInput = line.split("->")[0].split(",")[1].charAt(0);
			char charStack = line.split("->")[0].split(",")[2].charAt(0);
			
			String nodeToTrigger=line.split("->")[1].split(",")[0];
			String stackString=line.split("->")[1].split(",")[1];
			
			dpa.setTrigger(nodeThatTriggers, charInput, charStack, nodeToTrigger, stackString);
		}

		// Feeds input and expects output;
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < inputStrings.length; i++) {
			sb.append(dpa.reDo(inputStrings[i].split(",")));
			sb.append('\n');
		}
		
		
		
		System.out.println(sb.toString());
		System.out.println("Expected Output");
		System.out.println(output);
		System.out.println("Is correct");
		System.out.println(output.equals(sb.toString()));
		System.out.println();
//		System.out.println(input);
		
		
		try (PrintWriter out = new PrintWriter("C:\\utr shit\\file.txt")) {
		    out.println(output);
		}catch (Exception e) {
			sc.close();
			return;
		}
		
		sc.close();
	}

}
