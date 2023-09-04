import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NegativeTest {

	@Test
	public void test() {
		assertEquals(new Negative().isNegative(2),false);
	}

}