----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    17:01:22 02/18/2013 
-- Design Name: 
-- Module Name:    qd - Behavioral 
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

entity qd is
    Port ( d : in  STD_LOGIC;
				load : std_logic;
           clk : in  STD_LOGIC;
           clr : in  STD_LOGIC;
           q : out  STD_LOGIC);
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end qd;

architecture Behavioral of qd is
signal output : STD_LOGIC;
	attribute KEEP : string;
	attribute KEEP of output : signal is "yes";
begin	
	-- N-bit register with load
	process(clk, clr)
	begin
		if clk'event   and clk = '1' then
			if clr = '1' then
				output <=  '0';
			elsif load = '1' then
				output <= d;
			end if;
		end if;						 
	end process;
	q<=output;


end Behavioral;

