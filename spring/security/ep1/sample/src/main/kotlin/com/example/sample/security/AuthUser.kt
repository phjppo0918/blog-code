package com.example.sample.security

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal
annotation class AuthUser(
    val errorOnInvalidType: Boolean = true,
)
