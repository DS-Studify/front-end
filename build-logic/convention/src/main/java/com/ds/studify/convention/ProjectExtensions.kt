package com.ds.studify.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandlerScope.implementation(vararg dependencyNotations: Any) {
    for (notation in dependencyNotations) {
        add("implementation", notation)
    }
}

fun DependencyHandlerScope.testImplementation(vararg dependencyNotations: Any) {
    for (notation in dependencyNotations) {
        add("testImplementation", notation)
    }
}

fun DependencyHandlerScope.androidTestImplementation(vararg dependencyNotations: Any) {
    for (notation in dependencyNotations) {
        add("androidTestImplementation", notation)
    }
}

fun DependencyHandlerScope.debugImplementation(vararg dependencyNotations: Any) {
    for (notation in dependencyNotations) {
        add("debugImplementation", notation)
    }
}

fun DependencyHandlerScope.ksp(vararg dependencyNotations: Any) {
    for (notation in dependencyNotations) {
        add("ksp", notation)
    }
}

fun VersionConstraint.toInt() = toString().toInt()