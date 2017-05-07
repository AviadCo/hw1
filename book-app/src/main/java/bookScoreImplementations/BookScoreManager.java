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
import basicClasses.Review;
import basicClasses.Reviewer;
import databaseImplementations.Database;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;

public class BookScoreManager implements BookScoreInitializer, BookScoreReader {
	
	@Inject Database<String, Reviewer> reviewersDatabase = createReviewerDatabase();
	@Inject Database<String, Book> booksDatabase = createBookDatabase();
	
	public Database<String, Book> createBookDatabase() {
		return null;
	}
	
	public Database<String, Reviewer> createReviewerDatabase() {
		return null;
	}
	
	private List<Book> createListOfBooks(List<Reviewer> reviewers)
	{
	  	Map<String, Book> booksMap = new HashMap<String, Book>();
		
	  	reviewers.stream().forEach(r -> {
	  		for(Review review : r.getReviewslist()) {
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
	  	Map<String, Boolean> seen = new HashMap<String, Boolean>();
		
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
						
		reviewersDatabase.add(reviewers);
		booksDatabase.add(books);
		
		System.out.print("Finished");
	}

	@Override
	public boolean gaveReview(String reviewerId, String bookId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(reviewerId);
		List<Review> reviewList;
		
		if (!reviewer.isPresent()) {
			return false;
		}
		
		reviewList = reviewer.get().getReviewslist();
		
		return reviewList.stream().anyMatch(r -> r.getId().equals(bookId));
	}

	@Override
	public OptionalDouble getScore(String reviewerId, String bookId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(reviewerId);
		
		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
				
		Optional<Review> review = reviewer.get().getReviewslist()
				.stream()
				.filter(r -> r.getId().equals(bookId))
				.findAny();
		
		if (!review.isPresent()) {
			/* The reviewer didn't give review on the given book */
			return OptionalDouble.empty();
		}
		
		return OptionalDouble.of(review.get().getScore());
	}

	@Override
	public List<String> getReviewedBooks(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(reviewerId);
		List<String> booksIDs = new ArrayList<String>();

		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return booksIDs;
		}
		
		reviewer.get().getReviewslist()
					.stream()
					.forEach( r-> booksIDs.add(r.getId()));
		
		return booksIDs;
	}

	@Override
	public Map<String, Integer> getAllReviewsByReviewer(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(reviewerId);
		Map<String, Integer> reviewsMap = new HashMap<String, Integer>();

		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return reviewsMap;
		}
		
		reviewer.get().getReviewslist()
		.stream()
		.forEach( r-> reviewsMap.put(r.getId(), r.getScore()));

		return reviewsMap;
	}

	@Override
	public OptionalDouble getScoreAverageForReviewer(String reviewerId) {
		Optional<Reviewer> reviewer = reviewersDatabase.findElementByID(reviewerId);
		Double average = new Double(0);
		
		if (!reviewer.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
		
		if (!reviewer.get().getReviewslist().isEmpty()) {
			Integer sum = reviewer.get()
								 .getReviewslist()
								 .stream()
								 .mapToInt(r-> r.getScore())
								 .sum();
			
			average = (double) sum / reviewer.get().getReviewslist().size();
		}
		
		return OptionalDouble.of(average);
	}

	@Override
	public List<String> getReviewers(String bookId) {
		Optional<Book> book = booksDatabase.findElementByID(bookId);
		List<String> reviewers = new ArrayList<String>();
		
		if (!book.isPresent()) {
			/* No such reviewer */
			return reviewers;
		}
		
		book.get().getReviewslist()
		.stream()
		.forEach( r-> reviewers.add(r.getId()));
		
		return reviewers;
	}

	@Override
	public Map<String, Integer> getReviewsForBook(String reviewerId) {
		Optional<Book> book = booksDatabase.findElementByID(reviewerId);
		Map<String, Integer> reviewsMap = new HashMap<String, Integer>();

		if (!book.isPresent()) {
			/* No such reviewer */
			return reviewsMap;
		}
		
		book.get().getReviewslist()
		.stream()
		.forEach( r-> reviewsMap.put(r.getId(), r.getScore()));

		return reviewsMap;
	}

	@Override
	public OptionalDouble getAverageReviewScoreForBook(String bookId) {
		Optional<Book> book = booksDatabase.findElementByID(bookId);
		Double average = new Double(0);
		
		if (!book.isPresent()) {
			/* No such reviewer */
			return OptionalDouble.empty();
		}
		
		if (!book.get().getReviewslist().isEmpty()) {
			Integer sum = book.get()
							  .getReviewslist()
							  .stream()
						  	  .mapToInt(r-> r.getScore())
							  .sum();
			
			average = (double) sum / book.get().getReviewslist().size();
		}
		
		return OptionalDouble.of(average);
	}

}
