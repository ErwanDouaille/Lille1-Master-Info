----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    10:38:54 02/18/2013 
-- Design Name: 
-- Module Name:    IP_Snum - Behavioral 
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

entity IP_Snum is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );

    Port ( IPcode : in  STD_LOGIC_VECTOR (10 downto 0);
           Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           xnum : in  STD_LOGIC_VECTOR (4 downto 0);
           ynum : in  STD_LOGIC_VECTOR (4 downto 0));
end IP_Snum;

architecture Behavioral of IP_Snum is

begin
Tout <= "00000000000" & ynum & "00000000000" & xnum  when ipcode(10 downto 0)= Mycode else (others =>'Z');


end Behavioral;

