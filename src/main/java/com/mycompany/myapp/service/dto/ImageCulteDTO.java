package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ImageCulte} entity.
 */
public class ImageCulteDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    @Lob
    private byte[] image;

    private String imageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageCulteDTO)) {
            return false;
        }

        ImageCulteDTO imageCulteDTO = (ImageCulteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imageCulteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCulteDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
