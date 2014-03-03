----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : FIFO3port
-- Project Name : Homade V2.1
-- Revision :     no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Fifo with 3 port access unit
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
use IEEE.STD_LOGIC_unsigned.all;


entity FIFO3port is
port(   clk : in std_logic;  --Clock for the stack.
         clr : in std_logic;  --Enable the stack. Otherwise neither push nor pop will happen.
         ramin : in std_logic_vector(31 downto 0);  --Data to be pushed to stack 
        ram1in : in std_logic_vector(31 downto 0);  --Data to be pushed to stack 
        ram2in : in std_logic_vector(31 downto 0);  --Data to be pushed to stack 

         ramout : out std_logic_vector(31 downto 0);  --Data popped from the stack.  

         ram1out : out std_logic_vector(31 downto 0);  --Data popped from the stack.  

         ram2out : out std_logic_vector(31 downto 0);  --Data popped from the stack.  
         ramload, rampop, pop1,pop2,pop3,push1,push2,push3 : in std_logic --active low for POP and active high for PUSH.

         );  
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";			
end FIFO3port;

architecture Behavioral of FIFO3port is

component stack 
   port ( clk   : in    std_logic; 
			 clr   : in    std_logic; 
          Nin   : in    std_logic_vector (31 downto 0); 
          N2in  : in    std_logic_vector (31 downto 0); 
          pop1  : in    std_logic; 
          pop2  : in    std_logic; 
          pop3  : in    std_logic; 
          push1 : in    std_logic; 
          push2 : in    std_logic; 
          push3 : in    std_logic; 
          Tin   : in    std_logic_vector (31 downto 0); 
          Top   : in    std_logic_vector (5 downto 0); 
          Nout  : out   std_logic_vector (31 downto 0); 
          N2out : out   std_logic_vector (31 downto 0); 
          Tout  : out   std_logic_vector (31 downto 0));
end component;

signal addr :std_logic_vector (5 downto 0);
signal addrl : std_logic_vector( 5 downto 0);

begin

  Inst_stack : stack
      port map(
			 clk   => clk,
			 clr => clr,
          Nin   =>Ram1in,
          N2in  =>Ram2in,
          pop1  =>pop1,
          pop2  =>pop2,
          pop3  =>pop3,
          push1 =>push1,
          push2 =>push2,
          push3 =>push3, 
          Tin  =>Ramin, 
          Top  => addr, 
          Nout =>Ram1out,
          N2out =>Ram2out, 
          Tout => Ramout);

process(clr, clk, ramload, rampop) 
variable stack_ptr  : std_logic_vector (5 downto 0)  := "000000";



begin
if(clk'event and clk='1') then 
	if clr='1' then 
		stack_ptr:= "000000";
	elsif(ramload = '1') then

				--PUSH section.
				if push1 = '1' then
					stack_ptr := stack_ptr + 1;
				elsif push2 = '1'  then
					stack_ptr := stack_ptr +2;
				elsif push3 = '1' then
					stack_ptr := stack_ptr +3;
				end if;

	elsif rampop='1'  then
				if pop1 = '1'   then
				stack_ptr := stack_ptr -1;
				elsif pop2 = '1'  then
				stack_ptr := stack_ptr -2;
				elsif pop3 = '1' then
				stack_ptr := stack_ptr -3;
				end if;	

	end if;
addrl<= stack_ptr;

end if;

end process;
addr<= addrl when push1='1' or push2='1' or push3='1' else
		 addrl -1 when pop1='1' else
		 addrl-2 when pop2='1'
		 else
		 addrl - 3 when pop3='1' else (others=>'0');	
	
	
end Behavioral;