----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:47:34 10/04/2013 
-- Design Name: 
-- Module Name:    diviseur_clk - Behavioral 
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

entity diviseur_clk is
    Port ( clkin : in  STD_LOGIC;
           clk2hz : out  STD_LOGIC;
           clk16hz : out  STD_LOGIC;
           clk2khz : out  STD_LOGIC);
end diviseur_clk;

architecture Behavioral of diviseur_clk is

signal cnt2hz : integer range 0 to 4e6:= 0;
signal cnt16hz : integer range 0 to 5e5:= 0;
signal cnt2khz : integer range 0 to 4e3:= 0;
signal div2hz_temp : std_logic := '0';
signal div16hz_temp : std_logic := '0';
signal div2khz_temp : std_logic := '0';

begin
process (clkin) begin
	if (clkin'event and clkin = '1') then
		
		-- Diviseur par 4e6
		if cnt2hz >= 4e6 then
			div2hz_temp <= not(div2hz_temp);
			cnt2hz <= 0;
		else
			div2hz_temp <= div2hz_temp;
			cnt2hz <= cnt2hz + 1;
		end if;
		clk2hz <= div2hz_temp; -- horloge  2 Hz
		
		-- Diviseur par 5e5
		if cnt16hz >= 5e5 then
			div16hz_temp <= not(div16hz_temp);
			cnt16hz <= 0;
		else
			div16hz_temp <= div16hz_temp;
			cnt16hz <= cnt16hz + 1;
			
		end if;
		clk16hz <= div16hz_temp; -- horloge  16 Hz
		
		-- Diviseur par 4e3
		if cnt2khz >= 4e3 then
			div2khz_temp <= not(div2khz_temp);
			cnt2khz <= 0;
		else
			div2khz_temp <= div2khz_temp;
			cnt2khz <= cnt2khz + 1;
		end if;
		clk2khz <= div2khz_temp; -- horloge  2 kHz
	end if;
end process;

end Behavioral;

