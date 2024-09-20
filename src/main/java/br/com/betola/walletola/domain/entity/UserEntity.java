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

    public UserEntity() {
    }

    public UserEntity(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
