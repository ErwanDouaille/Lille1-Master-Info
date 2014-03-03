----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    18:50:05 02/15/2013 
-- Design Name: 
-- Module Name:    ffdce - Behavioral 
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

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity ffdce is
    Port ( d : in  STD_LOGIC;
           clk : in  STD_LOGIC;
           clk_en : in  STD_LOGIC;
           Q : out  STD_LOGIC);
end ffdce;

architecture Behavioral of ffdce is

begin
 p_ffcdce: process (clk)
begin 

if falling_edge ( clk) then
	if clk_en = '1' then
		q<= d;
	end if;
end if;
end process p_ffcdce;
end Behavioral;

