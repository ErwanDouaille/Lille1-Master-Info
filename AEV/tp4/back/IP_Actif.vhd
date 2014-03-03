----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    09:32:52 02/21/2013 
-- Design Name: 
-- Module Name:    IP_Actif - Behavioral 
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

entity IP_Actif is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( IPcode : in  STD_LOGIC_VECTOR (10 downto 0);
           load_actif : out  STD_LOGIC);
end IP_Actif;

architecture Behavioral of IP_Actif is

begin
load_actif <= '1' when IPcode(10 downto 0) = Mycode else '0';

end Behavioral;

