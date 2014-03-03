--------------------------------------------------------------------------------
-- Copyright (c) 1995-2010 Xilinx, Inc.  All rights reserved.
--------------------------------------------------------------------------------
--   ____  ____ 
--  /   /\/   / 
-- /___/  \  /    Vendor: Xilinx 
-- \   \   \/     Version : 12.4
--  \   \         Application : sch2hdl
--  /   /         Filename : stack.vhf
-- /___/   /\     Timestamp : 10/18/2013 11:11:41
-- \   \  /  \ 
--  \___\/\___\ 
--
--Command: sch2hdl -intstyle ise -family spartan6 -flat -suppress -vhdl /home/m1/douaille/AEV/tp2/homade/stack.vhf -w /home/m1/douaille/AEV/tp2/homade/stack.sch
--Design Name: stack
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

entity stack is
   port ( clk   : in    std_logic; 
          clr   : in    std_logic; 
          Nin   : in    std_logic_vector (31 downto 0); 
          N2in  : in    std_logic_vector (31 downto 0); 
          pop1  : in    std_logic; 
          pop2  : in    std_logic; 
          pop3  : in    std_logic; 
          push1 : in    std_logic; 
          push2 : in    std_logic; 
          push3 : in    std_logic; 
          Tin   : in    std_logic_vector (31 downto 0); 
          Top   : in    std_logic_vector (5 downto 0); 
          Nout  : out   std_logic_vector (31 downto 0); 
          N2out : out   std_logic_vector (31 downto 0); 
          Tout  : out   std_logic_vector (31 downto 0));
   attribute clock_signal : string ;
   attribute clock_signal of clk : signal is "yes";
end stack;

architecture BEHAVIORAL of stack is
   attribute BOX_TYPE   : string ;
   signal adr0         : std_logic_vector (3 downto 0);
   signal adr1         : std_logic_vector (3 downto 0);
   signal adr2         : std_logic_vector (3 downto 0);
   signal ena0         : std_logic;
   signal ena1         : std_logic;
   signal ena2         : std_logic;
   signal selin0       : std_logic_vector (1 downto 0);
   signal selin1       : std_logic_vector (1 downto 0);
   signal selin2       : std_logic_vector (1 downto 0);
   signal selin3       : std_logic_vector (1 downto 0);
   signal selout0      : std_logic_vector (1 downto 0);
   signal selout1      : std_logic_vector (1 downto 0);
   signal selout2      : std_logic_vector (1 downto 0);
   signal write_enable : std_logic;
   signal XLXN_22      : std_logic_vector (31 downto 0);
   signal XLXN_43      : std_logic_vector (31 downto 0);
   signal XLXN_44      : std_logic_vector (31 downto 0);
   signal XLXN_45      : std_logic_vector (31 downto 0);
   signal XLXN_53      : std_logic_vector (31 downto 0);
   signal XLXN_92      : std_logic_vector (31 downto 0);
   signal XLXN_96      : std_logic_vector (31 downto 0);
   signal XLXN_97      : std_logic_vector (31 downto 0);
   signal XLXN_103     : std_logic_vector (3 downto 0);
   signal XLXN_106     : std_logic;
   component enable_bank
      port ( pop1    : in    std_logic; 
             pop2    : in    std_logic; 
             pop3    : in    std_logic; 
             push1   : in    std_logic; 
             push2   : in    std_logic; 
             push3   : in    std_logic; 
             sel     : in    std_logic_vector (1 downto 0); 
             adr     : in    std_logic_vector (3 downto 0); 
             ena0    : out   std_logic; 
             ena1    : out   std_logic; 
             ena2    : out   std_logic; 
             ena3    : out   std_logic; 
             adr0    : out   std_logic_vector (3 downto 0); 
             adr1    : out   std_logic_vector (3 downto 0); 
             adr2    : out   std_logic_vector (3 downto 0); 
             adr3    : out   std_logic_vector (3 downto 0); 
             selin0  : out   std_logic_vector (1 downto 0); 
             selin1  : out   std_logic_vector (1 downto 0); 
             selin2  : out   std_logic_vector (1 downto 0); 
             selin3  : out   std_logic_vector (1 downto 0); 
             selout0 : out   std_logic_vector (1 downto 0); 
             selout1 : out   std_logic_vector (1 downto 0); 
             selout2 : out   std_logic_vector (1 downto 0));
   end component;
   
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
   
   component OR3
      port ( I0 : in    std_logic; 
             I1 : in    std_logic; 
             I2 : in    std_logic; 
             O  : out   std_logic);
   end component;
   attribute BOX_TYPE of OR3 : component is "BLACK_BOX";
   
   component Ram8
      port ( clk     : in    std_logic; 
             we      : in    std_logic; 
             en      : in    std_logic; 
             addr    : in    std_logic_vector (3 downto 0); 
             datain  : in    std_logic_vector (31 downto 0); 
             dataout : out   std_logic_vector (31 downto 0); 
             clr     : in    std_logic);
   end component;
   
begin
   enablebank : enable_bank
      port map (adr(3 downto 0)=>Top(5 downto 2),
                pop1=>pop1,
                pop2=>pop2,
                pop3=>pop3,
                push1=>push1,
                push2=>push2,
                push3=>push3,
                sel(1 downto 0)=>Top(1 downto 0),
                adr0(3 downto 0)=>adr0(3 downto 0),
                adr1(3 downto 0)=>adr1(3 downto 0),
                adr2(3 downto 0)=>adr2(3 downto 0),
                adr3(3 downto 0)=>XLXN_103(3 downto 0),
                ena0=>ena0,
                ena1=>ena1,
                ena2=>ena2,
                ena3=>XLXN_106,
                selin0(1 downto 0)=>selin0(1 downto 0),
                selin1(1 downto 0)=>selin1(1 downto 0),
                selin2(1 downto 0)=>selin2(1 downto 0),
                selin3(1 downto 0)=>selin3(1 downto 0),
                selout0(1 downto 0)=>selout0(1 downto 0),
                selout1(1 downto 0)=>selout1(1 downto 0),
                selout2(1 downto 0)=>selout2(1 downto 0));
   
   muxin0 : Mux3_32
      port map (sel(1 downto 0)=>selin0(1 downto 0),
                X0(31 downto 0)=>Tin(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_53(31 downto 0));
   
   muxin1 : Mux3_32
      port map (sel(1 downto 0)=>selin1(1 downto 0),
                X0(31 downto 0)=>Tin(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_22(31 downto 0));
   
   muxin2 : Mux3_32
      port map (sel(1 downto 0)=>selin2(1 downto 0),
                X0(31 downto 0)=>Tin(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_96(31 downto 0));
   
   muxin3 : Mux3_32
      port map (sel(1 downto 0)=>selin3(1 downto 0),
                X0(31 downto 0)=>Tin(31 downto 0),
                X1(31 downto 0)=>Nin(31 downto 0),
                X2(31 downto 0)=>N2in(31 downto 0),
                Y(31 downto 0)=>XLXN_97(31 downto 0));
   
   muxout0 : Mux4
      port map (sel(1 downto 0)=>selout0(1 downto 0),
                X0(31 downto 0)=>XLXN_43(31 downto 0),
                X1(31 downto 0)=>XLXN_44(31 downto 0),
                X2(31 downto 0)=>XLXN_45(31 downto 0),
                X3(31 downto 0)=>XLXN_92(31 downto 0),
                Y(31 downto 0)=>Tout(31 downto 0));
   
   muxout1 : Mux4
      port map (sel(1 downto 0)=>selout1(1 downto 0),
                X0(31 downto 0)=>XLXN_43(31 downto 0),
                X1(31 downto 0)=>XLXN_44(31 downto 0),
                X2(31 downto 0)=>XLXN_45(31 downto 0),
                X3(31 downto 0)=>XLXN_92(31 downto 0),
                Y(31 downto 0)=>Nout(31 downto 0));
   
   muxout2 : Mux4
      port map (sel(1 downto 0)=>selout2(1 downto 0),
                X0(31 downto 0)=>XLXN_43(31 downto 0),
                X1(31 downto 0)=>XLXN_44(31 downto 0),
                X2(31 downto 0)=>XLXN_45(31 downto 0),
                X3(31 downto 0)=>XLXN_92(31 downto 0),
                Y(31 downto 0)=>N2out(31 downto 0));
   
   Ou3 : OR3
      port map (I0=>push3,
                I1=>push2,
                I2=>push1,
                O=>write_enable);
   
   XLXI_1 : Ram8
      port map (addr(3 downto 0)=>adr0(3 downto 0),
                clk=>clk,
                clr=>clr,
                datain(31 downto 0)=>XLXN_53(31 downto 0),
                en=>ena0,
                we=>write_enable,
                dataout(31 downto 0)=>XLXN_43(31 downto 0));
   
   XLXI_2 : Ram8
      port map (addr(3 downto 0)=>adr1(3 downto 0),
                clk=>clk,
                clr=>clr,
                datain(31 downto 0)=>XLXN_22(31 downto 0),
                en=>ena1,
                we=>write_enable,
                dataout(31 downto 0)=>XLXN_44(31 downto 0));
   
   XLXI_3 : Ram8
      port map (addr(3 downto 0)=>adr2(3 downto 0),
                clk=>clk,
                clr=>clr,
                datain(31 downto 0)=>XLXN_96(31 downto 0),
                en=>ena2,
                we=>write_enable,
                dataout(31 downto 0)=>XLXN_45(31 downto 0));
   
   XLXI_4 : Ram8
      port map (addr(3 downto 0)=>XLXN_103(3 downto 0),
                clk=>clk,
                clr=>clr,
                datain(31 downto 0)=>XLXN_97(31 downto 0),
                en=>XLXN_106,
                we=>write_enable,
                dataout(31 downto 0)=>XLXN_92(31 downto 0));
   
end BEHAVIORAL;


