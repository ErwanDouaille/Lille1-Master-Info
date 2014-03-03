----------------------------------------------------------------------------------					
-- Company: 
-- Engineer: 
-- 
-- Create Date:    12:22:18 10/04/2013 
-- Design Name: 
-- Module Name:    msa_hdl - Behavioral 
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

entity msa_hdl is
    Port ( b0 : in  STD_LOGIC;
           b1 : in  STD_LOGIC;
           gs : in  STD_LOGIC;
           clkin : in  STD_LOGIC;
           rst : in  STD_LOGIC;
           enable_del : out  STD_LOGIC);
end msa_hdl;

architecture Behavioral of msa_hdl is

type etat is (a,b,c,d,e,f);
signal etatpres, etatsuiv : etat;

begin

--registre d'tat
xreg: process(rst,clkin)
begin

	if(rst = '1')then
		etatpres <= a;
	elsif(clkin'event and clkin = '1')then
		etatpres <= etatsuiv;
	end if;
end process;

--IFL
xifl: process(etatpres, b1,b0,gs)
begin
	case etatpres is
		when a =>
			if(gs = '1' and b1='0' and b0='0')then
				etatsuiv <= b;
			else
				etatsuiv <= a;
			end if;
		when b =>
		if(gs = '0')then
			etatsuiv <= c;
		else
			etatsuiv <= b;
		end if;
		when c =>
			if(gs = '1') then
				if(b1 = '0' and b0='1')then
					etatsuiv <= d;
				else
				etatsuiv <=  a;
				end if;
			else
				etatsuiv <= c;
			end if;
		when d =>
			if(gs = '0')then
				etatsuiv <= e;
			else
				etatsuiv <= d;
			end if;
		when e =>
			if(gs = '1') then
				if(b1 = '1' and b0='0') then
					etatsuiv <= f;
				else
					etatsuiv <= a;
				end if;
			else
				etatsuiv <= e;
			end if;
		when f =>
			etatsuiv <= f;
		when others => etatsuiv <= a;
	end case;
end process;

--OFL
enable_del <= '1' when etatpres = f else '0';
end Behavioral;


