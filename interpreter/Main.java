package interpreter;

import static java.lang.System.err;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import parser.Parser;
import parser.ParserException;
import parser.StreamParser;
import parser.StreamTokenizer;
import parser.Tokenizer;
import parser.ast.Prog;
import visitors.evaluation.Eval;
import visitors.evaluation.EvaluatorException;
import visitors.typechecking.TypeCheck;
import visitors.typechecking.TypecheckerException;

public class Main {
	public static void main(String[] args) {
		boolean fileIn = false, fileOut = false;
		String in = null, out = null;
          for (int i = 0; i < args.length; i++) {
              if (args[i].equals("-i")) {
            	  in = new String(args[++i]);
            	  fileIn=true;
              }
              if (args[i].equals("-o")) {
            	  out = new String(args[++i]);
            	  fileOut=true;
              }
          }
          try (Tokenizer tokenizer = (Tokenizer) new StreamTokenizer(fileIn ? new FileReader(in) : new InputStreamReader(System.in));
        		  PrintWriter p = (fileOut)? new PrintWriter(out): new PrintWriter(System.out, true)) {
        Parser parser = new StreamParser(tokenizer);
        Prog prog = parser.parseProg();
  		prog.accept(new TypeCheck());
        prog.accept(new Eval(p));
		} catch (ParserException e) {
			err.println("Syntax error: " + e.getMessage());
		} catch (IOException e) {
			err.println("I/O error: " + e.getMessage());
		} catch (TypecheckerException e) {
			err.println("Static error: " + e.getMessage());
		} catch (EvaluatorException e) {
			err.println("Dynamic error: " + e.getMessage());
		} catch (Throwable e) {
			err.println("Unexpected error.");
			e.printStackTrace();
		}
	}
}
