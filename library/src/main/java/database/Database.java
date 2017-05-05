package database;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;

import databaseInterfaces.IDatabase;
import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IFactory;
import databaseInterfaces.IStringable;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

public class Database<Key extends Comparable<Key> & IStringable, Value extends IStringable> implements IDatabase<Key, Value> {

	LineStorage lineStorageKeys;
	LineStorage lineStorageValues;
	IFactory<Key> keyFactory;
	IFactory<Value> valueFactory;
	
	private final String KEYS = "Keys";
	private final String VALUES = "Values";
	
	@Inject
	Database(LineStorageFactory lineStorageFactory, IFactory<Key> keyFactory, IFactory<Value> valueFactory, String databaseName) {
		this.lineStorageKeys = lineStorageFactory.open(databaseName + KEYS);
		this.lineStorageValues = lineStorageFactory.open(databaseName + VALUES);
		this.keyFactory = keyFactory;
		this.valueFactory = valueFactory;
	}
	
	private Value getValueByIndex(int index) {
		try {
			return valueFactory.create(lineStorageValues.read(index));
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	private Key getKeyByIndex(int index) {
		try {
			return keyFactory.create(lineStorageKeys.read(index));
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	@Override
	public Integer getNumberOfElements() {
		try {
			return lineStorageValues.numberOfLines();
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}

	private void add(IDatabaseElement<Key, Value> element) {
		lineStorageKeys.appendLine(element.getKey().parseObjectToString());
		lineStorageValues.appendLine(element.getValue().parseObjectToString());
	}
	
	@Override
	public void add(List<IDatabaseElement<Key, Value>> elements) {
		elements.stream()
		.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.forEach(e -> add(e));
	}

	@Override
	public Optional<Value> findElementByID(Key key) {
		Integer numberOfLines = getNumberOfElements();
		Integer lowLine, highLine;

		if (numberOfLines == 0) {
			return Optional.empty();
		}
		
		lowLine = 0;
		highLine = numberOfLines - 1;
				
		while (lowLine <= highLine) {
			int currentLine = (lowLine + highLine) / 2;
			Key currentElementKey = getKeyByIndex(currentLine);
			int compareResult = currentElementKey.compareTo(key);
			
			if (compareResult == 0) {
				return Optional.of(getValueByIndex(currentLine));
			} else if (compareResult < 0) {
				lowLine = currentLine + 1;
			} else {
				highLine = currentLine - 1;
			}
		}
		
		return Optional.empty();
	}
}
