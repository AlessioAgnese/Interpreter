package visitors.evaluation;

import java.util.Iterator;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

public class ListValue implements Value, Iterable<Value> {

	private final LinkedList<Value> list = new LinkedList<>();

	public ListValue() {
	}

	public ListValue(ListValue otherList) {
		for (Value el : otherList)
			list.add(el);
	}

	public ListValue(Value val, ListValue tail) {
		this(tail);
		list.addFirst(requireNonNull(val));
	}

	@Override
	public Iterator<Value> iterator() {
		return list.iterator();
	}

	public ListValue prefix(Value el) {
		ListValue res = new ListValue(this);
		res.list.addFirst(requireNonNull(el));
		return res;
	}

	@Override
	public ListValue asList() {
		return this;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ListValue))
			return false;
		return list.equals(((ListValue) obj).list);
	}
}
