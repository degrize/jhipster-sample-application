package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.BesoinType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Besoin} entity.
 */
public class BesoinDTO implements Serializable {

    private Long id;

    @NotNull
    private BesoinType besoin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BesoinType getBesoin() {
        return besoin;
    }

    public void setBesoin(BesoinType besoin) {
        this.besoin = besoin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BesoinDTO)) {
            return false;
        }

        BesoinDTO besoinDTO = (BesoinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, besoinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BesoinDTO{" +
            "id=" + getId() +
            ", besoin='" + getBesoin() + "'" +
            "}";
    }
}
