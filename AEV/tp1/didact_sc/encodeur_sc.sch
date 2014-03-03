<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="O0" />
        <signal name="O1" />
        <signal name="GS" />
        <signal name="E3" />
        <signal name="E2" />
        <signal name="E1" />
        <port polarity="Output" name="O0" />
        <port polarity="Output" name="O1" />
        <port polarity="Output" name="GS" />
        <port polarity="Input" name="E3" />
        <port polarity="Input" name="E2" />
        <port polarity="Input" name="E1" />
        <blockdef name="lut3">
            <timestamp>2007-4-25T21:45:29</timestamp>
            <line x2="320" y1="-192" y2="-192" x1="384" />
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-192" y2="-192" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <rect width="256" x="64" y="-384" height="320" />
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
        <block symbolname="lut3" name="LUT1_inst">
            <attr value="44" name="INIT">
                <trait editname="all:1 sch:0" />
                <trait edittrait="all:1 sch:0" />
                <trait verilog="all:0 dp:1nosynth wsynop:1 wsynth:1" />
                <trait vhdl="all:0 gm:1nosynth wa:1 wd:1" />
                <trait valuetype="BitVector 8 h" />
            </attr>
            <blockpin signalname="E1" name="I0" />
            <blockpin signalname="E2" name="I1" />
            <blockpin signalname="E3" name="I2" />
            <blockpin signalname="O0" name="O" />
        </block>
        <block symbolname="lut3" name="LUT2_inst">
            <attr value="10" name="INIT">
                <trait editname="all:1 sch:0" />
                <trait edittrait="all:1 sch:0" />
                <trait verilog="all:0 dp:1nosynth wsynop:1 wsynth:1" />
                <trait vhdl="all:0 gm:1nosynth wa:1 wd:1" />
                <trait valuetype="BitVector 8 h" />
            </attr>
            <blockpin signalname="E1" name="I0" />
            <blockpin signalname="E2" name="I1" />
            <blockpin signalname="E3" name="I2" />
            <blockpin signalname="O1" name="O" />
        </block>
        <block symbolname="or3" name="XLXI_3">
            <blockpin signalname="E1" name="I0" />
            <blockpin signalname="E2" name="I1" />
            <blockpin signalname="E3" name="I2" />
            <blockpin signalname="GS" name="O" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <branch name="GS">
            <wire x2="2032" y1="1584" y2="1584" x1="1600" />
        </branch>
        <branch name="O0">
            <wire x2="1616" y1="656" y2="656" x1="1600" />
            <wire x2="1968" y1="656" y2="656" x1="1616" />
        </branch>
        <instance x="1232" y="848" name="LUT1_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial;displayformat:NAMEEQUALSVALUE" attrname="INIT" x="0" y="-472" type="instance" />
        </instance>
        <iomarker fontsize="28" x="1968" y="656" name="O0" orien="R0" />
        <branch name="O1">
            <wire x2="1632" y1="1200" y2="1200" x1="1616" />
            <wire x2="1792" y1="1200" y2="1200" x1="1632" />
        </branch>
        <instance x="1344" y="1712" name="XLXI_3" orien="R0" />
        <instance x="1248" y="1392" name="LUT2_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial;displayformat:NAMEEQUALSVALUE" attrname="INIT" x="0" y="-472" type="instance" />
        </instance>
        <iomarker fontsize="28" x="640" y="656" name="E2" orien="R180" />
        <iomarker fontsize="28" x="1792" y="1200" name="O1" orien="R0" />
        <iomarker fontsize="28" x="2032" y="1584" name="GS" orien="R0" />
        <iomarker fontsize="28" x="640" y="720" name="E1" orien="R180" />
        <iomarker fontsize="28" x="640" y="592" name="E3" orien="R180" />
        <branch name="E3">
            <wire x2="1072" y1="592" y2="592" x1="640" />
            <wire x2="1232" y1="592" y2="592" x1="1072" />
            <wire x2="1072" y1="592" y2="1136" x1="1072" />
            <wire x2="1072" y1="1136" y2="1520" x1="1072" />
            <wire x2="1344" y1="1520" y2="1520" x1="1072" />
            <wire x2="1248" y1="1136" y2="1136" x1="1072" />
        </branch>
        <branch name="E2">
            <wire x2="880" y1="656" y2="656" x1="640" />
            <wire x2="1232" y1="656" y2="656" x1="880" />
            <wire x2="880" y1="656" y2="1200" x1="880" />
            <wire x2="912" y1="1200" y2="1200" x1="880" />
            <wire x2="1248" y1="1200" y2="1200" x1="912" />
            <wire x2="912" y1="1200" y2="1584" x1="912" />
            <wire x2="1344" y1="1584" y2="1584" x1="912" />
        </branch>
        <branch name="E1">
            <wire x2="704" y1="720" y2="720" x1="640" />
            <wire x2="704" y1="720" y2="752" x1="704" />
            <wire x2="752" y1="752" y2="752" x1="704" />
            <wire x2="752" y1="752" y2="1264" x1="752" />
            <wire x2="752" y1="1264" y2="1648" x1="752" />
            <wire x2="1344" y1="1648" y2="1648" x1="752" />
            <wire x2="1248" y1="1264" y2="1264" x1="752" />
            <wire x2="1232" y1="720" y2="720" x1="752" />
            <wire x2="752" y1="720" y2="752" x1="752" />
        </branch>
    </sheet>
</drawing>