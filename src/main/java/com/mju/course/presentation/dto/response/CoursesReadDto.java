package com.mju.course.presentation.dto.response;

import com.mju.course.domain.model.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CoursesReadDto {

    @Schema(description = "코스 인덱스", defaultValue = "1")
    private Long courseIndex;
    @Schema(description = "코스 이름", defaultValue = "자바 기초")
    private String courseName;
    @Schema(description = "가격", defaultValue = "100000")
    private String price;
    @Schema(description = "난이도", defaultValue = "2")
    private int difficulty;
    @Schema(description = "코스 기본 사진")
    private String courseTitlePhotoUrl;

    public static CoursesReadDto of(Course course){
        return CoursesReadDto.builder()
                .courseIndex(course.getId())
                .courseName(course.getCourseName())
                .price(course.getPrice())
                .difficulty(course.getDifficulty())
                .courseTitlePhotoUrl("https://d19wla4ff811v8.cloudfront.net/" + course.getCourseTitlePhotoUrl())
                .build();
    }
}
