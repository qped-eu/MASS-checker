import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.util.MathUtils;
import org.junit.jupiter.api.Test;

public class MathUtilsCustomTest {

	@Test
	public void test() {
		assertEquals(new MathUtilsCustom().add(3, 2), 5);
	}

	
}
