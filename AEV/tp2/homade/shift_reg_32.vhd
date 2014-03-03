----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    18:55:37 02/15/2013 
-- Design Name: 
-- Module Name:    shift_reg_32 - Behavioral 
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

entity shift_reg is
		Generic ( n : positive  := 32);
    Port ( D : in  STD_LOGIC;  -- input  daisy ring
				d_bus : in std_logic_vector (N - 1 downto 0);  -- input from tin
				ld_reg: in std_logic;  -- Load reg from slave
           clk : in  STD_LOGIC;
           shift_en : in  STD_LOGIC;  --  select shift activity
           Q : out  STD_LOGIC;  -- out daisy 
           q_bus : out  STD_LOGIC_VECTOR (N - 1 downto 0));  -- output N bit  to tout
end shift_reg;

architecture Behavioral of shift_reg is
signal qbus : std_logic_vector (N-1 downto 0);
signal in_d : std_logic_vector (N-1 downto 0);
signal load : std_logic;
COMPONENT ffdce
	PORT(
		d : IN std_logic;
		clk : IN std_logic;
		clk_en : IN std_logic;          
		Q : OUT std_logic
		);
	END COMPONENT;
begin
in_d(N-1) <= (shift_en and D ) or ( (not shift_en) and d_bus(N-1));

load <= shift_en or ld_reg;
	in_d(N-2 downto 0) <= ((N-2 downto 0 =>shift_en) and qbus(N-1 downto 1) ) or ( (N-2 downto 0=>not shift_en) and d_bus(N-2 downto 0));

g : for I in 0 to N-1 generate
	begin
	DFF : COMPONENT ffdce
			port map ( in_d(i), clk, load, qbus(i) );	
	
	
--	inst_left : if i = N-1 generate
--		BEGIN
--			DFF : COMPONENT ffdce
--				port map ( in_d(i), clk, shift_en, qbus(i) );
--		end generate inst_left;
--	inst_middle : if i < N-1  and i > 1 generate
--		BEGIN
--			DFF : COMPONENT ffdce
--				port map ( qbus(i-1), clk, shift_en, qbus(i) );
--		end generate inst_middle;
--	inst_right : if i = 1 generate
--		BEGIN
--			DFF : COMPONENT ffdce
--				port map ( qbus(i-1), clk, shift_en, qbus(i) );
--		end generate inst_right;
	end generate g;	
	Q<= qbus(0);
	q_bus <= qbus;
end Behavioral;

