package com.restapi.project_AI_diary_backend.domain.diary.mapper;


import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryRequest;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryResponse;
import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiaryMapper {
    Diary diaryRequestToDiary(DiaryRequest diaryRequest);

    DiaryResponse diaryToDiaryResponse(Diary diary);
}
