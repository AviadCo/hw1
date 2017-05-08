package bookScoreImplementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import basicClasses.DatabaseElement;
import basicClasses.Review;
import databaseImplementations.Database;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;

public class BookScoreManager implements BookScoreInitializer, BookScoreReader {
	
	private static final String BOOKS_DATA_BASE_NAME = "BOOKS_DATABASE";
	private static final String REVIEWERS_DATA_BASE_NAME = "REVIEWERS_DATABASE";
	
	@Inject Database<String, DatabaseElement> reviewersDatabase = createDatabase(REVIEWERS_DATA_BASE_NAME);
	@Inject Database<String, DatabaseElement> booksDatabase = createDatabase(BOOKS_DATA_BASE_NAME);
	
	public Database<String, DatabaseElement> createDatabase(String databaseName) {
		return null;
	}
	
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
	
	private List<DatabaseElement> removeReviewersDuplicates(List<DatabaseElement> reviewers)
	{
	  	Map<String, Boolean> seen = new HashMap<String, Boolean>();
		
	  	if (reviewers.isEmpty()) {
	  		/* stream dosen't handle empty array well */
	  		return reviewers;
	  	}
	  	
	  	List <DatabaseElement> reversedReviewers = reviewers.subList(0, reviewers.size());
		Collections.reverse(reversedReviewers);
		
		return reversedReviewers.stream()
				   .filter(r -> seen.putIfAbsent(r.getKey(), Boolean.TRUE) == null)
			       .collect(Collectors.toList());
	}
	
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
		
		reviewers = removeReviewersDuplicates(reviewers);
		
		books = createListOfBooks(reviewers);
						
		reviewersDatabase.add(reviewers);
		booksDatabase.add(books);
		
		System.out.print("Finished");
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
