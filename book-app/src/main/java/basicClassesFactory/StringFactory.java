package basicClassesFactory;

import databaseInterfaces.IStringableFactory;

/**
 * This class is a factory for String elements.
 * The class implements IStringableFactory factory
 * 
 * @author Aviad
 *
 */
public class StringFactory implements IStringableFactory<String> {

	@Override
	public String createObject(String s) {
		return s;
	}

	@Override
	public String createString(String v) {
		return v;
	}

}
