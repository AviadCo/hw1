package databaseImplementations;

import java.util.HashMap;
import java.util.Map;

import il.ac.technion.cs.sd.book.ext.LineStorage;

public class MapBasedStorage implements LineStorage {

	private Map<Integer, String> mapStorage;
	
	public MapBasedStorage() {
		this.mapStorage = new HashMap<Integer, String>();
	}
	
	@Override
	public void appendLine(String item) {
		mapStorage.put(mapStorage.size(), item);
	}

	@Override
	public int numberOfLines() throws InterruptedException {
		Thread.sleep(100);
		
		return mapStorage.size();
	}

	@Override
	public String read(int index) throws InterruptedException {
		if (mapStorage.containsKey(index)) {
			String result = mapStorage.get(index);
			
			Thread.sleep(result.length());
			
			return result;
		} else {
			return null;
		}
	}

}
