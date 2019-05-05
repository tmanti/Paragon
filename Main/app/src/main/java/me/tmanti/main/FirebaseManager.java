package me.tmanti.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private FirebaseFunctions functions;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    public FirebaseManager(){
        this.functions = FirebaseFunctions.getInstance();
    }

    public Task<String> createUser(String name, String description){
        getAuth();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);

        return functions.getHttpsCallable("createUser").call(data).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result = (String) task.getResult().getData();
                return result;
            }
        });
    }

    public Task<String> createItemSet(String name, String description){
        getAuth();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);

        return functions.getHttpsCallable("createItemSet").call(data).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result = (String) task.getResult().getData();
                return result;
            }
        });
    }

    public Task<HashMap<String, Object>> getUserInfo(String uid){
        getAuth();
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        return functions.getHttpsCallable("getUserInfo").call(data).continueWith(new Continuation<HttpsCallableResult, HashMap<String, Object>>() {
            @Override
            public HashMap<String, Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                HashMap<String, Object> results = (HashMap<String, Object>) task.getResult().getData();
                HashMap<String, Object> response = (HashMap<String, Object>) results.get("response");

                Log.wtf("asd", task.getResult().getData().toString());

                return response;
            }
        });
    }

    public Task<String> userExists(){
        getAuth();
        return functions.getHttpsCallable("userExists").call().continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                HashMap<String, Object> results = (HashMap<String, Object>) task.getResult().getData();
                Boolean response = (Boolean) results.get("response");
                if(response) {
                    return "true";
                } else {
                    return "false";
                }
            }
        });
    }

    public Task<String> updateUser(HashMap<String, Object> data){
        return functions.getHttpsCallable("updateUser").call(data).continueWith(new Continuation<HttpsCallableResult, String>() {
            @Override
            public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result = (String) task.getResult().getData();
                return result;
            }
        });
    }

    private void getAuth(){
        this.user = mAuth.getCurrentUser();
    }
}
