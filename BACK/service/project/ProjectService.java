package com.itso.occupancy.occupancy.service.project;


import com.itso.occupancy.occupancy.dto.model.project.*;
import com.itso.occupancy.occupancy.dto.model.user.UserListDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ResponseEntity<List<ProjectDto>> getAllProjects();
    ResponseEntity<List<ProjectDto>> postAllProjects();
    ResponseEntity<ProjectDto> getProject(Long id);
    ResponseEntity<ProjectDto> postProject(ProjectIdDto projectIdDto);
    ResponseEntity<List<ProjectDto>> getAllProjectByClient(Long id);
    ResponseEntity<List<ProjectDto>> postAllProjectByClient(ProjectClientDto projectClientDto);
    ResponseEntity<ProjectDto> createProject(ProjectCreateDto projectCreateDto);
    ResponseEntity<Object> deleteProject(Long id);
    ResponseEntity<ProjectDto> updateProject(Long id, ProjectUpdateDto projectUpdateDto);
    ResponseEntity<Object> deleteAllProject(List<ProjectIdDto>  projectIdDto);
    ResponseEntity<List<ProjectDto>> rechercheByName(ProjectRechercheDto recherche);
    ResponseEntity<List<ProjectUserDto>> getAllProjectsAndUser();
    ResponseEntity <ProjectUserDto> getProjectsAndUser(Long id);

    ResponseEntity<ProjectListDto> getProjectList(String search);

    ResponseEntity <List<ProjectWithoutClientDto>> getProjectWithoutClient();
    ResponseEntity<ClientUpdateOutputDto> getClientWithProjects(Long clientId);
}