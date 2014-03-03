----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    08:39:47 02/19/2013 
-- Design Name: 
-- Module Name:    IP_MEcom - Behavioral 
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

entity IP_MEcom is
		GENERIC (Mycode : std_logic_vector(9 downto 0) );
    Port ( IPcode : in  STD_LOGIC_VECTOR (10 downto 0);
           Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           Write_net : out  STD_LOGIC;
           Dbus : out  STD_LOGIC_VECTOR (31 downto 0);
           Qbus : in  STD_LOGIC_VECTOR (31 downto 0));
end IP_MEcom;

architecture Behavioral of IP_MEcom is

begin
tout <= Qbus when Mycode &"0" = IPcode else (others=>'Z');
Dbus <= Tin;
Write_net <= '1' when Mycode &"1" = IPcode else '0';
end Behavioral;

