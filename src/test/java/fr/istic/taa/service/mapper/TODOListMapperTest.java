package fr.istic.taa.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TODOListMapperTest {

    private TODOListMapper tODOListMapper;

    @BeforeEach
    public void setUp() {
        tODOListMapper = new TODOListMapperImpl();
    }
}
