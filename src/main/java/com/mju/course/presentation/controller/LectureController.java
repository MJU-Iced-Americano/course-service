package com.mju.course.presentation.controller;

import com.mju.course.application.LectureService;
import com.mju.course.domain.model.other.Result.CommonResult;
import com.mju.course.domain.service.ResponseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/lecture")
@Tag(name = "5. 강의", description = "강의 관련 api 입니다. ")
public class LectureController {

    private final LectureService lectureService;
    private final ResponseService responseService;

    // 강의 보기
    // [Read] (공통) 강의 조회 - tab : 기본(basic), 목차 (curriculum)
    @GetMapping("/{lecture_index}")
    public CommonResult readBasicLecture(@PathVariable Long lecture_index,
                                   @RequestParam("tab") String tab){
        return lectureService.readLecture(lecture_index, tab);
    }

    //////////////////////////////////////////////////////////////////////////
    // 강의 노트 - 유저로 구분하기 때문에 추후 개발
    // 강의 노트 작성
    @PostMapping("/{lecture_index}/my-note")
    public CommonResult createLectureNote(@PathVariable Long lecture_index,
                                          @RequestParam("tab") String note,
                                          @RequestBody String lectureNote){
        return lectureService.createLectureNote(lecture_index, note, lectureNote);
    }

    // 강의 노트 수정
    @PutMapping("/{lecture_index}/my-note")
    public CommonResult updateLectureNote(@PathVariable Long lecture_index,
                                          @RequestParam("tab") String note,
                                          @RequestBody String lectureNote){
        return lectureService.updateLectureNote(lecture_index, note, lectureNote);
    }

    // 강의 노트 삭제
    @DeleteMapping("/{lecture_index}/my-note")
    public CommonResult deleteLectureNote(@PathVariable Long lecture_index,
                                          @RequestParam("tab") String note){
        return lectureService.deleteLectureNote(lecture_index, note);
    }

    // 강의 질문








}
