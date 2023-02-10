package ru.practicum.main.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;
    @Column(name = "description", nullable = false, length = 7000)
    private String description;
    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "published")
    private LocalDateTime published;
    @Column(name = "lat", nullable = false)
    private float lat;
    @Column(name = "lon", nullable = false)
    private float lon;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "participantLimit", nullable = false)
    private Integer participantLimit;
    @Column(name = "requestModeration", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;

    public enum EventState {
        PENDING,
        PUBLISHED,
        CANCELED
    }

    public enum SortState {
        EVENT_DATE,
        VIEWS
    }
}