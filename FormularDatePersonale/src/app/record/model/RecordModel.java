/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.record.model;

/**
 * Model class for prsonal data record
 * @author mihael.buzdugan
 */
public class RecordModel {
    
    private String name;
    private String famillyName;
    private String address;
    private String phoneNumber;

    public RecordModel(String name, String famillyName, String address, String phoneNumber) {
        this.name = name;
        this.famillyName = famillyName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamillyName() {
        return famillyName;
    }

    public void setFamillyName(String famillyName) {
        this.famillyName = famillyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "name=" + name + ", famillyName=" + famillyName + ", address=" + address + ", phoneNumber=" + phoneNumber;
    }
    
}
