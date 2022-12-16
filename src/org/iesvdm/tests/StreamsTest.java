package org.iesvdm.tests;

import static org.junit.jupiter.api.Assertions.fail;
import static java.util.stream.Collectors.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.iesvdm.streams.Cliente;
import org.iesvdm.streams.ClienteHome;
import org.iesvdm.streams.Comercial;
import org.iesvdm.streams.ComercialHome;
import org.iesvdm.streams.Pedido;
import org.iesvdm.streams.PedidoHome;
import org.junit.jupiter.api.Test;

class StreamsTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}


	@Test
	void testSkeletonCliente() {
	
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			list.forEach(System.out::println);
		
			
			//TODO STREAMS
			
		
			cliHome.commitTransaction();
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	@Test
	void testSkeletonComercial() {
	
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			list.forEach(System.out::println);		
			//TODO STREAMS
		
			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	@Test
	void testSkeletonPedido() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			list.forEach(System.out::println);	
						
			//TODO STREAMS
		
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	/**
	 * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017, 
	 * cuya cantidad total sea superior a 500€.
	 * @throws ParseException 
	 */
	@Test
	void test1() throws ParseException {
		
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
			
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			
		
			List<Pedido> list = pedHome.findAll();
			

				
			//TODO STREAMS	
						
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido. 
	 * 
	 */
	@Test
	void test2() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
		
			cliHome.commitTransaction();
			
			
		
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
	 */
	@Test
	void test3() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			comHome.commitTransaction();
			
			Float maxComision = list.stream()
									.collect(maxBy(Comparator.comparingDouble(Comercial::getComisión)));
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL. 
	 * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
	 */
	@Test
	void test4() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
		
			cliHome.commitTransaction();
			
			var listado = list.stream()
								.filter(c -> c.getApellido2() != null)
								.map(c -> c.getId() + c.getNombre()+ c.getApellido1() )
								.sorted(Cliente::getApellido1)
								//.collect(toList())
								;
			
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o". 
	 *  Tenga en cuenta que se deberán eliminar los nombres repetidos.
	 */
	@Test
	void test5() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
	 */
	@Test
	void test6() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			
						
			//TODO STREAMS
		
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
	 */
	@Test
	void test7() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			comHome.commitTransaction();
			
			//List <Pedido> pedidos = pedHome.findAll();
			//id_comercial ==1
			//var mediaDanielSaez = pedidos.stream()
											//.filter(p -> p.getComercial() == 1);
			
			Long mediaDanielSaez = list.stream()
										.filter(c->c.getNombre().equals("Daniel")&c.getApellido1().equals("Sáez"))
										.collect(averagingLong());
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
}
