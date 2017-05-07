package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;

import bookScoreImplementations.BookScoreManager;
import databaseImplementations.Database;
import databaseInterfaces.IDatabase;
import il.ac.technion.cs.sd.book.app.BookScoreInitializer;
import il.ac.technion.cs.sd.book.app.BookScoreReader;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
class BookScoreModule extends AbstractModule {
  @Override
  protected void configure() {
	  bind(BookScoreInitializer.class).to(BookScoreManager.class);
	  bind(BookScoreReader.class).to(BookScoreManager.class);
	  bind(IDatabase.class).to(Database.class);
  }
}
