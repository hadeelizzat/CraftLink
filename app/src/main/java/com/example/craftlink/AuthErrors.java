package com.example.craftlink;

import com.google.firebase.FirebaseNetworkException;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
public class AuthErrors {
    public static String message(Exception e) {

        if (e instanceof FirebaseAuthInvalidUserException

                || e instanceof FirebaseAuthInvalidCredentialsException) {

            return "البريد أو كلمة المرور غير صحيحة";

        }

        if (e instanceof FirebaseAuthUserCollisionException) {

            return "هذا البريد مسجل مسبقاً";

        }

        if (e instanceof FirebaseAuthWeakPasswordException) {

            return "كلمة المرور ضعيفة، اختر كلمة أقوى";

        }

        if (e instanceof FirebaseNetworkException) {

            return "تحقق من اتصال الإنترنت";

        }

        return "حدث خطأ، حاول مرة أخرى";
    }
}
