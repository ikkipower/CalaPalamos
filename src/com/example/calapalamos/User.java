package com.example.calapalamos;

public class User {
	
	private String name;
	private String passwd;

	public User(){
		this.name = "";
		this.passwd = "";
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public void setPasswd(String p){
		this.passwd = p;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPasswd(){
		return this.passwd;
	}
}
