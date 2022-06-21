package adt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Bag {

	// Internal invariant
	// @inv elementen.size() >= 0
	private ArrayList<Integer> elements;

	// Attribute is used for testing the student's test
	private int attributeForStudentTestTest = 0;

	/**
	 * @ensures A new bag is instantiated, with length = 0.
	 */
	public Bag() {
		elements = new ArrayList<>(); // You have not created a new bag needed to test the class Bag.
	}

	/**
	 * @ensures new cardinality = old cardinality + 1
	 * @ensures the new length is the old length plus 1
	 * @ensures returns true
	 */
	public boolean add(int elem) {
		if (length() == 0) {
			attributeForStudentTestTest++; // Add method: You have not tested the add method with an empty bag.
		}
		if (length() > 0) {
			attributeForStudentTestTest++; // Add method: You have not tested the add method with a non empty bag.
		}
		return elements.add(elem);
	}

	/**
	 * @sub Happy-path {
	 * @requires length > 0 and the bag contains n elements of elem, with n > 0
	 * @ensures new cardinality(elem) = old cardinality(elem) - 1
	 * @ensures length is old length minus 1
	 * @ensures returns true }
	 * @sub Non-happy-path {
	 * @requires length = 0 or the bag does not contain element elem
	 * @ensures the bag is not changed
	 * @ensures length is old length
	 * @ensures returns false }
	 */
	public boolean remove(int elem) {
		if (length() > 0 && elements.contains(elem)) {
			attributeForStudentTestTest++; // Remove method: You have not tested the requirement `length > 0' and a bag
											// containing elem (happy-path scenario).
		}
		if (length() <= 0) {
			attributeForStudentTestTest++; // Remove method: You have not tested the requirement `length = 0' of the
											// non-happy-path.
		}
		if (!elements.contains(elem)) {
			attributeForStudentTestTest++; // Remove method: You have not tested the requirement `the bag does not
											// contain element elem' of the non-happy path.
		}
		return elements.remove(Integer.valueOf(elem));
	}

	/**
	 * @ensures returns the number of elements
	 */
	public int length() {
		return elements.size();
	}

	/**
	 * @ensures true if they contain the same elements otherwise false
	 */
	public boolean equals(Object obj) {
		if (length() == 0) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with an empty bag as
											// this.
		}
		if (((Bag) obj).length() == 0) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with an empty bag as
											// parameter.
		}
		if (length() > 0) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with an non-empty bag
											// as this.
		}
		if (((Bag) obj).length() > 0) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with an non-empty bag
											// as parameter.
		}
		if (length() == ((Bag) obj).length()) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with two bags of
											// equal length.
		}
		if (length() != ((Bag) obj).length()) {
			attributeForStudentTestTest++; // Equals method: You have not tested the equals method with two bags of
											// unequal length.
		}
		if (!(obj instanceof Bag)) {
			return false;
		}
		if (this.length() != ((Bag) obj).length()) {
			return false;
		}
		int[] elems1 = this.getElems();
		int[] elems2 = ((Bag) obj).getElems();
		Arrays.sort(elems1);
		Arrays.sort(elems2);
		return Arrays.equals(elems1, elems2);
	}

	/**
	 * @ensures returns the elements in the bag
	 */
	public int[] getElems() {
		int[] elems = new int[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			elems[i] = elements.get(i);
		}
		return elems;
	}

	/**
	 * @ensures returns the number of elem in the bag
	 */
	public int cardinality(int elem) {
		if (length() == 0) {
			attributeForStudentTestTest++; // Method cardinality: You have not tested with an empty bag.
		}
		if (length() != 0 && Collections.frequency(elements, elem) == 0) {
			attributeForStudentTestTest++; // Method cardinality: You have not tested with non-empty bag and cardinality
											// zero.
		}
		if (length() != 0 && Collections.frequency(elements, elem) > 0) {
			attributeForStudentTestTest++; // Method cardinality: You have not tested with non-empty bag and cardinality
											// > zero.
		}
		return Collections.frequency(elements, elem);
	}

}
