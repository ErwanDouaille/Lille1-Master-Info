----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    13:09:36 09/29/2013 
-- Design Name: 
-- Module Name:    select_out - Behavioral 
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

entity select_out is
    Port ( count : in  STD_LOGIC_VECTOR (1 downto 0);
           selTout : out  STD_LOGIC_VECTOR (1 downto 0);
           selNout : out  STD_LOGIC_VECTOR (1 downto 0);
           selN2out : out  STD_LOGIC_VECTOR (1 downto 0);
			  seloverS : out  STD_LOGIC_VECTOR (1 downto 0));
end select_out;

architecture Behavioral of select_out is

begin
selTout <= 	"00" when count = "01" else
				"01" when count = "10" else
				"10" when count = "11" else
				"11";
				
selNout <= 	"00" when count = "10" else
				"01" when count = "11" else
				"10" when count = "00" else
				"11";
				
selN2out <= "00" when count = "11" else
				"01" when count = "00" else
				"10" when count = "01" else
				"11";
seloverS <= "00" when count = "00" else
				"01" when count = "01" else
				"10" when count = "10" else
				"11";

end Behavioral;

