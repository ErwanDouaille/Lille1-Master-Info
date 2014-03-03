                    Core name: Xilinx LogiCORE Distributed Memory Generator 
                    Version: 5.1
                    Release Date: April 19, 2010


================================================================================

This document contains the following sections: 

1. Introduction
2. New Features
3. Supported Devices
4. Resolved Issues
5. Known Issues 
6. Technical Support
7. Core Release History
8. Legal Disclaimer

================================================================================

1. INTRODUCTION

For the most recent updates to the IP installation instructions for this core,
please go to:

   http://www.xilinx.com/ipcenter/coregen/ip_update_install_instructions.htm

 
For system requirements:

   http://www.xilinx.com/ipcenter/coregen/ip_update_system_requirements.htm 


This file contains release notes for the Xilinx LogiCORE IP Distributed Memory Generator v5.1
solution. For the latest core updates, see the product page at:
 
   http://www.xilinx.com/products/ipcenter/DIST_MEM_GEN.htm


2. NEW FEATURES  
 
   - ISE 12.1 software support

3. SUPPORTED DEVICES
 
   - SPARTAN-3, SPARTAN-3 XA, SPARTAN-3E SPARTAN-3E XA, SPARTAN-3A, SPARTAN-3A XA, SPARTAN-3ADSP, SPARTAN-3ADSP XA
   - SPARTAN-6, SPARTAN-6 XA, SPARTAN-6L and QSPARTAN-6
   - VIRTEX-4
   - VIRTEX-5 and QVIRTEX-5
   - VIRTEX-6, VIRTEX-6L and QVIRTEX-6
 
4. RESOLVED ISSUES 

   - In Distributed Memory Generator core, a function is repeatedly called within a 
     for loop causing the core generation to hang for the depth of 65536
     - Version fixed: v5.1
     - CR  522557

5. KNOWN ISSUES 

     The following are known issues for v4.3 of this core at time of release:
   
   - When a large Distributed Memory Generator IP is generated, CORE
     Generator runs out of memory and fails to generate.
     - CR  431917

 
   The most recent information, including known issues, workarounds, and
   resolutions for this version is provided in the IP Release Notes User Guide
   located at 

   www.xilinx.com/support/documentation/user_guides/xtp025.pdf


6. TECHNICAL SUPPORT 

   To obtain technical support, create a WebCase at www.xilinx.com/support.
   Questions are routed to a team with expertise using this product.  
     
   Xilinx provides technical support for use of this product when used
   according to the guidelines described in the core documentation, and
   cannot guarantee timing, functionality, or support of this product for
   designs that do not follow specified guidelines.

7. CORE RELEASE HISTORY 

Date           By            Version      Description
================================================================================
04/19/2010    Xilinx, Inc.   5.1          ISE 12.1 support
12/02/2009    Xilinx, Inc.   4.3          ISE 11.4 support; Spartan-6 Lower Power and Automotive Spartan-6 device support
09/16/2009    Xilinx, Inc.   4.2          11.3 support; Virtex-6 Lower Power and Virtex-6 HXT device support
06/24/2009    Xilinx, Inc.   4.1.1        11.2 support; Virtex-6 CXT device support
04/24/2009    Xilinx, Inc.   4.1          11.1 support; Revised to v4.1; Virtex-6 and Spartan-6 support
03/24/2008    Xilinx, Inc.   3.4          10.1 support; Revised to v3.4.
04/02/2007    Xilinx, Inc.   3.3          9.1i support; Revised to v3.3; Spartan-3AN and Spartan-3A DSP support
09/21/2006    Xilinx, Inc.   3.2          8.2i support; Revised to v3.2; Spartan-3A support
07/13/2006    Xilinx, Inc.   3.1          8.2i support; Revised to v3.1
01/18/2006    Xilinx, Inc.   2.1          8.1i support; Revised to v2.1
04/28/2005    Xilinx, Inc.   1.1          7.1i Service Pack 1 support; First release
================================================================================

8. Legal Disclaimer

 (c) Copyright 2002 - 2010 Xilinx, Inc. All rights reserved.
 
 This file contains confidential and proprietary information
 of Xilinx, Inc. and is protected under U.S. and
 international copyright and other intellectual property
 laws.
 
 DISCLAIMER
 This disclaimer is not a license and does not grant any
 rights to the materials distributed herewith. Except as
 otherwise provided in a valid license issued to you by
 Xilinx, and to the maximum extent permitted by applicable
 law: (1) THESE MATERIALS ARE MADE AVAILABLE "AS IS" AND
 WITH ALL FAULTS, AND XILINX HEREBY DISCLAIMS ALL WARRANTIES
 AND CONDITIONS, EXPRESS, IMPLIED, OR STATUTORY, INCLUDING
 BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY, NON-
 INFRINGEMENT, OR FITNESS FOR ANY PARTICULAR PURPOSE; and
 (2) Xilinx shall not be liable (whether in contract or tort,
 including negligence, or under any other theory of
 liability) for any loss or damage of any kind or nature
 related to, arising under or in connection with these
 materials, including for any direct, or any indirect,
 special, incidental, or consequential loss or damage
 (including loss of data, profits, goodwill, or any type of
 loss or damage suffered as a result of any action brought
 by a third party) even if such damage or loss was
 reasonably foreseeable or Xilinx had been advised of the
 possibility of the same.
 
 CRITICAL APPLICATIONS
 Xilinx products are not designed or intended to be fail-
 safe, or for use in any application requiring fail-safe
 performance, such as life-support or safety devices or
 systems, Class III medical devices, nuclear facilities,
 applications related to the deployment of airbags, or any
 other applications that could lead to death, personal
 injury, or severe property or environmental damage
 (individually and collectively, "Critical
 Applications"). Customer assumes the sole risk and
 liability of any use of Xilinx products in Critical
 Applications, subject only to applicable laws and
 regulations governing limitations on product liability.
 
 THIS COPYRIGHT NOTICE AND DISCLAIMER MUST BE RETAINED AS
 PART OF THIS FILE AT ALL TIMES.
