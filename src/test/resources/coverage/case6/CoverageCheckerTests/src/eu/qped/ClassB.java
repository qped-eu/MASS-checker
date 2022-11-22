package eu.qped;

public class ClassB {
	
	/**
	 * 1. The checksum of the empty String is -1
	 * 
	 * 2. The checksum of null is -2
	 * 
	 * 3. The checksum of a non-empty String is its length plus the number of spaces in the String
	 * @param argument
	 * @return
	 */
	public int checksum(String s) {
		return new ClassA().checksum(s);
	}

	/**
	 * 1. The checksum of the empty array -1
	 * 
	 * 2. The checksum of null is -2
	 * 
	 * 3. The checksum of a non-empty array is its length plus the number of spaces in the array
	 * @param argument
	 * @return
	 */
	public int checksum(char[] s) {
		if (s == null) {
			return new ClassA().checksum(null);
		}
		else {
		
			return new ClassA().checksum(new String(s).intern());
		}
	}

}
