package io.github.architpanigrahi.springbootdsl.dependency

enum class DependencyConfiguration(
    val gradleName: String
) {
    IMPLEMENTATION("implementation"),
    RUNTIME_ONLY("runtimeOnly"),
    COMPILE_ONLY("compileOnly"),
    ANNOTATION_PROCESSOR("annotationProcessor"),
    TEST_IMPLEMENTATION("testImplementation")
}