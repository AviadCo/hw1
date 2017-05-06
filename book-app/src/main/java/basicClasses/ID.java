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
	
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ID && id.equals(((ID) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
