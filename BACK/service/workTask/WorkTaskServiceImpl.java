package com.itso.occupancy.occupancy.service.workTask;

import com.itso.occupancy.occupancy.config.errorhandler.customexception.ElementNotFoundException;
import com.itso.occupancy.occupancy.dto.mapper.*;
import com.itso.occupancy.occupancy.dto.model.project.ProjectIdDto;
import com.itso.occupancy.occupancy.dto.model.workTask.*;
import com.itso.occupancy.occupancy.model.*;
import com.itso.occupancy.occupancy.repository.*;
import com.itso.occupancy.occupancy.service.user.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class WorkTaskServiceImpl implements WorkTaskService {
    private final WorkTaskRepository workTaskRepository;
    private final ProjectRepository projectRepository;
    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<List<WorkTaskWithoutDto>> getAllWorkTasks(){
        List<WorkTaskWithoutDto> allWorkTasksDto = workTaskRepository.SupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById()
                .stream()
                .map(WorkTaskMapper::toWorkTaskWithoutDto)//toworkTaskDto
                .toList();
        return new ResponseEntity<>(allWorkTasksDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskDto> getWorkTask(Long id){
        WorkTaskDto workTaskDto = WorkTaskMapper.toworkTaskDto(findWorkTaskIfExists(id));
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByWorkTime(int id){
        List<WorkTaskDto> allWorkTaskByWorkTime = workTaskRepository.findAllByWorkTimeAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrWorkTimeAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(id, id).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(allWorkTaskByWorkTime, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskWithoutDto>> getAllWorkTaskByIdUser(Long id){
        List<WorkTaskWithoutDto> allWorkTaskByWorkTime = workTaskRepository.findAllByUserIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrUserIdAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderByWorkDay(id,id).stream().map(WorkTaskMapper::toWorkTaskWithoutDto).toList();
        return new ResponseEntity<>(allWorkTaskByWorkTime, HttpStatus.OK);
    }

    
    //List ok tasks by user and by date range
    @Override
    public ResponseEntity<UserTaskListDto> getAllWorkTasksByDays(DaysTaskInputDto daysTaskInputDto){

        User currentUser = userService.getUserConnected();
        // Get start date (now - numberOfDays) and end date (now)
        Date nowDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(LocalDateTime.now().minusDays(daysTaskInputDto.getNumberOfDays()).atZone(ZoneId.systemDefault()).toInstant());
        
        // Get tasks by user and by dates
        UserTaskListDto userTaskListDto = new UserTaskListDto();
        List<WorkTask> tasks = workTaskRepository.findTasksByUserAndByDate(currentUser.getId(), startDate, nowDate);

        for(WorkTask task : tasks) {
                TaskListDto taskListDto = new TaskListDto();
                taskListDto.setDate(task.getWorkDay())
                .setDuration(task.getWorkTime())
                .setFeatureName(task.getFeature().getName())
                .setTag(task.getTag().getName())
                .setProjectName(task.getProject().getName());
               userTaskListDto.getTasks().add(taskListDto); 
           }

        return new ResponseEntity<>(userTaskListDto, HttpStatus.OK);   
    }

    @Override
    public ResponseEntity<TaskWithUserNameListDto> getAllTasksByDateRange(Long numberOfDays){       
        // Get start date (now - numberOfDays) and end date (now)
        Date nowDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(LocalDateTime.now().minusDays(numberOfDays).atZone(ZoneId.systemDefault()).toInstant());    
        //Get all tasks by date range
        TaskWithUserNameListDto taskWithUserNameListDto = new TaskWithUserNameListDto();
        List<WorkTask> tasks = workTaskRepository.findTasksByDateRange(startDate, nowDate);        
        
        for(WorkTask task : tasks) { 
                TaskWithUserNameDto taskWithUserNameDto = new TaskWithUserNameDto();
                    taskWithUserNameDto.setDate(task.getWorkDay())
                    .setUserName(task.getUser().getUsername())
                    .setTag(task.getTag().getName())
                    .setFeatureName(task.getFeature().getName())
                    .setDuration(task.getWorkTime())
                    .setProjectName(task.getProject().getName());           
                    taskWithUserNameListDto.getTasks().add(taskWithUserNameDto);                    
            }
            return new ResponseEntity<>(taskWithUserNameListDto, HttpStatus.OK);
    }

    // Admin Dashboard WeekList
    @Override
    public ResponseEntity<WeekListDto> getAllWorkTaskByWeek(WeekTaskInputDto weekTaskInputDto){
        //Set a start date (today minus weekTaskInputDto) and an end date (today)
        Date nowDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(LocalDateTime.now().minusWeeks(weekTaskInputDto.getWeekCount()).atZone(ZoneId.systemDefault()).toInstant());

        //Give the list of week numbers in the date range
        List<Long> weekNumbers = new ArrayList<>();        
        for(int i = 0; i < weekTaskInputDto.getWeekCount(); i++ ){
            int weekNumber = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().plusWeeks(i).get(ChronoField.ALIGNED_WEEK_OF_YEAR);

            weekNumbers.add(Long.valueOf(weekNumber));
        }

        // Reverse weekNumbers list
        Collections.sort(weekNumbers, (d1,d2) ->{
            return (int)(d2 - d1);
        });

        WeekListDto weekListDto = new WeekListDto();
        //Get a list of all users
        List<User> userList =  userRepository.findAll();
        for (Long weekNumber : weekNumbers) {
            WeekDto weekDto = new WeekDto();
            weekDto.setWeekNumber(weekNumber);

            for(User user : userList)
            {
                UserWorkTimeDto userWorkTimeDto = new UserWorkTimeDto();
                //Get all tasks
                List<WorkTask> userTasks = user.getWorkTasks();
                //Get a list of tasks by user and by week number
                List<WorkTask> filteredTasks = userTasks.stream()
                        .filter(task -> task.getWorkDay().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .get(ChronoField.ALIGNED_WEEK_OF_YEAR) == weekNumber)
                        .collect(Collectors.toList());
                //calculates the sum of work
                double totalWorkTime = filteredTasks.stream().mapToDouble(value -> value.getWorkTime()).sum();
                //Dto construction
                userWorkTimeDto.setUserName(user.getUsername());
                userWorkTimeDto.setWorktime((int)totalWorkTime);
                weekDto.getUsers().add(userWorkTimeDto);
            }
            weekListDto.getWeeks().add(weekDto);
        }
        return new ResponseEntity<>(weekListDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByProject(Long id){
        List<WorkTaskDto> AllWorkTaskByProject = workTaskRepository.findAllByProjectIdAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrProjectIdAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(id,id).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(AllWorkTaskByProject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByTag(Long id){
        List<WorkTaskDto> AllWorkTaskByTag = workTaskRepository.findAllByTagIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrTagIdAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(id,id).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(AllWorkTaskByTag, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTasks(){
        List<WorkTaskDto> allWorkTasksDto = workTaskRepository.SupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById()
                .stream()
                .map(WorkTaskMapper::toworkTaskDto)
                .toList();
        return new ResponseEntity<>(allWorkTasksDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskDto> postWorkTask(WorkTaskIdDto workTaskIdDto){
        WorkTaskDto workTaskDto = WorkTaskMapper.toworkTaskDto(findWorkTaskIfExists(workTaskIdDto.getId()));
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByWorkTime(WorkTaskWorkTimeDto workTaskWorkTimeDto){
        List<WorkTaskDto> allWorkTaskByWorkTime = workTaskRepository.findAllByWorkTimeAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrWorkTimeAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(workTaskWorkTimeDto.getWork_time(),workTaskWorkTimeDto.getWork_time()).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(allWorkTaskByWorkTime, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByIdUser(WorkTaskIdUserDto workTaskIdUserDto){
        List<WorkTaskDto> allWorkTaskByWorkTime = workTaskRepository.findAllByUserIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrUserIdAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderByWorkDay(workTaskIdUserDto.getId_user(),workTaskIdUserDto.getId_user()).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(allWorkTaskByWorkTime, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByProject(WorkTaskIdProjectDto workTaskIdProjectDto){
        //Calcules toutes les heures des task
        List<WorkTask> workTasks = workTaskRepository.findAllByWorkDayAndUserIdIsAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrWorkDayAndUserIdIsAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalse(workTaskIdProjectDto.getWork_day(),workTaskIdProjectDto.getId_user(),workTaskIdProjectDto.getWork_day(),workTaskIdProjectDto.getId_user());
        //Liste des task par project
        List<WorkTask> workTasksList = workTaskRepository.findAllByProjectIdAndUserIdAndWorkDayAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrProjectIdAndUserIdAndWorkDayAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderByWorkDay(workTaskIdProjectDto.getId_project(),workTaskIdProjectDto.getId_user(),workTaskIdProjectDto.getWork_day(),workTaskIdProjectDto.getId_project(),workTaskIdProjectDto.getId_user(),workTaskIdProjectDto.getWork_day());
                /*.stream().map(WorkTaskMapper::toworkTaskDto)
                .toList();*/
        double Duree = 0;
        List<WorkTaskDto> workTaskDto = new ArrayList<>();
        for (WorkTask workTask : workTasks) {
            Duree = Duree + workTask.getWorkTime();
        }
        for (WorkTask workTask : workTasksList) {
            workTaskDto.add(WorkTaskMapper.toworkTaskDto(workTask,Duree));
        }
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByTag(WorkTaskIdTagDto workTaskIdTagDto){
        List<WorkTaskDto> AllWorkTaskByTag = workTaskRepository.findAllByTagIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrTagIdAndSupprimerIsFalseAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(workTaskIdTagDto.getId_tag(),workTaskIdTagDto.getId_tag()).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(AllWorkTaskByTag, HttpStatus.OK);
    }
    
    @Override
    public  ResponseEntity<WorkTaskDto> createNewTask(WorkTaskCreateDto createWorkTask){
        // je verif tous les id des clées étrangers
        Project project = findProjectIfExists(createWorkTask.getId_project());
        Tag tag = findTagIfExists(createWorkTask.getId_tag());
        User user = findUserIfExists(createWorkTask.getId_user());
        Feature feature = findFeatureIfExists(createWorkTask.getId_feature());
        //je transforme tout en Worktask
        WorkTask newWorkTask = WorkTaskMapper.toWorkTaskModel(createWorkTask,project,tag,user,feature);
        //Je save en méthode et non en DTO
        WorkTask WorkTask = workTaskRepository.save(newWorkTask);
        //Je le crée en DTO pour l'envoyer au front
        WorkTaskDto workTaskDto = WorkTaskMapper.toworkTaskDto(WorkTask);
        return new ResponseEntity<>(workTaskDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<WorkTaskDto> deleteWorkTask(Long id){
        WorkTask task = findWorkTaskIfExists(id);
        task.setSupprimer(Boolean.TRUE);
        workTaskRepository.save(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<WorkTaskDto> updateWorkTask(Long id, WorkTaskUptadeDto workTaskUptadeDto){
        Project project = findProjectIfExists(workTaskUptadeDto.getId_project());
        Tag tag = findTagIfExists(workTaskUptadeDto.getId_tag());
        User user = findUserIfExists(workTaskUptadeDto.getId_user());
        Feature feature = findFeatureIfExists(workTaskUptadeDto.getId_feature());

        WorkTask workTask = findWorkTaskIfExists(id);
        workTask
                .setWorkTime(workTaskUptadeDto.getWork_time())
                .setWorkDay(workTaskUptadeDto.getWork_day())
                .setProject(project)
                .setTag(tag)
                .setUser(user)
                .setFeature(feature);
        WorkTask WorkTask = workTaskRepository.save(workTask);
        WorkTaskDto workTaskDto = WorkTaskMapper.toworkTaskDto(WorkTask);
        return new ResponseEntity<>(workTaskDto , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> postAllWorkTaskByIdFeature(WorkTaskIdFeatureDto workTaskIdFeatureDto){
        List<WorkTaskDto> allWorkTaskByBIdFeature = workTaskRepository.findByFeatureIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrFeatureIdAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(workTaskIdFeatureDto.getId_feature(),workTaskIdFeatureDto.getId_feature()).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(allWorkTaskByBIdFeature, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskByIdFeature(Long id){
        List<WorkTaskDto> allWorkTaskByBIdFeature = workTaskRepository.findByFeatureIdAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrFeatureIdAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(id,id).stream().map(WorkTaskMapper::toworkTaskDto).toList();
        return new ResponseEntity<>(allWorkTaskByBIdFeature, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskWithoutDto> postAddCommentaire(WorkTaskCommentaireDto workTaskCommentaireDto){
        WorkTask workTask = findWorkTaskIfExists(workTaskCommentaireDto.getId());
        workTask.setCommentaire(workTaskCommentaireDto.getCommentaire());
        WorkTask WorkTask = workTaskRepository.save(workTask);
        WorkTaskWithoutDto workTaskDto = WorkTaskMapper.toWorkTaskWithoutDto(WorkTask);
        return new ResponseEntity<>(workTaskDto , HttpStatus.OK);
    }

    @Override
    public  ResponseEntity<WorkTaskCreateWithoutDto> createNewTaskWithout(WorkTaskCreateWithoutDto createWorkTask){
        // je verif tous les id des clées étrangers
        Tag tag = findTagIfExists(createWorkTask.getId_tag());
        User user = findUserIfExists(createWorkTask.getId_user());
        Boolean vide = true;
        //je transforme tout en Worktask
        WorkTask newWorkTask = WorkTaskMapper.toWorkTaskModelWithout(createWorkTask,tag,user,vide);
        //Je save en méthode et non en DTO
        WorkTask WorkTask = workTaskRepository.save(newWorkTask);
        /*WorkTask.setVide(Boolean.TRUE);
        workTaskRepository.save(WorkTask);*/
        return new ResponseEntity<>(createWorkTask, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<WorkTaskWithoutDto> getWorkTaskWithout(Long id){
        WorkTaskWithoutDto workTaskDto = WorkTaskMapper.toWorkTaskWithoutDto(findWorkTaskIfExists(id));
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskVideDto> getWorkTaskVide(Long id){
        WorkTaskVideDto workTaskDto = WorkTaskMapper.toWorkTaskVideDto(findWorkTaskIfExists(id));
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskWithoutDto> putChangeWorkTime(Long id,WorkTaskWorkTimeDto workTaskWorkTimeDto){
        WorkTask workTask = findWorkTaskIfExists(id);
        workTask.setWorkTime(workTaskWorkTimeDto.getWork_time());
        WorkTask WorkTask = workTaskRepository.save(workTask);
        WorkTaskWithoutDto workTaskDto = WorkTaskMapper.toWorkTaskWithoutDto(WorkTask);
        return new ResponseEntity<>(workTaskDto , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskDto>> getAllWorkTaskVideFalseByUser(Long id){
            List<WorkTaskDto> allWorkTasks = workTaskRepository.VideIsFalseAndUserIdIsAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalse(id)
                .stream()
                .map(WorkTaskMapper::toworkTaskDto)
                .toList();
        return new ResponseEntity<>(allWorkTasks, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WorkTaskCalculateTimeDto> postCalculateHeure(CalculateTaskDurationInputDto workTaskToDateDto){
        
        //List des tâches selon la date
        List<WorkTask> workTasks = new ArrayList();
        
        for(Date workDay : workTaskToDateDto.getWork_day())
        {        
            workTaskRepository.findAllByWorkDayAndUserIdIsAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrWorkDayAndUserIdIsAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalse(workDay,workTaskToDateDto.getId_user(),workDay,workTaskToDateDto.getId_user());
        }
        //Initialisation des variables
        double Duree = 0;

        //Calcule de la durée
        for (WorkTask workTask : workTasks) {
            Duree = Duree + workTask.getWorkTime();
        }

        //Création de l'objet
        WorkTaskCalculateTimeDto workTaskDto = WorkTaskMapper.toWorkTaskCalculateTimeDto(workTaskToDateDto,Duree);
        return new ResponseEntity<>(workTaskDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<WorkTaskWithoutDto>> postLastCommentaire(){
        //Liste des taches
        List<WorkTask> allWorkTasksDto = workTaskRepository.CommentaireIsNotNullAndSupprimerIsFalseAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrCommentaireIsNotNullAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderByIdDesc();

        //Initialisation des variables
        List<WorkTaskWithoutDto> workTaskWithoutDtos = new ArrayList<>();

        //Ajouts des tâches qui on des com
        for (WorkTask workTask : allWorkTasksDto) {
            workTaskWithoutDtos.add(WorkTaskMapper.toWorkTaskWithoutDto(workTask));
        }
        return new ResponseEntity<>(workTaskWithoutDtos, HttpStatus.OK);
    }

    //Gestion des erreurs
    private WorkTask findWorkTaskIfExists(Long id) {
        return workTaskRepository.findByIdAndSupprimerIsFalseAndAndProjectSupprimerIsFalseAndFeatureSupprimerIsFalseAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrIdAndSupprimerIsFalseAndProjectSupprimerIsNullAndFeatureSupprimerIsNullAndTagSupprimerIsFalseAndUserIsDeletedIsFalseOrderById(id,id)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format("Unable to find WorkTask [id = %s]", id)
                ));
    }
    private Project findProjectIfExists(Long id) {

        return projectRepository.findByIdAndSupprimerIsFalseAndClientSupprimerIsFalse(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format("Unable to find Project [id = %s] or it is deleted", id)
                ));
    }
    private Feature findFeatureIfExists(Long id) {

        return featureRepository.findByIdAndProjectSupprimerIsFalseAndSupprimerIsFalse(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format("Unable to find Feature [id = %s] or it is deleted", id)
                ));
    }
    private User findUserIfExists(Long id) {

        return userRepository.findByIdAndIsDeletedIsFalseAndJobSupprimerIsFalseOrJobSupprimerIsNullAndIsDeletedIsFalseAndId(id, id)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format("Unable to find User [id = %s]", id)
                ));
    }
    private Tag findTagIfExists(Long id) {

        return tagRepository.findByIdAndSupprimerIsFalse(id)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format("Unable to find Tag [id = %s]", id)
                ));
    }

}
