import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EvenTest {

	@Test
	public void test() {
		Even even = new Even();

		assertFalse(even.isTrue(1));
		//assertFalse(even.isTrue(3));
		//assertFalse(even.isTrue(5));
		//assertFalse(even.isTrue(7));
	}

}