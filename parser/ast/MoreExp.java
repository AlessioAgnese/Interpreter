package parser.ast;

import visitors.Visitor;

public class MoreExp extends More<Exp, ExpSeq> implements ExpSeq {

	public MoreExp(Exp first, ExpSeq rest) {
		super(first, rest);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitMoreExp(first, rest);
	}
}
