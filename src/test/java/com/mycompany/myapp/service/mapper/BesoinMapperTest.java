package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BesoinMapperTest {

    private BesoinMapper besoinMapper;

    @BeforeEach
    public void setUp() {
        besoinMapper = new BesoinMapperImpl();
    }
}
