--------------------------------------------------------------------------------
-- Company: 
-- Engineer:
--
-- Create Date:   12:17:51 12/07/2012
-- Design Name:   
-- Module Name:   D:/Projet_Homade/hnexys3v1.5/Homade_wrap_PRAMsep/HomadeRAMsep/hsp_tb.vhd
-- Project Name:  henyx3
-- Target Device:  
-- Tool versions:  
-- Description:   
-- 
-- VHDL Test Bench Created by ISE for module: HSP
-- 
-- Dependencies:
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
--
-- Notes: 
-- This testbench has been automatically generated using types std_logic and
-- std_logic_vector for the ports of the unit under test.  Xilinx recommends
-- that these types always be used for the top-level I/O of a design in order
-- to guarantee that the testbench will bind correctly to the post-implementation 
-- simulation model.
--------------------------------------------------------------------------------
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
 
-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--USE ieee.numeric_std.ALL;
 
ENTITY hsp_tb IS
END hsp_tb;
 
ARCHITECTURE behavior OF hsp_tb IS 
 
    -- Component Declaration for the Unit Under Test (UUT)
 
    COMPONENT HSP
    PORT(
         clock : IN  std_logic;
         reset : IN  std_logic;
         BufO : OUT  std_logic_vector(31 downto 0);
         BusLed : OUT  std_logic_vector(7 downto 0);
         BusSwitch : IN  std_logic_vector(7 downto 0);
         BTwait : IN  std_logic_vector(4 downto 0);
         clkin : IN  std_logic;
         enb : IN  std_logic;
         data_WR : IN  std_logic_vector(63 downto 0);
         addr_WR : IN  std_logic_vector(25 downto 0)
        );
    END COMPONENT;
    
 
   --Inputs
   signal clock : std_logic ;
   signal reset : std_logic;
   signal BusSwitch : std_logic_vector(7 downto 0) ;
   signal BTwait : std_logic_vector(4 downto 0) ;
   signal starthcu : std_logic ;
   signal Startadr : std_logic_vector(31 downto 0) ;
   signal clkin : std_logic ;
   signal enb : std_logic;
   signal data_WR : std_logic_vector(63 downto 0);
   signal addr_WR : std_logic_vector(25 downto 0);

 	--Outputs
   signal BufO : std_logic_vector(31 downto 0);
   signal BusLed : std_logic_vector(7 downto 0);
 

 
BEGIN
 
	-- Instantiate the Unit Under Test (UUT)
   uut: HSP PORT MAP (
          clock => clock,
          reset => reset,
          BufO => BufO,
          BusLed => BusLed,
          BusSwitch => BusSwitch,
          BTwait => BTwait,
          clkin => clkin,
          enb => enb,
          data_WR => data_WR,
          addr_WR => addr_WR
        );

   -- Clock process definitions
   clock_process :process
   begin
		clock <= '0';
		wait for 5 ns;
		clock <= '1';
		wait for 5 ns;
   end process;
 
   clkin_process :process
   begin
		clkin <= '0';
		wait for 5 ns;
		clkin <= '1';
		wait for 5 ns;
   end process;
 

 					reset <= '1' , '0' after 185 ns;
--prog 1   -------------------------------------------------------------------------------------------
--				   data_WR <= x"0c0000000004ffff",x"2010a0021c00ffff" after 15 ns ; 
--               addr_WR <= "00000000000000000000000000","00000000000000000000000001" after 15 ns ;  
--					enb   <= '0','1' after 5 ns , '0' after 20 ns ; 
--prog 2   -------------------------------------------------------------------------------------------
data_WR <= 
--x"0C0000000014FFFF",
--x"0C00000000001400" after 15 ns ,
--x"0C00000000001400" after 25 ns,
--x"0C00000000001400" after 35 ns, 
--x"C8201400FFFFFFFF" after 45 ns,
--x"100000000010FFFF" after 55 ns,
--x"3001100000000010" after 65 ns,
--x"3002C8201400FFFF" after 75 ns,
--x"3003123456789ABC" after 85 ns,
--x"1000000000041C00" after 95 ns,
--x"0000000000000000" after 105 ns;

x"0C0000000024FFFF",
x"0C00000000001400" after 15 ns , 
x"200FAC0288048003" after 25 ns,
x"1400FFFFFFFFFFFF" after 35 ns, 
x"20002001F80C2002" after 45 ns,
x"C821A007B008F80C" after 55 ns,
x"C8208806A823B008" after 65 ns,
x"0BF9A000D009A000" after 75 ns,
x"1400FFFFFFFFFFFF" after 85 ns,
x"100000000008D009" after 95 ns,
x"2001C8360407FFFF" after 105 ns,
x"3001100000000010" after 115 ns,
x"0008FFFFFFFFFFFF" after 125 ns,
x"3001AC031400FFFF" after 135 ns,
x"8001FFFFFFFFFFFF" after 145 ns,
x"1000000000048801" after 155 ns,
x"A002200FA402A002" after 165 ns,
x"03E01C00FFFFFFFF" after 175 ns,
x"0000000000000000" after 185 ns;


----============================= avec - NOP WIM
--x"0C0000000018FFFF",
--x"0C00000000001400" after 15 ns ,
--x"200FAC0288048003" after 25 ns ,
--x"1400FFFFFFFFFFFF" after 35 ns ,
--x"20221400FFFFFFFF" after 45 ns ,
--x"20EE1400FFFFFFFF" after 55 ns ,
--x"100000000008D009" after 65 ns ,
--x"2001C836040A8000" after 75 ns ,
--x"3001100000000010" after 85 ns ,
--x"0008FFFFFFFFFFFF" after 95 ns ,
--x"3001100000000014" after 105 ns ,
--x"8001FFFFFFFFFFFF" after 115 ns ,
--x"1000000000048801" after 125 ns ,
--x"A002200FA402A002" after 135 ns ,
--x"03E01C00FFFFFFFF" after 145 ns ,
--x"0000000000000000" after 155 ns ;

----============================== sans NOP- WIM
--x"0C0000000018FFFF",
--x"0C00000000001400" after 15 ns ,
--x"200FAC0288048003" after 25 ns ,
--x"1400FFFFFFFFFFFF" after 35 ns ,
--x"20221400FFFFFFFF" after 45 ns ,
--x"20EE1400FFFFFFFF" after 55 ns ,
--x"100000000008D009" after 65 ns ,
--x"2001C8360407FFFF" after 75 ns ,
--x"3001100000000010" after 85 ns ,
--x"0008FFFFFFFFFFFF" after 95 ns ,
--x"3001100000000014" after 105 ns ,
--x"8001FFFFFFFFFFFF" after 115 ns ,
--x"1000000000048801" after 125 ns ,
--x"A002200FA402A002" after 135 ns ,
--x"03E01C00FFFFFFFF" after 145 ns ;

--===================================== Chrono
--x"0C000000000CFFFF",
--x"20062001C83EA401" after 15 ns ,
--x"1400FFFFFFFFFFFF" after 25 ns ,
--x"2000203CA007203C" after 35 ns ,
--x"A0078002FFFFFFFF" after 45 ns ,
--x"100000000004B008" after 55 ns ,
--x"200FC8252009C836" after 65 ns ,
--x"04042007C8200005" after 75 ns ,
--x"2001C820FFFFFFFF" after 85 ns ,
--x"8806A823B0080BE9" after 95 ns ,
--x"A0002F00C825B008" after 105 ns ,
--x"20002F00C83EC825" after 115 ns ,
--x"20002900C83EC836" after 125 ns ,
--x"04042700C8200005" after 135 ns ,
--x"2100C820FFFFFFFF" after 145 ns ,
--x"8806A823B0080BCF" after 155 ns ,
--x"A000A00003CA1C00" after 165 ns ,
--x"1C00FFFFFFFFFFFF" after 175 ns ;


--  ===================================== spmd
--x"0C000000000CFFFF",
--x"20062001C83EA401" after 15 ns ,
--x"1400FFFFFFFFFFFF" after 25 ns ,
--x"2005A1FE40016000" after 35 ns ,
------- halt
--x"8812A00285FF8812" after 45 ns ,
--x"A00285FF8812A002" after 55 ns ,
--x"85FF8812A0021C00" after 65 ns ,
--x"04042007C8200005" after 75 ns ,
--x"2001C820FFFFFFFF" after 85 ns ,
--x"8806A823B0080BE9" after 95 ns ,
--x"A0002F00C825B008" after 105 ns ,
--x"20002F00C83EC825" after 115 ns ,
--x"20002900C83EC836" after 125 ns ,
--x"04042700C8200005" after 135 ns ,
--x"2100C820FFFFFFFF" after 145 ns ,
--x"8806A823B0080BCF" after 155 ns ,
--x"A000A00003CA1C00" after 165 ns ,
--x"1C00FFFFFFFFFFFF" after 175 ns ;
--




addr_WR <= "00000000000000000000000000",
"00000000000000000000000001" after 15 ns ,
"00000000000000000000000010" after 25 ns ,
"00000000000000000000000011" after 35 ns ,
"00000000000000000000000100" after 45 ns ,
"00000000000000000000000101" after 55 ns ,
"00000000000000000000000110" after 65 ns ,
"00000000000000000000000111" after 75 ns ,
"00000000000000000000001000" after 85 ns ,
"00000000000000000000001001" after 95 ns ,
"00000000000000000000001010" after 105 ns ,
"00000000000000000000001011" after 115 ns ,
"00000000000000000000001100" after 125 ns ,
"00000000000000000000001101" after 135 ns ,
"00000000000000000000001110" after 145 ns ,
"00000000000000000000001111" after 155 ns ,
"00000000000000000000010000" after 165 ns ,
"00000000000000000000010001" after 175 ns ,
"00000000000000000000000000" after 185 ns ;

enb   <= '0','1' after 4 ns , '0' after 185 ns ; 





-- wait for 400 ns; 
--  btn(3)<='1';

 




--					BufO
--					BusLed
				BusSwitch <= "00010000";
					BTwait <= "00000" , "00010" after 400 ns ,"00000" after 450 ns,"00010" after 700 ns ;
--					starthcu						
--					Startadr 
  

 

 

 
 

END;
