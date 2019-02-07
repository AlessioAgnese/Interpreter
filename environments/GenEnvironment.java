package environments;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import parser.ast.Ident;

import static java.util.Objects.requireNonNull;

public class GenEnvironment<T> implements Environment<T> {

	private LinkedList<Map<Ident, T>> scopeLevels = new LinkedList<>();

	/*
	 * enter a new nested scope; private method shared by enterLevel() and the
	 * constructor GenEnvironment()
	 */
	private void addEmptyLevel() {
		scopeLevels.addFirst(new HashMap<>());
	}

	/* create an environment with just one empty scope */
	public GenEnvironment() {
		addEmptyLevel();
	}

	@Override
	public void enterLevel() {
		addEmptyLevel();
	}

	@Override
	public void exitLevel() {
		scopeLevels.removeFirst();
	}

	/*
	 * looks up the most nested scope level defining id; throws EnvironmentException
	 * if no scope could be found
	 */

	protected Map<Ident, T> resolve(Ident id) {
		for (Map<Ident, T> locEnv : scopeLevels)
			if (locEnv.containsKey(id))
				return locEnv;
		throw new EnvironmentException("Undeclared variable " + id.getName());
	}

	@Override
	public T lookup(Ident id) {
		return resolve(id).get(id);
	}

	/*
	 * updates map to associate id with payload; id and payload must be non-null
	 */

	private static <T> T updateLevel(Map<Ident, T> map, Ident id, T payload) {
		return map.put(requireNonNull(id), requireNonNull(payload));
	}

	@Override
	public T dec(Ident id, T payload) {
		Map<Ident, T> mostNestedScopeLevel = scopeLevels.get(0);
		return updateLevel(mostNestedScopeLevel, id, payload);
	}

	@Override
	public T update(Ident id, T payload) {
		Map<Ident, T> scopeLevelOfId = resolve(id);
		return updateLevel(scopeLevelOfId, id, payload);
	}

}
