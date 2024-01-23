package com.shopproject.shopbt.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
//    @Bean
//    public FirebaseApp firebaseApp() throws Exception {
//        try{
//            FileInputStream serviceAccount= new FileInputStream("serviceAccountKey.json");
//            FirebaseOptions options=new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://bichthuanshop-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .build();
//            return FirebaseApp.initializeApp(options);
//        }catch (IOException e){
//            throw new RuntimeException("Failed to initialize FirebaseApp: " + e.getMessage());
//        }
//    }
//    @Bean
//    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp){
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }
}
