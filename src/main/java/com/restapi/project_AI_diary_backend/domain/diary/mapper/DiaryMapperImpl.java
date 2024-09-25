package com.restapi.project_AI_diary_backend.domain.diary.mapper;

import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryRequest;
import com.restapi.project_AI_diary_backend.domain.diary.dto.DiaryResponse;
import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-09-25T10:23:43+0900",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class DiaryMapperImpl implements DiaryMapper {

    @Override
    public Diary diaryRequestToDiary(DiaryRequest diaryRequest) {
        if (diaryRequest == null) {
            return null;
        }

        Diary diary = new Diary();

        // DiaryRequest의 필드를 Diary로 매핑
        diary.setDiaryDate(diaryRequest.getDiaryDate());
        diary.setWeather(diaryRequest.getWeather());
        diary.setNotes(diaryRequest.getNotes());
        diary.setTitle(diaryRequest.getTitle());
        diary.setCreatedAt(diaryRequest.getCreatedAt().atStartOfDay()); // LocalDate -> LocalDateTime

        return diary;
    }

    @Override
    public DiaryResponse diaryToDiaryResponse(Diary diary) {
        if (diary == null) {
            return null;
        }

        DiaryResponse diaryResponse = new DiaryResponse();

        // Diary의 필드를 DiaryResponse로 매핑
        diaryResponse.setDiaryId(diary.getDiaryId());
        diaryResponse.setTitle(diary.getTitle());
        diaryResponse.setNotes(diary.getNotes());
        diaryResponse.setCreatedAt(diary.getCreatedAt().toLocalDate()); // LocalDateTime -> LocalDate

        return diaryResponse;
    }
}
