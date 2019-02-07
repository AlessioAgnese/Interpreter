package parser.ast;
import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class IfStmt implements Stmt{
	
	protected final Exp exp;
	protected final StmtSeq block;
	
	public IfStmt( Exp exp, StmtSeq block) {
		this.exp = requireNonNull(exp);
		this.block = requireNonNull(block);
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "if (" + exp + "){" + block + "}";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIfStmt( exp, block);
	}
}
