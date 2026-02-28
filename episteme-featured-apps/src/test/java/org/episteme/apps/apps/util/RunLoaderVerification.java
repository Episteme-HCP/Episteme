package org.episteme.apps.apps.util;

import org.junit.jupiter.api.Test;

public class RunLoaderVerification {
    @Test
    public void run() {
        System.out.println("USER.DIR: " + System.getProperty("user.dir"));
        LoaderVerification.main(new String[0]);
    }
}
