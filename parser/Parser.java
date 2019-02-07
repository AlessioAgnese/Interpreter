package parser;

import parser.ast.Prog;

public interface Parser {

	Prog parseProg() throws ParserException;

}