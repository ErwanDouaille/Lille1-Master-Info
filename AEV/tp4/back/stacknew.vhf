--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : stacknew.vhf
-- /___/   /\     Timestamp : 12/13/2013 11:38:50
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douylliez/Bureau/V4M1_new/stacknew.vhf -w /home/m1/douylliez/Bureau/V4M1_new/stacknew.sch
--Design Name: stacknew
--Device: spartan6
--Purpose:
--    This vhdl netlist is translated from an ECS schematic. It can be 
--    synthesized and simulated, but it should not be modified. 
--
----- CELL FD16CE_HXILINX_stacknew -----


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity FD16CE_HXILINX_stacknew is
port (
    Q   : out STD_LOGIC_VECTOR(15 downto 0) := (others => '0');

    C   : in STD_LOGIC;
    CE  : in STD_LOGIC;
    CLR : in STD_LOGIC;
    D   : in STD_LOGIC_VECTOR(15 downto 0)
    );
end FD16CE_HXILINX_stacknew;

architecture Behavioral of FD16CE_HXILINX_stacknew is

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

----- CELL FD8CE_HXILINX_stacknew -----


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity FD8CE_HXILINX_stacknew is
port (
    Q   : out STD_LOGIC_VECTOR(7 downto 0) := (others => '0');

    C   : in STD_LOGIC;
    CE  : in STD_LOGIC;
    CLR : in STD_LOGIC;
    D   : in STD_LOGIC_VECTOR(7 downto 0)
    );
end FD8CE_HXILINX_stacknew;

architecture Behavioral of FD8CE_HXILINX_stacknew is

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

----- CELL M2_1_HXILINX_stacknew -----
  
library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity M2_1_HXILINX_stacknew is
  
port(
    O   : out std_logic;

    D0  : in std_logic;
    D1  : in std_logic;
    S0  : in std_logic
  );
end M2_1_HXILINX_stacknew;

architecture M2_1_HXILINX_stacknew_V of M2_1_HXILINX_stacknew is
begin
  process (D0, D1, S0)
  begin
    case S0 is
    when '0' => O <= D0;
    when '1' => O <= D1;
    when others => NULL;
    end case;
    end process; 
end M2_1_HXILINX_stacknew_V;

library ieee;
use ieee.std_logic_1164.ALL;
use ieee.numeric_std.ALL;
library UNISIM;
use UNISIM.Vcomponents.ALL;

entity reg32_MUSER_stacknew is
   port ( C   : in    std_logic; 
          CE  : in    std_logic; 
          CLR : in    std_logic; 
          D   : in    std_logic_vector (31 downto 0); 
          Q   : out   std_logic_vector (31 downto 0));
end reg32_MUSER_stacknew;

architecture BEHAVIORAL of reg32_MUSER_stacknew is
   attribute HU_SET     : string ;
   component FD16CE_HXILINX_stacknew
      port ( C   : in    std_logic; 
             CE  : in    std_logic; 
             CLR : in    std_logic; 
             D   : in    std_logic_vector (15 downto 0); 
             Q   : out   std_logic_vector (15 downto 0));
   end component;
   
   attribute HU_SET of XLXI_1 : label is "XLXI_1_0";
   attribute HU_SET of XLXI_2 : label is "XLXI_2_1";
begin
   XLXI_1 : FD16CE_HXILINX_stacknew
      port map (C=>C,
                CE=>CE,
                CLR=>CLR,
                D(15 downto 0)=>D(31 downto 16),
                Q(15 downto 0)=>Q(31 downto 16));
   
   XLXI_2 : FD16CE_HXILINX_stacknew
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

entity stacknew is
   port ( clk       : in    std_logic; 
          ipdone    : in    std_logic; 
          Lit       : in    std_logic_vector (11 downto 0); 
          Litload   : in    std_logic; 
          Nin       : in    std_logic_vector (31 downto 0); 
          N2in      : in    std_logic_vector (31 downto 0); 
          reset     : in    std_logic; 
          shortIP   : in    std_logic; 
          Tin       : in    std_logic_vector (31 downto 0); 
          X         : in    std_logic_vector (1 downto 0); 
          Y         : in    std_logic_vector (1 downto 0); 
          Nout      : out   std_logic_vector (31 downto 0); 
          N2out     : out   std_logic_vector (31 downto 0); 
          offset    : out   std_logic; 
          oversized : out   std_logic_vector (31 downto 0); 
          Tout      : out   std_logic_vector (31 downto 0));
end stacknew;

architecture BEHAVIORAL of stacknew is
   attribute BOX_TYPE   : string ;
   attribute HU_SET     : string ;
   signal adr0        : std_logic_vector (3 downto 0);
   signal adr1        : std_logic_vector (3 downto 0);
   signal adr2        : std_logic_vector (3 downto 0);
   signal adr3        : std_logic_vector (3 downto 0);
   signal count       : std_logic_vector (7 downto 0);
   signal lastcycle   : std_logic;
   signal selin0      : std_logic_vector (1 downto 0);
   signal selin1      : std_logic_vector (1 downto 0);
   signal selin2      : std_logic_vector (1 downto 0);
   signal selin3      : std_logic_vector (1 downto 0);
   signal selpredicat : std_logic;
   signal selreg0     : std_logic_vector (1 downto 0);
   signal selreg1     : std_logic_vector (1 downto 0);
   signal selreg2     : std_logic_vector (1 downto 0);
   signal selreg3     : std_logic_vector (1 downto 0);
   signal Tin_lit     : std_logic_vector (31 downto 0);
   signal we0         : std_logic;
   signal we1         : std_logic;
   signal we2         : std_logic;
   signal we3         : std_logic;
   signal XLXN_15     : std_logic_vector (31 downto 0);
   signal XLXN_17     : std_logic_vector (31 downto 0);
   signal XLXN_24     : std_logic;
   signal XLXN_26     : std_logic_vector (31 downto 0);
   signal XLXN_29     : std_logic;
   signal XLXN_31     : std_logic_vector (7 downto 0);
   signal XLXN_33     : std_logic_vector (31 downto 0);
   signal XLXN_34     : std_logic_vector (31 downto 0);
   signal XLXN_38     : std_logic;
   signal XLXN_39     : std_logic_vector (31 downto 0);
   signal XLXN_40     : std_logic_vector (31 downto 0);
   signal XLXN_41     : std_logic_vector (31 downto 0);
   signal XLXN_45     : std_logic;
   signal XLXN_46     : std_logic_vector (31 downto 0);
   signal XLXN_47     : std_logic_vector (31 downto 0);
   signal XLXN_48     : std_logic_vector (31 downto 0);
   signal XLXN_52     : std_logic;
   signal XLXN_53     : std_logic_vector (31 downto 0);
   signal XLXN_71     : std_logic_vector (1 downto 0);
   signal XLXN_82     : std_logic_vector (1 downto 0);
   signal XLXN_83     : std_logic_vector (1 downto 0);
   signal XLXN_219    : std_logic_vector (31 downto 0);
   signal XLXN_223    : std_logic_vector (1 downto 0);
   signal XLXN_224    : std_logic_vector (31 downto 0);
   signal XLXN_225    : std_logic_vector (31 downto 0);
   signal XLXN_226    : std_logic_vector (31 downto 0);
   signal XLXN_227    : std_logic_vector (31 downto 0);
   signal XLXN_242    : std_logic;
   signal XLXN_243    : std_logic;
   signal Tout_DUMMY  : std_logic_vector (31 downto 0);
   component Mux3_32
      port ( X0  : in    std_logic_vector (31 downto 0); 
             X1  : in    std_logic_vector (31 downto 0); 
             X2  : in    std_logic_vector (31 downto 0); 
             sel : in    std_logic_vector (1 downto 0); 
             Y   : out   std_logic_vector (31 downto 0));
   end component;
   
   component Mux4
      port ( X0  : in    std_logic_vector (31 downto 0); 
             X1  : in    std_logic_vector (31 downto 0); 
             X2  : in    std_logic_vector (31 downto 0); 
             X3  : in    std_logic_vector (31 downto 0); 
             sel : in    std_logic_vector (1 downto 0); 
             Y   : out   std_logic_vector (31 downto 0));
   end component;
   
   component Ram8
      port ( clk     : in    std_logic; 
             we      : in    std_logic; 
             addr    : in    std_logic_vector (3 downto 0); 
             datain  : in    std_logic_vector (31 downto 0); 
             dataout : out   std_logic_vector (31 downto 0));
   end component;
   
   component reg32_MUSER_stacknew
      port ( D   : in    std_logic_vector (31 downto 0); 
             CLR : in    std_logic; 
             C   : in    std_logic; 
             CE  : in    std_logic; 
             Q   : out   std_logic_vector (31 downto 0));
   end component;
   
   component select_in
      port ( Lastcycle   : in    std_logic; 
             count       : in    std_logic_vector (7 downto 0); 
             X           : in    std_logic_vector (1 downto 0); 
             Y           : in    std_logic_vector (1 downto 0); 
             we0         : out   std_logic; 
             we1         : out   std_logic; 
             we2         : out   std_logic; 
             we3         : out   std_logic; 
             adr0        : out   std_logic_vector (3 downto 0); 
             adr1        : out   std_logic_vector (3 downto 0); 
             adr2        : out   std_logic_vector (3 downto 0); 
             adr3        : out   std_logic_vector (3 downto 0); 
             selin0      : out   std_logic_vector (1 downto 0); 
             selin1      : out   std_logic_vector (1 downto 0); 
             selin2      : out   std_logic_vector (1 downto 0); 
             selin3      : out   std_logic_vector (1 downto 0); 
             selreg0     : out   std_logic_vector (1 downto 0); 
             selreg1     : out   std_logic_vector (1 downto 0); 
             selreg2     : out   std_logic_vector (1 downto 0); 
             selreg3     : out   std_logic_vector (1 downto 0); 
             selpredicat : out   std_logic);
   end component;
   
   component select_out
      port ( count    : in    std_logic_vector (1 downto 0); 
             selTout  : out   std_logic_vector (1 downto 0); 
             selNout  : out   std_logic_vector (1 downto 0); 
             selN2out : out   std_logic_vector (1 downto 0); 
             seloverS : out   std_logic_vector (1 downto 0));
   end component;
   
   component next_count
      port ( lastcycle : in    std_logic; 
             count_in  : in    std_logic_vector (7 downto 0); 
             X         : in    std_logic_vector (1 downto 0); 
             Y         : in    std_logic_vector (1 downto 0); 
             count_out : out   std_logic_vector (7 downto 0));
   end component;
   
   component VCC
      port ( P : out   std_logic);
   end component;
   attribute BOX_TYPE of VCC : component is "BLACK_BOX";
   
   component Mux32
      port ( sel : in    std_logic; 
             a   : in    std_logic_vector (31 downto 0); 
             b   : in    std_logic_vector (31 downto 0); 
             s   : out   std_logic_vector (31 downto 0));
   end component;
   
   component constant32
      port ( value   : in    std_logic_vector (11 downto 0); 
             value32 : out   std_logic_vector (31 downto 0));
   end component;
   
   component OR3
      port ( I0 : in    std_logic; 
             I1 : in    std_logic; 
             I2 : in    std_logic; 
             O  : out   std_logic);
   end component;
   attribute BOX_TYPE of OR3 : component is "BLACK_BOX";
   
   component FD8CE_HXILINX_stacknew
      port ( C   : in    std_logic; 
             CE  : in    std_logic; 
             CLR : in    std_logic; 
             D   : in    std_logic_vector (7 downto 0); 
             Q   : out   std_logic_vector (7 downto 0));
   end component;
   
   component predicat
      port ( tin      : in    std_logic_vector (31 downto 0); 
             predicat : out   std_logic);
   end component;
   
   component M2_1_HXILINX_stacknew
      port ( D0 : in    std_logic; 
             D1 : in    std_logic; 
             S0 : in    std_logic; 
             O  : out   std_logic);
   end component;
   
   attribute HU_SET of XLXI_93 : label is "XLXI_93_2";
   attribute HU_SET of XLXI_100 : label is "XLXI_100_3";
begin
   Tout(31 downto 0) <= Tout_DUMMY(31 downto 0);
   muxi0 : Mux3_32
      port map (sel(1 downto 0)=>selin0(1 downto 0),
                X0(31 downto 0)=>Tin_lit(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_26(31 downto 0));
   
   muxi1 : Mux3_32
      port map (sel(1 downto 0)=>selin1(1 downto 0),
                X0(31 downto 0)=>Tin_lit(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_39(31 downto 0));
   
   muxi2 : Mux3_32
      port map (sel(1 downto 0)=>selin2(1 downto 0),
                X0(31 downto 0)=>Tin_lit(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_46(31 downto 0));
   
   muxi3 : Mux3_32
      port map (sel(1 downto 0)=>selin3(1 downto 0),
                X0(31 downto 0)=>Tin_lit(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_53(31 downto 0));
   
   muxoutN : Mux4
      port map (sel(1 downto 0)=>XLXN_82(1 downto 0),
                X0(31 downto 0)=>XLXN_224(31 downto 0),
                X1(31 downto 0)=>XLXN_225(31 downto 0),
                X2(31 downto 0)=>XLXN_226(31 downto 0),
                X3(31 downto 0)=>XLXN_227(31 downto 0),
                Y(31 downto 0)=>Nout(31 downto 0));
   
   muxoutN2 : Mux4
      port map (sel(1 downto 0)=>XLXN_83(1 downto 0),
                X0(31 downto 0)=>XLXN_224(31 downto 0),
                X1(31 downto 0)=>XLXN_225(31 downto 0),
                X2(31 downto 0)=>XLXN_226(31 downto 0),
                X3(31 downto 0)=>XLXN_227(31 downto 0),
                Y(31 downto 0)=>N2out(31 downto 0));
   
   muxoutT : Mux4
      port map (sel(1 downto 0)=>XLXN_71(1 downto 0),
                X0(31 downto 0)=>XLXN_224(31 downto 0),
                X1(31 downto 0)=>XLXN_225(31 downto 0),
                X2(31 downto 0)=>XLXN_226(31 downto 0),
                X3(31 downto 0)=>XLXN_227(31 downto 0),
                Y(31 downto 0)=>Tout_DUMMY(31 downto 0));
   
   muxr0 : Mux3_32
      port map (sel(1 downto 0)=>selreg0(1 downto 0),
                X0(31 downto 0)=>XLXN_224(31 downto 0),
                X1(31 downto 0)=>XLXN_26(31 downto 0),
                X2(31 downto 0)=>XLXN_17(31 downto 0),
                Y(31 downto 0)=>XLXN_15(31 downto 0));
   
   muxR1 : Mux3_32
      port map (sel(1 downto 0)=>selreg1(1 downto 0),
                X0(31 downto 0)=>XLXN_225(31 downto 0),
                X1(31 downto 0)=>XLXN_39(31 downto 0),
                X2(31 downto 0)=>XLXN_34(31 downto 0),
                Y(31 downto 0)=>XLXN_33(31 downto 0));
   
   muxr2 : Mux3_32
      port map (sel(1 downto 0)=>selreg2(1 downto 0),
                X0(31 downto 0)=>XLXN_226(31 downto 0),
                X1(31 downto 0)=>XLXN_46(31 downto 0),
                X2(31 downto 0)=>XLXN_41(31 downto 0),
                Y(31 downto 0)=>XLXN_40(31 downto 0));
   
   muxr3 : Mux3_32
      port map (sel(1 downto 0)=>selreg3(1 downto 0),
                X0(31 downto 0)=>XLXN_227(31 downto 0),
                X1(31 downto 0)=>XLXN_53(31 downto 0),
                X2(31 downto 0)=>XLXN_48(31 downto 0),
                Y(31 downto 0)=>XLXN_47(31 downto 0));
   
   ram0 : Ram8
      port map (addr(3 downto 0)=>adr0(3 downto 0),
                clk=>clk,
                datain(31 downto 0)=>XLXN_26(31 downto 0),
                we=>we0,
                dataout(31 downto 0)=>XLXN_17(31 downto 0));
   
   ram1 : Ram8
      port map (addr(3 downto 0)=>adr1(3 downto 0),
                clk=>clk,
                datain(31 downto 0)=>XLXN_39(31 downto 0),
                we=>we1,
                dataout(31 downto 0)=>XLXN_34(31 downto 0));
   
   ram2 : Ram8
      port map (addr(3 downto 0)=>adr2(3 downto 0),
                clk=>clk,
                datain(31 downto 0)=>XLXN_46(31 downto 0),
                we=>we2,
                dataout(31 downto 0)=>XLXN_41(31 downto 0));
   
   ram3 : Ram8
      port map (addr(3 downto 0)=>adr3(3 downto 0),
                clk=>clk,
                datain(31 downto 0)=>XLXN_53(31 downto 0),
                we=>we3,
                dataout(31 downto 0)=>XLXN_48(31 downto 0));
   
   r0 : reg32_MUSER_stacknew
      port map (C=>clk,
                CE=>XLXN_24,
                CLR=>reset,
                D(31 downto 0)=>XLXN_15(31 downto 0),
                Q(31 downto 0)=>XLXN_224(31 downto 0));
   
   R1 : reg32_MUSER_stacknew
      port map (C=>clk,
                CE=>XLXN_38,
                CLR=>reset,
                D(31 downto 0)=>XLXN_33(31 downto 0),
                Q(31 downto 0)=>XLXN_225(31 downto 0));
   
   R2 : reg32_MUSER_stacknew
      port map (C=>clk,
                CE=>XLXN_45,
                CLR=>reset,
                D(31 downto 0)=>XLXN_40(31 downto 0),
                Q(31 downto 0)=>XLXN_226(31 downto 0));
   
   r3 : reg32_MUSER_stacknew
      port map (C=>clk,
                CE=>XLXN_52,
                CLR=>reset,
                D(31 downto 0)=>XLXN_47(31 downto 0),
                Q(31 downto 0)=>XLXN_227(31 downto 0));
   
   sel_in : select_in
      port map (count(7 downto 0)=>count(7 downto 0),
                Lastcycle=>lastcycle,
                X(1 downto 0)=>X(1 downto 0),
                Y(1 downto 0)=>Y(1 downto 0),
                adr0(3 downto 0)=>adr0(3 downto 0),
                adr1(3 downto 0)=>adr1(3 downto 0),
                adr2(3 downto 0)=>adr2(3 downto 0),
                adr3(3 downto 0)=>adr3(3 downto 0),
                selin0(1 downto 0)=>selin0(1 downto 0),
                selin1(1 downto 0)=>selin1(1 downto 0),
                selin2(1 downto 0)=>selin2(1 downto 0),
                selin3(1 downto 0)=>selin3(1 downto 0),
                selpredicat=>selpredicat,
                selreg0(1 downto 0)=>selreg0(1 downto 0),
                selreg1(1 downto 0)=>selreg1(1 downto 0),
                selreg2(1 downto 0)=>selreg2(1 downto 0),
                selreg3(1 downto 0)=>selreg3(1 downto 0),
                we0=>we0,
                we1=>we1,
                we2=>we2,
                we3=>we3);
   
   sel_out : select_out
      port map (count(1 downto 0)=>count(1 downto 0),
                selNout(1 downto 0)=>XLXN_82(1 downto 0),
                selN2out(1 downto 0)=>XLXN_83(1 downto 0),
                seloverS(1 downto 0)=>XLXN_223(1 downto 0),
                selTout(1 downto 0)=>XLXN_71(1 downto 0));
   
   update_counter : next_count
      port map (count_in(7 downto 0)=>count(7 downto 0),
                lastcycle=>lastcycle,
                X(1 downto 0)=>X(1 downto 0),
                Y(1 downto 0)=>Y(1 downto 0),
                count_out(7 downto 0)=>XLXN_31(7 downto 0));
   
   XLXI_28 : VCC
      port map (P=>XLXN_24);
   
   XLXI_29 : VCC
      port map (P=>XLXN_29);
   
   XLXI_34 : VCC
      port map (P=>XLXN_38);
   
   XLXI_38 : VCC
      port map (P=>XLXN_45);
   
   XLXI_42 : VCC
      port map (P=>XLXN_52);
   
   XLXI_87 : Mux32
      port map (a(31 downto 0)=>Tin(31 downto 0),
                b(31 downto 0)=>XLXN_219(31 downto 0),
                sel=>Litload,
                s(31 downto 0)=>Tin_lit(31 downto 0));
   
   XLXI_89 : constant32
      port map (value(11 downto 0)=>Lit(11 downto 0),
                value32(31 downto 0)=>XLXN_219(31 downto 0));
   
   XLXI_91 : OR3
      port map (I0=>ipdone,
                I1=>shortIP,
                I2=>Litload,
                O=>lastcycle);
   
   XLXI_93 : FD8CE_HXILINX_stacknew
      port map (C=>clk,
                CE=>XLXN_29,
                CLR=>reset,
                D(7 downto 0)=>XLXN_31(7 downto 0),
                Q(7 downto 0)=>count(7 downto 0));
   
   XLXI_94 : Mux4
      port map (sel(1 downto 0)=>XLXN_223(1 downto 0),
                X0(31 downto 0)=>XLXN_224(31 downto 0),
                X1(31 downto 0)=>XLXN_225(31 downto 0),
                X2(31 downto 0)=>XLXN_226(31 downto 0),
                X3(31 downto 0)=>XLXN_227(31 downto 0),
                Y(31 downto 0)=>oversized(31 downto 0));
   
   XLXI_98 : predicat
      port map (tin(31 downto 0)=>Tout_DUMMY(31 downto 0),
                predicat=>XLXN_242);
   
   XLXI_99 : predicat
      port map (tin(31 downto 0)=>Tin_lit(31 downto 0),
                predicat=>XLXN_243);
   
   XLXI_100 : M2_1_HXILINX_stacknew
      port map (D0=>XLXN_243,
                D1=>XLXN_242,
                S0=>selpredicat,
                O=>offset);
   
end BEHAVIORAL;


