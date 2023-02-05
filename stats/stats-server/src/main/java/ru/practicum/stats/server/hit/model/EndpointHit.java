package ru.practicum.stats.server.hit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpointhits", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "app", nullable = false, length = 128)
    private String app;
    @Column(name = "uri", nullable = false, length = 128)
    private String uri;
    @Column(name = "ip", nullable = false, length = 45)
    private String ip;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}

