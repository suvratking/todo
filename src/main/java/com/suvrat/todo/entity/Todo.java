package com.suvrat.todo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "todo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    private String title;
    private boolean completed;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "todo_userEntities",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "userEntities_id"))
    @JsonManagedReference
    private Set<UserEntity> userEntities = new LinkedHashSet<>();

    public Todo(Long id, String title, boolean completed) {
        this.completed = completed;
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return new EqualsBuilder().append(completed, todo.completed).append(id, todo.id).append(title, todo.title).append(userEntities, todo.userEntities).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(title).append(completed).append(userEntities).toHashCode();
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", userEntities=" + userEntities +
                '}';
    }
}
