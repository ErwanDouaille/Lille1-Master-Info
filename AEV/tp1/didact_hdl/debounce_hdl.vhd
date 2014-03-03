----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:29:02 10/04/2013 
-- Design Name: 
-- Module Name:    debounce_hdl - Behavioral 
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

entity debounce_hdl is
    Port ( sig_in : in  STD_LOGIC;
           clkin : in  STD_LOGIC;
           sig_out : out  STD_LOGIC);
end debounce_hdl;

architecture Behavioral of debounce_hdl is


--  Provides a one-shot pulse from a non-clock input, with reset
--**Insert the following between the 'architecture' and
---'begin' keywords**
signal Q1, Q2, Q3 : std_logic;

begin
 
--**Insert the following after the 'begin' keyword**
process(clkin)
	begin
		if (clkin'event and clkin = '1') then
			Q1 <= sig_in;
			Q2 <= Q1;
			Q3 <= Q2;
		end if;
end process;
 
sig_out <= Q1 and Q2 and (not Q3);
 
				


end Behavioral;

