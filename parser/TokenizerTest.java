package parser;

import static java.lang.System.out;
import static java.lang.System.err;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TokenizerTest {
	public static void main(String[] args) {
		try (Tokenizer t = new StreamTokenizer(
				args.length > 0 ? new FileReader(args[0]) : new InputStreamReader(System.in))) {
			while (t.hasNext()) {
				try {
					t.next();
					out.println(t.tokenString() + " of type " + t.tokenType());
					switch (t.tokenType()) {
					case NUM:
						out.println("value " + t.intValue());
						break;
					case EOF:
						out.println("end of stream");
					default:
					}
				} catch (TokenizerException e) {
					err.println(e.getMessage());
					if (e.getCause() != null && e.getCause().getCause() != null)
						System.exit(1);
				} catch (Throwable e) {
					err.println("Unexpected error. " + e.getMessage());
				}
			}
		} catch (FileNotFoundException e) {
			err.println(e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error. " + e.getMessage());
		}
	}
}
