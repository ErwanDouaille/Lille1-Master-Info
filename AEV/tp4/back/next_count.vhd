----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    08:52:52 09/29/2013 
-- Design Name: 
-- Module Name:    next_count - Behavioral 
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
use IEEE.STD_LOGIC_signed.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity next_count is
    Port ( count_in : in  STD_LOGIC_VECTOR (7 downto 0);
				lastcycle : in std_logic;
           count_out : out  STD_LOGIC_VECTOR (7 downto 0);
           X,Y  : in  STD_LOGIC_vector (1 downto 0));

end next_count;

architecture Behavioral of next_count is

begin


count_out <= 	count_in - 1 when lastcycle='1' and ((X= "01" and Y="00" ) or (X= "10" and Y="01" ) or (X= "11" and Y="10" )) else
					count_in - 2 when lastcycle='1'  and ((X= "10" and Y="00" ) or (X= "11" and Y="01" ))  else
					count_in - 3 when lastcycle='1'  and ((X= "11" and Y="00" )) else
					count_in + 1 when lastcycle='1'  and (( X= "00" and Y="01" ) or (X= "01" and Y="10" ) or (X= "10" and Y="11" )) else
					count_in + 2 when lastcycle='1'  and ((X= "00" and Y="10" ) or (X= "01" and Y="11" )) else
					count_in + 3 when lastcycle='1'  and ((X= "00" and Y="11" )) else
					count_in ;



end Behavioral;

