package basicClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IStringable;

public class Book implements IDatabaseElement<ID, Book >, IStringable {
			
	private ID id;
	private List<Review> reviewslist;

	public final static String REVIEWER_SPLITER = ",";
	
	public Book (String id) {
		this.id = new ID(id);
		this.reviewslist = new ArrayList<>();
	}
	
	public Book (ID id) {
		this.id = id;
		this.reviewslist = new ArrayList<>();
	}
	
	Book(String id, String[] reviewslist) {
		this.id = (ID) this.id.parseObjectFromString(id);
		this.reviewslist = new ArrayList<>();
		
		for (String review : reviewslist ) {			
			this.reviewslist.add(Review.parseReviewFromString(review));
		}
	}
	
	@Override
	public ID getKey() {
		return id;
	}
	
	@Override
	public Book getValue() {
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
