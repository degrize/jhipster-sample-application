package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BesoinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Besoin.class);
        Besoin besoin1 = new Besoin();
        besoin1.setId(1L);
        Besoin besoin2 = new Besoin();
        besoin2.setId(besoin1.getId());
        assertThat(besoin1).isEqualTo(besoin2);
        besoin2.setId(2L);
        assertThat(besoin1).isNotEqualTo(besoin2);
        besoin1.setId(null);
        assertThat(besoin1).isNotEqualTo(besoin2);
    }
}
