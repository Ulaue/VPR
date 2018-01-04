package application.party;

/**
 * If this interface is implemented, it will generate a public method addCompartment().<br>
 * However if this interface is used directly like new Compartment(){} the method addCompartment() will me implemented inside the curly brackets.
 *
 * @author Daniel Hecker : 16.12.2017
 */
public interface Compartment<O extends Object>
{
	/**
	 * This method let's you add a new Compartment to your JavaFX application.
	 *
	 * @return This method returns a P extends Pane
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	public O addCompartment();

	/**
	 * This method let's you add a new Compartment directly on a P.
	 *
	 * @param comp A new Compartment(){} is needed.
	 * @param <T> An Object type T that shall be returned
	 * @return T that contains a Compartment
	 *
	 * @author Daniel Hecker : 16.12.2017
	 */
	public static <T> T newCompartment(Compartment<T> comp)
	{
		return comp.addCompartment();
	}
}
