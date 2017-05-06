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
}
