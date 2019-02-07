package visitors.typechecking;

import static java.util.Objects.requireNonNull;

public class ListType implements Type {

	private final Type elemType;

	public static final String TYPE_NAME = "LIST";

	public ListType(Type elemType) {
		this.elemType = requireNonNull(elemType);
	}

	public Type getElemType() {
		return elemType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ListType))
			return false;
		ListType lt = (ListType) obj;
		return elemType.equals(lt.elemType);
	}

	@Override
	public int hashCode() {
		return 31 * elemType.hashCode();
	}

	@Override
	public String toString() {
		return elemType + " " + TYPE_NAME;
	}
}
