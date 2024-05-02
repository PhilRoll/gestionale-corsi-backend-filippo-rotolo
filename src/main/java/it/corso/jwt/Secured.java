package it.corso.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

@NameBinding //una specie di legatura di un elemento gia esistente di JXA-RS
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD }) //possiamo metterlo sopra i metodi o sopra le classi
public @interface Secured {
	
	String role() default "all";
}
