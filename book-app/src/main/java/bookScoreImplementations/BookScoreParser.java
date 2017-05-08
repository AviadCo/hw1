package bookScoreImplementations;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import basicClasses.DatabaseElement;
import basicClasses.Review;

/**
 * This class parse the xml data to list of DatabaseElements.
 * 
 * @author Aviad
 *
 */
public class BookScoreParser {

	private static final String REVIEWER_TAG = "Reviewer";
	private static final String REVIEW_TAG = "Review";
	private static final String ID_TAG = "Id";
	private static final String SCORE_TAG = "Score";

	/**
	 * 
	 * @param xml - the xml string of the reviewers
	 * @return - list of reviewers (DatabaseElement)
	 * @throws Exception - throws exception on DOM xml parser failure
	 */
	public static List<DatabaseElement> createListOfReviewers(String xml) throws Exception
	{
		Map<String, DatabaseElement> reviewerMap = new HashMap<String, DatabaseElement> ();
		
		Document doc = loadXMLFromString(xml);
		
		doc.getDocumentElement().normalize();
        
        NodeList nList = doc.getElementsByTagName(REVIEWER_TAG);
        
        for (int currentReviewerIndex = 0; currentReviewerIndex < nList.getLength(); currentReviewerIndex++) {
            Node currentReviwerNode = nList.item(currentReviewerIndex);
            
            if (currentReviwerNode.getNodeType() == Node.ELEMENT_NODE) {
            	DatabaseElement currentReviewer = parseReviewerNode(currentReviwerNode);
            	
            	if (reviewerMap.containsKey(currentReviewer.getKey())) {
            		DatabaseElement sameReviewer = reviewerMap.get(currentReviewer.getKey());
            		
            		List<Review> allReviewes = new ArrayList<>(sameReviewer.getReviewslist());
            		allReviewes.addAll(currentReviewer.getReviewslist());
            		
            		reviewerMap.put(currentReviewer.getKey(), new DatabaseElement(currentReviewer.getKey(), allReviewes));
            	} else {
            		reviewerMap.put(currentReviewer.getKey(), currentReviewer);
            	}
            }
         }
	    
	    return new ArrayList<>(reviewerMap.values());
	}
	
	/**
	 * 
	 * @param xml - parse String xml to Document for DOM parser
	 * @return - Document of the xml
	 * @throws Exception - throws exception on DOM xml parser failure
	 */
	private static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    
	    return builder.parse(is);
	}
	
	/**
	 * 
	 * @param reviewNode - the xml node of the review
	 * @return - Review object
	 * @throws Exception - throws exception on DOM xml parser failure
	 */
	private static Review parseReviewNode(Node reviewNode) throws Exception
	{
		Element reviewElement = (Element) reviewNode;
		
		Element idTag = (Element) reviewElement.getElementsByTagName(ID_TAG).item(0);
		Element scoreNode = (Element) reviewElement.getElementsByTagName(SCORE_TAG).item(0);

		return new Review(idTag.getTextContent(), scoreNode.getTextContent());
     }
	
	/**
	 * 
	 * @param reviewerNode - xml node of a reviewer
	 * @return - Reviewer object (DatabaseElement)
	 * @throws Exception - throws exception on DOM xml parser failure
	 */
	private static DatabaseElement parseReviewerNode(Node reviewerNode) throws Exception
	{
		DatabaseElement reviewer;
		Element reviewerElement = (Element) reviewerNode;
		
		String reviewerID = reviewerElement.getAttribute(ID_TAG);
        
        reviewer = new DatabaseElement(reviewerID);
        
        NodeList reviewList = reviewerElement.getElementsByTagName(REVIEW_TAG);
        
        for (int reviewIndex = 0; reviewIndex < reviewList.getLength(); reviewIndex++) {
        	Node currentReview = reviewList.item(reviewIndex);
        	
            if (currentReview.getNodeType() == Node.ELEMENT_NODE) {
                reviewer.addReview(parseReviewNode(currentReview));            	
            }
        }
	    
	    return reviewer;
	}
}
