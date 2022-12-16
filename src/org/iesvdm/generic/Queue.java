package org.iesvdm.generic;

import java.util.ArrayList;

public class Queue <T>{
	private ArrayList<T> cola;

	public Queue() {
		this.cola = new ArrayList<>();
	}
	
	//enqueue(): agrega un elemento a la cola
	//boolean para saber si se ha agragado
	public boolean enqueue(T t) {
		return this.cola.add(t);
	}
	
	//dequeue(): extrae un  elemento de la cola
	public void dequeue(T t) {
		this.cola.remove(t);
	}
	
	//front(): devuelve el elemento frontal
	public T front() {
		T retorno= null;
		if(cola.size() > 0) {
			retorno = this.cola.get(0);
		}
		
		return retorno;
	}
	
	//rear() devuelve el elemento trasero
	public T rear() {
		T retorno= null;
		if(cola.size() > 0) {
			retorno = cola.get(cola.size() - 1);
		}
		
		return retorno;
	}
	
	//vacío (): devuelve si la cola está vacía o no
	public boolean vacio() {
		return !(cola.size() > 0);
	}


}
