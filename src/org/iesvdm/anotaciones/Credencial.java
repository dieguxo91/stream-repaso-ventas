package org.iesvdm.anotaciones;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

@Retention(RUNTIME)
@Repeatable(Credenciales.class)
public @interface Credencial {
	String usuario();
	String[] passwds();
}
