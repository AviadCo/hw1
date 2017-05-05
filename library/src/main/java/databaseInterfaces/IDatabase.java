package databaseInterfaces;

import java.util.List;
import java.util.Optional;

/**
 * This is interface represent Storage for elements.
 * The elements must implement IDatabaseElement interface.
 * 
 * @author Aviad
 *
 */
public interface IDatabase<Key extends Comparable<Key> & IStringable, Value extends IStringable> {	
	/**
	 * Returns the number of elements in database
	 * 
	 * @return - number of elements in database
	 */
	Integer getNumberOfElements();

	/**
	 * Add multiple elements at once
	 * 
	 * Do not add multiple elements more than once since the database sorting won't be maintained
	 * 
	 * @param students - list of elements to add
	 */
	void add(List<IDatabaseElement<Key, Value>> elements);

	/**
	 * findStudentByID - returns element by it's key using binary search algorithm.
	 * 
	 * If element dosen't exist returns Optional.empty().
	 *  */
	Optional<Value> findElementByID(Key key);
}
