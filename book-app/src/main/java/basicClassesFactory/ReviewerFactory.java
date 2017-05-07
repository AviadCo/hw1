package basicClassesFactory;

import basicClasses.Reviewer;
import databaseInterfaces.IStringableFactory;

public class ReviewerFactory implements IStringableFactory<Reviewer> {

	@Override
	public Reviewer create(String s) {
		return (Reviewer) new Reviewer("").parseObjectFromString(s);
	}
}
