package bookScoreImplementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import basicClasses.Book;
import basicClasses.ID;
import basicClasses.Review;
import basicClasses.Reviewer;
import basicClassesFactory.BookFactory;
import basicClassesFactory.IDFactory;
import basicClassesFactory.ReviewerFactory;
import databaseImplementations.Database;
import databaseImplementations.MapBasedStorageFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;

public class BookScoreManager implements BookScoreInitializer, BookScoreReader {
	
	private static final String BOOKS_DATA_BASE_NAME = "BOOKS_DATABASE";
	private static final String REVIEWERS_DATA_BASE_NAME = "REVIEWERS_DATABASE";
	
	Database<ID, Reviewer> reviewersDatabase;
	Database<ID, Book> booksDatabase;
	
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
		reviewersDatabase = new Database<ID, Reviewer>(new MapBasedStorageFactory(), new IDFactory(), new ReviewerFactory(), REVIEWERS_DATA_BASE_NAME);
		booksDatabase = new Database<ID, Book>(new MapBasedStorageFactory(), new IDFactory(), new BookFactory(), BOOKS_DATA_BASE_NAME);
		
		reviewersDatabase.add(reviewers);
		booksDatabase.add(books);
		
		System.out.print("Finished");
	}

	@Override
	public boolean gaveReview(String reviewerId, String bookId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(new ID(reviewerId));
		List<Review> reviewList;
		
		if (!reviewer.isPresent()) {
			return false;
		}
		
		reviewList = reviewer.get().getReviewsList();
		
		return reviewList.stream().anyMatch(r -> r.getId() == new ID(bookId));
	}

	@Override
	public OptionalDouble getScore(String reviewerId, String bookId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(new ID(reviewerId));
		
		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
				
		Optional<Review> review = reviewer.get().getReviewsList()
				.stream()
				.filter(r -> r.getId() == new ID(bookId))
				.findAny();
		
		if (!review.isPresent()) {
			/* The reviewer didn't give review on the given book */
			return OptionalDouble.empty();
		}
		
		return OptionalDouble.of(review.get().getScore());
	}

	@Override
	public List<String> getReviewedBooks(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(new ID(reviewerId));
		List<String> booksIDs = new ArrayList<String>();

		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return booksIDs;
		}
		
		reviewer.get().getReviewsList()
					.stream()
					.forEach( r-> booksIDs.add(r.getId().parseObjectToString()));
		
		return booksIDs;
	}

	@Override
	public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(new ID(reviewerId));
		Map<String, Integer> reviewsMap = new HashMap<String, Integer>();

		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return reviewsMap;
		}
		
		reviewer.get().getReviewsList()
		.stream()
		.forEach( r-> reviewsMap.put(r.getId().parseObjectToString(), r.getScore()));

		return reviewsMap;
	}

	@Override
	public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(new ID(reviewerId));
		Double average = new Double(0);
		
		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
		
		if (!reviewer.get().getReviewsList().isEmpty()) {
			Integer sum = reviewer.get()
								 .getReviewsList()
								 .stream()
								 .mapToInt(r-> r.getScore())
								 .sum();
			
			average = (double) sum / reviewer.get().getReviewsList().size();
		}
		
		return OptionalDouble.of(average);
	}

	@Override
	public List<String> getReviewers(String bookId) {
		Optional<Book> book = booksDatabase.findElementByID(new ID(bookId));
		List<String> reviewers = new ArrayList<String>();
		
		if (!book.isPresent()) {
			/* No such reviewer */
			return reviewers;
		}
		
		book.get().getReviewsList()
		.stream()
		.forEach( r-> reviewers.add(r.getId().parseObjectToString()));
		
		return reviewers;
	}

	@Override
	public Map<String, Integer> getReviewsForBook(String reviewerId) {
		Optional<Book> book = booksDatabase.findElementByID(new ID(reviewerId));
		Map<String, Integer> reviewsMap = new HashMap<String, Integer>();

		if (!book.isPresent()) {
			/* No such reviewer */
			return reviewsMap;
		}
		
		book.get().getReviewsList()
		.stream()
		.forEach( r-> reviewsMap.put(r.getId().parseObjectToString(), r.getScore()));

		return reviewsMap;
	}

	@Override
	public OptionalDouble getAverageReviewScoreForBook(String bookId) {
		Optional<Book> book = booksDatabase.findElementByID(new ID(bookId));
		Double average = new Double(0);
		
		if (!book.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
		
		if (!book.get().getReviewsList().isEmpty()) {
			Integer sum = book.get()
							  .getReviewsList()
							  .stream()
						  	  .mapToInt(r-> r.getScore())
							  .sum();
			
			average = (double) sum / book.get().getReviewsList().size();
		}
		
		return OptionalDouble.of(average);
	}

}
