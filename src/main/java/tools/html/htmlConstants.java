package tools.html;

public class htmlConstants {

    public static String getHtmlHead(String title){
        StringBuilder html = new StringBuilder();
        if(title.isEmpty()){
            title = "Roing Webapp";
        }

        html.append("<!DOCTYPE html>\n" +
                "<html lang=\"no\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>"+title+"</title>\n" +
                "\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/bootstrap-darkmode@0.7.0/dist/darktheme.css\"/>\n" +
                "    <!-- Font Awesome -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.8.2/css/all.css\">\n" +
                "    <!-- MDBootstrap Datatables  -->\n" +
                "    <link href=\"css/addons/datatables.min.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>\n" +
                "    <script src=\"js/bootstrap.min.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/bootstrap-darkmode@0.7.0/dist/theme.js\"></script>\n" +
                "\n" +
                "    <!-- MDBootstrap Datatables  -->\n" +
                "    <script type=\"text/javascript\" src=\"js/addons/datatables.min.js\"></script>\n" +
                "</head>\n" +
                "<body style=\"text-align: center;\">\n" +
                "\n" +
                "<div id=\"nav-placeholder\"></div>\n" +
                "<script src=\"js/menu.js\"></script>\n");

        return html.toString();
    }
}
