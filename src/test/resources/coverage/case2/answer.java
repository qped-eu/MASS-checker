package eu.qped;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.ClassA;
import eu.qped.ClassB;

class TestCoverageFull {

	private ClassB b;
	private ClassA a;

	@BeforeEach
	void setup() {
		b = new ClassB();
		a = new ClassA();
	}

	@Test
	void testAStringNull() {
		assertEquals(-2, a.checksum((String) null));
	}

	@Test
	void testAStringEmpty() {
		assertEquals(-1, a.checksum(""));
	}

	@Test
	void testAStringNonEmpty() {
		assertEquals(4, a.checksum("a b"));
	}

	@Test
	void testBStringNull() {
		assertEquals(-2, b.checksum((String) null));
	}

	@Test
	void testBStringEmpty() {
		assertEquals(-1, b.checksum(""));
	}

	@Test
	void testBStringNonEmpty() {
		assertEquals(4, b.checksum("a b"));
	}

	@Test
	void testBArrayNull() {
		assertEquals(-2, b.checksum((char[]) null));
	}

	@Test
	void testBArrayEmpty() {
		assertEquals(-1, b.checksum(new char[] {}));
	}

	@Test
	void testBArraygNonEmpty() {
		assertEquals(4, b.checksum(new char[] {'a', ' ', 'b'}));
	}

}
