<%@ page import="tools.html.htmlConstants" contentType="text/html;charset=UTF-8" language="java" %>
<%=htmlConstants.getHtmlHead("Upload File")%>
<div class="container-fluid" style="text-align: left;">
    <div style='text-align: center; margin-top: 15vh;'>
        <form method="POST" enctype="multipart/form-data" action="massinsert">
            <h3><b>Upload an .xlsx file to start parsing</b></h3>
            <p><label>.xlsx to upload: <input type="file" name="upfile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" /></label></p>
            <p><label><input type="submit" value="Press"> to upload the file!</label></p>
        </form>
    </div>
</div>

</body>
</html>
