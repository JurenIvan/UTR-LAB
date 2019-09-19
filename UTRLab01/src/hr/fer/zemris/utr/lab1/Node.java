package hr.fer.zemris.utr.lab1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable<Node>{

	private String name;
	private HashMap<String, HashSet<Node>> triggers;
	private boolean currentState;
//	private boolean isFinalNode;	//useless

	public Node(String name) {
		this.name = name;
//		this.isFinalNode = false;	//useless
		this.currentState = false;
		triggers = new HashMap<String, HashSet<Node>>();
	}

	public void setNodeTrigger(String key, Node nodeModel) {
		HashSet<Node> untillNow;

		if (triggers.containsKey(key))
			untillNow = triggers.get(key);
		else
			untillNow = new HashSet<>();

		untillNow.add(nodeModel);
		triggers.put(key, untillNow);
	}
	
	public boolean isState() {
		return currentState;
	}

	public void setState(boolean state) {
		this.currentState = state;
	}
	
	//useless
//	public void setFinalNode(boolean isFinalNode) {
//		this.isFinalNode = isFinalNode;
//	}
	
	//useless
//	public boolean isItFinalAndActive() {
//		return currentState && isFinalNode;
//	}

	public String getName() {
		return name;
	}

	public Set<Node> getTriggers(String key) {
		return triggers.get(key);
	}

	public Set<Node> expand() {
		Set<Node> nextLevel = new HashSet<>();
		nextLevel.add(this);
		this.setState(true);

		Set<Node> triggered = triggers.get("$");
		if (triggered != null) {
			for (Node epsilonNodes : triggered) {
				if (epsilonNodes.isState() == false)
					nextLevel.addAll(epsilonNodes.expand());
			}
		}
		return nextLevel;
	}

	@Override
	public int compareTo(Node o) {
		return this.name.compareTo(o.getName());
	}

}
