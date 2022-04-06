package com.bootcamp.ondemandreservation.model;

import javax.persistence.Entity;

@Entity
public class Admin extends ODRUser{
    {
        setAccountType("ADMIN");
    }

    public Admin() {

    }

    public Admin(Long id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }
}
