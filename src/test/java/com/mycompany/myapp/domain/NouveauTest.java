package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NouveauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nouveau.class);
        Nouveau nouveau1 = new Nouveau();
        nouveau1.setId(1L);
        Nouveau nouveau2 = new Nouveau();
        nouveau2.setId(nouveau1.getId());
        assertThat(nouveau1).isEqualTo(nouveau2);
        nouveau2.setId(2L);
        assertThat(nouveau1).isNotEqualTo(nouveau2);
        nouveau1.setId(null);
        assertThat(nouveau1).isNotEqualTo(nouveau2);
    }
}
