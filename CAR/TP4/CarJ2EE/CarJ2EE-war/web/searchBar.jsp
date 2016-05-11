<%-- 
    Document   : searchBar
    Created on : 7 mai 2014, 14:32:45
    Author     : erwan
--%>
<aside>
    <h2>Search</h2>
    <form method=POST action="BookSearch">
        <table>
            <tr>
                <td>Title :</td>
                <td>
                    <input type=text name="title">
                </td>
            </tr>

            <tr>
                <td>Author: </td>
                <td>
                    <INPUT type=text name="author" >
                </td>
            </tr>
            <tr>
                <td>Date: </td>
                <td>
                    <INPUT type="number" name="date" >
                </td>
            </tr>
            <tr>
                <td COLSPAN=2>
                    <input type="submit" value="Search">
                </td>
            </tr>
        </table>
    </form>
</aside>