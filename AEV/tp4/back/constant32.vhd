----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    18:47:38 09/29/2013 
-- Design Name: 
-- Module Name:    constant32 - Behavioral 
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

entity constant32 is
    Port ( value : in  STD_LOGIC_VECTOR (11 downto 0);
           value32 : out  STD_LOGIC_VECTOR (31 downto 0));
end constant32;

architecture Behavioral of constant32 is

begin

value32 <= x"00000" & value;
end Behavioral;

