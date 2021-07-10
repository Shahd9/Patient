package com.msaproject.patient.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationResponseModel {

    @SerializedName("recommendations")
    @Expose
    private List<String> recommendations = null;

    public List<String> getData() {
        return recommendations;
    }
}
