package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class ListLiteral implements Exp {
	private final ExpSeq exps;

	public ListLiteral(ExpSeq exps) {
		this.exps = requireNonNull(exps);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + exps + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitListLiteral(exps);
	}

}
