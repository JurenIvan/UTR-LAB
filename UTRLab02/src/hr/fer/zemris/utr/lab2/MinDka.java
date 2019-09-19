package hr.fer.zemris.utr.lab2;

import java.util.Scanner;

public class MinDka {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		String[] possibleStates;
		String[] alphabet;
		String[] finishNode;
		String[] startNode;

		DKAModel dka = new DKAModel();

		// 01 input stanja
		possibleStates = sc.nextLine().split(",");
		dka.addNodes(possibleStates);

		// 02 accepted alphabet
		alphabet = sc.nextLine().split(",");
		dka.setAlphabet(alphabet);

		// 03 FINAL states
		finishNode = sc.nextLine().split(",");
		dka.setFinalStates(finishNode);

		// 04 Active at Beginning
		startNode = sc.nextLine().split(",");
		dka.setInitialState(startNode);

		// 05 States and changes
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String nodeThatIsTriggered, triggerKey, getsTriggered[];
			if (line.isBlank())
				break;
			nodeThatIsTriggered = line.split("->")[0].split(",")[0];
			triggerKey = line.split("->")[0].split(",")[1];
			getsTriggered = line.split("->")[1].split(",");

			for (int i = 0; i < getsTriggered.length; i++) {
				dka.setTrigger(nodeThatIsTriggered, triggerKey, getsTriggered[i]);
			}
		}

		String outputMinimised = dka.minimise();
		System.out.println(outputMinimised);
		
		sc.close();
	}

}
