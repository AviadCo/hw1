package basicClassesFactory;

import databaseInterfaces.IStringableFactory;

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
