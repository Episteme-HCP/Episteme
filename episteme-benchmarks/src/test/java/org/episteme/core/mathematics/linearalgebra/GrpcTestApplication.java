/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.episteme.server.server.service.MatrixServiceImpl;

/**
 * Lightweight gRPC server configuration for high-precision audit tests.
 * Excludes database dependencies to speed up startup and avoid configuration issues.
 */
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    org.springframework.cloud.vault.config.VaultAutoConfiguration.class
})
@Import(MatrixServiceImpl.class)
public class GrpcTestApplication {

    public static ConfigurableApplicationContext start() {
        System.setProperty("grpc.server.port", "50051");
        System.setProperty("spring.main.web-application-type", "none");
        System.setProperty("spring.cloud.compatibility-verifier.enabled", "false");
        SpringApplication app = new SpringApplication(GrpcTestApplication.class);
        return app.run();
    }
}
