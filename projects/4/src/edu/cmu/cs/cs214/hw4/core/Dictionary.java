package edu.cmu.cs.cs214.hw4.core;

public class Dictionary {
	private String[] wordList;
	
	public Dictionary(String[] list){
		wordList = list;
	}

	//performs binary search to look up a word in dictionary
	public boolean isValid(String word){
		int lower = -1;
		int higher = wordList.length;
		int mid;
		while(lower+1 < higher){
			mid  = (lower+higher)/2;
			if (word.equals(wordList[mid]))
				return true;
			if (word.compareTo(wordList[mid]) > 0)
				lower = mid;
			else
				higher = mid;
		}
		return false;
	}
	
}

