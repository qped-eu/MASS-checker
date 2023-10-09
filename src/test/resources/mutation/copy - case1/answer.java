import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EvenTest {

	@Test
	public void test() {

		assertTrue(new Even().isTrue(2));
		//assertFalse(new Even().isTrue(1));
	}

}