package basicClasses;

public class Review {

	private final static String REVIEW_SPLITER = " ";
	
	private String id;
	private Integer score;
	
	public Review (String review) {
		String[] reviewArray = review.split(REVIEW_SPLITER);
		
		this.id = reviewArray[0];
		this.score = Integer.valueOf(reviewArray[1]);
	}
	
	public Review (String id, String score) {
		this.id = id;
		this.score = Integer.valueOf(score);
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

	public Review(String id, Integer score) {
		this.id = id;
		this.score = score;
	}
	
	public String parseReviewToString() {
		return id + REVIEW_SPLITER + score.toString();
	}
}
