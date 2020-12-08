package tools.htmltools;

import models.UserModel;
import tools.UserAuth;

public class HtmlConstants {

    private static final String ROOTPATH = "/roingwebapp";

    public static String getHtmlHeaders(){

        return "<meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\""+ROOTPATH+ "/css/bootstrap.min.css\">\n" +
                "    " +
                "<link rel=\"icon\" type=\"image/x-icon\" href=\""+ROOTPATH+ "/img/Roforbund.png\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/bootstrap-darkmode@0.7.0/dist/darktheme.css\"/>\n" +
                "    <!-- Font Awesome -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.8.2/css/all.css\">\n" +
                "    <!-- MDBootstrap Datatables  -->\n" +
                "    <link href=\""+ROOTPATH+"/css/addons/datatables.min.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>\n" +
                "    <script src=\""+ROOTPATH+"/js/bootstrap.min.js\"></script>\n" +
                "    <script src=\"https://unpkg.com/bootstrap-darkmode@0.7.0/dist/theme.js\"></script>\n" +
                "\n" +
                "    <!-- MDBootstrap Datatables  -->\n" +
                "    <script type=\"text/javascript\" src=\""+ROOTPATH+"/js/addons/datatables.min.js\"></script>\n";
    }

    public static String getHtmlHead(String title){
        return getHtmlHead(title, null);
    }

    public static String getHtmlHead(String title, UserModel currUser){
        StringBuilder html = new StringBuilder();

        if(title.isEmpty()){
            title = "Roing Webapp";
        }

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"no\">\n");
        html.append("<head>\n");
        html.append("    <title>").append(title).append("</title>\n    ");
        html.append( getHtmlHeaders() );
        html.append("</head>\n");
        html.append("<body style=\"text-align: center;\">\n");
        html.append("\n");
        html.append("<div id=\"nav-placeholder\" style=\"min-height: 70px;\"></div>\n");
        html.append(UserAuth.navBarLogin(currUser));
        html.append("<script src=\"").append(ROOTPATH).append("/js/menu.js\"></script>\n");

        return html.toString();
    }
}
