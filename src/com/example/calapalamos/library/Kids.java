package com.example.calapalamos.library;

/**
 * Clase que sirve para almacenar los datos de los kids.
 * 
 * @author sergio
 *
 */
public class Kids {
	
	private String name;
	private String age;
	
	public Kids(){
		this.name = "";
		this.age = "";
	}
	
	public void setKid(String name, String age){
		this.name = name;
		this.age = age;		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}

}
