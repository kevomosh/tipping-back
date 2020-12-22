package com.kakuom.finaltipping.model;

import com.kakuom.finaltipping.enums.Comp;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(length = 3)
    private Comp comp;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();


    public Groups() {
    }

    public Groups(String name, Comp comp) {
        this.name = name;
        this.comp = comp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comp getComp() {
        return comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groups groups = (Groups) o;

        if (!id.equals(groups.id)) return false;
        return name.equals(groups.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
