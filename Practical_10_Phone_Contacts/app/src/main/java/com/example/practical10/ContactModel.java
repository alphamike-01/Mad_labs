package com.example.practical10;

/**
 * Model representing a contact entry with name and phone number.
 */
public class ContactModel {
    private final String name;
    private final String phoneNumber;

    public ContactModel(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
