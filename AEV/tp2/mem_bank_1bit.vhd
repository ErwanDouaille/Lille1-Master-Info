----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    13:30:40 02/17/2013 
-- Design Name: 
-- Module Name:    mem_bank_1bit - Behavioral 
-- Project Name: 
-- Target Devices: 
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;

use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_unsigned.ALL;


entity mem_bank_1bit is
    Port ( clk : in std_logic;
			  addr : in  STD_LOGIC_VECTOR (5 downto 0);
			  addr_lect: in  STD_LOGIC_VECTOR (5 downto 0);
           data_in : in  STD_LOGIC;
           data_out : out  STD_LOGIC;
           we : in  STD_LOGIC;
           cs : in  STD_LOGIC
			  );
end mem_bank_1bit;

architecture Behavioral of mem_bank_1bit is


signal RAM64bit : std_logic_vector (63 downto 0) ;

begin

  
process (clk)

begin
if clk'event and clk='1' then
	if we= '1' and cs= '1' then
		RAM64bit(conv_integer(addr) )<= data_in;

end if;

end if;

end process;








	
data_out <= RAM64bit( conv_integer (addr_lect));-- when we= '0' and cs= '1'  ;

end Behavioral;

