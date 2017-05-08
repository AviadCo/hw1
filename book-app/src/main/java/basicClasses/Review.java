package basicClasses;

/**
 * This class represent a review.
 * The id of the review is either id of the book or the id of the reviewer.
 * For example: if the element is reviewer, the id of the review will be the book id. 
 * 
 * @author Aviad
 *
 */
public class Review {

	private final static String REVIEW_SPLITER = " ";
	
	private String id;
	private Integer score;
	
	/**
	 * 
	 * @param review - review in String ("id" "score")
	 */
	public Review (String review) {
		String[] reviewArray = review.split(REVIEW_SPLITER);
		
		this.id = reviewArray[0];
		this.score = Integer.valueOf(reviewArray[1]);
	}
	
	public Review (String id, String score) {
		this.id = id;
		this.score = Integer.valueOf(score);
	}
	
	public Review(String id, Integer score) {
		this.id = id;
		this.score = score;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	/**
	 * 
	 * @return string which represents the review
	 */
	public String parseReviewToString() {
		return id + REVIEW_SPLITER + score.toString();
	}
}
