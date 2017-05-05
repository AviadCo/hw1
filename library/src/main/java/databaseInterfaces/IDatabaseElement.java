package databaseInterfaces;

/**
 * This is interface represent Pair element to be stored in database.
 * 
 * @author Aviad
 *
 */
public interface IDatabaseElement<Key extends Comparable<Key> & IStringable, Value extends IStringable> {
	Key getKey();
	
	Value getValue();
}