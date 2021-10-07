package fr.istic.taa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.istic.taa.domain.enumeration.RepetitionType;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "date_butoire")
    private LocalDate dateButoire;

    @Enumerated(EnumType.STRING)
    @Column(name = "repetition")
    private RepetitionType repetition;

    @Column(name = "duree_estime")
    private Duration dureeEstime;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    private User affecteduser;

    @ManyToMany
    @JoinTable(name = "rel_task__tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tasks", "user" }, allowSetters = true)
    private TODOList todolist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Task libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateButoire() {
        return this.dateButoire;
    }

    public Task dateButoire(LocalDate dateButoire) {
        this.setDateButoire(dateButoire);
        return this;
    }

    public void setDateButoire(LocalDate dateButoire) {
        this.dateButoire = dateButoire;
    }

    public RepetitionType getRepetition() {
        return this.repetition;
    }

    public Task repetition(RepetitionType repetition) {
        this.setRepetition(repetition);
        return this;
    }

    public void setRepetition(RepetitionType repetition) {
        this.repetition = repetition;
    }

    public Duration getDureeEstime() {
        return this.dureeEstime;
    }

    public Task dureeEstime(Duration dureeEstime) {
        this.setDureeEstime(dureeEstime);
        return this;
    }

    public void setDureeEstime(Duration dureeEstime) {
        this.dureeEstime = dureeEstime;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Task lieu(String lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getUrl() {
        return this.url;
    }

    public Task url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAffecteduser() {
        return this.affecteduser;
    }

    public void setAffecteduser(User user) {
        this.affecteduser = user;
    }

    public Task affecteduser(User user) {
        this.setAffecteduser(user);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Task tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Task addTags(Tag tag) {
        this.tags.add(tag);
        tag.getTasks().add(this);
        return this;
    }

    public Task removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getTasks().remove(this);
        return this;
    }

    public TODOList getTodolist() {
        return this.todolist;
    }

    public void setTodolist(TODOList tODOList) {
        this.todolist = tODOList;
    }

    public Task todolist(TODOList tODOList) {
        this.setTodolist(tODOList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateButoire='" + getDateButoire() + "'" +
            ", repetition='" + getRepetition() + "'" +
            ", dureeEstime='" + getDureeEstime() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
