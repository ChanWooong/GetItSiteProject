package com.getit.domain.admin.lecture.service;

import com.getit.domain.admin.lecture.dto.TaskCreateRequestDto;
import com.getit.domain.admin.lecture.dto.TaskResponseDto;
import com.getit.domain.admin.lecture.dto.TaskUpdateRequestDto;
import com.getit.domain.assignment.entity.Assignment;
import com.getit.domain.assignment.entity.Task;
import com.getit.domain.assignment.repository.AssignmentRepository;
import com.getit.domain.assignment.repository.TaskRepository;
import com.getit.domain.assignment.service.FileStorageService;
import com.getit.domain.lecture.entity.Lecture;
import com.getit.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTaskService {

    private final LectureRepository lectureRepository;
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignmentRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public TaskResponseDto getTask(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."));
        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 강의의 과제를 찾을 수 없습니다."));
        return TaskResponseDto.from(task);
    }

    @Transactional
    public TaskResponseDto createTask(Long lectureId, TaskCreateRequestDto request) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."));
        if (taskRepository.findByLecture(lecture).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 해당 강의에 과제가 존재합니다.");
        }
        Task task = Task.builder()
                .lecture(lecture)
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .build();
        try {
            task = taskRepository.save(task);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 해당 강의에 과제가 존재합니다.");
        }
        return TaskResponseDto.from(task);
    }

    @Transactional
    public TaskResponseDto updateTask(Long lectureId, TaskUpdateRequestDto request) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."));
        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 강의의 과제를 찾을 수 없습니다."));
        try {
            task.update(request.getTitle(), request.getDescription(), request.getDeadline());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return TaskResponseDto.from(task);
    }

    @Transactional
    public void deleteTask(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다."));
        Task task = taskRepository.findByLecture(lecture)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 강의의 과제를 찾을 수 없습니다."));

        List<Assignment> assignments = assignmentRepository.findByTaskId(task.getId());
        for (Assignment a : assignments) {
            fileStorageService.deleteDir(a.getDirName());
        }
        assignmentRepository.deleteAll(assignments);
        taskRepository.delete(task);
    }
}
