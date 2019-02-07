package environments;

import parser.ast.Ident;

public interface Environment<T> {

	/* adds a new most nested scope level */

	void enterLevel();

	/* removes the most nested scope level */

	void exitLevel();

	/*
	 * lookups the value associated with id in the most nested scope level which
	 * defines it; throws an EnvironmentException if no scope could be found
	 */

	T lookup(Ident id);

	/*
	 * updates the most nested scope level which defines id by associating with it a
	 * new payload; throws EnvironmentException if no scope could be found
	 */

	T dec(Ident id, T info);

	/*
	 * updates the most nested scope level by associating id with payload; id and
	 * payload must be non-null; id is allowed to be already defined in the most
	 * nested scope level
	 */

	T update(Ident id, T payload);

}
