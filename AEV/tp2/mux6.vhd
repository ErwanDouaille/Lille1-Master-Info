----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : mux6
-- Project Name : Homade V1.4
-- Revision :     no
--                                         
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  MUX 6
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


entity mux6 is
    Port ( sel : in  STD_LOGIC_VECTOR (2 downto 0);
           in0sel : in  STD_LOGIC_VECTOR (31 downto 0);
           in1sel : in  STD_LOGIC_VECTOR (31 downto 0);
           in2sel : in  STD_LOGIC_VECTOR (31 downto 0);
           in3sel : in  STD_LOGIC_VECTOR (31 downto 0);
           in4sel : in  STD_LOGIC_VECTOR (31 downto 0);
           in5sel : in  STD_LOGIC_VECTOR (31 downto 0);
           outsel : out  STD_LOGIC_VECTOR (31 downto 0));
end mux6;

architecture Behavioral of mux6 is

begin
--	process (sel, in0sel,in1sel,in2sel,in3sel,in4sel,in5sel)
--	begin	
--		case sel is
--			when "000" => outsel <= in0sel;
--			when "001" => outsel <= in1sel;		
--			when "010" => outsel <= in2sel;
--			when "011" => outsel <= in3sel; 
--			when "100" => outsel <= in4sel;
--			when "101" => outsel <= in5sel;
--			when others  => outsel <= (others=>'0');
--		end case	;
--	end process;
--		
		
		
		
 with sel select  
    outsel <=   
        in0sel when "000", 
        in1sel when "001" , 
        in2sel when "010" ,  
        in3sel when "011" ,  
        in4sel when "100" ,  
        in5sel when "101" ,  
        (others=>'0') when others;  
		
		
		
		
		
		
		
		
		
		
		
		
end Behavioral;	
