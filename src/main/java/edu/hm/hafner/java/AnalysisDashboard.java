/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.hm.hafner.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for all web requests. Also responsible to start the Spring Boot Application.
 *
 * @author Ullrich Hafner
 */
@SuppressWarnings({"CheckStyle", "NonFinalUtilityClass"})
@SpringBootApplication
public class AnalysisDashboard {
    /**
     * Starts the application.
     *
     * @param args
     *         optional commandline arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(AnalysisDashboard.class, args);
    }
}
