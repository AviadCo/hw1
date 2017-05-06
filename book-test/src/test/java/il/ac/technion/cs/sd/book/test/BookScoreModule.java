package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;

import bookScoreImplementations.BookScoreManager;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
  @Override
  protected void configure() {
	  bind(BookScoreInitializer.class).to(BookScoreManager.class);
  }
}
