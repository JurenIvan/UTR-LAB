package hr.fer.zemris.utr.lab03;

import java.util.Objects;

public class Pair<F,S> {
	
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

