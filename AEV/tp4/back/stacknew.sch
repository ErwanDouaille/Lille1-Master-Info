<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="Nin(31:0)" />
        <signal name="N2in(31:0)" />
        <signal name="XLXN_15(31:0)" />
        <signal name="XLXN_17(31:0)" />
        <signal name="clk" />
        <signal name="reset" />
        <signal name="XLXN_24" />
        <signal name="XLXN_26(31:0)" />
        <signal name="XLXN_33(31:0)" />
        <signal name="XLXN_34(31:0)" />
        <signal name="XLXN_38" />
        <signal name="XLXN_39(31:0)" />
        <signal name="XLXN_40(31:0)" />
        <signal name="XLXN_41(31:0)" />
        <signal name="XLXN_45" />
        <signal name="XLXN_46(31:0)" />
        <signal name="XLXN_47(31:0)" />
        <signal name="XLXN_48(31:0)" />
        <signal name="XLXN_52" />
        <signal name="XLXN_53(31:0)" />
        <signal name="XLXN_71(1:0)" />
        <signal name="Tout(31:0)" />
        <signal name="Nout(31:0)" />
        <signal name="N2out(31:0)" />
        <signal name="XLXN_82(1:0)" />
        <signal name="XLXN_83(1:0)" />
        <signal name="XLXN_29" />
        <signal name="count(7:0)" />
        <signal name="XLXN_31(7:0)" />
        <signal name="count(1:0)" />
        <signal name="we0" />
        <signal name="we1" />
        <signal name="we2" />
        <signal name="we3" />
        <signal name="adr0(3:0)" />
        <signal name="adr1(3:0)" />
        <signal name="adr2(3:0)" />
        <signal name="adr3(3:0)" />
        <signal name="selin0(1:0)" />
        <signal name="selin1(1:0)" />
        <signal name="selin2(1:0)" />
        <signal name="selin3(1:0)" />
        <signal name="selreg0(1:0)" />
        <signal name="selreg1(1:0)" />
        <signal name="selreg2(1:0)" />
        <signal name="selreg3(1:0)" />
        <signal name="Litload" />
        <signal name="Tin(31:0)" />
        <signal name="Tin_lit(31:0)" />
        <signal name="Lit(11:0)" />
        <signal name="XLXN_219(31:0)" />
        <signal name="ipdone" />
        <signal name="shortIP" />
        <signal name="lastcycle" />
        <signal name="X(1:0)" />
        <signal name="Y(1:0)" />
        <signal name="oversized(31:0)" />
        <signal name="XLXN_223(1:0)" />
        <signal name="XLXN_224(31:0)" />
        <signal name="XLXN_225(31:0)" />
        <signal name="XLXN_226(31:0)" />
        <signal name="XLXN_227(31:0)" />
        <signal name="offset" />
        <signal name="XLXN_242" />
        <signal name="XLXN_243" />
        <signal name="selpredicat" />
        <port polarity="Input" name="Nin(31:0)" />
        <port polarity="Input" name="N2in(31:0)" />
        <port polarity="Input" name="clk" />
        <port polarity="Input" name="reset" />
        <port polarity="Output" name="Tout(31:0)" />
        <port polarity="Output" name="Nout(31:0)" />
        <port polarity="Output" name="N2out(31:0)" />
        <port polarity="Input" name="Litload" />
        <port polarity="Input" name="Tin(31:0)" />
        <port polarity="Input" name="Lit(11:0)" />
        <port polarity="Input" name="ipdone" />
        <port polarity="Input" name="shortIP" />
        <port polarity="Input" name="X(1:0)" />
        <port polarity="Input" name="Y(1:0)" />
        <port polarity="Output" name="oversized(31:0)" />
        <port polarity="Output" name="offset" />
        <blockdef name="Ram8">
            <timestamp>2013-9-29T16:6:26</timestamp>
            <line x2="0" y1="-288" y2="-288" x1="64" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <rect width="64" x="0" y="-108" height="24" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-300" height="24" />
            <line x2="384" y1="-288" y2="-288" x1="320" />
            <rect style="fillcolor:rgb(0,255,255)" width="256" x="64" y="-320" height="384" />
        </blockdef>
        <blockdef name="reg32">
            <timestamp>2013-9-29T6:11:59</timestamp>
            <rect width="256" x="64" y="-256" height="256" />
            <rect width="64" x="0" y="-236" height="24" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-236" height="24" />
            <line x2="384" y1="-224" y2="-224" x1="320" />
        </blockdef>
        <blockdef name="Mux3_32">
            <timestamp>2013-9-29T14:59:45</timestamp>
            <rect width="192" x="64" y="-256" height="256" />
            <rect width="64" x="0" y="-236" height="24" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <rect width="64" x="0" y="-172" height="24" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <rect width="64" x="0" y="-108" height="24" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="256" y="-236" height="24" />
            <line x2="320" y1="-224" y2="-224" x1="256" />
        </blockdef>
        <blockdef name="Mux4">
            <timestamp>2013-9-26T9:27:57</timestamp>
            <rect width="256" x="64" y="-320" height="320" />
            <rect width="64" x="0" y="-300" height="24" />
            <line x2="0" y1="-288" y2="-288" x1="64" />
            <rect width="64" x="0" y="-236" height="24" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <rect width="64" x="0" y="-172" height="24" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <rect width="64" x="0" y="-108" height="24" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-300" height="24" />
            <line x2="384" y1="-288" y2="-288" x1="320" />
        </blockdef>
        <blockdef name="vcc">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-32" y2="-64" x1="64" />
            <line x2="64" y1="0" y2="-32" x1="64" />
            <line x2="32" y1="-64" y2="-64" x1="96" />
        </blockdef>
        <blockdef name="next_count">
            <timestamp>2013-10-2T16:12:16</timestamp>
            <line x2="0" y1="32" y2="32" x1="64" />
            <rect width="64" x="0" y="84" height="24" />
            <line x2="0" y1="96" y2="96" x1="64" />
            <rect width="64" x="0" y="148" height="24" />
            <line x2="0" y1="160" y2="160" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="400" y="-428" height="24" />
            <line x2="464" y1="-416" y2="-416" x1="400" />
            <rect width="336" x="64" y="-448" height="640" />
        </blockdef>
        <blockdef name="select_out">
            <timestamp>2013-10-2T16:52:9</timestamp>
            <rect width="64" x="320" y="20" height="24" />
            <line x2="384" y1="32" y2="32" x1="320" />
            <rect width="64" x="0" y="-172" height="24" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <rect width="64" x="320" y="-172" height="24" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
            <rect width="64" x="320" y="-108" height="24" />
            <line x2="384" y1="-96" y2="-96" x1="320" />
            <rect width="64" x="320" y="-44" height="24" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
            <rect width="256" x="64" y="-192" height="256" />
        </blockdef>
        <blockdef name="select_in">
            <timestamp>2013-11-20T10:35:14</timestamp>
            <line x2="384" y1="480" y2="480" x1="320" />
            <line x2="0" y1="288" y2="288" x1="64" />
            <rect width="64" x="0" y="340" height="24" />
            <line x2="0" y1="352" y2="352" x1="64" />
            <rect width="64" x="0" y="404" height="24" />
            <line x2="0" y1="416" y2="416" x1="64" />
            <rect width="64" x="320" y="20" height="24" />
            <line x2="384" y1="32" y2="32" x1="320" />
            <rect width="64" x="320" y="84" height="24" />
            <line x2="384" y1="96" y2="96" x1="320" />
            <rect width="64" x="320" y="148" height="24" />
            <line x2="384" y1="160" y2="160" x1="320" />
            <rect width="64" x="320" y="212" height="24" />
            <line x2="384" y1="224" y2="224" x1="320" />
            <rect width="64" x="0" y="-76" height="24" />
            <line x2="0" y1="-64" y2="-64" x1="64" />
            <line x2="384" y1="-736" y2="-736" x1="320" />
            <line x2="384" y1="-672" y2="-672" x1="320" />
            <line x2="384" y1="-608" y2="-608" x1="320" />
            <line x2="384" y1="-544" y2="-544" x1="320" />
            <rect width="64" x="320" y="-492" height="24" />
            <line x2="384" y1="-480" y2="-480" x1="320" />
            <rect width="64" x="320" y="-428" height="24" />
            <line x2="384" y1="-416" y2="-416" x1="320" />
            <rect width="64" x="320" y="-364" height="24" />
            <line x2="384" y1="-352" y2="-352" x1="320" />
            <rect width="64" x="320" y="-300" height="24" />
            <line x2="384" y1="-288" y2="-288" x1="320" />
            <rect width="64" x="320" y="-236" height="24" />
            <line x2="384" y1="-224" y2="-224" x1="320" />
            <rect width="64" x="320" y="-172" height="24" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
            <rect width="64" x="320" y="-108" height="24" />
            <line x2="384" y1="-96" y2="-96" x1="320" />
            <rect width="64" x="320" y="-44" height="24" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
            <rect width="256" x="64" y="-768" height="1280" />
        </blockdef>
        <blockdef name="Mux32">
            <timestamp>2013-9-29T16:31:53</timestamp>
            <rect width="256" x="64" y="-192" height="192" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <rect width="64" x="0" y="-108" height="24" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-172" height="24" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
        </blockdef>
        <blockdef name="constant32">
            <timestamp>2013-9-29T16:48:26</timestamp>
            <rect width="256" x="64" y="-64" height="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-44" height="24" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
        </blockdef>
        <blockdef name="or3">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="48" y1="-64" y2="-64" x1="0" />
            <line x2="72" y1="-128" y2="-128" x1="0" />
            <line x2="48" y1="-192" y2="-192" x1="0" />
            <line x2="192" y1="-128" y2="-128" x1="256" />
            <arc ex="192" ey="-128" sx="112" sy="-80" r="88" cx="116" cy="-168" />
            <arc ex="48" ey="-176" sx="48" sy="-80" r="56" cx="16" cy="-128" />
            <line x2="48" y1="-64" y2="-80" x1="48" />
            <line x2="48" y1="-192" y2="-176" x1="48" />
            <line x2="48" y1="-80" y2="-80" x1="112" />
            <arc ex="112" ey="-176" sx="192" sy="-128" r="88" cx="116" cy="-88" />
            <line x2="48" y1="-176" y2="-176" x1="112" />
        </blockdef>
        <blockdef name="fd8ce">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-192" y2="-192" x1="0" />
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <line x2="64" y1="-32" y2="-32" x1="192" />
            <line x2="192" y1="-64" y2="-32" x1="192" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
            <rect width="64" x="320" y="-268" height="24" />
            <rect width="64" x="0" y="-268" height="24" />
            <rect width="256" x="64" y="-320" height="256" />
        </blockdef>
        <blockdef name="predicat">
            <timestamp>2013-10-3T10:41:33</timestamp>
            <rect width="256" x="64" y="-64" height="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
        </blockdef>
        <blockdef name="m2_1">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="96" y1="-64" y2="-192" x1="96" />
            <line x2="96" y1="-96" y2="-64" x1="256" />
            <line x2="256" y1="-160" y2="-96" x1="256" />
            <line x2="256" y1="-192" y2="-160" x1="96" />
            <line x2="96" y1="-32" y2="-32" x1="176" />
            <line x2="176" y1="-80" y2="-32" x1="176" />
            <line x2="96" y1="-32" y2="-32" x1="0" />
            <line x2="256" y1="-128" y2="-128" x1="320" />
            <line x2="96" y1="-96" y2="-96" x1="0" />
            <line x2="96" y1="-160" y2="-160" x1="0" />
        </blockdef>
        <block symbolname="Mux3_32" name="muxi0">
            <blockpin signalname="Tin_lit(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin0(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_26(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Ram8" name="ram0">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="we0" name="we" />
            <blockpin signalname="adr0(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_26(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_17(31:0)" name="dataout(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxi1">
            <blockpin signalname="Tin_lit(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin1(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_39(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxi2">
            <blockpin signalname="Tin_lit(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin2(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_46(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxi3">
            <blockpin signalname="Tin_lit(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin3(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_53(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxr0">
            <blockpin signalname="XLXN_224(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_26(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_17(31:0)" name="X2(31:0)" />
            <blockpin signalname="selreg0(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_15(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="reg32" name="r0">
            <blockpin signalname="XLXN_15(31:0)" name="D(31:0)" />
            <blockpin signalname="reset" name="CLR" />
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_24" name="CE" />
            <blockpin signalname="XLXN_224(31:0)" name="Q(31:0)" />
        </block>
        <block symbolname="vcc" name="XLXI_28">
            <blockpin signalname="XLXN_24" name="P" />
        </block>
        <block symbolname="Ram8" name="ram1">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="we1" name="we" />
            <blockpin signalname="adr1(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_39(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_34(31:0)" name="dataout(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxR1">
            <blockpin signalname="XLXN_225(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_39(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_34(31:0)" name="X2(31:0)" />
            <blockpin signalname="selreg1(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_33(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="reg32" name="R1">
            <blockpin signalname="XLXN_33(31:0)" name="D(31:0)" />
            <blockpin signalname="reset" name="CLR" />
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_38" name="CE" />
            <blockpin signalname="XLXN_225(31:0)" name="Q(31:0)" />
        </block>
        <block symbolname="vcc" name="XLXI_34">
            <blockpin signalname="XLXN_38" name="P" />
        </block>
        <block symbolname="Ram8" name="ram2">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="we2" name="we" />
            <blockpin signalname="adr2(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_46(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_41(31:0)" name="dataout(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxr2">
            <blockpin signalname="XLXN_226(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_46(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_41(31:0)" name="X2(31:0)" />
            <blockpin signalname="selreg2(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_40(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="reg32" name="R2">
            <blockpin signalname="XLXN_40(31:0)" name="D(31:0)" />
            <blockpin signalname="reset" name="CLR" />
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_45" name="CE" />
            <blockpin signalname="XLXN_226(31:0)" name="Q(31:0)" />
        </block>
        <block symbolname="vcc" name="XLXI_38">
            <blockpin signalname="XLXN_45" name="P" />
        </block>
        <block symbolname="Ram8" name="ram3">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="we3" name="we" />
            <blockpin signalname="adr3(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_53(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_48(31:0)" name="dataout(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxr3">
            <blockpin signalname="XLXN_227(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_53(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_48(31:0)" name="X2(31:0)" />
            <blockpin signalname="selreg3(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_47(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="reg32" name="r3">
            <blockpin signalname="XLXN_47(31:0)" name="D(31:0)" />
            <blockpin signalname="reset" name="CLR" />
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_52" name="CE" />
            <blockpin signalname="XLXN_227(31:0)" name="Q(31:0)" />
        </block>
        <block symbolname="vcc" name="XLXI_42">
            <blockpin signalname="XLXN_52" name="P" />
        </block>
        <block symbolname="Mux4" name="muxoutT">
            <blockpin signalname="XLXN_224(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_225(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_226(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_227(31:0)" name="X3(31:0)" />
            <blockpin signalname="XLXN_71(1:0)" name="sel(1:0)" />
            <blockpin signalname="Tout(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux4" name="muxoutN">
            <blockpin signalname="XLXN_224(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_225(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_226(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_227(31:0)" name="X3(31:0)" />
            <blockpin signalname="XLXN_82(1:0)" name="sel(1:0)" />
            <blockpin signalname="Nout(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux4" name="muxoutN2">
            <blockpin signalname="XLXN_224(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_225(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_226(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_227(31:0)" name="X3(31:0)" />
            <blockpin signalname="XLXN_83(1:0)" name="sel(1:0)" />
            <blockpin signalname="N2out(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="select_in" name="sel_in">
            <blockpin signalname="lastcycle" name="Lastcycle" />
            <blockpin signalname="count(7:0)" name="count(7:0)" />
            <blockpin signalname="X(1:0)" name="X(1:0)" />
            <blockpin signalname="Y(1:0)" name="Y(1:0)" />
            <blockpin signalname="we0" name="we0" />
            <blockpin signalname="we1" name="we1" />
            <blockpin signalname="we2" name="we2" />
            <blockpin signalname="we3" name="we3" />
            <blockpin signalname="adr0(3:0)" name="adr0(3:0)" />
            <blockpin signalname="adr1(3:0)" name="adr1(3:0)" />
            <blockpin signalname="adr2(3:0)" name="adr2(3:0)" />
            <blockpin signalname="adr3(3:0)" name="adr3(3:0)" />
            <blockpin signalname="selin0(1:0)" name="selin0(1:0)" />
            <blockpin signalname="selin1(1:0)" name="selin1(1:0)" />
            <blockpin signalname="selin2(1:0)" name="selin2(1:0)" />
            <blockpin signalname="selin3(1:0)" name="selin3(1:0)" />
            <blockpin signalname="selreg0(1:0)" name="selreg0(1:0)" />
            <blockpin signalname="selreg1(1:0)" name="selreg1(1:0)" />
            <blockpin signalname="selreg2(1:0)" name="selreg2(1:0)" />
            <blockpin signalname="selreg3(1:0)" name="selreg3(1:0)" />
            <blockpin signalname="selpredicat" name="selpredicat" />
        </block>
        <block symbolname="vcc" name="XLXI_29">
            <blockpin signalname="XLXN_29" name="P" />
        </block>
        <block symbolname="next_count" name="update_counter">
            <blockpin signalname="lastcycle" name="lastcycle" />
            <blockpin signalname="count(7:0)" name="count_in(7:0)" />
            <blockpin signalname="X(1:0)" name="X(1:0)" />
            <blockpin signalname="Y(1:0)" name="Y(1:0)" />
            <blockpin signalname="XLXN_31(7:0)" name="count_out(7:0)" />
        </block>
        <block symbolname="select_out" name="sel_out">
            <blockpin signalname="count(1:0)" name="count(1:0)" />
            <blockpin signalname="XLXN_71(1:0)" name="selTout(1:0)" />
            <blockpin signalname="XLXN_82(1:0)" name="selNout(1:0)" />
            <blockpin signalname="XLXN_83(1:0)" name="selN2out(1:0)" />
            <blockpin signalname="XLXN_223(1:0)" name="seloverS(1:0)" />
        </block>
        <block symbolname="Mux32" name="XLXI_87">
            <blockpin signalname="Litload" name="sel" />
            <blockpin signalname="Tin(31:0)" name="a(31:0)" />
            <blockpin signalname="XLXN_219(31:0)" name="b(31:0)" />
            <blockpin signalname="Tin_lit(31:0)" name="s(31:0)" />
        </block>
        <block symbolname="constant32" name="XLXI_89">
            <blockpin signalname="Lit(11:0)" name="value(11:0)" />
            <blockpin signalname="XLXN_219(31:0)" name="value32(31:0)" />
        </block>
        <block symbolname="or3" name="XLXI_91">
            <blockpin signalname="ipdone" name="I0" />
            <blockpin signalname="shortIP" name="I1" />
            <blockpin signalname="Litload" name="I2" />
            <blockpin signalname="lastcycle" name="O" />
        </block>
        <block symbolname="fd8ce" name="XLXI_93">
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_29" name="CE" />
            <blockpin signalname="reset" name="CLR" />
            <blockpin signalname="XLXN_31(7:0)" name="D(7:0)" />
            <blockpin signalname="count(7:0)" name="Q(7:0)" />
        </block>
        <block symbolname="Mux4" name="XLXI_94">
            <blockpin signalname="XLXN_224(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_225(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_226(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_227(31:0)" name="X3(31:0)" />
            <blockpin signalname="XLXN_223(1:0)" name="sel(1:0)" />
            <blockpin signalname="oversized(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="predicat" name="XLXI_98">
            <blockpin signalname="Tout(31:0)" name="tin(31:0)" />
            <blockpin signalname="XLXN_242" name="predicat" />
        </block>
        <block symbolname="predicat" name="XLXI_99">
            <blockpin signalname="Tin_lit(31:0)" name="tin(31:0)" />
            <blockpin signalname="XLXN_243" name="predicat" />
        </block>
        <block symbolname="m2_1" name="XLXI_100">
            <blockpin signalname="XLXN_243" name="D0" />
            <blockpin signalname="XLXN_242" name="D1" />
            <blockpin signalname="selpredicat" name="S0" />
            <blockpin signalname="offset" name="O" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="7040" height="5440">
        <instance x="960" y="784" name="muxi0" orien="R0">
        </instance>
        <instance x="1664" y="1152" name="ram0" orien="R0">
        </instance>
        <instance x="960" y="1600" name="muxi1" orien="R0">
        </instance>
        <instance x="960" y="2400" name="muxi2" orien="R0">
        </instance>
        <instance x="960" y="3328" name="muxi3" orien="R0">
        </instance>
        <branch name="Nin(31:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="576" y="736" type="branch" />
            <wire x2="576" y1="176" y2="624" x1="576" />
            <wire x2="576" y1="624" y2="736" x1="576" />
            <wire x2="576" y1="736" y2="1440" x1="576" />
            <wire x2="960" y1="1440" y2="1440" x1="576" />
            <wire x2="576" y1="1440" y2="2240" x1="576" />
            <wire x2="960" y1="2240" y2="2240" x1="576" />
            <wire x2="576" y1="2240" y2="3168" x1="576" />
            <wire x2="576" y1="3168" y2="3376" x1="576" />
            <wire x2="960" y1="3168" y2="3168" x1="576" />
            <wire x2="960" y1="624" y2="624" x1="576" />
        </branch>
        <branch name="N2in(31:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="736" y="976" type="branch" />
            <wire x2="736" y1="176" y2="688" x1="736" />
            <wire x2="736" y1="688" y2="976" x1="736" />
            <wire x2="736" y1="976" y2="1504" x1="736" />
            <wire x2="960" y1="1504" y2="1504" x1="736" />
            <wire x2="736" y1="1504" y2="2304" x1="736" />
            <wire x2="960" y1="2304" y2="2304" x1="736" />
            <wire x2="736" y1="2304" y2="3232" x1="736" />
            <wire x2="736" y1="3232" y2="3360" x1="736" />
            <wire x2="960" y1="3232" y2="3232" x1="736" />
            <wire x2="960" y1="688" y2="688" x1="736" />
        </branch>
        <instance x="1664" y="784" name="muxr0" orien="R0">
        </instance>
        <branch name="XLXN_15(31:0)">
            <wire x2="2192" y1="560" y2="560" x1="1984" />
        </branch>
        <branch name="XLXN_17(31:0)">
            <wire x2="1536" y1="688" y2="800" x1="1536" />
            <wire x2="2112" y1="800" y2="800" x1="1536" />
            <wire x2="2112" y1="800" y2="864" x1="2112" />
            <wire x2="1664" y1="688" y2="688" x1="1536" />
            <wire x2="2112" y1="864" y2="864" x1="2048" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="864" type="branch" />
            <wire x2="1616" y1="864" y2="864" x1="1600" />
            <wire x2="1664" y1="864" y2="864" x1="1616" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="688" type="branch" />
            <wire x2="2096" y1="688" y2="688" x1="2080" />
            <wire x2="2192" y1="688" y2="688" x1="2096" />
        </branch>
        <branch name="reset">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="624" type="branch" />
            <wire x2="2096" y1="624" y2="624" x1="2064" />
            <wire x2="2192" y1="624" y2="624" x1="2096" />
        </branch>
        <instance x="2192" y="784" name="r0" orien="R0">
        </instance>
        <branch name="XLXN_24">
            <wire x2="2192" y1="752" y2="752" x1="2160" />
        </branch>
        <instance x="2160" y="688" name="XLXI_28" orien="M90" />
        <branch name="XLXN_26(31:0)">
            <wire x2="1472" y1="560" y2="560" x1="1280" />
            <wire x2="1472" y1="560" y2="624" x1="1472" />
            <wire x2="1664" y1="624" y2="624" x1="1472" />
            <wire x2="1472" y1="624" y2="1120" x1="1472" />
            <wire x2="1664" y1="1120" y2="1120" x1="1472" />
        </branch>
        <instance x="1664" y="1968" name="ram1" orien="R0">
        </instance>
        <instance x="1664" y="1600" name="muxR1" orien="R0">
        </instance>
        <branch name="XLXN_33(31:0)">
            <wire x2="2192" y1="1376" y2="1376" x1="1984" />
        </branch>
        <branch name="XLXN_34(31:0)">
            <wire x2="1536" y1="1504" y2="1616" x1="1536" />
            <wire x2="2112" y1="1616" y2="1616" x1="1536" />
            <wire x2="2112" y1="1616" y2="1680" x1="2112" />
            <wire x2="1664" y1="1504" y2="1504" x1="1536" />
            <wire x2="2112" y1="1680" y2="1680" x1="2048" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="1680" type="branch" />
            <wire x2="1616" y1="1680" y2="1680" x1="1600" />
            <wire x2="1664" y1="1680" y2="1680" x1="1616" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="1504" type="branch" />
            <wire x2="2096" y1="1504" y2="1504" x1="2080" />
            <wire x2="2192" y1="1504" y2="1504" x1="2096" />
        </branch>
        <branch name="reset">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="1440" type="branch" />
            <wire x2="2096" y1="1440" y2="1440" x1="2064" />
            <wire x2="2192" y1="1440" y2="1440" x1="2096" />
        </branch>
        <instance x="2192" y="1600" name="R1" orien="R0">
        </instance>
        <branch name="XLXN_38">
            <wire x2="2192" y1="1568" y2="1568" x1="2160" />
        </branch>
        <instance x="2160" y="1504" name="XLXI_34" orien="M90" />
        <branch name="XLXN_39(31:0)">
            <wire x2="1472" y1="1376" y2="1376" x1="1280" />
            <wire x2="1472" y1="1376" y2="1440" x1="1472" />
            <wire x2="1664" y1="1440" y2="1440" x1="1472" />
            <wire x2="1472" y1="1440" y2="1936" x1="1472" />
            <wire x2="1664" y1="1936" y2="1936" x1="1472" />
        </branch>
        <instance x="1664" y="2768" name="ram2" orien="R0">
        </instance>
        <instance x="1664" y="2400" name="muxr2" orien="R0">
        </instance>
        <branch name="XLXN_40(31:0)">
            <wire x2="2192" y1="2176" y2="2176" x1="1984" />
        </branch>
        <branch name="XLXN_41(31:0)">
            <wire x2="1536" y1="2304" y2="2416" x1="1536" />
            <wire x2="2112" y1="2416" y2="2416" x1="1536" />
            <wire x2="2112" y1="2416" y2="2480" x1="2112" />
            <wire x2="1664" y1="2304" y2="2304" x1="1536" />
            <wire x2="2112" y1="2480" y2="2480" x1="2048" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="2480" type="branch" />
            <wire x2="1616" y1="2480" y2="2480" x1="1600" />
            <wire x2="1664" y1="2480" y2="2480" x1="1616" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="2304" type="branch" />
            <wire x2="2096" y1="2304" y2="2304" x1="2080" />
            <wire x2="2192" y1="2304" y2="2304" x1="2096" />
        </branch>
        <branch name="reset">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="2240" type="branch" />
            <wire x2="2096" y1="2240" y2="2240" x1="2064" />
            <wire x2="2192" y1="2240" y2="2240" x1="2096" />
        </branch>
        <instance x="2192" y="2400" name="R2" orien="R0">
        </instance>
        <branch name="XLXN_45">
            <wire x2="2192" y1="2368" y2="2368" x1="2160" />
        </branch>
        <instance x="2160" y="2304" name="XLXI_38" orien="M90" />
        <branch name="XLXN_46(31:0)">
            <wire x2="1472" y1="2176" y2="2176" x1="1280" />
            <wire x2="1472" y1="2176" y2="2240" x1="1472" />
            <wire x2="1664" y1="2240" y2="2240" x1="1472" />
            <wire x2="1472" y1="2240" y2="2736" x1="1472" />
            <wire x2="1664" y1="2736" y2="2736" x1="1472" />
        </branch>
        <instance x="1664" y="3696" name="ram3" orien="R0">
        </instance>
        <instance x="1664" y="3328" name="muxr3" orien="R0">
        </instance>
        <branch name="XLXN_47(31:0)">
            <wire x2="2192" y1="3104" y2="3104" x1="1984" />
        </branch>
        <branch name="XLXN_48(31:0)">
            <wire x2="1536" y1="3232" y2="3344" x1="1536" />
            <wire x2="2112" y1="3344" y2="3344" x1="1536" />
            <wire x2="2112" y1="3344" y2="3408" x1="2112" />
            <wire x2="1664" y1="3232" y2="3232" x1="1536" />
            <wire x2="2112" y1="3408" y2="3408" x1="2048" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="3408" type="branch" />
            <wire x2="1616" y1="3408" y2="3408" x1="1600" />
            <wire x2="1664" y1="3408" y2="3408" x1="1616" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="3232" type="branch" />
            <wire x2="2096" y1="3232" y2="3232" x1="2080" />
            <wire x2="2192" y1="3232" y2="3232" x1="2096" />
        </branch>
        <branch name="reset">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2096" y="3168" type="branch" />
            <wire x2="2096" y1="3168" y2="3168" x1="2064" />
            <wire x2="2192" y1="3168" y2="3168" x1="2096" />
        </branch>
        <instance x="2192" y="3328" name="r3" orien="R0">
        </instance>
        <branch name="XLXN_52">
            <wire x2="2192" y1="3296" y2="3296" x1="2160" />
        </branch>
        <instance x="2160" y="3232" name="XLXI_42" orien="M90" />
        <branch name="XLXN_53(31:0)">
            <wire x2="1472" y1="3104" y2="3104" x1="1280" />
            <wire x2="1472" y1="3104" y2="3168" x1="1472" />
            <wire x2="1664" y1="3168" y2="3168" x1="1472" />
            <wire x2="1472" y1="3168" y2="3664" x1="1472" />
            <wire x2="1664" y1="3664" y2="3664" x1="1472" />
        </branch>
        <branch name="XLXN_71(1:0)">
            <wire x2="3136" y1="3824" y2="3824" x1="2464" />
            <wire x2="3776" y1="800" y2="800" x1="3136" />
            <wire x2="3136" y1="800" y2="3824" x1="3136" />
        </branch>
        <branch name="Tout(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="4224" y="544" type="branch" />
            <wire x2="3184" y1="288" y2="288" x1="3136" />
            <wire x2="3136" y1="288" y2="432" x1="3136" />
            <wire x2="4208" y1="432" y2="432" x1="3136" />
            <wire x2="4208" y1="432" y2="544" x1="4208" />
            <wire x2="4224" y1="544" y2="544" x1="4208" />
            <wire x2="4288" y1="544" y2="544" x1="4224" />
            <wire x2="4208" y1="544" y2="544" x1="4160" />
        </branch>
        <instance x="3792" y="1824" name="muxoutN" orien="R0">
        </instance>
        <branch name="Nout(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="4240" y="1536" type="branch" />
            <wire x2="4240" y1="1536" y2="1536" x1="4176" />
            <wire x2="4304" y1="1536" y2="1536" x1="4240" />
        </branch>
        <instance x="3792" y="2752" name="muxoutN2" orien="R0">
        </instance>
        <branch name="N2out(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="4240" y="2464" type="branch" />
            <wire x2="4240" y1="2464" y2="2464" x1="4176" />
            <wire x2="4304" y1="2464" y2="2464" x1="4240" />
        </branch>
        <branch name="XLXN_82(1:0)">
            <wire x2="3184" y1="3888" y2="3888" x1="2464" />
            <wire x2="3792" y1="1792" y2="1792" x1="3184" />
            <wire x2="3184" y1="1792" y2="3888" x1="3184" />
        </branch>
        <branch name="XLXN_83(1:0)">
            <wire x2="3776" y1="3952" y2="3952" x1="2464" />
            <wire x2="3792" y1="2720" y2="2720" x1="3776" />
            <wire x2="3776" y1="2720" y2="3952" x1="3776" />
        </branch>
        <iomarker fontsize="28" x="4304" y="1536" name="Nout(31:0)" orien="R0" />
        <iomarker fontsize="28" x="4304" y="2464" name="N2out(31:0)" orien="R0" />
        <branch name="reset">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="880" y="4240" type="branch" />
            <wire x2="880" y1="4240" y2="4240" x1="864" />
            <wire x2="928" y1="4240" y2="4240" x1="880" />
        </branch>
        <branch name="clk">
            <wire x2="928" y1="4144" y2="4144" x1="864" />
        </branch>
        <branch name="XLXN_29">
            <wire x2="928" y1="4080" y2="4080" x1="896" />
        </branch>
        <instance x="896" y="4144" name="XLXI_29" orien="R270" />
        <branch name="count(7:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1344" y="4016" type="branch" />
            <wire x2="128" y1="3840" y2="4400" x1="128" />
            <wire x2="288" y1="4400" y2="4400" x1="128" />
            <wire x2="1392" y1="3840" y2="3840" x1="128" />
            <wire x2="1392" y1="3840" y2="3936" x1="1392" />
            <wire x2="1392" y1="3936" y2="4016" x1="1392" />
            <wire x2="1392" y1="4016" y2="4816" x1="1392" />
            <wire x2="1648" y1="4816" y2="4816" x1="1392" />
            <wire x2="1344" y1="4016" y2="4016" x1="1312" />
            <wire x2="1392" y1="4016" y2="4016" x1="1344" />
        </branch>
        <branch name="XLXN_31(7:0)">
            <wire x2="928" y1="4016" y2="4016" x1="752" />
        </branch>
        <bustap x2="1488" y1="3936" y2="3936" x1="1392" />
        <branch name="count(1:0)">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="1768" y="3936" type="branch" />
            <wire x2="1760" y1="3936" y2="3936" x1="1488" />
            <wire x2="1768" y1="3936" y2="3936" x1="1760" />
            <wire x2="2080" y1="3824" y2="3824" x1="1760" />
            <wire x2="1760" y1="3824" y2="3936" x1="1760" />
        </branch>
        <instance x="288" y="4432" name="update_counter" orien="R0">
        </instance>
        <branch name="we0">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4144" type="branch" />
            <wire x2="2208" y1="4144" y2="4144" x1="2032" />
            <wire x2="2240" y1="4144" y2="4144" x1="2208" />
        </branch>
        <branch name="we1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4208" type="branch" />
            <wire x2="2208" y1="4208" y2="4208" x1="2032" />
            <wire x2="2240" y1="4208" y2="4208" x1="2208" />
        </branch>
        <branch name="we2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4272" type="branch" />
            <wire x2="2208" y1="4272" y2="4272" x1="2032" />
            <wire x2="2240" y1="4272" y2="4272" x1="2208" />
        </branch>
        <branch name="we3">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4336" type="branch" />
            <wire x2="2208" y1="4336" y2="4336" x1="2032" />
            <wire x2="2240" y1="4336" y2="4336" x1="2208" />
        </branch>
        <branch name="adr0(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4400" type="branch" />
            <wire x2="2208" y1="4400" y2="4400" x1="2032" />
            <wire x2="2240" y1="4400" y2="4400" x1="2208" />
        </branch>
        <branch name="adr1(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4464" type="branch" />
            <wire x2="2208" y1="4464" y2="4464" x1="2032" />
            <wire x2="2240" y1="4464" y2="4464" x1="2208" />
        </branch>
        <branch name="adr2(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4528" type="branch" />
            <wire x2="2208" y1="4528" y2="4528" x1="2032" />
            <wire x2="2240" y1="4528" y2="4528" x1="2208" />
        </branch>
        <branch name="adr3(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4592" type="branch" />
            <wire x2="2208" y1="4592" y2="4592" x1="2032" />
            <wire x2="2240" y1="4592" y2="4592" x1="2208" />
        </branch>
        <branch name="selin0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4656" type="branch" />
            <wire x2="2208" y1="4656" y2="4656" x1="2032" />
            <wire x2="2240" y1="4656" y2="4656" x1="2208" />
        </branch>
        <branch name="selin1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4720" type="branch" />
            <wire x2="2208" y1="4720" y2="4720" x1="2032" />
            <wire x2="2240" y1="4720" y2="4720" x1="2208" />
        </branch>
        <branch name="selin2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4784" type="branch" />
            <wire x2="2208" y1="4784" y2="4784" x1="2032" />
            <wire x2="2240" y1="4784" y2="4784" x1="2208" />
        </branch>
        <branch name="selin3(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4848" type="branch" />
            <wire x2="2208" y1="4848" y2="4848" x1="2032" />
            <wire x2="2240" y1="4848" y2="4848" x1="2208" />
        </branch>
        <branch name="we0">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="928" type="branch" />
            <wire x2="1616" y1="928" y2="928" x1="1600" />
            <wire x2="1664" y1="928" y2="928" x1="1616" />
        </branch>
        <branch name="adr0(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="1056" type="branch" />
            <wire x2="1616" y1="1056" y2="1056" x1="1600" />
            <wire x2="1664" y1="1056" y2="1056" x1="1616" />
        </branch>
        <branch name="we1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="1744" type="branch" />
            <wire x2="1616" y1="1744" y2="1744" x1="1600" />
            <wire x2="1664" y1="1744" y2="1744" x1="1616" />
        </branch>
        <branch name="adr1(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="1872" type="branch" />
            <wire x2="1616" y1="1872" y2="1872" x1="1600" />
            <wire x2="1664" y1="1872" y2="1872" x1="1616" />
        </branch>
        <branch name="we2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="2544" type="branch" />
            <wire x2="1616" y1="2544" y2="2544" x1="1600" />
            <wire x2="1664" y1="2544" y2="2544" x1="1616" />
        </branch>
        <branch name="adr2(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="2672" type="branch" />
            <wire x2="1616" y1="2672" y2="2672" x1="1600" />
            <wire x2="1664" y1="2672" y2="2672" x1="1616" />
        </branch>
        <branch name="we3">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="3472" type="branch" />
            <wire x2="1616" y1="3472" y2="3472" x1="1600" />
            <wire x2="1664" y1="3472" y2="3472" x1="1616" />
        </branch>
        <branch name="adr3(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1616" y="3600" type="branch" />
            <wire x2="1616" y1="3600" y2="3600" x1="1600" />
            <wire x2="1664" y1="3600" y2="3600" x1="1616" />
        </branch>
        <branch name="selin3(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="896" y="3296" type="branch" />
            <wire x2="896" y1="3296" y2="3296" x1="880" />
            <wire x2="960" y1="3296" y2="3296" x1="896" />
        </branch>
        <branch name="selin2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="912" y="2368" type="branch" />
            <wire x2="912" y1="2368" y2="2368" x1="896" />
            <wire x2="960" y1="2368" y2="2368" x1="912" />
        </branch>
        <branch name="selin1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="912" y="1568" type="branch" />
            <wire x2="912" y1="1568" y2="1568" x1="896" />
            <wire x2="960" y1="1568" y2="1568" x1="912" />
        </branch>
        <branch name="selin0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="912" y="752" type="branch" />
            <wire x2="912" y1="752" y2="752" x1="896" />
            <wire x2="960" y1="752" y2="752" x1="912" />
        </branch>
        <iomarker fontsize="28" x="864" y="4144" name="clk" orien="R180" />
        <branch name="selreg0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4912" type="branch" />
            <wire x2="2208" y1="4912" y2="4912" x1="2032" />
            <wire x2="2240" y1="4912" y2="4912" x1="2208" />
        </branch>
        <branch name="selreg2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="5040" type="branch" />
            <wire x2="2208" y1="5040" y2="5040" x1="2032" />
            <wire x2="2240" y1="5040" y2="5040" x1="2208" />
        </branch>
        <branch name="selreg3(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="5104" type="branch" />
            <wire x2="2208" y1="5104" y2="5104" x1="2032" />
            <wire x2="2240" y1="5104" y2="5104" x1="2208" />
        </branch>
        <branch name="selreg0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1584" y="752" type="branch" />
            <wire x2="1584" y1="752" y2="752" x1="1568" />
            <wire x2="1664" y1="752" y2="752" x1="1584" />
        </branch>
        <branch name="selreg1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1584" y="1568" type="branch" />
            <wire x2="1584" y1="1568" y2="1568" x1="1568" />
            <wire x2="1664" y1="1568" y2="1568" x1="1584" />
        </branch>
        <branch name="selreg2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1584" y="2368" type="branch" />
            <wire x2="1584" y1="2368" y2="2368" x1="1568" />
            <wire x2="1664" y1="2368" y2="2368" x1="1584" />
        </branch>
        <branch name="selreg3(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1584" y="3296" type="branch" />
            <wire x2="1584" y1="3296" y2="3296" x1="1568" />
            <wire x2="1664" y1="3296" y2="3296" x1="1584" />
        </branch>
        <branch name="Litload">
            <wire x2="224" y1="176" y2="832" x1="224" />
        </branch>
        <iomarker fontsize="28" x="432" y="176" name="Tin(31:0)" orien="R270" />
        <iomarker fontsize="28" x="576" y="176" name="Nin(31:0)" orien="R270" />
        <iomarker fontsize="28" x="736" y="176" name="N2in(31:0)" orien="R270" />
        <branch name="Tin(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="320" y="336" type="branch" />
            <wire x2="160" y1="336" y2="832" x1="160" />
            <wire x2="320" y1="336" y2="336" x1="160" />
            <wire x2="432" y1="336" y2="336" x1="320" />
            <wire x2="432" y1="176" y2="336" x1="432" />
        </branch>
        <branch name="Tin_lit(31:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="336" y="832" type="branch" />
            <wire x2="224" y1="1216" y2="1376" x1="224" />
            <wire x2="336" y1="1376" y2="1376" x1="224" />
            <wire x2="960" y1="1376" y2="1376" x1="336" />
            <wire x2="336" y1="1376" y2="2176" x1="336" />
            <wire x2="336" y1="2176" y2="3104" x1="336" />
            <wire x2="960" y1="3104" y2="3104" x1="336" />
            <wire x2="960" y1="2176" y2="2176" x1="336" />
            <wire x2="496" y1="464" y2="464" x1="336" />
            <wire x2="336" y1="464" y2="560" x1="336" />
            <wire x2="960" y1="560" y2="560" x1="336" />
            <wire x2="336" y1="560" y2="832" x1="336" />
            <wire x2="336" y1="832" y2="1376" x1="336" />
            <wire x2="1904" y1="336" y2="336" x1="496" />
            <wire x2="496" y1="336" y2="464" x1="496" />
        </branch>
        <iomarker fontsize="28" x="96" y="192" name="Lit(11:0)" orien="R270" />
        <branch name="Lit(11:0)">
            <wire x2="96" y1="192" y2="320" x1="96" />
        </branch>
        <instance x="64" y="320" name="XLXI_89" orien="R90">
        </instance>
        <branch name="XLXN_219(31:0)">
            <wire x2="96" y1="704" y2="832" x1="96" />
        </branch>
        <instance x="64" y="832" name="XLXI_87" orien="R90">
        </instance>
        <iomarker fontsize="28" x="224" y="176" name="Litload" orien="R270" />
        <branch name="Litload">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="128" y="4816" type="branch" />
            <wire x2="192" y1="4816" y2="4816" x1="128" />
        </branch>
        <branch name="shortIP">
            <wire x2="192" y1="4880" y2="4880" x1="160" />
        </branch>
        <branch name="ipdone">
            <wire x2="192" y1="4944" y2="4944" x1="160" />
        </branch>
        <branch name="lastcycle">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="480" y="4880" type="branch" />
            <wire x2="80" y1="4464" y2="4704" x1="80" />
            <wire x2="544" y1="4704" y2="4704" x1="80" />
            <wire x2="544" y1="4704" y2="4880" x1="544" />
            <wire x2="544" y1="4880" y2="5168" x1="544" />
            <wire x2="1648" y1="5168" y2="5168" x1="544" />
            <wire x2="288" y1="4464" y2="4464" x1="80" />
            <wire x2="480" y1="4880" y2="4880" x1="448" />
            <wire x2="544" y1="4880" y2="4880" x1="480" />
        </branch>
        <iomarker fontsize="28" x="160" y="4880" name="shortIP" orien="R180" />
        <iomarker fontsize="28" x="160" y="4944" name="ipdone" orien="R180" />
        <instance x="1648" y="4880" name="sel_in" orien="R0">
        </instance>
        <branch name="X(1:0)">
            <wire x2="1648" y1="5232" y2="5232" x1="1424" />
        </branch>
        <branch name="Y(1:0)">
            <wire x2="1648" y1="5296" y2="5296" x1="1424" />
        </branch>
        <iomarker fontsize="28" x="1424" y="5232" name="X(1:0)" orien="R180" />
        <iomarker fontsize="28" x="1424" y="5296" name="Y(1:0)" orien="R180" />
        <branch name="X(1:0)">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="192" y="4528" type="branch" />
            <wire x2="288" y1="4528" y2="4528" x1="192" />
        </branch>
        <branch name="Y(1:0)">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="192" y="4592" type="branch" />
            <wire x2="288" y1="4592" y2="4592" x1="192" />
        </branch>
        <branch name="selreg1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2208" y="4976" type="branch" />
            <wire x2="2208" y1="4976" y2="4976" x1="2032" />
            <wire x2="2240" y1="4976" y2="4976" x1="2208" />
        </branch>
        <iomarker fontsize="28" x="912" y="112" name="reset" orien="R270" />
        <branch name="reset">
            <wire x2="912" y1="112" y2="240" x1="912" />
        </branch>
        <instance x="192" y="5008" name="XLXI_91" orien="R0" />
        <instance x="928" y="4272" name="XLXI_93" orien="R0" />
        <instance x="3856" y="4560" name="XLXI_94" orien="R0">
        </instance>
        <branch name="oversized(31:0)">
            <wire x2="4368" y1="4272" y2="4272" x1="4240" />
        </branch>
        <instance x="2080" y="3984" name="sel_out" orien="R0">
        </instance>
        <branch name="XLXN_223(1:0)">
            <wire x2="3152" y1="4016" y2="4016" x1="2464" />
            <wire x2="3152" y1="4016" y2="4528" x1="3152" />
            <wire x2="3856" y1="4528" y2="4528" x1="3152" />
        </branch>
        <branch name="XLXN_224(31:0)">
            <wire x2="2608" y1="432" y2="432" x1="1584" />
            <wire x2="2608" y1="432" y2="544" x1="2608" />
            <wire x2="2608" y1="544" y2="560" x1="2608" />
            <wire x2="2800" y1="544" y2="544" x1="2608" />
            <wire x2="2800" y1="544" y2="1536" x1="2800" />
            <wire x2="2800" y1="1536" y2="2464" x1="2800" />
            <wire x2="2800" y1="2464" y2="4272" x1="2800" />
            <wire x2="3856" y1="4272" y2="4272" x1="2800" />
            <wire x2="3792" y1="2464" y2="2464" x1="2800" />
            <wire x2="3792" y1="1536" y2="1536" x1="2800" />
            <wire x2="3776" y1="544" y2="544" x1="2800" />
            <wire x2="1584" y1="432" y2="560" x1="1584" />
            <wire x2="1664" y1="560" y2="560" x1="1584" />
            <wire x2="2608" y1="560" y2="560" x1="2576" />
        </branch>
        <branch name="XLXN_225(31:0)">
            <wire x2="2656" y1="1248" y2="1248" x1="1648" />
            <wire x2="2656" y1="1248" y2="1376" x1="2656" />
            <wire x2="2880" y1="1376" y2="1376" x1="2656" />
            <wire x2="2880" y1="1376" y2="1600" x1="2880" />
            <wire x2="3792" y1="1600" y2="1600" x1="2880" />
            <wire x2="2880" y1="1600" y2="2528" x1="2880" />
            <wire x2="3792" y1="2528" y2="2528" x1="2880" />
            <wire x2="2880" y1="2528" y2="4336" x1="2880" />
            <wire x2="3856" y1="4336" y2="4336" x1="2880" />
            <wire x2="1648" y1="1248" y2="1376" x1="1648" />
            <wire x2="1664" y1="1376" y2="1376" x1="1648" />
            <wire x2="2656" y1="1376" y2="1376" x1="2576" />
            <wire x2="3776" y1="608" y2="608" x1="2880" />
            <wire x2="2880" y1="608" y2="1376" x1="2880" />
        </branch>
        <branch name="XLXN_226(31:0)">
            <wire x2="1600" y1="2064" y2="2176" x1="1600" />
            <wire x2="1664" y1="2176" y2="2176" x1="1600" />
            <wire x2="2640" y1="2064" y2="2064" x1="1600" />
            <wire x2="2640" y1="2064" y2="2176" x1="2640" />
            <wire x2="2992" y1="2176" y2="2176" x1="2640" />
            <wire x2="2992" y1="2176" y2="2592" x1="2992" />
            <wire x2="3792" y1="2592" y2="2592" x1="2992" />
            <wire x2="2992" y1="2592" y2="4400" x1="2992" />
            <wire x2="3856" y1="4400" y2="4400" x1="2992" />
            <wire x2="2640" y1="2176" y2="2176" x1="2576" />
            <wire x2="3776" y1="672" y2="672" x1="2992" />
            <wire x2="2992" y1="672" y2="1664" x1="2992" />
            <wire x2="3792" y1="1664" y2="1664" x1="2992" />
            <wire x2="2992" y1="1664" y2="2176" x1="2992" />
        </branch>
        <branch name="XLXN_227(31:0)">
            <wire x2="1600" y1="2992" y2="3104" x1="1600" />
            <wire x2="1664" y1="3104" y2="3104" x1="1600" />
            <wire x2="2640" y1="2992" y2="2992" x1="1600" />
            <wire x2="2640" y1="2992" y2="3104" x1="2640" />
            <wire x2="3088" y1="3104" y2="3104" x1="2640" />
            <wire x2="3088" y1="3104" y2="4464" x1="3088" />
            <wire x2="3856" y1="4464" y2="4464" x1="3088" />
            <wire x2="2640" y1="3104" y2="3104" x1="2576" />
            <wire x2="3776" y1="736" y2="736" x1="3088" />
            <wire x2="3088" y1="736" y2="1728" x1="3088" />
            <wire x2="3792" y1="1728" y2="1728" x1="3088" />
            <wire x2="3088" y1="1728" y2="2656" x1="3088" />
            <wire x2="3792" y1="2656" y2="2656" x1="3088" />
            <wire x2="3088" y1="2656" y2="3104" x1="3088" />
        </branch>
        <instance x="3776" y="832" name="muxoutT" orien="R0">
        </instance>
        <iomarker fontsize="28" x="4288" y="544" name="Tout(31:0)" orien="R0" />
        <iomarker fontsize="28" x="4368" y="4272" name="oversized(31:0)" orien="R0" />
        <instance x="1904" y="368" name="XLXI_99" orien="R0">
        </instance>
        <instance x="3184" y="320" name="XLXI_98" orien="R0">
        </instance>
        <iomarker fontsize="28" x="4272" y="256" name="offset" orien="R0" />
        <branch name="offset">
            <wire x2="4272" y1="256" y2="256" x1="3968" />
        </branch>
        <instance x="3648" y="384" name="XLXI_100" orien="R0" />
        <branch name="XLXN_242">
            <wire x2="3648" y1="288" y2="288" x1="3568" />
        </branch>
        <branch name="XLXN_243">
            <wire x2="2960" y1="336" y2="336" x1="2288" />
            <wire x2="2960" y1="224" y2="336" x1="2960" />
            <wire x2="3648" y1="224" y2="224" x1="2960" />
        </branch>
        <branch name="selpredicat">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3584" y="352" type="branch" />
            <wire x2="3584" y1="352" y2="352" x1="3568" />
            <wire x2="3648" y1="352" y2="352" x1="3584" />
        </branch>
        <branch name="selpredicat">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2176" y="5360" type="branch" />
            <wire x2="2176" y1="5360" y2="5360" x1="2032" />
            <wire x2="2240" y1="5360" y2="5360" x1="2176" />
        </branch>
    </sheet>
</drawing>