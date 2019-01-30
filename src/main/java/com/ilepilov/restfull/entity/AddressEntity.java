package com.ilepilov.restfull.entity;

import com.ilepilov.restfull.dto.UserDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AddressEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 30, nullable = false)
    private String publicAddressId;

    @Column(length = 30, nullable = false)
    private String city;

    @Column(length = 30, nullable = false)
    private String country;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length = 7, nullable = false)
    private String postalCode;

    @Column(length = 10, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
}
