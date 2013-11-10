package com.example.calapalamos.library;

public class User {
	
	private String name;
	private String AuthToken;

	public User(){
		this.name = "";
		this.AuthToken = "";
	}

	public User(String n, String token){
		this.name = n;
		this.AuthToken = token;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	public void setAuthToken(String p){
		this.AuthToken = p;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getAuthToken(){
		return this.AuthToken;
	}
}
