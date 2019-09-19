package hr.fer.zemris.utr.lab03;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class SimPa {

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

	// #################################################################

	private static class Node implements Comparable<Node> {

		private String name;
		private HashMap<Pair<Character, Character>, Pair<Node, String>> triggers;
		private boolean isFinal = false;

		public Node(String name) {
			this.name = name;
			triggers = new HashMap<Pair<Character, Character>, Pair<Node, String>>();
		}

		public void setTrigger(char charInput, char charStack, Node nodeModel, String newStackChars) {
			triggers.put(new Pair<Character, Character>(charInput, charStack),
					new Pair<Node, String>(nodeModel, newStackChars));
		}

		public void setFinal(boolean isFinal) {
			this.isFinal = isFinal;
		}

		public boolean getFinal() {
			return this.isFinal;
		}

		public String getName() {
			return name;
		}

		public Pair<Node, String> getTriggers(char charInput, char charStack) {
			return triggers.get(new Pair<Character, Character>(charInput, charStack));
		}

		@Override
		public int compareTo(Node o) {
			return this.name.compareTo(o.getName());
		}
	}

// #################################################################

	private static class Pair<F, S> {

		private F first;
		private S second;

		public Pair(F first, S second) {
			super();
			this.first = first;
			this.second = second;
		}

		public F getFirst() {
			return first;
		}

		public S getSecond() {
			return second;
		}

		@Override
		public int hashCode() {
			return Objects.hash(second, first);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Pair))
				return false;
			Pair<?, ?> other = (Pair<?, ?>) obj;
			return Objects.equals(second, other.second) && Objects.equals(first, other.first);
		}
	}

// ###########################################

	private static class DPA {

		private Map<String, Node> nodes;
		private Node initiallyActiveNode;
		private Stack<Character> stack;
		private Node currentState;
		private char startStackChar;

		public DPA() {
			this.nodes = new HashMap<String, Node>();
			stack = new Stack<Character>();
		}

		public void addNodes(String[] name) {
			for (int i = 0; i < name.length; i++)
				nodes.put(name[i], new Node(name[i]));
		}

		public void setAlphabet(String[] alphabetChars) {
		}

		public void setTrigger(String nodeThatTriggers, char charInput, char charStack, String nodeToTrigger,
				String newStackChars) {
			if (newStackChars.equals("$"))
				newStackChars = "";
			nodes.get(nodeThatTriggers).setTrigger(charInput, charStack, nodes.get(nodeToTrigger), newStackChars);

		}

		public void setInitialState(String activeNode) {
			initiallyActiveNode = nodes.get(activeNode);
			reset();
		}

		private void reset() {
			currentState = initiallyActiveNode;
			setInitialStackChar(startStackChar);
		}

		public void setFinalNodes(String[] finishNode) {
			for (String e : finishNode) {
				if (e.isEmpty())  //promjenio
					continue;
				nodes.get(e).setFinal(true);
			}
		}

		public void setStackAlphabet(String[] alphabet) {
		}

		public void setInitialStackChar(char startStackChar) {
			this.startStackChar = startStackChar;
			stack = new Stack<>();
			stack.add(startStackChar);
		}

		public String reDo(String[] inputToken) {
			StringBuilder sb = new StringBuilder();
			Pair<Node, String> newState = null;
			reset();
			boolean flag = false;

			appendCurrStates(sb);
			for (String inputString : inputToken) {
				char charInput = inputString.charAt(0);
				try {
					while ((newState = currentState.getTriggers('$', stack.peek())) != null) {
						stack.pop();
						String toStack = newState.getSecond();
						for (int i = toStack.length() - 1; i >= 0; i--) {
							stack.add(toStack.charAt(i));
						}
						currentState = newState.getFirst();
						appendCurrStates(sb);
					}

					newState = currentState.getTriggers(charInput, stack.peek());
					if (newState == null) {
						newState = currentState.getTriggers('$', stack.peek());
					}
					if (newState == null) {
						sb.append("fail|0");
						flag = true;
						break;
					}
					stack.pop();

					String toStack = newState.getSecond();
					for (int i = toStack.length() - 1; i >= 0; i--) {
						stack.add(toStack.charAt(i));
					}
					currentState = newState.getFirst();
					appendCurrStates(sb);
				} catch (EmptyStackException e) {
					sb.append("fail|0");
					flag = true;
					break;
				}
			}
			while (!currentState.getFinal() && (newState = currentState.getTriggers('$', stack.peek())) != null) {
				stack.pop();
				String toStack = newState.getSecond();
				for (int i = toStack.length() - 1; i >= 0; i--) {
					stack.add(toStack.charAt(i));
				}
				currentState = newState.getFirst();
				appendCurrStates(sb);
			}

			if (flag)
				return sb.toString();
			sb.append(currentState.getFinal() ? "1" : "0");
			return sb.toString();
		}

		private void appendCurrStates(StringBuilder sb) {
			sb.append(currentState.getName());
			sb.append("#");
			sb.append(printStack());
			sb.append("|");
		}

		private String printStack() {
			if (stack.isEmpty()) {
				return "$";
			}
			StringBuilder sb = new StringBuilder();
			for (int i = stack.size() - 1; i >= 0; i--) {
				sb.append(stack.get(i));
			}
			return sb.toString();
		}

	}
}
