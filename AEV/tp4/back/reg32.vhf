--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : reg32.vhf
-- /___/   /\     Timestamp : 12/13/2013 11:38:50
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douylliez/Bureau/V4M1_new/reg32.vhf -w /home/m1/douylliez/Bureau/V4M1_new/reg32.sch
--Design Name: reg32
--Device: spartan6
--Purpose:
--    This vhdl netlist is translated from an ECS schematic. It can be 
--    synthesized and simulated, but it should not be modified. 
--
----- CELL FD16CE_HXILINX_reg32 -----


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity FD16CE_HXILINX_reg32 is
port (
    Q   : out STD_LOGIC_VECTOR(15 downto 0) := (others => '0');

    C   : in STD_LOGIC;
    CE  : in STD_LOGIC;
    CLR : in STD_LOGIC;
    D   : in STD_LOGIC_VECTOR(15 downto 0)
    );
end FD16CE_HXILINX_reg32;

architecture Behavioral of FD16CE_HXILINX_reg32 is

begin

process(C, CLR)
begin
  if (CLR='1') then
    Q <= (others => '0');
  elsif (C'event and C = '1') then
    if (CE='1') then 
      Q <= D;
    end if;
  end if;
end process;


end Behavioral;


library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity reg32 is
   port ( C   : in    std_logic; 
          CE  : in    std_logic; 
          CLR : in    std_logic; 
          D   : in    std_logic_vector (31 downto 0); 
          Q   : out   std_logic_vector (31 downto 0));
end reg32;

architecture BEHAVIORAL of reg32 is
   attribute HU_SET     : string ;
   component FD16CE_HXILINX_reg32
      port ( C   : in    std_logic; 
             CE  : in    std_logic; 
             CLR : in    std_logic; 
             D   : in    std_logic_vector (15 downto 0); 
             Q   : out   std_logic_vector (15 downto 0));
   end component;
   
   attribute HU_SET of XLXI_1 : label is "XLXI_1_4";
   attribute HU_SET of XLXI_2 : label is "XLXI_2_5";
begin
   XLXI_1 : FD16CE_HXILINX_reg32
      port map (C=>C,
                CE=>CE,
                CLR=>CLR,
                D(15 downto 0)=>D(31 downto 16),
                Q(15 downto 0)=>Q(31 downto 16));
   
   XLXI_2 : FD16CE_HXILINX_reg32
      port map (C=>C,
                CE=>CE,
                CLR=>CLR,
                D(15 downto 0)=>D(15 downto 0),
                Q(15 downto 0)=>Q(15 downto 0));
   
end BEHAVIORAL;


