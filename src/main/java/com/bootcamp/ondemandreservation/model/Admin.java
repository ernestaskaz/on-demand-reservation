package com.bootcamp.ondemandreservation.model;

import javax.persistence.Entity;

@Entity
public class Admin extends ODRUser{
    public static final String ADMIN_ROLE = "hasRole('ROLE_ADMIN')";

    {
        setAccountType("ADMIN");
    }

    public Admin() {

    }

    public Admin(Long id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }
}
