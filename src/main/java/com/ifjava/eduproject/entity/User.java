package com.ifjava.eduproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@DiscriminatorColumn(name="role", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, updatable = false, insertable = false)
    private Role role;
    private String email;
    private String password;
    private String name;
    private LocalDateTime created = LocalDateTime.now();
    private Boolean disabled = false;

    public enum Role {

        ADMIN("ADMIN"),
        TEACHER("TEACHER"),
        STUDENT("STUDENT");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

}
