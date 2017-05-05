package databaseInterfaces;

/**
 * This is interface represent Object which can be serialized to string and back.
 * 
 * @author Aviad
 *
 */
public interface IStringable {
	/**
	 * @return - string which represents the object
	 */
	String parseObjectToString();
	
	/**
	 * 
	 * @param - string which represents Object
	 * @return - the parsed Object from string
	 */
	IStringable parseObjectFromString(String s);
}
