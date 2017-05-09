package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import basicClasses.DatabaseElement;
import basicClassesFactory.DatabaseElementFactory;
import basicClassesFactory.StringFactory;
import bookScoreImplementations.BookScoreManager;
import databaseImplementations.Database;
import databaseImplementations.MapBasedStorageFactory;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
	
	private static final String KEYS = "KEYS";
	private static final String VALUES = "VALUES";

  @Override
  protected void configure() {
	  bind(BookScoreInitializer.class).to(BookScoreManager.class);
	  bind(BookScoreReader.class).to(BookScoreManager.class);
	  //TODO remove this after testing
	  bind(LineStorageFactory.class).to(MapBasedStorageFactory.class);
  }
  
  @Provides
  @Singleton 
  @Named(BookScoreManager.REVIEWERS_DATA_BASE_NAME)
  Database<String, DatabaseElement> createReviewDatabase(LineStorageFactory lineStorageFactory) {
	  return new Database<String, DatabaseElement>(lineStorageFactory.open(BookScoreManager.REVIEWERS_DATA_BASE_NAME + KEYS),
			  									   lineStorageFactory.open(BookScoreManager.REVIEWERS_DATA_BASE_NAME + VALUES),
			  									   new StringFactory(), new DatabaseElementFactory());
  }
  
  @Provides
  @Singleton 
  @Named(BookScoreManager.BOOKS_DATA_BASE_NAME)
  Database<String, DatabaseElement> createBooksDatabase(LineStorageFactory lineStorageFactory) {
	  return new Database<String, DatabaseElement>(lineStorageFactory.open(BookScoreManager.BOOKS_DATA_BASE_NAME + KEYS),
			  									   lineStorageFactory.open(BookScoreManager.BOOKS_DATA_BASE_NAME + VALUES),
			  									   new StringFactory(), new DatabaseElementFactory());
  }
}
