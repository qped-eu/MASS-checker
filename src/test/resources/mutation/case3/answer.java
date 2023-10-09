import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EvenTest {

	@Test
	public void test() {
		Even even = new Even();

		assertTrue(even.isTrue(2));
		assertFalse(even.isTrue(1));
	}

}