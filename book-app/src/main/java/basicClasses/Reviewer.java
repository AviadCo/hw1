package basicClasses;

import java.util.ArrayList;
import java.util.List;

import databaseInterfaces.IDatabaseElement;

public class Reviewer implements IDatabaseElement<String, Reviewer> {
	
	private String id;
	private List<Review> reviewslist;
		
	public Reviewer(String id, List<Review> reviewslist) {
		this.id = id;
		this.reviewslist = reviewslist;
	}
	
	public Reviewer(String id) {
		this.id = id;
		this.reviewslist = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Review> getReviewslist() {
		return reviewslist;
	}

	public void setReviewslist(List<Review> reviewslist) {
		this.reviewslist = reviewslist;
	}

	@Override
	public String getKey() {
		return id;
	}

	@Override
	public Reviewer getValue() {
		return this;
	}
	
	public void addReview(Review bookReview) {
		reviewslist.add(bookReview);
	}
}
