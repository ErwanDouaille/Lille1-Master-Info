/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restGateWay;

/**
 *
 * @author Erwan Douaille & Maxime Douylliez
 */

    

public  class JUnitTestString {
    
    public  JUnitTestString() {
    }
    
   

    /**
     * Return the expected result of getInstance method Test, of class HTMLGenerator.
     */
   
    public static HTMLGenerator testGetInstance() {
       return new HTMLGenerator();
    }

    /**
     * Return the expected result of getRemoveConfirmation method Test, of class HTMLGenerator.
     */
   
    public static String testGetRemoveConfirmation() {
        
    String result=null; return result; }

    /**
     * Return the expected result of getUploadConfirmation method Test, of class HTMLGenerator.
     */
    
    public static String testGetUploadConfirmation() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getUploadContent method Test, of class HTMLGenerator.
     */
 
    public static String testGetUploadContent() {
      
    String result=null; return result; }

    /**
     * Return the expected result of getUploadForm method Test, of class HTMLGenerator.
     */
   
    public static String testGetUploadForm() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getSignInContent method Test, of class HTMLGenerator.
     */
 
    public static String testGetSignInContent() {
      
    String result=null;
    result="<html><meta http-equiv=\"refresh\" content=\"1; URL=file\"><body><h1>Login ...</h1></body></html>";
    return result; }

    /**
     * Return the expected result of getBadConnectionContent method Test, of class HTMLGenerator.
     */
  
    public static String testGetBadConnectionContent() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getDisconnectionContent method Test, of class HTMLGenerator.
     */
  
    public static String testGetDisconnectionContent() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getDisconnectionButton method Test, of class HTMLGenerator.
     */
  
    public static String testGetDisconnectionButton() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getUploadButton method Test, of class HTMLGenerator.
     */
 
    public static String testGetUploadButton() {
        
    String result=null; return result; }

    /**
     * Return the expected result of getSomethingWentWrong method Test, of class HTMLGenerator.
     */
 
    public static String testGetSomethingWentWrong() {
      
    String result=null; return result; }

    /**
     * Return the expected result of getShouldBeImplemented method Test, of class HTMLGenerator.
     */

    public static String testGetShouldBeImplemented() {
      
    String result=null; return result; }

    /**
     * Return the expected result of getFileListWith method Test, of class HTMLGenerator.
     */

    public static String testGetFileListWith() {
       
    String result=null; return result; }

    /**
     * Return the expected result of getCssContent method Test, of class HTMLGenerator.
     */
 
    public static String testGetCssContent() {
       
    String result=null;
    result="\"<style type=\\\"text/css\\\">\\n\" +\n" +
"                \" \\n\" +\n" +
"\"html {\\n\" +\n" +
"\"    background: url('http://2.bp.blogspot.com/_UdH8rcs3s1Q/TGdcZB6sO3I/AAAAAAAAAKU/KrPuD_wmBC4/s320/iPadBackgroundTexture-grey.png');\\n\" +\n" +
"\"    font-size: 10pt;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"label {\\n\" +\n" +
"\"    display: block;\\n\" +\n" +
"\"    color: #fff;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".cf:before,\\n\" +\n" +
"\".cf:after {\\n\" +\n" +
"\"    content: \\\"\\\"; \\n\" +\n" +
"\"    display: table;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\".cf:after {\\n\" +\n" +
"\"    clear: both;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".cf {\\n\" +\n" +
"\"    *zoom: 1;\\n\" +\n" +
"\"}\\n\" +\n" +
"\":focus {\\n\" +\n" +
"\"    outline: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".loginform {\\n\" +\n" +
"\"    width: 410px;\\n\" +\n" +
"\"    margin: 50px auto;\\n\" +\n" +
"\"    padding: 25px;\\n\" +\n" +
"\"    background-color: rgba(250,250,250,0.5);\\n\" +\n" +
"\"    border-radius: 5px;\\n\" +\n" +
"\"    box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.2), \\n\" +\n" +
"\"        inset 0px 1px 0px 0px rgba(250, 250, 250, 0.5);\\n\" +\n" +
"\"    border: 1px solid rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"}\\n\" +\n" +
"\".loginform ul {\\n\" +\n" +
"\"    padding: 0;\\n\" +\n" +
"\"    margin: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".loginform li {\\n\" +\n" +
"\"    display: inline;\\n\" +\n" +
"\"    float: left;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".loginform input:not([type=submit]) {\\n\" +\n" +
"\"    padding: 5px;\\n\" +\n" +
"\"    margin-right: 10px;\\n\" +\n" +
"\"    border: 1px solid rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"    border-radius: 3px;\\n\" +\n" +
"\"    box-shadow: inset 0px 1px 3px 0px rgba(0, 0, 0, 0.1), \\n\" +\n" +
"\"        0px 1px 0px 0px rgba(250, 250, 250, 0.5) ;\\n\" +\n" +
"\"}\\n\" +\n" +
"\".loginform input[type=submit] {\\n\" +\n" +
"\"    border: 1px solid rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"    background: #64c8ef; /* Old browsers */\\n\" +\n" +
"\"    background: -moz-linear-gradient(top,  #64c8ef 0%, #00a2e2 100%); /* FF3.6+ */\\n\" +\n" +
"\"    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#64c8ef), color-stop(100%,#00a2e2)); /* Chrome,Safari4+ */\\n\" +\n" +
"\"    background: -webkit-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Chrome10+,Safari5.1+ */\\n\" +\n" +
"\"    background: -o-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Opera 11.10+ */\\n\" +\n" +
"\"    background: -ms-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* IE10+ */\\n\" +\n" +
"\"    background: linear-gradient(to bottom,  #64c8ef 0%,#00a2e2 100%); /* W3C */\\n\" +\n" +
"\"    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#64c8ef', endColorstr='#00a2e2',GradientType=0 ); /* IE6-9 */\\n\" +\n" +
"\"    color: #fff;\\n\" +\n" +
"\"    padding: 5px 15px;\\n\" +\n" +
"\"    margin-right: 0;\\n\" +\n" +
"\"    margin-top: 15px;\\n\" +\n" +
"\"    border-radius: 3px;\\n\" +\n" +
"\"    text-shadow: 1px 1px 0px rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"\\n\" +\n" +
"\"article,\\n\" +\n" +
"\"aside,\\n\" +\n" +
"\"details,\\n\" +\n" +
"\"figcaption,\\n\" +\n" +
"\"figure,\\n\" +\n" +
"\"footer,\\n\" +\n" +
"\"header,\\n\" +\n" +
"\"hgroup,\\n\" +\n" +
"\"nav,\\n\" +\n" +
"\"section,\\n\" +\n" +
"\"summary {\\n\" +\n" +
"\"    display: block;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Corrects `inline-block` display not defined in IE6/7/8/9 & FF3.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"audio,\\n\" +\n" +
"\"canvas,\\n\" +\n" +
"\"video {\\n\" +\n" +
"\"    display: inline-block;\\n\" +\n" +
"\"    *display: inline;\\n\" +\n" +
"\"    *zoom: 1;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Prevents modern browsers from displaying `audio` without controls.\\n\" +\n" +
"\" * Remove excess height in iOS5 devices.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"audio:not([controls]) {\\n\" +\n" +
"\"    display: none;\\n\" +\n" +
"\"    height: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses styling for `hidden` attribute not present in IE7/8/9, FF3, S4.\\n\" +\n" +
"\" * Known issue: no IE6 support.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"[hidden] {\\n\" +\n" +
"\"    display: none;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Base\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Corrects text resizing oddly in IE6/7 when body `font-size` is set using\\n\" +\n" +
"\" *    `em` units.\\n\" +\n" +
"\" * 2. Prevents iOS text size adjust after orientation change, without disabling\\n\" +\n" +
"\" *    user zoom.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"html {\\n\" +\n" +
"\"    font-size: 100%; /* 1 */\\n\" +\n" +
"\"    -webkit-text-size-adjust: 100%; /* 2 */\\n\" +\n" +
"\"    -ms-text-size-adjust: 100%; /* 2 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses `font-family` inconsistency between `textarea` and other form\\n\" +\n" +
"\" * elements.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"html,\\n\" +\n" +
"\"button,\\n\" +\n" +
"\"input,\\n\" +\n" +
"\"select,\\n\" +\n" +
"\"textarea {\\n\" +\n" +
"\"    font-family: sans-serif;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses margins handled incorrectly in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"body {\\n\" +\n" +
"\"    margin: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Links\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses `outline` inconsistency between Chrome and other browsers.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"a:focus {\\n\" +\n" +
"\"    outline: thin dotted;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Improves readability when focused and also mouse hovered in all browsers.\\n\" +\n" +
"\" * people.opera.com/patrickl/experiments/keyboard/test\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"a:active,\\n\" +\n" +
"\"a:hover {\\n\" +\n" +
"\"    outline: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Typography\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses font sizes and margins set differently in IE6/7.\\n\" +\n" +
"\" * Addresses font sizes within `section` and `article` in FF4+, Chrome, S5.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"h1 {\\n\" +\n" +
"\"    font-size: 2em;\\n\" +\n" +
"\"    margin: 0.67em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"h2 {\\n\" +\n" +
"\"    font-size: 1.5em;\\n\" +\n" +
"\"    margin: 0.83em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"h3 {\\n\" +\n" +
"\"    font-size: 1.17em;\\n\" +\n" +
"\"    margin: 1em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"h4 {\\n\" +\n" +
"\"    font-size: 1em;\\n\" +\n" +
"\"    margin: 1.33em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"h5 {\\n\" +\n" +
"\"    font-size: 0.83em;\\n\" +\n" +
"\"    margin: 1.67em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"h6 {\\n\" +\n" +
"\"    font-size: 0.75em;\\n\" +\n" +
"\"    margin: 2.33em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses styling not present in IE7/8/9, S5, Chrome.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"abbr[title] {\\n\" +\n" +
"\"    border-bottom: 1px dotted;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses style set to `bolder` in FF3+, S4/5, Chrome.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"b,\\n\" +\n" +
"\"strong {\\n\" +\n" +
"\"    font-weight: bold;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"blockquote {\\n\" +\n" +
"\"    margin: 1em 40px;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses styling not present in S5, Chrome.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"dfn {\\n\" +\n" +
"\"    font-style: italic;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses styling not present in IE6/7/8/9.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"mark {\\n\" +\n" +
"\"    background: #ff0;\\n\" +\n" +
"\"    color: #000;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses margins set differently in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"p,\\n\" +\n" +
"\"pre {\\n\" +\n" +
"\"    margin: 1em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Corrects font family set oddly in IE6, S4/5, Chrome.\\n\" +\n" +
"\" * en.wikipedia.org/wiki/User:Davidgothberg/Test59\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"code,\\n\" +\n" +
"\"kbd,\\n\" +\n" +
"\"pre,\\n\" +\n" +
"\"samp {\\n\" +\n" +
"\"    font-family: monospace, serif;\\n\" +\n" +
"\"    _font-family: 'courier new', monospace;\\n\" +\n" +
"\"    font-size: 1em;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Improves readability of pre-formatted text in all browsers.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"pre {\\n\" +\n" +
"\"    white-space: pre;\\n\" +\n" +
"\"    white-space: pre-wrap;\\n\" +\n" +
"\"    word-wrap: break-word;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses CSS quotes not supported in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"q {\\n\" +\n" +
"\"    quotes: none;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses `quotes` property not supported in S4.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"q:before,\\n\" +\n" +
"\"q:after {\\n\" +\n" +
"\"    content: '';\\n\" +\n" +
"\"    content: none;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"small {\\n\" +\n" +
"\"    font-size: 75%;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Prevents `sub` and `sup` affecting `line-height` in all browsers.\\n\" +\n" +
"\" * gist.github.com/413930\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"sub,\\n\" +\n" +
"\"sup {\\n\" +\n" +
"\"    font-size: 75%;\\n\" +\n" +
"\"    line-height: 0;\\n\" +\n" +
"\"    position: relative;\\n\" +\n" +
"\"    vertical-align: baseline;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"sup {\\n\" +\n" +
"\"    top: -0.5em;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"sub {\\n\" +\n" +
"\"    bottom: -0.25em;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Lists\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses margins set differently in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"dl,\\n\" +\n" +
"\"menu,\\n\" +\n" +
"\"ol,\\n\" +\n" +
"\"ul {\\n\" +\n" +
"\"    margin: 1em 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"dd {\\n\" +\n" +
"\"    margin: 0 0 0 40px;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses paddings set differently in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"menu,\\n\" +\n" +
"\"ol,\\n\" +\n" +
"\"ul {\\n\" +\n" +
"\"    padding: 0 0 0 40px;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Corrects list images handled incorrectly in IE7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"nav ul,\\n\" +\n" +
"\"nav ol {\\n\" +\n" +
"\"    list-style: none;\\n\" +\n" +
"\"    list-style-image: none;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Embedded content\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Removes border when inside `a` element in IE6/7/8/9, FF3.\\n\" +\n" +
"\" * 2. Improves image quality when scaled in IE7.\\n\" +\n" +
"\" *    code.flickr.com/blog/2008/11/12/on-ui-quality-the-little-things-client-side-image-resizing/\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"img {\\n\" +\n" +
"\"    border: 0; /* 1 */\\n\" +\n" +
"\"    -ms-interpolation-mode: bicubic; /* 2 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Corrects overflow displayed oddly in IE9.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"svg:not(:root) {\\n\" +\n" +
"\"    overflow: hidden;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Figures\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses margin not present in IE6/7/8/9, S5, O11.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"figure {\\n\" +\n" +
"\"    margin: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Forms\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Corrects margin displayed oddly in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"form {\\n\" +\n" +
"\"    margin: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Define consistent border, margin, and padding.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"fieldset {\\n\" +\n" +
"\"    border: 1px solid #c0c0c0;\\n\" +\n" +
"\"    margin: 0 2px;\\n\" +\n" +
"\"    padding: 0.35em 0.625em 0.75em;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Corrects color not being inherited in IE6/7/8/9.\\n\" +\n" +
"\" * 2. Corrects text not wrapping in FF3.\\n\" +\n" +
"\" * 3. Corrects alignment displayed oddly in IE6/7.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"legend {\\n\" +\n" +
"\"    border: 0; /* 1 */\\n\" +\n" +
"\"    padding: 0;\\n\" +\n" +
"\"    white-space: normal; /* 2 */\\n\" +\n" +
"\"    *margin-left: -7px; /* 3 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Corrects font size not being inherited in all browsers.\\n\" +\n" +
"\" * 2. Addresses margins set differently in IE6/7, FF3+, S5, Chrome.\\n\" +\n" +
"\" * 3. Improves appearance and consistency in all browsers.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"button,\\n\" +\n" +
"\"input,\\n\" +\n" +
"\"select,\\n\" +\n" +
"\"textarea {\\n\" +\n" +
"\"    font-size: 100%; /* 1 */\\n\" +\n" +
"\"    margin: 0; /* 2 */\\n\" +\n" +
"\"    vertical-align: baseline; /* 3 */\\n\" +\n" +
"\"    *vertical-align: middle; /* 3 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Addresses FF3/4 setting `line-height` on `input` using `!important` in the\\n\" +\n" +
"\" * UA stylesheet.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"button,\\n\" +\n" +
"\"input {\\n\" +\n" +
"\"    line-height: normal;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Avoid the WebKit bug in Android 4.0.* where (2) destroys native `audio`\\n\" +\n" +
"\" *    and `video` controls.\\n\" +\n" +
"\" * 2. Corrects inability to style clickable `input` types in iOS.\\n\" +\n" +
"\" * 3. Improves usability and consistency of cursor style between image-type\\n\" +\n" +
"\" *    `input` and others.\\n\" +\n" +
"\" * 4. Removes inner spacing in IE7 without affecting normal text inputs.\\n\" +\n" +
"\" *    Known issue: inner spacing remains in IE6.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"button,\\n\" +\n" +
"\"html input[type=\\\"button\\\"], /* 1 */\\n\" +\n" +
"\"input[type=\\\"reset\\\"],\\n\" +\n" +
"\"input[type=\\\"submit\\\"] {\\n\" +\n" +
"\"    -webkit-appearance: button; /* 2 */\\n\" +\n" +
"\"    cursor: pointer; /* 3 */\\n\" +\n" +
"\"    *overflow: visible;  /* 4 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Re-set default cursor for disabled elements.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"button[disabled],\\n\" +\n" +
"\"input[disabled] {\\n\" +\n" +
"\"    cursor: default;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Addresses box sizing set to content-box in IE8/9.\\n\" +\n" +
"\" * 2. Removes excess padding in IE8/9.\\n\" +\n" +
"\" * 3. Removes excess padding in IE7.\\n\" +\n" +
"\" *    Known issue: excess padding remains in IE6.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"input[type=\\\"checkbox\\\"],\\n\" +\n" +
"\"input[type=\\\"radio\\\"] {\\n\" +\n" +
"\"    box-sizing: border-box; /* 1 */\\n\" +\n" +
"\"    padding: 0; /* 2 */\\n\" +\n" +
"\"    *height: 13px; /* 3 */\\n\" +\n" +
"\"    *width: 13px; /* 3 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Addresses `appearance` set to `searchfield` in S5, Chrome.\\n\" +\n" +
"\" * 2. Addresses `box-sizing` set to `border-box` in S5, Chrome (include `-moz`\\n\" +\n" +
"\" *    to future-proof).\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"input[type=\\\"search\\\"] {\\n\" +\n" +
"\"    -webkit-appearance: textfield; /* 1 */\\n\" +\n" +
"\"    -moz-box-sizing: content-box;\\n\" +\n" +
"\"    -webkit-box-sizing: content-box; /* 2 */\\n\" +\n" +
"\"    box-sizing: content-box;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Removes inner padding and search cancel button in S5, Chrome on OS X.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"input[type=\\\"search\\\"]::-webkit-search-cancel-button,\\n\" +\n" +
"\"input[type=\\\"search\\\"]::-webkit-search-decoration {\\n\" +\n" +
"\"    -webkit-appearance: none;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Removes inner padding and border in FF3+.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"button::-moz-focus-inner,\\n\" +\n" +
"\"input::-moz-focus-inner {\\n\" +\n" +
"\"    border: 0;\\n\" +\n" +
"\"    padding: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * 1. Removes default vertical scrollbar in IE6/7/8/9.\\n\" +\n" +
"\" * 2. Improves readability and alignment in all browsers.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"textarea {\\n\" +\n" +
"\"    overflow: auto; /* 1 */\\n\" +\n" +
"\"    vertical-align: top; /* 2 */\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"/* ==========================================================================\\n\" +\n" +
"\"   Tables\\n\" +\n" +
"\"   ========================================================================== */\\n\" +\n" +
"\"\\n\" +\n" +
"\"/*\\n\" +\n" +
"\" * Remove most spacing between table cells.\\n\" +\n" +
"\" */\\n\" +\n" +
"\"\\n\" +\n" +
"\"table {\\n\" +\n" +
"\"    border-collapse: collapse;\\n\" +\n" +
"\"    border-spacing: 0;\\n\" +\n" +
"\"}\\n\" +\n" +
"\"\\n\" +\n" +
"\"\\n\" +\n" +
"\"display {\\n\" +\n" +
"\"    border: 1px solid rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"    background: #64c8ef; /* Old browsers */\\n\" +\n" +
"\"    background: -moz-linear-gradient(top,  #64c8ef 0%, #00a2e2 100%); /* FF3.6+ */\\n\" +\n" +
"\"    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#64c8ef), color-stop(100%,#00a2e2)); /* Chrome,Safari4+ */\\n\" +\n" +
"\"    background: -webkit-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Chrome10+,Safari5.1+ */\\n\" +\n" +
"\"    background: -o-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Opera 11.10+ */\\n\" +\n" +
"\"    background: -ms-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* IE10+ */\\n\" +\n" +
"\"    background: linear-gradient(to bottom,  #64c8ef 0%,#00a2e2 100%); /* W3C */\\n\" +\n" +
"\"    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#64c8ef', endColorstr='#00a2e2',GradientType=0 ); /* IE6-9 */\\n\" +\n" +
"\"    color: #fff;\\n\" +\n" +
"\"    padding: 5px 15px;\\n\" +\n" +
"\"    margin-right: 0;\\n\" +\n" +
"\"    margin-top: 15px;\\n\" +\n" +
"\"    border-radius: 3px;\\n\" +\n" +
"\"    text-shadow: 1px 1px 0px rgba(0, 0, 0, 0.3);\\n\" +\n" +
"\"}\" +\n" +
"                \"#corps\\n\" +\n" +
"\"{	margin:10px auto 10px auto;\\n\" +\n" +
"\"	-khtml-border-radius: 10px;\\n\" +\n" +
"	\"-webkit-border-radius: 10px;\\n\" +\n" +
"\"	-moz-border-radius: 10px;\\n\" +\n" +
"	\"border-radius: 10px;\\n\" +\n" +
"	\"margin-left:auto;\\n\" +\n" +
"	\"margin-bottom:auto;\\n\" +\n" +
"	\"padding:5px;\\n\" +\n" +
"\n" +
"	\"color:#B3B3B3;\\n\" +\n" +
"	\"background-color:#FFFFFF;\\n\" +\n" +
"	\"background-repeat:repeat-x;\\n\" +\n" +
"	\"border:2px solid black;\\n\" +\n" +
"\"}\\n\" +\n" +
"                \n" +
"                \"  </style>\";";
    return result; }
    
}


