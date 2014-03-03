----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : Wrapper_RAM
-- Project Name : Homade V1.5
-- Revision :     no
--                                          
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  UART Driver : a wrapper disign for storinf data-RAM providing from PC-USB-connection
-- 
-- 
-- Contributor(s) :
-- Wissem Chouchene ( Creation  Ocotbre 2012) wissem.chouchene@inria.fr
-- Dekeyser Jean-Luc ( ) jean-luc.dekeyser@lifl.fr
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
use IEEE.std_Logic_1164.ALL;
use IEEE.std_Logic_UNSIGNED.ALL;
use IEEE.std_Logic_ARITH.ALL;


entity Wrapper_RAM is
    Port ( 

----------------------------------------------
		  start        :out std_logic;
		  reset        :in  std_logic;
        rx_enable    :in  std_logic;
        rx_in        :in  std_logic;
--=============================================
       rxclk     : in     STD_LOGIC;  
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

end Wrapper_RAM;

architecture rom_io of Wrapper_RAM is
--===================================================================
--===================================================================
--===================================================================
    signal rx_reg,reg         :std_logic_vector (7 downto 0);
    signal rx_sample_cnt  :std_logic_vector (3 downto 0);
    signal rx_cnt        :std_logic_vector (3 downto 0):= "0000";
    signal rx_d1          :std_logic:='0';
    signal rx_d2          :std_logic:='0';
	 signal rx_ok,clk_buff       :std_logic;
    signal rx_busy ,validate:std_logic:='0';

--	 signal end_prog :   STD_LOGIC:= '1';
	 signal S_M,reload :   STD_LOGIC := '0';
--=================================================================== 
    signal data_buff,data_buff1 : std_logic_vector ( 31 downto 0 ):= (others => '0'); 
    signal var1,var11      : std_logic_vector ( 31 downto 0 ):= (others => '0'); 
    signal var,var0      : std_logic_vector ( 3 downto 0 ):= (others => '0'); 	 

--===================================================================
signal  clk,baud_clk : std_logic;
--===================================================================
--===================================================================
--===================================================================
signal next_data_add : integer range 0 to 2048:=64 ;
signal k,k1             : integer range 0 to 32:=2 ;
--===================================================================

type rec is (idle, sig_off);
signal rx_sig : rec := idle;

type control_prom is (state_00,state_01,state_02,state_03, wait_state, state_M, state_WSlave );
signal prom_state : control_prom := state_00;

type control_prom1 is (state_s00,state_s01,state_s02,state_s03, state_s1 , state_S2, fin, load_file);
signal prom_state1 : control_prom1 := state_s00;

--=====================================================================================
--=====================================================================================
component uart_baudClock is

    Port (      in_clk : in std_logic;
             baud_clk : out std_logic;
			    clk : out std_logic
         );
end component;
--=====================================================================================

--==============================
--==============================
begin
--==============================
--==============================
--=====================================================================================

--=====================================================================================
    -- UART RX Logic
    process (baud_clk, reset) begin
        if (reset = '1') then
       --     rx_reg        <= (others=>'0');
        --    rx_data       <= (others=>'0');
            rx_sample_cnt <= (others=>'0');
            rx_cnt        <= (others=>'0');

            rx_d1         <= '1';
            rx_d2         <= '1';
            rx_busy       <= '0';

        elsif (rising_edge(baud_clk)) then
            -- Synchronize the asynch signal
            rx_d1 <= rx_in;
            rx_d2 <= rx_d1;
				rx_ok <= '0';
            -- Uload the rx data

            -- Receive data only when rx is enabled
            if (rx_enable = '1') then
                -- Check if just received start of frame
                if (rx_busy = '0' and rx_d2 = '0') then
                    rx_busy       <= '1';
                    rx_sample_cnt <= X"1";
                    rx_cnt        <= X"0";
                end if;
                -- Start of frame detected, Proceed with rest of data
                if (rx_busy = '1') then
                    rx_sample_cnt <= rx_sample_cnt + 1;
                    -- Logic to sample at middle of data
                    if (rx_sample_cnt = 7) then
                        if ((rx_d2 = '1') and (rx_cnt = 0)) then
                            rx_busy <= '0';
                        else
                            rx_cnt <= rx_cnt + 1;
                            -- Start storing the rx data
                            if (rx_cnt > 0 and rx_cnt < 9) then
                                reg(conv_integer(rx_cnt) - 1) <= rx_d2;
										  -- to prom 
																	  
                            end if;
                            if (rx_cnt = 9) then
									 rx_ok <= '1';
                                rx_busy <= '0';
										 
										  rx_reg<=reg;
										  rx_cnt        <= X"0";
                                -- Check if End of frame received correctly
                                
                            end if;
                        end if;
                    end if;
                end if;
            end if;
            if (rx_enable = '0') then
                rx_busy <= '0';
            end if;
        end if;
    end process;
--====================================================================================================================

uart_baudClock_inst : uart_baudClock

  port map (      in_clk => rxclk,
               baud_clk => baud_clk,
					clk => clk
           );
--baud_clk <= rxclk;
--====================================================================================================================

process (reset,Hclk,validate) 
variable j : integer range 0 to 8 := 0 ;
variable instruction : integer range 0 to 4 := 0 ;

begin 

if reset = '1' then
instruction := 0 ;
prom_state <= state_00 ;
S_M <= '0';
j := 0;
k <= 2;
next_data_add <= 64;
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';
var <= (others =>'0');
var1<= (others =>'0');
wphase_M <= '0';


elsif (rising_edge(Hclk)) then

--============================================================================
--                                     Master
--============================================================================

case prom_state is 

when state_00 =>
S_M <= '0';	
wphase_M <= '0';
next_data_add <= 64;	
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';

if  validate = '1' and  s_m = '0'  then
                
					 data_buff(31 downto 24) <= rx_reg	;
					 
					  prom_state <= state_01 ;
					  else
					   prom_state <= state_00 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_01 =>
S_M <= '0';	
wphase_M <= '0';
next_data_add <= 64;	
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';

if  validate = '1' and  s_m = '0'  then

					 data_buff(23 downto 16) <= rx_reg	;
					 
					   prom_state <= state_02 ;
					  else
					   prom_state <= state_01 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_02 =>
S_M <= '0';	
wphase_M <= '0';
next_data_add <= 64;	
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';

if  validate = '1' and  s_m = '0'  then

					 data_buff(15 downto 8) <= rx_reg	;
					 
					   prom_state <= state_03 ;
					  else
					   prom_state <= state_02 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_03 =>
S_M <= '0';	
wphase_M <= '0';
next_data_add <= 64;	
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';

if  validate = '1' and  s_m = '0'  then

					 data_buff(7 downto 0) <= rx_reg	;
					 
					  prom_state <= wait_state ;
					  k<= 2 ;
					  j:= 0 ;
					  var <= (others =>'0');
					  var1<= (others =>'0');
					  next_data_add <= 64;
					  wphase_M <= '0';

					  else
					   prom_state <= state_03 ;
					  end if ;
--=============================================================================

--===========================================================================
----*************************************************************************
--===========================================================================
----*************************************************************************
when wait_state =>
if validate = '1' and S_M = '0' then

--next_data_add <= (64*k);
wphase_M <= '1';
next_data_add <= next_data_add - 1 ;
addr_M <= conv_std_logic_vector(next_data_add,12) - 1 ;

data_M <= rx_reg(7-j);
prom_state <= state_M ;
else 
prom_state <=wait_state;
------------------------------------------------------------------
------------------------------------------------------------------
end if ;



if conv_integer(var1) = conv_integer(data_buff) then 
data_M <= '0';
wphase_M <= '0';
prom_state <= state_WSlave;

end if ;
--===========================================================================
----*************************************************************************
--===========================================================================
----*************************************************************************
--===========================================================================
----*************************************************************************
when state_M =>

-----------------------------------
if conv_integer(var1) < conv_integer(data_buff)  then 	
j := j + 1 ;
var <= var + '1' ;

--===================
if conv_integer(var) = 15 then
var1 <= var1 + 1 ;
var <= (others =>'0');
---------------------
if  instruction =  3 then
k<=k+1 ;
next_data_add <= (64*k);
instruction := 0 ;
else 
instruction := instruction +1; 
end if ;
end if ;
--===================

if j < 8 then 
wphase_M <= '1';
data_M <= rx_reg(7-j);
next_data_add <= next_data_add - 1 ;
addr_M <= conv_std_logic_vector(next_data_add,12) - 1 ;
prom_state <= state_M ;

else 

prom_state <= wait_state ;
j := 0;
wphase_M <= '0';
data_M <= '0';
end if ;
end if ;
--===========================================================================
----*************************************************************************
when state_WSlave =>

instruction := 0 ;
prom_state <= state_00 ;
j := 0;
k <= 2;
next_data_add <= 64;
addr_M <= conv_std_logic_vector(next_data_add,12);
data_M <= '0';
var <= (others =>'0');
var1<= (others =>'0');
wphase_M <= '0';

S_M <= '1';
if reload = '1' then 
S_M <= '0';
 prom_state <= state_00;
else 

prom_state <= state_WSlave;

end if ;
--============================================================================
--============================================================================
when others => 
 prom_state <= state_00 ;

addr_M <= (others => '0');
next_data_add <= 64;
j := 0 ;
k <= 0 ;
wphase_M <= '0';
data_M <= '0';

end case ;					 
end if ;
end process ;

--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
--============================================================================
----==========================================================================
process (reset,Hclk,validate) 
variable j1 : integer range 0 to 8 := 0 ;
variable instruction1 : integer range 0 to 4 := 0 ;
variable end_prog : std_logic := '1' ;
begin 

if reset = '1' then
var0 <= x"0";
instruction1 := 0 ;
var11<= x"00000000";
prom_state1 <= state_S00 ;
data_buff1<= x"00000000";
j1 := 0;
k1 <= 2;
wphase_s <= '0';
data_s <= '0';
end_prog := '1' ;
elsif (rising_edge(Hclk)) then
case prom_state1 is 
--============================================================================
--                                     SLAVE
--============================================================================
--============================================================================
--state_S0, state_s1 ,next_instruction_Slave, state_S2, fin

when state_s00 =>	
wphase_s <= '0';
data_s <= '0';
reload <= '0' ;


if  validate = '1' and  s_m = '1'  then
reload <= '0' ;                
					 data_buff1(31 downto 24) <= rx_reg	;
					 
					  prom_state1 <= state_s01 ;
					  else
					   prom_state1 <= state_s00 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_s01 =>
reload <= '0' ;
wphase_s <= '0';
data_s <= '0';

if  validate = '1' and  s_m = '1'  then

					 data_buff1(23 downto 16) <= rx_reg	;
					 
					   prom_state1 <= state_s02 ;
					  else
					   prom_state1 <= state_s01 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_s02 =>
reload <= '0' ;	
wphase_s <= '0';
data_s <= '0';

if  validate = '1' and  s_m = '1'  then

					 data_buff1(15 downto 8) <= rx_reg	;
					 
					   prom_state1 <= state_s03 ;
					  else
					   prom_state1 <= state_s02 ;
					  end if ;
--=============================================================================
--=============================================================================
when state_s03 =>
reload <= '0' ;	
wphase_s <= '0';
data_s <= '0';

if  validate = '1' and  s_m = '1'  then

					 data_buff1( 7 downto 0) <= rx_reg	;
					 
					  prom_state1 <= state_s1 ;
					  k1<= 2 ;
					  j1:= 0 ;
					  var0 <= (others =>'0');
					  var11<= (others =>'0');
					  wphase_s <= '0';

					  else
					   prom_state1 <= state_s03 ;
					  end if ;
--=============================================================================

----*************************************************************************
--===========================================================================
----*************************************************************************
when state_s1 =>
reload <= '0' ;
if  validate = '1'  and  S_M = '1' then

wphase_S <= '1';

data_S <= rx_reg(7-j1);
prom_state1 <= state_s2 ;

else 

 prom_state1 <= state_s1 ;

end if ;
------------------------------------------------------------------
------------------------------------------------------------------
if conv_integer(var11) = conv_integer(data_buff1) then 
wphase_S <= '0';
data_S <= '0';

prom_state1 <= fin;
end if ;
--===========================================================================
----*************************************************************************
when state_s2 =>
reload <= '0' ;
-----------------------------------
if conv_integer(var11) < conv_integer(data_buff1) then 	

var0 <= var0 + 1 ;
j1 := j1 + 1 ; 

--===================
if conv_integer(var0) = 15 then
var11 <= var11 + 1 ;
var0 <= (others =>'0');
---------------------
if  instruction1 =  3 then
k1<=k1+1 ;

instruction1 := 0 ;
else 
instruction1 := instruction1 +1; 
end if ;
end if ;
--===================


if j1 < 8 then 
wphase_S <= '1';
data_S <= rx_reg(7-j1);
prom_state1 <= state_s2 ;						  			  
else
prom_state1 <= state_s1 ;
j1 := 0;
wphase_S <= '0';
data_S <= '0';
end if ;
end if ;

--===========================================================================
----*************************************************************************
when fin=>
end_prog := '0' ;
reload <= '0' ;
if rx_ok = '0' then
prom_state1 <= load_file;

var0 <= x"0";
instruction1 := 0 ;
var11<= x"00000000";
data_buff1<= x"00000000";
j1 := 0;
k1 <= 2;
wphase_s <= '0';
data_s <= '0';

else
prom_state1 <= fin;

end if ;

--===========================================================================
----*************************************************************************
when load_file =>

end_prog := '0' ;
if rx_busy = '1' then 
end_prog := '1' ;
prom_state1 <=  state_s00 ;
reload <= '1' ;
else

prom_state1 <= load_file ;
end if; 
--===========================================================================
when others => 
prom_state1 <= state_s00 ;
j1 := 0 ;
k1 <= 2 ;
wphase_S <= '0';
data_S <= '0';
end case ;					 
end if ;
start <= end_prog;
end process ; 
--============================================================================
--============================================================================
--============================================================================
process (Hclk,rx_ok) 

begin 

if (rising_edge(Hclk)) then

case rx_sig is 
when idle =>

if rx_ok = '1' then 
validate <= '1' ; 
rx_sig <= sig_off;
end if ;
--==================
when sig_off =>

validate <= '0' ; 

if rx_ok = '0'   then 

rx_sig <= idle;
end if ;
--==================
when others =>
validate <= '0' ;
end case ;
end if ;

end process;
--============================================================================

--============================================================================
--============================================================================







end rom_io;
