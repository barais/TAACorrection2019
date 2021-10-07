package fr.istic.taa.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.istic.taa.domain.TODOList} entity.
 */
public class TODOListDTO implements Serializable {

    private Long id;

    private String nom;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TODOListDTO)) {
            return false;
        }

        TODOListDTO tODOListDTO = (TODOListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tODOListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TODOListDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
