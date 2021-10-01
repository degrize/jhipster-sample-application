package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunauteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Communaute.class);
        Communaute communaute1 = new Communaute();
        communaute1.setId(1L);
        Communaute communaute2 = new Communaute();
        communaute2.setId(communaute1.getId());
        assertThat(communaute1).isEqualTo(communaute2);
        communaute2.setId(2L);
        assertThat(communaute1).isNotEqualTo(communaute2);
        communaute1.setId(null);
        assertThat(communaute1).isNotEqualTo(communaute2);
    }
}
