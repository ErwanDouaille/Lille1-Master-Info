--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : encodeur_sc.vhf
-- /___/   /\     Timestamp : 09/27/2013 12:37:53
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -sympath /home/m1/douaille/didact_sc/ipcore_dir -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douaille/didact_sc/encodeur_sc.vhf -w /home/m1/douaille/didact_sc/encodeur_sc.sch
--Design Name: encodeur_sc
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

entity encodeur_sc is
   port ( E1 : in    std_logic; 
          E2 : in    std_logic; 
          E3 : in    std_logic; 
          GS : out   std_logic; 
          O0 : out   std_logic; 
          O1 : out   std_logic);
end encodeur_sc;

architecture BEHAVIORAL of encodeur_sc is
   attribute INIT       : string ;
   attribute BOX_TYPE   : string ;
   component LUT3
      -- synopsys translate_off
      generic( INIT : bit_vector :=  x"00");
      -- synopsys translate_on
      port ( I0 : in    std_logic; 
             I1 : in    std_logic; 
             I2 : in    std_logic; 
             O  : out   std_logic);
   end component;
   attribute INIT of LUT3 : component is "00";
   attribute BOX_TYPE of LUT3 : component is "BLACK_BOX";
   
   component OR3
      port ( I0 : in    std_logic; 
             I1 : in    std_logic; 
             I2 : in    std_logic; 
             O  : out   std_logic);
   end component;
   attribute BOX_TYPE of OR3 : component is "BLACK_BOX";
   
   attribute INIT of LUT1_inst : label is "44";
   attribute INIT of LUT2_inst : label is "10";
begin
   LUT1_inst : LUT3
   -- synopsys translate_off
   generic map( INIT => x"44")
   -- synopsys translate_on
      port map (I0=>E1,
                I1=>E2,
                I2=>E3,
                O=>O0);
   
   LUT2_inst : LUT3
   -- synopsys translate_off
   generic map( INIT => x"10")
   -- synopsys translate_on
      port map (I0=>E1,
                I1=>E2,
                I2=>E3,
                O=>O1);
   
   XLXI_3 : OR3
      port map (I0=>E1,
                I1=>E2,
                I2=>E3,
                O=>GS);
   
end BEHAVIORAL;


