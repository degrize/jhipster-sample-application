package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageCulteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageCulte.class);
        ImageCulte imageCulte1 = new ImageCulte();
        imageCulte1.setId(1L);
        ImageCulte imageCulte2 = new ImageCulte();
        imageCulte2.setId(imageCulte1.getId());
        assertThat(imageCulte1).isEqualTo(imageCulte2);
        imageCulte2.setId(2L);
        assertThat(imageCulte1).isNotEqualTo(imageCulte2);
        imageCulte1.setId(null);
        assertThat(imageCulte1).isNotEqualTo(imageCulte2);
    }
}
