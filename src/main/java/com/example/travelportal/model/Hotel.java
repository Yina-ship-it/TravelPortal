package com.example.travelportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "country_id"})
})
@Entity
public class Hotel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false)
    @DecimalMin("1")
    @DecimalMax("5")
    private Integer stars;

    @Pattern(regexp = "^(http://|https://)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(/.*)?$")
    private String website;
}
