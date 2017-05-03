package il.ac.technion.cs.sd.book.test;

import com.google.inject.AbstractModule;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
public class BookScoreModule extends AbstractModule {
  @Override
  protected void configure() {
    throw new UnsupportedOperationException("Not implemented");
  }
}
