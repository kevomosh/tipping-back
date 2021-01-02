package com.kakuom.finaltipping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakuom.finaltipping.enums.Role;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20)
    private String name;

    @NaturalId
    @Column(length = 60)
    private String email;

    private String password;

    @Column(length = 1)
    private Role role;

    private Integer nrlTotalScore;

    private Integer nrlLastScore;

    private Integer aflTotalScore;

    private Integer aflLastScore;


    @JsonIgnore
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "group_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Groups> groups = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Pick> picks = new LinkedHashSet<>();

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private PassToken passToken;


    public User() {
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nrlTotalScore = 0;
        this.aflTotalScore = 0;
        this.nrlLastScore = 0;
        this.aflLastScore = 0;
    }

    public void addGroup(Groups group) {
        groups.add(group);
        group.getUsers().add(this);
    }

    public void removeGroup(Groups group) {
        groups.remove(group);
        group.getUsers().remove(this);
    }

    public void addPick(Pick pick) {
        picks.add(pick);
        pick.setUser(this);
    }

    public void removePick(Pick pick) {
        picks.remove(pick);
        pick.setUser(null);
    }

    public void setPassToken(PassToken passToken) {
        if (passToken == null) {
            if (this.passToken != null) {
                this.passToken.setUser(null);
            }
        } else {
            passToken.setUser(this);
        }
        this.passToken = passToken;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    public Set<Pick> getPicks() {
        return picks;
    }

    public void setPicks(Set<Pick> picks) {
        this.picks = picks;
    }

    public Integer getNrlTotalScore() {
        return nrlTotalScore;
    }

    public void setNrlTotalScore(Integer nrlTotalScore) {
        this.nrlTotalScore = nrlTotalScore;
    }

    public Integer getNrlLastScore() {
        return nrlLastScore;
    }

    public void setNrlLastScore(Integer nrlLastScore) {
        this.nrlLastScore = nrlLastScore;
    }

    public Integer getAflTotalScore() {
        return aflTotalScore;
    }

    public void setAflTotalScore(Integer aflTotalScore) {
        this.aflTotalScore = aflTotalScore;
    }

    public Integer getAflLastScore() {
        return aflLastScore;
    }

    public void setAflLastScore(Integer aflLastScore) {
        this.aflLastScore = aflLastScore;
    }

    public PassToken getPassToken() {
        return passToken;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!name.equals(user.name)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
