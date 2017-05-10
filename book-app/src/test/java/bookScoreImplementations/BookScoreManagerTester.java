package bookScoreImplementations;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Scanner;

import org.junit.*;
import org.junit.BeforeClass;

import basicClasses.DatabaseElement;
import basicClassesFactory.DatabaseElementFactory;
import basicClassesFactory.StringFactory;
import databaseImplementations.Database;
import databaseImplementations.MapBasedStorageFactory;

public class BookScoreManagerTester {
	
	private static BookScoreManager manager;
		
	private static final String KEYS = "KEYS";
	private static final String VALUES = "VALUES";
	
	@BeforeClass
	public static void setup() {
		/* Parsing example file */
	    String fileContents = null;
		try {
			fileContents = new Scanner(new File("src/test/resources/exmapleTest.xml")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		manager = new BookScoreManager(
				  new Database<String, DatabaseElement>(new MapBasedStorageFactory().open(BookScoreManager.REVIEWERS_DATA_BASE_NAME + KEYS),
						  							    new MapBasedStorageFactory().open(BookScoreManager.REVIEWERS_DATA_BASE_NAME + VALUES),
						  							    new StringFactory(), new DatabaseElementFactory()),
				  new Database<String, DatabaseElement>(new MapBasedStorageFactory().open(BookScoreManager.BOOKS_DATA_BASE_NAME + KEYS),
						    							new MapBasedStorageFactory().open(BookScoreManager.BOOKS_DATA_BASE_NAME + VALUES),
						    							new StringFactory(), new DatabaseElementFactory())
				);
		
		manager.setup(fileContents);
	}
	
	@Test
	public void getReviewedBooksTest() {
		List<String> reviewedBooks = manager.getReviewedBooks("123");
		
		assertTrue(reviewedBooks.contains("Boobar"));
		assertTrue(reviewedBooks.contains("Foobar"));
		assertTrue(reviewedBooks.contains("Moobar"));
		
		reviewedBooks = manager.getReviewedBooks("Aviad");

		assertTrue(reviewedBooks.contains("Check"));

		reviewedBooks = manager.getReviewedBooks("Noam");

		assertTrue(reviewedBooks.contains("Good"));
		assertTrue(reviewedBooks.contains("Bad"));
		assertFalse(reviewedBooks.contains("Boobar"));
	}

	@Test
	public void getAllReviewsByReviewerTest() {
		Map<String, Integer> reviewsMap = manager.getAllReviewsByReviewer("Aviad");
		
		assertEquals(reviewsMap.get("Check"), Integer.valueOf(2));
		
		reviewsMap = manager.getAllReviewsByReviewer("Noam");

		assertEquals(reviewsMap.get("Good"), Integer.valueOf(10));
		assertEquals(reviewsMap.get("Bad"), Integer.valueOf(0));
		
		reviewsMap = manager.getAllReviewsByReviewer("Shimon");
		
		assertEquals(reviewsMap.get("Good"), Integer.valueOf(8));
		
		reviewsMap = manager.getAllReviewsByReviewer("NotExists");
		
		assertTrue(reviewsMap.isEmpty());
	}

	@Test
	public void getScoreAverageForReviewerTest() {
		OptionalDouble average = manager.getScoreAverageForReviewer("123");
		
		assertEquals((Double)average.getAsDouble(), Double.valueOf(4));
		
		average = manager.getScoreAverageForReviewer("Noam");
		
		assertEquals((Double)average.getAsDouble(), Double.valueOf(5));

		average = manager.getScoreAverageForReviewer("NotExists");
		
		assertFalse(average.isPresent());
	}

	@Test
	public void getReviewersTest() {
		List<String> reviewers = manager.getReviewers("Good");

		assertTrue(reviewers.contains("Shimon"));
		assertTrue(reviewers.contains("Noam"));
		
		reviewers = manager.getReviewers("Bad");
		
		assertTrue(reviewers.contains("Noam"));
		
		reviewers = manager.getReviewers("Foobar");

		assertTrue(reviewers.contains("123"));
		assertFalse(reviewers.contains("Aviad"));
	}

	@Test
	public void getReviewsForBookTest() {
		Map<String, Integer> reviewsMap = manager.getReviewsForBook("Good");
		
		assertEquals(reviewsMap.get("Shimon"), Integer.valueOf(8));
		assertEquals(reviewsMap.get("Noam"), Integer.valueOf(10));

		
		reviewsMap = manager.getAllReviewsByReviewer("123");

		assertEquals(reviewsMap.get("Foobar"), Integer.valueOf(4));
		assertEquals(reviewsMap.get("Moobar"), Integer.valueOf(3));

		reviewsMap = manager.getAllReviewsByReviewer("NotExists");
		
		assertTrue(reviewsMap.isEmpty());
	}

	@Test
	public void getAverageReviewScoreForBookTest() {
		OptionalDouble average = manager.getAverageReviewScoreForBook("Good");
		
		assertEquals((Double)average.getAsDouble(), Double.valueOf(9));
		
		average = manager.getAverageReviewScoreForBook("Foobar");
		
		assertEquals((Double)average.getAsDouble(), Double.valueOf(4));

		average = manager.getAverageReviewScoreForBook("NotExists");
		
		assertFalse(average.isPresent());
	}
}
