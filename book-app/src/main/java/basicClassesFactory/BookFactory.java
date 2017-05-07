package basicClassesFactory;

import basicClasses.Book;
import databaseInterfaces.IStringableFactory;

public class BookFactory implements IStringableFactory<Book> {

	@Override
	public Book create(String s) {
		return (Book) new Book("").parseObjectFromString(s);
	}
}
