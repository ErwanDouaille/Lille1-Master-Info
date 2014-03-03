/**********************************************************************/
/*   ____  ____                                                       */
/*  /   /\/   /                                                       */
/* /___/  \  /                                                        */
/* \   \   \/                                                       */
/*  \   \        Copyright (c) 2003-2009 Xilinx, Inc.                */
/*  /   /          All Right Reserved.                                 */
/* /---/   /\                                                         */
/* \   \  /  \                                                      */
/*  \___\/\___\                                                    */
/***********************************************************************/

/* This file is designed for use with ISim build 0x141a37e9 */

#define XSI_HIDE_SYMBOL_SPEC true
#include "xsi.h"
#include <memory.h>
#ifdef __GNUC__
#include <stdlib.h>
#else
#include <malloc.h>
#define alloca _alloca
#endif
static const char *ng0 = "/home/m1/douaille/didact_sc/diviseur_clk.vhd";
extern char *IEEE_P_2592010699;

unsigned char ieee_p_2592010699_sub_1690584930_503743352(char *, unsigned char );


static void work_a_4231643051_3212880686_p_0(char *t0)
{
    unsigned char t1;
    char *t2;
    unsigned char t3;
    char *t4;
    char *t5;
    unsigned char t6;
    unsigned char t7;
    char *t8;
    int t9;
    unsigned char t10;
    char *t11;
    unsigned char t12;
    unsigned char t13;
    char *t14;
    char *t15;
    char *t16;
    char *t17;
    int t18;

LAB0:    xsi_set_current_line(52, ng0);
    t2 = (t0 + 568U);
    t3 = xsi_signal_has_event(t2);
    if (t3 == 1)
        goto LAB5;

LAB6:    t1 = (unsigned char)0;

LAB7:    if (t1 != 0)
        goto LAB2;

LAB4:
LAB3:    t2 = (t0 + 2248);
    *((int *)t2) = 1;

LAB1:    return;
LAB2:    xsi_set_current_line(55, ng0);
    t4 = (t0 + 960U);
    t8 = *((char **)t4);
    t9 = *((int *)t8);
    t10 = (t9 >= 4000000);
    if (t10 != 0)
        goto LAB8;

LAB10:    xsi_set_current_line(59, ng0);
    t2 = (t0 + 1236U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2292);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast(t2);
    xsi_set_current_line(60, ng0);
    t2 = (t0 + 960U);
    t4 = *((char **)t2);
    t9 = *((int *)t4);
    t18 = (t9 + 1);
    t2 = (t0 + 2328);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((int *)t14) = t18;
    xsi_driver_first_trans_fast(t2);

LAB9:    xsi_set_current_line(62, ng0);
    t2 = (t0 + 1236U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2364);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast_port(t2);
    xsi_set_current_line(65, ng0);
    t2 = (t0 + 1052U);
    t4 = *((char **)t2);
    t9 = *((int *)t4);
    t1 = (t9 >= 500000);
    if (t1 != 0)
        goto LAB11;

LAB13:    xsi_set_current_line(69, ng0);
    t2 = (t0 + 1328U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2400);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast(t2);
    xsi_set_current_line(70, ng0);
    t2 = (t0 + 1052U);
    t4 = *((char **)t2);
    t9 = *((int *)t4);
    t18 = (t9 + 1);
    t2 = (t0 + 2436);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((int *)t14) = t18;
    xsi_driver_first_trans_fast(t2);

LAB12:    xsi_set_current_line(72, ng0);
    t2 = (t0 + 1328U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2472);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast_port(t2);
    xsi_set_current_line(75, ng0);
    t2 = (t0 + 1144U);
    t4 = *((char **)t2);
    t9 = *((int *)t4);
    t1 = (t9 >= 4000);
    if (t1 != 0)
        goto LAB14;

LAB16:    xsi_set_current_line(79, ng0);
    t2 = (t0 + 1420U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2508);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast(t2);
    xsi_set_current_line(80, ng0);
    t2 = (t0 + 1144U);
    t4 = *((char **)t2);
    t9 = *((int *)t4);
    t18 = (t9 + 1);
    t2 = (t0 + 2544);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((int *)t14) = t18;
    xsi_driver_first_trans_fast(t2);

LAB15:    xsi_set_current_line(82, ng0);
    t2 = (t0 + 1420U);
    t4 = *((char **)t2);
    t1 = *((unsigned char *)t4);
    t2 = (t0 + 2580);
    t5 = (t2 + 32U);
    t8 = *((char **)t5);
    t11 = (t8 + 32U);
    t14 = *((char **)t11);
    *((unsigned char *)t14) = t1;
    xsi_driver_first_trans_fast_port(t2);
    goto LAB3;

LAB5:    t4 = (t0 + 592U);
    t5 = *((char **)t4);
    t6 = *((unsigned char *)t5);
    t7 = (t6 == (unsigned char)3);
    t1 = t7;
    goto LAB7;

LAB8:    xsi_set_current_line(56, ng0);
    t4 = (t0 + 1236U);
    t11 = *((char **)t4);
    t12 = *((unsigned char *)t11);
    t13 = ieee_p_2592010699_sub_1690584930_503743352(IEEE_P_2592010699, t12);
    t4 = (t0 + 2292);
    t14 = (t4 + 32U);
    t15 = *((char **)t14);
    t16 = (t15 + 32U);
    t17 = *((char **)t16);
    *((unsigned char *)t17) = t13;
    xsi_driver_first_trans_fast(t4);
    xsi_set_current_line(57, ng0);
    t2 = (t0 + 2328);
    t4 = (t2 + 32U);
    t5 = *((char **)t4);
    t8 = (t5 + 32U);
    t11 = *((char **)t8);
    *((int *)t11) = 0;
    xsi_driver_first_trans_fast(t2);
    goto LAB9;

LAB11:    xsi_set_current_line(66, ng0);
    t2 = (t0 + 1328U);
    t5 = *((char **)t2);
    t3 = *((unsigned char *)t5);
    t6 = ieee_p_2592010699_sub_1690584930_503743352(IEEE_P_2592010699, t3);
    t2 = (t0 + 2400);
    t8 = (t2 + 32U);
    t11 = *((char **)t8);
    t14 = (t11 + 32U);
    t15 = *((char **)t14);
    *((unsigned char *)t15) = t6;
    xsi_driver_first_trans_fast(t2);
    xsi_set_current_line(67, ng0);
    t2 = (t0 + 2436);
    t4 = (t2 + 32U);
    t5 = *((char **)t4);
    t8 = (t5 + 32U);
    t11 = *((char **)t8);
    *((int *)t11) = 0;
    xsi_driver_first_trans_fast(t2);
    goto LAB12;

LAB14:    xsi_set_current_line(76, ng0);
    t2 = (t0 + 1420U);
    t5 = *((char **)t2);
    t3 = *((unsigned char *)t5);
    t6 = ieee_p_2592010699_sub_1690584930_503743352(IEEE_P_2592010699, t3);
    t2 = (t0 + 2508);
    t8 = (t2 + 32U);
    t11 = *((char **)t8);
    t14 = (t11 + 32U);
    t15 = *((char **)t14);
    *((unsigned char *)t15) = t6;
    xsi_driver_first_trans_fast(t2);
    xsi_set_current_line(77, ng0);
    t2 = (t0 + 2544);
    t4 = (t2 + 32U);
    t5 = *((char **)t4);
    t8 = (t5 + 32U);
    t11 = *((char **)t8);
    *((int *)t11) = 0;
    xsi_driver_first_trans_fast(t2);
    goto LAB15;

}


extern void work_a_4231643051_3212880686_init()
{
	static char *pe[] = {(void *)work_a_4231643051_3212880686_p_0};
	xsi_register_didat("work_a_4231643051_3212880686", "isim/didact_top_didact_top_sch_tb_isim_beh.exe.sim/work/a_4231643051_3212880686.didat");
	xsi_register_executes(pe);
}
