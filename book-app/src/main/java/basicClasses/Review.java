package basicClasses;

import databaseInterfaces.IStringable;

public class Review implements IStringable {
	private ID id;
	private Integer grade;
	
	public final static String REVIEW_SPLITER = " ";
	
	Review(String id, String grade) {
		this.id = (ID) this.id.parseObjectFromString(id);
		this.grade = Integer.valueOf(grade);
	}
			
	public static Review parseReviewFromString(String s) {
		String[]  review = s.split(REVIEW_SPLITER);
		
		return new Review(review[0], review[1]);
	}
	
	@Override
	public String parseObjectToString() {
		return id.parseObjectToString() + REVIEW_SPLITER + Integer.toString(grade);
	}
	
	@Override
	public IStringable parseObjectFromString(String s) {
		return parseReviewFromString(s);
	}
}
