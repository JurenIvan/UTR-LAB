package hr.fer.zemris.utr.lab1;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class SimEnka {

	public static void main(String[] args) {
		String input="";
		String output="";
		List<String> linesInput=null;
		List<String> linesOutput=null;
		
		String numberOfTest="33";
		String pathToTest="C:\\Faks\\G2\\S4\\Utr\\LabTestovi\\lab1_primjeri\\test"+numberOfTest;
		try {
			linesInput = Files.readAllLines(Paths.get(pathToTest+"\\test.a"));
			linesOutput= Files.readAllLines(Paths.get(pathToTest+"\\test.b"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(var line:linesInput) 
			input=input+line+"\n";
		
		for(var line:linesOutput) 
			output=output+line+"\n";
		
		Scanner sc = new Scanner(input);
		
		String[] inputStrings;
		String[] possibleStates;
		String[] alphabet;
		String[] finishNode;
		String[] startNode;

		ENKAModel enka = new ENKAModel();

		// 01 input ulaza
		inputStrings = sc.nextLine().split("\\|");

		// 02 input stanja
		possibleStates = sc.nextLine().split(",");
		enka.addNodes(possibleStates);

		// 03 accepted alphabet
		alphabet = sc.nextLine().split(",");
		enka.setAlphabet(alphabet);

		// 04 FINAL states
		finishNode = sc.nextLine().split(",");
		enka.setFinalStates(finishNode);

		// 05 Active at Beginning
		startNode = sc.nextLine().split(",");
		enka.setInitialState(startNode);

		// 06 States and changes
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String nodeThatIsTriggered, triggerKey, getsTriggered[];

			nodeThatIsTriggered = line.split("->")[0].split(",")[0];
			triggerKey = line.split("->")[0].split(",")[1];
			getsTriggered = line.split("->")[1].split(",");

			for (int i = 0; i < getsTriggered.length; i++) {
				enka.setTrigger(nodeThatIsTriggered, triggerKey, getsTriggered[i]);
			}

		}

		// Feeds input and expects output;
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < inputStrings.length; i++) {
			sb.append(enka.reDo(inputStrings[i].split(",")));
			sb.append('\n');
		}
		System.out.println(sb.toString());
		System.out.println();
		System.out.println(output);
		System.out.println(output.equals(sb.toString()));
		
		try (PrintWriter out = new PrintWriter("C:\\utr shit\\file.txt")) {
		    out.println(output);
		}catch (Exception e) {
			sc.close();
			return;
		}
		
		sc.close();
	}

}
