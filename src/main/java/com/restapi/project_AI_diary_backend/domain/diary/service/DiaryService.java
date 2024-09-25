package com.restapi.project_AI_diary_backend.domain.diary.service;


import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryRequest;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryResponse;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryUpdateRequest;
import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import com.restapi.project_AI_diary_backend.domain.diary.mapper.DiaryMapper;
import com.restapi.project_AI_diary_backend.domain.diary.repository.DiaryRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryService {
    @Autowired
    DiaryMapper diaryMapper;

    @Autowired
    DiaryRepsitory diaryRepository;

    public int addDiary(DiaryRequest diaryRequest) {
        Diary diary = diaryMapper.diaryRequestToDiary(diaryRequest);
        diaryRepository.save(diary);
        return diary.getDiaryId();
    }


    public int updateDiary(int diaryId, DiaryUpdateRequest diaryUpdateRequest) {
        // ID로 기존 일기 조회
        Diary existingDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("Diary not found with id: " + diaryId));

        // 기존 일기의 내용 업데이트
        existingDiary.setTitle(diaryUpdateRequest.getTitle());
        existingDiary.setContent(diaryUpdateRequest.getContent());
        existingDiary.setUpdatedDatetime(diaryUpdateRequest.getUpdatedDateTime());

        // 변경된 일기 저장
        diaryRepository.save(existingDiary);

        return existingDiary.getDiaryId();
    }

    //다이어리 작성일로 조회
    //달력에서 특정 날짜를 클릭했을 때 해당 날짜의 모든 일기 목록을 조회하는 기능을 구현하기 위해 LocalDateTime 대신 LocalDate를 사용
    //LocalDateTime는 특정 날짜와 시간에 작성된 일기 한 편만 조회하는 경우에 더 적합
    public List<DiaryResponse> selectDiaryListByDate(LocalDate createdDate) {
        List<Diary> diaries = diaryRepository.findByCreatedDate(createdDate);
        List<DiaryResponse> diaryResponseList = diaries.stream()
                .map(diaryMapper::diaryToDiaryResponse)
                .collect(Collectors.toList());
        return diaryResponseList;
    }

    //다이어리 아이디로 조회하여 일기 상세보기
    public DiaryResponse getDiaryDetailById(int diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();
        DiaryResponse diaryResponse = diaryMapper.diaryToDiaryResponse(diary);
        return diaryResponse;
    }

    //모든 다이어리 목록 조회
    public List<DiaryResponse> getAllDiary() {
        List<Diary> diaries = diaryRepository.findAll();
        List<DiaryResponse> diaryResponseList = diaries.stream()
                .map(diaryMapper::diaryToDiaryResponse)
                .collect(Collectors.toList());
        return diaryResponseList;
    }

    public void deleteDiary(int diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}



