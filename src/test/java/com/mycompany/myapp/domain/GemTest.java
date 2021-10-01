package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gem.class);
        Gem gem1 = new Gem();
        gem1.setId(1L);
        Gem gem2 = new Gem();
        gem2.setId(gem1.getId());
        assertThat(gem1).isEqualTo(gem2);
        gem2.setId(2L);
        assertThat(gem1).isNotEqualTo(gem2);
        gem1.setId(null);
        assertThat(gem1).isNotEqualTo(gem2);
    }
}
