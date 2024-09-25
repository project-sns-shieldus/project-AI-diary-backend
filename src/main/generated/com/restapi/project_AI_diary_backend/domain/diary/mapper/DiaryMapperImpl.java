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
        if ( diaryRequest == null ) {
            return null;
        }

        Diary diary = new Diary();

        diary.setContent( diaryRequest.getContent() );
        diary.setTitle( diaryRequest.getTitle() );
        diary.setCreatedDate( diaryRequest.getCreatedDate() );

        return diary;
    }

    @Override
    public DiaryResponse diaryToDiaryResponse(Diary diary) {
        if ( diary == null ) {
            return null;
        }

        DiaryResponse diaryResponse = new DiaryResponse();

        diaryResponse.setDiaryId( diary.getDiaryId() );
        diaryResponse.setTitle( diary.getTitle() );
        diaryResponse.setContent( diary.getContent() );
        diaryResponse.setCreatedDate( diary.getCreatedDate() );

        return diaryResponse;
    }
}
