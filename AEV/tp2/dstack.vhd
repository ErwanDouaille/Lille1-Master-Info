----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : dstack
-- Project Name : Homade V2.1
-- Revision :     keep internal connection with "KEEP" attribute
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Data Stack unit 
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


entity dstack is
    Port ( clk : in  STD_LOGIC;
           push : in  STD_LOGIC;
           pop : in  STD_LOGIC;
           data_in : in  STD_LOGIC_VECTOR (31 downto 0);
           data_out : out  STD_LOGIC_VECTOR (31 downto 0);
           clr : in  STD_LOGIC);
			  	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end dstack;

architecture Behavioral of dstack is
 type ram_type is array (15 downto 0)of std_logic_vector (31 downto 0);
  signal RAM : ram_type;
  signal stack_ptr  : std_logic_vector (3 downto 0):= "1111" ;
  
    	attribute KEEP : string;
	attribute KEEP of RAM : signal is "yes";
begin
process (clr,clk, push,ram,stack_ptr)

begin 

if clr='1' then 
	stack_ptr<= "1111";
elsif(clk'event and clk='0') then 

	if push = '1' then
		RAM(conv_integer(stack_ptr)) <=data_in;
		if stack_ptr/= "0000" then
			stack_ptr <= stack_ptr - 1;
		end if;
	end if;
	if pop = '1' then
		if stack_ptr/= "1111" then
			stack_ptr<= stack_ptr + 1;
			
		end if;
	end if;
end if;
end process;
process (clk,stack_ptr,ram)
begin
	if stack_ptr /= 15 then 
		data_out <= RAM(conv_integer(stack_ptr) +1);
	else 
		data_out <= RAM(conv_integer(stack_ptr));
	end if;


	
end process;


end Behavioral;

