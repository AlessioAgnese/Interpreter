package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class ForEachStmt implements Stmt {
	private final Ident ident;
	private final Exp exp;
	private final StmtSeq block;

	public ForEachStmt(Ident id, Exp exp, StmtSeq block) {
		this.ident = requireNonNull(id);
		this.exp = requireNonNull(exp);
		this.block = requireNonNull(block);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + ident + "," + exp + "," + block + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitForEachStmt(ident, exp, block);
	}
}
