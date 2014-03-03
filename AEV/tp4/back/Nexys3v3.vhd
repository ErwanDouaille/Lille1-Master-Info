----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : Nexys3v3
-- Project Name : Homade V1.4
-- Revision :     Make common mclk and mclk25 process
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Homade Association unit
-- 
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  juin 2012) jean-luc.dekeyser@lifl.fr
-- Wissem Chouchene ( revision 001,  Ocotbre 2012) wissem.chouchene@inria.fr
-- 
-- 
-- Cecil Licence:
-- This software is a computer program whose purpose is to Implement the
-- Homade processor on Xilinx FPGA systems.
-- 
-- This software is governed by the CeCILL license under French law and
-- abiding by the rules of distribution of free software.  You can  use,
-- modify and/ or redistribute the software under the terms of the CeCILL
-- license as circulated by CEA, CNRS and INRIA at the following URL
-- "http://www.cecill.info".
-- 
-- As a counterpart to the access to the source code and  rights to copy,
-- modify and redistribute granted by the license, users are provided only
-- with a limited warranty  and the software's author,  the holder of the
-- economic rights,  and the successive licensors  have only  limited
-- liability.
--                                                                                          
-- In this respect, the user's attention is drawn to the risks associated
-- with loading,  using,  modifying and/or developing or reproducing the
-- software by the user in light of its specific status of free software,
-- that may mean  that it is complicated to manipulate,  and  that  also
-- therefore means  that it is reserved for developers  and  experienced
-- professionals having in-depth computer knowledge. Users are therefore
-- encouraged to load and test the software's suitability as regards their                                                                           
-- requirements in conditions enabling the security of their systems and/or
-- data to be ensured and,  more generally, to use and operate it in the
-- same conditions as regards security.
-- 
-- The fact that you are presently reading this means that you have had
-- knowledge of the CeCILL license and that you accept its terms.   
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

entity Nexys3v3 is
    Port ( btn   : in  STD_LOGIC_VECTOR (4 downto 0);
			  an      : out  STD_LOGIC_VECTOR (3 downto 0);
           a_to_g   : out  STD_LOGIC_VECTOR (6 downto 0);
			  sw     : in STD_LOGIC_VECTOR (7 downto 0);
			  led    : out STD_LOGIC_VECTOR (7 downto 0);
           mclk : in  STD_LOGIC;
           rx_in   :in  std_logic
			  
			  
			  
			  );
	attribute clock_signal : string;
	attribute clock_signal of mclk : signal is "yes";

end Nexys3v3;

architecture Behavioral of Nexys3v3 is

component HMaster
Port 	(			clock: in std_logic;
					reset : in std_logic;
					Out32:out std_logic_vector(15 downto 0);
					Out8:out std_logic_vector(7 downto 0);
					In8:in std_logic_vector(7 downto 0);
					InBtn : in std_logic_vector (4 downto 0);
  --   clock Wrapper ============
		
			--   Master ============
              
              enb      : in STD_LOGIC;
				  data_WR : in STD_LOGIC ;
              addr_WR     : in STD_LOGIC_VECTOR (11 downto 0);
			--   Slaves ============

              data_S             : in     STD_LOGIC;
              wphase_S           : in     STD_LOGIC
				  
					);
end component;



	COMPONENT clkdiv2
	PORT(
		mclk : IN std_logic;        
		clk25 : OUT std_logic;
		clk190 : OUT std_logic
		);
	END COMPONENT;
--=====================================================================
COMPONENT Wrapper_RAM 
    Port ( 

		  start        :out std_logic;
		  reset        :in  std_logic;
        rx_enable    :in  std_logic;
        rx_in        :in  std_logic;
--=============================================
       rxclk         : in     STD_LOGIC;  
--=============================================
		 Hclk      : in     STD_LOGIC;  
-----------------------------------------------		 
--Master
       addr_M             : out     STD_LOGIC_VECTOR (11 downto 0);
       wphase_M           : out     STD_LOGIC;
       data_M             : out     STD_LOGIC;

--Slave
       data_S             : out     STD_LOGIC;
       wphase_S           : out     STD_LOGIC
			  );
end COMPONENT;

--=====================================================================
COMPONENT x7segb 
    Port ( x         : in std_logic_vector(15 downto 0);
           cclk, clr : in std_logic;
           a_to_g    : out std_logic_vector(6 downto 0);
           an        : out std_logic_vector(3 downto 0)
		 );
		 
end COMPONENT ;
--=====================================================================		 
component ckl_gen
port
 (-- Clock in ports
  CLK_IN1           : in     std_logic;
  -- Clock out ports
  CLK_OUT1          : out    std_logic
 );
end component;
--========================================		 
	COMPONENT debounce4
	PORT(
		cclk : IN std_logic;
		clr : IN std_logic;
		inp : IN std_logic_vector(4 downto 0);          
		outp : OUT std_logic_vector(4 downto 0)
		);
	END COMPONENT;
	
	component sample is
    Port ( data : in std_logic_vector(31 downto 0);
           RS : out  STD_LOGIC;
           RW : out  STD_LOGIC;
           E : out  STD_LOGIC;
           dout : out  STD_LOGIC_VECTOR (3 downto 0);
			  clk50 : in std_logic  
			  );
	END COMPONENT;
signal Busdisplay : std_logic_vector (15 downto 0) := x"0000" ;
signal clk25 , clk190 , clk200Mhz, en, ram_clk : std_logic ;
signal btnd : std_logic_vector (4 downto 0) ;
signal swin : std_logic_vector (7 downto 0) ;
signal reset : std_logic := '1' ;--
signal data_prog : std_logic ;--
signal write_prog : std_logic_vector (11 downto 0 ) ;--
signal reste_Homad ,slave_W, slave_d: std_logic;--


type state_type is (idle,finish);
    signal next_state: state_type:=idle;
begin



--==========================================
UART_Wrapper : Wrapper_RAM  PORT MAP(
start     => reste_Homad,
reset     => '0' ,      
rx_enable => '1', 
rx_in     => rx_in,
--==========================
rxclk     => mclk, 
Hclk      => clk25,
--==========================
addr_M    => write_prog,
data_M    => data_prog,   
data_S    => slave_d,  
wphase_M  => en,  
wphase_S  => slave_W
);
--==========================


my_Master : HMaster PORT MAP(
		clock => clk25,
		reset => reset,
		Out32 => Busdisplay,
		Out8 => led,
		In8 => sw,
		InBtn => Btnd ,
		enb		=> en,
		addr_WR  => write_prog,
		data_WR	=> data_prog,
		---==============

		 data_S  => slave_d,
		 wphase_S => slave_W
	
	);

--	======================================
--=====================================================================================

--====================================================================================
--mclk_gen : clk_slower
--  port map
--   (-- Clock in ports
--    CLK_IN1_P => mclk_P,
--    CLK_IN1_N => mclk_N,
--    -- Clock out ports
--    CLK_OUT1 => mclk,
--	 CLK_OUT2 => clk200Mhz);
	 
Inst_debounce4: debounce4 PORT MAP(
		cclk => clk190,
--		cclk => mclk,   --- mode simulation du btn
		clr => reset,
		inp => btn,	
		outp => btnd
	);


Inst_clkdiv2: clkdiv2 PORT MAP(
		mclk =>  mclk,
		clk25 => clk25,
		clk190 =>clk190
	);
	

--clk_gen : ckl_gen
--port map
-- (
--  CLK_IN1  => mclk,        
--  CLK_OUT1 => clk25      
----  CLK_OUT2 => clk190        
-- );


	
--=====================================================================================
D7seg_display : x7segb
port map 
(
 x   => Busdisplay,     
 cclk => clk190,
 clr  => reset,
 a_to_g => a_to_g,  
 an     => an   
);
--====================================================================================	
	
	
reset_logic: process(mclk,reste_Homad)
begin

	-- use case statement to show the 
	-- state transistion
if mclk'event and mclk='1' then

	case next_state is
---====================
when idle =>	
reset <='1';
if reste_Homad = '0' then 
reset <='0';
next_state <= finish;  
else 
next_state <= idle;
end if ;	
---====================	
when finish =>	
reset <='0';
if reste_Homad = '1' then 
next_state <= idle;
else 
next_state <= finish;
end if ;

when others =>

next_state <= idle;

end case;

end if ;
end process;



end Behavioral;

