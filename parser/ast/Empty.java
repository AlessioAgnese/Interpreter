package parser.ast;

import visitors.Visitor;

public class Empty  extends UnaryOp {

	public Empty(Exp exp) {
		super(exp);
	}


	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitEmpty(exp);
	}
}