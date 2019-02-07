package visitors.evaluation;

public interface Value {
	/* default conversion methods */
	default int asInt() {
		throw new EvaluatorException("Expecting an integer value");
	}

	default String asString() {
		throw new EvaluatorException("Expecting a string value");
	}
	
	default ListValue asList() {
		throw new EvaluatorException("Expecting a list value");
	}
	//aggiunto
	default boolean asBool() {
		throw new EvaluatorException("Expecting a boolean value");
		}
	default boolean isEmpty() {
		throw new EvaluatorException("Expecting a opt value");
		}
	default void modifyEmpty() {
		throw new EvaluatorException("Expecting a opt value");
		}

	default OptValue asOpt() {
		throw new EvaluatorException("Expecting a opt value");
		};
	

}
