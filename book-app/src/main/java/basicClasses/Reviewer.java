package basicClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IStringable;

public class Reviewer implements IDatabaseElement<ID, Reviewer>, IStringable {
	
	private ID id;
	private List<Review> reviewslist;

	public final static String REVIEWER_SPLITER = ",";
		
	Reviewer(String id, String[] reviewslist) {
		this.id = new ID(id);
		this.reviewslist = new ArrayList<>();
		
		for (String review : reviewslist ) {			
			this.reviewslist.add(Review.parseReviewFromString(review));
		}
	}
	
	public Reviewer(String id) {
		this.id = new ID(id);
		this.reviewslist = new ArrayList<>();
	}
	
	public Reviewer(ID id, List<Review> reviewslist) {
		this.id = id;
		this.reviewslist = reviewslist;
	}

	@Override
	public ID getKey() {
		return id;
	}
	
	@Override
	public Reviewer getValue() {
		return this;
	}

	@Override
	public String parseObjectToString() {
		String result = "";
		
		result += id.parseObjectToString();
		
		for(Review review : reviewslist) {
			result += REVIEWER_SPLITER + review.parseObjectToString();
		}
		
		return result;
	}

	@Override
	public IStringable parseObjectFromString(String s) {
		String[] parsedString = s.split(REVIEWER_SPLITER);

		return new Reviewer(parsedString[0], Arrays.copyOfRange(parsedString, 1, parsedString.length));
	}

	public void addReview(Review review) {
		reviewslist.add(review);
	}
	
	public List<Review> getReviewsList() {
		return reviewslist;
	}
}
