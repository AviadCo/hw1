package basicClassesFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import basicClasses.Review;
import basicClasses.Reviewer;
import databaseInterfaces.IStringableFactory;

public class ReviewerFactory implements IStringableFactory<Reviewer> {

	private final static String REVIEWER_SPLITER = ",";

	@Override
	public Reviewer createObject(String s) {
		List<Review> reviewslist = new ArrayList<>();
		String[] reviewerString = s.split(REVIEWER_SPLITER);
				
		for (String review : Arrays.copyOfRange(reviewerString, 1, reviewerString.length)) {			
			reviewslist.add(new Review(review));
		}
		
		return new Reviewer(reviewerString[0], reviewslist);
	}

	@Override
	public String createString(Reviewer v) {
		String bookString = v.getId();
		
		
		for (Review review : v.getReviewslist()) {
			bookString += REVIEWER_SPLITER + review.parseReviewToString();
		}
		
		return bookString;
	}
}
