package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@DiscriminatorColumn(name="accountType", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BasicEntity {


    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, updatable = false, insertable = false)
    private AccountType accountType;

    private String email;
    private String password;
    private String name;

    private Boolean disabled = false;

    //TODO: Implement Spring Security roles system
    private Boolean isAdmin = false;

    public enum AccountType {

        ADMIN("ADMIN"),
        TEACHER("TEACHER"),
        STUDENT("STUDENT");

        private final String value;

        AccountType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}
