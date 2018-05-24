package edu.hm.hafner.java;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Defines several architecture rules for the analysis dashboard.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings("JUnitTestMethodWithNoAssertions")
class ArchitectureRulesTest {
    @Test
    void shouldConformToLayering() {
        JavaClasses classes = getClasses();

        classes().that().resideInAPackage("..db..")
                .should().onlyBeAccessed().byAnyPackage("..uc..", "..db..").check(classes);
        classes().that().resideInAPackage("..uc..")
                .should().onlyBeAccessed().byAnyPackage("..ui..", "..uc..").check(classes);
        classes().that().resideInAPackage("..ui..")
                .should().onlyBeAccessed().byAnyPackage("..ui..").check(classes);
    }

    /**
     * Test classes should not be public (Junit 5).
     */
    @Test
    void shouldNotUsePublicInTestCases() {
        JavaClasses classes = getClasses();

        ArchRule noPublicClasses = noClasses()
                .that().dontHaveModifier(JavaModifier.ABSTRACT)
                .and().haveSimpleNameEndingWith("Test")
                .should().bePublic();

        noPublicClasses.check(classes);
    }

    private JavaClasses getClasses() {
        return new ClassFileImporter().importPackages("edu.hm.hafner.java");
    }
}