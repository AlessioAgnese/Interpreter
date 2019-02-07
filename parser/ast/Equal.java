package parser.ast;
import visitors.Visitor;

public class Equal extends BinaryOp{
	
	public Equal(Exp left, Exp right){
		super(left, right);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitEqual(left, right);
	}
}
