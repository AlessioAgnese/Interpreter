package parser;

import static java.lang.System.out;
import static java.lang.System.err;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

enum LexType {
	IDENT, NUM, SKIP
}

public class ScannerTest {

	private static LexType checkType(Scanner sc) {
		for (LexType type : LexType.values())
			if (sc.group(type.ordinal() + 1) != null)
				return type;
		throw new AssertionError("This code should be unreachable!");
	}

	public static void main(String[] args) {
		final String regEx = "([a-zA-Z][a-zA-Z0-9]*)|(0|[1-9][0-9]*)|(\\s+|//.*)";
		try (StreamScanner sc = new StreamScanner(regEx,
				args.length > 0 ? new FileReader(args[0]) : new InputStreamReader(System.in))) {
			while (sc.hasNext())
				try {
					sc.next();
					out.println("\"" + sc.group() + "\"" + " of type " + checkType(sc));
				} catch (ScannerException e) {
					err.println(e.getMessage());
					if (e.getCause() != null)
						System.exit(1);
				} catch (Throwable e) {
					err.println("Unexpected error. " + e.getMessage());
				}
		} catch (FileNotFoundException e) {
			err.println(e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error. " + e.getMessage());
		}
	}
}
