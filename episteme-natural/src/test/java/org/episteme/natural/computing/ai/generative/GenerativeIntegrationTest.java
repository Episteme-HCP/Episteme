/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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
package org.episteme.natural.computing.ai.generative;

import org.episteme.natural.computing.ai.generative.prompt.PromptTemplate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Verification for Generative AI components.
 */
public class GenerativeIntegrationTest {

    @Test
    public void testPromptTemplate() {
        String templateStr = "Hello {{name}}, welcome to {{city}}!";
        PromptTemplate template = new PromptTemplate(templateStr);
        
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", "User");
        vars.put("city", "Paris");
        
        String result = template.render(vars);
        assertEquals("Hello User, welcome to Paris!", result);
    }

    @Test
    public void testToolCallingRegistry() throws Exception {
        ToolCallingRegistry registry = new ToolCallingRegistry();
        
        // Register a simple calculator tool
        Calculator calc = new Calculator();
        registry.registerTool("add", calc, "add");
        
        Object result = registry.execute("add", 5, 3);
        assertEquals(8, result);
    }
    
    // Simple helper class for testing tool calling
    public static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
    }
}
