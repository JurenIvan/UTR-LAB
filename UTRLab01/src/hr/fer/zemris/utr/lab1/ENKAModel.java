package hr.fer.zemris.utr.lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ENKAModel {

	private Map<String, Node> nodes;
	private List<Node> listOfActiveNodes;
	private List<Node> listOfInitiallyActiveNodes;
//	private List<String> alphabet;

	public ENKAModel() {
		this.nodes = new HashMap<String, Node>();
		listOfActiveNodes = new LinkedList<>();
		listOfInitiallyActiveNodes = new ArrayList<>();
//		alphabet = new ArrayList<String>();
	}

	public void addNodes(String[] name) {
		for (int i = 0; i < name.length; i++) 
			nodes.put(name[i], new Node(name[i]));
	}
	
	//useless
	public void setAlphabet(String[] alphabetChars) {
//		for (int i = 0; i < alphabetChars.length; i++) {
//			alphabet.add(alphabetChars[i]);
//		}
	}

	public void setTrigger(String nodeThatTriggers, String key, String nodeToTrigger) {
		if (!nodeToTrigger.equals("#")) {
			Node node = nodes.get(nodeThatTriggers);
			node.setNodeTrigger(key, nodes.get(nodeToTrigger));
		}
	}

	public void setInitialState(String[] initiallySet) {
		Node nm;
		for (String nodeName : initiallySet) {
			nm = nodes.get(nodeName);
			nm.setState(true);
			listOfInitiallyActiveNodes.add(nm);
		}
		reset();
	}

	public void reset() {
		for (String nodeToClear : nodes.keySet()) 
			nodes.get(nodeToClear).setState(false);

		listOfActiveNodes = new LinkedList<Node>(listOfInitiallyActiveNodes);
		
		for (Node nodeToClear : listOfActiveNodes) 
			nodeToClear.setState(true);
	}

	public String getActiveNodes(boolean first) {
		StringBuilder sb = new StringBuilder();
		if (listOfActiveNodes.size() == 0) {
			if (first == true) {
				return "#";
			}
			return "|#";
		}
		
		listOfActiveNodes.sort(null);
		
		if(first==false) 
			sb.append("|");
		
		
		for (int i = 0; i < listOfActiveNodes.size() - 1; i++) {
			sb.append(listOfActiveNodes.get(i).getName());
			sb.append(",");
		}
		
		sb.append(listOfActiveNodes.get(listOfActiveNodes.size() - 1).getName());
		return sb.toString();
	}
	
	//useless
	public void setFinalStates(String[] finishNode) {
//		for (String e : finishNode) {
//			nodes.get(e).setFinalNode(true);
//		}
	}

	// -----------------------------------------

	public String reDo(String[] inputToken) {
		StringBuilder sb=new StringBuilder();
		reset();
		eExpansion();
		sb.append(getActiveNodes(true));
		for (String token : inputToken) {
			Set<Node> activeNextStep = new HashSet<Node>();
			Set<Node> triggeredSet = new HashSet<Node>();

			for (Node activenode : listOfActiveNodes) {
				triggeredSet = activenode.getTriggers(token);
				activenode.setState(false);
				if (triggeredSet != null) {	
					activeNextStep.addAll(triggeredSet);
				}
			}
			
			listOfActiveNodes.clear();
			for (Node nodeToExpand : activeNextStep) {
				nodeToExpand.setState(true);
				listOfActiveNodes.addAll(nodeToExpand.expand());
			}
			for (Node nodeToSetPositive : listOfActiveNodes) {
				nodeToSetPositive.setState(true);
			}
			eExpansion();

			sb.append(getActiveNodes(false));
		}
		return sb.toString();
	}

	private void eExpansion() {
		Set<Node> maybeExpand = new HashSet<>(listOfActiveNodes);
		listOfActiveNodes.clear();
		for (Node node : maybeExpand) {
			listOfActiveNodes.addAll(node.expand());
			
		}
	}

}
