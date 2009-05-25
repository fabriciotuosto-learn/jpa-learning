package org.jpa.learning.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Libro {

	private Long id;
	private String isbn;
	private String titulo;
	private String autor;

	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(null)
				 .append(id)
		         .append(isbn)
		         .append(autor)
				 .append(titulo)
		         .toString();
	}
}

