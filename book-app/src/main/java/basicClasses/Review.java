package basicClasses;

import databaseInterfaces.IStringable;

public class Review implements IStringable {
	private ID id;
	private Integer score;
	
	public final static String REVIEW_SPLITER = " ";
	
	public Review(ID id, Integer score) {
		this.id = id;
		this.score = score;
	}
	
	public Review(String id, String score) {
		this.id = new ID(id);
		this.score = Integer.valueOf(score);
	}
			
	public static Review parseReviewFromString(String s) {
		String[]  review = s.split(REVIEW_SPLITER);
		
		return new Review(review[0], review[1]);
	}
	
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public String parseObjectToString() {
		return id.parseObjectToString() + REVIEW_SPLITER + Double.toString(score);
	}
	
	@Override
	public IStringable parseObjectFromString(String s) {
		return parseReviewFromString(s);
	}
}
