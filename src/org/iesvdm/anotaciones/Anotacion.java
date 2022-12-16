package org.iesvdm.anotaciones;

import java.util.ArrayList;
import java.util.Scanner;

@Credencial(usuario = "usuario1", passwds = {"1234", "admin1234"})
@Credencial(usuario = "usuario2", passwds = {"admin", "root"}) 

public class Anotacion {
	private ArrayList<String> usuario;
	private ArrayList<String> pass;
	
	/**
	 * @param usuario
	 * @param pass
	 */
	public Anotacion() {
		usuario = new ArrayList<>();
		pass = new ArrayList<>();
		
		Credenciales cred = Anotacion.class.getAnnotation(Credenciales.class);
		
		this.usuario.add(cred.value()[0].usuario());
		this.usuario.add(cred.value()[1].usuario());
		
		this.pass.add(cred.value()[0].passwds ());
		this.pass.add(cred.value()[1].passwds ());
	}
	
	public static void anotacion(String usuario, String[] passwds) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Nombre de usuario: ");
		String user = sc.next();
		
		System.out.print("Contraseña: ");
		String contra = sc.next();
		
		if (usuario.equals(this.usuario) && ( (contra.equals(this.pass[0])) || (contra.equals(this.pass[1])) ){
			System.out.println("ACCESO");
		} else {
			System.out.println("ACCESO DENEGADO");
		}
	}

}
