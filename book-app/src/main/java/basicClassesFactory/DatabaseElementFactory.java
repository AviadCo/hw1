package basicClassesFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import basicClasses.DatabaseElement;
import basicClasses.Review;
import databaseInterfaces.IStringableFactory;

/**
 * This class is Factory for DatabaseElement.
 * The class implements IStringableFactory factory
 * 
 * @author Aviad
 *
 */
public class DatabaseElementFactory implements IStringableFactory<DatabaseElement> {

	private final static String ELEMENT_SPLITER = ",";
	
	@Override
	public DatabaseElement createObject(String s) {
		List<Review> reviewslist = new ArrayList<>();
		String[] elementString = s.split(ELEMENT_SPLITER);
				
		for (String review : Arrays.copyOfRange(elementString, 1, elementString.length)) {			
			reviewslist.add(new Review(review));
		}
		
		return new DatabaseElement(elementString[0], reviewslist);
	}

	@Override
	public String createString(DatabaseElement b) {
		String elementString = b.getId();
		
		for (Review review : b.getReviewslist()) {
			elementString += ELEMENT_SPLITER + review.parseReviewToString();
		}
		
		return elementString;
	}
	
}
