package visitors.typechecking;

import static visitors.typechecking.PrimtType.*;

import environments.EnvironmentException;
import environments.GenEnvironment;
import parser.ast.Exp;
import parser.ast.ExpSeq;
import parser.ast.Ident;
import parser.ast.SimpleIdent;
import parser.ast.Stmt;
import parser.ast.StmtSeq;
import visitors.Visitor;

public class TypeCheck implements Visitor<Type> {

	private final GenEnvironment<Type> env = new GenEnvironment<>();

	private void checkBinOp(Exp left, Exp right, Type type) {
		type.checkEqual(left.accept(this));
		type.checkEqual(right.accept(this));
	}

	// static semantics for programs; no value returned by the visitor

	@Override
	public Type visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new TypecheckerException(e);
		}
		return null;
	}

	// static semantics for statements; no value returned by the visitor

	@Override
	public Type visitAssignStmt(Ident ident, Exp exp) {
		Type found = env.lookup(ident);
		found.checkEqual(exp.accept(this));
		return null;
	}

	@Override
	public Type visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		Type ty = exp.accept(this).getListElemType();
		env.enterLevel();
		env.dec(ident, ty);
		block.accept(this);
		env.exitLevel();
		return null;
	}

	@Override
	public Type visitPrintStmt(Exp exp) {
		exp.accept(this);
		return null;
	}

	@Override
	public Type visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// static semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Type visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Type visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// static semantics of expressions; a type is returned by the visitor

	@Override
	public Type visitAdd(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitIntLiteral(int value) {
		return INT;
	}

	@Override
	public Type visitListLiteral(ExpSeq exps) {
		return new ListType(exps.accept(this));
	}

	@Override
	public Type visitMul(Exp left, Exp right) {
		checkBinOp(left, right, INT);
		return INT;
	}

	@Override
	public Type visitPrefix(Exp left, Exp right) {
		Type elemType = left.accept(this);
		return new ListType(elemType).checkEqual(right.accept(this));
	}

	@Override
	public Type visitSign(Exp exp) {
		return INT.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// static semantics of sequences of expressions
	// a type is returned by the visitor

	@Override
	public Type visitSingleExp(Exp exp) {
		return exp.accept(this);
	}

	@Override
	public Type visitMoreExp(Exp first, ExpSeq rest) {
		Type found = first.accept(this);
		return found.checkEqual(rest.accept(this));
	}
	
	@Override
	public Type visitIfStmt(Exp exp, StmtSeq block) {
		BOOL.checkEqual(exp.accept(this));
		env.enterLevel();
		block.accept(this);
		env.exitLevel();
		return null;
	}
	@Override
	public Type visitIfElseStmt(Exp exp, StmtSeq block, StmtSeq option) {
		BOOL.checkEqual(exp.accept(this));
		env.enterLevel();
			block.accept(this);
		env.exitLevel();
		env.enterLevel();
			option.accept(this);
		env.exitLevel();
		return null;
	}

	@Override
	public Type visitDoStmt(StmtSeq stmt, Exp exp) {
		BOOL.checkEqual(exp.accept(this));
		env.enterLevel();
			stmt.accept(this);
		env.exitLevel();
		return null;
		
	}
	@Override
	public Type visitBoolLiteral(Boolean value) {
		return BOOL;
	}

	@Override
	public Type visitAnd(Exp left, Exp right) {
		checkBinOp(left, right, BOOL);
		return BOOL;
	}

	@Override
	public Type visitEqual(Exp left, Exp right) {
		Type expected = right.accept(this);
		left.accept(this).checkEqual(expected);
		return BOOL;
	}

	@Override
	public Type visitNot(Exp exp) {
		return BOOL.checkEqual(exp.accept(this));
	}

	@Override
	public Type visitEmpty(Exp exp) {
		Type expected=exp.accept(this);
		expected.getOptElemType();
		return expected;
	}

	@Override
	public Type visitOptLiteral(Exp exp) {
		return new OptType(exp.accept(this));
	}

	@Override
	public Type visitGet(Exp exp) {
		Type expected=exp.accept(this);
		return expected.getOptElemType();
		
	}

	@Override
	public Type visitDef(Exp exp) {
		exp.accept(this).getOptElemType();
		return BOOL;
	}

	

}

