<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="D(31:0)" />
        <signal name="Q(31:0)" />
        <signal name="CLR" />
        <signal name="C" />
        <signal name="CE" />
        <signal name="D(31:16)" />
        <signal name="D(15:0)" />
        <signal name="Q(15:0)" />
        <signal name="Q(31:16)" />
        <port polarity="Input" name="D(31:0)" />
        <port polarity="Output" name="Q(31:0)" />
        <port polarity="Input" name="CLR" />
        <port polarity="Input" name="C" />
        <port polarity="Input" name="CE" />
        <blockdef name="fd16ce">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-192" y2="-192" x1="0" />
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
            <rect width="64" x="320" y="-268" height="24" />
            <rect width="64" x="0" y="-268" height="24" />
            <line x2="64" y1="-32" y2="-32" x1="192" />
            <line x2="192" y1="-64" y2="-32" x1="192" />
            <rect width="256" x="64" y="-320" height="256" />
        </blockdef>
        <block symbolname="fd16ce" name="XLXI_1">
            <blockpin signalname="C" name="C" />
            <blockpin signalname="CE" name="CE" />
            <blockpin signalname="CLR" name="CLR" />
            <blockpin signalname="D(31:16)" name="D(15:0)" />
            <blockpin signalname="Q(31:16)" name="Q(15:0)" />
        </block>
        <block symbolname="fd16ce" name="XLXI_2">
            <blockpin signalname="C" name="C" />
            <blockpin signalname="CE" name="CE" />
            <blockpin signalname="CLR" name="CLR" />
            <blockpin signalname="D(15:0)" name="D(15:0)" />
            <blockpin signalname="Q(15:0)" name="Q(15:0)" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <branch name="D(31:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="336" y="512" type="branch" />
            <wire x2="336" y1="288" y2="512" x1="336" />
            <wire x2="336" y1="512" y2="592" x1="336" />
            <wire x2="336" y1="592" y2="1040" x1="336" />
            <wire x2="336" y1="1040" y2="1360" x1="336" />
        </branch>
        <branch name="Q(31:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="1520" y="432" type="branch" />
            <wire x2="1520" y1="288" y2="432" x1="1520" />
            <wire x2="1520" y1="432" y2="592" x1="1520" />
            <wire x2="1520" y1="592" y2="1024" x1="1520" />
            <wire x2="1520" y1="1024" y2="1040" x1="1520" />
            <wire x2="1520" y1="1040" y2="1360" x1="1520" />
        </branch>
        <branch name="CLR">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="496" y="512" type="branch" />
            <wire x2="496" y1="288" y2="512" x1="496" />
            <wire x2="496" y1="512" y2="816" x1="496" />
            <wire x2="496" y1="816" y2="1264" x1="496" />
            <wire x2="496" y1="1264" y2="1360" x1="496" />
            <wire x2="912" y1="1264" y2="1264" x1="496" />
            <wire x2="912" y1="816" y2="816" x1="496" />
        </branch>
        <branch name="C">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="624" y="432" type="branch" />
            <wire x2="624" y1="288" y2="432" x1="624" />
            <wire x2="624" y1="432" y2="720" x1="624" />
            <wire x2="624" y1="720" y2="1168" x1="624" />
            <wire x2="624" y1="1168" y2="1360" x1="624" />
            <wire x2="912" y1="1168" y2="1168" x1="624" />
            <wire x2="912" y1="720" y2="720" x1="624" />
        </branch>
        <branch name="CE">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="720" y="464" type="branch" />
            <wire x2="720" y1="288" y2="464" x1="720" />
            <wire x2="720" y1="464" y2="656" x1="720" />
            <wire x2="912" y1="656" y2="656" x1="720" />
            <wire x2="720" y1="656" y2="1104" x1="720" />
            <wire x2="720" y1="1104" y2="1360" x1="720" />
            <wire x2="912" y1="1104" y2="1104" x1="720" />
        </branch>
        <instance x="912" y="848" name="XLXI_1" orien="R0" />
        <instance x="912" y="1296" name="XLXI_2" orien="R0" />
        <bustap x2="432" y1="592" y2="592" x1="336" />
        <branch name="D(31:16)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="672" y="592" type="branch" />
            <wire x2="672" y1="592" y2="592" x1="432" />
            <wire x2="912" y1="592" y2="592" x1="672" />
        </branch>
        <bustap x2="432" y1="1040" y2="1040" x1="336" />
        <branch name="D(15:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="672" y="1040" type="branch" />
            <wire x2="672" y1="1040" y2="1040" x1="432" />
            <wire x2="912" y1="1040" y2="1040" x1="672" />
        </branch>
        <bustap x2="1424" y1="1040" y2="1040" x1="1520" />
        <branch name="Q(15:0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1360" y="1040" type="branch" />
            <wire x2="1360" y1="1040" y2="1040" x1="1296" />
            <wire x2="1424" y1="1040" y2="1040" x1="1360" />
        </branch>
        <bustap x2="1424" y1="592" y2="592" x1="1520" />
        <branch name="Q(31:16)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1360" y="592" type="branch" />
            <wire x2="1360" y1="592" y2="592" x1="1296" />
            <wire x2="1424" y1="592" y2="592" x1="1360" />
        </branch>
        <iomarker fontsize="28" x="336" y="288" name="D(31:0)" orien="R270" />
        <iomarker fontsize="28" x="496" y="288" name="CLR" orien="R270" />
        <iomarker fontsize="28" x="624" y="288" name="C" orien="R270" />
        <iomarker fontsize="28" x="720" y="288" name="CE" orien="R270" />
        <iomarker fontsize="28" x="1520" y="1360" name="Q(31:0)" orien="R90" />
    </sheet>
</drawing>