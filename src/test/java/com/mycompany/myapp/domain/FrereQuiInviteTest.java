package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrereQuiInviteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrereQuiInvite.class);
        FrereQuiInvite frereQuiInvite1 = new FrereQuiInvite();
        frereQuiInvite1.setId(1L);
        FrereQuiInvite frereQuiInvite2 = new FrereQuiInvite();
        frereQuiInvite2.setId(frereQuiInvite1.getId());
        assertThat(frereQuiInvite1).isEqualTo(frereQuiInvite2);
        frereQuiInvite2.setId(2L);
        assertThat(frereQuiInvite1).isNotEqualTo(frereQuiInvite2);
        frereQuiInvite1.setId(null);
        assertThat(frereQuiInvite1).isNotEqualTo(frereQuiInvite2);
    }
}
