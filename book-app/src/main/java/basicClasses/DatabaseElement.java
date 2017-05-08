package basicClasses;

import java.util.ArrayList;
import java.util.List;

import databaseInterfaces.IDatabaseElement;

public class DatabaseElement implements IDatabaseElement<String, DatabaseElement> {

	private String id;
	private List<Review> reviewslist;
	
	public DatabaseElement (String id) {
		this.id = id;
		this.reviewslist = new ArrayList<>();
	}
		
	public DatabaseElement (String id, List<Review> reviewslist) {
		this.id = id;
		this.reviewslist = reviewslist;
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
	public DatabaseElement getValue() {
		return this;
	}

	public void addReview(Review bookReview) {
		reviewslist.add(bookReview);
	}
}
