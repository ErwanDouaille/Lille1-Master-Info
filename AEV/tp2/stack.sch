<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="virtex6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="Nin(31:0)" />
        <signal name="N2in(31:0)" />
        <signal name="push1" />
        <signal name="push2" />
        <signal name="push3" />
        <signal name="pop1" />
        <signal name="pop2" />
        <signal name="pop3" />
        <signal name="Top(5:0)" />
        <signal name="XLXN_22(31:0)" />
        <signal name="XLXN_43(31:0)" />
        <signal name="XLXN_44(31:0)" />
        <signal name="XLXN_45(31:0)" />
        <signal name="clk">
            <attr value="yes" name="clock_signal">
                <trait verilog="all:0 wsynth:1" />
                <trait vhdl="all:0 wa:1 wd:1" />
            </attr>
        </signal>
        <signal name="write_enable" />
        <signal name="XLXN_53(31:0)" />
        <signal name="Top(1:0)" />
        <signal name="ena0" />
        <signal name="ena1" />
        <signal name="ena2" />
        <signal name="Top(5:2)" />
        <signal name="adr0(3:0)" />
        <signal name="adr1(3:0)" />
        <signal name="adr2(3:0)" />
        <signal name="selin0(1:0)" />
        <signal name="selin1(1:0)" />
        <signal name="selin2(1:0)" />
        <signal name="selout0(1:0)" />
        <signal name="selout1(1:0)" />
        <signal name="selout2(1:0)" />
        <signal name="Tout(31:0)" />
        <signal name="N2out(31:0)" />
        <signal name="Nout(31:0)" />
        <signal name="XLXN_92(31:0)" />
        <signal name="selin3(1:0)" />
        <signal name="XLXN_96(31:0)" />
        <signal name="XLXN_97(31:0)" />
        <signal name="Tin(31:0)" />
        <signal name="XLXN_103(3:0)" />
        <signal name="XLXN_104" />
        <signal name="clr" />
        <signal name="XLXN_106" />
        <port polarity="Input" name="Nin(31:0)" />
        <port polarity="Input" name="N2in(31:0)" />
        <port polarity="Input" name="push1" />
        <port polarity="Input" name="push2" />
        <port polarity="Input" name="push3" />
        <port polarity="Input" name="pop1" />
        <port polarity="Input" name="pop2" />
        <port polarity="Input" name="pop3" />
        <port polarity="Input" name="Top(5:0)" />
        <port polarity="Input" name="clk" />
        <port polarity="Output" name="Tout(31:0)" />
        <port polarity="Output" name="N2out(31:0)" />
        <port polarity="Output" name="Nout(31:0)" />
        <port polarity="Input" name="Tin(31:0)" />
        <port polarity="Input" name="clr" />
        <blockdef name="Ram8">
            <timestamp>2013-4-4T14:12:57</timestamp>
            <line x2="0" y1="32" y2="32" x1="64" />
            <line x2="0" y1="-288" y2="-288" x1="64" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <rect width="64" x="0" y="-108" height="24" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-300" height="24" />
            <line x2="384" y1="-288" y2="-288" x1="320" />
            <rect style="fillcolor:rgb(0,255,255)" width="256" x="64" y="-320" height="384" />
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
        <blockdef name="enable_bank">
            <timestamp>2012-11-7T12:29:2</timestamp>
            <rect width="64" x="0" y="20" height="24" />
            <line x2="0" y1="32" y2="32" x1="64" />
            <line x2="0" y1="-416" y2="-416" x1="64" />
            <line x2="0" y1="-352" y2="-352" x1="64" />
            <line x2="0" y1="-288" y2="-288" x1="64" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <rect width="64" x="0" y="-44" height="24" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <line x2="384" y1="-416" y2="-416" x1="320" />
            <rect width="256" x="64" y="-448" height="952" />
            <line x2="384" y1="-368" y2="-368" x1="320" />
            <line x2="384" y1="-320" y2="-320" x1="320" />
            <line x2="384" y1="-272" y2="-272" x1="320" />
            <rect width="64" x="320" y="-204" height="24" />
            <line x2="384" y1="-192" y2="-192" x1="320" />
            <rect width="64" x="320" y="-140" height="24" />
            <line x2="384" y1="-128" y2="-128" x1="320" />
            <rect width="64" x="320" y="-76" height="24" />
            <line x2="384" y1="-64" y2="-64" x1="320" />
            <rect width="64" x="320" y="-12" height="24" />
            <line x2="384" y1="0" y2="0" x1="320" />
            <rect width="64" x="320" y="68" height="24" />
            <line x2="384" y1="80" y2="80" x1="320" />
            <rect width="64" x="320" y="132" height="24" />
            <line x2="384" y1="144" y2="144" x1="320" />
            <rect width="64" x="320" y="196" height="24" />
            <line x2="384" y1="208" y2="208" x1="320" />
            <rect width="64" x="320" y="260" height="24" />
            <line x2="384" y1="272" y2="272" x1="320" />
            <rect width="64" x="320" y="324" height="24" />
            <line x2="384" y1="336" y2="336" x1="320" />
            <rect width="64" x="320" y="388" height="24" />
            <line x2="384" y1="400" y2="400" x1="320" />
            <rect width="64" x="320" y="452" height="24" />
            <line x2="384" y1="464" y2="464" x1="320" />
        </blockdef>
        <blockdef name="Mux4">
            <timestamp>2012-11-7T7:15:39</timestamp>
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
        <blockdef name="Mux3_32">
            <timestamp>2012-11-7T11:12:27</timestamp>
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
        <block symbolname="or3" name="Ou3">
            <blockpin signalname="push3" name="I0" />
            <blockpin signalname="push2" name="I1" />
            <blockpin signalname="push1" name="I2" />
            <blockpin signalname="write_enable" name="O" />
        </block>
        <block symbolname="enable_bank" name="enablebank">
            <blockpin signalname="pop1" name="pop1" />
            <blockpin signalname="pop2" name="pop2" />
            <blockpin signalname="pop3" name="pop3" />
            <blockpin signalname="push1" name="push1" />
            <blockpin signalname="push2" name="push2" />
            <blockpin signalname="push3" name="push3" />
            <blockpin signalname="Top(1:0)" name="sel(1:0)" />
            <blockpin signalname="Top(5:2)" name="adr(3:0)" />
            <blockpin signalname="ena0" name="ena0" />
            <blockpin signalname="ena1" name="ena1" />
            <blockpin signalname="ena2" name="ena2" />
            <blockpin signalname="XLXN_106" name="ena3" />
            <blockpin signalname="adr0(3:0)" name="adr0(3:0)" />
            <blockpin signalname="adr1(3:0)" name="adr1(3:0)" />
            <blockpin signalname="adr2(3:0)" name="adr2(3:0)" />
            <blockpin signalname="XLXN_103(3:0)" name="adr3(3:0)" />
            <blockpin signalname="selin0(1:0)" name="selin0(1:0)" />
            <blockpin signalname="selin1(1:0)" name="selin1(1:0)" />
            <blockpin signalname="selin2(1:0)" name="selin2(1:0)" />
            <blockpin signalname="selin3(1:0)" name="selin3(1:0)" />
            <blockpin signalname="selout0(1:0)" name="selout0(1:0)" />
            <blockpin signalname="selout1(1:0)" name="selout1(1:0)" />
            <blockpin signalname="selout2(1:0)" name="selout2(1:0)" />
        </block>
        <block symbolname="Mux4" name="muxout0">
            <blockpin signalname="XLXN_43(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_44(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_45(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_92(31:0)" name="X3(31:0)" />
            <blockpin signalname="selout0(1:0)" name="sel(1:0)" />
            <blockpin signalname="Tout(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux4" name="muxout1">
            <blockpin signalname="XLXN_43(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_44(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_45(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_92(31:0)" name="X3(31:0)" />
            <blockpin signalname="selout1(1:0)" name="sel(1:0)" />
            <blockpin signalname="Nout(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux4" name="muxout2">
            <blockpin signalname="XLXN_43(31:0)" name="X0(31:0)" />
            <blockpin signalname="XLXN_44(31:0)" name="X1(31:0)" />
            <blockpin signalname="XLXN_45(31:0)" name="X2(31:0)" />
            <blockpin signalname="XLXN_92(31:0)" name="X3(31:0)" />
            <blockpin signalname="selout2(1:0)" name="sel(1:0)" />
            <blockpin signalname="N2out(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxin0">
            <blockpin signalname="Tin(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin0(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_53(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxin2">
            <blockpin signalname="Tin(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin2(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_96(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxin3">
            <blockpin signalname="Tin(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin3(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_97(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Mux3_32" name="muxin1">
            <blockpin signalname="Tin(31:0)" name="X0(31:0)" />
            <blockpin signalname="Nin(31:0)" name="X1(31:0)" />
            <blockpin signalname="N2in(31:0)" name="X2(31:0)" />
            <blockpin signalname="selin1(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_22(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="Ram8" name="XLXI_1">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="write_enable" name="we" />
            <blockpin signalname="ena0" name="en" />
            <blockpin signalname="adr0(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_53(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_43(31:0)" name="dataout(31:0)" />
            <blockpin signalname="clr" name="clr" />
        </block>
        <block symbolname="Ram8" name="XLXI_2">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="write_enable" name="we" />
            <blockpin signalname="ena1" name="en" />
            <blockpin signalname="adr1(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_22(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_44(31:0)" name="dataout(31:0)" />
            <blockpin signalname="clr" name="clr" />
        </block>
        <block symbolname="Ram8" name="XLXI_4">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="write_enable" name="we" />
            <blockpin signalname="XLXN_106" name="en" />
            <blockpin signalname="XLXN_103(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_97(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_92(31:0)" name="dataout(31:0)" />
            <blockpin signalname="clr" name="clr" />
        </block>
        <block symbolname="Ram8" name="XLXI_3">
            <blockpin signalname="clk" name="clk" />
            <blockpin signalname="write_enable" name="we" />
            <blockpin signalname="ena2" name="en" />
            <blockpin signalname="adr2(3:0)" name="addr(3:0)" />
            <blockpin signalname="XLXN_96(31:0)" name="datain(31:0)" />
            <blockpin signalname="XLXN_45(31:0)" name="dataout(31:0)" />
            <blockpin signalname="clr" name="clr" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="7040" height="5440">
        <branch name="N2in(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="384" y="768" type="branch" />
            <wire x2="384" y1="768" y2="768" x1="288" />
            <wire x2="464" y1="768" y2="768" x1="384" />
            <wire x2="976" y1="768" y2="768" x1="464" />
            <wire x2="1792" y1="768" y2="768" x1="976" />
            <wire x2="2448" y1="768" y2="768" x1="1792" />
            <wire x2="672" y1="592" y2="592" x1="464" />
            <wire x2="464" y1="592" y2="768" x1="464" />
            <wire x2="1456" y1="592" y2="592" x1="976" />
            <wire x2="976" y1="592" y2="768" x1="976" />
            <wire x2="1792" y1="576" y2="768" x1="1792" />
            <wire x2="2160" y1="576" y2="576" x1="1792" />
            <wire x2="2448" y1="576" y2="768" x1="2448" />
            <wire x2="3040" y1="576" y2="576" x1="2448" />
        </branch>
        <branch name="push1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="336" y="896" type="branch" />
            <wire x2="336" y1="896" y2="896" x1="272" />
            <wire x2="432" y1="896" y2="896" x1="336" />
            <wire x2="464" y1="896" y2="896" x1="432" />
            <wire x2="432" y1="896" y2="1888" x1="432" />
            <wire x2="544" y1="1888" y2="1888" x1="432" />
            <wire x2="576" y1="848" y2="848" x1="464" />
            <wire x2="464" y1="848" y2="896" x1="464" />
        </branch>
        <branch name="push2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="336" y="992" type="branch" />
            <wire x2="336" y1="992" y2="992" x1="272" />
            <wire x2="464" y1="992" y2="992" x1="336" />
            <wire x2="528" y1="992" y2="992" x1="464" />
            <wire x2="528" y1="992" y2="1952" x1="528" />
            <wire x2="544" y1="1952" y2="1952" x1="528" />
            <wire x2="576" y1="912" y2="912" x1="464" />
            <wire x2="464" y1="912" y2="992" x1="464" />
        </branch>
        <branch name="push3">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="320" y="1088" type="branch" />
            <wire x2="320" y1="1088" y2="1088" x1="272" />
            <wire x2="480" y1="1088" y2="1088" x1="320" />
            <wire x2="576" y1="1088" y2="1088" x1="480" />
            <wire x2="480" y1="1024" y2="1024" x1="336" />
            <wire x2="480" y1="1024" y2="1088" x1="480" />
            <wire x2="336" y1="1024" y2="2016" x1="336" />
            <wire x2="544" y1="2016" y2="2016" x1="336" />
            <wire x2="576" y1="976" y2="1088" x1="576" />
        </branch>
        <branch name="pop1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="352" y="1200" type="branch" />
            <wire x2="352" y1="1200" y2="1200" x1="272" />
            <wire x2="496" y1="1200" y2="1200" x1="352" />
            <wire x2="496" y1="1200" y2="1696" x1="496" />
            <wire x2="544" y1="1696" y2="1696" x1="496" />
        </branch>
        <branch name="pop2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="352" y="1280" type="branch" />
            <wire x2="352" y1="1280" y2="1280" x1="272" />
            <wire x2="480" y1="1280" y2="1280" x1="352" />
            <wire x2="480" y1="1280" y2="1760" x1="480" />
            <wire x2="544" y1="1760" y2="1760" x1="480" />
        </branch>
        <branch name="pop3">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="352" y="1360" type="branch" />
            <wire x2="352" y1="1360" y2="1360" x1="272" />
            <wire x2="512" y1="1360" y2="1360" x1="352" />
            <wire x2="512" y1="1360" y2="1824" x1="512" />
            <wire x2="544" y1="1824" y2="1824" x1="512" />
        </branch>
        <iomarker fontsize="28" x="272" y="576" name="Nin(31:0)" orien="R180" />
        <iomarker fontsize="28" x="288" y="768" name="N2in(31:0)" orien="R180" />
        <iomarker fontsize="28" x="272" y="896" name="push1" orien="R180" />
        <iomarker fontsize="28" x="272" y="992" name="push2" orien="R180" />
        <iomarker fontsize="28" x="272" y="1088" name="push3" orien="R180" />
        <iomarker fontsize="28" x="272" y="1200" name="pop1" orien="R180" />
        <iomarker fontsize="28" x="272" y="1280" name="pop2" orien="R180" />
        <iomarker fontsize="28" x="272" y="1360" name="pop3" orien="R180" />
        <branch name="Top(5:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="320" y="1456" type="branch" />
            <wire x2="320" y1="1456" y2="1456" x1="272" />
            <wire x2="368" y1="1456" y2="1456" x1="320" />
            <wire x2="416" y1="1456" y2="1456" x1="368" />
            <wire x2="464" y1="1456" y2="1456" x1="416" />
        </branch>
        <iomarker fontsize="28" x="272" y="1456" name="Top(5:0)" orien="R180" />
        <iomarker fontsize="28" x="256" y="128" name="Tin(31:0)" orien="R180" />
        <branch name="XLXN_22(31:0)">
            <wire x2="1728" y1="832" y2="1040" x1="1728" />
            <wire x2="1792" y1="1040" y2="1040" x1="1728" />
            <wire x2="1792" y1="1040" y2="1248" x1="1792" />
            <wire x2="1904" y1="1248" y2="1248" x1="1792" />
            <wire x2="1824" y1="832" y2="832" x1="1728" />
            <wire x2="1824" y1="464" y2="464" x1="1776" />
            <wire x2="1824" y1="464" y2="832" x1="1824" />
        </branch>
        <branch name="XLXN_43(31:0)">
            <wire x2="1568" y1="1024" y2="1024" x1="1376" />
            <wire x2="1568" y1="1024" y2="1520" x1="1568" />
            <wire x2="2112" y1="1520" y2="1520" x1="1568" />
            <wire x2="2784" y1="1520" y2="1520" x1="2112" />
            <wire x2="2112" y1="1520" y2="1872" x1="2112" />
            <wire x2="2112" y1="1872" y2="2224" x1="2112" />
            <wire x2="2784" y1="2224" y2="2224" x1="2112" />
            <wire x2="2784" y1="1872" y2="1872" x1="2112" />
        </branch>
        <branch name="XLXN_44(31:0)">
            <wire x2="2336" y1="992" y2="992" x1="2288" />
            <wire x2="2336" y1="992" y2="1008" x1="2336" />
            <wire x2="2448" y1="1008" y2="1008" x1="2336" />
            <wire x2="2448" y1="1008" y2="1584" x1="2448" />
            <wire x2="2784" y1="1584" y2="1584" x1="2448" />
            <wire x2="2448" y1="1584" y2="1936" x1="2448" />
            <wire x2="2448" y1="1936" y2="2288" x1="2448" />
            <wire x2="2784" y1="2288" y2="2288" x1="2448" />
            <wire x2="2784" y1="1936" y2="1936" x1="2448" />
        </branch>
        <branch name="XLXN_45(31:0)">
            <wire x2="2512" y1="1408" y2="1648" x1="2512" />
            <wire x2="2784" y1="1648" y2="1648" x1="2512" />
            <wire x2="2512" y1="1648" y2="2000" x1="2512" />
            <wire x2="2512" y1="2000" y2="2352" x1="2512" />
            <wire x2="2784" y1="2352" y2="2352" x1="2512" />
            <wire x2="2784" y1="2000" y2="2000" x1="2512" />
            <wire x2="3168" y1="1408" y2="1408" x1="2512" />
            <wire x2="3168" y1="1008" y2="1008" x1="2992" />
            <wire x2="3168" y1="1008" y2="1408" x1="3168" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="384" y="1616" type="branch" />
            <wire x2="384" y1="1616" y2="1616" x1="272" />
            <wire x2="912" y1="1616" y2="1616" x1="384" />
            <wire x2="1552" y1="1616" y2="1616" x1="912" />
            <wire x2="912" y1="1216" y2="1616" x1="912" />
            <wire x2="928" y1="1216" y2="1216" x1="912" />
            <wire x2="928" y1="1024" y2="1216" x1="928" />
            <wire x2="992" y1="1024" y2="1024" x1="928" />
            <wire x2="1552" y1="1200" y2="1392" x1="1552" />
            <wire x2="1552" y1="1392" y2="1616" x1="1552" />
            <wire x2="2400" y1="1392" y2="1392" x1="1552" />
            <wire x2="3216" y1="1392" y2="1392" x1="2400" />
            <wire x2="1808" y1="1200" y2="1200" x1="1552" />
            <wire x2="1808" y1="992" y2="1200" x1="1808" />
            <wire x2="1904" y1="992" y2="992" x1="1808" />
            <wire x2="2400" y1="1200" y2="1392" x1="2400" />
            <wire x2="2544" y1="1200" y2="1200" x1="2400" />
            <wire x2="2544" y1="1008" y2="1200" x1="2544" />
            <wire x2="2608" y1="1008" y2="1008" x1="2544" />
            <wire x2="3440" y1="1008" y2="1008" x1="3216" />
            <wire x2="3216" y1="1008" y2="1392" x1="3216" />
        </branch>
        <iomarker fontsize="28" x="272" y="1616" name="clk" orien="R180" />
        <instance x="576" y="1040" name="Ou3" orien="R0" />
        <branch name="write_enable">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1264" y="880" type="branch" />
            <wire x2="864" y1="912" y2="912" x1="832" />
            <wire x2="864" y1="912" y2="1152" x1="864" />
            <wire x2="944" y1="1152" y2="1152" x1="864" />
            <wire x2="864" y1="880" y2="912" x1="864" />
            <wire x2="1264" y1="880" y2="880" x1="864" />
            <wire x2="1552" y1="880" y2="880" x1="1264" />
            <wire x2="1552" y1="880" y2="1136" x1="1552" />
            <wire x2="1824" y1="1136" y2="1136" x1="1552" />
            <wire x2="2400" y1="880" y2="880" x1="1552" />
            <wire x2="2400" y1="880" y2="1136" x1="2400" />
            <wire x2="2560" y1="1136" y2="1136" x1="2400" />
            <wire x2="3152" y1="880" y2="880" x1="2400" />
            <wire x2="3152" y1="880" y2="1072" x1="3152" />
            <wire x2="3440" y1="1072" y2="1072" x1="3152" />
            <wire x2="944" y1="1088" y2="1152" x1="944" />
            <wire x2="992" y1="1088" y2="1088" x1="944" />
            <wire x2="1824" y1="1056" y2="1136" x1="1824" />
            <wire x2="1904" y1="1056" y2="1056" x1="1824" />
            <wire x2="2560" y1="1072" y2="1136" x1="2560" />
            <wire x2="2608" y1="1072" y2="1072" x1="2560" />
        </branch>
        <branch name="XLXN_53(31:0)">
            <wire x2="896" y1="896" y2="1056" x1="896" />
            <wire x2="912" y1="1056" y2="1056" x1="896" />
            <wire x2="912" y1="1056" y2="1072" x1="912" />
            <wire x2="1056" y1="896" y2="896" x1="896" />
            <wire x2="912" y1="1072" y2="1072" x1="896" />
            <wire x2="896" y1="1072" y2="1280" x1="896" />
            <wire x2="992" y1="1280" y2="1280" x1="896" />
            <wire x2="1056" y1="464" y2="464" x1="992" />
            <wire x2="1056" y1="464" y2="896" x1="1056" />
        </branch>
        <instance x="544" y="2112" name="enablebank" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="144" y="-432" type="instance" />
        </instance>
        <branch name="Top(1:0)">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="368" y="1816" type="branch" />
            <wire x2="368" y1="1552" y2="2080" x1="368" />
            <wire x2="544" y1="2080" y2="2080" x1="368" />
        </branch>
        <bustap x2="368" y1="1456" y2="1552" x1="368" />
        <branch name="ena0">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="992" y="1504" type="branch" />
            <wire x2="880" y1="1088" y2="1376" x1="880" />
            <wire x2="992" y1="1376" y2="1376" x1="880" />
            <wire x2="992" y1="1376" y2="1504" x1="992" />
            <wire x2="992" y1="1504" y2="1696" x1="992" />
            <wire x2="912" y1="1088" y2="1088" x1="880" />
            <wire x2="912" y1="1088" y2="1104" x1="912" />
            <wire x2="960" y1="1104" y2="1104" x1="912" />
            <wire x2="960" y1="1104" y2="1152" x1="960" />
            <wire x2="992" y1="1152" y2="1152" x1="960" />
            <wire x2="992" y1="1696" y2="1696" x1="928" />
        </branch>
        <branch name="ena1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1200" y="1744" type="branch" />
            <wire x2="1200" y1="1744" y2="1744" x1="928" />
            <wire x2="1536" y1="1744" y2="1744" x1="1200" />
            <wire x2="1536" y1="1072" y2="1744" x1="1536" />
            <wire x2="1760" y1="1072" y2="1072" x1="1536" />
            <wire x2="1760" y1="1072" y2="1120" x1="1760" />
            <wire x2="1904" y1="1120" y2="1120" x1="1760" />
        </branch>
        <branch name="ena2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1216" y="1792" type="branch" />
            <wire x2="1216" y1="1792" y2="1792" x1="928" />
            <wire x2="2384" y1="1792" y2="1792" x1="1216" />
            <wire x2="2512" y1="1072" y2="1072" x1="2384" />
            <wire x2="2384" y1="1072" y2="1792" x1="2384" />
            <wire x2="2512" y1="1056" y2="1072" x1="2512" />
            <wire x2="2576" y1="1056" y2="1056" x1="2512" />
            <wire x2="2576" y1="1056" y2="1136" x1="2576" />
            <wire x2="2608" y1="1136" y2="1136" x1="2576" />
        </branch>
        <branch name="Top(5:2)">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="416" y="1848" type="branch" />
            <wire x2="416" y1="1552" y2="2144" x1="416" />
            <wire x2="544" y1="2144" y2="2144" x1="416" />
        </branch>
        <bustap x2="416" y1="1456" y2="1552" x1="416" />
        <branch name="adr0(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1008" y="1920" type="branch" />
            <wire x2="752" y1="1024" y2="1456" x1="752" />
            <wire x2="1104" y1="1456" y2="1456" x1="752" />
            <wire x2="1104" y1="1456" y2="1920" x1="1104" />
            <wire x2="912" y1="1024" y2="1024" x1="752" />
            <wire x2="912" y1="960" y2="1024" x1="912" />
            <wire x2="976" y1="960" y2="960" x1="912" />
            <wire x2="976" y1="960" y2="1216" x1="976" />
            <wire x2="992" y1="1216" y2="1216" x1="976" />
            <wire x2="1008" y1="1920" y2="1920" x1="928" />
            <wire x2="1104" y1="1920" y2="1920" x1="1008" />
        </branch>
        <branch name="adr1(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1056" y="1984" type="branch" />
            <wire x2="1056" y1="1984" y2="1984" x1="928" />
            <wire x2="1520" y1="1984" y2="1984" x1="1056" />
            <wire x2="1520" y1="1008" y2="1984" x1="1520" />
            <wire x2="1776" y1="1008" y2="1008" x1="1520" />
            <wire x2="1776" y1="1008" y2="1184" x1="1776" />
            <wire x2="1904" y1="1184" y2="1184" x1="1776" />
        </branch>
        <branch name="adr2(3:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1088" y="2048" type="branch" />
            <wire x2="1088" y1="2048" y2="2048" x1="928" />
            <wire x2="2496" y1="2048" y2="2048" x1="1088" />
            <wire x2="2512" y1="1008" y2="1008" x1="2496" />
            <wire x2="2496" y1="1008" y2="2048" x1="2496" />
            <wire x2="2512" y1="960" y2="1008" x1="2512" />
            <wire x2="2592" y1="960" y2="960" x1="2512" />
            <wire x2="2592" y1="960" y2="1200" x1="2592" />
            <wire x2="2608" y1="1200" y2="1200" x1="2592" />
        </branch>
        <branch name="selin0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="592" y="656" type="branch" />
            <wire x2="560" y1="656" y2="1632" x1="560" />
            <wire x2="960" y1="1632" y2="1632" x1="560" />
            <wire x2="960" y1="1632" y2="2192" x1="960" />
            <wire x2="592" y1="656" y2="656" x1="560" />
            <wire x2="672" y1="656" y2="656" x1="592" />
            <wire x2="960" y1="2192" y2="2192" x1="928" />
        </branch>
        <branch name="selin1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1408" y="656" type="branch" />
            <wire x2="1648" y1="2256" y2="2256" x1="928" />
            <wire x2="1392" y1="656" y2="928" x1="1392" />
            <wire x2="1648" y1="928" y2="928" x1="1392" />
            <wire x2="1648" y1="928" y2="2256" x1="1648" />
            <wire x2="1408" y1="656" y2="656" x1="1392" />
            <wire x2="1456" y1="656" y2="656" x1="1408" />
        </branch>
        <branch name="selin2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2016" y="640" type="branch" />
            <wire x2="1744" y1="2320" y2="2320" x1="928" />
            <wire x2="1744" y1="640" y2="2320" x1="1744" />
            <wire x2="2016" y1="640" y2="640" x1="1744" />
            <wire x2="2160" y1="640" y2="640" x1="2016" />
        </branch>
        <branch name="selout0(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1104" y="2448" type="branch" />
            <wire x2="1104" y1="2448" y2="2448" x1="928" />
            <wire x2="1728" y1="2448" y2="2448" x1="1104" />
            <wire x2="2784" y1="1776" y2="1776" x1="1728" />
            <wire x2="1728" y1="1776" y2="2448" x1="1728" />
        </branch>
        <branch name="selout1(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1120" y="2512" type="branch" />
            <wire x2="1120" y1="2512" y2="2512" x1="928" />
            <wire x2="1712" y1="2512" y2="2512" x1="1120" />
            <wire x2="2784" y1="2128" y2="2128" x1="1712" />
            <wire x2="1712" y1="2128" y2="2512" x1="1712" />
        </branch>
        <branch name="selout2(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1136" y="2576" type="branch" />
            <wire x2="1136" y1="2576" y2="2576" x1="928" />
            <wire x2="1760" y1="2576" y2="2576" x1="1136" />
            <wire x2="2784" y1="2480" y2="2480" x1="1760" />
            <wire x2="1760" y1="2480" y2="2576" x1="1760" />
        </branch>
        <branch name="Tout(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3760" y="1632" type="branch" />
            <wire x2="3728" y1="1520" y2="1520" x1="3168" />
            <wire x2="3728" y1="1520" y2="1632" x1="3728" />
            <wire x2="3760" y1="1632" y2="1632" x1="3728" />
            <wire x2="3824" y1="1632" y2="1632" x1="3760" />
        </branch>
        <branch name="N2out(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3760" y="1936" type="branch" />
            <wire x2="3728" y1="2224" y2="2224" x1="3168" />
            <wire x2="3760" y1="1936" y2="1936" x1="3728" />
            <wire x2="3824" y1="1936" y2="1936" x1="3760" />
            <wire x2="3728" y1="1936" y2="2224" x1="3728" />
        </branch>
        <branch name="Nout(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3760" y="1776" type="branch" />
            <wire x2="3744" y1="1872" y2="1872" x1="3168" />
            <wire x2="3760" y1="1776" y2="1776" x1="3744" />
            <wire x2="3840" y1="1776" y2="1776" x1="3760" />
            <wire x2="3744" y1="1776" y2="1872" x1="3744" />
        </branch>
        <iomarker fontsize="28" x="3824" y="1632" name="Tout(31:0)" orien="R0" />
        <iomarker fontsize="28" x="3824" y="1936" name="N2out(31:0)" orien="R0" />
        <iomarker fontsize="28" x="3840" y="1776" name="Nout(31:0)" orien="R0" />
        <instance x="2784" y="1808" name="muxout0" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="192" y="-192" type="instance" />
        </instance>
        <instance x="2784" y="2160" name="muxout1" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="176" y="-176" type="instance" />
        </instance>
        <instance x="2784" y="2512" name="muxout2" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="192" y="-160" type="instance" />
        </instance>
        <branch name="XLXN_92(31:0)">
            <wire x2="2704" y1="1472" y2="1712" x1="2704" />
            <wire x2="2784" y1="1712" y2="1712" x1="2704" />
            <wire x2="2704" y1="1712" y2="2064" x1="2704" />
            <wire x2="2784" y1="2064" y2="2064" x1="2704" />
            <wire x2="2704" y1="2064" y2="2416" x1="2704" />
            <wire x2="2784" y1="2416" y2="2416" x1="2704" />
            <wire x2="4032" y1="1472" y2="1472" x1="2704" />
            <wire x2="4032" y1="1008" y2="1008" x1="3824" />
            <wire x2="4032" y1="1008" y2="1472" x1="4032" />
        </branch>
        <branch name="selin3(1:0)">
            <wire x2="1504" y1="2384" y2="2384" x1="928" />
            <wire x2="1504" y1="944" y2="2384" x1="1504" />
            <wire x2="2512" y1="944" y2="944" x1="1504" />
            <wire x2="2512" y1="640" y2="944" x1="2512" />
            <wire x2="3040" y1="640" y2="640" x1="2512" />
        </branch>
        <instance x="672" y="688" name="muxin0" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="208" y="-128" type="instance" />
        </instance>
        <instance x="2160" y="672" name="muxin2" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="208" y="-128" type="instance" />
        </instance>
        <instance x="3040" y="672" name="muxin3" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="208" y="-128" type="instance" />
        </instance>
        <branch name="Nin(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="352" y="576" type="branch" />
            <wire x2="352" y1="576" y2="576" x1="272" />
            <wire x2="432" y1="576" y2="576" x1="352" />
            <wire x2="432" y1="240" y2="576" x1="432" />
            <wire x2="512" y1="240" y2="240" x1="432" />
            <wire x2="1248" y1="240" y2="240" x1="512" />
            <wire x2="1248" y1="240" y2="528" x1="1248" />
            <wire x2="1456" y1="528" y2="528" x1="1248" />
            <wire x2="1760" y1="240" y2="240" x1="1248" />
            <wire x2="1760" y1="240" y2="512" x1="1760" />
            <wire x2="2160" y1="512" y2="512" x1="1760" />
            <wire x2="2448" y1="240" y2="240" x1="1760" />
            <wire x2="2448" y1="240" y2="512" x1="2448" />
            <wire x2="3040" y1="512" y2="512" x1="2448" />
            <wire x2="512" y1="240" y2="528" x1="512" />
            <wire x2="672" y1="528" y2="528" x1="512" />
        </branch>
        <branch name="XLXN_96(31:0)">
            <wire x2="2544" y1="448" y2="448" x1="2480" />
            <wire x2="2544" y1="448" y2="736" x1="2544" />
            <wire x2="2544" y1="736" y2="736" x1="2480" />
            <wire x2="2480" y1="736" y2="1040" x1="2480" />
            <wire x2="2528" y1="1040" y2="1040" x1="2480" />
            <wire x2="2528" y1="1040" y2="1264" x1="2528" />
            <wire x2="2608" y1="1264" y2="1264" x1="2528" />
        </branch>
        <branch name="XLXN_97(31:0)">
            <wire x2="3424" y1="736" y2="736" x1="3296" />
            <wire x2="3296" y1="736" y2="1264" x1="3296" />
            <wire x2="3440" y1="1264" y2="1264" x1="3296" />
            <wire x2="3424" y1="448" y2="448" x1="3360" />
            <wire x2="3424" y1="448" y2="736" x1="3424" />
        </branch>
        <branch name="Tin(31:0)">
            <wire x2="416" y1="128" y2="128" x1="256" />
            <wire x2="416" y1="128" y2="464" x1="416" />
            <wire x2="672" y1="464" y2="464" x1="416" />
            <wire x2="1136" y1="128" y2="128" x1="416" />
            <wire x2="1136" y1="128" y2="464" x1="1136" />
            <wire x2="1456" y1="464" y2="464" x1="1136" />
            <wire x2="1840" y1="128" y2="128" x1="1136" />
            <wire x2="1840" y1="128" y2="448" x1="1840" />
            <wire x2="2160" y1="448" y2="448" x1="1840" />
            <wire x2="2624" y1="128" y2="128" x1="1840" />
            <wire x2="2624" y1="128" y2="448" x1="2624" />
            <wire x2="3040" y1="448" y2="448" x1="2624" />
        </branch>
        <instance x="1456" y="688" name="muxin1" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="192" y="-128" type="instance" />
        </instance>
        <instance x="992" y="1312" name="XLXI_1" orien="R0">
        </instance>
        <instance x="1904" y="1280" name="XLXI_2" orien="R0">
        </instance>
        <instance x="3440" y="1296" name="XLXI_4" orien="R0">
        </instance>
        <branch name="XLXN_103(3:0)">
            <wire x2="992" y1="2112" y2="2112" x1="928" />
            <wire x2="992" y1="2112" y2="2176" x1="992" />
            <wire x2="3248" y1="2176" y2="2176" x1="992" />
            <wire x2="3248" y1="1200" y2="2176" x1="3248" />
            <wire x2="3440" y1="1200" y2="1200" x1="3248" />
        </branch>
        <instance x="2608" y="1296" name="XLXI_3" orien="R0">
        </instance>
        <branch name="clr">
            <wire x2="304" y1="1536" y2="1536" x1="192" />
            <wire x2="304" y1="1536" y2="1600" x1="304" />
            <wire x2="1424" y1="1600" y2="1600" x1="304" />
            <wire x2="1424" y1="1600" y2="1824" x1="1424" />
            <wire x2="2352" y1="1824" y2="1824" x1="1424" />
            <wire x2="3360" y1="1824" y2="1824" x1="2352" />
            <wire x2="304" y1="1344" y2="1536" x1="304" />
            <wire x2="992" y1="1344" y2="1344" x1="304" />
            <wire x2="1424" y1="1312" y2="1600" x1="1424" />
            <wire x2="1904" y1="1312" y2="1312" x1="1424" />
            <wire x2="2608" y1="1328" y2="1328" x1="2352" />
            <wire x2="2352" y1="1328" y2="1824" x1="2352" />
            <wire x2="3360" y1="1328" y2="1824" x1="3360" />
            <wire x2="3440" y1="1328" y2="1328" x1="3360" />
        </branch>
        <branch name="XLXN_106">
            <wire x2="976" y1="1840" y2="1840" x1="928" />
            <wire x2="976" y1="1840" y2="2560" x1="976" />
            <wire x2="3232" y1="2560" y2="2560" x1="976" />
            <wire x2="3232" y1="1136" y2="2560" x1="3232" />
            <wire x2="3440" y1="1136" y2="1136" x1="3232" />
        </branch>
        <iomarker fontsize="28" x="192" y="1536" name="clr" orien="R180" />
    </sheet>
</drawing>