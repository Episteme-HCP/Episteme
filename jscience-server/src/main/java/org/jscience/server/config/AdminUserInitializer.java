/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.server.config;

import org.jscience.server.auth.Roles;
import org.jscience.server.model.User;
import org.jscience.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes the Admin user on startup.
 */
@Component
public class AdminUserInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserInitializer.class);
    private final UserRepository userRepository;

    public AdminUserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create admin user from environment variables (secure configuration)
        String adminUser = System.getenv("JSCIENCE_ADMIN_USER");
        String adminPass = System.getenv("JSCIENCE_ADMIN_PASSWORD");

        if (adminUser != null && adminPass != null) {
            if (userRepository.findByUsername(adminUser).isEmpty()) {
                // Validate password strength
                if (adminPass.length() >= 12 &&
                        !adminPass.equalsIgnoreCase("admin") &&
                        !adminPass.equalsIgnoreCase("secret") &&
                        !adminPass.equalsIgnoreCase("password")) {

                    // Simple hash for now (should match AuthServiceImpl logic)
                    String hashedPass = hashPassword(adminPass);

                    userRepository.save(new User(adminUser, hashedPass, Roles.ADMIN));
                    LOG.info("✓ Created admin user: {}", adminUser);
                } else {
                    LOG.error("🔴 SECURITY: Admin password rejected - too weak or uses default value");
                }
            }
        } else {
            LOG.warn("⚠️  No admin credentials configured (JSCIENCE_ADMIN_USER/PASSWORD not set)");
        }
    }

    private String hashPassword(String plain) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
