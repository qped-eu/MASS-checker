package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.qped.ClassA;
import eu.qped.ClassB;

class TestMissEverything {

	private ClassB b;
	private ClassA a;

	@BeforeEach
	void setup() {
		b = new ClassB();
		a = new ClassA();
	}
	
}
