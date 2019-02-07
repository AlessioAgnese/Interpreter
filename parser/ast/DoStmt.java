package parser.ast;
import static java.util.Objects.requireNonNull;

import visitors.Visitor;
public class DoStmt implements Stmt {

	StmtSeq stmt;
	Exp exp;
	public DoStmt(StmtSeq stmts, Exp exp) {
		this.stmt=requireNonNull(stmts);
		this.exp=requireNonNull(exp);
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "do{" + stmt +"}while("+ exp +")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitDoStmt(stmt,exp);
	} 

}
