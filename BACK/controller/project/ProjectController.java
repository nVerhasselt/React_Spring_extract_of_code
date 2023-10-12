package com.itso.occupancy.occupancy.controller.project;

import com.itso.occupancy.occupancy.dto.model.project.*;
import com.itso.occupancy.occupancy.dto.model.user.UserListDto;
import com.itso.occupancy.occupancy.model.Project;
import com.itso.occupancy.occupancy.service.project.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    @GetMapping("")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectDto>> getAllProjects() { //Récupère tous les projects en get
        return projectService.getAllProjects();
    }

    @PostMapping("")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectDto>> postAllProjects() { //Récupère tous les projects en post
        return projectService.postAllProjects();
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id){ //Récupère un project en get
        return projectService.getProject(id);
    }

    @PostMapping("/id")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectDto>  postProject(@RequestBody ProjectIdDto projectIdDto){ //Récupère un project pour un id donné par post
        return projectService.postProject(projectIdDto);
    }

    @GetMapping("/client/{id}")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectDto>> getAllProjectByClient(@PathVariable Long id){ //Récupère tous les projects pour un client donné par get
        return projectService.getAllProjectByClient(id);
    }

    @PostMapping("/client")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectDto>> postAllProjectByClient(@RequestBody ProjectClientDto projectClientDto){ //Récupère tous les projects pour un client donné par post
        return projectService.postAllProjectByClient(projectClientDto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectCreateDto projectCreateDto) { //Crée un project
        return projectService.createProject(projectCreateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<Object> deleteProject(@PathVariable Long id){ //Supprime un project
        return projectService.deleteProject(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id,@RequestBody ProjectUpdateDto projectUpdateDto) { //Modifie un project
        return projectService.updateProject(id, projectUpdateDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<Object> deleteAllProject(@RequestBody List<ProjectIdDto>  projectIdDto){ //Supprime plusieurs projects
        return projectService.deleteAllProject(projectIdDto);
    }

    @PostMapping("/recherche")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectDto>> rechercheByName(@RequestBody ProjectRechercheDto recherche) { //Recherche
        return projectService.rechercheByName(recherche);
    }

    @GetMapping("/user")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<List<ProjectUserDto>> getAllProjectsAndUser() { //Récupère tous les projects avec les utilisateurs concernés et la temps pour le project
        return projectService.getAllProjectsAndUser();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectUserDto> getProjectsAndUser(@PathVariable Long id) {
        return projectService.getProjectsAndUser(id);
    }

    @GetMapping("/projectList")
    @PreAuthorize("hasPermission(returnObject, 'export')")
    public ResponseEntity<ProjectListDto> getProjectList(@RequestParam String search) {
        return projectService.getProjectList(search);
    }

    @GetMapping("/projectWithoutClient")
    public ResponseEntity<List<ProjectWithoutClientDto>> getProjectWithoutClient() {
        return projectService.getProjectWithoutClient();
    }
}
