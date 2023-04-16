package com.example.clientfx.modeleClientFx;

//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Course implements Serializable {
    private String name;
    private String code;
    private String session;


    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
/*
    private final StringProperty name;
    private final StringProperty code;
    private final StringProperty session;


    public Course(String name, String code, String session) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
        this.session = new SimpleStringProperty(session);

    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public String getSession() {
        return session.get();
    }

    public void setSession(String session) {
        this.session.set(session);
    }

    public StringProperty sessionProperty() {
        return session;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}

*/
