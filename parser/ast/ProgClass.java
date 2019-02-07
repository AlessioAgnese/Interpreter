package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class ProgClass implements Prog {
	private final StmtSeq stmtSeq;

	public ProgClass(StmtSeq stmtSeq) {
		this.stmtSeq = requireNonNull(stmtSeq);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + stmtSeq + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitProg(stmtSeq);
	}
}
