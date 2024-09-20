package br.com.betola.walletola.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public UserEntity() {
    }

    public UserEntity(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
