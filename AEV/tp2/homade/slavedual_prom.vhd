----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    08:52:53 02/17/2013 
-- Design Name: 
-- Module Name:    slavedual_prom - Behavioral 
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
use IEEE.STD_LOGIC_unsigned.ALL;


-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;
 
entity slavedual_prom is
    Port (  clk : in  STD_LOGIC;
           addr_a : in  STD_LOGIC_VECTOR (11 downto 0);
           data_a : in  STD_LOGIC;
			  wphase , w48: in std_logic;
           addr_b : in  STD_LOGIC_VECTOR (5 downto 0);
			  addr_w : in  STD_LOGIC_VECTOR (5 downto 0);
			  data_w : in STD_LOGIC_VECTOR (47 downto 0);
           data_b : out  STD_LOGIC_VECTOR (63 downto 0)
			  );
end slavedual_prom;

architecture Behavioral of slavedual_prom is
signal wrt : std_logic ;




signal csbus ,chipselect: std_logic_vector (63 downto 0);
signal data_in : std_logic_vector (63 downto 0) ;
signal wenable : std_logic;
signal addrbus,addr_lect : std_logic_vector (5 downto 0);
signal x, y : std_logic_vector (7 downto 0);

	COMPONENT mem_bank_1bit
	PORT(
		clk : in std_logic;

		addr : IN std_logic_vector(5 downto 0);
		data_in : IN std_logic;
		addr_lect : IN std_logic_vector(5 downto 0);
		we : IN std_logic;
		cs : IN std_logic;          
		data_out : OUT std_logic
--		mem : out STD_LOGIC_VECTOR (63 downto 0)
		);
	END COMPONENT;
begin
addrbus <= addr_a(11 downto 6 ) when wphase = '1' else addr_b when w48 = '0' else addr_w ;

addr_lect <= addr_b;

with  addr_a(5 downto 3) select
x <= 
 "00000001" when "000",
 "00000010" when "001",
 "00000100" when "010",
 "00001000" when "011",
 "00010000" when "100",
 "00100000" when "101",
 "01000000" when "110",
 "10000000" when others;


with  addr_a(2 downto 0) select
y <= "00000001" when "000",
 "00000010" when "001",
 "00000100" when "010",
 "00001000" when "011",
 "00010000" when "100",
 "00100000" when "101",
 "01000000" when "110",
 "10000000" when others;

csbus(0) <= x(0)  and y(0) ;
csbus(1) <= x(0)  and y(1) ;
csbus(2) <= x(0)  and y(2) ;
csbus(3) <= x(0)  and y(3) ;
csbus(4) <= x(0)  and y(4) ;
csbus(5) <= x(0)  and y(5) ;
csbus(6) <= x(0)  and y(6) ;
csbus(7) <= x(0)  and y(7) ;

csbus(8) <= x(1)  and y(0) ;
csbus(9) <= x(1)  and y(1) ;
csbus(10) <= x(1)  and y(2) ;
csbus(11) <= x(1)  and y(3) ;
csbus(12) <= x(1)  and y(4) ;
csbus(13) <= x(1)  and y(5) ;
csbus(14) <= x(1)  and y(6) ;
csbus(15) <= x(1)  and y(7) ;

csbus(16) <= x(2)  and y(0) ;
csbus(17) <= x(2)  and y(1) ;
csbus(18) <= x(2)  and y(2) ;
csbus(19) <= x(2)  and y(3) ;
csbus(20) <= x(2)  and y(4) ;
csbus(21) <= x(2)  and y(5) ;
csbus(22) <= x(2)  and y(6) ;
csbus(23) <= x(2)  and y(7) ;

csbus(24) <= x(3)  and y(0) ;
csbus(25) <= x(3)  and y(1) ;
csbus(26) <= x(3)  and y(2) ;
csbus(27) <= x(3)  and y(3) ;
csbus(28) <= x(3)  and y(4) ;
csbus(29) <= x(3)  and y(5) ;
csbus(30) <= x(3)  and y(6) ;
csbus(31) <= x(3)  and y(7) ;

csbus(32) <= x(4)  and y(0) ;
csbus(33) <= x(4)  and y(1) ;
csbus(34) <= x(4)  and y(2) ;
csbus(35) <= x(4)  and y(3) ;
csbus(36) <= x(4)  and y(4) ;
csbus(37) <= x(4)  and y(5) ;
csbus(38) <= x(4)  and y(6) ;
csbus(39) <= x(4)  and y(7) ;

csbus(40) <= x(5)  and y(0) ;
csbus(41) <= x(5)  and y(1) ;
csbus(42) <= x(5)  and y(2) ;
csbus(43) <= x(5)  and y(3) ;
csbus(44) <= x(5)  and y(4) ;
csbus(45) <= x(5)  and y(5) ;
csbus(46) <= x(5)  and y(6) ;
csbus(47) <= x(5)  and y(7) ;

csbus(48) <= x(6)  and y(0) ;
csbus(49) <= x(6)  and y(1) ;
csbus(50) <= x(6)  and y(2) ;
csbus(51) <= x(6)  and y(3) ;
csbus(52) <= x(6)  and y(4) ;
csbus(53) <= x(6)  and y(5) ;
csbus(54) <= x(6)  and y(6) ;
csbus(55) <= x(6)  and y(7) ;

csbus(56) <= x(7)  and y(0) ;
csbus(57) <= x(7)  and y(1) ;
csbus(58) <= x(7)  and y(2) ;
csbus(59) <= x(7)  and y(3) ;
csbus(60) <= x(7)  and y(4) ;
csbus(61) <= x(7)  and y(5) ;
csbus(62) <= x(7)  and y(6) ;
csbus(63) <= x(7)  and y(7) ;

chipselect <= x"FFFFFFFFFFFF0000" when w48='1' else csbus when wphase = '1'  else (others =>'1');

data_in <= data_w & x"0000" when w48='1' else (others => data_a);

multi_bank : for i in 0 to 63 generate
	bank : mem_bank_1bit PORT MAP(
		clk=> clk,
		addr => addrbus,
		data_in =>data_in(i) ,
		data_out => data_b(i),
		
		addr_lect => addr_lect,
		we => wrt,
		cs => chipselect(i)

		);
end generate multi_bank;

wrt <= wphase or w48;






end Behavioral;

