----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : x7segb
-- Project Name : Homade V2.1
-- Revision :     no
--                                          
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  7 segment transcoder unit
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
use IEEE.STD_LOGIC_1164.all;
use IEEE.std_logic_unsigned.all;

entity x7segb is
    Port ( x : in std_logic_vector(15 downto 0);
           cclk, clr : in std_logic;
           a_to_g : out std_logic_vector(6 downto 0);
           an : out std_logic_vector(3 downto 0)
		 );
		 	attribute clock_signal : string;
	attribute clock_signal of cclk : signal is "yes";
end x7segb;

architecture arch_x7segb of x7segb is

	signal digit : std_logic_vector(3 downto 0);
	signal count : std_logic_vector(1 downto 0);
	signal aen: std_logic_vector(3 downto 0);

begin
  -- set aen(3:0) for leading blanks
    aen(3) <= (x(15) or x(14) or x(13) or x(12));
  aen(2) <= (x(15) or x(14) or x(13) or x(12) or x(11) or x(10) or x(9) or x(8));
  aen(1) <= (x(15) or x(14) or x(13) or x(12) or x(11) or x(10) or x(9) or x(8) or x(7) or x(6) or x(5) or x(4));
  aen(0) <= '1';  -- digit 0 always on
 
  -- 2-bit counter
  ctr2bit: process(cclk,clr)
	begin
		if(clr = '1') then
			count <= "00";
		elsif(cclk'event and cclk = '1') then
			count <= count + 1;
		end if;
	end process;

  -- MUX4
  with count select
	  digit <=  x(3 downto 0)   when "00",
			x(7 downto 4)   when "01",
			x(11 downto 8)  when "10",
			x(15 downto 12) when others;


  -- seg7dec
 with digit select  
    a_to_g <=   
        "1000000" when x"0" , 
        "1111001" when x"1" , 
        "0100100" when x"2" ,  
        "0110000" when x"3" ,  
        "0011001" when x"4" ,  
        "0010010" when x"5" ,  
        "0000010" when x"6" ,  
        "1111000" when x"7" ,  
        "0000000" when x"8" ,  
        "0010000" when x"9" ,  
        "0001000" when x"A" ,  
        "0000011" when x"B" ,  
        "1000110" when x"C" ,  
        "0100001" when x"D" ,  
        "0000110" when x"E" ,  
        "0001110" when others;  

 
   --digit select
  ancode: process(count,aen)
	begin
		if(aen(conv_integer(count)) = '1') then
			an <= (others => '1');
			an(conv_integer(count)) <= '0';
		else
			an <= "1111";
		end if;
	end process;



end arch_x7segb;
