----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IP_datastack
-- Project Name :  Homade V2.1
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Data Stack IP
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
use work.IPcodes.all;

entity IP_datastack is
		GENERIC (Mycode : std_logic_vector(9 downto 0) );
    Port ( clk : in  STD_LOGIC;
           clr : in  STD_LOGIC;
           Tin : in  STD_LOGIC_VECTOR (31 downto 0);
           Tout : out  STD_LOGIC_VECTOR (31 downto 0);
           IPcode : in  STD_LOGIC_VECTOR(10 downto 0));
			  	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end IP_datastack;

architecture Behavioral of IP_datastack is

	COMPONENT dstack
	PORT(
		clk : IN std_logic;
		push : IN std_logic;
		pop : IN std_logic;
		data_in : IN std_logic_vector(31 downto 0);
		clr : IN std_logic;          
		data_out : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;

	COMPONENT reg0
	generic (N : integer := 32);
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(N-1 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(N-1 downto 0)
		);
	END COMPONENT;	
	
signal stbus , Topbus, busreg: std_logic_vector (31 downto 0) ;
signal popld, pushld: std_logic;
begin
	DatastackTop_reg: reg0 
		generic map (N=>32)
		PORT MAP(
		load =>'1',
		d => busreg,
		clk => clk,
		clr => clr,
		q => Topbus
	);
	Inst_datastack: dstack PORT MAP(
		clk => clk,
		push => pushld,
		pop => popld,
		data_in =>Tin,
		data_out => stbus(31 downto 0),
		clr => clr
	);		

pushld <= '1' when IPcode(10 downto 0) = Mycode & IDataPush else '0';
popld <= '1' when IPcode(10 downto 0) = Mycode & IDataPop else '0';
Tout <= Topbus when IPcode(10 downto 0) = Mycode & IDataPop else (others=>'Z');


busreg <= Tin when IPcode(10 downto 0) = Mycode & IDataPush else stbus;

end Behavioral;

