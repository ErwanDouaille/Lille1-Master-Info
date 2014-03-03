----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : debounce4
-- Project Name : Homade V2.1
-- Revision :     no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Debounce pushbuttons 
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
use IEEE.std_logic_1164.all;

entity debounce4 is
	  port (
		cclk, clr: in std_logic;
		inp: in std_logic_vector(4 downto 0);
		outp: out std_logic_vector(4 downto 0)
	  );
	attribute clock_signal : string;
	attribute clock_signal of cclk : signal is "yes";
end debounce4;

architecture debounce4 of debounce4 is
signal delay1, delay2, delay3: std_logic_vector(4 downto 0);
begin
    process(cclk, clr)
    begin
       	if clr = '1' then
    		delay1 <= "00000";
	   	    	delay2 <= "00000";
	   		delay3 <= "00000"; 
		elsif cclk'event and cclk='1' then
	   		delay1 <= inp;
	   		delay2 <= delay1;
	   		delay3 <= delay2;
		end if;
    end process;

    outp <= delay1 and delay2 and  delay3;
end debounce4;
