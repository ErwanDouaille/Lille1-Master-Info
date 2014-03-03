----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : Cunit
-- Project Name : Homade V2.1
-- Revision :     MAP Promlem : changing discription from sequential to parallel instruction 
-- 			  1-  "Next_inst" signal assignment described sequentially
-- 			  2-  "next1" signal assignment described CONCURRENTLY
--                                          
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  Control Unit
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
use work.OPcodes.all;

entity CUnit is
    Port ( Instruction : in  STD_LOGIC_VECTOR (15 downto 0);
           IPdone : in  STD_LOGIC;
           Instr_ready : in  STD_LOGIC;
			  Next_inst : out std_logic ;
--           Ortree : in  STD_LOGIC;
           clr : in  STD_LOGIC;
			  clk : in  STD_LOGIC;
--           pop1 : out  STD_LOGIC:='0';
--           pop2 : out  STD_LOGIC:='0';
--           pop3 : out  STD_LOGIC:='0';
--           push1 : out  STD_LOGIC:='0';
--           push2 : out  STD_LOGIC:='0';
--           push3 : out  STD_LOGIC:='0';
			  pop_predicat: in std_logic;
--           Tsel : out  STD_LOGIC_VECTOR (2 downto 0);
--           Nsel : out  STD_LOGIC_VECTOR (2 downto 0);
--           N2sel : out  STD_LOGIC_VECTOR (2 downto 0);
--           Ramsel  : out  STD_LOGIC_VECTOR (1 downto 0);
--			  Ram1sel  : out  STD_LOGIC_VECTOR (1 downto 0);

           Tlit : out  STD_LOGIC_VECTOR (11 downto 0);
--           IPtrig : out  STD_LOGIC;
           Ipcode : out  STD_LOGIC_VECTOR (10 downto 0);
           LITload : out  STD_LOGIC;
			  offset: out std_logic;
			  Twait : in std_logic;
			  shortIP : out std_logic;
			  X, Y : out std_logic_vector (1 downto 0) ;
			  SPMDtrig: out std_logic;
			  SPMDcode: out std_logic_vector(12 downto 0));

	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end CUnit;
architecture Behavioral of CUnit is

	COMPONENT reg1
	generic (N : integer := 32);
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(N-1 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(N-1 downto 0)
		);
	END COMPONENT;
	COMPONENT qd
	PORT(
		load : IN std_logic;
		d : IN std_logic;
		clr ,clk: IN std_logic;          
		q : OUT std_logic
		);
	END COMPONENT;

Constant negatif : std_logic_vector(31 downto 0) := (others =>'1');
Constant positif : std_logic_vector(31 downto 0) := (others =>'0');

signal next1,  endwait, next3 , iptrig0,iptrig1: std_logic:='0';
signal val ,control: std_logic_vector (3 downto 0) :="0000";
signal instrr_ready : std_logic_vector (0 downto 0);
signal ins, ins_in : std_logic_vector (15 downto 0);
signal Iready : std_logic_vector (0 downto 0);
signal dwait,qwait : std_logic;



	attribute KEEP : string;
	attribute KEEP of next1 : signal is "yes";
	attribute KEEP of next3 : signal is "yes";
	attribute KEEP of Iready : signal is "yes";

attribute KEEP of dwait : signal is "yes";
attribute KEEP of qwait : signal is "yes";

attribute KEEP of ins_in : signal is "yes";
attribute KEEP of ins : signal is "yes";

attribute KEEP of instrr_ready : signal is "yes";
attribute KEEP of val : signal is "yes";




begin

	RI: reg1 
	generic map (N=>16)
	PORT MAP(
		load => '1',
		d => ins_in(15 downto 0),
		clk => clk,
		clr => clr,
		q => ins
	);
	
	
	Rwait: qd
	PORT MAP(
		load => '1',
		d => dwait,
		clk => clk,
		clr => clr,
		q => qwait
	);
	ins_in <= instruction when instr_ready ='1'  else ins;

	
	instrr_ready(0) <= instr_ready;
	RI_ready: reg1
	generic map (N=>1)
	PORT MAP(
		load => '1',
		d => instrr_ready(0 downto 0),
		clk => clk,
		clr => clr,
		q => Iready
	);

Next_inst <=  IPdone or endwait;

--******************************************************************
process (clk,clr, instr_ready)
begin
	if  clk'event and clk='0'then
		if clr='1' then	
			  next1 <='1';
	elsif instr_ready ='1' then
			next1 <= '0';
		end if;
	end if;	
end process;	

--next1 <='1' when clr='1' and clk'event and clk='0' else '0' when clk'event and clk='0' and instr_ready ='1' else 'Z';
--******************************************************************	
process (clk)
begin
if clk'event and clk='1' then
	control<="0000";
	IPcode <= "11111111111";
	val <= "0000";
	LITload <='0';
	endwait <= '0';
	Tlit <= x"fff";
	SPMDcode <= "0000000000000";
	SPMDtrig <= '0';
	dwait <='0';
	shortIP <= '0';
	x<= "00";
	Y <= "00";
--	IPtrig <= '0';
--	offset<='0';

	if Iready(0)='1' then
		if  ins(15 downto 12) =  LIT then --- Literal  12 bits
						Tlit <=   ins (11 downto 0);
						LITload<='1';
						Y<= "01";
		elsif ins(15) = '1' then --- IP
						IPcode <= ins (10 downto 0);
						X<= ins (14 downto 13);
						Y <= ins(12 downto 11);
						if ins(10) = '0' then --- instruction in one cycle
							shortIP<='1';
						end if;
		elsif ins(15 downto 13) = SPMD then --- SPMD
						SPMDcode <= ins (12 downto 0);
						SPMDtrig <='1';
--- instruction in one cycle
						shortIP<='1';
		elsif ins(15 downto 13) = IWAIT then   ---- Wait
						Dwait <= '1';
						if twait = '1' and Qwait='1' then
						   endwait<='1';
							dwait <= '0';
						end if;							
		elsif ins(15 downto 10) =  BRANCH_ZERO then   		-- BZ realtif
		   X<="01";
			shortIP<='1';

		elsif 			ins(15 downto 10) =  BRANCH_NOT_ZERO then   		-- BNZ realtif
			X<= "01";
			shortIP<='1';

		else null;
		end if;
		
		
		------
		
	else 
		if ins(15) = '1'  and ins( 10) = '1' and ipdone='0' then --- IP long continued 
				X <= ins (14 downto 13);
				Y <= ins (12 downto 11);
		elsif ins(15 downto 13) = IWAIT then   ---- Wait
						Dwait <= '1';
						if twait = '1' and Qwait='1' then
						   endwait<='1';
							dwait <= '0';						
						end if;	
		end if ;
	end if;
end if;
end process;
		
end Behavioral;

