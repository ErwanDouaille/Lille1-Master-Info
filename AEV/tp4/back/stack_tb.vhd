-- Vhdl test bench created from schematic C:\Users\jld\Documents\xilinx\Homade\V4\homade\stacknew.sch - Wed Oct 02 10:21:36 2013
--
-- Notes: 
-- 1) This testbench template has been automatically generated using types
-- std_logic and std_logic_vector for the ports of the unit under test.
-- Xilinx recommends that these types always be used for the top-level
-- I/O of a design in order to guarantee that the testbench will bind
-- correctly to the timing (post-route) simulation model.
-- 2) To use this template as your testbench, change the filename to any
-- name of your choice with the extension .vhd, and use the "Source->Add"
-- menu in Project Navigator to import the testbench. Then
-- edit the user defined section below, adding code to generate the 
-- stimulus for your design.
--
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
USE ieee.numeric_std.ALL;
LIBRARY UNISIM;
USE UNISIM.Vcomponents.ALL;
ENTITY stacknew_stacknew_sch_tb IS
END stacknew_stacknew_sch_tb;
ARCHITECTURE behavioral OF stacknew_stacknew_sch_tb IS 

   COMPONENT stacknew
   PORT( Nin	:	IN	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          N2in	:	IN	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          clk	:	IN	STD_LOGIC; 
          reset	:	IN	STD_LOGIC; 
          Tout	:	OUT	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          Nout	:	OUT	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          N2out	:	OUT	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          Litload	:	IN	STD_LOGIC; 
          Tin	:	IN	STD_LOGIC_VECTOR (31 DOWNTO 0); 
          Lit	:	IN	STD_LOGIC_VECTOR (11 DOWNTO 0); 
          ipdone	:	IN	STD_LOGIC; 
          shortIP	:	IN	STD_LOGIC; 
          X	:	IN	STD_LOGIC_VECTOR (1 DOWNTO 0); 
          Y	:	IN	STD_LOGIC_VECTOR (1 DOWNTO 0));
   END COMPONENT;

   SIGNAL Nin	:	STD_LOGIC_VECTOR (31 DOWNTO 0):=x"00000002";
   SIGNAL N2in	:	STD_LOGIC_VECTOR (31 DOWNTO 0):=x"00000001";
   SIGNAL clk	:	STD_LOGIC:='1';
   SIGNAL reset	:	STD_LOGIC:='1';
   SIGNAL Tout	:	STD_LOGIC_VECTOR (31 DOWNTO 0);
   SIGNAL Nout	:	STD_LOGIC_VECTOR (31 DOWNTO 0);
   SIGNAL N2out	:	STD_LOGIC_VECTOR (31 DOWNTO 0);
   SIGNAL Litload	:	STD_LOGIC:='0';
   SIGNAL Tin	:	STD_LOGIC_VECTOR (31 DOWNTO 0):=x"00000003";
   SIGNAL Lit	:	STD_LOGIC_VECTOR (11 DOWNTO 0):=x"0FE";
   SIGNAL ipdone	:	STD_LOGIC:='0';
   SIGNAL shortIP	:	STD_LOGIC:='0';
   SIGNAL X	:	STD_LOGIC_VECTOR (1 DOWNTO 0):="00";
   SIGNAL Y	:	STD_LOGIC_VECTOR (1 DOWNTO 0):="00";

BEGIN

   UUT: stacknew PORT MAP(
		Nin => Nin, 
		N2in => N2in, 
		clk => clk, 
		reset => reset, 
		Tout => Tout, 
		Nout => Nout, 
		N2out => N2out, 
		Litload => Litload, 
		Tin => Tin, 
		Lit => Lit, 
		ipdone => ipdone, 
		shortIP => shortIP, 
		X => X, 
		Y => Y
   );
reset<= '0' after 30 ns;
X <="00" after 61 ns, "00" after 101 ns;
Y<= "01" after 61 ns, "00" after 101 ns;
shortip <= '1' after 61 ns, '0' after 101 ns;
Tin <= x"00000004" after 120 ns, x"0000000F" after 141 ns;
Nin <= x"00000005" after 120 ns;

-- *** Test Bench - User Defined Section ***
   tb : PROCESS
   BEGIN
		 clk <= not clk ;
      WAIT for 20 ns;
   END PROCESS;

-- *** End Test Bench - User Defined Section ***

END;
