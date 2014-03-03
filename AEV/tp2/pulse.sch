<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="XLXN_1" />
        <signal name="XLXN_2" />
        <signal name="clk" />
        <signal name="XLXN_4" />
        <signal name="XLXN_5" />
        <signal name="XLXN_6" />
        <signal name="XLXN_7" />
        <signal name="inp" />
        <signal name="outp" />
        <port polarity="Input" name="clk" />
        <port polarity="Input" name="inp" />
        <port polarity="Output" name="outp" />
        <blockdef name="fd">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <rect width="256" x="64" y="-320" height="256" />
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
        </blockdef>
        <blockdef name="and3">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-64" y2="-64" x1="0" />
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-192" y2="-192" x1="0" />
            <line x2="192" y1="-128" y2="-128" x1="256" />
            <line x2="144" y1="-176" y2="-176" x1="64" />
            <line x2="64" y1="-80" y2="-80" x1="144" />
            <arc ex="144" ey="-176" sx="144" sy="-80" r="48" cx="144" cy="-128" />
            <line x2="64" y1="-64" y2="-192" x1="64" />
        </blockdef>
        <blockdef name="inv">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="160" y1="-32" y2="-32" x1="224" />
            <line x2="128" y1="-64" y2="-32" x1="64" />
            <line x2="64" y1="-32" y2="0" x1="128" />
            <line x2="64" y1="0" y2="-64" x1="64" />
            <circle r="16" cx="144" cy="-32" />
        </blockdef>
        <block symbolname="fd" name="XLXI_1">
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="inp" name="D" />
            <blockpin signalname="XLXN_4" name="Q" />
        </block>
        <block symbolname="fd" name="XLXI_2">
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_4" name="D" />
            <blockpin signalname="XLXN_5" name="Q" />
        </block>
        <block symbolname="fd" name="XLXI_3">
            <blockpin signalname="clk" name="C" />
            <blockpin signalname="XLXN_5" name="D" />
            <blockpin signalname="XLXN_6" name="Q" />
        </block>
        <block symbolname="and3" name="XLXI_4">
            <blockpin signalname="XLXN_7" name="I0" />
            <blockpin signalname="XLXN_5" name="I1" />
            <blockpin signalname="XLXN_4" name="I2" />
            <blockpin signalname="outp" name="O" />
        </block>
        <block symbolname="inv" name="XLXI_5">
            <blockpin signalname="XLXN_6" name="I" />
            <blockpin signalname="XLXN_7" name="O" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <instance x="576" y="944" name="XLXI_1" orien="R0" />
        <instance x="1184" y="928" name="XLXI_2" orien="R0" />
        <instance x="1824" y="912" name="XLXI_3" orien="R0" />
        <branch name="clk">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="496" y="1008" type="branch" />
            <wire x2="384" y1="1104" y2="1104" x1="288" />
            <wire x2="576" y1="816" y2="816" x1="384" />
            <wire x2="384" y1="816" y2="1008" x1="384" />
            <wire x2="384" y1="1008" y2="1104" x1="384" />
            <wire x2="496" y1="1008" y2="1008" x1="384" />
            <wire x2="1040" y1="1008" y2="1008" x1="496" />
            <wire x2="1632" y1="1008" y2="1008" x1="1040" />
            <wire x2="1040" y1="800" y2="1008" x1="1040" />
            <wire x2="1184" y1="800" y2="800" x1="1040" />
            <wire x2="1632" y1="784" y2="1008" x1="1632" />
            <wire x2="1824" y1="784" y2="784" x1="1632" />
        </branch>
        <instance x="2528" y="496" name="XLXI_4" orien="R0" />
        <branch name="XLXN_4">
            <wire x2="1040" y1="688" y2="688" x1="960" />
            <wire x2="1072" y1="688" y2="688" x1="1040" />
            <wire x2="2528" y1="304" y2="304" x1="1040" />
            <wire x2="1040" y1="304" y2="688" x1="1040" />
            <wire x2="1072" y1="672" y2="688" x1="1072" />
            <wire x2="1184" y1="672" y2="672" x1="1072" />
        </branch>
        <branch name="XLXN_5">
            <wire x2="1632" y1="672" y2="672" x1="1568" />
            <wire x2="1696" y1="672" y2="672" x1="1632" />
            <wire x2="2528" y1="368" y2="368" x1="1632" />
            <wire x2="1632" y1="368" y2="672" x1="1632" />
            <wire x2="1696" y1="656" y2="672" x1="1696" />
            <wire x2="1824" y1="656" y2="656" x1="1696" />
        </branch>
        <instance x="2304" y="688" name="XLXI_5" orien="R0" />
        <branch name="XLXN_6">
            <wire x2="2304" y1="656" y2="656" x1="2208" />
        </branch>
        <branch name="XLXN_7">
            <wire x2="2528" y1="432" y2="496" x1="2528" />
            <wire x2="2592" y1="496" y2="496" x1="2528" />
            <wire x2="2592" y1="496" y2="656" x1="2592" />
            <wire x2="2592" y1="656" y2="656" x1="2528" />
        </branch>
        <branch name="inp">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="448" y="688" type="branch" />
            <wire x2="448" y1="688" y2="688" x1="368" />
            <wire x2="576" y1="688" y2="688" x1="448" />
        </branch>
        <branch name="outp">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2848" y="368" type="branch" />
            <wire x2="2848" y1="368" y2="368" x1="2784" />
            <wire x2="2960" y1="368" y2="368" x1="2848" />
        </branch>
        <iomarker fontsize="28" x="288" y="1104" name="clk" orien="R180" />
        <iomarker fontsize="28" x="368" y="688" name="inp" orien="R180" />
        <iomarker fontsize="28" x="2960" y="368" name="outp" orien="R0" />
    </sheet>
</drawing>