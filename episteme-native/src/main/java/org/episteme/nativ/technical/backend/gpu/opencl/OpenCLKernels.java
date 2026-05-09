/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.gpu.opencl;

/**
 * Repository for OpenCL kernel sources.
 */
public final class OpenCLKernels {
    
    private OpenCLKernels() {}

    public static final String FP64_HEADER = 
        "#if defined(cl_khr_fp64)\n" +
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
        "#elif defined(cl_amd_fp64)\n" +
        "#pragma OPENCL EXTENSION cl_amd_fp64 : enable\n" +
        "#endif\n\n";

    public static final String DENSE_DOUBLE_KERNELS = 
        "__kernel void matrixMultiply(__global const double *a, __global const double *b, __global double *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        double sum = 0.0;\n" +
        "        for (int i = 0; i < k; i++) sum += a[row*k+i] * b[i*n+col];\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void saxpy(__global const double *x, __global double *y, const double alpha, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) y[i] = alpha * x[i] + y[i];\n" +
        "}\n" +
        "__kernel void vec_dot(__global const double *a, __global const double *b, __global double *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        double sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * b[i];\n" +
        "        result[0] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_norm(__global const double *a, __global double *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        double sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * a[i];\n" +
        "        result[0] = sqrt(sum);\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_sub(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] - b[i];\n" +
        "}\n" +
        "__kernel void vec_scale(__global const double *a, const double s, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] * s;\n" +
        "}\n" +
        "__kernel void vec_dot_partial(__global const double *a, __global const double *b, __global double *partial_sums, const int n, __local double *local_sums) {\n" +
        "    int local_id = get_local_id(0); int group_id = get_group_id(0); int local_size = get_local_size(0); int i = get_global_id(0);\n" +
        "    double val = (i < n) ? a[i] * b[i] : 0.0;\n" +
        "    local_sums[local_id] = val; barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    for (int stride = local_size / 2; stride > 0; stride /= 2) {\n" +
        "        if (local_id < stride) local_sums[local_id] += local_sums[local_id + stride];\n" +
        "        barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    }\n" +
        "    if (local_id == 0) partial_sums[group_id] = local_sums[0];\n" +
        "}\n" +
        "__kernel void gaussElimPhase1(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double factor = a[i*n + k] / a[k*n + k];\n" +
        "        for (int j = k + 1; j < n; j++) a[i*n + j] -= factor * a[k*n + j];\n" +
        "        a[i*n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void transpose(__global const double *a, __global double *b, const int rows, const int cols) {\n" +
        "    int r = get_global_id(1); int c = get_global_id(0);\n" +
        "    if (r < rows && c < cols) b[c * rows + r] = a[r * cols + c];\n" +
        "}\n" +
        "__kernel void vec_add(__global const double *a, __global const double *b, __global double *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] + b[i];\n" +
        "}\n" +
        "__kernel void normalizeRow(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    double pivot = a[k * cols + k];\n" +
        "    if (j < cols) a[k * cols + j] /= pivot;\n" +
        "    if (j == k + 1) a[k * cols + k] = 1.0;\n" +
        "}\n" +
        "__kernel void gaussJordan(__global double *a, const int rows, const int cols, const int k) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < rows && i != k) {\n" +
        "        double pivot = a[k * cols + k];\n" +
        "        if (fabs(pivot) < 1e-15) return;\n" +
        "        double factor = a[i * cols + k] / pivot;\n" +
        "        for (int j = k + 1; j < cols; j++) a[i * cols + j] -= factor * a[k * cols + j];\n" +
        "        a[i * cols + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void normalizeRowInv(__global double *a, __global double *inv, const int n, const int k) {\n" +
        "    int j = get_global_id(0);\n" +
        "    if (j < n) {\n" +
        "        double pivot = a[k * n + k];\n" +
        "        if (j > k) a[k * n + j] /= pivot;\n" +
        "        inv[k * n + j] /= pivot;\n" +
        "        if (j == k) a[k * n + k] = 1.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussJordanInv(__global double *a, __global double *inv, const int n, const int k) {\n" +
        "    int j = get_global_id(0); int i = get_global_id(1);\n" +
        "    if (i < n && j < n && i != k) {\n" +
        "        double factor = a[i * n + k];\n" +
        "        if (j > k) a[i * n + j] -= factor * a[k * n + j];\n" +
        "        inv[i * n + j] -= factor * inv[k * n + j];\n" +
        "        if (j == k) a[i * n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void swapRows(__global double *a, const int n, const int r1, const int r2) {\n" +
        "    int j = get_global_id(0);\n" +
        "    if (j < n) {\n" +
        "        double temp = a[r1 * n + j];\n" +
        "        a[r1 * n + j] = a[r2 * n + j];\n" +
        "        a[r2 * n + j] = temp;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussElimPhase1WithB(__global double *a, __global double *b, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double factor = a[i*n + k] / a[k*n + k];\n" +
        "        for (int j = k + 1; j < n; j++) a[i*n + j] -= factor * a[k*n + j];\n" +
        "        b[i] -= factor * b[k];\n" +
        "        a[i*n + k] = 0.0;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void lu_decompose_step(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(1) + k + 1;\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    if (i < n && j < n) {\n" +
        "        if (j == k + 1) a[i * n + k] /= a[k * n + k];\n" +
        "        a[i * n + j] -= a[i * n + k] * a[k * n + j];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void cholesky_decompose_step(__global double *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        double sum = 0.0;\n" +
        "        for (int j = 0; j < k; j++) sum += a[i * n + j] * a[k * n + j];\n" +
        "        a[i * n + k] = (a[i * n + k] - sum) / a[k * n + k];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void qr_householder_apply(__global double *a, const int rows, const int cols, const int k, __global const double *v) {\n" +
        "    int j = get_global_id(0) + k;\n" +
        "    int i = get_global_id(1) + k;\n" +
        "    if (i < rows && j < cols) {\n" +
        "        double dot = 0.0;\n" +
        "        for (int m = k; m < rows; m++) dot += v[m] * a[m * cols + j];\n" +
        "        a[i * cols + j] -= 2.0 * v[i] * dot;\n" +
        "    }\n" +
        "}\n" +
        "typedef struct { double r; double i; } double2_custom;\n" +
        "__kernel void complex_saxpy(__global const double2_custom *x, __global double2_custom *y, const double2_custom alpha, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) {\n" +
        "        double2_custom xv = x[i]; double2_custom yv = y[i];\n" +
        "        y[i].r = yv.r + alpha.r * xv.r - alpha.i * xv.i;\n" +
        "        y[i].i = yv.i + alpha.r * xv.i + alpha.i * xv.r;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complexMatrixMultiply(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        double2_custom sum = {0.0, 0.0};\n" +
        "        for (int i = 0; i < k; i++) {\n" +
        "            double2_custom av = a[row*k+i];\n" +
        "            double2_custom bv = b[i*n+col];\n" +
        "            sum.r += av.r * bv.r - av.i * bv.i;\n" +
        "            sum.i += av.r * bv.i + av.i * bv.r;\n" +
        "        }\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_vec_add(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].r = a[i].r + b[i].r; c[i].i = a[i].i + b[i].i; }\n" +
        "}\n" +
        "__kernel void complex_vec_sub(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].r = a[i].r - b[i].r; c[i].i = a[i].i - b[i].i; }\n" +
        "}\n" +
        "__kernel void complex_vec_scale(__global const double2_custom *a, const double2_custom s, __global double2_custom *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) {\n" +
        "        double2_custom av = a[i];\n" +
        "        c[i].r = av.r * s.r - av.i * s.i;\n" +
        "        c[i].i = av.r * s.i + av.i * s.r;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_dot_partial(__global const double2_custom *a, __global const double2_custom *b, __global double2_custom *partial_sums, const int n, __local double2_custom *local_sums) {\n" +
        "    int local_id = get_local_id(0); int group_id = get_group_id(0); int local_size = get_local_size(0); int i = get_global_id(0);\n" +
        "    double2_custom val = {0.0, 0.0};\n" +
        "    if (i < n) {\n" +
        "        double2_custom av = a[i]; double2_custom bv = b[i];\n" +
        "        val.r = av.r * bv.r + av.i * bv.i;\n" +
        "        val.i = av.i * bv.r - av.r * bv.i;\n" +
        "    }\n" +
        "    local_sums[local_id] = val; barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    for (int stride = local_size / 2; stride > 0; stride /= 2) {\n" +
        "        if (local_id < stride) {\n" +
        "            local_sums[local_id].r += local_sums[local_id + stride].r;\n" +
        "            local_sums[local_id].i += local_sums[local_id + stride].i;\n" +
        "        }\n" +
        "        barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    }\n" +
        "    if (local_id == 0) partial_sums[group_id] = local_sums[0];\n" +
        "}\n" +
        "__kernel void hestenes_jacobi_dot(__global const double *a, int m, int n, int i_col, int j_col, __global double *out_dot) {\n" +
        "    if (get_global_id(0) == 0) {\n" +
        "        double s_ii = 0; double s_jj = 0; double s_ij = 0;\n" +
        "        for (int k = 0; k < m; k++) {\n" +
        "            double val_i = a[k * n + i_col]; double val_j = a[k * n + j_col];\n" +
        "            s_ii += val_i * val_i; s_jj += val_j * val_j; s_ij += val_i * val_j;\n" +
        "        }\n" +
        "        out_dot[0] = s_ii; out_dot[1] = s_jj; out_dot[2] = s_ij;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void hestenes_jacobi_apply(__global double *a, __global double *v, int m, int n, int i_col, int j_col, double cos_v, double sin_v) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < m) {\n" +
        "        double ai = a[row * n + i_col]; double aj = a[row * n + j_col];\n" +
        "        a[row * n + i_col] = cos_v * ai + sin_v * aj;\n" +
        "        a[row * n + j_col] = -sin_v * ai + cos_v * aj;\n" +
        "    }\n" +
        "    if (row < n) {\n" +
        "        double vi = v[row * n + i_col]; double vj = v[row * n + j_col];\n" +
        "        "        v[row * n + i_col] = cos_v * vi + sin_v * vj;\n" +
        "        v[row * n + j_col] = -sin_v * vi + cos_v * vj;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void solve_triangular_lower(__global const double *a, __global double *b, const int n, const int unit_diag) {\n" +
        "    for (int i = 0; i < n; i++) {\n" +
        "        if (get_global_id(0) == 0) {\n" +
        "            double sum = 0.0;\n" +
        "            for (int j = 0; j < i; j++) sum += a[i * n + j] * b[j];\n" +
        "            double diag = unit_diag ? 1.0 : a[i * n + i];\n" +
        "            b[i] = (b[i] - sum) / diag;\n" +
        "        }\n" +
        "        barrier(CLK_GLOBAL_MEM_FENCE);\n" +
        "    }\n" +
        "}\n" +
        "__kernel void solve_triangular_upper(__global const double *a, __global double *b, const int n, const int unit_diag) {\n" +
        "    for (int i = n - 1; i >= 0; i--) {\n" +
        "        if (get_global_id(0) == 0) {\n" +
        "            double sum = 0.0;\n" +
        "            for (int j = i + 1; j < n; j++) sum += a[i * n + j] * b[j];\n" +
        "            double diag = unit_diag ? 1.0 : a[i * n + i];\n" +
        "            b[i] = (b[i] - sum) / diag;\n" +
        "        }\n" +
        "        barrier(CLK_GLOBAL_MEM_FENCE);\n" +
        "    }\n" +
        "}\n";

    public static final String DENSE_FLOAT_KERNELS = 
        "__kernel void matrixMultiplyFloat(__global const float *a, __global const float *b, __global float *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        float sum = 0.0f;\n" +
        "        for (int i = 0; i < k; i++) sum += a[row*k+i] * b[i*n+col];\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_add_float(__global const float *a, __global const float *b, __global float *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] + b[i];\n" +
        "}\n" +
        "__kernel void vec_sub_float(__global const float *a, __global const float *b, __global float *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] - b[i];\n" +
        "}\n" +
        "__kernel void vec_scale_float(__global const float *a, const float s, __global float *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) c[i] = a[i] * s;\n" +
        "}\n" +
        "__kernel void transposeFloat(__global const float *a, __global float *b, const int rows, const int cols) {\n" +
        "    int r = get_global_id(1); int c = get_global_id(0);\n" +
        "    if (r < rows && c < cols) b[c * rows + r] = a[r * cols + c];\n" +
        "}\n" +
        "__kernel void saxpy_float(__global const float *x, __global float *y, const float alpha, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) y[i] = alpha * x[i] + y[i];\n" +
        "}\n" +
        "__kernel void complex_saxpy_float(__global const float2 *x, __global float2 *y, const float2 alpha, const int n) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n) {\n" +
        "        float2 xv = x[i]; float2 yv = y[i];\n" +
        "        y[i].x = yv.x + alpha.x * xv.x - alpha.y * xv.y;\n" +
        "        y[i].y = yv.y + alpha.x * xv.y + alpha.y * xv.x;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_dot_float(__global const float *a, __global const float *b, __global float *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        float sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * b[i];\n" +
        "        result[0] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_norm_float(__global const float *a, __global float *result, const int n) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row == 0) {\n" +
        "        float sum = 0;\n" +
        "        for (int i = 0; i < n; i++) sum += a[i] * a[i];\n" +
        "        result[0] = sqrt(sum);\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussElimPhase1Float(__global float *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        float factor = a[i*n + k] / a[k*n + k];\n" +
        "        for (int j = k + 1; j < n; j++) a[i*n + j] -= factor * a[k*n + j];\n" +
        "        a[i*n + k] = 0.0f;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussJordanFloat(__global float *a, const int rows, const int cols, const int k) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < rows && i != k) {\n" +
        "        float pivot = a[k * cols + k];\n" +
        "        if (fabs(pivot) < 1e-7f) return;\n" +
        "        float factor = a[i * cols + k] / pivot;\n" +
        "        for (int j = k + 1; j < cols; j++) a[i * cols + j] -= factor * a[k * cols + j];\n" +
        "        a[i * cols + k] = 0.0f;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void normalizeRowFloat(__global float *a, const int rows, const int cols, const int k) {\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    float pivot = a[k * cols + k];\n" +
        "    if (j < cols) a[k * cols + j] /= pivot;\n" +
        "    if (j == k + 1) a[k * cols + k] = 1.0f;\n" +
        "}\n" +
        "__kernel void normalizeRowInvFloat(__global float *a, __global float *inv, const int n, const int k) {\n" +
        "    int j = get_global_id(0);\n" +
        "    if (j < n) {\n" +
        "        float pivot = a[k * n + k];\n" +
        "        inv[k * n + j] /= pivot;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void gaussJordanInvFloat(__global float *a, __global float *inv, const int n, const int k) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i < n && i != k) {\n" +
        "        float factor = a[i * n + k];\n" +
        "        for (int j = 0; j < n; j++) inv[i * n + j] -= factor * inv[k * n + j];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void lu_decompose_step_float(__global float *a, const int n, const int k) {\n" +
        "    int i = get_global_id(1) + k + 1;\n" +
        "    int j = get_global_id(0) + k + 1;\n" +
        "    if (i < n && j < n) {\n" +
        "        if (j == k + 1) a[i * n + k] /= a[k * n + k];\n" +
        "        a[i * n + j] -= a[i * n + k] * a[k * n + j];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void cholesky_decompose_step_float(__global float *a, const int n, const int k) {\n" +
        "    int i = get_global_id(0) + k + 1;\n" +
        "    if (i < n) {\n" +
        "        float sum = 0.0f;\n" +
        "        for (int j = 0; j < k; j++) sum += a[i * n + j] * a[k * n + j];\n" +
        "        a[i * n + k] = (a[i * n + k] - sum) / a[k * n + k];\n" +
        "    }\n" +
        "}\n" +
        "__kernel void qr_householder_apply_float(__global float *a, const int rows, const int cols, const int k, __global const float *v) {\n" +
        "    int j = get_global_id(0) + k;\n" +
        "    int i = get_global_id(1) + k;\n" +
        "    if (i < rows && j < cols) {\n" +
        "        float dot = 0.0f;\n" +
        "        for (int m = k; m < rows; m++) dot += v[m] * a[m * cols + j];\n" +
        "        a[i * cols + j] -= 2.0f * v[i] * dot;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void solve_triangular_lower_float(__global const float *a, __global float *b, const int n, const int unit_diag) {\n" +
        "    for (int i = 0; i < n; i++) {\n" +
        "        if (get_global_id(0) == 0) {\n" +
        "            float sum = 0.0f;\n" +
        "            for (int j = 0; j < i; j++) sum += a[i * n + j] * b[j];\n" +
        "            float diag = unit_diag ? 1.0f : a[i * n + i];\n" +
        "            b[i] = (b[i] - sum) / diag;\n" +
        "        }\n" +
        "        barrier(CLK_GLOBAL_MEM_FENCE);\n" +
        "    }\n" +
        "}\n" +
        "__kernel void solve_triangular_upper_float(__global const float *a, __global float *b, const int n, const int unit_diag) {\n" +
        "    for (int i = n - 1; i >= 0; i--) {\n" +
        "        if (get_global_id(0) == 0) {\n" +
        "            float sum = 0.0f;\n" +
        "            for (int j = i + 1; j < n; j++) sum += a[i * n + j] * b[j];\n" +
        "            float diag = unit_diag ? 1.0f : a[i * n + i];\n" +
        "            b[i] = (b[i] - sum) / diag;\n" +
        "        }\n" +
        "        barrier(CLK_GLOBAL_MEM_FENCE);\n" +
        "    }\n" +
        "}\n";
    
    // TODO: Add more float kernels as needed (Sparse, etc.)

    public static final String SPARSE_FLOAT_KERNELS =
        "__kernel void spmv_csr_float(int num_rows, __global const int* ptr, __global const int* indices, __global const float* values, __global const float* x, __global float* y) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        float dot = 0.0f;\n" +
        "        int start = ptr[row];\n" +
        "        int end = ptr[row+1];\n" +
        "        for (int j = start; j < end; j++) {\n" +
        "            dot += values[j] * x[indices[j]];\n" +
        "        }\n" +
        "        y[row] = dot;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_spmv_csr_float(int num_rows, __global const int* ptr, __global const int* indices, __global const float2* values, __global const float2* x, __global float2* y) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        float2 dot = (float2)(0.0f, 0.0f);\n" +
        "        int start = ptr[row];\n" +
        "        int end = ptr[row+1];\n" +
        "        for (int j = start; j < end; j++) {\n" +
        "            float2 av = values[j];\n" +
        "            float2 xv = x[indices[j]];\n" +
        "            dot.x += av.x * xv.x - av.y * xv.y;\n" +
        "            dot.y += av.x * xv.y + av.y * xv.x;\n" +
        "        }\n" +
        "        y[row] = dot;\n" +
        "    }\n" +
        "}\n";

    public static final String DENSE_FLOAT_COMPLEX_KERNELS =
        "__kernel void complexMatrixMultiplyFloat(__global const float2 *a, __global const float2 *b, __global float2 *c, const int m, const int n, const int k) {\n" +
        "    int row = get_global_id(1); int col = get_global_id(0);\n" +
        "    if (row < m && col < n) {\n" +
        "        float2 sum = (float2)(0.0f, 0.0f);\n" +
        "        for (int i = 0; i < k; i++) {\n" +
        "            float2 av = a[row*k+i];\n" +
        "            float2 bv = b[i*n+col];\n" +
        "            sum.x += av.x * bv.x - av.y * bv.y;\n" +
        "            sum.y += av.x * bv.y + av.y * bv.x;\n" +
        "        }\n" +
        "        c[row*n+col] = sum;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_vec_add_float(__global const float2 *a, __global const float2 *b, __global float2 *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].x = a[i].x + b[i].x; c[i].y = a[i].y + b[i].y; }\n" +
        "}\n" +
        "__kernel void complex_vec_sub_float(__global const float2 *a, __global const float2 *b, __global float2 *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) { c[i].x = a[i].x - b[i].x; c[i].y = a[i].y - b[i].y; }\n" +
        "}\n" +
        "__kernel void complex_vec_scale_float(__global const float2 *a, const float2 s, __global float2 *c, const int n) {\n" +
        "    int i = get_global_id(0); if (i < n) {\n" +
        "        float2 av = a[i];\n" +
        "        c[i].x = av.x * s.x - av.y * s.y;\n" +
        "        c[i].y = av.x * s.y + av.y * s.x;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void vec_dot_partial_float(__global const float *a, __global const float *b, __global float *partial_sums, const int n, __local float *local_sums) {\n" +
        "    int local_id = get_local_id(0); int group_id = get_group_id(0); int local_size = get_local_size(0); int i = get_global_id(0);\n" +
        "    float val = (i < n) ? a[i] * b[i] : 0.0f;\n" +
        "    local_sums[local_id] = val; barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    for (int stride = local_size / 2; stride > 0; stride /= 2) {\n" +
        "        if (local_id < stride) local_sums[local_id] += local_sums[local_id + stride];\n" +
        "        barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    }\n" +
        "    if (local_id == 0) partial_sums[group_id] = local_sums[0];\n" +
        "}\n" +
        "__kernel void complex_dot_partial_float(__global const float2 *a, __global const float2 *b, __global float2 *partial_sums, const int n, __local float2 *local_sums) {\n" +
        "    int local_id = get_local_id(0); int group_id = get_group_id(0); int local_size = get_local_size(0); int i = get_global_id(0);\n" +
        "    float2 val = (float2)(0.0f, 0.0f);\n" +
        "    if (i < n) {\n" +
        "        float2 av = a[i]; float2 bv = b[i];\n" +
        "        val.x = av.x * bv.x + av.y * bv.y;\n" +
        "        val.y = av.y * bv.x - av.x * bv.y;\n" +
        "    }\n" +
        "    local_sums[local_id] = val; barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    for (int stride = local_size / 2; stride > 0; stride /= 2) {\n" +
        "        if (local_id < stride) {\n" +
        "            local_sums[local_id].x += local_sums[local_id + stride].x;\n" +
        "            local_sums[local_id].y += local_sums[local_id + stride].y;\n" +
        "        }\n" +
        "        barrier(CLK_LOCAL_MEM_FENCE);\n" +
        "    }\n" +
        "    if (local_id == 0) partial_sums[group_id] = local_sums[0];\n" +
        "}\n";

    public static final String SPARSE_DOUBLE_KERNELS =
        "__kernel void spmv_csr_double(int num_rows, __global const int* ptr, __global const int* indices, __global const double* values, __global const double* x, __global double* y) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        double dot = 0.0;\n" +
        "        int start = ptr[row];\n" +
        "        int end = ptr[row+1];\n" +
        "        for (int j = start; j < end; j++) {\n" +
        "            dot += values[j] * x[indices[j]];\n" +
        "        }\n" +
        "        y[row] = dot;\n" +
        "    }\n" +
        "}\n" +
        "__kernel void complex_spmv_csr_double(int num_rows, __global const int* ptr, __global const int* indices, __global const double2* values, __global const double2* x, __global double2* y) {\n" +
        "    int row = get_global_id(0);\n" +
        "    if (row < num_rows) {\n" +
        "        double2 dot = (double2)(0.0, 0.0);\n" +
        "        int start = ptr[row];\n" +
        "        int end = ptr[row+1];\n" +
        "        for (int j = start; j < end; j++) {\n" +
        "            double2 av = values[j];\n" +
        "            double2 xv = x[indices[j]];\n" +
        "            dot.x += av.x * xv.x - av.y * xv.y;\n" +
        "            dot.y += av.x * xv.y + av.y * xv.x;\n" +
        "        }\n" +
        "        y[row] = dot;\n" +
        "    }\n" +
        "}\n";
}
