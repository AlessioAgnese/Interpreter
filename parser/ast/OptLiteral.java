package parser.ast;

import visitors.Visitor;

public class OptLiteral  extends UnaryOp {

	public OptLiteral(Exp exp) {
		super(exp);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + exp + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitOptLiteral(exp);
	}

}
