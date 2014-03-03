----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:18:10 12/13/2013 
-- Design Name: 
-- Module Name:    random - Behavioral 
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

 

entity random is

    generic ( width : integer :=  32 );

port (

      clk : in std_logic;
		enable : in std_logic;

      random_num : out std_logic_vector (width-1 downto 0)   --output vector           

    );

end random;

 

architecture Behavioral of random is

begin

process(clk)

variable rand_temp : std_logic_vector(width-1 downto 0):=(width-1 => '1',others => '0');

variable temp : std_logic := '0';

begin

if(rising_edge(clk)) then
if(enable='1') then

   temp := (((rand_temp(0) xnor rand_temp(2))

                xnor rand_temp(3))

                xnor rand_temp(4));

   rand_temp(width-2 downto 0) := rand_temp(width-1 downto 1);

   rand_temp(width - 1) := temp;
end if;
end if;

random_num <= rand_temp;

end process;

end Behavioral;