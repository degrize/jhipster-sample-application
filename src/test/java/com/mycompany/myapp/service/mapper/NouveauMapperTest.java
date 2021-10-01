package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NouveauMapperTest {

    private NouveauMapper nouveauMapper;

    @BeforeEach
    public void setUp() {
        nouveauMapper = new NouveauMapperImpl();
    }
}
