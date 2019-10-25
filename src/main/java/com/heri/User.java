package com.heri;

public class User {

  private String id;
  private String firstName;
  private String lastName;

  public User(String id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getId() {
    return id;
  }
}
