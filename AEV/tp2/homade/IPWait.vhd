----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IP_Wait
-- Project Name : Homade V2.1
-- Revision :     signal "rst" assignement out of state machine (concurrent instruction)
--                                         
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  IP Wait unit FSM
--
--**********************************************************************
--				this IP  will wait for Tin cycle ( top of the stack)
--
--						*********	Tin value MUST BE >= 4			  **********
-- *********************************************************************
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
use IEEE.std_logic_unsigned.all;
use IEEE.std_logic_arith.all;
use IEEE.numeric_std.all;



entity IP_Wait is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           IPcode : in  STD_LOGIC_vector (10 downto 0);
				clk : in STD_LOGIC;
				reset: in STD_LOGIC;
           IPdone : out  STD_LOGIC);
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end IP_Wait;


architecture FSM of IP_wait is
--
--    -- define the states of FSM model
-- signal action,rst, cp_zero,IPd: STD_LOGIC:='0';
 signal rst,  IPd : std_logic;
 signal cptr_d,cptr_next : std_logic_vector (31 downto 0);
--
--
 signal c :integer :=0;
 
    -- define the states of FSM model

    type state_type is (idle, starting, finish, fetch_next,Decode_next);
    signal next_state, current_statew: state_type;
--    type state_type is (Idle, Init, Decr, Done);
--    signal next_state, current_state: state_type := idle;
--  
COMPONENT reg1
 PORT(
  load : IN std_logic;
  d : IN std_logic_vector(31 downto 0);
  clr ,clk: IN std_logic;          
  q : OUT std_logic_vector(31 downto 0)
  );
 END COMPONENT;
begin
  Wait_reg : reg1
      port map (
                d(31 downto 0)=>cptr_d,
                load=>'1',
      clk=>clk,
      clr => reset,
                q(31 downto 0)=>cptr_next );  


--

--
cptr_d <= Tin - 3 when rst='1' else cptr_next-1;
---

IPdone<=IPd;

    -- cocurrent process#1: state registers
    state_reg: process(clk, reset)
    begin

 if (clk'event and clk='1') then
  if (reset='1') then
            current_statew <= idle;
  else
   current_statew <= next_state; 
  end if;
 end if;

    end process;        

    -- cocurrent process#2: combinational logic
    comb_logic: process( Ipcode,cptr_next,current_statew,cptr_d)
    begin

 -- use case statement to show the 
 -- state transistion

 case current_statew is

     when idle => 
 
--   if Ipcode =  Mycode and the_end='0' then

   if Ipcode =  Mycode then
     rst<='1';
       next_state <= starting;

   else

    rst <='0';
    next_state <= idle;
   end if;
   IPd <= '0'; 

     when starting =>
   rst<='0';

    if cptr_d= x"00000000"
    then
     next_state <= finish;       
    else 
     next_state <= starting;  
    end if; 
     Ipd <= '0';

     when finish => 
   IPd  <= '1';  
   rst<='0';

   next_state<= fetch_next;
   
     when fetch_next => 
   IPd  <= '0';  
   rst<='0';

    next_state<= Decode_next;
   
     when Decode_next => 
   IPd  <= '0';  
   rst<='0';

   next_state<= idle;



     when others =>  
--   IPd <= '0';     
--   rst<='0';
   next_state <= idle;
   NULL;
 end case;

    end process;

end FSM;

----------------------------
