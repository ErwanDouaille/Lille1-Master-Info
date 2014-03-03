--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : msa_sc.vhf
-- /___/   /\     Timestamp : 09/27/2013 12:31:57
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -sympath /home/m1/douaille/didact_sc/ipcore_dir -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douaille/didact_sc/msa_sc.vhf -w /home/m1/douaille/didact_sc/msa_sc.sch
--Design Name: msa_sc
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

entity msa_sc is
   port ( b0         : in    std_logic; 
          b1         : in    std_logic; 
          clkin      : in    std_logic; 
          gs         : in    std_logic; 
          rst        : in    std_logic; 
          enable_del : out   std_logic; 
          etatpres   : out   std_logic_vector (2 downto 0); 
          etatsuiv   : out   std_logic_vector (2 downto 0));
end msa_sc;

architecture BEHAVIORAL of msa_sc is
   attribute BOX_TYPE   : string ;
   signal romin      : std_logic_vector (5 downto 0);
   signal romout     : std_logic_vector (3 downto 0);
   component FDC
      port ( C   : in    std_logic; 
             CLR : in    std_logic; 
             D   : in    std_logic; 
             Q   : out   std_logic);
   end component;
   attribute BOX_TYPE of FDC : component is "BLACK_BOX";
   
   component rom_msa
      port ( a   : in    std_logic_vector (5 downto 0); 
             spo : out   std_logic_vector (3 downto 0));
   end component;
   
   component BUF
      port ( I : in    std_logic; 
             O : out   std_logic);
   end component;
   attribute BOX_TYPE of BUF : component is "BLACK_BOX";
   
begin
   Q0_inst : FDC
      port map (C=>clkin,
                CLR=>rst,
                D=>romout(1),
                Q=>romin(3));
   
   Q1_inst : FDC
      port map (C=>clkin,
                CLR=>rst,
                D=>romout(2),
                Q=>romin(4));
   
   Q2_inst : FDC
      port map (C=>clkin,
                CLR=>rst,
                D=>romout(3),
                Q=>romin(5));
   
   rom_inst : rom_msa
      port map (a(5 downto 0)=>romin(5 downto 0),
                spo(3 downto 0)=>romout(3 downto 0));
   
   XLXI_10 : BUF
      port map (I=>romin(5),
                O=>etatpres(2));
   
   XLXI_11 : BUF
      port map (I=>romin(4),
                O=>etatpres(1));
   
   XLXI_12 : BUF
      port map (I=>romin(3),
                O=>etatpres(0));
   
   XLXI_13 : BUF
      port map (I=>romout(3),
                O=>etatsuiv(2));
   
   XLXI_14 : BUF
      port map (I=>romout(2),
                O=>etatsuiv(1));
   
   XLXI_15 : BUF
      port map (I=>romout(1),
                O=>etatsuiv(0));
   
   XLXI_16 : BUF
      port map (I=>romout(0),
                O=>enable_del);
   
   XLXI_17 : BUF
      port map (I=>b0,
                O=>romin(1));
   
   XLXI_18 : BUF
      port map (I=>b1,
                O=>romin(2));
   
   XLXI_19 : BUF
      port map (I=>gs,
                O=>romin(0));
   
end BEHAVIORAL;


