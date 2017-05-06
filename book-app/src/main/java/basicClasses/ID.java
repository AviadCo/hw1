package basicClasses;

import databaseInterfaces.IStringable;

public class ID implements Comparable<ID>, IStringable{

	String id;
	
	public ID(String id) {
		this.id = id;
	}

	@Override
	public String parseObjectToString() {
		return id;
	}

	@Override
	public IStringable parseObjectFromString(String s) {
		return new ID(s);
	}

	@Override
	public int compareTo(ID other) {
		return id.compareTo(other.id);
	}

}
