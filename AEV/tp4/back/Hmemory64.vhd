----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : Hmemory64
-- Project Name : Homade V2.1
-- Revision :     yes
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Hmemory unit 
-- 
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  juin 2012) jean-luc.dekeyser@lifl.fr
-- Wissem Chouchene ( revision 001,  Ocotbre 2012) wissem.chouchene@inria.fr
-- Dekeyser Jean-Luc + Wissem Chouchene ( revision 002,  Ocotbre 2012)
-- 			debug two consecutive instructions with the same type


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
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_signed.ALL;
use work.OPcodes.all;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity Hmemory64 is
    Port ( Next_inst : in  STD_LOGIC;
           StartHomade : in  STD_LOGIC;
           Startadres : in  STD_LOGIC_VECTOR (31 downto 0);
           Instr_ready : out  STD_LOGIC;
			  ortree : out std_logic;
           clr : in  STD_LOGIC;
           clk : in  STD_LOGIC;
           offset : in  STD_LOGIC;
			  write_adr : out STD_LOGIC_VECTOR (31 downto 0);
			  write_data : out STD_LOGIC_VECTOR (47 downto 0);
			  write_en, w48e : out STD_LOGIC;
           prog_adr : out  STD_LOGIC_VECTOR (31 downto 0);
           Instruction : out  STD_LOGIC_VECTOR (15 downto 0);
           Prog_instr : in  STD_LOGIC_VECTOR (63 downto 0));
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end Hmemory64;

architecture Behavioral of Hmemory64 is

type StateType is (short_instr, bzbnz, bzbnz2,long_instr);
signal etat : StateType:= short_instr;

signal stackpush, stackpop, Trld ,W48: std_logic;
signal osbusin,osbusout: std_logic_vector(9 downto 0);
signal ortreeld, ortreeload, ortreestate: std_logic_vector(0 downto 0);

signal retbus,PC_adr, PC_ret: std_logic_vector(31 downto 0):=x"00000000";








	COMPONENT reg_offset
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(9 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(9 downto 0)
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
	
	COMPONENT reg1
			generic (N : integer := 32);
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(N-1 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(N-1 downto 0)
		);
	END COMPONENT;
	
	COMPONENT rstack
	PORT(
		clk : IN std_logic;
		push : IN std_logic;
		pop : IN std_logic;
		data_in : IN std_logic_vector(31 downto 0);
		clr : IN std_logic;          
		data_out : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
--	
--	
signal wim_ins : std_logic_vector (47 downto 0) ;
---- dummy code
begin

--		osbusin<= 		Prog_instr (57 downto 48)when PC_adr( 1 downto 0 ) = "00" else
--							Prog_instr (41 downto 32)when PC_adr( 1 downto 0 ) = "01" else
--							Prog_instr (25 downto 16)when PC_adr( 1 downto 0 ) = "10" else
--							Prog_instr (9 downto 0)when PC_adr( 1 downto 0 ) = "11" else
--							 "0000000001";
		Prog_adr <= "00" & PC_adr( 31 downto 2 );
		w48e <= w48;


 flag_ortree :reg0
	generic map ( N => 1)
		port map(
                d=>ortreeload,
                load=>'1',
					 clk=>clk,
					 clr => clr,
                q=>ortreestate
					 ); 
					 
	ortree <= (not starthomade )and ortreestate(0);
ortreeload(0) <= (not starthomade )and ortreeld(0);


	Inst_returnstack: rstack PORT MAP(
		clk => clk,
		push => Stackpush,
		pop => Stackpop,
		data_in => PC_ret(31 downto 0),
		data_out => retbus(31 downto 0),
		clr => clr
	);					 

process (clk,clr,starthomade,startadres)
variable PC, PCret: std_logic_vector (31 downto 0)  ;
variable wr_adr: std_logic_vector (31 downto 0):=x"00000000"  ;	
variable inslocal: std_logic_vector (15 downto 0) ;	
variable orlocal: std_logic_vector (0 downto 0) ;	
variable wr_data: std_logic_vector (47 downto 0) := x"000000000000";	
variable Iready , stpush, stpop, wr_en: std_logic :='0';


	attribute KEEP : string;
	attribute KEEP of PCret : variable is "yes";
	attribute KEEP of PC : variable is "yes";
	attribute KEEP of wr_adr : variable is "yes";
	attribute KEEP of wr_data : variable is "yes";

begin


if  (clr = '1' ) then
			PC:=x"00000000";
etat <= short_instr;
elsif StartHomade = '1' then
			PC :=Startadres ;
etat <= short_instr;
elsif  clk'event and clk = '1' then
	Iready :='0';
	stpush:='0';
	w48<='0';
	stpop := '0';		
	wr_en :='0';
	wr_adr := x"00000000";
	wr_data := x"000000000000";
	orlocal :="0";
	
			case etat is
--********************************************************************************************************************************************			
			when short_instr =>
				case 	    PC_adr( 1 downto 0 ) is
				when  "00"  => inslocal := 		Prog_instr(63 downto 48);
				when  "01"  => inslocal :=		Prog_instr(47 downto 32); 
				when  "10"	=> inslocal :=		Prog_instr(31 downto 16); 
				when  "11"	=> inslocal :=		Prog_instr(15 downto 0) ; 
				when others	=> inslocal :=		x"0000" ;
				end case;		
--================================================================================
					if inslocal /= HLT then
							Iready :='1';
							etat <= short_instr;
----------------------------------------------------------------------------------
						if  inslocal= 	BRANCH_ABSOLUTE then
								etat <= short_instr;
								if PC(1 downto 0)= "00" then
									PC := Prog_instr ( 47 downto 16 );
								else
									PC := Prog_instr ( 31 downto 0 );
								end if;
										Iready:='0';
						-----------------------------------
						elsif inslocal = CALL then
								etat <= short_instr;
								if PC(1 downto 0)= "00" then
									if Prog_instr ( 15 downto 0 ) = x"ffff" then
										PCret := PC + 4;
										PC := Prog_instr ( 47 downto 16 );
									else
										PCret := PC(31 downto 2) & "11";
										PC := Prog_instr ( 47 downto 16 );
									end if ;
								else
									PCret := PC + 3;
									PC := Prog_instr ( 31 downto 0 );
								end if;
								Iready:='0';
								stpush:='1';
						-----------------------------------							
						elsif inslocal = RET then
								etat <= short_instr;
								pc := retbus;
								Iready:='0';
								stpop:= '1';

						-----------------------------------								
						elsif inslocal(15 downto 10) = 	BRANCH_RELATIVE then
								etat <= short_instr;
								PC := PC +  inslocal( 9 downto 0) ;--conv_integer ( inslocal( 9 downto 0) );
								Iready:='0';
						-----------------------------------								
						elsif inslocal(15 downto 10) =  BRANCH_ZERO then
								etat <= bzbnz;
--								Iready:='0';
								Iready:='1';
						-----------------------------------								
						elsif inslocal(15 downto 10) =  BRANCH_NOT_ZERO then
								etat <= bzbnz;
--								Iready:='0';
								Iready:='1';
						-----------------------------------							
						elsif inslocal = x"ffff" then
								etat <= short_instr;
								PC := PC + 1;
								Iready :='0';
								case PC(1 downto 0) is
									when "01" =>
										if Prog_instr ( 47 downto 32 ) = x"ffff" then
											PC := PC + 3 ;
										end if; 
									when "10" =>
										if Prog_instr ( 31 downto 16 ) = x"ffff" then
											PC := PC + 2 ;
										end if; 				
									when "11" =>
										if Prog_instr ( 15 downto 0 ) = x"ffff" then
											PC := PC + 1 ;
										end if; 								
									when others => Null;
								end case;
						elsif (inslocal (10) = '1' and inslocal (15) = '1' )or inslocal(15 downto 13 ) = IWAIT then   ---  long  IP  or WAIT
								etat <= long_instr;
								PC := PC + 1;
								Iready :='1';
								case PC(1 downto 0) is
									when "01" =>
										if Prog_instr ( 47 downto 32 ) = x"ffff" then
											PC := PC + 3 ;
										end if; 
									when "10" =>
										if Prog_instr ( 31 downto 16 ) = x"ffff" then
											PC := PC + 2 ;
										end if; 				
									when "11" =>
										if Prog_instr ( 15 downto 0 ) = x"ffff" then
											PC := PC + 1 ;
										end if; 								
									when others => Null;
								end case;
--===============================================================================================	
						elsif inslocal (15 downto 12) = WIM then
							etat <= short_instr;
							Iready :='0';
							wr_adr := x"00000" & inslocal( 11 downto 0);
--							wr_data := Prog_instr (47 downto 0) & RET ;
--							wr_data := Prog_instr (47 downto 0) ;
wr_data := wim_ins;
							wr_en :='1';
							w48 <= '1';
							Pc := PC + 4;
							
						else 					-- short IP ou LIT
								etat <= short_instr;
								PC := PC + 1;
								Iready :='1';
								case PC(1 downto 0) is
									when "01" =>
										if Prog_instr ( 47 downto 32 ) = x"ffff" then
											PC := PC + 3 ;
										end if; 
									when "10" =>
										if Prog_instr ( 31 downto 16 ) = x"ffff" then
											PC := PC + 2 ;
										end if; 				
									when "11" =>
										if Prog_instr ( 15 downto 0 ) = x"ffff" then
											PC := PC + 1 ;
										end if; 								
									when others => Null;
								end case;
						end if ;
					else
						orlocal:="1";
					end if;
--********************************************************************************************************************************************					
			when bzbnz =>
				etat<= bzbnz2;
--				Iready :='1';
				Iready :='0';
--********************************************************************************************************************************************
			when bzbnz2 =>
				etat <= short_instr;
				Iready :='0';
				if (inslocal(15 downto 10) =  BRANCH_ZERO and  offset = '0') or (inslocal(15 downto 10) =  BRANCH_NOT_ZERO and  offset = '1')then 
								PC := PC + inslocal( 9 downto 0)  ;--conv_integer ( inslocal( 9 downto 0) );
					else					
								PC := PC + 1;
								case PC(1 downto 0) is
									when "01" =>
										if Prog_instr ( 47 downto 32 ) = x"ffff" then
											PC := PC + 3 ;
										end if; 
									when "10" =>
										if Prog_instr ( 31 downto 16 ) = x"ffff" then
											PC := PC + 2 ;
										end if; 				
									when "11" =>
										if Prog_instr ( 15 downto 0 ) = x"ffff" then
											PC := PC + 1 ;
										end if; 								
									when others => Null;
								end case;
					end if;
--********************************************************************************************************************************************
			when long_instr =>
					Iready :='0';

					if Next_inst = '1' then
						etat <= short_instr;
--								Iready := '0';

					else 
						etat <= long_instr;
					end if;
--********************************************************************************************************************************************					
			when others => etat <= short_instr;
			end case;
--			end if;			
--		end if;
	end if;
	PC_adr <= PC;
	PC_Ret <= PCret;
	instruction<=inslocal;
	instr_ready <= Iready;
	stackpush<= stpush;
	stackpop<= stpop;	
	write_adr <= wr_adr;
	write_data <= wr_data;
	write_en <= wr_en;
	ortreeld <= orlocal;
	wim_ins <= prog_instr(47 downto 0) ;
end process;

end Behavioral;



