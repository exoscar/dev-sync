package org.devsync.spring.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.project.dto.CreateProjectRequest;
import org.devsync.spring.project.dto.ProjectResponse;
import org.devsync.spring.project.dto.UpdateProjectRequest;
import org.devsync.spring.project.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditor;
import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    public ApiResponse<ProjectResponse> createProject(@PathVariable String workspaceId,
                                                      @Valid @RequestBody CreateProjectRequest request){
        ProjectResponse response = projectService.createProject(workspaceId,request);
        return ApiResponseUtil.success(response,"Project creation success");
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects(@PathVariable String workspaceId){
        List<ProjectResponse> responses = projectService.getAllProjects(workspaceId);
        return ApiResponseUtil.success(responses);
    }

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectResponse> getProjectById(@PathVariable String workspaceId,@PathVariable String projectId){
        ProjectResponse response = projectService.getProjectById(workspaceId,projectId);
        return ApiResponseUtil.success(response);
    }

    @PutMapping("/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable String workspaceId, @PathVariable String projectId,
                                                      @Valid @RequestBody UpdateProjectRequest request){
        ProjectResponse response = projectService.updateProject(workspaceId,projectId,request);
        return ApiResponseUtil.success(response,"Project update successful");
    }

    @DeleteMapping("/{projectId}")
    public ApiResponse<Void> deleteProject(@PathVariable String workspaceId,@PathVariable String projectId){
        projectService.deleteProject(workspaceId,projectId);
        return ApiResponseUtil.success("Project deletion successful");
    }
}
