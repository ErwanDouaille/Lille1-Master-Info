<%-- 
    Document   : index
    Created on : 5 mai 2014, 13:12:07
    Author     : erwan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<jsp:include page="/InitDB" />
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    }
%>
<jsp:include page="header.jsp" />
<section>

    <jsp:include page="searchBar.jsp" />
    <article>
        <p>Hello,</p> 

Kita perlu skor yang baik untuk projek ini. Ini kerja amali kami telah meletakkan masa dan sukar. Kami suka bahan aplikasi reka bentuk diedarkan tetapi kita tidak mempunyai skor akhir hebat di bawah pemeriksaan. 

Jika tidak minggu lalu saya melihat takhta besi dan Geoffrey meninggal dunia. Binomial saya adalah menyeronokkan menaja semua orang. 

Jika tidak, ia adalah baik untuk mempunyai Smalltalk kerja amali dengan tepi laut. Masalahnya ialah keperluan untuk menggunakan Jawa EJB.</p>
    </article>
    <jsp:include page="cartView.jsp" />
</section>
<jsp:include page="foot.jsp" />