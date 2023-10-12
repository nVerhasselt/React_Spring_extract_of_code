package com.itso.occupancy.occupancy.service.workTask;

import com.itso.occupancy.occupancy.dto.model.workTask.*;
import com.itso.occupancy.occupancy.model.WorkTask;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface WorkTaskService {
    public ResponseEntity<List<WorkTaskWithoutDto>> getAllWorkTasks();
    public ResponseEntity<WorkTaskDto> getWorkTask(Long id);
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByWorkTime(int id);
    public ResponseEntity<List<WorkTaskWithoutDto>> getAllWorkTaskByIdUser(Long id);

    public ResponseEntity<UserTaskListDto> getAllWorkTasksByDays(DaysTaskInputDto daysTaskInputDto);
    
    public ResponseEntity<WeekListDto> getAllWorkTaskByWeek(WeekTaskInputDto weekTaskInputDto);
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByProject (Long id);
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByTag (Long id);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTasks();
    public ResponseEntity<WorkTaskDto> postWorkTask(WorkTaskIdDto workTaskIdDto);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByWorkTime(WorkTaskWorkTimeDto workTaskWorkTimeDto);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByIdUser(WorkTaskIdUserDto workTaskIdUserDto);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByProject (WorkTaskIdProjectDto workTaskIdProjectDto);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByTag (WorkTaskIdTagDto workTaskIdTagDto);
    public ResponseEntity<WorkTaskDto> createNewTask(WorkTaskCreateDto workTask);
    ResponseEntity<WorkTaskDto> deleteWorkTask(Long id);
    ResponseEntity<WorkTaskDto> updateWorkTask(Long id, WorkTaskUptadeDto workTaskUptadeDto);
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByIdFeature(Long id);
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByIdFeature(WorkTaskIdFeatureDto workTaskIdFeatureDto);
    public ResponseEntity<WorkTaskWithoutDto> postAddCommentaire(WorkTaskCommentaireDto workTaskCommentaireDto);
    public ResponseEntity<WorkTaskCreateWithoutDto> createNewTaskWithout(WorkTaskCreateWithoutDto workTask);
    public ResponseEntity<WorkTaskWithoutDto> getWorkTaskWithout(Long id);
    public ResponseEntity<WorkTaskVideDto> getWorkTaskVide(Long id);
    public ResponseEntity<WorkTaskWithoutDto> putChangeWorkTime(Long id,WorkTaskWorkTimeDto workTaskWorkTimeDto);
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskVideFalseByUser(Long id);
    public ResponseEntity<WorkTaskCalculateTimeDto> postCalculateHeure(CalculateTaskDurationInputDto workTaskToDateDto);
    public ResponseEntity<List<WorkTaskWithoutDto>> postLastCommentaire();
    public ResponseEntity<TaskWithUserNameListDto> getAllTasksByDateRange(Long numberOfDays);
}
