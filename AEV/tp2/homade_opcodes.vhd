----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : opcodes
-- Project Name :  Homade V1.4
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  opcodes : A package of opcodes for the Homade stack processor
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

package opcodes is
  subtype branch is std_logic_vector(5 downto 0);
  subtype ip_instr  	  is std_logic ;
  subtype ip_push_pop is std_logic_vector(1 downto 0);
  subtype SPMD_instr is std_logic_vector(2 downto 0);
  subtype LITTYPE is std_logic_vector(3 downto 0);
  subtype instruction is std_logic_vector(15 downto 0);
  
    --   NILL  code IP 1111111111 not allowed
  constant NILL: 			instruction := x"FFFF";

  -- BRANCH instructions
  constant BRANCH_RELATIVE: 			branch := "000000";			-- Branch to relative addr (last 10 bits of the instruction)
  constant BRANCH_ZERO: 				branch := "000001";			-- branch to relative addr IF Zero (last 10 bits of the instruction)
  constant BRANCH_NOT_ZERO: 			branch := "000010";			-- branch to relative addr IF NOT Zero (last 10 bits of the instruction)
  constant BRANCH_ABSOLUTE: 			instruction := "0000110000000000";			-- branch to absolute addr ( PC = next 2 instructions)
 
  -- OTHERS
	constant HLT:				instruction := "0001110000000000";	

	-- halt call
	--  constant LOCAL_END: 					branch := "000110";			-- LOCAL end of SPMD Call
 
  constant CALL: 							instruction := "0001000000000000";			-- Function CALL jump to absoulte addr , store pc in stack ( PC = next 2 instr )
  constant RET: 							instruction := "0001010000000000";			--  RETURN
  --  WIM dynamique VC
  constant WIM: 			LITTYPE := "0011";
  -- IP instructions
   constant IP: 			ip_instr := '1';										-- IP instruction
	constant IP_PUSH0:	ip_push_pop := "00";							-- IP PUSH 
	constant IP_PUSH1:	ip_push_pop := "01";							
	constant IP_PUSH2:	ip_push_pop := "10";	
	constant IP_PUSH3:	ip_push_pop := "11";	
	constant IP_POP0:		ip_push_pop := "00";							-- IP POP
	constant IP_POP1:		ip_push_pop := "01";	
	constant IP_POP2:		ip_push_pop := "10";	
	constant IP_POP3:		ip_push_pop := "11";	
  
  -- LIT instructions
	constant LIT:				LITTYPE := "0010";						-- immdediate8 push 8 bits of same instruction to fifo
--	
  -- SPMD
	constant SPMD:				SPMD_instr := "010";								-- SPMD call
	constant IWAIT:         SPMD_instr  := "011" ;							--   wait ortree
	



	constant NOP:				instruction := "1000000000000000";								-- SPMD call

  
end opcodes;

