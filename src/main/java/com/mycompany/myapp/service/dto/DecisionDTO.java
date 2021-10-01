package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.DecisionType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Decision} entity.
 */
public class DecisionDTO implements Serializable {

    private Long id;

    @NotNull
    private DecisionType decision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DecisionType getDecision() {
        return decision;
    }

    public void setDecision(DecisionType decision) {
        this.decision = decision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionDTO)) {
            return false;
        }

        DecisionDTO decisionDTO = (DecisionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, decisionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionDTO{" +
            "id=" + getId() +
            ", decision='" + getDecision() + "'" +
            "}";
    }
}
