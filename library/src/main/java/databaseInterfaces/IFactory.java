package databaseInterfaces;

public interface IFactory<Value> {
	Value create(String s);
}
