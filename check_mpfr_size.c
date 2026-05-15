#include <stdio.h>
#include <mpfr.h>

int main() {
    printf("sizeof(mpfr_t) = %zu\n", sizeof(mpfr_t));
    printf("sizeof(mpfr_ptr) = %zu\n", sizeof(mpfr_ptr));
    printf("sizeof(mp_prec_t) = %zu\n", sizeof(mp_prec_t));
    printf("sizeof(mpfr_sign_t) = %zu\n", sizeof(mpfr_sign_t));
    printf("sizeof(mpfr_exp_t) = %zu\n", sizeof(mpfr_exp_t));
    return 0;
}
