--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : netring.vhf
-- /___/   /\     Timestamp : 12/13/2013 11:38:50
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douylliez/Bureau/V4M1_new/netring.vhf -w /home/m1/douylliez/Bureau/V4M1_new/netring.sch
--Design Name: netring
--Device: spartan6
--Purpose:
--    This vhdl netlist is translated from an ECS schematic. It can be 
--    synthesized and simulated, but it should not be modified. 
--
----- CELL FD16CE_HXILINX_netring -----


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity FD16CE_HXILINX_netring is
port (
    Q   : out STD_LOGIC_VECTOR(15 downto 0) := (others => '0');

    C   : in STD_LOGIC;
    CE  : in STD_LOGIC;
    CLR : in STD_LOGIC;
    D   : in STD_LOGIC_VECTOR(15 downto 0)
    );
end FD16CE_HXILINX_netring;

architecture Behavioral of FD16CE_HXILINX_netring is

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

entity reg32_MUSER_netring is
   port ( C   : in    std_logic; 
          CE  : in    std_logic; 
          CLR : in    std_logic; 
          D   : in    std_logic_vector (31 downto 0); 
          Q   : out   std_logic_vector (31 downto 0));
end reg32_MUSER_netring;

architecture BEHAVIORAL of reg32_MUSER_netring is
   attribute HU_SET     : string ;
   component FD16CE_HXILINX_netring
      port ( C   : in    std_logic; 
             CE  : in    std_logic; 
             CLR : in    std_logic; 
             D   : in    std_logic_vector (15 downto 0); 
             Q   : out   std_logic_vector (15 downto 0));
   end component;
   
   attribute HU_SET of XLXI_1 : label is "XLXI_1_6";
   attribute HU_SET of XLXI_2 : label is "XLXI_2_7";
begin
   XLXI_1 : FD16CE_HXILINX_netring
      port map (C=>C,
                CE=>CE,
                CLR=>CLR,
                D(15 downto 0)=>D(31 downto 16),
                Q(15 downto 0)=>Q(31 downto 16));
   
   XLXI_2 : FD16CE_HXILINX_netring
      port map (C=>C,
                CE=>CE,
                CLR=>CLR,
                D(15 downto 0)=>D(15 downto 0),
                Q(15 downto 0)=>Q(15 downto 0));
   
end BEHAVIORAL;



library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity netring is
   port ( clk : in    std_logic; 
          clr : in    std_logic; 
          I1  : in    std_logic_vector (31 downto 0); 
          I2  : in    std_logic_vector (31 downto 0); 
          I3  : in    std_logic_vector (31 downto 0); 
          sel : in    std_logic_vector (1 downto 0); 
          O1  : out   std_logic_vector (31 downto 0));
end netring;

architecture BEHAVIORAL of netring is
   attribute BOX_TYPE   : string ;
   signal XLXN_1   : std_logic_vector (31 downto 0);
   signal XLXN_10  : std_logic;
   signal O1_DUMMY : std_logic_vector (31 downto 0);
   component Mux4
      port ( X0  : in    std_logic_vector (31 downto 0); 
             X1  : in    std_logic_vector (31 downto 0); 
             X2  : in    std_logic_vector (31 downto 0); 
             X3  : in    std_logic_vector (31 downto 0); 
             sel : in    std_logic_vector (1 downto 0); 
             Y   : out   std_logic_vector (31 downto 0));
   end component;
   
   component reg32_MUSER_netring
      port ( D   : in    std_logic_vector (31 downto 0); 
             CLR : in    std_logic; 
             C   : in    std_logic; 
             CE  : in    std_logic; 
             Q   : out   std_logic_vector (31 downto 0));
   end component;
   
   component VCC
      port ( P : out   std_logic);
   end component;
   attribute BOX_TYPE of VCC : component is "BLACK_BOX";
   
begin
   O1(31 downto 0) <= O1_DUMMY(31 downto 0);
   muxin : Mux4
      port map (sel(1 downto 0)=>sel(1 downto 0),
                X0(31 downto 0)=>O1_DUMMY(31 downto 0),
                X1(31 downto 0)=>I1(31 downto 0),
                X2(31 downto 0)=>I2(31 downto 0),
                X3(31 downto 0)=>I3(31 downto 0),
                Y(31 downto 0)=>XLXN_1(31 downto 0));
   
   shift_register : reg32_MUSER_netring
      port map (C=>clk,
                CE=>XLXN_10,
                CLR=>clr,
                D(31 downto 0)=>XLXN_1(31 downto 0),
                Q(31 downto 0)=>O1_DUMMY(31 downto 0));
   
   XLXI_3 : VCC
      port map (P=>XLXN_10);
   
end BEHAVIORAL;


