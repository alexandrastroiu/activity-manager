package com.example.activity_manager.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.activity_manager.dto.ActivityCreateDto;
import com.example.activity_manager.dto.TeacherDashboardDto;
import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.enums.AttendanceStatus;
import com.example.activity_manager.enums.Difficulty;
import com.example.activity_manager.enums.Priority;
import com.example.activity_manager.model.Activity;
import com.example.activity_manager.model.ActivitySubtask;
import com.example.activity_manager.model.Attendance;
import com.example.activity_manager.model.Course;
import com.example.activity_manager.model.CourseSession;
import com.example.activity_manager.model.Student;
import com.example.activity_manager.model.Teacher;
import com.example.activity_manager.service.ActivityService;
import com.example.activity_manager.service.ActivitySubtaskService;
import com.example.activity_manager.service.AttendanceService;
import com.example.activity_manager.service.CourseService;
import com.example.activity_manager.service.CourseSessionService;
import com.example.activity_manager.service.StudentService;
import com.example.activity_manager.service.TeacherService;
import com.example.activity_manager.service.UserService;

import jakarta.validation.Valid;

// Controller for the dashboard web interface. Handles requests related to courses, sessions, activities, and attendance management.
@Controller
@RequestMapping("/ui")
public class DashboardWebController {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final CourseSessionService sessionService;
    private final AttendanceService attendanceService;
    private final ActivityService activityService;
    private final ActivitySubtaskService subtaskService;
    private final StudentService studentService;
    private final UserService userService;

    public DashboardWebController(
            TeacherService teacherService,
            CourseService courseService,
            CourseSessionService sessionService,
            AttendanceService attendanceService,
            ActivityService activityService,
            ActivitySubtaskService subtaskService,
            StudentService studentService,
            UserService userService
    ) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.sessionService = sessionService;
        this.attendanceService = attendanceService;
        this.activityService = activityService;
        this.subtaskService = subtaskService;
        this.studentService = studentService;
        this.userService = userService;
    }

    public record CourseVM(Long id, String name, long sessionsCount) {

    }

    public record CourseOption(Long id, String name) {

    }

    public record SessionVM(
            Long id,
            LocalDate date,
            LocalTime time,
            LocalTime duration,
            int attendanceCount
            ) {

    }

    public record SubtaskVM(Long id, String title, boolean completed) {

    }

    public record ActivityVM(
            Long id,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            ActivityStatus status,
            Priority priority,
            Difficulty difficulty,
            int progress,
            boolean overdue,
            List<SubtaskVM> subtasks
            ) {

    }

    public record AttendanceRow(
            Long sessionId,
            Long studentId,
            String studentName,
            String groupName,
            AttendanceStatus status
            ) {

    }

    public static class SessionForm {

        public LocalDate sessionDate;
        public LocalTime sessionTime;
        public LocalTime duration;
    }

    private Long resolveUserId(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userService.findByUsername(principal.getName()).getUserId();
    }

    private void addEnums(Model model) {
        model.addAttribute("statuses", ActivityStatus.values());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("attendanceStatuses", AttendanceStatus.values());
    }

    private String redirectDashboard(
            Long courseId,
            Long sessionId,
            ActivityStatus status,
            Priority priority,
            Difficulty difficulty,
            String sort,
            Long editActivityId,
            Long editSessionId
    ) {
        UriComponentsBuilder url = UriComponentsBuilder.fromPath("/ui/dashboard");

        if (courseId != null) {
            url.queryParam("courseId", courseId);
        }
        if (sessionId != null) {
            url.queryParam("sessionId", sessionId);
        }
        if (status != null) {
            url.queryParam("status", status);
        }
        if (priority != null) {
            url.queryParam("priority", priority);
        }
        if (difficulty != null) {
            url.queryParam("difficulty", difficulty);
        }
        if (sort != null && !sort.isBlank()) {
            url.queryParam("sort", sort);
        }
        if (editActivityId != null) {
            url.queryParam("editActivityId", editActivityId);
        }
        if (editSessionId != null) {
            url.queryParam("editSessionId", editSessionId);
        }

        return "redirect:" + url.build().encode().toUriString();
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Principal principal,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long editActivityId,
            @RequestParam(required = false) Long editSessionId,
            Model model
    ) {
        Long resolvedUserId = resolveUserId(principal);
        Teacher teacher = teacherService.getByUserId(resolvedUserId);
        Long teacherId = teacher.getTeacherId();

        model.addAttribute("userId", resolvedUserId);
        model.addAttribute("teacherName", teacher.getFirstName() + " " + teacher.getLastName());

        long total = activityService.countTotalActivities(teacherId);
        long completed = activityService.countCompletedActivities(teacherId);
        double avgProgress = activityService.getAverageProgress(teacherId);
        model.addAttribute("dashboard", new TeacherDashboardDto(total, completed, avgProgress));

        List<Course> courses = courseService.getCoursesByTeacher(teacherId);

        List<CourseVM> courseTable = courses.stream()
                .map(c -> new CourseVM(
                c.getCourseId(),
                c.getCourseName(),
                courseService.countSessionsForCourse(c.getCourseId())
        ))
                .toList();
        model.addAttribute("courseTable", courseTable);

        List<CourseOption> courseOptions = courses.stream()
                .map(c -> new CourseOption(c.getCourseId(), c.getCourseName()))
                .toList();
        model.addAttribute("courses", courseOptions);

        Set<Long> teacherCourseIds = courses.stream().map(Course::getCourseId).collect(Collectors.toSet());
        Long selectedCourseId = (courseId != null && teacherCourseIds.contains(courseId))
                ? courseId
                : (courses.isEmpty() ? null : courses.get(0).getCourseId());
        model.addAttribute("selectedCourseId", selectedCourseId);

        List<CourseSession> sessions = (selectedCourseId == null) ? List.of() : sessionService.getSessionsByCourse(selectedCourseId);

        Set<Long> courseSessionIds = sessions.stream().map(CourseSession::getSessionId).collect(Collectors.toSet());
        Long selectedSessionId = (sessionId != null && courseSessionIds.contains(sessionId))
                ? sessionId
                : (sessions.isEmpty() ? null : sessions.get(0).getSessionId());
        model.addAttribute("selectedSessionId", selectedSessionId);

        List<SessionVM> sessionTable = sessions.stream()
                .map(s -> new SessionVM(
                s.getSessionId(),
                s.getSessionDate(),
                s.getSessionTime(),
                s.getDuration(),
                attendanceService.getAttendanceForSession(s.getSessionId()).size()
        ))
                .sorted(Comparator.comparing(SessionVM::date).thenComparing(SessionVM::time))
                .toList();
        model.addAttribute("sessionTable", sessionTable);

        model.addAttribute("sessionCreateForm", new SessionForm());

        if (editSessionId != null && courseSessionIds.contains(editSessionId)) {
            CourseSession s = sessions.stream().filter(x -> x.getSessionId().equals(editSessionId)).findFirst().orElse(null);
            if (s != null) {
                SessionForm f = new SessionForm();
                f.sessionDate = s.getSessionDate();
                f.sessionTime = s.getSessionTime();
                f.duration = s.getDuration();
                model.addAttribute("editSessionId", editSessionId);
                model.addAttribute("sessionEditForm", f);
            }
        }

        List<AttendanceRow> attendanceRows = List.of();
        if (selectedCourseId != null && selectedSessionId != null) {
            List<Attendance> existing = attendanceService.getAttendanceForSession(selectedSessionId);
            Map<Long, Attendance> byStudentId = existing.stream()
                    .collect(Collectors.toMap(a -> a.getStudent().getStudentId(), a -> a, (a, b) -> a));

            List<Student> students = studentService.getStudentsByCourse(selectedCourseId);

            attendanceRows = students.stream()
                    .map(st -> {
                        Attendance found = byStudentId.get(st.getStudentId());
                        AttendanceStatus stStatus = (found != null) ? found.getAttendanceStatus() : AttendanceStatus.PRESENT;
                        String groupName = (st.getStudentGroup() != null) ? st.getStudentGroup().getGroupName() : "";
                        return new AttendanceRow(
                                selectedSessionId,
                                st.getStudentId(),
                                st.getFirstName() + " " + st.getLastName(),
                                groupName,
                                stStatus
                        );
                    })
                    .toList();
        }
        model.addAttribute("attendanceRows", attendanceRows);

        List<Activity> base = activityService.getActivitiesByTeacher(teacherId);

        if (status != null) {
            base = base.stream().filter(a -> a.getStatus() == status).toList();
        }
        if (priority != null) {
            base = base.stream().filter(a -> a.getPriority() == priority).toList();
        }
        if (difficulty != null) {
            base = base.stream().filter(a -> a.getDifficulty() == difficulty).toList();
        }

        if ("deadline".equalsIgnoreCase(sort)) {
            base = base.stream()
                    .sorted(Comparator.comparing(Activity::getEndDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();
        }

        List<ActivityVM> activityVMs = base.stream()
                .map(a -> {
                    int prog = activityService.calculateProgress(a.getActivityId());
                    boolean overdue = a.getEndDate() != null
                            && a.getEndDate().isBefore(LocalDate.now())
                            && a.getStatus() != ActivityStatus.COMPLETED;

                    List<ActivitySubtask> subtasks = subtaskService.getSubtasksByActivity(a.getActivityId());
                    List<SubtaskVM> subVMs = subtasks.stream()
                            .map(st -> new SubtaskVM(st.getSubtaskId(), st.getTitle(), Boolean.TRUE.equals(st.getIsCompleted())))
                            .toList();

                    return new ActivityVM(
                            a.getActivityId(),
                            a.getTitle(),
                            a.getStartDate(),
                            a.getEndDate(),
                            a.getStatus(),
                            a.getPriority(),
                            a.getDifficulty(),
                            prog,
                            overdue,
                            subVMs
                    );
                })
                .toList();

        model.addAttribute("activities", activityVMs);

        model.addAttribute("progressLabels", activityVMs.stream().map(ActivityVM::title).toList());
        model.addAttribute("progressValues", activityVMs.stream().map(ActivityVM::progress).toList());

        model.addAttribute("activityCreateForm", new ActivityCreateDto());

        if (editActivityId != null) {
            try {
                Activity a = activityService.getById(editActivityId);

                ActivityCreateDto f = new ActivityCreateDto();
                f.setTitle(a.getTitle());
                f.setDescription(a.getDescription());
                f.setStartDate(a.getStartDate());
                f.setEndDate(a.getEndDate());
                f.setStatus(a.getStatus());
                f.setPriority(a.getPriority());
                f.setDifficulty(a.getDifficulty());

                model.addAttribute("editActivityId", editActivityId);
                model.addAttribute("activityEditForm", f);
            } catch (ResponseStatusException ignored) {

            }
        }

        model.addAttribute("filterStatus", status);
        model.addAttribute("filterPriority", priority);
        model.addAttribute("filterDifficulty", difficulty);
        model.addAttribute("sort", sort);

        addEnums(model);
        return "dashboard";
    }

    @PostMapping("/sessions/create")
    public String createSessionInline(
            Principal principal,
            @RequestParam Long courseId,
            @RequestParam LocalDate sessionDate,
            @RequestParam LocalTime sessionTime,
            @RequestParam LocalTime duration,
            RedirectAttributes ra
    ) {

        try {
            Course course = courseService.getById(courseId);

            CourseSession s = new CourseSession();
            s.setCourse(course);
            s.setSessionDate(sessionDate);
            s.setSessionTime(sessionTime);
            s.setDuration(duration);

            CourseSession saved = sessionService.createSession(s);

            ra.addFlashAttribute("flashOk", "Session created.");
            return redirectDashboard(courseId, saved.getSessionId(), null, null, null, null, null, null);
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not create session. Check date/time values.");
            return redirectDashboard(courseId, null, null, null, null, null, null, null);
        }
    }

    @PostMapping("/sessions/{sessionId}/update")
    public String updateSessionInline(
            Principal principal,
            @RequestParam Long courseId,
            @PathVariable Long sessionId,
            @RequestParam LocalDate sessionDate,
            @RequestParam LocalTime sessionTime,
            @RequestParam LocalTime duration,
            RedirectAttributes ra
    ) {
        try {
            CourseSession updated = new CourseSession();
            updated.setSessionDate(sessionDate);
            updated.setSessionTime(sessionTime);
            updated.setDuration(duration);

            sessionService.updateSession(sessionId, updated);

            ra.addFlashAttribute("flashOk", "Session updated.");
            return redirectDashboard(courseId, sessionId, null, null, null, null, null, null);
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not update session.");
            return redirectDashboard(courseId, sessionId, null, null, null, null, null, sessionId);
        }
    }

    @PostMapping("/sessions/{sessionId}/delete")
    public String deleteSessionInline(
            Principal principal,
            @RequestParam Long courseId,
            @PathVariable Long sessionId,
            RedirectAttributes ra
    ) {

        try {
            sessionService.deleteSession(sessionId);
            ra.addFlashAttribute("flashOk", "Session deleted.");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("flashError",
                    "Cannot delete this session because attendance exists for it. ");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not delete session.");
        }
        return redirectDashboard(courseId, null, null, null, null, null, null, null);
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(
            Principal principal,
            @RequestParam Long courseId,
            @RequestParam Long sessionId,
            @RequestParam Long studentId,
            @RequestParam AttendanceStatus status,
            RedirectAttributes ra
    ) {
        try {
            attendanceService.markAttendance(sessionId, studentId, status);
            ra.addFlashAttribute("flashOk", "Attendance saved.");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not save attendance.");
        }
        return redirectDashboard(courseId, sessionId, null, null, null, null, null, null);
    }

    @PostMapping("/activities/create")
    public String createActivityInline(
            java.security.Principal principal,
            @Valid @ModelAttribute("activityCreateForm") ActivityCreateDto dto,
            BindingResult br,
            RedirectAttributes ra
    ) {
        Long resolvedUserId = resolveUserId(principal);

        if (br.hasErrors()) {
            ra.addFlashAttribute("flashError", "Activity create failed. Please fill required fields.");
            return redirectDashboard(null, null, null, null, null, null, null, null);
        }

        try {
            Teacher teacher = teacherService.getByUserId(resolvedUserId);

            Activity a = new Activity();
            a.setTeacher(teacher);
            a.setTitle(dto.getTitle());
            a.setDescription(dto.getDescription());
            a.setStartDate(dto.getStartDate());
            a.setEndDate(dto.getEndDate());
            a.setStatus(dto.getStatus());
            a.setPriority(dto.getPriority());
            a.setDifficulty(dto.getDifficulty());

            activityService.create(a);
            ra.addFlashAttribute("flashOk", "Activity created.");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not create activity.");
        }

        return redirectDashboard(null, null, null, null, null, null, null, null);
    }

    @PostMapping("/activities/{activityId}/update")
    public String updateActivityInline(
            Principal principal,
            @PathVariable Long activityId,
            @Valid @ModelAttribute("activityEditForm") ActivityCreateDto dto,
            BindingResult br,
            RedirectAttributes ra
    ) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("flashError", "Activity update failed. Please correct fields.");
            return redirectDashboard(null, null, null, null, null, null, activityId, null);
        }
        try {
            activityService.update(activityId, dto);
            ra.addFlashAttribute("flashOk", "Activity updated.");
            return redirectDashboard(null, null, null, null, null, null, null, null);
        } catch (ResponseStatusException ex) {
            ra.addFlashAttribute("flashError", ex.getReason() != null ? ex.getReason() : "Could not update activity.");
            return redirectDashboard(null, null, null, null, null, null, activityId, null);
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not update activity.");
            return redirectDashboard(null, null, null, null, null, null, activityId, null);
        }
    }

    @PostMapping("/activities/{activityId}/delete")
    public String deleteActivity(Principal principal, @PathVariable Long activityId, RedirectAttributes ra) {
        try {
            activityService.delete(activityId);
            ra.addFlashAttribute("flashOk", "Activity deleted.");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not delete activity.");
        }
        return redirectDashboard(null, null, null, null, null, null, null, null);
    }

    @PostMapping("/activities/{activityId}/subtasks")
    public String addSubtask(
            Principal principal,
            @PathVariable Long activityId,
            @RequestParam String title,
            RedirectAttributes ra
    ) {
        if (title == null || title.trim().isEmpty()) {
            ra.addFlashAttribute("flashError", "Subtask title cannot be empty.");
            return redirectDashboard(null, null, null, null, null, null, activityId, null);
        }

        try {
            Activity activity = activityService.getById(activityId);

            ActivitySubtask st = new ActivitySubtask();
            st.setActivity(activity);
            st.setTitle(title.trim());
            st.setIsCompleted(false);

            subtaskService.create(st);
            ra.addFlashAttribute("flashOk", "Subtask added.");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not add subtask.");
        }

        return redirectDashboard(null, null, null, null, null, null, activityId, null);
    }

    @PostMapping("/subtasks/{subtaskId}/toggle")
    public String toggleSubtask(
            Principal principal,
            @RequestParam Long activityId,
            @PathVariable Long subtaskId,
            @RequestParam(required = false) String checked,
            RedirectAttributes ra
    ) {
        boolean value = (checked != null);
        try {
            subtaskService.setCompleted(subtaskId, value);
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not update subtask status.");
        }
        return redirectDashboard(null, null, null, null, null, null, activityId, null);
    }

    @PostMapping("/subtasks/{subtaskId}/update")
    public String updateSubtask(
            Principal principal,
            @RequestParam Long activityId,
            @PathVariable Long subtaskId,
            @RequestParam String title,
            RedirectAttributes ra
    ) {
        try {
            subtaskService.updateTitle(subtaskId, title);
            ra.addFlashAttribute("flashOk", "Subtask updated.");
        } catch (ResponseStatusException ex) {
            ra.addFlashAttribute("flashError", ex.getReason());
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not update subtask.");
        }
        return redirectDashboard(null, null, null, null, null, null, activityId, null);
    }

    @PostMapping("/subtasks/{subtaskId}/delete")
    public String deleteSubtask(
            Principal principal,
            @RequestParam Long activityId,
            @PathVariable Long subtaskId,
            RedirectAttributes ra
    ) {
        try {
            subtaskService.delete(subtaskId);
            ra.addFlashAttribute("flashOk", "Subtask deleted.");
        } catch (Exception ex) {
            ra.addFlashAttribute("flashError", "Could not delete subtask.");
        }
        return redirectDashboard(null, null, null, null, null, null, activityId, null);
    }
}
