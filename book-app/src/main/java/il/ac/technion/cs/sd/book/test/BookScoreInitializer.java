package il.ac.technion.cs.sd.book.test;

public interface BookScoreInitializer {
  /** Saves the XML data persistently, so that it could be run using BookScoreReader. */
  void setup(String XmlData);
}
