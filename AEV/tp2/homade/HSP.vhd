----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : HSP
-- Project Name :  Homade V2.1
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  HSP unit
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
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use work.IPcodes.all;

library unisim;
use unisim.vcomponents.all;

entity HMaster is
    Port 	(	clock: in std_logic;
					reset : in std_logic;
					Out32 :out std_logic_vector(15 downto 0);
					Out8:out std_logic_vector(7 downto 0);
					In8:in std_logic_vector(7 downto 0);
					InBtn : in std_logic_vector (4 downto 0);
  --   clock Wrapper ============
			--   Master ============
              
              enb      : in STD_LOGIC;
				  data_WR : in STD_LOGIC ;
              addr_WR     : in STD_LOGIC_VECTOR (11 downto 0);
			--   Slaves ============
              data_S             : in     STD_LOGIC;
              wphase_S           : in     STD_LOGIC
					);
	attribute clock_signal : string;
	attribute clock_signal of clock : signal is "yes";

end HMaster;

architecture Behavioral of HMaster is
--==================================================================
type state is (Idle,Wrapp_w,Hmemory_w) ;
signal next_state : state ;
--==================================================================
signal  Instbus : std_logic_vector(63 downto 0);
signal Adrbus, Tbusld,Tbusst,Nbusld,Nbusst,N2busld,N2busst, adjbus: std_logic_vector(31 downto 0) := (others => '0');
signal Instructionbus: std_logic_vector(15 downto 0);
signal Icode: std_logic_vector(10 downto 0);
signal Bswbus: std_logic_vector(7 downto 0);
signal   IPd,Ird,  Nxi, ofs, nclock,clk_prod, ort, we_bcast,we_bit:  std_logic;
signal  BufOld, BusLdld ,shift_cmd_1,shift_cmd_32,ld_reg0: std_logic ;
signal  IPdbt, IPdwt, IPdft, IPdC32 : std_logic ;
signal wr_eh, bus_d , bus_e, write_net_en, ld_actif, W48b: std_logic;
signal wr_dh: std_logic_vector(47 downto 0);
signal wr_ah, bus_a, dbuslink0,dbus0: std_logic_vector(31 downto 0);

signal spmdt : std_logic;
signal spmdc : std_logic_vector (12 downto 0) ;

--	attribute KEEP : string;
--	attribute KEEP of Ird : signal is "yes";

----- salve
constant NBSLAVE : integer := 2;


--   32 bit max sur le ring
signal data_bcast,reset_aux : std_logic_vector ( NBSLAVE-1 downto 0);
signal bit_bcast : std_logic_vector ( 31 downto 0);
-- de 32 a 64  sur le ring  consomme sommet et ss sommet
--signal bit_bcast : std_logic_vector ( 63 downto 0);

signal shift_cmd : std_logic ;
type std_link is  array (0 to NBSLAVE -1) of STD_Logic;
signal Dlink , Qlink, Ld_reg_link, ort_link: std_link;
type std_vector_link is array (0 to NBSLAVE -1) of STD_Logic_vector (31 downto 0) ;
signal D_bus_link , Q_bus_link : std_vector_link;
component HSlave
Port 	(			clock: in std_logic;
					xnum : std_logic_vector (4 downto 0);
					ynum : std_logic_vector (4 downto 0);
					reset : in std_logic;
					put:out std_logic_vector(31 downto 0);
					put_en : out std_logic;
					get:in std_logic_vector(31 downto 0);
					starthcu: in std_logic;										
					startadr:in std_logic_vector(12 downto 0);
					ortree : out std_logic;
					bit_broadcast : in std_logic;
					we_broadcast : in std_logic;
					data_bit : in std_logic;
		         w_bit : in std_logic
					--============
					);
end component;

	COMPONENT shift_reg
	PORT(
		D : IN std_logic;
		d_bus : IN std_logic_vector(31 downto 0);
		ld_reg : IN std_logic;
		clk : IN std_logic;
		shift_en : IN std_logic;          
		Q : OUT std_logic;
		q_bus : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;

------
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
		write_en ,w48e: out STD_LOGIC;
		Prog_instr : IN std_logic_vector(63 downto 0);          
		Instr_ready : OUT std_logic;
		prog_adr : OUT std_logic_vector(31 downto 0);
		Instruction : OUT std_logic_vector(15 downto 0)
		);
	END COMPONENT;


--	COMPONENT sync_prom
--	PORT(
--	           clkin    : in STD_LOGIC;
----=================== Write       	    
--              enb , w48     : in STD_LOGIC;
--				  data_WR : in STD_LOGIC_VECTOR ( 63 downto 0 ) ;
--              addr_WR     : in STD_LOGIC_VECTOR ( 25 downto 0 );
----=================== Read
--       	     data_RD : out STD_LOGIC_VECTOR ( 63 downto 0 ) ;
--              addr_RD    : in STD_LOGIC_VECTOR ( 31 downto 0 )
--		);
--	END COMPONENT;

--================================================================
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

--================================================================
	
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
	generic (N : integer := 16);
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
COMPONENT IP_actif
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		IPcode : IN std_logic_vector(10 downto 0);          
		load_actif: OUT std_logic
		);
	END COMPONENT;
	
COMPONENT IP_Led
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		IPcode : IN std_logic_vector(10 downto 0);          
		BusLedld : OUT std_logic
		);
	END COMPONENT;
	
COMPONENT	IP_switch 
	GENERIC (Mycode : std_logic_vector (10 downto 0));
   Port ( 
		Bsw : in  STD_LOGIC_VECTOR (7 downto 0);
      IPcode : in  STD_LOGIC_VECTOR (10 downto 0);
		Tout : out  STD_LOGIC_VECTOR (31 downto 0));
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

	COMPONENT IP_waitBt
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		Tin : IN std_logic_vector(4 downto 0);
		clk : IN std_logic;
		reset : in std_logic;
		IPcode : IN std_logic_vector(10 downto 0);
		Btn : IN std_logic_vector(4 downto 0); 
		Tout : OUT std_logic_vector(31 downto 0);		
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

COMPONENT IP_Com
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		ipcode : IN std_logic_vector(10 downto 0);
          
		Shift_en : OUT std_logic
		);
	END COMPONENT;
	
	COMPONENT IP_Com32
	GENERIC (Mycode : std_logic_vector (10 downto 0));
	PORT(
		ipcode : IN std_logic_vector(10 downto 0);
      clk : std_logic;
		reset : in std_logic;		
		Shift_en : OUT std_logic;
		IPdone : out std_logic
		);
	END COMPONENT;
		COMPONENT IP_MEcom
	GENERIC (Mycode : std_logic_vector (9 downto 0));
	PORT(
		ipcode : IN std_logic_vector(10 downto 0);
		Tin: in std_logic_vector (31 downto 0);
		Tout: out std_logic_vector (31 downto 0);
		Qbus: in std_logic_vector (31 downto 0);
		Dbus: out std_logic_vector (31 downto 0);
		write_net : OUT std_logic
		);
	END COMPONENT;
	signal ortbus : std_logic_vector ( NBSLAVE-1 downto 0) ;

--
--	
--signal add_wslave :   STD_LOGIC_VECTOR (11 downto 0):= "000000111111" ;
--signal count  : STD_LOGIC_VECTOR (6 downto 0):= "0000000";
--signal count_supp  : STD_LOGIC_VECTOR (5 downto 0):= "000001";	
--	
	

begin
-- Master PROM

--
----
--bus_e <= wr_eh or enb;
--bus_a <= wr_ah 		or 		"000000" & addr_WR 	;
--bus_d <= wr_dh 		or			data_WR ;
--clk_prod <= clkin ;
--
--
--
--
--	Inst_sync_prom: sync_prom PORT MAP(
--              clkin   => clk_prod,
--				  
--              enb     => bus_e,
--				  w48 => w48b,
--				  data_WR => bus_d,
--              addr_WR => bus_a(25 downto 0) , 
--
--       	     data_RD => Instbus,
--              addr_RD => Adrbus 	
--	);		
--=========================================================	
Inst_sync_prom: slavedual_prom Port map( 
	        clk    => clock,
           addr_a =>  addr_WR,
           data_a => data_WR,
			  wphase => enb,
			  
			  w48    => w48b,
           addr_b => Adrbus(5 downto 0),
			  addr_w =>  wr_ah(5 downto 0),
			  data_w => wr_dh,
           data_b => Instbus
			  );








------------------------------------------------------------	
	----
	Inst_Hmemory64: Hmemory64 PORT MAP(
		Next_inst =>Nxi ,
		StartHomade => '0',
		Startadres => x"00000000",
		Instr_ready => Ird,
		clr => reset,
		clk => clock,
		offset =>ofs ,
		w48e=> w48b,
		ortree=> open,
		write_adr => wr_ah,
		write_data => wr_dh,
		write_en =>wr_eh,
		prog_adr => Adrbus,
		Instruction => Instructionbus,
		Prog_instr => Instbus
	);	
	
--=========================================================	
---- slaves
--=========================================================	
slaves : for I in 0 to NBSLAVE-1 generate	
--signal xSnum : std_logic_vector (4 downto 0) := conv_std_logic_vector( I, 5); ---- 32 slave max for a ring ...
--signal ySnum : std_logic_vector (4 downto 0) := "00000";  ---- 32 slave max for a ring ...

begin

	One_salve : HSlave PORT MAP(
		clock => clock,
		xnum => conv_std_logic_vector( i, 5),
		ynum => "00000",
		reset => reset_aux(i),
		put => d_bus_link (i),
		put_en =>ld_reg_link(i),
		get => q_bus_link(i),
		starthcu =>  spmdt,
		startadr => spmdc,
		ortree => ort_link(i),
		
		bit_broadcast => bit_bcast(i),
		data_bit => data_bcast(i),
		w_bit =>we_bit,
		we_broadcast => we_bcast
		--============

	);
end generate slaves;	

--
--data_conn : for I in 0 to NBSLAVE-1 generate	
--data_bcast (i) <= data_S;
--end generate data_conn;	
	
	
	
	
	
	
ortree:	for i in 0 to NBSLAVE -1 generate
	firstand: 	if i=0 generate 
			ortbus(0) <= ort_link(0);
	end generate firstand;
	otherand:	if i> 0 generate 
			ortbus(i) <= ortbus(i-1) and ort_link(i);
	end generate otherand;
end generate ortree;
ort <= ortbus (NBSLAVE-1);

--==========================================================================================
bit_bcast <= Tbusld when reset = '0' else (others=>'0') ; ------ ici il faudra mettre le bit d ecriture en sortie du wrapper sur chaque fil
we_bcast <= ld_actif when reset ='0' else '0';	-- mettre ici la sortie du wrapper en
---------------------------------------------------------------------------------------
data_bcast <= (others=>data_S) when reset = '1' else (others=>'0');
we_bit    <= wphase_S          ;--when reset = '1' else '0';
--==========================================================================================
reset_aux <= (others=>reset) when reset = '1' else  (others=>'0');

--==========================================================================================
ring : for I in 0 to NBSLAVE-1 generate
	acces_master : if I = 0 generate 
		sreg0 :shift_reg PORT MAP(
			D => Dlink(i),
			d_bus => dbuslink0,
			ld_reg => ld_reg0,
			clk => clock,
			shift_en => shift_cmd,
			Q => Qlink(i),
			q_bus => q_bus_link(i)
		);
	end generate acces_master;
	
	node_net : if i> 0 generate
		sreg :  shift_reg PORT MAP(
			D => Dlink(i),
			d_bus => d_bus_link(i),
			ld_reg => ld_reg_link(i),
			clk => clock,
			shift_en => shift_cmd,
			Q => Qlink(i),
			q_bus => q_bus_link(i)
		);
	end generate node_net;
end generate ring;
ld_reg0 <= ld_reg_link(0) or write_net_en;
dbuslink0 <= dbus0 when write_net_en='1' else d_bus_link(0);
Dlink(0)  <= Qlink (NBSlave -1)  ;
Dlink(1 to NBSLAVE - 1) <=  Qlink(0 to NBSLAVE - 2) ;







--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--process
--begin
--case next_state is
----========etat Idle======---
--when Idle => 
--bus_e <= '0' ;
--bus_a <= (others =>'0');
--bus_d <= (others =>'0');
--clk_prod <= '0' ;
--
--if wr_eh = '1' then 
--bus_e <= wr_eh ;
--bus_a <= wr_ah ;
--bus_d <= wr_dh ;
--clk_prod <= clock ;
--next_state <= Hmemory_w ;
--elsif enb = '1' then 
--bus_e <= enb;
--bus_a <= "000000" & addr_WR 	;
--bus_d <= data_WR ;
--clk_prod <= clkin ;
--next_state <= Wrapp_w ;
--else 
--next_state <= Idle ;
--end if ;
--
----========etat Hmemory_w======---
--when Hmemory_w => 
--bus_e <= wr_eh ;
--bus_a <= wr_ah ;
--bus_d <= wr_dh ;
--clk_prod <= clock ;
--
--if wr_eh = '0' then 
--bus_e <= '0' ;
--bus_a <= (others =>'0');
--bus_d <= (others =>'0');
--clk_prod <= '0' ;
--next_state <= Idle ;
--end if ;
--
----========etat Wrapp_w======---
--when Wrapp_w => 
--bus_e <= enb;
--bus_a <= "000000" & addr_WR 	;
--bus_d <= data_WR ;
--clk_prod <= clkin ;
--next_state <= Wrapp_w ;
--if enb = '0' then 
--bus_e <= '0' ;
--bus_a <= (others =>'0');
--bus_d <= (others =>'0');
--clk_prod <= '0' ;
--next_state <= Idle ;
--end if ;
--when others => 
--bus_e <= '0' ;
--bus_a <= (others =>'0');
--bus_d <= (others =>'0');
--clk_prod <= '0' ;
--next_state <= Idle ;
--end case ;
--
--end process;
--
--wr_ah <= wr_ah when wr_eh = '1' else wr_ah;
--wr_dh <= wr_dh when wr_eh = '1' else wr_dh;

--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================
--================================================================================================================================================

--==============================================================================================	
Inst_HSUcore: HSUcore PORT MAP(
		Instruction => Instructionbus,
		Instr_ready => Ird,
--		IPtrig => IPt,
		clr => reset,
		clk => clock,
		IPdone => IPd,

		Next_inst => Nxi,
		IPcode => Icode,
		SPMDcode => spmdc,
		Spmdtrig => spmdt,
		Twait =>ort,
		offset => ofs,

		Tout => TBusld,
		Nout => Nbusld,
		N2out => N2busld,
		Tin => Tbusst,
		Nin => Nbusst,
		N2in => N2busst 
	);
	--
	BufO_reg: reg0 
		generic map (N=>16)
		PORT MAP(
		load => BufOld,
		d => Tbusld(15 downto 0),
		clk => clock,
		clr => reset,
		q => Out32
	);
	
	BufLed_reg: reg0 
	generic map (N=>8)
	PORT MAP(
		load => BusLdld,
		d => Tbusld(7 downto 0),
		clk => clock,
		clr => reset,
		q => Out8
	);
	BufSwitch_reg: reg0 
		generic map (N=>8)
		PORT MAP(
		load => '1',
		d => In8,
		clk => clock,
		clr => reset,
		q => Bswbus
	);
Inst_IP_Led: IP_Led
		generic map (Mycode =>IPLed)
		PORT MAP(
		IPcode => Icode,
		BusLedld =>BusLdld
	);
	
Inst_IP_Switch: IP_Switch
		generic map (Mycode =>IPSwitch)
		PORT MAP(
		Tout => Tbusst,
		IPcode => Icode,
		Bsw =>Bswbus
	);
	


Inst_IPBufO: IP_BufO
		generic map (Mycode =>IPBufOut)
		PORT MAP(
		IPcode => Icode,
		BOld =>BufOld
	);
	

Inst_IPactif: IP_actif
		generic map (Mycode =>IPactif)
		PORT MAP(
		IPcode => Icode,
		load_actif =>ld_actif
	);

Inst_IPwaitBt: IP_waitBt 
		generic map (Mycode =>IPWaitBtn)
		PORT MAP(
		Tin => Tbusld( 4 downto 0),
		clk => clock,
		reset => reset,
		IPcode => Icode,
		Btn => InBtn,
		Tout => Tbusst,
		IPdone => IPdbt
	);
	
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

	Inst_IP_Wait: IP_Wait 
		generic map (Mycode =>IPWait)
		PORT MAP(
		Tin => Tbusld,
		IPcode => Icode,
		clk => clock,
		reset => reset,
		IPdone => IPdwt
	);
	IPd <= IPdwt or IPdbt or IPdft or IPdC32;
	
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

	Inst_IP_Com: IP_Com
		generic map (Mycode =>IPCom)
		PORT MAP(
		ipcode => Icode,
		shift_en => shift_cmd_1
	);

	Inst_IP_Com32: IP_Com32
		generic map (Mycode =>IPCom32)
		PORT MAP(
		ipcode => Icode,
		clk => clock,
		reset => reset,
		IPdone=> IPdC32,
		shift_en => shift_cmd_32
	);
shift_cmd <= shift_cmd_1 or shift_cmd_32;

Inst_IPMEcom: IP_MEcom

		generic map (Mycode =>IPME)
		PORT MAP(
		Tin => Tbusld,
		IPcode => Icode,
		Dbus=> dbus0,
		Qbus=> Q_bus_link(0),
		write_net => write_net_en,
		Tout => Tbusst
	);
end Behavioral;

