package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.DataStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {

    public static Path exportAllCsv(DataStore ds) throws IOException {
        Path dir = AppConfig.getInstance().getDataDir();
        List<String> students = new ArrayList<>();
        students.add("id,regNo,fullName,email,createdAt");
        ds.listStudents().forEach(s -> students.add(String.join(",",
                s.getId(), s.getRegNo(), escape(s.getFullName()), s.getEmail(), s.getCreatedAt().toString()
        )));
        Path studentsFile = dir.resolve("students.csv");
        Files.write(studentsFile, students, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        List<String> courses = new ArrayList<>();
        courses.add("code,title,credits,department,semester,instructorName");
        ds.listCourses().forEach(c -> courses.add(String.join(",",
                c.getCode().getCode(), escape(c.getTitle()), String.valueOf(c.getCredits()),
                c.getDepartment(), c.getSemester()==null?"":c.getSemester().name(),
                c.getInstructor()==null?"":escape(c.getInstructor().getFullName())
        )));
        Path coursesFile = dir.resolve("courses.csv");
        Files.write(coursesFile, courses, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        List<String> enrolls = new ArrayList<>();
        enrolls.add("studentId,courseCode,enrolledAt,marks");
        for (Map.Entry<String, List<Enrollment>> e : ds.allEnrollments().entrySet()) {
            for (Enrollment en : e.getValue()) {
                enrolls.add(String.join(",",
                        en.getStudentId(), en.getCourseCode().getCode(), en.getEnrolledAt().toString(),
                        en.getMarks().map(Object::toString).orElse("")
                ));
            }
        }
        Path enrollFile = dir.resolve("enrollments.csv");
        Files.write(enrollFile, enrolls, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return dir;
    }

    public static Path backupCsvs(Path folder) throws IOException {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backup = AppConfig.getInstance().getDataDir().resolve("backup_" + ts);
        Files.createDirectories(backup);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(folder, "*.csv")) {
            for (Path p : ds) Files.copy(p, backup.resolve(p.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
        return backup;
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", " ");
    }
}
