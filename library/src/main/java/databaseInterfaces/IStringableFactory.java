package databaseInterfaces;

public interface IStringableFactory<Value> {
	Value createObject(String s);
	String createString(Value v);
}
