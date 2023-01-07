package org.iesvdm.tests;

import static org.junit.jupiter.api.Assertions.fail;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.iesvdm.streams.Cliente;
import org.iesvdm.streams.ClienteHome;
import org.iesvdm.streams.Comercial;
import org.iesvdm.streams.ComercialHome;
import org.iesvdm.streams.Pedido;
import org.iesvdm.streams.PedidoHome;
import org.junit.jupiter.api.Test;

import com.mysql.cj.x.protobuf.MysqlxCrud.Limit;

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
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2017 = sdf2.parse("2018-00-00");
			
			List<Pedido> list = pedHome.findAll();
			List<Pedido> list2017 = list.stream()
										 .filter(t -> t.getFecha().after(ultimoDia2016))
										 .filter(t -> t.getFecha().before(ultimoDia2017))
										 .filter(t -> t.getTotal() > 500)
										 .collect(toList());	
			list2017.forEach(System.out::println);
						
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
			
			List<Integer> ids = list.stream()
								.filter(t -> t.getPedidos().isEmpty())
								.map(Cliente::getId)
								.collect(toList());		
			ids.forEach(System.out::println);
			
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
			
			Optional<Float> comMayor = list.stream()
											.map(Comercial::getComisión)
											.max((o1, o2) -> o1.compareTo(o2));
			
			System.out.println(comMayor.get());
				
			comHome.commitTransaction();
			
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
			
			List<String> listIdNombre = list.stream()
											.filter(t -> t.getApellido2() != null)
											.sorted((t1, t2) -> t1.getApellido1().compareTo(t2.getApellido1()))
											.sorted((t1, t2) -> t1.getNombre().compareTo(t2.getNombre()))
											.map(t -> "ID: " + t.getId() + " Nombre: " + t.getNombre() + " Apellido 1: " + t.getApellido1())
											.collect(toList());
			
			listIdNombre.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
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
			
			List<String> listNomFinalList = list.stream()
												.filter(t -> t.getNombre().endsWith("el") || t.getNombre().endsWith("o"))
												.map(Comercial::getNombre)
												.distinct()
												.collect(toList());
											

			listNomFinalList.forEach(System.out::println);
			
			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
	 * @throws ParseException 
	 */
	@Test
	void test6() throws ParseException {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2017 = sdf2.parse("2018-00-00");
			
			List<Cliente> list2017 = list.stream()
										 .filter(t -> t.getFecha().after(ultimoDia2016))
										 .filter(t -> t.getFecha().before(ultimoDia2017))
										 .filter(t -> t.getTotal() >= 300)
										 .filter(t -> t.getTotal() <= 1000)
										 .map(Pedido::getCliente)
										 .collect(toList());
		
			list2017.forEach(System.out::println);
			
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
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();
			List<Pedido> listPedidos = pedHome.findAll();
			
			List<Set> pedidos = list.stream()
										 .filter(t -> t.getNombre().equals("Daniel") && t.getApellido1().equals("Sáez"))
										 .map(t -> t.getPedidos())
										 .collect(toList());
			
			
			
					// falta la media					 
		
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 8. Devuelve un listado con todos los pedidos que se han realizado.
	 *  Los pedidos deben estar ordenados por la fecha de realización
	 * , mostrando en primer lugar los pedidos más recientes
	 */
	@Test
	void test8() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			List<Pedido> recientes = list.stream()
										 .sorted((o1, o2) -> -o1.getFecha().compareTo(o2.getFecha()))
										 .collect(toList());
			
			recientes.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 9. Devuelve todos los datos de los dos pedidos de mayor valor.
	 */
	@Test
	void test9() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			List<Pedido> mayorValor2 = list.stream()
										   .sorted((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()))
										   .limit(2)
										   .collect(toList());
			
			mayorValor2.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 10. Devuelve un listado con los identificadores de los clientes que han realizado algún pedido. 
	 * Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
	 */
	@Test
	void test10() {
		
		PedidoHome pedHome = new PedidoHome();	
		
		try {
			pedHome.beginTransaction();
			
		
			List<Pedido> list = pedHome.findAll();
			
			List<Integer> listId = list.stream()
										   .map(t -> t.getCliente().getId())
										   .distinct()
										   .collect(toList());
			
			listId.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 11. Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
	 * 
	 */
	@Test
	void test11() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			List<String> listNomApe = list.stream()
										  .filter(t -> t.getComisión() >= 0.05 && t.getComisión() <= 0.11)
										  .map(t -> t.getNombre() + " " + t.getApellido1())
										  .collect(toList());
			
			listNomApe.forEach(System.out::println);
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 12. Devuelve el valor de la comisión de menor valor que existe para los comerciales.
	 * 
	 */
	@Test
	void test12() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			Optional<Float> menorCom = list.stream()
											.map(t -> t.getComisión())
											.min((o1, o2) -> o1.compareTo(o2));
			
			menorCom.ifPresent(System.out::println);
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 13. Devuelve un listado de los nombres de los comercial que 
	 * empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 * 
	 */
	@Test
	void test13() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			List<String> nombres = list.stream()
									   .filter(t -> t.getNombre().startsWith("A") && t.getNombre().endsWith("n") || t.getNombre().startsWith("P"))
									   .map(Comercial::getNombre)
									   .sorted(Comparator.naturalOrder())
									   .collect(toList());
			
			nombres.forEach(System.out::println);
			
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 14. Devuelve un listado de los nombres de los clientes 
	 * que empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 */
	@Test
	void test14() {
		
		ClienteHome cliHome = new ClienteHome();
		
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			List<String> nombres = list.stream()
					   .filter(t -> t.getNombre().startsWith("A") && t.getNombre().endsWith("n") || t.getNombre().startsWith("P"))
					   .map(Cliente::getNombre)
					   .sorted(Comparator.naturalOrder())
					   .collect(toList());

			nombres.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 15. Devuelve un listado de los clientes cuyo nombre no empieza por A. 
	 * El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
	 */
	@Test
	void test15() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			List<Cliente> nombres = list.stream()
					.filter(t -> !(t.getNombre().startsWith("A")))
					.sorted((o1, o2) -> o1.getNombre().compareTo(o2.getNombre()))
					.sorted((o1, o2) -> o1.getApellido1().compareTo(o2.getApellido1()))
					.collect(toList());
			
			nombres.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 16. Devuelve un listado con el identificador, nombre y los apellidos de todos 
	 * los clientes que han realizado algún pedido. 
	 * El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
	 */
	@Test
	void test16() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			List<String> lisClientes = list.stream()
										   .map(t -> t.getCliente())
										   .sorted((o1,o2) -> o1.getApellido1().compareTo(o2.getApellido1()))
										   .sorted((o1,o2) -> o1.getNombre().compareTo(o2.getNombre()))
										   .distinct()
										   .map(t -> t.getId() + " Nombre: " + t.getNombre() + " Apellido1: " + t.getApellido1() + " Apellido2: " + t.getApellido2())
										   .collect(toList());
			
			lisClientes.forEach(System.out::println);
			//TODO STREAMS
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 17. Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente. 
	 * El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos. 
	 * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
	 * 
Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100]
	Pedido [id=2, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=270.65, fecha=2016-09-10]
	Pedido [id=16, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=2389.23, fecha=2019-03-11]
	Pedido [id=15, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=370.85, fecha=2019-03-11]
Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200]
	Pedido [id=12, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=3045.6, fecha=2017-04-25]
	Pedido [id=7, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=5760.0, fecha=2015-09-10]
	Pedido [id=3, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=65.26, fecha=2017-10-05]
	...
	 */
	@Test
	void test17() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			List<String> lisPedidos = list.stream()
										  .sorted((o1,o2) -> o1.getNombre().compareTo(o2.getNombre()))
										  .sorted((o1,o2) -> o1.getApellido1().compareTo(o2.getApellido1()))
										  .map(t -> t + "\n" + t.getPedidos())
										  .collect(toList());
			
			lisPedidos.forEach(System.out::println);
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 18. Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial. 
	 * El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos. 
	 * El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
	 */
	@Test
	void test18() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
		
			List<String> listado = list.stream()
									   .sorted((o1,o2)-> o1.getApellido1().compareTo(o2.getApellido1()))
									   .map(t -> t.getId() +" " + t.getNombre() + " " + t.getApellido1() + " " + t.getApellido2() +" "+ t.getComisión() + "\n" + t.getPedidos()).collect(toList());
			
			listado.forEach(System.out::println);
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 19. Devuelve el nombre y los apellidos de todos los comerciales que ha participado 
	 * en algún pedido realizado por María Santana Moreno.
	 */
	@Test
	void test19() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			List<String> nomApe = list.stream()
									  .filter(t -> t.getCliente().getNombre().equals("María")
											  && t.getCliente().getApellido1().equals("Santana") 
											  && t.getCliente().getApellido2().equals("Moreno"))
									  .map(t -> t.getComercial().getNombre() + " " + t.getComercial().getApellido1() + " " + t.getComercial().getApellido2())
									  .collect(toList());
			
			nomApe.forEach(System.out::println);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	/**
	 * 20. Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
	 */
	@Test
	void test20() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
		
			List<Comercial> sinPed = list.stream()
										 .filter(t -> t.getPedidos().isEmpty())
										 .map(t -> t)
										 .collect(toList());
			
			sinPed.forEach(System.out::println);
			
			
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 21. Calcula el número total de comerciales distintos que aparecen en la tabla pedido
	 */
	@Test
	void test21() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			Long totalCom = list.stream()
										 .map(t -> t.getComercial())
										 .distinct()
										 .count();
										 
			System.out.println(totalCom);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 22. Calcula el máximo y el mínimo de total de pedido en un solo stream, transforma el pedido a un array de 2 double total, utiliza reduce junto con el array de double para calcular ambos valores.
	 */
	@Test
	void test22() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			

			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 * 23. Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
	 */
	@Test
	void test23() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS
			
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 24. Calcula cuál es el máximo valor de los pedidos realizados 
	 * durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber 
	 * realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es 
	 * el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido. 
	 * Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.
	 * Pista: utiliza collect, groupingBy, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test24() {
		
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
	 *  25. Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes, 
	 *  teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
	 *  Pista: utiliza collect, groupingBy, filtering, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test25() {
		
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
	 *  26. Devuelve un listado con el identificador de cliente, nombre y apellidos 
	 *  y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
	 * @throws ParseException 
	 */
	@Test
	void test26() throws ParseException {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			Date primerDia2018 = sdf.parse("2018-01-01");
			
			//TODO STREAMS
			
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 27. Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año. El listado debe mostrarse ordenado por año.
	 */
	@Test
	void test27() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			
			//TODO STREAMS
					
					
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  28. Devuelve el número total de pedidos que se han realizado cada año.
	 */
	@Test
	void test28() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			//TODO STREAMS
					
							
					
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  29. Devuelve los datos del cliente que realizó el pedido
	 *  
	 *   más caro en el año 2019.
	 * @throws ParseException 
	 */
	@Test
	void test29() throws ParseException {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2018 = sdf.parse("2018-12-31");
			Date primerDia2020 = sdf.parse("2020-01-01");
			
			//TODO STREAMS
			
				
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  30. Calcula la estadísticas de total de todos los pedidos.
	 *  Pista: utiliza collect con summarizingDouble
	 */
	@Test
	void test30() throws ParseException {
		
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
	
}
