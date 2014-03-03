----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IP_fibo                                         
-- Project Name : Homade V2.1
-- Revision :     signal "rst" assignement out of state machine (concurrent instruction)
--                                         
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  IP fibo unit FSM
--
--**********************************************************************
--				this IP  process the Nieme niumber of the fibonacci Fi list 
-- in N+1 cycle ( result on top of the stack)
-- F0 = 0 F1 = 1 F2= 1 F3= 2 F4 = 3 F5 = 5 .....
--
--						*********		N>=2		  **********
-- *********************************************************************
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  nov 2012) jean-luc.dekeyser@lifl.fr
-- Wissem Chouchene () wissem.chouchene@inria.fr
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
use IEEE.std_logic_unsigned.all;
use IEEE.std_logic_arith.all;
use IEEE.numeric_std.all;


entity IP_fibo is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           IPcode : in  STD_LOGIC_vector (10 downto 0);
				clk : in STD_LOGIC;
				reset: in STD_LOGIC;
			Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           IPdone : out  STD_LOGIC);
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end IP_fibo;


architecture FSM_fibo of IP_fibo is
--
--    -- define the states of FSM model
--	signal action,rst, cp_zero,IPd: STD_LOGIC:='0';
	signal rst,  IPd ,the_end: std_logic;
	signal cptr_d,cptr_next,fibobus : std_logic_vector (31 downto 0);
--
--
	signal c :integer :=0;
	
    -- define the states of FSM model
    type state_type is (idle, starting, finish, finish2, finish3);
    signal  current_state_fibo: state_type;

--	 
COMPONENT reg1
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(31 downto 0);
		clr ,clk: IN std_logic;                  
		q : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
	


COMPONENT fibogen
	PORT(
		clk:		in std_logic;
	init:		in std_logic;
	fiboout : out STD_LOGIC_VECTOR(31 downto 0) 
		);
	END COMPONENT;
	
	
  
	
begin
  Fibo_reg : reg1
      port map (
                d(31 downto 0)=>cptr_d,
                load=>'1',
					 clk=>clk,
					 clr => reset,
                q(31 downto 0)=>cptr_next );  
					 
	Fibo_gen : fibogen
      port map (
                
					 clk=>clk,
					 init => rst,
                fiboout(31 downto 0)=>fibobus );  


cptr_d <= Tin  when rst='1' else cptr_next-1;



    comb_logic: process( reset, clk)
    begin

	-- use case statement to show the 
	-- state transistion
	if reset ='1' then
		current_state_fibo<= idle;
	elsif clk'event  and clk='1' then		
		case current_state_fibo is

	    when idle =>	

			if Ipcode =  Mycode then
				 rst<='1';
			    current_state_fibo <= starting;

			else

				rst <='0';
				current_state_fibo <= idle;
			end if;

	    when starting =>
			rst<='0';

			
				if cptr_d= x"00000002"
				then
					current_state_fibo <= finish;       
				else 
					current_state_fibo <= starting;  
				end if; 
	    when finish =>	
 
			rst<='0';

			current_state_fibo<= finish2;
     when finish2 => 
 
   current_state_fibo <= finish3;
     when finish3 => 
 
   current_state_fibo <= idle;

	    when others =>  

			current_state_fibo <= idle;
			NULL;
	end case;
	end if;
    end process;
	 tout <= fibobus when current_state_fibo = finish else (others =>'Z');
	ipdone <= '1' when current_state_fibo = finish else '0';
end FSM_fibo;

-----------------------------------------------------