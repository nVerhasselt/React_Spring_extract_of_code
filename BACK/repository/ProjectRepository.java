package com.itso.occupancy.occupancy.repository;

import com.itso.occupancy.occupancy.model.Feature;
import com.itso.occupancy.occupancy.model.Project;
import com.itso.occupancy.occupancy.model.Client;
import com.itso.occupancy.occupancy.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByClientIdAndSupprimerIsFalseAndClientSupprimerIsFalse(Long id);
    
    @Query("FROM Project p WHERE p.supprimer IS FALSE AND p.client.supprimer IS FALSE ORDER BY p.name")
    List<Project>SupprimerIsFalseAndClientSupprimerIsFalseOrderByName();
    
    Optional<Project>findByIdAndSupprimerIsFalseAndClientSupprimerIsFalse(Long id);
    List<Project>findByNameContainingIgnoreCaseAndSupprimerIsFalseAndClientSupprimerIsFalseOrderByName(String name);

    @Query("FROM Project p WHERE p.jira_id = ?1")
    Project findProjectByJira_id(String jira_id);  
    
    @Query("FROM Project p WHERE p.supprimer IS FALSE")
    List<Project> findAllProjects();

    @Query("FROM Project")
    List<Project> findAll();

    @Query("FROM Project p " +
        "WHERE " +
       //"p.supprimer IS FALSE AND (p.client IS NULL OR (p.client IS NOT NULL AND p.client.supprimer IS FALSE)) AND " +
        "LOWER(p.name) LIKE '%' || LOWER(?1) || '%' "
    )
    List<Project> findAllProjectsBySearch(String search);

    @Query("FROM Project WHERE client IS NULL")
    List<Project> findProjectWithoutClient();
}
