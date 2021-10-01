package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Guard} entity.
 */
public class GuardDTO implements Serializable {

    private Long id;

    private GemDTO guard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GemDTO getGuard() {
        return guard;
    }

    public void setGuard(GemDTO guard) {
        this.guard = guard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuardDTO)) {
            return false;
        }

        GuardDTO guardDTO = (GuardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuardDTO{" +
            "id=" + getId() +
            ", guard=" + getGuard() +
            "}";
    }
}
