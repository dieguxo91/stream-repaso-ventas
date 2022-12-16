package org.iesvdm.generic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestQueue {

	Queue<String> cola;

	
	@Test
	void test1() {
		cola = new Queue<>();
		cola.enqueue("palabraParaDentro");
		
		assertEquals(false, cola.vacio());
	}
	
	@Test
	void test2() {
		cola = new Queue<>();
		cola.dequeue("palabraParaDentro");;
		
		assertEquals(true, cola.vacio());
	}
	
	@Test
	void test3() {
		cola = new Queue<>();
		cola.enqueue("primerElemento");
		cola.enqueue("segundoElemento");
		
		assertEquals("primerElemento", cola.front());
	}
	
	@Test
	void test4() {
		cola = new Queue<>();
		cola.enqueue("primerElemento");
		cola.enqueue("segundoElemento");
		
		assertEquals("segundoElemento", cola.rear());
	}
	
	@Test
	void test5() {
		cola = new Queue<>();
		//si no se introduce nada, deve devolver true
		assertEquals(true, cola.vacio());
	}
	
}
