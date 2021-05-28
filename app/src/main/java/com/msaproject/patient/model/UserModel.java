package com.msaproject.patient.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.msaproject.patient.Constants;
import com.msaproject.patient.model.types.AccountType;
import com.msaproject.patient.model.types.Gender;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class UserModel implements Serializable {

    public static final String PHONE = "phone";
    public static final String ACCOUNT_TYPE = "accountType";

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    @Expose
    private String id;
    @SerializedName(PHONE)
    @PrimaryKey
    @NonNull
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profilePicLink")
    @Nullable
    @Expose
    private String profilePicLink;
    @SerializedName("birthDate")
    @Expose
    private Date birthDate;
    @SerializedName("gender")
    @Expose
    @Gender
    private Integer gender;
    @SerializedName("weight")
    @Nullable
    @Expose
    private Float weight;
    @SerializedName("height")
    @Nullable
    @Expose
    private Float height;
    @SerializedName("specializationId")
    @Nullable
    @Expose
    private String specializationId;
    @SerializedName("diseaseId")
    @Nullable
    @Expose
    private String diseaseId;
    @SerializedName(ACCOUNT_TYPE)
    @AccountType
    @Expose
    private Integer accountType;
    @ServerTimestamp
    @SerializedName(Constants.MAP_KEY_CREATED_AT)
    @Expose
    private Date createdAt;
    @ServerTimestamp
    @SerializedName(Constants.MAP_KEY_UPDATED_AT)
    @Expose
    private Date updatedAt;

    public UserModel() {
    }

    public UserModel(@NonNull String id, @NonNull String phone) {
        this.id = id;
        this.phone = phone;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(@Nullable String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Gender
    public Integer getGender() {
        return gender;
    }

    public void setGender(@Gender Integer gender) {
        this.gender = gender;
    }

    @Nullable
    public Float getWeight() {
        return weight;
    }

    public void setWeight(@Nullable Float weight) {
        this.weight = weight;
    }

    @Nullable
    public Float getHeight() {
        return height;
    }

    public void setHeight(@Nullable Float height) {
        this.height = height;
    }

    @Nullable
    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(@Nullable String specializationId) {
        this.specializationId = specializationId;
    }

    @Nullable
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@Nullable String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @AccountType
    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(@AccountType Integer accountType) {
        this.accountType = accountType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getModelMap() {
        HashMap<String, Object> modelMap = new HashMap<>();
        modelMap.put("id", id);
        modelMap.put(PHONE, phone);
        modelMap.put("name", name);
        modelMap.put("profilePicLink", profilePicLink);
        modelMap.put("birthDate", birthDate);
        modelMap.put("gender", gender);
        modelMap.put("weight", weight);
        modelMap.put("height", height);
        modelMap.put("specializationId", specializationId);
        modelMap.put("diseaseId", diseaseId);
        modelMap.put(ACCOUNT_TYPE, accountType);
        modelMap.put(Constants.MAP_KEY_CREATED_AT, createdAt);
        modelMap.put(Constants.MAP_KEY_UPDATED_AT, updatedAt);
        return modelMap;
    }
}
