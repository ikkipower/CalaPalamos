package com.example.calapalamos.library;

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
