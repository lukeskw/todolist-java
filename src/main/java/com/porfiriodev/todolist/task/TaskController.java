package com.porfiriodev.todolist.task;

import com.porfiriodev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.porfiriodev.todolist.exception.ResponseUtils.defaultResponse;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            System.out.println("A data de início/término deve ser maior que a data atual!");

            return defaultResponse(HttpStatus.BAD_REQUEST, "A data de início/término deve ser maior que a data atual!");

        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            System.out.println("A data de início deve ser menor que a data de término!");

            return defaultResponse(HttpStatus.BAD_REQUEST, "A data de início deve ser menor que a data de término!");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> listAll(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        return this.taskRepository.findByIdUser((UUID) idUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var idUser = request.getAttribute("idUser");

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return defaultResponse(HttpStatus.BAD_REQUEST, "Tarefa não encontrada!");
        }

        if(!task.getIdUser().equals(idUser)){
            return defaultResponse(HttpStatus.BAD_REQUEST, "Usuário não tem permissão para alterar essa tarefa!");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
}
