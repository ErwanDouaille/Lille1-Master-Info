----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : uart_baudClock
-- Project Name : Homade V1.5
-- Revision :     no
--                                          
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  throughput adaptator to 9600 Bit/s
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
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- Static Module to turn chip clock into a baud rate clock to be
--    used by uarts
entity uart_baudClock is
    Generic ( clock_rate : integer := 100000000;
               baud_rate : integer := 9600
            );

    Port (   in_clk : in std_logic;
             baud_clk : out std_logic;
			    clk : out std_logic
           );
end uart_baudClock;


architecture Behavioral of uart_baudClock is
signal q : std_logic_vector (1 downto 0):= "00";
signal in_clk_aux : std_logic;
begin




--
--process (in_clk)
--
--begin
--
--if in_clk'event and in_clk = '1' then
--q <= q + 1 ;
--end if ;
--
--end process ;
--
--in_clk_aux <= q(0) ;

-- The following process controls the baud clock. The signal baud_clk is 
-- pulsed high for one clock cycle at a rate 16 times of the baud rate. 
-- Clock rate and baud rate are set in the top-level VHDL-file.
baudRate_process : process(in_clk)

-- Adjust the clock rate and the baud rate:
constant clock_rate : integer := 100000000;	-- 100 MHz
constant  baud_rate : integer := 9600;		-- 9600 buad	

variable count : integer range 0 to 2047 := 0;
constant clk_divide : integer := clock_rate / (baud_rate * 16 );
begin


--	Wait for a rising clock edge
	if in_clk'event and in_clk = '1' then
		count := count + 1;
		if count = clk_divide/2 then
		   clk <= '1';
		elsif count = clk_divide then
			count := 0;
			baud_clk <= '1';
			clk <= '1';
		else
		   clk <= '0';
			baud_clk <= '0';
		end if;
	end if;
end process;
--========================================================


end Behavioral;
