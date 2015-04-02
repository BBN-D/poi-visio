/*
 * Copyright (c) 2015 Raytheon BBN Technologies Corp. All rights reserved.
 */

package org.apache.poi.xdgf.usermodel.section;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map.Entry;

// iterates over the base and master
public class CombinedIterable<T> implements Iterable<T> {

	final SortedMap<Long, T> _baseItems;
	final SortedMap<Long, T> _masterItems;
	
	public CombinedIterable(SortedMap<Long, T> baseItems, 
							SortedMap<Long, T> masterItems) {
		_baseItems = baseItems;
		_masterItems = masterItems;
	}
	
	@Override
	public Iterator<T> iterator() {
		
		final Iterator<Entry<Long, T>> vmasterI;
		
		if (_masterItems != null)
			vmasterI = _masterItems.entrySet().iterator();
		else
			vmasterI = Collections.emptyIterator();
		
		return new Iterator<T>() {

			Long lastI = Long.MIN_VALUE;
			
			Entry<Long, T> currentBase = null;
			Entry<Long, T> currentMaster = null;
			
			// grab the iterator for both
			Iterator<Entry<Long, T>> baseI = _baseItems.entrySet().iterator();
			Iterator<Entry<Long, T>> masterI = vmasterI;
			
			@Override
			public boolean hasNext() {
				return currentBase != null || currentMaster != null || baseI.hasNext() || masterI.hasNext();
			}

			@Override
			public T next() {
				
				// TODO: This seems far more complex than it needs to be
				
				long baseIdx = Long.MAX_VALUE;
				long masterIdx = Long.MAX_VALUE;
				
				if (currentBase == null) {
					while (baseI.hasNext()) {
						currentBase = baseI.next();
						if (currentBase.getKey() > lastI) {
							baseIdx = currentBase.getKey();
							break;
						}
					}
				} else {
					baseIdx = currentBase.getKey();
				}
				
				if (currentMaster == null) {
					while (masterI.hasNext()) {
						currentMaster = masterI.next();
						if (currentMaster.getKey() > lastI) {
							masterIdx = currentMaster.getKey();
							break;
						}
					}
				} else {
					masterIdx = currentMaster.getKey();
				}
				
				T val;
				
				if (currentBase != null) {
					
					if (baseIdx <= masterIdx) {
						lastI = baseIdx;
						val = currentBase.getValue();
						
						// discard master if same as base
						if (masterIdx == baseIdx) {
							currentMaster = null;
						}
						
						currentBase = null;
						
					} else {
						lastI = masterIdx;
						val = currentMaster.getValue();
						currentMaster = null;
					}
					
				} else if (currentMaster != null) {
					lastI = currentMaster.getKey();
					val = currentMaster.getValue();
					
					currentMaster = null;
				} else {
					throw new NoSuchElementException();
				}
				
				return val;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
