<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="b0" />
        <signal name="b1" />
        <signal name="romin(5:0)" />
        <signal name="romout(3)" />
        <signal name="romout(2)" />
        <signal name="romout(1)" />
        <signal name="romin(3)" />
        <signal name="romin(4)" />
        <signal name="romin(5)" />
        <signal name="etatpres(2:0)" />
        <signal name="etatpres(1)" />
        <signal name="etatpres(0)" />
        <signal name="etatpres(2)" />
        <signal name="romout(3:0)" />
        <signal name="romout(0)" />
        <signal name="etatsuiv(2:0)" />
        <signal name="etatsuiv(2)" />
        <signal name="etatsuiv(1)" />
        <signal name="etatsuiv(0)" />
        <signal name="enable_del" />
        <signal name="gs" />
        <signal name="romin(2)" />
        <signal name="romin(1)" />
        <signal name="romin(0)" />
        <signal name="clkin" />
        <signal name="rst" />
        <port polarity="Input" name="b0" />
        <port polarity="Input" name="b1" />
        <port polarity="Output" name="etatpres(2:0)" />
        <port polarity="Output" name="etatsuiv(2:0)" />
        <port polarity="Output" name="enable_del" />
        <port polarity="Input" name="gs" />
        <port polarity="Input" name="clkin" />
        <port polarity="Input" name="rst" />
        <blockdef name="rom_msa">
            <timestamp>2013-9-27T10:28:28</timestamp>
            <rect width="224" x="32" y="32" height="512" />
            <line x2="32" y1="80" y2="80" style="linewidth:W" x1="0" />
            <line x2="256" y1="80" y2="80" style="linewidth:W" x1="288" />
        </blockdef>
        <blockdef name="fdc">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="64" y1="-256" y2="-256" x1="0" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <rect width="256" x="64" y="-320" height="256" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="192" y1="-64" y2="-32" x1="192" />
            <line x2="64" y1="-32" y2="-32" x1="192" />
        </blockdef>
        <blockdef name="buf">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="128" y1="-32" y2="-32" x1="224" />
            <line x2="128" y1="0" y2="-32" x1="64" />
            <line x2="64" y1="-32" y2="-64" x1="128" />
            <line x2="64" y1="-64" y2="0" x1="64" />
        </blockdef>
        <block symbolname="rom_msa" name="rom_inst">
            <blockpin signalname="romin(5:0)" name="a(5:0)" />
            <blockpin signalname="romout(3:0)" name="spo(3:0)" />
        </block>
        <block symbolname="fdc" name="Q1_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="rst" name="CLR" />
            <blockpin signalname="romout(2)" name="D" />
            <blockpin signalname="romin(4)" name="Q" />
        </block>
        <block symbolname="fdc" name="Q2_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="rst" name="CLR" />
            <blockpin signalname="romout(3)" name="D" />
            <blockpin signalname="romin(5)" name="Q" />
        </block>
        <block symbolname="fdc" name="Q0_inst">
            <blockpin signalname="clkin" name="C" />
            <blockpin signalname="rst" name="CLR" />
            <blockpin signalname="romout(1)" name="D" />
            <blockpin signalname="romin(3)" name="Q" />
        </block>
        <block symbolname="buf" name="XLXI_10">
            <blockpin signalname="romin(5)" name="I" />
            <blockpin signalname="etatpres(2)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_11">
            <blockpin signalname="romin(4)" name="I" />
            <blockpin signalname="etatpres(1)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_12">
            <blockpin signalname="romin(3)" name="I" />
            <blockpin signalname="etatpres(0)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_13">
            <blockpin signalname="romout(3)" name="I" />
            <blockpin signalname="etatsuiv(2)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_14">
            <blockpin signalname="romout(2)" name="I" />
            <blockpin signalname="etatsuiv(1)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_15">
            <blockpin signalname="romout(1)" name="I" />
            <blockpin signalname="etatsuiv(0)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_16">
            <blockpin signalname="romout(0)" name="I" />
            <blockpin signalname="enable_del" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_17">
            <blockpin signalname="b0" name="I" />
            <blockpin signalname="romin(1)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_18">
            <blockpin signalname="b1" name="I" />
            <blockpin signalname="romin(2)" name="O" />
        </block>
        <block symbolname="buf" name="XLXI_19">
            <blockpin signalname="gs" name="I" />
            <blockpin signalname="romin(0)" name="O" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="5440" height="3520">
        <branch name="b0">
            <wire x2="1104" y1="1104" y2="1104" x1="640" />
            <wire x2="1120" y1="1104" y2="1104" x1="1104" />
        </branch>
        <branch name="b1">
            <wire x2="1104" y1="1008" y2="1008" x1="640" />
            <wire x2="1120" y1="1008" y2="1008" x1="1104" />
        </branch>
        <instance x="2192" y="560" name="rom_inst" orien="R0">
        </instance>
        <instance x="3008" y="1344" name="Q1_inst" orien="R0" />
        <instance x="3008" y="1696" name="Q0_inst" orien="R0" />
        <branch name="romin(5:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="1968" y="784" type="branch" />
            <wire x2="2192" y1="640" y2="640" x1="1968" />
            <wire x2="1968" y1="640" y2="704" x1="1968" />
            <wire x2="1968" y1="704" y2="752" x1="1968" />
            <wire x2="1968" y1="752" y2="784" x1="1968" />
            <wire x2="1968" y1="784" y2="800" x1="1968" />
            <wire x2="1968" y1="800" y2="1008" x1="1968" />
            <wire x2="1968" y1="1008" y2="1104" x1="1968" />
            <wire x2="1968" y1="1104" y2="1200" x1="1968" />
            <wire x2="1968" y1="1200" y2="1296" x1="1968" />
        </branch>
        <instance x="3008" y="976" name="Q2_inst" orien="R0" />
        <bustap x2="2768" y1="672" y2="672" x1="2672" />
        <branch name="romout(3)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2772" y="672" type="branch" />
            <wire x2="2772" y1="672" y2="672" x1="2768" />
            <wire x2="2880" y1="672" y2="672" x1="2772" />
            <wire x2="2880" y1="672" y2="720" x1="2880" />
            <wire x2="3008" y1="720" y2="720" x1="2880" />
        </branch>
        <bustap x2="2768" y1="1072" y2="1072" x1="2672" />
        <branch name="romout(2)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2784" y="1072" type="branch" />
            <wire x2="2784" y1="1072" y2="1072" x1="2768" />
            <wire x2="2800" y1="1072" y2="1072" x1="2784" />
            <wire x2="2800" y1="1072" y2="1088" x1="2800" />
            <wire x2="3008" y1="1088" y2="1088" x1="2800" />
        </branch>
        <bustap x2="2768" y1="1440" y2="1440" x1="2672" />
        <branch name="romout(1)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2784" y="1440" type="branch" />
            <wire x2="2784" y1="1440" y2="1440" x1="2768" />
            <wire x2="3008" y1="1440" y2="1440" x1="2784" />
        </branch>
        <branch name="romin(4)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1736" y="752" type="branch" />
            <wire x2="3440" y1="416" y2="416" x1="1600" />
            <wire x2="3440" y1="416" y2="1088" x1="3440" />
            <wire x2="3584" y1="1088" y2="1088" x1="3440" />
            <wire x2="1600" y1="416" y2="432" x1="1600" />
            <wire x2="1600" y1="432" y2="752" x1="1600" />
            <wire x2="1736" y1="752" y2="752" x1="1600" />
            <wire x2="1872" y1="752" y2="752" x1="1736" />
            <wire x2="3440" y1="1088" y2="1088" x1="3392" />
        </branch>
        <branch name="romin(5)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1760" y="704" type="branch" />
            <wire x2="3408" y1="480" y2="480" x1="1648" />
            <wire x2="3408" y1="480" y2="720" x1="3408" />
            <wire x2="3600" y1="720" y2="720" x1="3408" />
            <wire x2="1648" y1="480" y2="704" x1="1648" />
            <wire x2="1760" y1="704" y2="704" x1="1648" />
            <wire x2="1872" y1="704" y2="704" x1="1760" />
            <wire x2="3408" y1="720" y2="720" x1="3392" />
        </branch>
        <branch name="etatpres(2:0)">
            <wire x2="4144" y1="720" y2="1088" x1="4144" />
            <wire x2="4144" y1="1088" y2="1424" x1="4144" />
            <wire x2="4144" y1="1424" y2="1520" x1="4144" />
            <wire x2="4352" y1="1520" y2="1520" x1="4144" />
        </branch>
        <iomarker fontsize="28" x="4352" y="1520" name="etatpres(2:0)" orien="R0" />
        <instance x="3600" y="752" name="XLXI_10" orien="R0" />
        <instance x="3584" y="1120" name="XLXI_11" orien="R0" />
        <instance x="3600" y="1472" name="XLXI_12" orien="R0" />
        <bustap x2="4048" y1="1088" y2="1088" x1="4144" />
        <branch name="etatpres(1)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="4004" y="1088" type="branch" />
            <wire x2="4004" y1="1088" y2="1088" x1="3808" />
            <wire x2="4048" y1="1088" y2="1088" x1="4004" />
        </branch>
        <bustap x2="4048" y1="1424" y2="1424" x1="4144" />
        <branch name="etatpres(0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3975" y="1440" type="branch" />
            <wire x2="3975" y1="1440" y2="1440" x1="3824" />
            <wire x2="4032" y1="1440" y2="1440" x1="3975" />
            <wire x2="4048" y1="1424" y2="1424" x1="4032" />
            <wire x2="4032" y1="1424" y2="1440" x1="4032" />
        </branch>
        <bustap x2="4048" y1="720" y2="720" x1="4144" />
        <branch name="etatpres(2)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3999" y="720" type="branch" />
            <wire x2="3999" y1="720" y2="720" x1="3824" />
            <wire x2="4048" y1="720" y2="720" x1="3999" />
        </branch>
        <instance x="3056" y="2048" name="XLXI_14" orien="R0" />
        <instance x="3056" y="2240" name="XLXI_15" orien="R0" />
        <instance x="3056" y="2416" name="XLXI_16" orien="R0" />
        <instance x="3056" y="1888" name="XLXI_13" orien="R0" />
        <branch name="romout(3:0)">
            <attrtext style="alignment:SOFT-TVCENTER" attrname="Name" x="2672" y="880" type="branch" />
            <wire x2="2672" y1="640" y2="640" x1="2480" />
            <wire x2="2672" y1="640" y2="672" x1="2672" />
            <wire x2="2672" y1="672" y2="880" x1="2672" />
            <wire x2="2672" y1="880" y2="1072" x1="2672" />
            <wire x2="2672" y1="1072" y2="1440" x1="2672" />
            <wire x2="2672" y1="1440" y2="1856" x1="2672" />
            <wire x2="2672" y1="1856" y2="2016" x1="2672" />
            <wire x2="2672" y1="2016" y2="2208" x1="2672" />
            <wire x2="2672" y1="2208" y2="2384" x1="2672" />
        </branch>
        <bustap x2="2768" y1="1856" y2="1856" x1="2672" />
        <branch name="romout(3)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2912" y="1856" type="branch" />
            <wire x2="2912" y1="1856" y2="1856" x1="2768" />
            <wire x2="3056" y1="1856" y2="1856" x1="2912" />
        </branch>
        <bustap x2="2768" y1="2016" y2="2016" x1="2672" />
        <branch name="romout(2)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2912" y="2016" type="branch" />
            <wire x2="2912" y1="2016" y2="2016" x1="2768" />
            <wire x2="3056" y1="2016" y2="2016" x1="2912" />
        </branch>
        <bustap x2="2768" y1="2208" y2="2208" x1="2672" />
        <branch name="romout(1)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2912" y="2208" type="branch" />
            <wire x2="2912" y1="2208" y2="2208" x1="2768" />
            <wire x2="3056" y1="2208" y2="2208" x1="2912" />
        </branch>
        <bustap x2="2768" y1="2384" y2="2384" x1="2672" />
        <branch name="etatsuiv(2:0)">
            <wire x2="3632" y1="1856" y2="2016" x1="3632" />
            <wire x2="3632" y1="2016" y2="2208" x1="3632" />
            <wire x2="3984" y1="2208" y2="2208" x1="3632" />
            <wire x2="4032" y1="2208" y2="2208" x1="3984" />
        </branch>
        <iomarker fontsize="28" x="4032" y="2208" name="etatsuiv(2:0)" orien="R0" />
        <bustap x2="3536" y1="1856" y2="1856" x1="3632" />
        <branch name="etatsuiv(2)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3408" y="1856" type="branch" />
            <wire x2="3408" y1="1856" y2="1856" x1="3280" />
            <wire x2="3536" y1="1856" y2="1856" x1="3408" />
        </branch>
        <bustap x2="3536" y1="2016" y2="2016" x1="3632" />
        <branch name="etatsuiv(1)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3408" y="2016" type="branch" />
            <wire x2="3408" y1="2016" y2="2016" x1="3280" />
            <wire x2="3536" y1="2016" y2="2016" x1="3408" />
        </branch>
        <bustap x2="3536" y1="2208" y2="2208" x1="3632" />
        <branch name="etatsuiv(0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="3408" y="2208" type="branch" />
            <wire x2="3408" y1="2208" y2="2208" x1="3280" />
            <wire x2="3536" y1="2208" y2="2208" x1="3408" />
        </branch>
        <branch name="enable_del">
            <wire x2="3776" y1="2384" y2="2384" x1="3280" />
            <wire x2="3872" y1="2384" y2="2384" x1="3776" />
        </branch>
        <iomarker fontsize="28" x="3872" y="2384" name="enable_del" orien="R0" />
        <branch name="romout(0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2912" y="2384" type="branch" />
            <wire x2="2832" y1="2384" y2="2384" x1="2768" />
            <wire x2="2912" y1="2384" y2="2384" x1="2832" />
            <wire x2="3056" y1="2384" y2="2384" x1="2912" />
        </branch>
        <instance x="1120" y="1136" name="XLXI_17" orien="R0" />
        <branch name="romin(3)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1680" y="800" type="branch" />
            <wire x2="3488" y1="352" y2="352" x1="1488" />
            <wire x2="3488" y1="352" y2="1440" x1="3488" />
            <wire x2="3600" y1="1440" y2="1440" x1="3488" />
            <wire x2="1488" y1="352" y2="800" x1="1488" />
            <wire x2="1680" y1="800" y2="800" x1="1488" />
            <wire x2="1872" y1="800" y2="800" x1="1680" />
            <wire x2="3472" y1="1440" y2="1440" x1="3392" />
            <wire x2="3488" y1="1440" y2="1440" x1="3472" />
        </branch>
        <bustap x2="1872" y1="704" y2="704" x1="1968" />
        <bustap x2="1872" y1="752" y2="752" x1="1968" />
        <bustap x2="1872" y1="800" y2="800" x1="1968" />
        <iomarker fontsize="28" x="640" y="1104" name="b0" orien="R180" />
        <instance x="1120" y="1040" name="XLXI_18" orien="R0" />
        <iomarker fontsize="28" x="640" y="1008" name="b1" orien="R180" />
        <branch name="gs">
            <wire x2="1104" y1="1200" y2="1200" x1="640" />
            <wire x2="1120" y1="1200" y2="1200" x1="1104" />
        </branch>
        <instance x="1120" y="1232" name="XLXI_19" orien="R0" />
        <iomarker fontsize="28" x="640" y="1200" name="gs" orien="R180" />
        <bustap x2="1872" y1="1008" y2="1008" x1="1968" />
        <branch name="romin(2)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1608" y="1008" type="branch" />
            <wire x2="1608" y1="1008" y2="1008" x1="1344" />
            <wire x2="1872" y1="1008" y2="1008" x1="1608" />
        </branch>
        <bustap x2="1872" y1="1104" y2="1104" x1="1968" />
        <branch name="romin(1)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1608" y="1104" type="branch" />
            <wire x2="1608" y1="1104" y2="1104" x1="1344" />
            <wire x2="1872" y1="1104" y2="1104" x1="1608" />
        </branch>
        <bustap x2="1872" y1="1200" y2="1200" x1="1968" />
        <branch name="romin(0)">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1608" y="1200" type="branch" />
            <wire x2="1608" y1="1200" y2="1200" x1="1344" />
            <wire x2="1872" y1="1200" y2="1200" x1="1608" />
        </branch>
        <branch name="clkin">
            <wire x2="1744" y1="1520" y2="1520" x1="1584" />
            <wire x2="1744" y1="1520" y2="1568" x1="1744" />
            <wire x2="2832" y1="1568" y2="1568" x1="1744" />
            <wire x2="2864" y1="1568" y2="1568" x1="2832" />
            <wire x2="3008" y1="1568" y2="1568" x1="2864" />
            <wire x2="3008" y1="848" y2="848" x1="2832" />
            <wire x2="2832" y1="848" y2="1568" x1="2832" />
            <wire x2="3008" y1="1216" y2="1216" x1="2864" />
            <wire x2="2864" y1="1216" y2="1568" x1="2864" />
        </branch>
        <iomarker fontsize="28" x="1584" y="1520" name="clkin" orien="R180" />
        <branch name="rst">
            <wire x2="1744" y1="1616" y2="1616" x1="1584" />
            <wire x2="1744" y1="1616" y2="1664" x1="1744" />
            <wire x2="2880" y1="1664" y2="1664" x1="1744" />
            <wire x2="2912" y1="1664" y2="1664" x1="2880" />
            <wire x2="3008" y1="1664" y2="1664" x1="2912" />
            <wire x2="3008" y1="944" y2="944" x1="2880" />
            <wire x2="2880" y1="944" y2="1664" x1="2880" />
            <wire x2="3008" y1="1312" y2="1312" x1="2912" />
            <wire x2="2912" y1="1312" y2="1664" x1="2912" />
        </branch>
        <iomarker fontsize="28" x="1584" y="1616" name="rst" orien="R180" />
    </sheet>
</drawing>