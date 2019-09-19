package hr.fer.zemris.utr.lab03;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class DPA {

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
		if(newStackChars.equals("$")) newStackChars="";
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
			if (e.isBlank())
				continue;
			nodes.get(e).setFinal(true);
		}
	}

	public void setStackAlphabet(String[] alphabet) {
	}

	public void setInitialStackChar(char startStackChar) {
		this.startStackChar=startStackChar;
		stack = new Stack<>();
		stack.add(startStackChar);
	}

	// -----------------------------------------

	public String reDo(String[] inputToken) {
		StringBuilder sb = new StringBuilder();
		Pair<Node, String> newState = null;
		reset();
		boolean flag = false;

		appendCurrStates(sb);
		for (var inputString : inputToken) {
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
			}catch (EmptyStackException e) {
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
		if(stack.isEmpty()) {
			return "$";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = stack.size() - 1; i >= 0; i--) {
			sb.append(stack.get(i));
		}
		return sb.toString();
	}

}
