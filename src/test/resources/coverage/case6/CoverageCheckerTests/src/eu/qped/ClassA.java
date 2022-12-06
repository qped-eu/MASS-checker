package eu.qped;

public class ClassA {

	/**
	 * 1. The checksum of the empty String is -1
	 * 
	 * 2. The checksum of null is -2
	 * 
	 * 3. The checksum of a non-empty String is its length plus the number of spaces in the String
	 * @param argument
	 * @return
	 */
	public int checksum(String argument) {
		
		if (argument != null && argument.isEmpty()) {
			int result = 0;
			// the hash code of the empty String is 0 and therefore, the condition is always satisfied
			result += argument.hashCode() == 0 ? -1 : argument.hashCode();
			return result;
		}
		else if (argument != null) {
			int result = argument.length();
			result += argument.chars().filter(c -> c == ' ').count();
			return result;
		}
		else {
			return -2;
		}
		
	}
	
}
