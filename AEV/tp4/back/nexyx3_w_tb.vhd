library IEEE;
use std.textio.all;
use IEEE.std_Logic_1164.ALL;
use IEEE.std_Logic_UNSIGNED.ALL;
use IEEE.std_Logic_ARITH.ALL;

  ENTITY testbench IS
  
  
  
  
  
  
  END testbench;

  ARCHITECTURE behavior OF testbench IS 
   signal      rx_out:  std_logic;
--=====================================================================================
component uart_baudClock is

    Port (      in_clk : in std_logic;
             baud_clk : out std_logic;
			    clk : out std_logic
         );
end component;
--=====================================================================================  
type rom_array is array (natural range <>) of std_logic_vector ( 63 downto 0 ) ;
constant  rom : rom_array := (
x"0000001C0C000000",  
x"0004FFFF200AB008",  
x"A00788228806A823",  
x"B0080BFBA0002000",  
x"2000A000A000200A",  
x"A007A0028806A823",  
x"B0080BFBA0001C00",  
x"1C00FFFF00000000"
--x"000000440C000000", 
--x"0008FFFF20018822", 
--x"1400FFFF10000000", 
--x"0004800120002002", 
--x"A007A8282001C826", 
--x"81FED009A0134002", 
--x"8000800080008000", 
--x"85FF60008806A823", 
--x"B0080BEFA0002001", 
--x"A1FE400260004001", 
--x"60002002A00785FF", 
--x"88128806A823B008", 
--x"0BFAA0008801A002", 
--x"201FA40220FF201F", 
--x"A4022002A007A002", 
--x"201FA4028806A823", 
--x"B0080BF9A0001C00", 
--x"FFFFFFFF00000014",
--x"1000000000101C00",
--x"A2011C0080001C00",
--x"8A00E82D1C001C00",
--x"BA03882220001400",
--x"10000000000C1400"
);



type rec is (idle, wait_16 ,payload,loading_buff,seting,bit8_fin,end_fsm);
signal rx_sig : rec := idle;
signal count :integer := 0;
signal bitline :integer := 63;
signal cur_line,i :integer  := 0;
signal data_buff0,data_buff : std_logic_vector (63 downto 0);
signal reg0,reg1,reg2,reg3,reg4,reg5,reg6,reg7: std_logic_vector (7 downto 0);
signal res0,res1,res2,res3,res4,res5,res6,res7: std_logic_vector (7 downto 0);
signal chk_gt : std_logic;  

  -- Component Declaration
          COMPONENT virtex7_Homade
     Port (
       	  btn   : in  STD_LOGIC_VECTOR (4 downto 0);
			  RS     : out  STD_LOGIC;
           RW     : out  STD_LOGIC;
           E      : out  STD_LOGIC;
           dout   : out  STD_LOGIC_VECTOR (3 downto 0);
			  sw     : in STD_LOGIC_VECTOR (7 downto 0);
			  led    : out STD_LOGIC_VECTOR (7 downto 0);
           mclk_p, mclk_n : in  STD_LOGIC;
           rx_in   :in  std_logic;
			  chk_gt   :out  std_logic
          
			  );
          END COMPONENT;
--========================================================================================
component Nexys3v3
    Port ( btn   : in  STD_LOGIC_VECTOR (4 downto 0);
			  an      : out  STD_LOGIC_VECTOR (3 downto 0);
           a_to_g   : out  STD_LOGIC_VECTOR (6 downto 0);
			  sw     : in STD_LOGIC_VECTOR (7 downto 0);
			  led    : out STD_LOGIC_VECTOR (7 downto 0);
           mclk : in  STD_LOGIC;
           rx_in   :in  std_logic
			  );
			   END COMPONENT;
--========================================================================================			  

          SIGNAL mclk_p,mclk_n, mclk  :  std_logic;
          signal btn   : STD_LOGIC_VECTOR (4 downto 0):="00000";
			 signal sw     : STD_LOGIC_VECTOR (7 downto 0):="10101010";
          

  BEGIN
  
  btn<="11111" after 63.604 ms;
--===========================================
--    uut: virtex7_Homade PORT MAP 
--	 (
--          mclk_p => mclk_p,
--			 sw => sw,
--			 btn=> btn,
--          mclk_n=> mclk_n,
--			 rx_in => rx_out,
--			 chk_gt => chk_gt
--
--        );
--===========================================
Nexys3_tb : Nexys3v3
    Port map
	      (mclk => mclk,
			 sw => sw,
			 btn=> btn,
			 rx_in => rx_out
			  );

--===========================================		  
		  
uart_baudClock_inst : uart_baudClock

  port map (   in_clk => mclk,
               baud_clk => chk_gt
           );
--============================== Virtex 7 et 6
--   clock_process :process
--   begin
--		mclk_p <= '0';
--		wait for 10 ns;
--		mclk_p <= '1';
--		wait for 10 ns;
--   end process;
-- 
--   clkin_process :process
--   begin
--		mclk_n <= '1';
--		wait for 10 ns;
--		mclk_n <= '0';
--		wait for 10 ns;
--   end process;
--============================== Nexys 3 et 2
   clkin_process :process
   begin
		mclk <= '1';
		wait for 10 ns;
		mclk <= '0';
		wait for 10 ns;
   end process;





process (chk_gt) 
begin 

if (rising_edge(chk_gt)) then

case rx_sig is
--======================================= 
when idle =>
count <= 0 ;
bitline <= 63;
data_buff0 <= rom (conv_integer(cur_line));

reg0 <= rom (conv_integer(cur_line)) (63 downto 56) ;
reg1 <= rom (conv_integer(cur_line)) (55 downto 48) ;
reg2 <= rom (conv_integer(cur_line)) (47 downto 40) ;
reg3 <= rom (conv_integer(cur_line)) (39 downto 32) ;
reg4 <= rom (conv_integer(cur_line)) (31 downto 24) ;
reg5 <= rom (conv_integer(cur_line)) (23 downto 16) ;
reg6 <= rom (conv_integer(cur_line)) (15 downto  8) ;
reg7 <= rom (conv_integer(cur_line)) (7  downto  0) ;

if cur_line /= 0 then
count <= count + 1 ;
end if ;

rx_out <='1';
rx_sig <= loading_buff;
--======================================= 
when loading_buff => 

if cur_line /= 0 then
count <= count + 1 ;
end if ;

for i in 0 to 7 loop

res0(i) <= reg0(7-i);
res1(i) <= reg1(7-i);
res2(i) <= reg2(7-i);
res3(i) <= reg3(7-i);
res4(i) <= reg4(7-i);
res5(i) <= reg5(7-i);
res6(i) <= reg6(7-i);
res7(i) <= reg7(7-i);

end loop;
rx_sig <= seting;
--======================================= 
when seting =>
count <= count + 1 ;
data_buff <= res0 & res1 & res2 & res3 & res4 & res5 & res6 & res7 ;

if count = 24 then
rx_sig <= wait_16;
rx_out <='0';
count <= 0 ;
elsif count = 14 then
rx_out <='0';
rx_sig <= seting;
else 
rx_sig <= seting;
end if ;


--=======================================
when  wait_16 =>
count <= count + 1 ;


if count = 15 then 

----------------------------
if (bitline ) mod 8 = 0  and bitline /= 63 and bitline /= 0 then
--rx_out <='1';
rx_sig <= bit8_fin;
rx_out <=data_buff(bitline);

else 

rx_sig <= payload;
end if ;
----------------------------
count <= 0 ;
else 
rx_sig <= wait_16;
rx_out <=data_buff(bitline);
end if ;

--======================================= 
--======================================= 

when bit8_fin=>

count <= count + 1 ;
rx_out <='0';

if count =23 then
rx_sig <= payload;
rx_out <='0';
count <= 0;
elsif  count = 13 then
rx_out <='0';
else
rx_sig <= bit8_fin;
end if ;


--=======================================
when payload =>
--rx_out <='0';
bitline <= bitline - 1 ;
if bitline = 0 then 
rx_sig <= loading_buff;
--rx_sig <= idle;
data_buff0 <= rom (conv_integer(cur_line));

reg0 <= rom (conv_integer(cur_line)) (63 downto 56) ;
reg1 <= rom (conv_integer(cur_line)) (55 downto 48) ;
reg2 <= rom (conv_integer(cur_line)) (47 downto 40) ;
reg3 <= rom (conv_integer(cur_line)) (39 downto 32) ;
reg4 <= rom (conv_integer(cur_line)) (31 downto 24) ;
reg5 <= rom (conv_integer(cur_line)) (23 downto 16) ;
reg6 <= rom (conv_integer(cur_line)) (15 downto  8) ;
reg7 <= rom (conv_integer(cur_line)) (7  downto  0) ;

--rx_out <='1';
if cur_line /= 0 then
count <= 1 ;
else 
count <= 0;
end if ;

bitline <= 63;
elsif bitline = 1 then

if cur_line + 1 = rom'high + 1 then
rx_sig <= end_fsm;
else
cur_line <= cur_line + 1 ;
rx_sig <= wait_16;
rx_out <='0';
count <= 1 ;
end if ;
else
rx_sig <= wait_16;
rx_out <='0';
count <= 1 ;
end if ;
--=======================================
when end_fsm =>
rx_out <='0';
count <= 0 ;
rx_sig <= end_fsm;
--=======================================
when others =>
count <= 15 ;
bitline <= 0;
data_buff <= rom (conv_integer(cur_line));
rx_out <='1';
end case ;
--=======================================
end if ;
end process ;
--=======================================







  END;
