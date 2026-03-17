package org.episteme.nativ.physics.classical;

import org.episteme.natural.physics.classical.mechanics.collision.MechanicsBackend;
import org.episteme.natural.physics.classical.mechanics.collision.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.collision.RigidBodyBridge;
import org.episteme.nativ.physics.classical.mechanics.collision.NativeCollisionProvider;
import org.junit.jupiter.api.Test;
import java.util.ServiceLoader;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Compliance tests for native physics providers to ensure they handle 
 * unsupported operations correctly and provide valid execution contexts.
 */
public class PhysicsProviderComplianceTest {

    @Test
    public void testMechanicsBackendCompliance() {
        ServiceLoader<MechanicsBackend> loader = ServiceLoader.load(MechanicsBackend.class);
        boolean found = false;
        for (MechanicsBackend backend : loader) {
            found = true;
            System.out.println("Verifying MechanicsBackend: " + backend.getName());
            
            // Execution context should never be null
            assertNotNull(backend.createContext(), "ExecutionContext must not be null for " + backend.getName());
            
            // Check for explicit UOE instead of null for createRigidBody or createWorld if not supported
            try {
                PhysicsWorldBridge world = backend.createWorld();
                assertNotNull(world, "World should be non-null if createWorld persists");
            } catch (UnsupportedOperationException e) {
                System.out.println("  [OK] createWorld() correctly throws UnsupportedOperationException");
            }

            try {
                // We pass null because we just want to see if it throws UOE or NPE early
                backend.createRigidBody(null);
                // If it doesn't throw, it should return null or a bridge. 
                // But as per our refactor, it should throw if body-less creation isn't supported.
            } catch (UnsupportedOperationException e) {
                System.out.println("  [OK] createRigidBody() correctly throws UnsupportedOperationException");
            } catch (NullPointerException e) {
                // Acceptable if it tried to use the body
            }
        }
        assertTrue(found, "At least one MechanicsBackend should be found via ServiceLoader");
    }

    @Test
    public void testNativeCollisionProviderCompliance() {
        ServiceLoader<NativeCollisionProvider> loader = ServiceLoader.load(NativeCollisionProvider.class);
        for (NativeCollisionProvider provider : loader) {
            System.out.println("Verifying NativeCollisionProvider: " + provider.getName());
            if (provider.isLoaded()) {
                // Basic check that parallelExecute doesn't just hang or crash (can't easily check for UOE here without dummy tasks)
            }
        }
    }
}
