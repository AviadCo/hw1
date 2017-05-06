package il.ac.technion.cs.sd.book.app;

import com.google.inject.Inject;

public interface BookScoreInitializer {
  /** Saves the XML data persistently, so that it could be run using BookScoreReader. */	
	@Inject
	void setup(String xmlData);
}
