----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : RAM 8 controller 
-- Project Name : Homade V2.1
-- Revision :     no
--                                         
-- Target Device :  spartan 6 spartan 3
-- Tool Version  :  tested on ISE 12.4,
--                                                   
-- Description   :  RAM 8 controller
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
use IEEE.STD_LOGIC_signed.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity enable_bank is
    Port ( pop1 : in  STD_LOGIC;
           pop2 : in  STD_LOGIC;
           pop3 : in  STD_LOGIC;
           push1 : in  STD_LOGIC;
           push2 : in  STD_LOGIC;
           push3 : in  STD_LOGIC;
           sel : in  STD_LOGIC_VECTOR (1 downto 0); --- deux bits de poids faible du stack adresse
			  adr : in 	STD_LOGIC_VECTOR (3 downto 0); -- bits de poids fort du stack adresse qui pointe vers la prochaine case libre
			  adr0 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr1 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr2 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr3 : out STD_LOGIC_VECTOR (3 downto 0);
			  selin0 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin1 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin2 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin3 : out STD_LOGIC_VECTOR (1 downto 0);
			  selout0 : out STD_LOGIC_VECTOR (1 downto 0);
			  selout1 : out STD_LOGIC_VECTOR (1 downto 0);
			  selout2 : out STD_LOGIC_VECTOR (1 downto 0);			  
           ena0 : out  STD_LOGIC;
           ena1 : out  STD_LOGIC;
           ena2 : out  STD_LOGIC;
			  ena3 : out  STD_LOGIC);
end enable_bank;

architecture Behavioral of enable_bank is
signal poppush, poppush3 , poppush23: std_logic;
begin
poppush <= pop1 or pop2 or pop3 or push1 or push2 or push3;
poppush23 <=  pop2 or pop3 or push2 or push3;
poppush3 <= pop3 or push3;


ena0 <= '1' when ( poppush3='1'and sel="10") or( poppush23='1' and sel = "11" )or(poppush='1' and sel="00") 
	else '0';
ena1 <= '1' when ( poppush3='1'and sel="11") or( poppush23='1' and sel = "00" )or(poppush='1' and sel="01") 
	else '0';
ena2 <= '1' when  ( poppush3='1'and sel="00") or( poppush23='1' and sel = "01" )or(poppush='1' and sel="10") 
	else '0';
ena3 <= '1' when  ( poppush3='1'and sel="01") or( poppush23='1' and sel = "10" )or(poppush='1' and sel="11") 
	else '0';

adr0 <= adr + 1 when (poppush3='1' and sel="10") or (poppush23='1' and sel="11")
	else adr;
adr1 <= adr + 1 when (poppush3='1' and sel="11" )
	else adr;
adr2 <=  adr ;
adr3 <= adr;

selin0 <=  "00"  when (sel="10" and push3='1')    							   -- select Nin sur bank0
		else "01" when ((sel = "11" and push3='1')or (sel ="00" and push2='1'))																-- select N2in by default
		else "10" ;																											-- select Tin sur bank0					
selin1 <=  "00"  when (sel="11" and push3='1')   							  
		else "01" when ((sel = "00" and push3='1')or (sel ="01" and push2='1'))																		
		else "10" ;													
selin2 <=  "00"  when (sel="00" and push3='1') 						   
		else "01" when ((sel = "01" and push3='1')or (sel ="10" and push2='1'))																	
		else "10" ;													
selin3 <=  "00"  when (sel="01" and push3='1')   							   
		else "01" when ((sel = "10" and push3='1')or (sel ="11" and push2='1'))																		
		else "10" ;													

		
selout0 <= "00" when ((sel="10" and pop3='1') or ( sel= "11" and pop2='1') or (sel ="00" and pop1='1')) 								
		else "01"  when ((sel="11" and pop3='1') or ( sel= "00" and pop2='1') or (sel ="01" and pop1='1')) 	 -- bank1
		else "10"  when ((sel="00" and pop3='1') or ( sel= "01" and pop2='1') or (sel ="10" and pop1='1'))
		else "11" ;
selout1 <= "00" when ((sel="11" and pop3='1') or (sel ="00" and pop2='1'))										
		else "01"  when ((sel="00" and pop3='1') or (sel ="01" and pop2='1'))		 -- bank2
		else "10" when ((sel="01" and pop3='1') or (sel ="10" and pop2='1'))		
		else "11";
selout2 <= "00" when (sel="10" and pop3='1') 										
		else "01"  when (sel ="11" and pop3='1')   -- bank3
		else "10" when (sel ="00" and pop3='1')
		else "11";		



end Behavioral;

