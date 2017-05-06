package bookScoreImplementations;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import il.ac.technion.cs.sd.book.test.BookScoreInitializer;
import il.ac.technion.cs.sd.book.test.BookScoreReader;

public class BookScoreManager implements BookScoreInitializer, BookScoreReader {

	@Override
	public void setup(String XmlData) {
		// TODO Auto-generated method stub
		
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
