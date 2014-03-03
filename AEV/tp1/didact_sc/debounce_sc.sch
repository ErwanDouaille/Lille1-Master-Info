<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="sig_in" />
        <signal name="Q0" />
        <signal name="Q1" />
        <signal name="Q2" />
        <signal name="XLXN_6" />
        <signal name="clkin" />
        <signal name="sig_out" />
        <port polarity="Input" name="sig_in" />
        <port polarity="Input" name="clkin" />
        <port polarity="Output" name="sig_out" />
        <blockdef name="fd">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <rect width="256" x="64" y="-320" height="256" />
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
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
        <block symbolname="fd" name="Q0_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="sig_in" name="D" />
            <blockpin signalname="Q0" name="Q" />
        </block>
        <block symbolname="fd" name="Q1_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="Q0" name="D" />
            <blockpin signalname="Q1" name="Q" />
        </block>
        <block symbolname="fd" name="Q2_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="Q1" name="D" />
            <blockpin signalname="Q2" name="Q" />
        </block>
        <block symbolname="inv" name="XLXI_5">
            <blockpin signalname="Q2" name="I" />
            <blockpin signalname="XLXN_6" name="O" />
        </block>
        <block symbolname="and3" name="XLXI_6">
            <blockpin signalname="XLXN_6" name="I0" />
            <blockpin signalname="Q1" name="I1" />
            <blockpin signalname="Q0" name="I2" />
            <blockpin signalname="sig_out" name="O" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <branch name="sig_in">
            <wire x2="1376" y1="1008" y2="1008" x1="1040" />
        </branch>
        <instance x="1376" y="1264" name="Q0_inst" orien="R0" />
        <branch name="Q0">
            <attrtext style="alignment:SOFT-TCENTER" attrname="Name" x="1776" y="1008" type="branch" />
            <wire x2="1776" y1="1008" y2="1008" x1="1760" />
            <wire x2="1792" y1="1008" y2="1008" x1="1776" />
            <wire x2="2800" y1="720" y2="720" x1="1776" />
            <wire x2="2800" y1="720" y2="880" x1="2800" />
            <wire x2="2880" y1="880" y2="880" x1="2800" />
            <wire x2="1776" y1="720" y2="1008" x1="1776" />
        </branch>
        <instance x="1792" y="1264" name="Q1_inst" orien="R0" />
        <branch name="Q1">
            <attrtext style="alignment:SOFT-TCENTER" attrname="Name" x="2192" y="1008" type="branch" />
            <wire x2="2192" y1="1008" y2="1008" x1="2176" />
            <wire x2="2208" y1="1008" y2="1008" x1="2192" />
            <wire x2="2192" y1="784" y2="1008" x1="2192" />
            <wire x2="2720" y1="784" y2="784" x1="2192" />
            <wire x2="2720" y1="784" y2="944" x1="2720" />
            <wire x2="2880" y1="944" y2="944" x1="2720" />
        </branch>
        <instance x="2208" y="1264" name="Q2_inst" orien="R0" />
        <branch name="Q2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2608" y="1008" type="branch" />
            <wire x2="2608" y1="1008" y2="1008" x1="2592" />
            <wire x2="2624" y1="1008" y2="1008" x1="2608" />
        </branch>
        <instance x="2624" y="1040" name="XLXI_5" orien="R0" />
        <branch name="XLXN_6">
            <wire x2="2880" y1="1008" y2="1008" x1="2848" />
        </branch>
        <instance x="2880" y="1072" name="XLXI_6" orien="R0" />
        <branch name="clkin">
            <wire x2="1296" y1="1136" y2="1136" x1="1024" />
            <wire x2="1376" y1="1136" y2="1136" x1="1296" />
            <wire x2="1296" y1="1136" y2="1328" x1="1296" />
            <wire x2="1792" y1="1328" y2="1328" x1="1296" />
            <wire x2="1776" y1="1248" y2="1248" x1="1744" />
            <wire x2="1776" y1="1248" y2="1280" x1="1776" />
            <wire x2="1792" y1="1280" y2="1280" x1="1776" />
            <wire x2="1792" y1="1280" y2="1328" x1="1792" />
            <wire x2="1792" y1="1136" y2="1136" x1="1776" />
            <wire x2="1776" y1="1136" y2="1248" x1="1776" />
            <wire x2="1792" y1="1264" y2="1280" x1="1792" />
            <wire x2="2208" y1="1264" y2="1264" x1="1792" />
            <wire x2="2208" y1="1136" y2="1136" x1="2192" />
            <wire x2="2192" y1="1136" y2="1248" x1="2192" />
            <wire x2="2208" y1="1248" y2="1248" x1="2192" />
            <wire x2="2208" y1="1248" y2="1264" x1="2208" />
        </branch>
        <branch name="sig_out">
            <wire x2="3152" y1="944" y2="944" x1="3136" />
            <wire x2="3200" y1="944" y2="944" x1="3152" />
        </branch>
        <iomarker fontsize="28" x="1040" y="1008" name="sig_in" orien="R180" />
        <iomarker fontsize="28" x="1024" y="1136" name="clkin" orien="R180" />
        <iomarker fontsize="28" x="3200" y="944" name="sig_out" orien="R0" />
    </sheet>
</drawing>