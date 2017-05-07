package databaseImplementations;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;

import databaseInterfaces.IDatabase;
import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IStringableFactory;
import il.ac.technion.cs.sd.book.ext.LineStorage;

public class Database<Key extends Comparable<Key>, Value> implements IDatabase<Key, Value> {

	LineStorage lineStorageKeys;
	LineStorage lineStorageValues;
	IStringableFactory<Key> keyFactory;
	IStringableFactory<Value> valueFactory;
	
	@Inject
	public Database(LineStorage lineStorageKeys, LineStorage lineStorageValues,
					IStringableFactory<Key> keyFactory, IStringableFactory<Value> valueFactory) {
		this.lineStorageKeys = lineStorageKeys;
		this.lineStorageValues = lineStorageValues;
		this.keyFactory = keyFactory;
		this.valueFactory = valueFactory;
	}
	
	private Value getValueByIndex(int index) {
		try {
			return valueFactory.createObject(lineStorageValues.read(index));
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	private Key getKeyByIndex(int index) {
		try {
			return keyFactory.createObject(lineStorageKeys.read(index));
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

	private void addElement(IDatabaseElement<Key, Value> element) {
		lineStorageKeys.appendLine(keyFactory.createString(element.getKey()));
		lineStorageValues.appendLine(valueFactory.createString(element.getValue()));
	}
	
	@Override
	public void add(List<? extends IDatabaseElement<Key, Value>> elements) {
		elements.stream()
		.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.forEach(e -> addElement(e));
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
