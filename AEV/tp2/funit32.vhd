----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : funit32
-- Project Name : Homade V2.1
-- Revision :     no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  FC16 unit 
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
use IEEE.std_logic_unsigned.all;
use IEEE.std_logic_arith.all;

entity funit32 is
    port (
        a: in STD_LOGIC_VECTOR(31 downto 0);
	    b: in STD_LOGIC_VECTOR(31 downto 0);
	    c: in STD_LOGIC_VECTOR(31 downto 0);
        fcode: in STD_LOGIC_VECTOR(4 downto 0);
        y: out STD_LOGIC_VECTOR(31 downto 0);
	    y1: out STD_LOGIC_VECTOR(31 downto 0)
    );
end funit32;


architecture funit32_arch of funit32 is
begin
  funit_32: process(a,b,c,fcode)
  variable true, false: STD_LOGIC_VECTOR (31 downto 0);
  variable avs, bvs: signed (31 downto 0);
  variable AVector: STD_LOGIC_VECTOR (32 downto 0); 
--  variable BVector: STD_LOGIC_VECTOR (32 downto 0); 
  variable CVector: STD_LOGIC_VECTOR (32 downto 0); 
  variable yVector: STD_LOGIC_VECTOR (32 downto 0);   
  variable y1_tmp: STD_LOGIC_VECTOR (31 downto 0);  
  
  begin
--	-- true is all ones; false is all zeros
--    	for i in 0 to 31 loop
--		   true(i)  := '1';
--		   false(i) := '0';
--		   avs(i) := a(i);
--		   bvs(i) := b(i);
--    	end loop;

true  := (others=> '1');
false  := (others=> '0');
avs := signed(a);
bvs := signed(b);
    -- Variables for mul/div
    AVector := '0' & a;  
    CVector := '0' & c;  
    y1_tmp := false;
    yVector := '0' & false;
    y1 <= false;
	case fcode is  
	    when "00000" =>	-- b+a
        	   y <= b + a;
        	
      	when "00001" =>	-- b-a
        	   y <= b-a; 
        	       
      	when "00010" =>	-- 1+
        	   y <= a + 1;
		
      	when "00011" =>	-- 1-
        	   y <= a - 1; 
        	       
	    when "00100" =>	-- invert
		   	   y <= not a;
		
	    when "00101" =>	-- AND
		   	   y <= a AND b;
		
      	when "00110" =>	-- OR
		       y <= a OR b; 
		 
      	when "00111" =>	-- XOR
		       y <= a XOR b; 
		  
      	when "01000" =>	-- 2*
		   		y <= a(30 downto 0) & '0'; 
		 
      	when "01001" =>	-- U2/
		   		y <= '0' & a(31 downto 1); 
		            	           
      	when "01010" =>	-- 2/
		   		y <= a(31) & a(31 downto 1);
	
		when "01011" =>	-- RSHIFT ss par sommet
		   		y <= SHR(b,a);
 	
		when "01100" =>	-- LSHIFT
		   		y <= SHL(b,a);

		when "01101" =>  -- muladd  
  	  		
         		y <= b + a(15 downto 0) * c(15 downto 0)   ; 
 

--		when "01110" =>  -- shldc  
--	  		yVector := a & b(31);  
--	  		y1_tmp := b(30 downto 0) & '0';  
--	  		if yVector > CVector then   
--         		yVector := yVector - CVector;  
--         		y1_tmp(0) := '1';  
--	  		end if;
--			y <= yVector(31 downto 0);
--			y1 <= y1_tmp; 
			
		when "01111" =>  -- 2's complement 
			y<= ( not a )+ 1;
	  	
      	when "10000" =>	-- TRUE
		   y <= true;

		when "10001" =>	-- FALSE
		   y <= false;               

      	when "10010" =>	-- 0=
		   if a = false then
			 y <= true;
		   else
			y <= false;
		   end if;
		
      	when "10011" =>	-- 0<        
		   if a(31) = '1' then
			y <= true;
		   else
			y <= false;
		   end if;      
		       	           
        when "10100" =>	-- U>
        	if b > a then
			y <= true;
		else
			y <= false;
		end if;

       	when "10101" =>	-- U<
        	if b < a then
				y <= true;
			else
				y <= false;
			end if;
		
   		when "10110" =>	-- =
        	if b = a then
				y <= true;
			else
				y <= false;
			end if;
        	
      	when "10111" =>	-- U>=
        	if b >= a then
				y <= true;
			else
				y <= false;
			end if;
				 
      	when "11000" =>	-- U<=
        	if b <= a then
				y <= true;
			else
				y <= false;
			end if;
		            	           
        when "11001" =>	-- <>
        	if b /= a then
				y <= true;
			else
				y <= false;
			end if;
   
      	when "11010" =>	-- >
		   if bvs > avs then
			y <= true;
		   else
			y <= false;
		   end if;
			
    	when "11011" =>	-- <
		   if bvs < avs then
			 	y <= true;
		   else
				y <= false;
		   end if;
		
		when "11100" =>	-- >=
		   if bvs >= avs then
				y <= true;
		   else
				y <= false;
		   end if;      
		       	           
      	when "11101" =>	-- <=
		   if bvs <= avs then
				y <= true;
		   else
				y <= false;
		   end if; 
		  when "11110" =>    -- shitAnd
				y <= a(19 downto 0) & b(11 downto 0);
		when others =>
		   y <= false;
  	end case;       
  end process funit_32;
end funit32_arch;
