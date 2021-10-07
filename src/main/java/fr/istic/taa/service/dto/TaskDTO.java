package fr.istic.taa.service.dto;

import fr.istic.taa.domain.enumeration.RepetitionType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;

/**
 * A DTO for the {@link fr.istic.taa.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private String libelle;

    private LocalDate dateButoire;

    private RepetitionType repetition;

    private Duration dureeEstime;

    private String lieu;

    private String url;

    @Lob
    private String description;

    private UserDTO affecteduser;

    private Set<TagDTO> tags = new HashSet<>();

    private TODOListDTO todolist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateButoire() {
        return dateButoire;
    }

    public void setDateButoire(LocalDate dateButoire) {
        this.dateButoire = dateButoire;
    }

    public RepetitionType getRepetition() {
        return repetition;
    }

    public void setRepetition(RepetitionType repetition) {
        this.repetition = repetition;
    }

    public Duration getDureeEstime() {
        return dureeEstime;
    }

    public void setDureeEstime(Duration dureeEstime) {
        this.dureeEstime = dureeEstime;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDTO getAffecteduser() {
        return affecteduser;
    }

    public void setAffecteduser(UserDTO affecteduser) {
        this.affecteduser = affecteduser;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public TODOListDTO getTodolist() {
        return todolist;
    }

    public void setTodolist(TODOListDTO todolist) {
        this.todolist = todolist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateButoire='" + getDateButoire() + "'" +
            ", repetition='" + getRepetition() + "'" +
            ", dureeEstime='" + getDureeEstime() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", affecteduser=" + getAffecteduser() +
            ", tags=" + getTags() +
            ", todolist=" + getTodolist() +
            "}";
    }
}
