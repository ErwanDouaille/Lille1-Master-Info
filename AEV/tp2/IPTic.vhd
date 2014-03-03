----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IP_Tic
-- Project Name : Homade V2.1
-- Revision :     MAP Promlem : changing discription from sequential to parallel instruction 
--                                          
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  IP TIC
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
use IEEE.std_logic_unsigned.all;
use IEEE.std_logic_arith.all;

entity IP_Tic is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           ipcode : in  STD_LOGIC_vector (10 downto 0);
           clk : in  STD_LOGIC;
			  reset : in  STD_LOGIC
			  );
			  	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end IP_Tic;


architecture Behavioral of IP_Tic is
signal qbus , busreg: std_logic_vector(31 downto 0) ;
signal rst : std_logic;



	COMPONENT Tic_counter
	PORT(
		clk : IN std_logic;
		d : IN std_logic_vector(31 downto 0); 
		rst : in std_logic;		
		q : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;

begin

	Inst_Tic_counter: Tic_counter PORT MAP(
		clk => clk,
		d => busreg,
		rst => reset,
		q => qbus
	);

busreg <= x"00000000" when  ipcode(10 downto 0)= Mycode else qbus + 1 ;
Tout <= qbus when ipcode(10 downto 0)= Mycode else (others =>'Z');

end Behavioral;

