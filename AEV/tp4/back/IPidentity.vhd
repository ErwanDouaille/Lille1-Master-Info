----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    19:20:26 10/02/2013 
-- Design Name: 
-- Module Name:    IPidentity - Behavioral 
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
use work.IPcodes.all;



entity IP_identity is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           Nin : in  STD_LOGIC_VECTOR (31 downto 0);
           N2in : in  STD_LOGIC_VECTOR (31 downto 0);
           IPcode : in  STD_LOGIC_vector (10 downto 0);
           Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           Nout : out  STD_LOGIC_VECTOR (31 downto 0);
           N2out : out  STD_LOGIC_VECTOR (31 downto 0));
end IP_identity;

architecture Behavioral of IP_identity is
begin

Tout <= Tin when (IPcode = Mycode) else (others =>'Z');
Nout <= Nin when (IPcode = Mycode) else (others =>'Z');
N2out <= N2in when (IPcode = Mycode) else (others =>'Z');

end Behavioral;

