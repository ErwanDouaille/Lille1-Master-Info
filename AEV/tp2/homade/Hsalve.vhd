----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : HSlave
-- Project Name :  Homade V4.0
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Homade slave
-- 
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  juin 2012) jean-luc.dekeyser@lifl.fr
-- 
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
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use work.IPcodes.all;

entity HSlave is
    Port 	(	clock: in std_logic;
					reset : in std_logic;
					xnum : std_logic_vector (4 downto 0);
					ynum : std_logic_vector (4 downto 0);
					put :out std_logic_vector(31 downto 0);
					get :in std_logic_vector(31 downto 0);
					put_en : out std_logic;
					starthcu: in std_logic;										
					startadr:in std_logic_vector(12 downto 0);
					ortree : out std_logic;
					bit_broadcast : in std_logic;
					we_broadcast : in std_logic;
					data_bit : in std_logic;
		         w_bit : in std_logic
					
					);
	attribute clock_signal : string;
	attribute clock_signal of clock : signal is "yes";

end HSlave;

architecture Behavioral of HSlave is
--==================================================================
type state is (Idle,Wrapp_w,Hmemory_w) ;
signal next_state : state ;
--==================================================================
signal  Instbus : std_logic_vector(63 downto 0);
signal Adrbus, Tbusld,Tbusst,Nbusld,Nbusst,N2busld,N2busst, adjbus: std_logic_vector(31 downto 0);
signal Instructionbus: std_logic_vector(15 downto 0);
signal Icode: std_logic_vector(10 downto 0);
signal Bswbus: std_logic_vector(7 downto 0);
signal   IPd,Ird,  Nxi, ofs, nclock,clk_prod:  std_logic;
signal  BufOld, BusLdld : std_logic ;
signal   IPdwt, IPdft : std_logic :='0' ;
signal flag_activity,start_signal,ortreebus ,w48b: std_logic;
signal flag_reset, actif : std_logic_vector (0 downto 0);
signal wr_eh , bus_e: std_logic;
signal wr_dh, bus_d: std_logic_vector(47 downto 0);
signal wr_ah, bus_a, start: std_logic_vector(31 downto 0);

	attribute KEEP : string;
	attribute KEEP of Ird : signal is "yes";

	COMPONENT Hmemory64
	PORT(
		Next_inst : IN std_logic;
		StartHomade : IN std_logic;
		Startadres : IN std_logic_vector(31 downto 0);
		clr : IN std_logic;
		clk : IN std_logic;
		offset : IN std_logic;
		ortree : out std_logic;
		write_adr : out STD_LOGIC_VECTOR (31 downto 0);
		write_data : out STD_LOGIC_VECTOR (47 downto 0);
		write_en , w48e: out STD_LOGIC;
		Prog_instr : IN std_logic_vector(63 downto 0);          
		Instr_ready : OUT std_logic;
		prog_adr : OUT std_logic_vector(31 downto 0);
		Instruction : OUT std_logic_vector(15 downto 0)
		);
	END COMPONENT;


--	COMPONENT slave_prom
--	PORT(
--	           clkin    : in STD_LOGIC;
----=================== Write       	    
--              enb , w48     : in STD_LOGIC;
--				  data_WR : in STD_LOGIC_VECTOR ( 47 downto 0 ) ;
--              addr_WR     : in STD_LOGIC_VECTOR ( 25 downto 0 );
----=================== Read
--       	     data_RD : out STD_LOGIC_VECTOR ( 63 downto 0 ) ;
--              addr_RD    : in STD_LOGIC_VECTOR ( 31 downto 0 )
--		);
--	END COMPONENT;



COMPONENT slavedual_prom 
    Port ( clk    : in  STD_LOGIC;
           addr_a : in  STD_LOGIC_VECTOR (11 downto 0);
           data_a : in  STD_LOGIC;
			  wphase : in std_logic;
			  
			  w48    : in std_logic;
           addr_b : in  STD_LOGIC_VECTOR (5 downto 0);
			  addr_w : in  STD_LOGIC_VECTOR (5 downto 0);
			  data_w : in STD_LOGIC_VECTOR (47 downto 0);
           data_b : out  STD_LOGIC_VECTOR (63 downto 0)
			  );
end COMPONENT;

	
		COMPONENT HSUcore
	PORT(
		Instruction : IN std_logic_vector(15 downto 0);
		Instr_ready : IN std_logic;
		clr : IN std_logic;
		clk : IN std_logic;
		IPdone : IN std_logic;
--		Ortree : IN std_logic := 'Z';
		Tin : IN std_logic_vector(31 downto 0);
		Nin : IN std_logic_vector(31 downto 0);
		N2in : IN std_logic_vector(31 downto 0);          
--		IPtrig : OUT std_logic;
		Next_inst : OUT std_logic;
		IPcode : OUT std_logic_vector(10 downto 0);
		Twait : in std_logic;
		SPMDcode : OUT std_logic_vector(12 downto 0);
		Spmdtrig : OUT std_logic;
		offset: out std_logic;
		Tout : OUT std_logic_vector(31 downto 0);
		Nout : OUT std_logic_vector(31 downto 0);
		N2out : OUT std_logic_vector(31 downto 0)
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
	--
	---------------------------
	-- all the IPs here
	----------------------------
	--
COMPONENT IP_bufO
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		IPcode : IN std_logic_vector(10 downto 0);          
		BOld : OUT std_logic
		);
	END COMPONENT;
				

	
COMPONENT IP_wait
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		clk : in STD_LOGIC;
		reset : in std_logic;
		IPcode : IN std_logic_vector(10 downto 0);
		Tin : IN std_logic_vector (31 downto 0);          
		IPdone : OUT std_logic
		);
	END COMPONENT;

	
	COMPONENT IP_fibo
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		Tin : IN std_logic_vector(31 downto 0);
		clk : IN std_logic;
		reset : in std_logic;
		IPcode : IN std_logic_vector(10 downto 0);
		Tout : OUT std_logic_vector(31 downto 0);		
		IPdone : OUT std_logic
		);
	END COMPONENT;	

COMPONENT IP_Funit
	GENERIC (Mycode : std_logic_vector (5 downto 0));
	PORT(
		Tin : IN std_logic_vector(31 downto 0);
		Nin : IN std_logic_vector(31 downto 0);
		N2in : IN std_logic_vector(31 downto 0);
		IPcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0);
		Nout : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
	
COMPONENT IP_Stack
	GENERIC (Mycode : std_logic_vector (7 downto 0));
	PORT(
		Tin : IN std_logic_vector(31 downto 0);
		Nin : IN std_logic_vector(31 downto 0);
		N2in : IN std_logic_vector(31 downto 0);
		IPcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0);
		Nout : OUT std_logic_vector(31 downto 0);
		N2out : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;

COMPONENT IP_DataStack
	GENERIC (Mycode : std_logic_vector (9 downto 0));
	PORT(
		Tin : IN std_logic_vector(31 downto 0);
		IPcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0);
		clk, clr : std_logic		);
	END COMPONENT;
	
COMPONENT IP_pushpop
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(

		IPcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0);
		Nout : OUT std_logic_vector(31 downto 0);
		N2out : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
	
COMPONENT IP_Tic
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		ipcode : IN std_logic_vector(10 downto 0);
		clk : IN std_logic;
		reset : IN std_logic;          
		Tout : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;

COMPONENT IP_get
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		fromcom : in std_logic_vector(31 downto 0);
		ipcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
	COMPONENT IP_Put
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(

		ipcode : IN std_logic_vector(10 downto 0);          
		put_ld : OUT std_logic
		);
	END COMPONENT;
		COMPONENT IP_Snum
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(

		ipcode : IN std_logic_vector(10 downto 0);          
		Tout : OUT std_logic_vector(31 downto 0);
		xnum : IN std_logic_vector ( 4 downto 0) ;
		ynum : IN std_logic_vector ( 4 downto 0) 
		);
	END COMPONENT;
	
	
signal add_wslave :   STD_LOGIC_VECTOR (11 downto 0):= "000000111111" ;
signal count  : STD_LOGIC_VECTOR (6 downto 0):= "0000000";
signal count_supp  : STD_LOGIC_VECTOR (5 downto 0):= "000001";	
	

type state_type is (idle,run);
    signal load_prom: state_type:=idle;	
	
begin

--   activity for each slave
	Actiity_reg: reg0 
		generic map (N=>1)
		PORT MAP(
		load => flag_activity,
		d=> flag_reset,
		clk => clock,
		clr => '0',
		q => actif	);
-- init addresse reset
start <= "00000000000000000" & startadr & "00" ;
start_signal <= starthcu and actif(0);
flag_activity <= we_broadcast when reset ='0' else '1' ;
flag_reset(0) <= bit_broadcast when reset ='0' else '1' ;

ortree <= ortreebus when actif(0) ='1' else '1';

-- end activity




--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================

--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--
--bus_e <= wr_eh or enb;
--bus_a <= wr_ah 		or 		"000000" & addr_WR 	;
--bus_d <= wr_dh 		or			data_WR ;
-------------------------no ---------------------------


--
--bus_e <= wr_eh ;
--bus_a <= wr_ah 	;
--bus_d <= wr_dh  ;
--
--
--clk_prod <= clock ;

--	inst_Slave_prom: slave_prom PORT MAP(
--              clkin   => clk_prod,
--				  
--              enb     => bus_e,
--				  w48=> w48b,
--				  data_WR => bus_d,
--              addr_WR => bus_a(25 downto 0) , 
--
--       	     data_RD => Instbus,
--              addr_RD => Adrbus 	
--	);			
--=============================================================
Slave_prom: slavedual_prom PORT MAP(
           clk => clock ,
			  addr_a => add_wslave,
           data_a => data_bit,
			  wphase => w_bit ,
			  w48    => W48b,
			  
           addr_b => Adrbus(5 downto 0),
			  addr_w => wr_ah (5 downto 0),
			  data_w => wr_dh,
           data_b =>Instbus

);




--
--add_wslave <= add_wslave - '1' when conv_integer (count) < 63 and w_bit = '1' and clock = '1' and clock'event  and reset = '1'
--else (count_supp & "000000")- 1 when  conv_integer (count) = 0 and w_bit = '0'  and reset = '1'
--else  "000000111111" when reset = '0';
--
--count <= count + '1' when w_bit = '1' and clock = '1' and clock'event  and reset = '1'
--else (others => '0') when conv_integer (count) = 64 and reset = '1'
--else  "0000000" when reset = '0';
--
--count_supp <= count_supp + 1 when conv_integer (count) = 63 and clock = '1' and clock'event and w_bit = '1' and reset = '1'
--else "000001"  when reset = '0';
--






--
--
--process(clock,reset)
--
--variable addr_a1 :   STD_LOGIC_VECTOR (11 downto 0):= "000000111111" ;
--variable count1  : STD_LOGIC_VECTOR (6 downto 0):= "0000000";
--variable count_supp1  : STD_LOGIC_VECTOR (5 downto 0):= "000001";
--
--
--begin
--
--
--if clock = '1' and clock'event then
--
--case load_prom is
-----====================
--when idle =>	
--
--if reset = '1'  then
--
--if w_bit = '1' then
--
--count1 := count1 + '1';
----+++++++++++++++++++++++++++++++++
--if conv_integer (count1) = 64 then 
--count_supp1 := count_supp1 + 1;
--end if ;
----==========================
--if conv_integer (count1) < 64 then
--addr_a1 := addr_a1 - 1;
--end if ;
----=----------------------
--elsif w_bit = '0' then
----***************************
--if conv_integer (count1) = 0 then 
--addr_a1 :=(count_supp1 & "000000")- 1;
--end if ;
----+++++++++++++++++++++++++++
--
--if  conv_integer (count1) = 64 then 
--
--count1 :=(others => '0') ;
--end if ;
--
--
--end if ;
--load_prom <= idle ; 
--
----elsif reset = '0' and conv_integer (count1) = 0  then
----
----load_prom <= run ; 
--end if ;
-----====================
----when run =>	
----addr_a1 := "000000111111" ;
----count1 := "0000000";
----count_supp1 := "000001";
----load_prom <= idle ;
-------====================
--when others =>
--
--addr_a1 := "000000111111" ;
--count1 := "0000000";
--count_supp1 := "000001";
--load_prom <= idle ; 
--
--end case ;
--end if ;
--
--
--
----if reset = '0'  then 
----addr_a1 := "000000111111" ;
----count1 := "0000000";
----count_supp1 := "000001";
----end if ;
--
--add_wslave <= addr_a1;
--count  <= count1;
--count_supp <= count_supp1;
--
--end process ;



--============================================================================
process(clock,reset)

variable addr_a1 :   STD_LOGIC_VECTOR (11 downto 0):= "000000111111" ;
variable count1  : STD_LOGIC_VECTOR (6 downto 0):= "0000000";
variable count_supp1  : STD_LOGIC_VECTOR (5 downto 0):= "000001";


begin


if clock = '1' and clock'event  and  reset = '1' then
	if w_bit = '1' then
		count1 := count1 + '1';
		--+++++++++++++++++++++++++++++++++
		if conv_integer (count1) = 64 then 
			count_supp1 := count_supp1 + 1;
		end if ;
		--==========================
		if conv_integer (count1) < 64 then
			addr_a1 := addr_a1 - 1;
		end if ;
		--=----------------------
	elsif w_bit = '0' then
	--***************************
		if conv_integer (count1) = 0 then 
			addr_a1 :=(count_supp1  & "000000")- 1;
		end if ;
	--+++++++++++++++++++++++++++
		if  conv_integer (count1) = 64 then 
			count1 :=(others => '0') ;
		end if ;
	end if ;
elsif reset = '0' and w_bit = '0' then 
	 addr_a1 := "000000111111" ;
	 count1  := "0000000";
	 count_supp1  := "000001";
end if ;
if reset = '0' and w_bit = '0' then 
	 addr_a1 := "000000111111" ;
	 count1  := "0000000";
	 count_supp1  := "000001";
else 
 	 add_wslave <= addr_a1;
	count  <= count1;
	count_supp <= count_supp1;
end if ;

end process ;

--=============================================================
	Inst_Hmemory64: Hmemory64 PORT MAP(
		Next_inst =>Nxi ,
		StartHomade => start_signal,
		Startadres => start,
		Instr_ready => Ird,
		clr => reset,
		clk => clock,
		offset =>ofs ,
   	ortree => ortreebus,
		write_adr => wr_ah,
		write_data => wr_dh,
		write_en =>wr_eh,
		W48e => W48b,
		prog_adr => Adrbus,
		Instruction => Instructionbus,
		Prog_instr => Instbus
	);	
--=============================================================
--=============================================================
--=============================================================
	
Slave_HSUcore: HSUcore PORT MAP(
		Instruction => Instructionbus,
		Instr_ready => Ird,
--		IPtrig => IPt,
		clr => reset,
		clk => clock,
		IPdone => IPd,

		Next_inst => Nxi,
		IPcode => Icode,
		SPMDcode => open,
		Spmdtrig => open,

		Twait =>'0',
		offset => ofs,

		Tout => TBusld,
		Nout => Nbusld,
		N2out => N2busld,
		Tin => Tbusst,
		Nin => Nbusst,
		N2in => N2busst 
	);
	--

	
	Inst_IPfibo: IP_fibo
		generic map (Mycode =>IPfibo)
		PORT MAP(
		Tin => Tbusld( 31 downto 0),
		clk => clock,
		reset => reset,
		IPcode => Icode,
		Tout => Tbusst,
		IPdone => IPdft
	);	
--

	IPd <= IPdwt  or IPdft;

Inst_IPFunit: IP_Funit
		generic map (Mycode =>IPFunit)
		PORT MAP(
		Tin => Tbusld,
		Nin => Nbusld,
		N2in => N2busld,
		IPcode => Icode,
		Tout => Tbusst,
		Nout => NbusST
	);
	
Inst_IPStack: IP_Stack
		generic map (Mycode =>IPStack)
		PORT MAP(
		Tin => Tbusld,
		Nin => Nbusld,
		N2in => N2busld,
		IPcode => Icode,
		Tout => Tbusst,
		Nout => NbusST,
		N2out => N2busST
	);
	
Inst_IPdataStack: IP_dataStack
		generic map (Mycode =>IPDataStack)
		PORT MAP(
		Tin => Tbusld,
		clk=> clock,
		clr=>reset, 
		IPcode => Icode,
		Tout => Tbusst
	);
		
Inst_IPpushpop: IP_pushpop
		generic map (Mycode =>IPNop)
		PORT MAP(

		IPcode => Icode,
		Tout => Tbusst,
		Nout => NbusST,
		N2out => N2busst
	);
	Inst_IP_Tic: IP_Tic
		generic map (Mycode =>IPTic)
		PORT MAP(
		Tout => Tbusst,
		ipcode => Icode,
		clk =>clock ,
		reset => reset 
	);
	Inst_IP_get: IP_get
		generic map (Mycode =>IPGet)
		PORT MAP(
		fromcom => Get,
		Tout => Tbusst,
		ipcode => Icode
	);
	Put <= Tbusld;
		Inst_IP_Put: IP_Put
		generic map (Mycode =>IPPut)
		PORT MAP(

		put_ld => put_en,
		ipcode => Icode
	);
		Inst_IP_Snum: IP_Snum
		generic map (Mycode =>IPSnum)
		PORT MAP(
		Tout => Tbusst,
		xnum=> xnum,
		ynum => ynum,
		ipcode => Icode
	);
	
	
	

	
	
	
	
	
	
	
	
	
	

end Behavioral;

