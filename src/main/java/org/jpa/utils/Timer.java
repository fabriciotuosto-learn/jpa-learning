package org.jpa.utils;

import java.util.concurrent.TimeUnit;

/**
 * Clase utilitaria para medir los tiempos de ejecucion
 * 
 * @author ftuosto
 *
 */
public class Timer {
	
	private final long startTimeMilis;
	private volatile Long endTimeMilis;
	private volatile long calculatedTime;
	private Object lock = new Object();
	
	public Timer() {
		startTimeMilis = System.nanoTime();
	}

	/**
	 * Termina de contar el tiempo de ejecucion
	 * @return
	 */
	public Timer elapse(){
		if(endTimeMilis == null){
			synchronized (lock) {
				if (endTimeMilis == null){
					endTimeMilis = System.nanoTime();
					calculatedTime = endTimeMilis - startTimeMilis;
				}
			}

		}
		return this;
	}
	
	/**
	 * @see Timer.elapsedTimeMessage pero en milisegundos
	 * 
	 * @return
	 */
	public String elapsedTimeMessage() {
		return elapsedTimeMessage(TimeUnit.MILLISECONDS);
	}
	
	/**
	 * @see Timer.elapsedTimeMessage pero en segundos
	 * 
	 * @return
	 */
	public String elapsedTimeSecondsMessage() {
		return elapsedTimeMessage(TimeUnit.SECONDS);
	}

	/**
	 * Devuelve un mensaje del estilo "Finished in 10 SECONDS"
	 * donde 10 es el tiempo que se demoro la ejecucion y seconds
	 * es el nombre del @see {@link TimeUnit} que se utilizo 
	 * como parametro
	 * 
	 * @param unit
	 * @return
	 */
	public String elapsedTimeMessage(TimeUnit unit) {
		elapse();
		return String.format("Finished in %d %s", elapsedTime(unit),unit.name());
	}	
	
	/**
	 * 
	 * Devuelve el tiempo de ejecucion en la unidad dada por 
	 * el parametro @see {@link TimeUnit}
	 * @param unit
	 * @return
	 */
	public Long elapsedTime(TimeUnit unit) {
		elapse();
		return unit.convert(elapsedTime(),TimeUnit.NANOSECONDS);
	}

	/**
	 * devuelve el tiempo de ejecucion en nanosegundos
	 * @return
	 */
	public Long elapsedTime() {
		elapse();
		return calculatedTime;
	}
}
