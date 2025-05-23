package com.migros.couriertracking.courier.entity;

import com.migros.couriertracking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "courier", indexes = {@Index(name = "idx_courier_identity_number", columnList = "identity_number")})
public class CourierEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "identity_number", nullable = false, unique = true, length = 11)
    private String identityNumber;
}
