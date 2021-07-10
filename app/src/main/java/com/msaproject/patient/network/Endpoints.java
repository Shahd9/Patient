package com.msaproject.patient.network;

public class Endpoints {

    public static final String BASE_URL = "https://us-central1-mimetic-pursuit-318107.cloudfunctions.net";

    // Recommendation API
    public static final String RECOMMENDATION_API = "/recommend_api";


    // Collections
    public static String COLLECTION_STATICS = "Statics";
    public static String COLLECTION_SPECIALIZATIONS = "Specializations";
    public static String COLLECTION_DISEASES = "Diseases";
    public static String COLLECTION_USERS = "Users";
    public static String COLLECTION_MEDICAL_FILES = "MedicalFiles";
    public static String COLLECTION_PATIENT_DOCTOR = "PatientDoctor";
    public static String COLLECTION_APPOINTMENTS = "Appointments";
    public static String COLLECTION_APPOINTMENT_IMAGES = "AppointmentImages";

    // Documents
    public static String DOCUMENT_APP_CURRENT_CONFIGS = "AppCurrentConfigs";

    // Firebase database references
    public static String REFERENCE_DISEASES_RECOMMENDATIONS = "disease_users/X";
    public static String REFERENCE_RECIPES = "recipes/X";

    // Storage Folders
    public static final String FOLDER_PROFILE_PICTURES = "ProfilePictures";
    public static final String FOLDER_MEDICAL_FILES = "MedicalFiles";
    public static final String FOLDER_APPOINTMENT_IMAGES = "AppointmentImages";
}
