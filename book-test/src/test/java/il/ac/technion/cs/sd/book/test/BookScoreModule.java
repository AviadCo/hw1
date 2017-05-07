package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import basicClasses.Book;
import basicClasses.Reviewer;
import basicClassesFactory.BookFactory;
import basicClassesFactory.ReviewerFactory;
import basicClassesFactory.StringFactory;
import bookScoreImplementations.BookScoreManager;
import databaseImplementations.Database;
import databaseImplementations.MapBasedStorageFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
	
	private static final String BOOKS_DATA_BASE_NAME = "BOOKS_DATABASE";
	private static final String REVIEWERS_DATA_BASE_NAME = "REVIEWERS_DATABASE";
	private static final String KEYS = "KEYS";
	private static final String VALUES = "VALUES";

  @Override
  protected void configure() {
	  bind(BookScoreInitializer.class).to(BookScoreManager.class);
	  bind(BookScoreReader.class).to(BookScoreManager.class);
  }
  
  @Provides
  @Singleton Database<String, Reviewer> createReviewerDatabase() {
	  LineStorageFactory lineStorageFactory = new MapBasedStorageFactory();

	  return new Database<String, Reviewer>(lineStorageFactory.open(REVIEWERS_DATA_BASE_NAME + KEYS),
			  								lineStorageFactory.open(REVIEWERS_DATA_BASE_NAME + VALUES),
				   						    new StringFactory(), new ReviewerFactory());
  }
  
  @Provides
  @Singleton Database<String, Book> createBookDatabase() {
	  LineStorageFactory lineStorageFactory = new MapBasedStorageFactory();

	  return new Database<String, Book>(lineStorageFactory.open(BOOKS_DATA_BASE_NAME + KEYS),
			  							lineStorageFactory.open(BOOKS_DATA_BASE_NAME + VALUES),
				   						new StringFactory(), new BookFactory());
  }
}
