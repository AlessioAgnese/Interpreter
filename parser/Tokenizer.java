package parser;

public interface Tokenizer extends AutoCloseable {

	TokenType next() throws TokenizerException;

	String tokenString();

	int intValue();

	TokenType tokenType();

	boolean hasNext();

	public void close() throws TokenizerException;
	
	Boolean boolValue();

}