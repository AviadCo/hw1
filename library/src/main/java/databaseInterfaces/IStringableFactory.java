package databaseInterfaces;

/**
 * This interface represents a factory of element.
 * The factory is able to create object from string and the string to object.
 * 
 * @author Aviad
 *
 * @param <ElementType> - The element type
 */
public interface IStringableFactory<ElementType> {
	/**
	 * 
	 * @param s  - String which represents the object form type ElementType
	 * @return - the element from type ElementType
	 */
	ElementType createObject(String s);
	
	/**
	 * 
	 * @param e  - the element from type ElementType
	 * @return - String which represents the object form type ElementType
	 */
	String createString(ElementType e);
}
