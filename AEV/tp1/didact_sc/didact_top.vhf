--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : didact_top.vhf
-- /___/   /\     Timestamp : 09/27/2013 12:37:53
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -sympath /home/m1/douaille/didact_sc/ipcore_dir -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douaille/didact_sc/didact_top.vhf -w /home/m1/douaille/didact_sc/didact_top.sch
--Design Name: didact_top
--Device: spartan6
--Purpose:
--    This vhdl netlist is translated from an ECS schematic. It can be 
--    synthesized and simulated, but it should not be modified. 
--
----- CELL SR8CE_HXILINX_didact_top -----


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity SR8CE_HXILINX_didact_top is
port (
    Q   : out STD_LOGIC_VECTOR(7 downto 0);
    C   : in STD_LOGIC;
    CE  : in STD_LOGIC;
    CLR : in STD_LOGIC;
    SLI : in STD_LOGIC
    );
end SR8CE_HXILINX_didact_top;

architecture Behavioral of SR8CE_HXILINX_didact_top is
signal q_tmp : std_logic_vector(7 downto 0);
begin

process(C, CLR)
begin
  if (CLR='1') then
    q_tmp <= (others => '0');
  elsif (C'event and C = '1') then
    if (CE='1') then 
      q_tmp <= ( q_tmp(6 downto 0) & SLI );
    end if;
  end if;
end process;

Q <= q_tmp;


end Behavioral;


library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity encodeur_sc_MUSER_didact_top is
   port ( E1 : in    std_logic; 
          E2 : in    std_logic; 
          E3 : in    std_logic; 
          GS : out   std_logic; 
          O0 : out   std_logic; 
          O1 : out   std_logic);
end encodeur_sc_MUSER_didact_top;

architecture BEHAVIORAL of encodeur_sc_MUSER_didact_top is
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



library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity debounce_sc_MUSER_didact_top is
   port ( clkin   : in    std_logic; 
          sig_in  : in    std_logic; 
          sig_out : out   std_logic);
end debounce_sc_MUSER_didact_top;

architecture BEHAVIORAL of debounce_sc_MUSER_didact_top is
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



library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity msa_sc_MUSER_didact_top is
   port ( b0         : in    std_logic; 
          b1         : in    std_logic; 
          clkin      : in    std_logic; 
          gs         : in    std_logic; 
          rst        : in    std_logic; 
          enable_del : out   std_logic; 
          etatpres   : out   std_logic_vector (2 downto 0); 
          etatsuiv   : out   std_logic_vector (2 downto 0));
end msa_sc_MUSER_didact_top;

architecture BEHAVIORAL of msa_sc_MUSER_didact_top is
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



library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity didact_top is
   port ( bouton1 : in    std_logic; 
          bouton2 : in    std_logic; 
          bouton3 : in    std_logic; 
          clkin   : in    std_logic; 
          rst     : in    std_logic; 
          Q_del   : out   std_logic_vector (7 downto 0));
end didact_top;

architecture BEHAVIORAL of didact_top is
   attribute HU_SET     : string ;
   signal b0         : std_logic;
   signal b1         : std_logic;
   signal clk_dcm1   : std_logic;
   signal clk2hz     : std_logic;
   signal clk2khz    : std_logic;
   signal clk16hz    : std_logic;
   signal debout1    : std_logic;
   signal debout2    : std_logic;
   signal debout3    : std_logic;
   signal enable_del : std_logic;
   signal etatpres   : std_logic_vector (2 downto 0);
   signal etatsuiv   : std_logic_vector (2 downto 0);
   signal gs         : std_logic;
   component dcm1
      port ( clk_in1  : in    std_logic; 
             reset    : in    std_logic; 
             clk_out1 : out   std_logic);
   end component;
   
   component debounce_sc_MUSER_didact_top
      port ( sig_in  : in    std_logic; 
             clkin   : in    std_logic; 
             sig_out : out   std_logic);
   end component;
   
   component diviseur_clk
      port ( clkin   : in    std_logic; 
             clk2hz  : out   std_logic; 
             clk16hz : out   std_logic; 
             clk2khz : out   std_logic);
   end component;
   
   component msa_sc_MUSER_didact_top
      port ( b0         : in    std_logic; 
             b1         : in    std_logic; 
             gs         : in    std_logic; 
             clkin      : in    std_logic; 
             rst        : in    std_logic; 
             etatpres   : out   std_logic_vector (2 downto 0); 
             etatsuiv   : out   std_logic_vector (2 downto 0); 
             enable_del : out   std_logic);
   end component;
   
   component SR8CE_HXILINX_didact_top
      port ( C   : in    std_logic; 
             CE  : in    std_logic; 
             CLR : in    std_logic; 
             SLI : in    std_logic; 
             Q   : out   std_logic_vector (7 downto 0));
   end component;
   
   component encodeur_sc_MUSER_didact_top
      port ( E3 : in    std_logic; 
             E2 : in    std_logic; 
             E1 : in    std_logic; 
             O0 : out   std_logic; 
             O1 : out   std_logic; 
             GS : out   std_logic);
   end component;
   
   attribute HU_SET of shiftreg_inst : label is "shiftreg_inst_0";
begin
   dcm_inst : dcm1
      port map (clk_in1=>clkin,
                reset=>rst,
                clk_out1=>clk_dcm1);
   
   debounce1_inst : debounce_sc_MUSER_didact_top
      port map (clkin=>clk2khz,
                sig_in=>bouton1,
                sig_out=>debout1);
   
   debounce2_inst : debounce_sc_MUSER_didact_top
      port map (clkin=>clk2khz,
                sig_in=>bouton2,
                sig_out=>debout2);
   
   debounce3_inst : debounce_sc_MUSER_didact_top
      port map (clkin=>clk2khz,
                sig_in=>bouton3,
                sig_out=>debout3);
   
   diviseur_inst : diviseur_clk
      port map (clkin=>clk_dcm1,
                clk2hz=>clk2hz,
                clk2khz=>clk2khz,
                clk16hz=>clk16hz);
   
   msa_inst : msa_sc_MUSER_didact_top
      port map (b0=>b0,
                b1=>b1,
                clkin=>clk_dcm1,
                gs=>gs,
                rst=>rst,
                enable_del=>enable_del,
                etatpres(2 downto 0)=>etatpres(2 downto 0),
                etatsuiv(2 downto 0)=>etatsuiv(2 downto 0));
   
   shiftreg_inst : SR8CE_HXILINX_didact_top
      port map (C=>clk16hz,
                CE=>enable_del,
                CLR=>rst,
                SLI=>clk2hz,
                Q(7 downto 0)=>Q_del(7 downto 0));
   
   XLXI_3 : encodeur_sc_MUSER_didact_top
      port map (E1=>debout1,
                E2=>debout2,
                E3=>debout3,
                GS=>gs,
                O0=>b0,
                O1=>b1);
   
end BEHAVIORAL;


