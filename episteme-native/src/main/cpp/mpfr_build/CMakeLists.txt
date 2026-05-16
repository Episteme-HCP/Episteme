cmake_minimum_required(VERSION 3.10)
project(libmpfr LANGUAGES C)

# MPFR Source Dir
set(MPFR_SRC "../.././../../../mpfr-4.2.2/src")

include_directories(. ${MPFR_SRC})

# Configuration macros
add_definitions(-DHAVE_CONFIG_H -D_CRT_SECURE_NO_WARNINGS)

if(UNIX)
    add_definitions(-DHAVE_STDARG=1 -UOS_WINDOWS)
endif()

# List of source files based on Makefile.am
set(MPFR_SOURCES
    ${MPFR_SRC}/exceptions.c
    ${MPFR_SRC}/extract.c
    ${MPFR_SRC}/uceil_exp2.c
    ${MPFR_SRC}/uceil_log2.c
    ${MPFR_SRC}/ufloor_log2.c
    ${MPFR_SRC}/add.c
    ${MPFR_SRC}/add1.c
    ${MPFR_SRC}/add_ui.c
    ${MPFR_SRC}/agm.c
    ${MPFR_SRC}/clear.c
    ${MPFR_SRC}/cmp.c
    ${MPFR_SRC}/cmpabs.c
    ${MPFR_SRC}/cmp_si.c
    ${MPFR_SRC}/cmp_ui.c
    ${MPFR_SRC}/comparisons.c
    ${MPFR_SRC}/div_2exp.c
    ${MPFR_SRC}/div_2si.c
    ${MPFR_SRC}/div_2ui.c
    ${MPFR_SRC}/div.c
    ${MPFR_SRC}/div_ui.c
    ${MPFR_SRC}/dot.c
    ${MPFR_SRC}/dump.c
    ${MPFR_SRC}/eq.c
    ${MPFR_SRC}/exp10.c
    ${MPFR_SRC}/exp2.c
    ${MPFR_SRC}/exp3.c
    ${MPFR_SRC}/exp.c
    ${MPFR_SRC}/frac.c
    ${MPFR_SRC}/frexp.c
    ${MPFR_SRC}/get_d.c
    ${MPFR_SRC}/get_exp.c
    ${MPFR_SRC}/get_str.c
    ${MPFR_SRC}/init.c
    ${MPFR_SRC}/inp_str.c
    ${MPFR_SRC}/isinteger.c
    ${MPFR_SRC}/isinf.c
    ${MPFR_SRC}/isnan.c
    ${MPFR_SRC}/isnum.c
    ${MPFR_SRC}/const_log2.c
    ${MPFR_SRC}/log.c
    ${MPFR_SRC}/modf.c
    ${MPFR_SRC}/mul_2exp.c
    ${MPFR_SRC}/mul_2si.c
    ${MPFR_SRC}/mul_2ui.c
    ${MPFR_SRC}/mul.c
    ${MPFR_SRC}/mul_ui.c
    ${MPFR_SRC}/neg.c
    ${MPFR_SRC}/next.c
    ${MPFR_SRC}/out_str.c
    ${MPFR_SRC}/printf.c
    ${MPFR_SRC}/vasprintf.c
    ${MPFR_SRC}/const_pi.c
    ${MPFR_SRC}/pow.c
    ${MPFR_SRC}/pow_si.c
    ${MPFR_SRC}/pow_ui.c
    ${MPFR_SRC}/pow_sj.c
    ${MPFR_SRC}/pow_uj.c
    ${MPFR_SRC}/print_raw.c
    ${MPFR_SRC}/print_rnd_mode.c
    ${MPFR_SRC}/reldiff.c
    ${MPFR_SRC}/round_prec.c
    ${MPFR_SRC}/set.c
    ${MPFR_SRC}/setmax.c
    ${MPFR_SRC}/setmin.c
    ${MPFR_SRC}/set_d.c
    ${MPFR_SRC}/set_dfl_prec.c
    ${MPFR_SRC}/set_exp.c
    ${MPFR_SRC}/set_rnd.c
    ${MPFR_SRC}/set_f.c
    ${MPFR_SRC}/set_prc_raw.c
    ${MPFR_SRC}/set_prec.c
    ${MPFR_SRC}/set_q.c
    ${MPFR_SRC}/set_si.c
    ${MPFR_SRC}/set_str.c
    ${MPFR_SRC}/set_str_raw.c
    ${MPFR_SRC}/set_ui.c
    ${MPFR_SRC}/set_z.c
    ${MPFR_SRC}/sqrt.c
    ${MPFR_SRC}/sqrt_ui.c
    ${MPFR_SRC}/sub.c
    ${MPFR_SRC}/sub1.c
    ${MPFR_SRC}/sub_ui.c
    ${MPFR_SRC}/rint.c
    ${MPFR_SRC}/ui_div.c
    ${MPFR_SRC}/ui_sub.c
    ${MPFR_SRC}/urandom.c
    ${MPFR_SRC}/urandomb.c
    ${MPFR_SRC}/get_z_2exp.c
    ${MPFR_SRC}/swap.c
    ${MPFR_SRC}/factorial.c
    ${MPFR_SRC}/cosh.c
    ${MPFR_SRC}/sinh.c
    ${MPFR_SRC}/tanh.c
    ${MPFR_SRC}/sinh_cosh.c
    ${MPFR_SRC}/acosh.c
    ${MPFR_SRC}/asinh.c
    ${MPFR_SRC}/atanh.c
    ${MPFR_SRC}/atan.c
    ${MPFR_SRC}/cmp2.c
    ${MPFR_SRC}/exp_2.c
    ${MPFR_SRC}/asin.c
    ${MPFR_SRC}/const_euler.c
    ${MPFR_SRC}/cos.c
    ${MPFR_SRC}/sin.c
    ${MPFR_SRC}/tan.c
    ${MPFR_SRC}/fma.c
    ${MPFR_SRC}/fms.c
    ${MPFR_SRC}/hypot.c
    ${MPFR_SRC}/log1p.c
    ${MPFR_SRC}/expm1.c
    ${MPFR_SRC}/log2.c
    ${MPFR_SRC}/log2p1.c
    ${MPFR_SRC}/log10.c
    ${MPFR_SRC}/log10p1.c
    ${MPFR_SRC}/ui_pow.c
    ${MPFR_SRC}/ui_pow_ui.c
    ${MPFR_SRC}/minmax.c
    ${MPFR_SRC}/dim.c
    ${MPFR_SRC}/signbit.c
    ${MPFR_SRC}/copysign.c
    ${MPFR_SRC}/setsign.c
    ${MPFR_SRC}/gmp_op.c
    ${MPFR_SRC}/init2.c
    ${MPFR_SRC}/acos.c
    ${MPFR_SRC}/sin_cos.c
    ${MPFR_SRC}/set_nan.c
    ${MPFR_SRC}/set_inf.c
    ${MPFR_SRC}/set_zero.c
    ${MPFR_SRC}/powerof2.c
    ${MPFR_SRC}/gamma.c
    ${MPFR_SRC}/set_ld.c
    ${MPFR_SRC}/get_ld.c
    ${MPFR_SRC}/cbrt.c
    ${MPFR_SRC}/volatile.c
    ${MPFR_SRC}/fits_sshort.c
    ${MPFR_SRC}/fits_sint.c
    ${MPFR_SRC}/fits_slong.c
    ${MPFR_SRC}/fits_ushort.c
    ${MPFR_SRC}/fits_uint.c
    ${MPFR_SRC}/fits_ulong.c
    ${MPFR_SRC}/fits_uintmax.c
    ${MPFR_SRC}/fits_intmax.c
    ${MPFR_SRC}/get_si.c
    ${MPFR_SRC}/get_ui.c
    ${MPFR_SRC}/zeta.c
    ${MPFR_SRC}/cmp_d.c
    ${MPFR_SRC}/erf.c
    ${MPFR_SRC}/inits.c
    ${MPFR_SRC}/inits2.c
    ${MPFR_SRC}/clears.c
    ${MPFR_SRC}/sgn.c
    ${MPFR_SRC}/check.c
    ${MPFR_SRC}/sub1sp.c
    ${MPFR_SRC}/version.c
    ${MPFR_SRC}/mpn_exp.c
    ${MPFR_SRC}/mpfr-gmp.c
    ${MPFR_SRC}/mp_clz_tab.c
    ${MPFR_SRC}/sum.c
    ${MPFR_SRC}/add1sp.c
    ${MPFR_SRC}/free_cache.c
    ${MPFR_SRC}/si_op.c
    ${MPFR_SRC}/cmp_ld.c
    ${MPFR_SRC}/set_ui_2exp.c
    ${MPFR_SRC}/set_si_2exp.c
    ${MPFR_SRC}/set_uj.c
    ${MPFR_SRC}/set_sj.c
    ${MPFR_SRC}/get_sj.c
    ${MPFR_SRC}/get_uj.c
    ${MPFR_SRC}/get_z.c
    ${MPFR_SRC}/iszero.c
    ${MPFR_SRC}/cache.c
    ${MPFR_SRC}/sqr.c
    ${MPFR_SRC}/int_ceil_log2.c
    ${MPFR_SRC}/isqrt.c
    ${MPFR_SRC}/strtofr.c
    ${MPFR_SRC}/pow_z.c
    ${MPFR_SRC}/logging.c
    ${MPFR_SRC}/mulders.c
    ${MPFR_SRC}/get_f.c
    ${MPFR_SRC}/round_p.c
    ${MPFR_SRC}/erfc.c
    ${MPFR_SRC}/atan2.c
    ${MPFR_SRC}/atan2u.c
    ${MPFR_SRC}/subnormal.c
    ${MPFR_SRC}/const_catalan.c
    ${MPFR_SRC}/root.c
    ${MPFR_SRC}/sec.c
    ${MPFR_SRC}/csc.c
    ${MPFR_SRC}/cot.c
    ${MPFR_SRC}/eint.c
    ${MPFR_SRC}/sech.c
    ${MPFR_SRC}/csch.c
    ${MPFR_SRC}/coth.c
    ${MPFR_SRC}/round_near_x.c
    ${MPFR_SRC}/constant.c
    ${MPFR_SRC}/abort_prec_max.c
    ${MPFR_SRC}/stack_interface.c
    ${MPFR_SRC}/lngamma.c
    ${MPFR_SRC}/zeta_ui.c
    ${MPFR_SRC}/set_d64.c
    ${MPFR_SRC}/get_d64.c
    ${MPFR_SRC}/jn.c
    ${MPFR_SRC}/yn.c
    ${MPFR_SRC}/rem1.c
    ${MPFR_SRC}/add_d.c
    ${MPFR_SRC}/sub_d.c
    ${MPFR_SRC}/d_sub.c
    ${MPFR_SRC}/mul_d.c
    ${MPFR_SRC}/div_d.c
    ${MPFR_SRC}/d_div.c
    ${MPFR_SRC}/li2.c
    ${MPFR_SRC}/rec_sqrt.c
    ${MPFR_SRC}/min_prec.c
    ${MPFR_SRC}/buildopt.c
    ${MPFR_SRC}/digamma.c
    ${MPFR_SRC}/bernoulli.c
    ${MPFR_SRC}/isregular.c
    ${MPFR_SRC}/set_flt.c
    ${MPFR_SRC}/get_flt.c
    ${MPFR_SRC}/scale2.c
    ${MPFR_SRC}/set_z_2exp.c
    ${MPFR_SRC}/ai.c
    ${MPFR_SRC}/gammaonethird.c
    ${MPFR_SRC}/grandom.c
    ${MPFR_SRC}/fpif.c
    ${MPFR_SRC}/set_float128.c
    ${MPFR_SRC}/get_float128.c
    ${MPFR_SRC}/rndna.c
    ${MPFR_SRC}/nrandom.c
    ${MPFR_SRC}/random_deviate.c
    ${MPFR_SRC}/erandom.c
    ${MPFR_SRC}/mpfr-mini-gmp.c
    ${MPFR_SRC}/fmma.c
    ${MPFR_SRC}/log_ui.c
    ${MPFR_SRC}/gamma_inc.c
    ${MPFR_SRC}/ubf.c
    ${MPFR_SRC}/beta.c
    ${MPFR_SRC}/odd_p.c
    ${MPFR_SRC}/get_q.c
    ${MPFR_SRC}/pool.c
    ${MPFR_SRC}/total_order.c
    ${MPFR_SRC}/set_d128.c
    ${MPFR_SRC}/get_d128.c
    ${MPFR_SRC}/nbits_ulong.c
    ${MPFR_SRC}/cmpabs_ui.c
    ${MPFR_SRC}/sinu.c
    ${MPFR_SRC}/cosu.c
    ${MPFR_SRC}/tanu.c
    ${MPFR_SRC}/fmod_ui.c
    ${MPFR_SRC}/acosu.c
    ${MPFR_SRC}/asinu.c
    ${MPFR_SRC}/atanu.c
    ${MPFR_SRC}/compound.c
    ${MPFR_SRC}/exp2m1.c
    ${MPFR_SRC}/exp10m1.c
    ${MPFR_SRC}/powr.c
    # mini-gmp
    mini-gmp.c
    # Exports
)

if(WIN32)
    list(APPEND MPFR_SOURCES ${MPFR_SRC}/mpfr.def)
endif()

if(WIN32)
    set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)
endif()

add_library(mpfr SHARED ${MPFR_SOURCES})

if(WIN32)
    set_target_properties(mpfr PROPERTIES
        PREFIX ""
        SUFFIX ".dll"
    )
else()
    set_target_properties(mpfr PROPERTIES
        PREFIX "lib"
        SUFFIX ".so"
    )
endif()

if(WIN32)
    install(TARGETS mpfr DESTINATION ${CMAKE_SOURCE_DIR}/../../../../libs)
else()
    install(TARGETS mpfr DESTINATION ${CMAKE_SOURCE_DIR}/../../../../libs)
endif()
