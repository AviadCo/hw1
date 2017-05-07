package basicClassesFactory;

import databaseInterfaces.IStringableFactory;
import basicClasses.ID;

public class IDFactory implements IStringableFactory<ID> {

	@Override
	public ID create(String s) {
		return new ID(s);
	}

}
