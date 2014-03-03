----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : HSUcore
-- Project Name :  Homade V2.1
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  HSU core
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
use IEEE.STD_LOGIC_1164.ALL;

entity HSUcore is
    Port ( Instruction : in  STD_LOGIC_VECTOR (15 downto 0);
           Instr_ready : in  STD_LOGIC;
--           IPtrig : out  STD_LOGIC;
           clr : in  STD_LOGIC;
			  clk : in  STD_LOGIC;
           IPdone : in  STD_LOGIC;
--           Ortree : in  STD_LOGIC; 
           Next_inst : out  STD_LOGIC;
           IPcode : out  STD_LOGIC_VECTOR (10 downto 0);
			  offset: out std_logic;
			  twait : in std_logic;
			  SPMDtrig: out std_logic;
			  SPMDcode: out std_logic_vector(12 downto 0);			  
           Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           Nout : out  STD_LOGIC_VECTOR (31 downto 0);
           N2out : out  STD_LOGIC_VECTOR (31 downto 0);
           Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           Nin : in  STD_LOGIC_VECTOR (31 downto 0);
           N2in : in  STD_LOGIC_VECTOR (31 downto 0));
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
	
   attribute KEEP : string;
	attribute KEEP of SPMDcode : signal is "yes";
	attribute KEEP of SPMDtrig : signal is "yes";
	
end HSUcore;

architecture Behavioral of HSUcore is

signal pp1,pp2,pp3,ph1,ph2,ph3,LITld,Tld,Nld,N2ld, bool_T: std_logic;
signal Tsl,Nsl,N2sl: std_logic_vector (2 downto 0);
signal Rsl, R1sl: std_logic_vector (1 downto 0);
signal Litteral, Tout_bus : std_logic_vector (31 downto 0);
COMPONENT CUnit
	PORT(
		Instruction : IN std_logic_vector(15 downto 0);
		IPdone : IN std_logic;
		Instr_ready : IN std_logic;
--		Ortree : IN std_logic;
		clr : IN std_logic;  
		clk : IN std_logic;  		
		Next_inst : OUT std_logic;
		pop_predicat: in std_logic;
		pop1 : OUT std_logic;
		pop2 : OUT std_logic;
		pop3 : OUT std_logic;
		push1 : OUT std_logic;
		push2 : OUT std_logic;
		push3 : OUT std_logic;
		Tsel : OUT std_logic_vector(2 downto 0);
		Nsel : OUT std_logic_vector(2 downto 0);
		N2sel : OUT std_logic_vector(2 downto 0);
		Ramsel : OUT std_logic_vector(1 downto 0);
		Ram1sel : OUT std_logic_vector(1 downto 0);
		Tlit : OUT std_logic_vector(31 downto 0);
--		IPtrig : OUT std_logic;
		Ipcode : OUT std_logic_vector(10 downto 0);
		LITload : OUT std_logic;
		Tload : OUT std_logic;
		Nload : OUT std_logic;
		N2load : OUT std_logic;
		offset : out std_logic;
		Twait : in std_logic;
		SPMDtrig : out std_logic;
		SPMDcode: out std_logic_vector (12 downto 0)

		);
	END COMPONENT;
	COMPONENT hstack
	PORT(
		clr : IN std_logic;
		clk : IN std_logic;
		push1 : IN std_logic;
		push2 : IN std_logic;
		push3 : IN std_logic;
		pop1 : IN std_logic;
		pop2 : IN std_logic;
		pop3 : IN std_logic;
		Tsel : IN std_logic_vector(2 downto 0);
		Nsel : IN std_logic_vector(2 downto 0);
		N2sel : IN std_logic_vector(2 downto 0);
		ramsel : IN std_logic_vector(1 downto 0);
		ram1sel : IN std_logic_vector(1 downto 0);
		Tload : IN std_logic;
		LITload : INout std_logic;
		Nload : IN std_logic;
		N2load : IN std_logic;
		Tlit : IN std_logic_vector(31 downto 0);
		Tin : IN std_logic_vector(31 downto 0);
		Nin : IN std_logic_vector(31 downto 0);
		N2in : IN std_logic_vector(31 downto 0);
		IPdone : IN std_logic;		
		Tout : OUT std_logic_vector(31 downto 0);
		Nout : OUT std_logic_vector(31 downto 0);
		N2out : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;




	attribute KEEP1 : string;

	attribute KEEP1 of Rsl : signal is "yes";


begin
bool_T <= '0' when Tout_bus= (31 downto 0 =>'0') else '1' ;

Tout <= Tout_bus;
HSU_CUnit: CUnit PORT MAP(
		Instruction => Instruction,
		IPdone => IPdone,
		Instr_ready => Instr_ready,
		Next_inst => Next_inst,
--		Ortree => Ortree,
		clr => clr,
		clk => clk,
		pop_predicat => bool_T,
		pop1 => pp1,
		pop2 => pp2,
		pop3 => pp3,
		push1 => ph1,
		push2 => ph2,
		push3 => ph3,
		Tsel => Tsl,
		Nsel => Nsl,
		N2sel => N2sl,
		Ramsel => Rsl,
		Ram1sel => R1sl,
		Tlit => Litteral,
--		IPtrig => IPtrig,
		Ipcode => Ipcode,
		LITload => LITld,
		Tload => Tld,
		Nload => Nld,
		N2load => N2ld,
		offset =>open,
		Twait =>Twait,
		SPMDtrig =>SPMDtrig,
		SPMDcode=>SPMDcode

	);
	
	offset <= Bool_T;
HSU_hstack: hstack PORT MAP(
		clr => clr,
		clk => clk,
		push1 => ph1,
		push2 => ph2,
		push3 => ph3,
		pop1 => pp1,
		pop2 => pp2,
		pop3 => pp3,
		Tsel => Tsl,
		Nsel => Nsl,
		N2sel => N2sl,
		ramsel => Rsl,
		ram1sel => R1sl,
		Tload => Tld,
		LITload => LITld,
		Nload => Nld,
		N2load => N2ld,
		Tlit => Litteral,
		Tin => Tin,
		Nin => Nin,
		N2in => N2in,
		Tout => Tout_bus,
		Nout => Nout,
		N2out => N2out,
		IPdone => IPdone
	);


end Behavioral;

