package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VilleMapperTest {

    private VilleMapper villeMapper;

    @BeforeEach
    public void setUp() {
        villeMapper = new VilleMapperImpl();
    }
}
