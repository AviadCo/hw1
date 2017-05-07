package basicClassesFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import basicClasses.Book;
import basicClasses.Review;
import databaseInterfaces.IStringableFactory;

public class BookFactory implements IStringableFactory<Book> {
	
	private final static String REVIEWER_SPLITER = ",";

	@Override
	public Book createObject(String s) {
		List<Review> reviewslist = new ArrayList<>();
		String[] bookString = s.split(REVIEWER_SPLITER);
				
		for (String review : Arrays.copyOfRange(bookString, 1, bookString.length)) {			
			reviewslist.add(new Review(review));
		}
		
		return new Book(bookString[0], reviewslist);
	}

	@Override
	public String createString(Book b) {
		String bookString = b.getId();
		
		for (Review review : b.getReviewslist()) {
			bookString += REVIEWER_SPLITER + review.parseReviewToString();
		}
		
		return bookString;
	}
}
