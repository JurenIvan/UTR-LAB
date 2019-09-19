package hr.fer.zemris.utr.lab2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public  class DKAModel {

	private Map<String, Node> nodes;
	private List<Node> listOfNodes;
	private List<Node> listOfInitiallyActiveNodes;
	private List<Node> listOfFinalNodes;
	private List<String> alphabet;
	private boolean[][] flags;
	private boolean[][] flagsVisites;

	public DKAModel() {
		this.nodes = new HashMap<String, Node>();
		this.listOfNodes = new ArrayList<>();
		this.listOfInitiallyActiveNodes = new ArrayList<>();
		this.alphabet = new ArrayList<String>();
		this.listOfFinalNodes = new LinkedList<Node>();
	}

	public void addNodes(String[] name) {
		for (int i = 0; i < name.length; i++) {
			Node newOne = new Node(name[i].trim());
			nodes.put(name[i].trim(), newOne);
			listOfNodes.add(newOne);
		}
	}

	public void setAlphabet(String[] alphabetChars) {
		for (int i = 0; i < alphabetChars.length; i++)
			alphabet.add(alphabetChars[i]);
		alphabet.sort(null);
	}

	public void setTrigger(String nodeThatTriggers, String key, String nodeToTrigger) {
		if (!nodeToTrigger.equals("#")) {
			Node node = nodes.get(nodeThatTriggers);
			node.setNodeTrigger(key, nodes.get(nodeToTrigger));
		}
	}

	public void setInitialState(String[] initiallySet) {
		Node node;
		for (String nodeName : initiallySet) {
			node = nodes.get(nodeName);
			listOfInitiallyActiveNodes.add(node);
		}
	}

	public void setFinalStates(String[] finishNode) {
		for (String e : finishNode) {
			if (e.isBlank())
				break;
			nodes.get(e).setFinalNode(true);
			listOfFinalNodes.add(nodes.get(e));
		}
	}

	public String minimise() {

		this.getRidOfUseless();
		flags = new boolean[listOfNodes.size()][listOfNodes.size()];
		this.solveEqualState();

		this.getRidOfSame();
		this.getRidOfUseless();

		// svaki node iz listNode->sredi mu koga tocno triggera i sredi hashmap nodes
		return getOutputText(getProductions());
	}

	private String getOutputText(List<String> productions2) {
		StringBuilder sb = new StringBuilder();
		// states of DKA
		for (var e : listOfNodes) {
			sb.append(e.getName());
			sb.append(",");
		}
		if (listOfNodes.size() != 0)
			sb.setLength(sb.length() - 1);
		sb.append("\n");

		// alphabet
		for (var e : alphabet) {
			sb.append(e);
			sb.append(",");
		}
		if (alphabet.size() != 0)
			sb.setLength(sb.length() - 1);
		sb.append("\n");

		// final states
		for (var e : listOfFinalNodes) {
			if (listOfNodes.contains(e)) {
				sb.append(e.getName());
				sb.append(",");
			}
		}
		if (listOfFinalNodes.size() != 0)
			sb.setLength(sb.length() - 1);
		sb.append("\n");

		// initial states
		for (var e : listOfInitiallyActiveNodes) {
			if (listOfNodes.contains(e)) {
				sb.append(e.getName());
				sb.append(",");
			}
		}
		if (listOfInitiallyActiveNodes.size() != 0)
			sb.setLength(sb.length() - 1);
		sb.append("\n");

		// productions
		for (var e : productions2) {
			sb.append(e);
			sb.append("\n");
		}
		return sb.toString();
	}

	private List<String> getProductions() {
		Set<String> productions = new HashSet<String>();
		for (var e : listOfNodes)
			for (var e2 : e.productions())
				productions.add(e2);

		List<String> produkcije = new ArrayList<String>(productions);
		produkcije.sort(null);
		return produkcije;
	}

	private void getRidOfSame() {
		List<Node> nodesToBeDeleted = new ArrayList<>();
		for (int i = 0; i < listOfNodes.size(); i++) {
			for (int j = 0; j < listOfNodes.size(); j++) {
				if (!flags[i][j] && i != j) {
					if (j > i) {
						int temp = j;
						j = i;
						i = temp;
					}
					Node nj = listOfNodes.get(j);
					listOfNodes.get(i).setName(nj.getName());
					nodesToBeDeleted.add(listOfNodes.get(i));
				}
			}
		}

		for (var e : nodesToBeDeleted)
			listOfNodes.remove(e);

		takeCareOfList(listOfInitiallyActiveNodes);
		takeCareOfList(listOfFinalNodes);
	}

	private void solveEqualState() {
		this.initialFill();
		for (int i = 0; i < listOfNodes.size(); i++) {
			for (int j = 0; j < listOfNodes.size(); j++) {
				check(i, j);
				flagsVisites = new boolean[listOfNodes.size()][listOfNodes.size()];
			}
		}
	}

	private void initialFill() {
		flagsVisites = new boolean[listOfNodes.size()][listOfNodes.size()];
		for (var elem : listOfFinalNodes) {
			int index = listOfNodes.indexOf(elem);
			for (int i = 0; i < listOfNodes.size(); i++) {
				flags[index][i] = flags[i][index] = true;
			}
		}
		for (var e1 : listOfFinalNodes)
			for (var e2 : listOfFinalNodes)
				flags[listOfNodes.indexOf(e1)][listOfNodes.indexOf(e2)] = false;
	}

	private boolean check(int i, int j) {
		if (i == j)
			return false;
		if (flags[i][j])
			return true;
		if (flagsVisites[i][j])
			return false;
		flagsVisites[i][j] = true;

		Node a = listOfNodes.get(i);
		Node b = listOfNodes.get(j);

		for (var character : alphabet) {
			Node ta = a.getTriggers(character);
			Node tb = b.getTriggers(character);
			int ita = listOfNodes.indexOf(ta);
			int itb = listOfNodes.indexOf(tb);
			if (flags[ita][itb])
				return flags[i][j] = true;
		}

		for (var character : alphabet) {
			Node ta = a.getTriggers(character);
			Node tb = b.getTriggers(character);
			int ita = listOfNodes.indexOf(ta);
			int itb = listOfNodes.indexOf(tb);
			if (check(ita, itb))
				return flags[i][j] = true;
		}
		return false;
	}

	private void getRidOfUseless() {
		Set<Node> reachable = new HashSet<Node>(listOfInitiallyActiveNodes);
		Set<Node> newReach = new HashSet<Node>(listOfInitiallyActiveNodes);
		Set<Node> temp = new HashSet<Node>();
		int rsize;

		do {
			rsize = reachable.size();
			temp.clear();
			for (Node e : newReach)
				temp.addAll(e.getAllTriggers());
			reachable.addAll(temp);
			newReach = new HashSet<Node>(temp);
		} while (rsize != reachable.size());

		nodes.clear();
		listOfNodes.clear();

		for (Node elem : reachable) {
			nodes.put(elem.getName(), elem);
			listOfNodes.add(elem);
		}

		listOfNodes.sort(null);
		takeCareOfList(listOfFinalNodes);
		takeCareOfList(listOfInitiallyActiveNodes);
	}

	private void takeCareOfList(List<Node> list) {
		Set<Node> tempFinal = new HashSet<Node>(list);
		list.clear();
		for (Node elem : tempFinal) {
			if (listOfNodes.contains(elem))
				list.add(elem);
		}
		list.sort(null);
	}
}