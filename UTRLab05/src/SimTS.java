import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class SimTS {

	private static int inputPos = 0;
	private static int headPosition;
	private static String state;
	private static String[] acceptableStates;
	private static char[] track;

	public static void main(String[] args) throws IOException {

//		String path = "C:\\Faks\\G2\\S4\\Utr\\LabTestovi\\lab5_primjeri\\test";
//		String addon = "21";
//		Scanner inputFile = new Scanner(Files.newInputStream(Paths.get(path + addon + "\\test.in")));
//		Scanner outputFile = new Scanner(Files.newInputStream(Paths.get(path + addon + "\\test.out")));
//		System.out.println(outputFile.nextLine());
//
//		Scanner sc = inputFile;
		Scanner sc = new Scanner(System.in);

		sc.nextLine(); // 1
		String[] input = sc.nextLine().split(","); // 2
		sc.nextLine(); // 3
		sc.nextLine(); // 4
		track = sc.nextLine().toCharArray(); // 5
		acceptableStates = sc.nextLine().split(","); // 6
		state = sc.nextLine(); // 7
		headPosition = Integer.parseInt(sc.nextLine()); // 8
		
		HashMap<String, String[]> proba = new HashMap<>();

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.isEmpty())
				break;
			proba.put(line.split("->")[0], line.split("->")[1].split(","));
		}

		while (inputPos <= input.length) {
			String key = state + "," + track[headPosition];
			String[] result = proba.get(key);
			if (result == null) {
				output();
				return;
			}

			state = result[0];
			track[headPosition] = result[1].charAt(0);
			if (headPosition + move(result[2]) < 0 || headPosition + move(result[2]) > 69) {
				output();
				return;
			}
			headPosition += move(result[2]);
		}

		System.out.println(String.valueOf(track));

	}

	private static void output() {
		String output = state + '|' + headPosition + '|' + String.valueOf(track) + '|' + isFinal();
		System.out.println(output);
	}

	private static int isFinal() {
		for (String pstate : acceptableStates) {
			if (state.equals(pstate))
				return 1;
		}
		return 0;
	}

	private static int move(String letter) {
		if (letter.equals("R"))
			return 1;
		return -1;
	}

}
