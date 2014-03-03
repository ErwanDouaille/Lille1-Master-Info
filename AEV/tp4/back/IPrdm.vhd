----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:20:05 12/13/2013 
-- Design Name: 
-- Module Name:    iprdm - Behavioral 
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

entity IPrdm is

GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( --Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           clk:in STD_LOGIC;
           IPcode : in  STD_LOGIC_vector (10 downto 0);
           Tout : out  STD_LOGIC_VECTOR (31 downto 0));
           
end IPrdm;

architecture Behavioral of IPrdm is
signal en: std_logic;
	signal toto : std_logic_vector (31 downto 0);
	COMPONENT random
	 generic ( width : integer :=  32 );
	port (

      clk : in std_logic;
		enable : in std_logic;

      random_num : out std_logic_vector (width-1 downto 0)   --output vector           

    );
 END COMPONENT;
	
begin

Random_reg : random
      port map (
                clk => clk,
		enable =>en,
		

      random_num =>toto);
		
		en <= '1' when (IPcode = Mycode) else ('Z');
Tout <= toto when (IPcode = Mycode) else (others =>'Z');
end Behavioral;

