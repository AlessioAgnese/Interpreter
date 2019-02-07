package parser;

import static parser.TokenType.*;

import parser.ast.*;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'var'? ID '=' Exp | 'print' Exp |  'for' ID ':' Exp '{' StmtSeq '}'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Add ('::' Exp)?
 Add ::= Mul ('+' Mul)*
 Mul::= Atom ('*' Atom)*
 Atom ::= '-' Atom | '[' ExpSeq ']' | NUM | ID | '(' Exp ')'

*/

public class StreamParser implements Parser {

	private final Tokenizer tokenizer;

	private void tryNext() throws ParserException {
		try {
			tokenizer.next();
		} catch (TokenizerException e) {
			throw new ParserException(e);
		}
	}

	private void match(TokenType expected) throws ParserException {
		final TokenType found = tokenizer.tokenType();
		if (found != expected)
			throw new ParserException(
					"Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
	}

	private void consume(TokenType expected) throws ParserException {
		match(expected);
		tryNext();
	}

	private void unexpectedTokenError() throws ParserException {
		throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
	}

	public StreamParser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public Prog parseProg() throws ParserException {
		tryNext(); // one look-ahead symbol
		Prog prog = new ProgClass(parseStmtSeq());
		match(EOF);
		return prog;
	}

	private StmtSeq parseStmtSeq() throws ParserException {
		Stmt stmt = parseStmt();
		if (tokenizer.tokenType() == STMT_SEP) {
			tryNext();
			return new MoreStmt(stmt, parseStmtSeq());
		}
		return new SingleStmt(stmt);
	}

	private ExpSeq parseExpSeq() throws ParserException {
		Exp exp = parseExp();
		if (tokenizer.tokenType() == EXP_SEP) {
			tryNext();
			return new MoreExp(exp, parseExpSeq());
		}
		return new SingleExp(exp);
	}

	private Stmt parseStmt() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case PRINT:
			return parsePrintStmt();
		case VAR:
			return parseVarStmt();
		case IDENT:
			return parseAssignStmt();
		case FOR:
			return parseForEachStmt();
		case IF:
			return parseIfStmt();
		case DO:
			return parseDoStmt();
		}
	}

	private PrintStmt parsePrintStmt() throws ParserException {
		consume(PRINT); // or tryNext();
		return new PrintStmt(parseExp());
	}

	private VarStmt parseVarStmt() throws ParserException {
		consume(VAR); // or tryNext();
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new VarStmt(ident, parseExp());
	}

	private AssignStmt parseAssignStmt() throws ParserException {
		Ident ident = parseIdent();
		consume(ASSIGN);
		return new AssignStmt(ident, parseExp());
	}

	private ForEachStmt parseForEachStmt() throws ParserException {
		consume(FOR); // or tryNext();
		Ident ident = parseIdent();
		consume(IN);
		Exp exp = parseExp();
		consume(OPEN_BLOCK);
		StmtSeq stmts = parseStmtSeq();
		consume(CLOSE_BLOCK);
		return new ForEachStmt(ident, exp, stmts);
	}
	
	private DoStmt parseDoStmt() throws ParserException{
		consume(DO);
		consume(OPEN_BLOCK);
		StmtSeq stmts=parseStmtSeq();
		consume(CLOSE_BLOCK);
		consume(WHILE);
		Exp exp=parseExp();
		return new DoStmt(stmts,exp);
	}

	private IfStmt parseIfStmt() throws ParserException {
		consume(IF);
		Exp exp=parseExp();
		consume(OPEN_BLOCK);
		StmtSeq stmts =parseStmtSeq();
		consume(CLOSE_BLOCK);
		if (tokenizer.tokenType() == ELSE) {
			tryNext();
			consume(OPEN_BLOCK);
			StmtSeq option=parseStmtSeq();
			consume(CLOSE_BLOCK);
			return new IfElseStmt(exp,stmts,option);
		}
		return new IfStmt(exp,stmts);
		
	}
	
	private Exp parseExp() throws ParserException {
		Exp exp = parseEqual();
		if (tokenizer.tokenType() == AND) {
			tryNext();
			exp = new And(exp, parseExp());
		}
		return exp;
	}
		
	
	private Exp parseEqual() throws ParserException {
		Exp exp = parsePrefix();
		while(tokenizer.tokenType() == COMPARE) {
			tryNext();
			exp=new Equal(exp,parsePrefix());
		}
		return exp;
	}
	private Exp parsePrefix() throws ParserException {
		Exp exp = parseAdd();
		while(tokenizer.tokenType() == PREFIX) {
			tryNext();
			exp= new Prefix(exp, parseAdd());
		}
		return exp;
	}
	
	
	private Exp parseAdd() throws ParserException {
		Exp exp = parseMul();
		while (tokenizer.tokenType() == PLUS) {
			tryNext();
			exp = new Add(exp, parseMul());
		}
		return exp;
	}

	private Exp parseMul() throws ParserException {
		Exp exp = parseAtom();
		while (tokenizer.tokenType() == TIMES) {
			tryNext();
			exp = new Mul(exp, parseAtom());
		}
		return exp;
	}

	private Exp parseAtom() throws ParserException {
		switch (tokenizer.tokenType()) {
		default:
			unexpectedTokenError();
		case BOOL:
			return parseBool();	
		case NUM:
			return parseNum();
		case IDENT:
			return parseIdent();
		case MINUS:
			return parseMinus();
		case OPEN_LIST:
			return parseList();
		case OPEN_PAR:
			return parseRoundPar();
		case NOT:
			return parseNot();
		case OPT:
			return parseOpt();
		case EMPTY:
			return parseEmpty();
		case DEF:
			return parseDef();
		case GET:
			return parseGet();
		}
	}
	
	private PrimLiteral<Boolean>  parseBool() throws ParserException {
		boolean val = tokenizer.boolValue();
		consume(BOOL);// or tryNext();
		return new BoolLiteral(val);
	}

	private IntLiteral parseNum() throws ParserException {
		int val = tokenizer.intValue();
		consume(NUM); // or tryNext();
		return new IntLiteral(val);
	}

	private Ident parseIdent() throws ParserException {
		String name = tokenizer.tokenString();
		consume(IDENT); // or tryNext();
		return new SimpleIdent(name);
	}

	private Sign parseMinus() throws ParserException {
		consume(MINUS); // or tryNext();
		return new Sign(parseAtom());
	}

	private ListLiteral parseList() throws ParserException {
		consume(OPEN_LIST); // or tryNext();
		ExpSeq exps = parseExpSeq();
		consume(CLOSE_LIST);
		return new ListLiteral(exps);
	}

	private Exp parseRoundPar() throws ParserException {
		consume(OPEN_PAR); // or tryNext();
		Exp exp = parseExp();
		consume(CLOSE_PAR);
		return exp;
	}
	
	private Exp parseNot() throws ParserException {
		consume(NOT);// or tryNext();
		return new Not(parseAtom());
	}
	
	private Exp parseOpt() throws ParserException {
		consume(OPT);// or tryNext();
		return new OptLiteral(parseAtom());
		
	}
	
	private Exp parseEmpty() throws ParserException {
		consume(EMPTY);// or tryNext();
		return new Empty(parseAtom());
	}
	
	private Exp parseDef() throws ParserException {
		consume(DEF);// or tryNext();
		return new Def(parseAtom());
	}
	
	private Exp parseGet() throws ParserException {
		consume(GET);// or tryNext();
		return new Get(parseAtom());
	}


}
