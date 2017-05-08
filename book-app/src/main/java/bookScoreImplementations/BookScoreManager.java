package bookScoreImplementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.google.inject.Inject;

import basicClasses.DatabaseElement;
import basicClasses.Review;
import databaseImplementations.Database;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;

/**
 * This class implements BookScoreInitializer & BookScoreReader using LineStorageFactory
 * 
 * @author Aviad
 *
 */
public class BookScoreManager implements BookScoreInitializer, BookScoreReader {
	
	private static final String BOOKS_DATA_BASE_NAME = "BOOKS_DATABASE";
	private static final String REVIEWERS_DATA_BASE_NAME = "REVIEWERS_DATABASE";
	
	@Inject Database<String, DatabaseElement> reviewersDatabase = createDatabase(REVIEWERS_DATA_BASE_NAME);
	@Inject Database<String, DatabaseElement> booksDatabase = createDatabase(BOOKS_DATA_BASE_NAME);
	
	public Database<String, DatabaseElement> createDatabase(String databaseName) {
		return null;
	}
	
	/**
	 * 
	 * @param reviewers - get list of reviewers from parser
	 * @return - list of books. each book has id and list of reviews
	 */
	private List<DatabaseElement> createListOfBooks(List<DatabaseElement> reviewers)
	{
	  	Map<String, DatabaseElement> booksMap = new HashMap<String, DatabaseElement>();
		
	  	reviewers.stream().forEach(r -> {
	  		for(Review review : r.getReviewslist()) {
	  			Review bookReview = new Review(r.getKey(), review.getScore());
	  			DatabaseElement updateBook = booksMap.containsKey(bookReview.getId()) ? 
	  					booksMap.get(review.getId()) : new DatabaseElement(review.getId());
	  					
	  			updateBook.addReview(bookReview);
	  					
	            booksMap.put(updateBook.getKey(), updateBook);
	  		}
	  	});
	  	
	  	return new ArrayList<DatabaseElement>(booksMap.values());
	}
	
	/**
	 * Gets xmlData of Reviewer with reviewer, parse it and initialize LineStorage database.
	 */
	@Override
	public void setup(String XmlData) {
		List<DatabaseElement> books;
		List<DatabaseElement> reviewers;
		
		try {
			reviewers = BookScoreParser.createListOfReviewers(XmlData);
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
				
		books = createListOfBooks(reviewers);
						
		reviewersDatabase.add(reviewers);
		booksDatabase.add(books);
	}
	
	private OptionalInt getScore(String reviewerId, String bookId, Database<String, DatabaseElement> database)
	{
		Optional<DatabaseElement> element = database.findElementByID(reviewerId);
		
		if (!element.isPresent()) {
			/* No such element */
			return OptionalInt.empty();
		}
				
		Optional<Review> review = element.get().getReviewslist()
				.stream()
				.filter(r -> r.getId().equals(bookId))
				.findAny();
		
		if (!review.isPresent()) {
			/* The reviewer didn't give review on the given book */
			return OptionalInt.empty();
		}
		
		return OptionalInt.of(review.get().getScore());
	}
	
	private Map<String, Integer> getAllReviewsOfElement(String elementId, Database<String, DatabaseElement> database) {
		Optional<DatabaseElement> element = database.findElementByID(elementId);
		Map<String, Integer> reviewsMap = new HashMap<String, Integer>();

		if (!element.isPresent()) {
			/* No such element */
			return reviewsMap;
		}
		
		element.get()
				.getReviewslist()
				.stream()
				.forEach( r-> reviewsMap.put(r.getId(), r.getScore()));

		return reviewsMap;
	}
	
	private OptionalDouble getAverageReviewsOfElement(String elementId, Database<String, DatabaseElement> database) {
		Optional<DatabaseElement> element = database.findElementByID(elementId);
		
		if (!element.isPresent()) {
			/* No such element */
			return OptionalDouble.empty();
		}
		
		Integer sum = getReviewsForBook(elementId)
										.values()
										.stream()
										.mapToInt(score -> score)
										.sum();
				
		return OptionalDouble.of((double) sum / element.get().getReviewslist().size());
	}
	
	private List<String> getElementList(String elementId, Database<String, DatabaseElement> database) {
		List<String> elementList = new ArrayList<String>();
		
		elementList.addAll(getAllReviewsOfElement(elementId, database).keySet());
		Collections.sort(elementList);
		
		return elementList;
	}
	
	@Override
	public boolean gaveReview(String reviewerId, String bookId) {
		return getScore(reviewerId, bookId) != OptionalDouble.empty();
	}

	@Override
	public OptionalDouble getScore(String reviewerId, String bookId) {
		OptionalInt score = getScore(reviewerId, bookId, reviewersDatabase);
		
		if (!score.isPresent()) {
			return OptionalDouble.empty();
		}
				
		return OptionalDouble.of(score.getAsInt());
	}

	@Override
	public List<String> getReviewedBooks(String reviewerId) {
		return getElementList(reviewerId, reviewersDatabase);
	}

	@Override
	public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
		return getAllReviewsOfElement(reviewerId, reviewersDatabase);
	}

	@Override
	public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
		return getAverageReviewsOfElement(reviewerId, reviewersDatabase);
	}

	@Override
	public List<String> getReviewers(String bookId) {
		return getElementList(bookId, booksDatabase);
	}

	@Override
	public Map<String, Integer> getReviewsForBook(String reviewerId) {
		return getAllReviewsOfElement(reviewerId, booksDatabase);
	}

	@Override
	public OptionalDouble getAverageReviewScoreForBook(String bookId) {
		return getAverageReviewsOfElement(bookId, booksDatabase);
	}
}
