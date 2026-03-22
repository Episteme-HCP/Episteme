import os
import re

files_to_fix = {
    # File -> list of (pattern, replacement, flags)
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-client\src\test\java\org\episteme\client\client\distributed\GrpcDistributedContextTest.java": [
        (r'^\s*@SuppressWarnings\("unchecked"\)\s*\n\s*io\.grpc\.Server server = grpcCleanup\.register\(', r'        grpcCleanup.register(', re.MULTILINE),
        (r'^\s*@SuppressWarnings\("unchecked"\)\s*\n\s*ManagedChannel channel = grpcCleanup\.register\(', r'        ManagedChannel channel = grpcCleanup.register(', re.MULTILINE),
        (r'public void setUp\(\) throws Exception \{', r'@SuppressWarnings("null")\n    public void setUp() throws Exception {', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-core\src\main\java\org\episteme\core\mathematics\linearalgebra\matrices\SIMDRealDoubleMatrix.java": [
        (r'import org\.episteme\.core\.mathematics\.linearalgebra\.matrices\.solvers;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-core\src\main\java\org\episteme\core\mathematics\linearalgebra\matrices\TiledMatrix.java": [
        (r'import org\.episteme\.core\.mathematics\.context\.MathContext;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-core\src\test\java\org\episteme\core\mathematics\linearalgebra\algorithms\tests\CARMAAlgorithmTest.java": [
        (r'import org\.episteme\.core\.mathematics\.context\.MathContext;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\main\java\org\episteme\nativ\mathematics\linearalgebra\backends\NativeCUDASparseLinearAlgebraBackend.java": [
        (r'^\s*private static MethodHandle CUSPARSE_DESTROY;\n', '', re.MULTILINE),
        (r'^\s*private static MethodHandle CUSOLVER_SP_DESTROY;\n', '', re.MULTILINE),
        (r'^\s*private static MethodHandle CUSOLVER_SP_D_CSRLSV_LU;\n', '', re.MULTILINE),
        (r'^\s*private static MethodHandle CUSPARSE_CREATE_MAT_DESCR;\n', '', re.MULTILINE),
        (r'^\s*private static MethodHandle CUSPARSE_DESTROY_MAT_DESCR;\n', '', re.MULTILINE),
        (r'^\s*private static MemorySegment CUSOLVER_HANDLE;\n', '', re.MULTILINE),
        (r'^\s*CUSPARSE_DESTROY = lookup.*cusparseDestroy.*\n', '', re.MULTILINE),
        (r'^\s*CUSPARSE_CREATE_MAT_DESCR = lookup.*cusparseCreateMatDescr.*\n', '', re.MULTILINE),
        (r'^\s*CUSPARSE_DESTROY_MAT_DESCR = lookup.*cusparseDestroyMatDescr.*\n', '', re.MULTILINE),
        (r'^\s*CUSOLVER_SP_DESTROY = lookup.*cusolverSpDestroy.*\n', '', re.MULTILINE),
        (r'^\s*CUSOLVER_SP_D_CSRLSV_LU = lookup.*cusolverSpDcsrlsvlu.*\n', '', re.MULTILINE),
        (r'^\s*CUSOLVER_HANDLE = MemorySegment.*\n', '', re.MULTILINE),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\main\java\org\episteme\nativ\mathematics\linearalgebra\tensors\backends\NativeND4JCUDASparseTensorBackend.java": [
        (r'import org\.episteme\.core\.mathematics\.numbers\.real\.Real;\n?', '', 0),
        (r'import org\.episteme\.core\.mathematics\.linearalgebra\.tensors\.backends\.CPUSparseTensorBackend;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\test\java\org\episteme\nativ\mathematics\analysis\MPFRPrecisionComplianceSuite.java": [
        (r'import org\.episteme\.core\.mathematics\.numbers\.real\.RealBig;\n?', '', 0),
        (r'import org\.episteme\.core\.mathematics\.linearalgebra\.Matrix;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\test\java\org\episteme\nativ\mathematics\analysis\MPFRPrecisionComplianceTest.java": [
        (r'import java\.math\.BigDecimal;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\test\java\org\episteme\nativ\mathematics\linearalgebra\backends\MPFRSparseSolverTest.java": [
        (r'import org\.episteme\.core\.mathematics\.linearalgebra\.Matrix;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-native\src\test\java\org\episteme\nativ\technical\backend\nativ\NativeSafeTest.java": [
        (r'import org\.junit\.jupiter\.api\.AfterAll;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-natural\src\test\java\org\episteme\natural\physics\classical\mechanics\nbody\providers\DistributedNBodyVerificationTest.java": [
        (r'import org\.episteme\.core\.mathematics\.context\.MathContext;\n?', '', 0),
    ],
    r"c:\Silvere\Encours\Developpement\Episteme\episteme-core\src\main\java\org\episteme\core\mathematics\linearalgebra\providers\CPUDenseLinearAlgebraProvider.java": [
        (r'ctx\.checkCancelled\(\);', r'org.episteme.core.mathematics.context.MathContext.checkCurrentCancelled();', 0),
    ]
}

for path, substitutions in files_to_fix.items():
    if not os.path.exists(path):
        print(f"File not found: {path}")
        continue
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = content
    for pattern, replacement, flags in substitutions:
        new_content = re.sub(pattern, replacement, new_content, flags=flags)
    
    if new_content != content:
        with open(path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {path}")
    else:
        print(f"No changes made to {path}")
