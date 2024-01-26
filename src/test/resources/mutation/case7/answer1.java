import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MathUtilsTest {

	@Test
	public void test() {
		MathUtils mathUtils = new MathUtils();

		assertEquals(0, mathUtils.multato5(0));
	}

}