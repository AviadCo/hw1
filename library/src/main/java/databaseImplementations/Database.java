package databaseImplementations;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;

import databaseInterfaces.IDatabase;
import databaseInterfaces.IDatabaseElement;
import databaseInterfaces.IStringableFactory;
import il.ac.technion.cs.sd.book.ext.LineStorage;

/**
 * This class represents a database implementation.
 * The elements in the database msut consist of Key and Value pair. 
 * 
 * @author Aviad
 *
 * @param <Key> - The type of element key
 * @param <Value> - The type of element value.
 */
public class Database<Key extends Comparable<Key>, Value> implements IDatabase<Key, Value> {

	LineStorage lineStorageKeys;
	LineStorage lineStorageValues;
	IStringableFactory<Key> keyFactory;
	IStringableFactory<Value> valueFactory;
	
	/**
	 * 
	 * @param lineStorageKeys - lineStorage to store the keys
	 * @param lineStorageValues - lineStorage to store the values
	 * @param keyFactory - factory to create key from string
	 * @param valueFactory - factory to create value from string
	 */
	@Inject
	public Database(LineStorage lineStorageKeys, LineStorage lineStorageValues,
					IStringableFactory<Key> keyFactory, IStringableFactory<Value> valueFactory) {
		this.lineStorageKeys = lineStorageKeys;
		this.lineStorageValues = lineStorageValues;
		this.keyFactory = keyFactory;
		this.valueFactory = valueFactory;
	}
	
	/**
	 * 
	 * @param index - line index of the value
	 * @return - Value that is stored in the row index
	 */
	private Value getValueByIndex(int index) {
		try {
			return valueFactory.createObject(lineStorageValues.read(index));
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	/**
	 * 
	 * @param index - line index of the key 
	 * @return - key that is stored in the row index
	 */
	private Key getKeyByIndex(int index) {
		try {
			return keyFactory.createObject(lineStorageKeys.read(index));
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}
	
	/**
	 * @return - number of elements in database
	 */
	@Override
	public Integer getNumberOfElements() {
		try {
			return lineStorageValues.numberOfLines();
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
	}

	/**
	 * 
	 * @param element - add one element to the lineStorage
	 */
	private void addElement(IDatabaseElement<Key, Value> element) {
		lineStorageKeys.appendLine(keyFactory.createString(element.getKey()));
		lineStorageValues.appendLine(valueFactory.createString(element.getValue()));
	}
	
	/**
	 * @param - list of elements to add at once.
	 */
	@Override
	public void add(List<? extends IDatabaseElement<Key, Value>> elements) {
		elements.stream()
		.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
				.forEach(e -> addElement(e));
	}

	/**
	 * @param - key of the wanted element
	 * @return - if key exists,  Optional<Value> of the element. else, Optionl.empty().
	 */
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
