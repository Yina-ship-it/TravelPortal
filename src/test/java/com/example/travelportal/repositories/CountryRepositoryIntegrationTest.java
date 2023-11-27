package com.example.travelportal.repositories;

import com.example.travelportal.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CountryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void findByName() {
        // Arrange
        Country country = Country.builder().name("CountryTest").capital("CapitalTest").build();
        entityManager.persist(country);
        entityManager.flush();

        // Act
        Country result = countryRepository.findByName(country.getName()).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals(country, result);
    }
}