package com.merostore.backend.security.domain;

import com.merostore.backend.buyer.domain.Buyer;
import com.merostore.backend.security.domain.Role.Role;
import com.merostore.backend.seller.domain.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getSeller() {
        return seller.getId();
    }

    @Column(name = "mobile_number")
    @NotBlank
    private String mobileNumber;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "pin")
    @NotBlank
    private String pin;

    @Column(name = "role")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Seller seller;

    @OneToOne(mappedBy = "user")
    private Buyer buyer;

    public User(String mobileNumber, String pin) {
        this.mobileNumber = mobileNumber;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", pin='" + pin + '\'' +
                ", role=" + role +
                '}';
    }
}