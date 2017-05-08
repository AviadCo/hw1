package databaseImplementations;

import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

/**
 * This class implements LineStorageFactory to create MapBasedStorage objects.
 * 
 * @author Aviad
 *
 */
public class MapBasedStorageFactory implements LineStorageFactory {

	@Override
	public LineStorage open(String arg0) throws IndexOutOfBoundsException {
		return new MapBasedStorage();
	}

}
