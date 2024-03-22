package com.example.noaadatamanager.dtos.update;


import com.example.noaadatamanager.annotations.AllowEmptyValue;

@AllowEmptyValue
public class MeasurementUpdateCommentDto implements UpdateDto{

    private String measurementId;

    private String newComment;

    @Override
    public String getEntityId() {
        return measurementId;
    }

    @Override
    public String getUpdatedFieldValue() {
        return newComment;
    }

    public void setMeasurementId(String measurementId) {
        this.measurementId = measurementId;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }
}
