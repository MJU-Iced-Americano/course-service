package com.mju.course.application;

import com.mju.course.domain.model.Course;
import com.mju.course.domain.repository.CourseRepository;
import com.mju.course.infrastructure.config.JasyptConfig;
import com.mju.course.presentation.dto.PostCourseDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class CourseServiceImplTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private JasyptConfig jasyptConfig;
    @Test
    void plus(){
        int result = 3;
        assertEquals(3, result);
    }

    @Test
    void 코스_DB_저장(){
        // given
        PostCourseDto postCourseDto = PostCourseDto.builder()
                .category("test")
                .courseName("test")
                .price("test")
                .courseDescription("test")
                .difficulty(3)
                .courseTime(100)
                .skill("test")
                .coursePeriod("test")
                .build();
        Course course = Course.of(postCourseDto);

        // when
        Course saveCourse = courseRepository.save(course);

        log.info(String.valueOf(course.getId()));
        log.info(String.valueOf(saveCourse.getId()));
        log.info(saveCourse.getCourseName());

        // then
        assertEquals(course.getId(), saveCourse.getId());
    }

}