package com.msaproject.patient.network;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.msaproject.patient.Constants;
import com.msaproject.patient.model.response.UploadStatusResponse;
import com.msaproject.patient.utils.Optional;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class FirebaseManager {

    private final FirebaseFirestore fireStoreInstance;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;

    @Inject
    public FirebaseManager() {
        fireStoreInstance = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    // Firestore

    CollectionReference getCollectionReference(String collectionId) {
        return fireStoreInstance.collection(collectionId);
    }

    public void useCreatedAtServerTime(Map<String, Object> map) {
        map.put(Constants.MAP_KEY_CREATED_AT, FieldValue.serverTimestamp());
    }

    public void useUpdatedAtServerTime(Map<String, Object> map) {
        map.put(Constants.MAP_KEY_UPDATED_AT, FieldValue.serverTimestamp());
    }

    public <T> Single<Optional<T>> getDocumentSnapshot(String collectionId, String documentId, Class<T> parseClass) {
        Single<Optional<T>> observable = Single.create(emitter ->
                fireStoreInstance
                        .collection(collectionId)
                        .document(documentId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (emitter.isDisposed())
                                return;
                            emitter.onSuccess(Optional.ofNullable(documentSnapshot.toObject(parseClass)));
                        })
                        .addOnFailureListener(emitter::onError));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<List<T>> getDocumentsInCollection(String collectionId, Class<T> parseClass) {
        Single<List<T>> observable = Single.create(emitter ->
                fireStoreInstance
                        .collection(collectionId)
                        .get()
                        .addOnCompleteListener(querySnapshotTask -> {
                            if (emitter.isDisposed())
                                return;
                            if (querySnapshotTask.isSuccessful())
                                emitter.onSuccess(Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(parseClass));
                            else
                                emitter.onError(new NullPointerException());
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<List<T>> getDocumentsByQuery(QueryBuilder queryBuilder, Class<T> parseClass) {
        Single<List<T>> observable = Single.create(emitter ->
                queryBuilder
                        .getQuerySnapshotTask()
                        .addOnCompleteListener(querySnapshotTask -> {
                            if (emitter.isDisposed())
                                return;
                            if (querySnapshotTask.isSuccessful())
                                emitter.onSuccess(Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(parseClass));
                            else
                                emitter.onError(querySnapshotTask.getException());
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable setValueToDocument(String collectionId, String documentId, Map<String, Object> objectHashMap) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .set(objectHashMap)
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateDocument(String collectionId, String documentId, Map<String, Object> objectHashMap) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .update(objectHashMap)
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteDocument(String collectionId, String documentId) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // Firebase Database

    public <T> Single<Optional<T>> getSingleEventReferenceSnapshot(String referenceId, Class<T> parseClass) {
        Single<Optional<T>> observable = Single.create(emitter ->
                firebaseDatabase
                        .getReference(referenceId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (emitter.isDisposed())
                                    return;
                                emitter.onSuccess(Optional.ofNullable(snapshot.getValue(parseClass)));
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                emitter.onError(error.toException());
                            }
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<List<T>> getSingleEventReferenceSnapshotList(String referenceId, Class<T> parseClass) {
        Single<List<T>> observable = Single.create(emitter ->
                firebaseDatabase
                        .getReference(referenceId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (emitter.isDisposed())
                                    return;
                                List<T> list = new ArrayList<>();
                                for (DataSnapshot snapshotChild : snapshot.getChildren()) {
                                    list.add(snapshotChild.getValue(parseClass));
                                }
                                emitter.onSuccess(list);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                emitter.onError(error.toException());
                            }
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // Storage

    public Observable<UploadStatusResponse> uploadFileWithProgressObservable(String folderId, String fileNameOnStorage, String extensionOnStorage, Uri photoUri) {
        Observable<UploadStatusResponse> observable = Observable.create(emitter ->
                storageReference
                        .child(folderId)
                        .child(fileNameOnStorage.concat(".").concat(extensionOnStorage))
                        .putFile(photoUri)
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            emitter.onNext(new UploadStatusResponse(false, (int) progress, null));
                        })
                        .addOnCompleteListener(task -> {
                            if (emitter.isDisposed())
                                return;
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                    emitter.onNext(new UploadStatusResponse(true, 100, uri.toString()));
                                    emitter.onComplete();
                                }).addOnFailureListener(emitter::onError);
                            } else
                                emitter.onError(Objects.requireNonNull(task.getException()));
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteFileFromStorage(String fileDownloadUrl) {
        Completable completable = Completable.create(emitter -> firebaseStorage.getReferenceFromUrl(fileDownloadUrl).delete().addOnCompleteListener(task -> {
            if (emitter.isDisposed())
                return;
            if (task.isSuccessful())
                emitter.onComplete();
            else
                emitter.onError(task.getException());
        }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
