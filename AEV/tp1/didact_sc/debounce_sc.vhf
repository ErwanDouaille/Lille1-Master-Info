--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : debounce_sc.vhf
-- /___/   /\     Timestamp : 09/27/2013 12:37:53
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -sympath /home/m1/douaille/didact_sc/ipcore_dir -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douaille/didact_sc/debounce_sc.vhf -w /home/m1/douaille/didact_sc/debounce_sc.sch
--Design Name: debounce_sc
--Device: spartan6
--Purpose:
--    This vhdl netlist is translated from an ECS schematic. It can be 
--    synthesized and simulated, but it should not be modified. 
--

library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity debounce_sc is
   port ( clkin   : in    std_logic; 
          sig_in  : in    std_logic; 
          sig_out : out   std_logic);
end debounce_sc;

architecture BEHAVIORAL of debounce_sc is
   attribute BOX_TYPE   : string ;
   signal Q0      : std_logic;
   signal Q1      : std_logic;
   signal Q2      : std_logic;
   signal XLXN_6  : std_logic;
   component FD
      generic( INIT : bit :=  '0');
      port ( C : in    std_logic; 
             D : in    std_logic; 
             Q : out   std_logic);
   end component;
   attribute BOX_TYPE of FD : component is "BLACK_BOX";
   
   component INV
      port ( I : in    std_logic; 
             O : out   std_logic);
   end component;
   attribute BOX_TYPE of INV : component is "BLACK_BOX";
   
   component AND3
      port ( I0 : in    std_logic; 
             I1 : in    std_logic; 
             I2 : in    std_logic; 
             O  : out   std_logic);
   end component;
   attribute BOX_TYPE of AND3 : component is "BLACK_BOX";
   
begin
   Q0_inst : FD
      port map (C=>clkin,
                D=>sig_in,
                Q=>Q0);
   
   Q1_inst : FD
      port map (C=>clkin,
                D=>Q0,
                Q=>Q1);
   
   Q2_inst : FD
      port map (C=>clkin,
                D=>Q1,
                Q=>Q2);
   
   XLXI_5 : INV
      port map (I=>Q2,
                O=>XLXN_6);
   
   XLXI_6 : AND3
      port map (I0=>XLXN_6,
                I1=>Q1,
                I2=>Q0,
                O=>sig_out);
   
end BEHAVIORAL;


