package com.itso.occupancy.occupancy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "project",
        indexes = {
        @Index(columnList = "id_client",name = "ix_client")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Project extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

//     @JsonBackReference
//     @ManyToOne(cascade = CascadeType.ALL )
//     @JoinColumn(name="id_client", nullable = false) 
//     private Client client;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_client", nullable = true)
    private Client client;

    @JsonManagedReference
    @OneToMany(mappedBy = "project")
    private List<WorkTask> workTasks;

    @JsonBackReference
    @OneToMany(mappedBy = "project")
    private List<Feature> features;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false") // Default value
    private Boolean supprimer = false ;
    
    @Column(nullable = true, unique = true)
    private String jira_id;
}
