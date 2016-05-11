/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restGateWay;

import org.apache.commons.net.ftp.FTPFile;

/**
 * HTMLGenerator is responsible to generate HTML content
 * HTMLGenerator is a singleton
 * 
 * @author DOUAILLE ERWAN & DOUYLLIEZ MAXIME
 */
public class HTMLGenerator {

    public String path = "http://localhost:8080/RestGateway/";
    
    private static class SingletonHolder {
        private final static HTMLGenerator instance = new HTMLGenerator();
    }

    public static HTMLGenerator getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * ************************************************** DELETE PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    /**
     * Return HTML content with delete confirmation of file param file
     * @param dir current working directory
     * @param file deleted file
     * @return String containing HTML content for remove file confirmation
     */
    String getRemoveConfirmation(String dir, String file) {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "resources/file/"+ dir + "\"><body><h1>" + file + " has been removed</h1></body></html>";
    }
    
    /**
     * 
     * @param dir current working directory
     * @param file undeleted file
     * @param reason error message
     * @return String containing HTML content for remove file failure
     */
    String getRemoveFailure(String dir, String file, String reason ) {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "resources/file/"+ dir + "\"><body><h1>" + file + " has not been removed " + reason + "</h1></body></html>";
    }

    /**
     * ************************************************** UPLOAD PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    
    /**
     * Return Upload confirmation HTML content
     * @param dir current working directory
     * @param file uploaded file
     * @return String containing HTML content for upload confirmation
     */
    public String getUploadConfirmation(String dir, String file) {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "resources/file/"+ dir + "\"><body><h1>" + file + " has been uploaded</h1></body></html>";
    }   
    
    /**
     * Return HTML content including upload form
     * @param cwd current working directory
     * @return String containing HTML content, including upload form
     */
    public String getUploadContent(String cwd){
        return "<html>" + this.getCssContent() + "<body><h1>Upload<div id=\"corps\">" + this.getUploadForm(cwd) + "</div>" + this.getDisconnectionButton() + "</body></html>";
        
    }
    
    /**
     * Return Upload Form
     * @param cwd current working directory
     * @return String containing HTML updload form
     */
    public String getUploadForm(String cwd){
        return  "<section class=\"loginform cf\"><form enctype=\"multipart/form-data\" action=\"" + path + "resources/file/" + cwd + "\" method=\"post\">" +
            "Envoyez ce fichier : <input name=\"file\" type=\"file\" /><br />" +
            "<input type=\"submit\" value=\"Envoyer le fichier\" />" +
            "</form>\n" +
"    </section>";
    }
    

    /**
     * ************************************************** CONNECTION PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */    
    
    /**
     * Return a HTML content age containing login process
     * @return String containing HTML content login process
     */
    public String getSignInContent() {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=file\"><body><h1>Login ...</h1></body></html>";
    }
    
    /**
     * Return a HTML content page containing the default behavior when
     * authentification failed
     *
     * @return String containing HTML content for authentification failure error message
     */
    public String getBadConnectionContent() {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "\"><body><h1>Authentification failure</h1></body></html>";
    }

    /**
     * Return a HTML content page containing the default behavior for client
     * disconnection
     *
     * @return String containing HTML content for diconnection
     */
    public String getDisconnectionContent() {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "\"><body><h1>Disconnected</h1></body></html>";
    }

    /**
     * ************************************************** BUTTON PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */

    /**
     * Return a button for disconnection method
     *
     * @return String containing HTML link, href
     */
    public String getDisconnectionButton() {
        return "<a class=\"btn btn-primary btn-large\" href=\"" + path + "resources/disconnect\">Logout </a>" ;//"<a href=\"" + path + "resources/disconnect\">Logout</a>";
    }

    /**
     * Return a button for upload method
     *
     * @return String containing HTML link, href
     */
    public String getUploadButton(String cwd) {
        return "<a href=\"" + path + "resources/store"+ cwd + "\">Upload</a>";
    }
    
    

    /**
     * ************************************************** ERROR, CATCH PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */

    /**
     * Return a default HTML content page when something went wrong, such as an
     * error exception or a non expected behavior
     *
     * @return String containing HTML content for error message, in case of something went wrong
     */
    public String getSomethingWentWrong(String errorCode) {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "\"><body><h1>Something went wrong... "+errorCode+"</h1></body></html>";
    }

    /**
     * Return a default HTML content page when something went wrong, such as an
     * error exception or a non expected behavior
     *
     * @return String containing HTML content for not implemented methods
     */
    public String getShouldBeImplemented() {
        return "<html><meta http-equiv=\"refresh\" content=\"1; URL=" + path + "\"><body><h1>Should be implemented</h1></body></html>";
    }
    
    

    /**
     * ************************************************** LIST PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */

    /**
     * Return a default HTML content page parameters has to been provide by the
     * rest gateway
     *
     * @param cwd current working directory
     * @param fileList list of Files
     * @return String containing HTML content, listing all files and directory
     */
    public String getFileListWith(String cwd, FTPFile[] fileList) {
        return "<html>" + this.getCssContent() + "<body><h1>" + cwd + "</h1><div id=\"corps\">" + this.processFileList(cwd, fileList) + "</div>" + this.getDisconnectionButton() + this.getUploadButton(cwd) + "</body></html>";
    }

    /**
     * This method has to parse all files and provide default HTML source code
     * displaying files needed in body.
     *
     * @param fileList list of Files
     * @return String containing HTML part containing href link for download files and browse directory
     */
    private String processFileList(String cwd, FTPFile[] fileList) {
        String tmp = "\n";
        tmp += this.getLinkForParentDirectory(cwd);
        for (FTPFile fTPFile : fileList) {
            tmp += this.getLinkForFileName(cwd, fTPFile);
        }
        return tmp + "\n";
    }

    /**
     * This method return HTML source code than provide user to browse files, or retrive one
     *
     * @param file list of Files
     * @return String containing HTML part containing href link 
     */
    private String getLinkForFileName(String cwd, FTPFile file) {
        String tmp = "";
        if (file.isDirectory()) 
            tmp += "<img src=\"http://agingparentsauthority.com/wp-content/plugins/sem-theme-pro/icons/folder.png\" alt=\"[   ]\" />       <a href='" + path + "resources/file" + cwd + "/" + file.getName() + "'>" + file.getName() + "</a></br>\n";
        if (file.isFile()) {
            tmp += "<img src=\"http://www.appropedia.org/skins/vector/images/file-icon.png\" alt=\"[   ]\" /> "+ 
                    "<a href=\"" + path + "/resources/file/" + cwd + "/delete/" + file.getName() + "\"><img src=\"http://www.domainedesnoms.com/themes/site/ddn.fr/images/picto/delete.png\" /></a> " +
                    "<a href=\"" + path + "/resources/file/" + cwd + "/download/" + file.getName() + "\">" + file.getName() + "</a></br>";
        }
        return tmp;
    }

    /**
     * This method return HTML source code than provide cdup ability to the user
     *
     * @return String containing HTML link for cdup dir
     */
    private String getLinkForParentDirectory(String cwd) {
        String[] split = cwd.split("/");
        String parent = "";
        for (int i = 0; i < split.length-1; i++) {
            parent += split[i] + "/";            
        }
        return "<a href='" + path + "/resources/file/" + parent +"'>Parent directory</a></br>\n";
    }
    
    
    

    /**
     * ************************************************** CSS PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */

    /**
     * Return default CSS content for HTML pages
     * @return  String containing CSS content
     */
    public String getCssContent(){
        return "<style type=\"text/css\">\n" +
                " \n" +
"html {\n" +
"    background: url('http://2.bp.blogspot.com/_UdH8rcs3s1Q/TGdcZB6sO3I/AAAAAAAAAKU/KrPuD_wmBC4/s320/iPadBackgroundTexture-grey.png');\n" +
"    font-size: 10pt;\n" +
"}\n" +
"label {\n" +
"    display: block;\n" +
"    color: #fff;\n" +
"}\n" +
".cf:before,\n" +
".cf:after {\n" +
"    content: \"\"; \n" +
"    display: table;\n" +
"}\n" +
"\n" +
".cf:after {\n" +
"    clear: both;\n" +
"}\n" +
".cf {\n" +
"    *zoom: 1;\n" +
"}\n" +
":focus {\n" +
"    outline: 0;\n" +
"}\n" +
".loginform {\n" +
"    width: 410px;\n" +
"    margin: 50px auto;\n" +
"    padding: 25px;\n" +
"    background-color: rgba(250,250,250,0.5);\n" +
"    border-radius: 5px;\n" +
"    box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.2), \n" +
"        inset 0px 1px 0px 0px rgba(250, 250, 250, 0.5);\n" +
"    border: 1px solid rgba(0, 0, 0, 0.3);\n" +
"}\n" +
".loginform ul {\n" +
"    padding: 0;\n" +
"    margin: 0;\n" +
"}\n" +
".loginform li {\n" +
"    display: inline;\n" +
"    float: left;\n" +
"}\n" +
".loginform input:not([type=submit]) {\n" +
"    padding: 5px;\n" +
"    margin-right: 10px;\n" +
"    border: 1px solid rgba(0, 0, 0, 0.3);\n" +
"    border-radius: 3px;\n" +
"    box-shadow: inset 0px 1px 3px 0px rgba(0, 0, 0, 0.1), \n" +
"        0px 1px 0px 0px rgba(250, 250, 250, 0.5) ;\n" +
"}\n" +
".loginform input[type=submit] {\n" +
"    border: 1px solid rgba(0, 0, 0, 0.3);\n" +
"    background: #64c8ef; /* Old browsers */\n" +
"    background: -moz-linear-gradient(top,  #64c8ef 0%, #00a2e2 100%); /* FF3.6+ */\n" +
"    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#64c8ef), color-stop(100%,#00a2e2)); /* Chrome,Safari4+ */\n" +
"    background: -webkit-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Chrome10+,Safari5.1+ */\n" +
"    background: -o-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Opera 11.10+ */\n" +
"    background: -ms-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* IE10+ */\n" +
"    background: linear-gradient(to bottom,  #64c8ef 0%,#00a2e2 100%); /* W3C */\n" +
"    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#64c8ef', endColorstr='#00a2e2',GradientType=0 ); /* IE6-9 */\n" +
"    color: #fff;\n" +
"    padding: 5px 15px;\n" +
"    margin-right: 0;\n" +
"    margin-top: 15px;\n" +
"    border-radius: 3px;\n" +
"    text-shadow: 1px 1px 0px rgba(0, 0, 0, 0.3);\n" +
"}\n" +
"\n" +
"\n" +
"article,\n" +
"aside,\n" +
"details,\n" +
"figcaption,\n" +
"figure,\n" +
"footer,\n" +
"header,\n" +
"hgroup,\n" +
"nav,\n" +
"section,\n" +
"summary {\n" +
"    display: block;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Corrects `inline-block` display not defined in IE6/7/8/9 & FF3.\n" +
" */\n" +
"\n" +
"audio,\n" +
"canvas,\n" +
"video {\n" +
"    display: inline-block;\n" +
"    *display: inline;\n" +
"    *zoom: 1;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Prevents modern browsers from displaying `audio` without controls.\n" +
" * Remove excess height in iOS5 devices.\n" +
" */\n" +
"\n" +
"audio:not([controls]) {\n" +
"    display: none;\n" +
"    height: 0;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses styling for `hidden` attribute not present in IE7/8/9, FF3, S4.\n" +
" * Known issue: no IE6 support.\n" +
" */\n" +
"\n" +
"[hidden] {\n" +
"    display: none;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Base\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * 1. Corrects text resizing oddly in IE6/7 when body `font-size` is set using\n" +
" *    `em` units.\n" +
" * 2. Prevents iOS text size adjust after orientation change, without disabling\n" +
" *    user zoom.\n" +
" */\n" +
"\n" +
"html {\n" +
"    font-size: 100%; /* 1 */\n" +
"    -webkit-text-size-adjust: 100%; /* 2 */\n" +
"    -ms-text-size-adjust: 100%; /* 2 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses `font-family` inconsistency between `textarea` and other form\n" +
" * elements.\n" +
" */\n" +
"\n" +
"html,\n" +
"button,\n" +
"input,\n" +
"select,\n" +
"textarea {\n" +
"    font-family: sans-serif;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses margins handled incorrectly in IE6/7.\n" +
" */\n" +
"\n" +
"body {\n" +
"    margin: 0;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Links\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Addresses `outline` inconsistency between Chrome and other browsers.\n" +
" */\n" +
"\n" +
"a:focus {\n" +
"    outline: thin dotted;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Improves readability when focused and also mouse hovered in all browsers.\n" +
" * people.opera.com/patrickl/experiments/keyboard/test\n" +
" */\n" +
"\n" +
"a:active,\n" +
"a:hover {\n" +
"    outline: 0;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Typography\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Addresses font sizes and margins set differently in IE6/7.\n" +
" * Addresses font sizes within `section` and `article` in FF4+, Chrome, S5.\n" +
" */\n" +
"\n" +
"h1 {\n" +
"    font-size: 2em;\n" +
"    margin: 0.67em 0;\n" +
"}\n" +
"\n" +
"h2 {\n" +
"    font-size: 1.5em;\n" +
"    margin: 0.83em 0;\n" +
"}\n" +
"\n" +
"h3 {\n" +
"    font-size: 1.17em;\n" +
"    margin: 1em 0;\n" +
"}\n" +
"\n" +
"h4 {\n" +
"    font-size: 1em;\n" +
"    margin: 1.33em 0;\n" +
"}\n" +
"\n" +
"h5 {\n" +
"    font-size: 0.83em;\n" +
"    margin: 1.67em 0;\n" +
"}\n" +
"\n" +
"h6 {\n" +
"    font-size: 0.75em;\n" +
"    margin: 2.33em 0;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses styling not present in IE7/8/9, S5, Chrome.\n" +
" */\n" +
"\n" +
"abbr[title] {\n" +
"    border-bottom: 1px dotted;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses style set to `bolder` in FF3+, S4/5, Chrome.\n" +
" */\n" +
"\n" +
"b,\n" +
"strong {\n" +
"    font-weight: bold;\n" +
"}\n" +
"\n" +
"blockquote {\n" +
"    margin: 1em 40px;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses styling not present in S5, Chrome.\n" +
" */\n" +
"\n" +
"dfn {\n" +
"    font-style: italic;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses styling not present in IE6/7/8/9.\n" +
" */\n" +
"\n" +
"mark {\n" +
"    background: #ff0;\n" +
"    color: #000;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses margins set differently in IE6/7.\n" +
" */\n" +
"\n" +
"p,\n" +
"pre {\n" +
"    margin: 1em 0;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Corrects font family set oddly in IE6, S4/5, Chrome.\n" +
" * en.wikipedia.org/wiki/User:Davidgothberg/Test59\n" +
" */\n" +
"\n" +
"code,\n" +
"kbd,\n" +
"pre,\n" +
"samp {\n" +
"    font-family: monospace, serif;\n" +
"    _font-family: 'courier new', monospace;\n" +
"    font-size: 1em;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Improves readability of pre-formatted text in all browsers.\n" +
" */\n" +
"\n" +
"pre {\n" +
"    white-space: pre;\n" +
"    white-space: pre-wrap;\n" +
"    word-wrap: break-word;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses CSS quotes not supported in IE6/7.\n" +
" */\n" +
"\n" +
"q {\n" +
"    quotes: none;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses `quotes` property not supported in S4.\n" +
" */\n" +
"\n" +
"q:before,\n" +
"q:after {\n" +
"    content: '';\n" +
"    content: none;\n" +
"}\n" +
"\n" +
"small {\n" +
"    font-size: 75%;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Prevents `sub` and `sup` affecting `line-height` in all browsers.\n" +
" * gist.github.com/413930\n" +
" */\n" +
"\n" +
"sub,\n" +
"sup {\n" +
"    font-size: 75%;\n" +
"    line-height: 0;\n" +
"    position: relative;\n" +
"    vertical-align: baseline;\n" +
"}\n" +
"\n" +
"sup {\n" +
"    top: -0.5em;\n" +
"}\n" +
"\n" +
"sub {\n" +
"    bottom: -0.25em;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Lists\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Addresses margins set differently in IE6/7.\n" +
" */\n" +
"\n" +
"dl,\n" +
"menu,\n" +
"ol,\n" +
"ul {\n" +
"    margin: 1em 0;\n" +
"}\n" +
"\n" +
"dd {\n" +
"    margin: 0 0 0 40px;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses paddings set differently in IE6/7.\n" +
" */\n" +
"\n" +
"menu,\n" +
"ol,\n" +
"ul {\n" +
"    padding: 0 0 0 40px;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Corrects list images handled incorrectly in IE7.\n" +
" */\n" +
"\n" +
"nav ul,\n" +
"nav ol {\n" +
"    list-style: none;\n" +
"    list-style-image: none;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Embedded content\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * 1. Removes border when inside `a` element in IE6/7/8/9, FF3.\n" +
" * 2. Improves image quality when scaled in IE7.\n" +
" *    code.flickr.com/blog/2008/11/12/on-ui-quality-the-little-things-client-side-image-resizing/\n" +
" */\n" +
"\n" +
"img {\n" +
"    border: 0; /* 1 */\n" +
"    -ms-interpolation-mode: bicubic; /* 2 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * Corrects overflow displayed oddly in IE9.\n" +
" */\n" +
"\n" +
"svg:not(:root) {\n" +
"    overflow: hidden;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Figures\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Addresses margin not present in IE6/7/8/9, S5, O11.\n" +
" */\n" +
"\n" +
"figure {\n" +
"    margin: 0;\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Forms\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Corrects margin displayed oddly in IE6/7.\n" +
" */\n" +
"\n" +
"form {\n" +
"    margin: 0;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Define consistent border, margin, and padding.\n" +
" */\n" +
"\n" +
"fieldset {\n" +
"    border: 1px solid #c0c0c0;\n" +
"    margin: 0 2px;\n" +
"    padding: 0.35em 0.625em 0.75em;\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Corrects color not being inherited in IE6/7/8/9.\n" +
" * 2. Corrects text not wrapping in FF3.\n" +
" * 3. Corrects alignment displayed oddly in IE6/7.\n" +
" */\n" +
"\n" +
"legend {\n" +
"    border: 0; /* 1 */\n" +
"    padding: 0;\n" +
"    white-space: normal; /* 2 */\n" +
"    *margin-left: -7px; /* 3 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Corrects font size not being inherited in all browsers.\n" +
" * 2. Addresses margins set differently in IE6/7, FF3+, S5, Chrome.\n" +
" * 3. Improves appearance and consistency in all browsers.\n" +
" */\n" +
"\n" +
"button,\n" +
"input,\n" +
"select,\n" +
"textarea {\n" +
"    font-size: 100%; /* 1 */\n" +
"    margin: 0; /* 2 */\n" +
"    vertical-align: baseline; /* 3 */\n" +
"    *vertical-align: middle; /* 3 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * Addresses FF3/4 setting `line-height` on `input` using `!important` in the\n" +
" * UA stylesheet.\n" +
" */\n" +
"\n" +
"button,\n" +
"input {\n" +
"    line-height: normal;\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Avoid the WebKit bug in Android 4.0.* where (2) destroys native `audio`\n" +
" *    and `video` controls.\n" +
" * 2. Corrects inability to style clickable `input` types in iOS.\n" +
" * 3. Improves usability and consistency of cursor style between image-type\n" +
" *    `input` and others.\n" +
" * 4. Removes inner spacing in IE7 without affecting normal text inputs.\n" +
" *    Known issue: inner spacing remains in IE6.\n" +
" */\n" +
"\n" +
"button,\n" +
"html input[type=\"button\"], /* 1 */\n" +
"input[type=\"reset\"],\n" +
"input[type=\"submit\"] {\n" +
"    -webkit-appearance: button; /* 2 */\n" +
"    cursor: pointer; /* 3 */\n" +
"    *overflow: visible;  /* 4 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * Re-set default cursor for disabled elements.\n" +
" */\n" +
"\n" +
"button[disabled],\n" +
"input[disabled] {\n" +
"    cursor: default;\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Addresses box sizing set to content-box in IE8/9.\n" +
" * 2. Removes excess padding in IE8/9.\n" +
" * 3. Removes excess padding in IE7.\n" +
" *    Known issue: excess padding remains in IE6.\n" +
" */\n" +
"\n" +
"input[type=\"checkbox\"],\n" +
"input[type=\"radio\"] {\n" +
"    box-sizing: border-box; /* 1 */\n" +
"    padding: 0; /* 2 */\n" +
"    *height: 13px; /* 3 */\n" +
"    *width: 13px; /* 3 */\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Addresses `appearance` set to `searchfield` in S5, Chrome.\n" +
" * 2. Addresses `box-sizing` set to `border-box` in S5, Chrome (include `-moz`\n" +
" *    to future-proof).\n" +
" */\n" +
"\n" +
"input[type=\"search\"] {\n" +
"    -webkit-appearance: textfield; /* 1 */\n" +
"    -moz-box-sizing: content-box;\n" +
"    -webkit-box-sizing: content-box; /* 2 */\n" +
"    box-sizing: content-box;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Removes inner padding and search cancel button in S5, Chrome on OS X.\n" +
" */\n" +
"\n" +
"input[type=\"search\"]::-webkit-search-cancel-button,\n" +
"input[type=\"search\"]::-webkit-search-decoration {\n" +
"    -webkit-appearance: none;\n" +
"}\n" +
"\n" +
"/*\n" +
" * Removes inner padding and border in FF3+.\n" +
" */\n" +
"\n" +
"button::-moz-focus-inner,\n" +
"input::-moz-focus-inner {\n" +
"    border: 0;\n" +
"    padding: 0;\n" +
"}\n" +
"\n" +
"/*\n" +
" * 1. Removes default vertical scrollbar in IE6/7/8/9.\n" +
" * 2. Improves readability and alignment in all browsers.\n" +
" */\n" +
"\n" +
"textarea {\n" +
"    overflow: auto; /* 1 */\n" +
"    vertical-align: top; /* 2 */\n" +
"}\n" +
"\n" +
"/* ==========================================================================\n" +
"   Tables\n" +
"   ========================================================================== */\n" +
"\n" +
"/*\n" +
" * Remove most spacing between table cells.\n" +
" */\n" +
"\n" +
"table {\n" +
"    border-collapse: collapse;\n" +
"    border-spacing: 0;\n" +
"}\n" +
"\n" +
"\n" +
"display {\n" +
"    border: 1px solid rgba(0, 0, 0, 0.3);\n" +
"    background: #64c8ef; /* Old browsers */\n" +
"    background: -moz-linear-gradient(top,  #64c8ef 0%, #00a2e2 100%); /* FF3.6+ */\n" +
"    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#64c8ef), color-stop(100%,#00a2e2)); /* Chrome,Safari4+ */\n" +
"    background: -webkit-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Chrome10+,Safari5.1+ */\n" +
"    background: -o-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* Opera 11.10+ */\n" +
"    background: -ms-linear-gradient(top,  #64c8ef 0%,#00a2e2 100%); /* IE10+ */\n" +
"    background: linear-gradient(to bottom,  #64c8ef 0%,#00a2e2 100%); /* W3C */\n" +
"    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#64c8ef', endColorstr='#00a2e2',GradientType=0 ); /* IE6-9 */\n" +
"    color: #fff;\n" +
"    padding: 5px 15px;\n" +
"    margin-right: 0;\n" +
"    margin-top: 15px;\n" +
"    border-radius: 3px;\n" +
"    text-shadow: 1px 1px 0px rgba(0, 0, 0, 0.3);\n" +
"}" +
                "#corps\n" +
"{	margin:10px auto 10px auto;\n" +
"	-khtml-border-radius: 10px;\n" +
	"-webkit-border-radius: 10px;\n" +
"	-moz-border-radius: 10px;\n" +
	"border-radius: 10px;\n" +
	"margin-left:auto;\n" +
	"margin-bottom:auto;\n" +
	"padding:5px;\n" +

	"color:#B3B3B3;\n" +
	"background-color:#FFFFFF;\n" +
	"background-repeat:repeat-x;\n" +
	"border:2px solid black;\n" +
"}\n" +
                
                "  </style>";
    }
}
