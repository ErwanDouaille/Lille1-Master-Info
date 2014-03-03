<?xml version="1.0" encoding="UTF-8"?>
<drawing version="7">
    <attr value="spartan6" name="DeviceFamilyName">
        <trait delete="all:0" />
        <trait editname="all:0" />
        <trait edittrait="all:0" />
    </attr>
    <netlist>
        <signal name="clkin" />
        <signal name="rst" />
        <signal name="bouton1" />
        <signal name="bouton2" />
        <signal name="bouton3" />
        <signal name="Q_del(7:0)" />
        <signal name="clk_dcm1" />
        <signal name="clk2hz" />
        <signal name="clk16hz" />
        <signal name="clk2khz" />
        <signal name="b0" />
        <signal name="b1" />
        <signal name="gs" />
        <signal name="etatsuiv(2:0)" />
        <signal name="enable_del" />
        <signal name="etatpres(2:0)" />
        <signal name="XLXN_9" />
        <signal name="XLXN_10" />
        <signal name="XLXN_11" />
        <signal name="XLXN_12" />
        <signal name="XLXN_13" />
        <signal name="XLXN_14" />
        <signal name="XLXN_15" />
        <signal name="XLXN_17" />
        <signal name="XLXN_18" />
        <signal name="XLXN_19" />
        <signal name="XLXN_21" />
        <signal name="XLXN_22" />
        <signal name="XLXN_24" />
        <signal name="XLXN_26" />
        <signal name="debout1" />
        <signal name="debout2" />
        <signal name="debout3" />
        <port polarity="Input" name="clkin" />
        <port polarity="Input" name="rst" />
        <port polarity="Input" name="bouton1" />
        <port polarity="Input" name="bouton2" />
        <port polarity="Input" name="bouton3" />
        <port polarity="Output" name="Q_del(7:0)" />
        <blockdef name="debounce_sc">
            <timestamp>2013-9-20T9:33:38</timestamp>
            <rect width="256" x="64" y="-128" height="128" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <line x2="384" y1="-96" y2="-96" x1="320" />
        </blockdef>
        <blockdef name="dcm1">
            <timestamp>2013-9-27T10:28:7</timestamp>
            <rect width="544" x="32" y="32" height="1056" />
            <line x2="32" y1="80" y2="80" x1="0" />
            <line x2="32" y1="432" y2="432" x1="0" />
            <line x2="576" y1="80" y2="80" x1="608" />
        </blockdef>
        <blockdef name="diviseur_clk">
            <timestamp>2013-9-20T9:48:43</timestamp>
            <rect width="256" x="64" y="-192" height="192" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
            <line x2="384" y1="-96" y2="-96" x1="320" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
        </blockdef>
        <blockdef name="encodeur_sc">
            <timestamp>2013-9-27T10:36:43</timestamp>
            <rect width="256" x="64" y="-192" height="192" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
            <line x2="384" y1="-96" y2="-96" x1="320" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
        </blockdef>
        <blockdef name="msa_sc">
            <timestamp>2013-9-27T9:23:19</timestamp>
            <rect width="256" x="64" y="-320" height="320" />
            <line x2="0" y1="-288" y2="-288" x1="64" />
            <line x2="0" y1="-224" y2="-224" x1="64" />
            <line x2="0" y1="-160" y2="-160" x1="64" />
            <line x2="0" y1="-96" y2="-96" x1="64" />
            <line x2="0" y1="-32" y2="-32" x1="64" />
            <rect width="64" x="320" y="-300" height="24" />
            <line x2="384" y1="-288" y2="-288" x1="320" />
            <rect width="64" x="320" y="-172" height="24" />
            <line x2="384" y1="-160" y2="-160" x1="320" />
            <line x2="384" y1="-32" y2="-32" x1="320" />
        </blockdef>
        <blockdef name="sr8ce">
            <timestamp>2000-1-1T10:10:10</timestamp>
            <rect width="64" x="320" y="-268" height="24" />
            <line x2="64" y1="-192" y2="-192" x1="0" />
            <line x2="64" y1="-128" y2="-144" x1="80" />
            <line x2="80" y1="-112" y2="-128" x1="64" />
            <line x2="320" y1="-256" y2="-256" x1="384" />
            <line x2="64" y1="-128" y2="-128" x1="0" />
            <line x2="64" y1="-32" y2="-32" x1="192" />
            <line x2="192" y1="-64" y2="-32" x1="192" />
            <line x2="64" y1="-32" y2="-32" x1="0" />
            <line x2="64" y1="-320" y2="-320" x1="0" />
            <rect width="256" x="64" y="-384" height="320" />
        </blockdef>
        <block symbolname="diviseur_clk" name="diviseur_inst">
            <blockpin signalname="clk_dcm1" name="clkin" />
            <blockpin signalname="clk2hz" name="clk2hz" />
            <blockpin signalname="clk16hz" name="clk16hz" />
            <blockpin signalname="clk2khz" name="clk2khz" />
        </block>
        <block symbolname="msa_sc" name="msa_inst">
            <blockpin signalname="b0" name="b0" />
            <blockpin signalname="b1" name="b1" />
            <blockpin signalname="gs" name="gs" />
            <blockpin signalname="clk_dcm1" name="clkin" />
            <blockpin signalname="rst" name="rst" />
            <blockpin signalname="etatpres(2:0)" name="etatpres(2:0)" />
            <blockpin signalname="etatsuiv(2:0)" name="etatsuiv(2:0)" />
            <blockpin signalname="enable_del" name="enable_del" />
        </block>
        <block symbolname="sr8ce" name="shiftreg_inst">
            <blockpin signalname="clk16hz" name="C" />
            <blockpin signalname="enable_del" name="CE" />
            <blockpin signalname="rst" name="CLR" />
            <blockpin signalname="clk2hz" name="SLI" />
            <blockpin signalname="Q_del(7:0)" name="Q(7:0)" />
        </block>
        <block symbolname="debounce_sc" name="debounce3_inst">
            <blockpin signalname="bouton3" name="sig_in" />
            <blockpin signalname="clk2khz" name="clkin" />
            <blockpin signalname="debout3" name="sig_out" />
        </block>
        <block symbolname="debounce_sc" name="debounce2_inst">
            <blockpin signalname="bouton2" name="sig_in" />
            <blockpin signalname="clk2khz" name="clkin" />
            <blockpin signalname="debout2" name="sig_out" />
        </block>
        <block symbolname="debounce_sc" name="debounce1_inst">
            <blockpin signalname="bouton1" name="sig_in" />
            <blockpin signalname="clk2khz" name="clkin" />
            <blockpin signalname="debout1" name="sig_out" />
        </block>
        <block symbolname="encodeur_sc" name="XLXI_3">
            <blockpin signalname="debout3" name="E3" />
            <blockpin signalname="debout2" name="E2" />
            <blockpin signalname="debout1" name="E1" />
            <blockpin signalname="b0" name="O0" />
            <blockpin signalname="b1" name="O1" />
            <blockpin signalname="gs" name="GS" />
        </block>
        <block symbolname="dcm1" name="dcm_inst">
            <blockpin signalname="clkin" name="clk_in1" />
            <blockpin signalname="rst" name="reset" />
            <blockpin signalname="clk_dcm1" name="clk_out1" />
        </block>
    </netlist>
    <sheet sheetnum="1" width="3520" height="2720">
        <branch name="clkin">
            <wire x2="1824" y1="784" y2="784" x1="448" />
            <wire x2="1824" y1="784" y2="800" x1="1824" />
            <wire x2="1904" y1="800" y2="800" x1="1824" />
        </branch>
        <branch name="rst">
            <wire x2="448" y1="1136" y2="1136" x1="432" />
            <wire x2="448" y1="1136" y2="1152" x1="448" />
            <wire x2="1904" y1="1152" y2="1152" x1="448" />
        </branch>
        <iomarker fontsize="28" x="448" y="784" name="clkin" orien="R180" />
        <iomarker fontsize="28" x="432" y="1136" name="rst" orien="R180" />
        <instance x="2688" y="960" name="diviseur_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="0" y="0" type="instance" />
        </instance>
        <instance x="656" y="2448" name="debounce3_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="0" y="0" type="instance" />
        </instance>
        <instance x="656" y="2208" name="debounce2_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="0" y="0" type="instance" />
        </instance>
        <instance x="656" y="1968" name="debounce1_inst" orien="R0">
            <attrtext style="fontsize:28;fontname:Arial" attrname="InstName" x="0" y="0" type="instance" />
        </instance>
        <branch name="bouton1">
            <wire x2="656" y1="1872" y2="1872" x1="464" />
        </branch>
        <branch name="bouton2">
            <wire x2="656" y1="2112" y2="2112" x1="464" />
        </branch>
        <branch name="bouton3">
            <wire x2="656" y1="2352" y2="2352" x1="464" />
        </branch>
        <iomarker fontsize="28" x="464" y="1872" name="bouton1" orien="R180" />
        <iomarker fontsize="28" x="464" y="2112" name="bouton2" orien="R180" />
        <iomarker fontsize="28" x="464" y="2352" name="bouton3" orien="R180" />
        <instance x="2128" y="2704" name="shiftreg_inst" orien="R0" />
        <instance x="2160" y="2240" name="msa_inst" orien="R0">
        </instance>
        <branch name="Q_del(7:0)">
            <wire x2="2704" y1="2448" y2="2448" x1="2512" />
        </branch>
        <iomarker fontsize="28" x="2704" y="2448" name="Q_del(7:0)" orien="R0" />
        <branch name="clk_dcm1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2637" y="800" type="branch" />
            <wire x2="2637" y1="800" y2="800" x1="2512" />
            <wire x2="2688" y1="800" y2="800" x1="2637" />
        </branch>
        <branch name="clk2hz">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="3264" y="800" type="branch" />
            <wire x2="3264" y1="800" y2="800" x1="3072" />
        </branch>
        <branch name="clk16hz">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="3280" y="864" type="branch" />
            <wire x2="3280" y1="864" y2="864" x1="3072" />
        </branch>
        <branch name="clk2khz">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="3280" y="928" type="branch" />
            <wire x2="3280" y1="928" y2="928" x1="3072" />
        </branch>
        <branch name="clk2khz">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="448" y="1936" type="branch" />
            <wire x2="656" y1="1936" y2="1936" x1="448" />
        </branch>
        <branch name="clk2khz">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="448" y="2176" type="branch" />
            <wire x2="656" y1="2176" y2="2176" x1="448" />
        </branch>
        <branch name="clk2khz">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="464" y="2416" type="branch" />
            <wire x2="656" y1="2416" y2="2416" x1="464" />
        </branch>
        <branch name="b0">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2010" y="1952" type="branch" />
            <wire x2="2010" y1="1952" y2="1952" x1="1824" />
            <wire x2="2160" y1="1952" y2="1952" x1="2010" />
        </branch>
        <branch name="b1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2029" y="2016" type="branch" />
            <wire x2="2029" y1="2016" y2="2016" x1="1824" />
            <wire x2="2160" y1="2016" y2="2016" x1="2029" />
        </branch>
        <branch name="gs">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="2031" y="2080" type="branch" />
            <wire x2="2031" y1="2080" y2="2080" x1="1824" />
            <wire x2="2160" y1="2080" y2="2080" x1="2031" />
        </branch>
        <branch name="clk_dcm1">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="2080" y="2144" type="branch" />
            <wire x2="2160" y1="2144" y2="2144" x1="2080" />
        </branch>
        <branch name="rst">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="2080" y="2208" type="branch" />
            <wire x2="2160" y1="2208" y2="2208" x1="2080" />
        </branch>
        <branch name="etatsuiv(2:0)">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="2688" y="2080" type="branch" />
            <wire x2="2688" y1="2080" y2="2080" x1="2544" />
        </branch>
        <branch name="enable_del">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="2688" y="2208" type="branch" />
            <wire x2="2688" y1="2208" y2="2208" x1="2544" />
        </branch>
        <branch name="etatpres(2:0)">
            <attrtext style="alignment:SOFT-LEFT" attrname="Name" x="2640" y="1952" type="branch" />
            <wire x2="2640" y1="1952" y2="1952" x1="2544" />
        </branch>
        <branch name="enable_del">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="2016" y="2512" type="branch" />
            <wire x2="2128" y1="2512" y2="2512" x1="2016" />
        </branch>
        <branch name="clk2hz">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="1952" y="2384" type="branch" />
            <wire x2="2128" y1="2384" y2="2384" x1="1952" />
        </branch>
        <branch name="clk16hz">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="1968" y="2576" type="branch" />
            <wire x2="2128" y1="2576" y2="2576" x1="1968" />
        </branch>
        <branch name="rst">
            <attrtext style="alignment:SOFT-RIGHT" attrname="Name" x="1968" y="2672" type="branch" />
            <wire x2="2128" y1="2672" y2="2672" x1="1968" />
        </branch>
        <instance x="1440" y="2112" name="XLXI_3" orien="R0">
        </instance>
        <branch name="debout1">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1207" y="1872" type="branch" />
            <wire x2="1207" y1="1872" y2="1872" x1="1040" />
            <wire x2="1232" y1="1872" y2="1872" x1="1207" />
            <wire x2="1232" y1="1872" y2="1952" x1="1232" />
            <wire x2="1440" y1="1952" y2="1952" x1="1232" />
        </branch>
        <branch name="debout2">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1248" y="2016" type="branch" />
            <wire x2="1232" y1="2112" y2="2112" x1="1040" />
            <wire x2="1232" y1="2016" y2="2112" x1="1232" />
            <wire x2="1248" y1="2016" y2="2016" x1="1232" />
            <wire x2="1440" y1="2016" y2="2016" x1="1248" />
        </branch>
        <branch name="debout3">
            <attrtext style="alignment:SOFT-BCENTER" attrname="Name" x="1263" y="2080" type="branch" />
            <wire x2="1248" y1="2352" y2="2352" x1="1040" />
            <wire x2="1248" y1="2080" y2="2352" x1="1248" />
            <wire x2="1263" y1="2080" y2="2080" x1="1248" />
            <wire x2="1440" y1="2080" y2="2080" x1="1263" />
        </branch>
        <instance x="1904" y="720" name="dcm_inst" orien="R0">
        </instance>
    </sheet>
</drawing>