----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IPcodes
-- Project Name :  Homade V1.4
-- Revision :      
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  IPcodes : A package of IPcodes for the Homade stack processor
-- 
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  juin 2012) jean-luc.dekeyser@lifl.fr
-- Wissem Chouchene ( revision 001,  Ocotbre 2012) wissem.chouchene@inria.fr
-- 
-- 
-- Cecil Licence:
-- This software is a computer program whose purpose is to Implement the
-- Homade processor on Xilinx FPGA systems.
-- 
-- This software is governed by the CeCILL license under French law and
-- abiding by the rules of distribution of free software.  You can  use,
-- modify and/ or redistribute the software under the terms of the CeCILL
-- license as circulated by CEA, CNRS and INRIA at the following URL
-- "http://www.cecill.info".
-- 
-- As a counterpart to the access to the source code and  rights to copy,
-- modify and redistribute granted by the license, users are provided only
-- with a limited warranty  and the software's author,  the holder of the
-- economic rights,  and the successive licensors  have only  limited
-- liability.
--                                                                                          
-- In this respect, the user's attention is drawn to the risks associated
-- with loading,  using,  modifying and/or developing or reproducing the
-- software by the user in light of its specific status of free software,
-- that may mean  that it is complicated to manipulate,  and  that  also
-- therefore means  that it is reserved for developers  and  experienced
-- professionals having in-depth computer knowledge. Users are therefore
-- encouraged to load and test the software's suitability as regards their                                                                           
-- requirements in conditions enabling the security of their systems and/or
-- data to be ensured and,  more generally, to use and operate it in the
-- same conditions as regards security.
-- 
-- The fact that you are presently reading this means that you have had
-- knowledge of the CeCILL license and that you accept its terms.   
----------------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

package IPcodes is          
  subtype code is std_logic_vector(10 downto 0);
  subtype ip_instr  	  is std_logic ;

--
-- code "11111111111" is forbidden!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
--IP custom
constant IPRAND	: code:= "00000010000";
-- IP NOP  0

constant IPNop 			: code := "00000000000";  -- x x  nop pop1 ... push3

-- IP system 1
constant IPTic 			: code := "00000000001";  -- 0 1 

-- IP I/O 2 to 4

constant IPBufOut 		: code := "00000000010";  -- 1/0 0
constant IPLed 			: code := "00000000011";   -- 1/0 1 
constant IPSwitch 		: code := "00000000100";	-- 0 1 

-- IP identity   5

constant IPidentity  	: code := "00000000101";	-- x y  manip sur les sommets de piles

-- dta stack T to R, R to T 6 et 7
constant IPDataStack		: std_logic_vector (9 downto 0) :="0000000011";
constant IDataPush		: std_logic_vector (0 downto 0) :="1";
constant IDataPop		: std_logic_vector (0 downto 0) :="0";
constant IPDataPush 		: code := IPDataStack & IDataPush;	-- 1/0 0
constant IPDataPop 		: code := IPDataStack & IDataPop;	-- 0 1 






--  IP stack operations   Unit  8 to 15
constant IPStack		: std_logic_vector (7 downto 0) :="00000001";
constant IDup 			: std_logic_vector (2 downto 0) := "000";  -- 1 2
constant ISwap 		: std_logic_vector (2 downto 0) := "001";	-- 2 2 
constant IDrop 		: std_logic_vector (2 downto 0) := "000";  --same as IPNop with 1 0
constant ITuck			: std_logic_vector (2 downto 0) := "010";  -- 2 3
constant IOver 		: std_logic_vector (2 downto 0) := "011";  -- 2 3 
constant IRot 			: std_logic_vector (2 downto 0) := "100";  -- 3 3 
constant IInvRot 		: std_logic_vector (2 downto 0) := "101";  -- 3 3
constant INip 			: std_logic_vector (2 downto 0) := "110";  -- 2 1
constant IPDup 			: code := IPStack & IDup;  -- 1 2
constant IPSwap 			: code := IPStack & ISwap;	-- 2 2 
constant IPDrop 			: code := IPStack & IDrop;  --same as IPNop with 1 0
constant IPTuck			: code := IPStack & ITuck;  -- 2 3
constant IPOver 			: code := IPStack & IOver;  -- 2 3 
constant IPRot 			: code := IPStack & IRot;  -- 3 3 
constant IPInvRot 		: code := IPStack & IInvRot;  -- 3 3
constant IPNip 			: code := IPStack & INip;  -- 2 1
--constant IPRot_Drop 		: code := "00000001111";



-- IPunit  from 32 to 63
constant IPFunit		: std_logic_vector (5 downto 0) :="000001";

constant IPAdd 			: code := IPFunit & "00000";  --2 1
constant IPMinus 			: code := IPFunit & "00001";  -- 2 1
constant IPInc 			: code := IPFunit & "00010";  -- 1 1
constant IPDec 			: code := IPFunit & "00011";  -- 1 1
constant IPNot 			: code := IPFunit & "00100";  -- 1 1
constant IPAnd 			: code := IPFunit & "00101";  -- 2 1
constant IPOr 				: code := IPFunit & "00110";  -- 2 1
constant IPXor 			: code := IPFunit & "00111";  -- 2 1
constant IPMul2 			: code := IPFunit & "01000";  -- 1 1
constant IPDiv2U 			: code := IPFunit & "01001";  -- 1 1
constant IPDiv2 			: code := IPFunit & "01010";  -- 1 1
constant IPRshift 		: code := IPFunit & "01011";  -- 2 1
constant IPLshift 		: code := IPFunit & "01100";  -- 2 1
constant IPMpp 			: code := IPFunit & "01101";  --  ? ? 
constant IPShldc 			: code := IPFunit & "01110";  --  ? ? 
constant IPcomp2 			: code := IPFunit & "01111";  --  1 1 
constant IPTrue 			: code := IPFunit & "10000";  -- 0 1
constant IPFalse 			: code := IPFunit & "10001";  -- 0 1
constant IPEqZero 		: code := IPFunit & "10010";  -- 1 1
constant IPNeg				: code := IPFunit & "10011";  -- 1 1
constant IPGtU 			: code := IPFunit & "10100";  -- 2 1
constant IPLtU 			: code := IPFunit & "10101";  -- 2 1
constant IPEq 				: code := IPFunit & "10110";  -- 2 1
constant IPGeU 			: code := IPFunit & "10111";  -- 2 1
constant IPLeU 			: code := IPFunit & "11000";  -- 2 1
constant IPNe 				: code := IPFunit & "11001";  -- 2 1
constant IPGt 				: code := IPFunit & "11010";  -- 2 1
constant IPLt 				: code := IPFunit & "11011";  -- 2 1
constant IPLe 				: code := IPFunit & "11100";  -- 2 1
constant IPGe 				: code := IPFunit & "11101";  -- 2 1
constant IPShiftLit 		: code := IPFunit & "11110";  -- 2 1 -- Literal : shitf 12 left  the top and fill the 12  first bit with 12 bit of subtop 

---dispo  in funit 11111 01111  for 3  function to develop

-- 
constant IPRegFile		: std_logic_vector (9 downto 0) :="0000001000";
constant IRegLd		: std_logic_vector (0 downto 0) :="1";
constant IRegSt		: std_logic_vector (0 downto 0) :="0";
constant IPRegLd 		: code := IPDataStack & IRegLd;	-- 1 1
constant IPRegSt 		: code := IPDataStack & IRegSt;	-- 1/2 0
constant IPCom 		: code := "00111111111";	--  0
constant IPME			: std_logic_vector (9 downto 0) :="0000001001";
constant IPM2E			: code := IPME & "1";
constant IPE2M			: code := IPME & "0";
constant IPactif		: code := "00111111110";	--  0
constant IPCom32 		: code := "10111111111";	--  long
-- salves
constant IPGet  : code := "01000000000";
constant IPPut  : code := "01000000001";
constant IPSnum  : code := "01000000011";

--  long IP

constant IPWait 		   : code := "10000000001";  -- 1 0
constant IPWaitBtn 		: code := "10000000010";   -- 1 0

constant IPfibo 		: code := "10000000011";   -- 1 0





  
end IPcodes;
