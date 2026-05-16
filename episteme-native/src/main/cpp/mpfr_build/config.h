/* config.h.  Generated manually for MSVC + mini-gmp build. */

#ifndef MPFR_CONFIG_H
#define MPFR_CONFIG_H

#define HAVE_STDINT_H 1
#define HAVE_INTTYPES_H 1
#define HAVE_LIMITS_H 1
#define HAVE_FLOAT_H 1
#define HAVE_STRING_H 1
#define HAVE_LOCALE_H 1
#define HAVE_WCHAR_H 1
#define HAVE_SIGNAL_H 1
#define HAVE_SETLOCALE 1
#define HAVE_GETTIMEOFDAY 0
#define HAVE_DENORMAL_DOUBLE 1
#define HAVE_DENORMAL_FLOAT 1

/* Use mini-gmp */
#define MPFR_USE_MINI_GMP 1
#define MINI_GMP_LIMB_TYPE long long
#define GMP_NUMB_BITS 64
#define GMP_NAIL_BITS 0

#define _MPFR_PREC_FORMAT 3
#define _MPFR_EXP_FORMAT 3

/* Endianness */
#define HAVE_LITTLE_ENDIAN 1

/* Tune case */
#define MPFR_TUNE_CASE "generic"

/* Windows specific */
#define OS_WINDOWS 1
#define __MPFR_WITHIN_MPFR 1

/* Version */
#define MPFR_VERSION_STRING "4.2.2"
#define MPFR_VERSION_MAJOR 4
#define MPFR_VERSION_MINOR 2
#define MPFR_VERSION_PATCHLEVEL 2

/* Features */
#define HAVE_LDOUBLE_IEEE_EXT_LITTLE 1
#define HAVE_DOUBLE_IEEE_LITTLE 1

#endif

