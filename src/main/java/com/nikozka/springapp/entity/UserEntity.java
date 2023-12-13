package com.nikozka.springapp.entity;

import javax.persistence.*;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;

    private String lastName;
    @Column(unique = true)
    private String iin;

    public UserEntity(String firstName, String lastName, String iin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.iin = iin;
    }

    public UserEntity() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
