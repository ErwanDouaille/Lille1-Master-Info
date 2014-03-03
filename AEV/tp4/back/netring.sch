<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="XLXN_1(31:0)" />
        <signal name="O1(31:0)" />
        <signal name="XLXN_3(31:0)" />
        <signal name="I1(31:0)" />
        <signal name="I2(31:0)" />
        <signal name="I3(31:0)" />
        <signal name="sel(1:0)" />
        <signal name="clr" />
        <signal name="clk" />
        <signal name="XLXN_10" />
        <port polarity="Output" name="O1(31:0)" />
        <port polarity="Input" name="I1(31:0)" />
        <port polarity="Input" name="I2(31:0)" />
        <port polarity="Input" name="I3(31:0)" />
        <port polarity="Input" name="sel(1:0)" />
        <port polarity="Input" name="clr" />
        <port polarity="Input" name="clk" />
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
        <block symbolname="reg32" name="shift_register">
            <blockpin signalname="XLXN_1(31:0)" name="D(31:0)" />
            <blockpin signalname="clr" name="CLR" />
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_10" name="CE" />
            <blockpin signalname="O1(31:0)" name="Q(31:0)" />
        </block>
        <block symbolname="Mux4" name="muxin">
            <blockpin signalname="O1(31:0)" name="X0(31:0)" />
            <blockpin signalname="I1(31:0)" name="X1(31:0)" />
            <blockpin signalname="I2(31:0)" name="X2(31:0)" />
            <blockpin signalname="I3(31:0)" name="X3(31:0)" />
            <blockpin signalname="sel(1:0)" name="sel(1:0)" />
            <blockpin signalname="XLXN_1(31:0)" name="Y(31:0)" />
        </block>
        <block symbolname="vcc" name="XLXI_3">
            <blockpin signalname="XLXN_10" name="P" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <instance x="1072" y="688" name="shift_register" orien="R0">
        </instance>
        <instance x="480" y="752" name="muxin" orien="R0">
        </instance>
        <branch name="XLXN_1(31:0)">
            <wire x2="1072" y1="464" y2="464" x1="864" />
        </branch>
        <branch name="O1(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1648" y="464" type="branch" />
            <wire x2="1568" y1="288" y2="288" x1="352" />
            <wire x2="1568" y1="288" y2="464" x1="1568" />
            <wire x2="1648" y1="464" y2="464" x1="1568" />
            <wire x2="1712" y1="464" y2="464" x1="1648" />
            <wire x2="352" y1="288" y2="464" x1="352" />
            <wire x2="480" y1="464" y2="464" x1="352" />
            <wire x2="1568" y1="464" y2="464" x1="1456" />
        </branch>
        <branch name="I1(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="384" y="528" type="branch" />
            <wire x2="384" y1="528" y2="528" x1="352" />
            <wire x2="480" y1="528" y2="528" x1="384" />
        </branch>
        <branch name="I2(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="400" y="592" type="branch" />
            <wire x2="400" y1="592" y2="592" x1="352" />
            <wire x2="480" y1="592" y2="592" x1="400" />
        </branch>
        <branch name="I3(31:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="384" y="656" type="branch" />
            <wire x2="384" y1="656" y2="656" x1="352" />
            <wire x2="480" y1="656" y2="656" x1="384" />
        </branch>
        <branch name="sel(1:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="384" y="720" type="branch" />
            <wire x2="384" y1="720" y2="720" x1="352" />
            <wire x2="480" y1="720" y2="720" x1="384" />
        </branch>
        <branch name="clr">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="400" y="896" type="branch" />
            <wire x2="400" y1="896" y2="896" x1="288" />
            <wire x2="912" y1="896" y2="896" x1="400" />
            <wire x2="912" y1="528" y2="896" x1="912" />
            <wire x2="1072" y1="528" y2="528" x1="912" />
        </branch>
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="400" y="992" type="branch" />
            <wire x2="400" y1="992" y2="992" x1="288" />
            <wire x2="928" y1="992" y2="992" x1="400" />
            <wire x2="928" y1="592" y2="992" x1="928" />
            <wire x2="1072" y1="592" y2="592" x1="928" />
        </branch>
        <branch name="XLXN_10">
            <wire x2="1072" y1="656" y2="688" x1="1072" />
        </branch>
        <instance x="1136" y="688" name="XLXI_3" orien="R180" />
        <iomarker fontsize="28" x="352" y="528" name="I1(31:0)" orien="R180" />
        <iomarker fontsize="28" x="352" y="592" name="I2(31:0)" orien="R180" />
        <iomarker fontsize="28" x="352" y="656" name="I3(31:0)" orien="R180" />
        <iomarker fontsize="28" x="1712" y="464" name="O1(31:0)" orien="R0" />
        <iomarker fontsize="28" x="288" y="896" name="clr" orien="R180" />
        <iomarker fontsize="28" x="288" y="992" name="clk" orien="R180" />
        <iomarker fontsize="28" x="352" y="720" name="sel(1:0)" orien="R180" />
    </sheet>
</drawing>