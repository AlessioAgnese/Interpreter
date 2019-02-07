package parser.ast;

import static java.util.Objects.requireNonNull;

import visitors.Visitor;

public class IfElseStmt extends IfStmt{
	
	
	private final StmtSeq option;
	
	public IfElseStmt( Exp exp, StmtSeq block,StmtSeq option) {
		super(exp,block);
		this.option=requireNonNull(option);
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "if (" + exp + "){" + block + "}({"+ option + "})";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitIfElseStmt( exp, block,option);
	} 
}
