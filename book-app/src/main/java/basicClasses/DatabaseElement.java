package basicClasses;

import java.util.ArrayList;
import java.util.List;

import databaseInterfaces.IDatabaseElement;

/**
 * This class implements IDatabaseElement interface.
 * The DatabaseElement is match to reviewer and book structures
 * 
 * @author Aviad
 *
 */
public class DatabaseElement implements IDatabaseElement<String, DatabaseElement> {

	private String id;
	private List<Review> reviewslist;
	
	/**
	 * @param id - id of the element
	 */
	public DatabaseElement (String id) {
		this.id = id;
		this.reviewslist = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param id - id of the element
	 * @param reviewslist - list of reviews which belongs to element
	 */
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

	/**
	 * 
	 * @param review - review to add to the element list
	 */
	public void addReview(Review review) {
		reviewslist.add(review);
	}
}
