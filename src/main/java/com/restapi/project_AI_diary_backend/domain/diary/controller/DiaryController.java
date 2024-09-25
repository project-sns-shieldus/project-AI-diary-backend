package com.restapi.project_AI_diary_backend.domain.diary.controller;


import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryRequest;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryResponse;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryUpdateRequest;
import com.restapi.project_AI_diary_backend.domain.diary.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {
    @Autowired
    DiaryService diaryService;

    // 일기 작성
    @PostMapping("/add")
    public ResponseEntity<Long> addDiary(@RequestBody DiaryRequest diaryRequest) {
        long diaryId = diaryService.addDiary(diaryRequest);
        return ResponseEntity.ok(diaryId);
    }

    //일기 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateDiary(@PathVariable("id") long diaryId, @RequestBody DiaryUpdateRequest diaryUpdateRequest) {
        long updatedDiaryId = diaryService.updateDiary(diaryId, diaryUpdateRequest);
        return ResponseEntity.ok(updatedDiaryId);
    }

    //특정 날짜의 일기 목록 조회
    @GetMapping("/get/list/{date}")
    public ResponseEntity<List<DiaryResponse>> selectDiaryListByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
                List<DiaryResponse> diaryResponseList = diaryService.selectDiaryListByDate(date);
                return ResponseEntity.ok(diaryResponseList);
    }

    //일기 상세보기
    @GetMapping("/get/{id}")
    public ResponseEntity<DiaryResponse> getDiaryDetailById(@PathVariable("id") long diaryId) {
        DiaryResponse diaryResponse = diaryService.getDiaryDetailById(diaryId);
        return ResponseEntity.ok(diaryResponse);
    }

    //모든 일기 목록 조회
    @GetMapping("/get/all")
    public ResponseEntity<List<DiaryResponse>> getAllDiary() {
        List<DiaryResponse> diaryResponseList = diaryService.getAllDiary();
        return ResponseEntity.ok(diaryResponseList);
    }
    //일기 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable long id) {
        diaryService.deleteDiary(id);
        //return new ResponseEntity<>("Deleted", HttpStatus.OK); //ResponseEntity<String>로 선언해야힘
        return ResponseEntity.noContent().build(); //ResponseEntity<Void>로 선언해야힘
    }

}
