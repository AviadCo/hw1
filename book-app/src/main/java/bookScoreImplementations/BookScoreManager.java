package bookScoreImplementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import basicClasses.Book;
import basicClasses.ID;
import basicClasses.Review;
import basicClasses.Reviewer;
import basicClassesFactory.BookFactory;
import basicClassesFactory.IDFactory;
import basicClassesFactory.ReviewerFactory;
import databaseImplementations.Database;
import databaseImplementations.MapBasedStorageFactory;
import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IStringableFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import il.ac.technion.cs.sd.book.ext.LineStorageModule;

public class BookScoreManager implements BookScoreInitializer, BookScoreReader {
	
	private static final String BOOKS_DATA_BASE_NAME = "BOOKS_DATABASE";
	private static final String REVIEWERS_DATA_BASE_NAME = "REVIEWERS_DATABASE";
	
	@Inject
	private List<Book> createListOfBooks(List<Reviewer> reviewers)
	{
	  	Map<ID, Book> booksMap = new HashMap<ID, Book>();
		
	  	reviewers.stream().forEach(r -> {
	  		for(Review review : r.getReviewsList()) {
	  			Review bookReview = new Review(r.getKey(), review.getScore());
	  			Book updateBook = booksMap.containsKey(bookReview.getId()) ? 
	  					booksMap.get(review.getId()) : new Book(review.getId());
	  					
	  			updateBook.addReview(bookReview);
	  					
	            booksMap.put(updateBook.getKey(), updateBook);
	  		}
	  	});
	  	
	  	return new ArrayList<Book>(booksMap.values());
	}
	
	private List<Reviewer> removeReviewersDuplicates(List<Reviewer> reviewers)
	{
	  	Map<ID, Boolean> seen = new HashMap<ID, Boolean>();
		
	  	if (reviewers.isEmpty()) {
	  		/* stream dosen't handle empty array well */
	  		return reviewers;
	  	}
	  	
	  	List <Reviewer> reversedReviewers = reviewers.subList(0, reviewers.size());
		Collections.reverse(reversedReviewers);
		
		return reversedReviewers.stream()
				   .filter(r -> seen.putIfAbsent(r.getKey(), Boolean.TRUE) == null)
			       .collect(Collectors.toList());
	}
	
	@Override
	public void setup(String XmlData) {
		List<Book> books;
		List<Reviewer> reviewers;
		
		try {
			reviewers = BookScoreParser.createListOfReviewers(XmlData);
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
		
		reviewers = removeReviewersDuplicates(reviewers);
		
		books = createListOfBooks(reviewers);
		
		//TODO use injection
		Database<ID, Reviewer> booksDatabase = new Database<ID, Reviewer>(new MapBasedStorageFactory(), new IDFactory(), new ReviewerFactory(), REVIEWERS_DATA_BASE_NAME);
		
		booksDatabase.add(reviewers);
		
		System.out.print("Finished");
	}

	@Override
	public boolean gaveReview(String reviewerId, String bookId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OptionalDouble getScore(String reviewerId, String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReviewedBooks(String reviewerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getReviewers(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getReviewsForBook(String reviewerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OptionalDouble getAverageReviewScoreForBook(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

}
