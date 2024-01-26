import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EvenTest {

	@Test
	public void test() {
		Even even = new Even();

		assertTrue(new Even().isTrue(2));
		//assertTrue(new Even().isTrue(4));
	}

}