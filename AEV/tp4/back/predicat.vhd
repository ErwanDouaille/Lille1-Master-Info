----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    12:37:00 10/03/2013 
-- Design Name: 
-- Module Name:    predicat - Behavioral 
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

entity predicat is
    Port ( tin : in  STD_LOGIC_VECTOR (31 downto 0);
           predicat : out  STD_LOGIC);
end predicat;

architecture Behavioral of predicat is

begin
predicat <= '0' when tin = (31 downto 0 =>'0') else '1' ;

end Behavioral;

