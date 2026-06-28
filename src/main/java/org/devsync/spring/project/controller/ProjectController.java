package org.devsync.spring.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.response.ApiResponse;
import org.devsync.spring.common.util.ApiResponseUtil;
import org.devsync.spring.project.dto.CreateProjectRequest;
import org.devsync.spring.project.dto.ProjectResponse;
import org.devsync.spring.project.dto.UpdateProjectRequest;
import org.devsync.spring.project.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Projects")
@RestController
@RequestMapping("/workspaces/{workspaceId}/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Create Project")
    @PostMapping()
    public ApiResponse<ProjectResponse> createProject(@PathVariable String workspaceId,
                                                      @Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(workspaceId, request);
        return ApiResponseUtil.success(response, "Project creation success");
    }

    @Operation(summary = "Get All Projects")
    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects(@PathVariable String workspaceId) {
        List<ProjectResponse> responses = projectService.getAllProjects(workspaceId);
        return ApiResponseUtil.success(responses);
    }

    @Operation(summary = "Get Project By Id")
    @GetMapping("/{projectId}")
    public ApiResponse<ProjectResponse> getProjectById(@PathVariable String workspaceId, @PathVariable String projectId) {
        ProjectResponse response = projectService.getProjectById(workspaceId, projectId);
        return ApiResponseUtil.success(response);
    }

    @Operation(summary = "Update Project")
    @PutMapping("/{projectId}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable String workspaceId, @PathVariable String projectId,
                                                      @Valid @RequestBody UpdateProjectRequest request) {
        ProjectResponse response = projectService.updateProject(workspaceId, projectId, request);
        return ApiResponseUtil.success(response, "Project update successful");
    }

    @Operation(summary = "Delete Project")
    @DeleteMapping("/{projectId}")
    public ApiResponse<Void> deleteProject(@PathVariable String workspaceId, @PathVariable String projectId) {
        projectService.deleteProject(workspaceId, projectId);
        return ApiResponseUtil.success("Project deletion successful");
    }
}
