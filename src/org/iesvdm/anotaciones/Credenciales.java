package org.iesvdm.anotaciones;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Credenciales {
	Credencial[] value();
}