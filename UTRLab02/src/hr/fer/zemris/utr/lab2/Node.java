package hr.fer.zemris.utr.lab2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public  class Node implements Comparable<Node> {

	private String name;
	private HashMap<String, Node> triggers;
	@SuppressWarnings("unused")
	private boolean currentState;
	@SuppressWarnings("unused")
	private boolean isFinalNode; 

	public Node(String name) {
		this.name = name;
		this.isFinalNode = false; 
		this.currentState = false;
		triggers = new HashMap<String, Node>();
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public Set<Node> getAllTriggers() {
		return new HashSet<Node>(triggers.values());
	}

	public void setNodeTrigger(String key, Node nodeModel) {
		triggers.put(key, nodeModel);
	}

	public void setState(boolean state) {
		this.currentState = state;
	}

	public void setFinalNode(boolean isFinalNode) {
		this.isFinalNode = isFinalNode;
	}

	public String getName() {
		return name;
	}

	public Node getTriggers(String key) {
		return triggers.get(key);
	}

	@Override
	public int compareTo(Node o) {
		return this.name.compareTo(o.getName());
	}
	
	public String[] productions() {
		String[] productions =new String[triggers.size()];
		int counter=0;
		for(String elem:triggers.keySet()) 
			productions[counter++]=this.name+","+elem+"->"+triggers.get(elem).getName();
		
		return productions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		return Objects.equals(name, other.name);
	}

}