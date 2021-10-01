package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Culte} entity.
 */
public class CulteDTO implements Serializable {

    private Long id;

    @NotNull
    private String theme;

    @NotNull
    private LocalDate date;

    private Set<ImageCulteDTO> imageCultes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<ImageCulteDTO> getImageCultes() {
        return imageCultes;
    }

    public void setImageCultes(Set<ImageCulteDTO> imageCultes) {
        this.imageCultes = imageCultes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CulteDTO)) {
            return false;
        }

        CulteDTO culteDTO = (CulteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, culteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CulteDTO{" +
            "id=" + getId() +
            ", theme='" + getTheme() + "'" +
            ", date='" + getDate() + "'" +
            ", imageCultes=" + getImageCultes() +
            "}";
    }
}
