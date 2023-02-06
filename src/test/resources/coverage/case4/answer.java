package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.ClassA;
import eu.qped.ClassB;

class TestMissEmpty {

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
	void testAStringNonEmpty() {
		assertEquals(4, a.checksum("a b"));
	}

}
