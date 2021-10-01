package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CulteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Culte.class);
        Culte culte1 = new Culte();
        culte1.setId(1L);
        Culte culte2 = new Culte();
        culte2.setId(culte1.getId());
        assertThat(culte1).isEqualTo(culte2);
        culte2.setId(2L);
        assertThat(culte1).isNotEqualTo(culte2);
        culte1.setId(null);
        assertThat(culte1).isNotEqualTo(culte2);
    }
}
