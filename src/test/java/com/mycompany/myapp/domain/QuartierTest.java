package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuartierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quartier.class);
        Quartier quartier1 = new Quartier();
        quartier1.setId(1L);
        Quartier quartier2 = new Quartier();
        quartier2.setId(quartier1.getId());
        assertThat(quartier1).isEqualTo(quartier2);
        quartier2.setId(2L);
        assertThat(quartier1).isNotEqualTo(quartier2);
        quartier1.setId(null);
        assertThat(quartier1).isNotEqualTo(quartier2);
    }
}
