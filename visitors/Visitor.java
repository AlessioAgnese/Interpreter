package visitors;

import parser.ast.Exp;
import parser.ast.ExpSeq;
import parser.ast.Ident;
import parser.ast.Stmt;
import parser.ast.StmtSeq;

public interface Visitor<T> {
	T visitAdd(Exp left, Exp right);

	T visitAssignStmt(Ident ident, Exp exp);

	T visitForEachStmt(Ident ident, Exp exp, StmtSeq block);

	T visitIntLiteral(int value);

	T visitListLiteral(ExpSeq exps);

	T visitMoreExp(Exp first, ExpSeq rest);

	T visitMoreStmt(Stmt first, StmtSeq rest);

	T visitMul(Exp left, Exp right);

	T visitPrefix(Exp left, Exp right);

	T visitPrintStmt(Exp exp);

	T visitProg(StmtSeq stmtSeq);

	T visitSign(Exp exp);

	T visitIdent(String name);

	T visitSingleExp(Exp exp);

	T visitSingleStmt(Stmt stmt);

	T visitVarStmt(Ident ident, Exp exp);
	//---
	
	T visitIfStmt(Exp exp, StmtSeq block);

	T visitIfElseStmt(Exp exp, StmtSeq block, StmtSeq option);

	T visitDoStmt(StmtSeq stmt, Exp exp);

	T visitBoolLiteral(Boolean value);

	T visitAnd(Exp left, Exp right);

	T visitEqual(Exp left, Exp right);

	T visitNot(Exp exp);

	T visitEmpty(Exp exp);

	T visitGet(Exp exp);

	T visitDef(Exp exp);

	T visitOptLiteral(Exp exp);
}
