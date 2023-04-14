package com.example.clientfx.modeleClientFx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Course implements Serializable {
    private final StringProperty name;
    private final StringProperty code;
    private final StringProperty session;


    public Course(String name, String code, String session) {
        this.name = new SimpleStringProperty(name);
        this.code =  new SimpleStringProperty(code);
        this.session =  new SimpleStringProperty(session);

    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {return name;}

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }
    public StringProperty codeProperty() {return code;}

    public String getSession() {return session.get();}

    public void setSession(String session) {this.session.set(session);}
   public StringProperty sessionProperty() {return code;}

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}

