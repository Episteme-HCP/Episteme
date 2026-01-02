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
