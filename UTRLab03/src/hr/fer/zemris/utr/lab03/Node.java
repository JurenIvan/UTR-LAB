package hr.fer.zemris.utr.lab03;

import java.util.HashMap;

public class Node implements Comparable<Node>{

	private String name;
	private HashMap<Pair<Character,Character>, Pair<Node,String>> triggers;
	private boolean isFinal=false;

	public Node(String name) {
		this.name = name;
		triggers = new HashMap<Pair<Character,Character>, Pair<Node,String>>();
	}

	public void setTrigger(char charInput,char charStack, Node nodeModel,String newStackChars) {
		triggers.put(new Pair<Character,Character>(charInput, charStack), new Pair<Node, String>(nodeModel, newStackChars));
	}
	
	public void setFinal(boolean isFinal) {
		this.isFinal=isFinal;
	}
	
	public boolean getFinal() {
		return this.isFinal;
	}
	
	public String getName() {
		return name;
	}

	public Pair<Node, String> getTriggers(char charInput,char charStack) {
		return triggers.get(new Pair<Character,Character>(charInput, charStack));
	}

	@Override
	public int compareTo(Node o) {
		return this.name.compareTo(o.getName());
	}

}
