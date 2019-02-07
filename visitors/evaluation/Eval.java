package visitors.evaluation;

import environments.EnvironmentException;
import environments.GenEnvironment;
import parser.ast.Exp;
import parser.ast.ExpSeq;
import parser.ast.Ident;
import parser.ast.SimpleIdent;
import parser.ast.Stmt;
import parser.ast.StmtSeq;
import visitors.Visitor;
import java.io.PrintWriter;

public class Eval implements Visitor<Value> {

	private PrintWriter p;

	public Eval(PrintWriter p) {
		this.p = p;
	}
	private final GenEnvironment<Value> env = new GenEnvironment<>();

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
		} catch (EnvironmentException e) { // undefined variable
			throw new EvaluatorException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(Ident ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitForEachStmt(Ident ident, Exp exp, StmtSeq block) {
		ListValue list = exp.accept(this).asList();
		for (Value val : list) {
			env.enterLevel();
			env.dec(ident, val);
			block.accept(this);
			env.exitLevel();
		}
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		p.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(Ident ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
	}

	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public Value visitListLiteral(ExpSeq exps) {
		return exps.accept(this);
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
	}

	@Override
	public Value visitPrefix(Exp left, Exp right) {
		Value el = left.accept(this);
		return right.accept(this).asList().prefix(el);
	}

	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).asInt());
	}

	@Override
	public Value visitIdent(String name) {
		return env.lookup(new SimpleIdent(name));
	}

	// dynamic semantics of sequences of expressions
	// a list of values is returned by the visitor

	@Override
	public Value visitSingleExp(Exp exp) {
		return new ListValue(exp.accept(this), new ListValue());
	}

	@Override
	public Value visitMoreExp(Exp first, ExpSeq rest) {
		return new ListValue(first.accept(this), rest.accept(this).asList());
	}

	@Override
	public Value visitIfStmt(Exp exp, StmtSeq block) {
		if(exp.accept(this).asBool()) {
			env.enterLevel();
			block.accept(this);
			env.exitLevel();
		}
		return null;
	}

	@Override
	public Value visitIfElseStmt(Exp exp, StmtSeq block, StmtSeq option) {
		if(exp.accept(this).asBool()) {
			env.enterLevel();
			block.accept(this);
			env.exitLevel();
		}
		else{
			env.enterLevel();
			option.accept(this);
			env.exitLevel();
		}
		return null;
	}

	@Override
	public Value visitDoStmt(StmtSeq stmt, Exp exp) {
		do {
			env.enterLevel();
			stmt.accept(this);
			env.exitLevel();
		}while(exp.accept(this).asBool());
		return null;
	}

	@Override
	public Value visitBoolLiteral(Boolean value) {
		return new BoolValue(value);
	}

	@Override
	public Value visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).asBool() && right.accept(this).asBool());
	}

	@Override
	public Value visitEqual(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public Value visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).asBool());
	}

	@Override
	public Value visitEmpty(Exp exp) {
		Value aux=exp.accept(this);
		if(aux instanceof OptValue) {
			OptValue opt=new OptValue(aux.asOpt());
			opt.modifyEmpty();
			return opt;
		}
		throw new EvaluatorException("Expecting a opt value");
	}

	@Override
	public Value visitGet(Exp exp) {
		Value aux=exp.accept(this);
		if(aux instanceof OptValue) {
			if(!aux.asOpt().isEmpty())
				return aux.asOpt().getValue();
			throw new EvaluatorException("Expecting a opt value not a empty opt");
		}
		throw new EvaluatorException("Expecting a opt value");
	}

	@Override
	public Value visitDef(Exp exp) {
		Value aux=exp.accept(this);
		if(aux instanceof OptValue) {
			return new BoolValue(!aux.asOpt().isEmpty());
		}
		throw new EvaluatorException("Expecting a opt value");
	}

	@Override
	public Value visitOptLiteral(Exp exp) {
		return new OptValue(exp.accept(this));
	}

}

