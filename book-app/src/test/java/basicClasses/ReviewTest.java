package basicClasses;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class is a simple test for Review class.
 * 
 * @author Aviad
 *
 */
public class ReviewTest {

	@Test (timeout = 500)
	public void parsingReviewToFromStringTest()
	{
		Review review = new Review("check", 5);
		String reviewString = review.parseReviewToString();
		
		assertEquals(new Review(reviewString).getId(), review.getId());
		assertEquals(new Review(reviewString).getScore(), review.getScore());
	}
}
