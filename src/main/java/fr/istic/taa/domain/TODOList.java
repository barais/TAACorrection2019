package fr.istic.taa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TODOList.
 */
@Entity
@Table(name = "todo_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TODOList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "todolist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "affecteduser", "tags", "todolist" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TODOList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public TODOList nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setTodolist(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setTodolist(this));
        }
        this.tasks = tasks;
    }

    public TODOList tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public TODOList addTasks(Task task) {
        this.tasks.add(task);
        task.setTodolist(this);
        return this;
    }

    public TODOList removeTasks(Task task) {
        this.tasks.remove(task);
        task.setTodolist(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TODOList user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TODOList)) {
            return false;
        }
        return id != null && id.equals(((TODOList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TODOList{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
