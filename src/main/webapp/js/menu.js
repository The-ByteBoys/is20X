const themeConfig = new ThemeConfig();
themeConfig.initTheme();

$(function(){
    $("#nav-placeholder").load("/roingwebapp/nav.html", function(){
        $(".custom-control-input")
            .prop("checked", themeConfig.getTheme() == "dark")
            .on("change", function(){
                if($(this).prop('checked')){
                    themeConfig.setTheme("dark");
                }
                else {
                    themeConfig.setTheme("light");
                }
                updateToTheme();
            });
        updateToTheme();

        // Set login-button content
        if($("#loginInfo").length > 0){
            $("#navLogin").html( $("#loginInfo").html() );
        }

        // Set active page
        let path = window.location.pathname;
        if(path === "/roingwebapp/index.jsp"){ path = "/roingwebapp/"}
        $(".nav-item.nav-link").each(function (){
            if(path === $(this).attr("href")){
                $(this)
                    .addClass("active")
                    .append(" <span class=\"sr-only\">(nåværende)</span>");
            }
        });
    });
});

function updateToTheme(){
    if(themeConfig.getTheme() == "dark"){
        $("table").addClass("table-dark");
    }
    else {
        $("table").removeClass("table-dark");
    }
}
