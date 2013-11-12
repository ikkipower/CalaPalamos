package com.example.calapalamos.library;

public class Kids{ 

private String name;
private int age;

  public Kids(){
	this.name = "";
	this.age = 0;
  }

  public Kids(String n, int a){
	this.name = n;
	this.age = a;
  }

  public void setName(String n){
	this.name = n;
  }

  public void setAge(int a){
	this.age = a;
  }

  public String getName(){
	return this.name;
  }

  public int getAge(){
	return this.age;
  }
} 