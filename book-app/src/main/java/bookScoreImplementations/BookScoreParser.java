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

import basicClasses.Review;
import basicClasses.Reviewer;

public class BookScoreParser {

	private static final String REVIEWER_TAG = "Reviewer";
	private static final String REVIEW_TAG = "Review";
	private static final String ID_TAG = "Id";
	private static final String SCORE_TAG = "Score";

	/*
	 * Create Document Object out of String
	 */
	public static List<Reviewer> createListOfReviewers(String xml) throws Exception
	{
		Map<String, Reviewer> reviewerMap = new HashMap<String, Reviewer> ();
		
		Document doc = loadXMLFromString(xml);
		
		doc.getDocumentElement().normalize();
		
		System.out.print("Root element: ");
		
        System.out.println(doc.getDocumentElement().getNodeName());
        
        System.out.println("----------------------------");
        
        NodeList nList = doc.getElementsByTagName(REVIEWER_TAG);
        
        for (int currentReviewerIndex = 0; currentReviewerIndex < nList.getLength(); currentReviewerIndex++) {
            Node currentReviwerNode = nList.item(currentReviewerIndex);
            
            System.out.println("\nCurrent Reviewer :");
            
            System.out.print(currentReviwerNode.getNodeName());
            
            if (currentReviwerNode.getNodeType() == Node.ELEMENT_NODE) {
            	Reviewer currentReviewer = parseReviewerNode(currentReviwerNode);
            	
            	if (reviewerMap.containsKey(currentReviewer.getKey())) {
            		Reviewer sameReviewer = reviewerMap.get(currentReviewer.getKey());
            		
            		List<Review> allReviewes = new ArrayList<>(sameReviewer.getReviewslist());
            		allReviewes.addAll(currentReviewer.getReviewslist());
            		
            		reviewerMap.put(currentReviewer.getKey(), new Reviewer(currentReviewer.getKey(), allReviewes));
            	} else {
            		reviewerMap.put(currentReviewer.getKey(), currentReviewer);
            	}
            }
         }
	    
	    return new ArrayList<>(reviewerMap.values());
	}
	
	/*
	 * Create Document Object out of String
	 */
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    
	    return builder.parse(is);
	}
	
	/*
	 * Create Document Object out of String
	 */
	public static Review parseReviewNode(Node reviewNode) throws Exception
	{
		Element reviewElement = (Element) reviewNode;
		
		Element idTag = (Element) reviewElement.getElementsByTagName(ID_TAG).item(0);
		Element scoreNode = (Element) reviewElement.getElementsByTagName(SCORE_TAG).item(0);

        System.out.print("Review : ");
        System.out.println("ID: " + idTag.getTextContent());
        System.out.println("Score: " + scoreNode.getTextContent());

		return new Review(idTag.getTextContent(), scoreNode.getTextContent());
     }
	
	/*
	 * Create Document Object out of String
	 */
	public static Reviewer parseReviewerNode(Node reviewerNode) throws Exception
	{
		Reviewer reviewer;
		Element reviewerElement = (Element) reviewerNode;
		
		String reviewerID = reviewerElement.getAttribute(ID_TAG);
		
        System.out.print("ID : ");
        System.out.println(reviewerID);
        
        reviewer = new Reviewer(reviewerID);
        
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
